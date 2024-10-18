package rta.gambatte;

import java.util.Arrays;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

public class Gb {

	public static final int BG_PALETTE = 0;
	public static final int SP1_PALETTE = 1;
	public static final int SP2_PALETTE = 2;
	public static final int LOADRES_BAD_FILE_OR_UNKNOWN_MBC = -0x7FFF;
	public static final int LOADRES_IO_ERROR = -0x1FE;
	public static final int LOADRES_UNSUPPORTED_MBC_HUC3 = -0x1FE;
	public static final int LOADRES_UNSUPPORTED_MBC_TAMA5 = -0x122;
	public static final int LOADRES_UNSUPPORTED_MBC_POCKET_CAMERA = -0x122;
	public static final int LOADRES_UNSUPPORTED_MBC_MBC7 = -0x122;
	public static final int LOADRES_UNSUPPORTED_MBC_MBC6 = -0x120;
	public static final int LOADRES_UNSUPPORTED_MBC_MBC4 = -0x117;
	public static final int LOADRES_UNSUPPORTED_MBC_MMM01 = -0x10D;
	public static final int LOADRES_OK = 0;

	public static final int VIDEO_BUFFER_WIDTH = 160;
	public static final int VIDEO_BUFFER_HEIGHT = 144;
	public static final int VIDEO_BUFFER_SIZE = VIDEO_BUFFER_WIDTH * VIDEO_BUFFER_HEIGHT;
	public static final int AUDIO_BUFFER_SIZE = (35112 + 2064) * 2;
	public static final int NUM_SAMPLES_PER_FRAME = 35112;
	public static final int ADDRESS_BUFFER_SIZE = 32;

	private Libgambatte lib;
	private Pointer gbHandle;

	private Display display;
	private Memory videoBuffer;
	private Memory audioBuffer;
	private Memory samples;
	private int frameOverflow;
	private long cycleCount;
	private int saveStateSize;
	private int currentJoypad;
	private Memory addressBuffer;
	private Memory saveStateBuffer;
	private IJoypadCallback inputCallback;
	
	public boolean showDisplay = false;

	public Gb() {
		lib = Libgambatte.INSTANCE;
		gbHandle = lib.gambatte_create();
		frameOverflow = 0;
		cycleCount = 0;
		currentJoypad = 0;
		addressBuffer = new Memory(ADDRESS_BUFFER_SIZE);
		videoBuffer = new Memory(VIDEO_BUFFER_SIZE * 4);
		audioBuffer = new Memory(AUDIO_BUFFER_SIZE * 4);
		samples = new Memory(4);
		inputCallback = () -> currentJoypad;
		lib.gambatte_setinputgetter(gbHandle, inputCallback, null);
	}

	public void createDisplay(int scale) {
		if(display != null) {
			return;
		}
		display = new Display(VIDEO_BUFFER_WIDTH, VIDEO_BUFFER_HEIGHT, scale, "");
	}

	public void loadRom(String rom, int flags) {
		int result = lib.gambatte_load(gbHandle, rom, flags);
		if(result != LOADRES_OK) {
			throw new RuntimeException("Unable to load rom");
		}
	}

	public void loadBios(String bios) {
		int result = lib.gambatte_loadbios(gbHandle, bios, 0, 0);
		if(result < 0) {
			throw new RuntimeException("Unable to load bios");
		}
	}
	
	public void injectRtcOffset(int rtcoffset) {
		byte state[] = saveState();
		GSRUtils.writeRTC(state, rtcoffset);
		loadState(state);
	}

	public int readMemory(int address) {
		return lib.gambatte_cpuread(gbHandle, address);
	}

	public int writeMemory(int address, int value) {
		return lib.gambatte_cpuwrite(gbHandle, address, value);
	}

	public void injectRBInput(int input) {
		writeMemory(0xFFF8, input);
	}

	public void injectCrysMenuInput(int input) {
		writeMemory(0xFFA4, input);
	}

	public void injectCrysInput(int input) {
		writeMemory(0xFFA7, input);
		writeMemory(0xFFA8, input);
	}

	public void injectGSInput(int input) {
		writeMemory(0xFFA9, input);
		writeMemory(0xFFAA, input);
	}

	public void injectGSMenuInput(int input) {
		writeMemory(0xFFA6, input);
	}

	public void injectYellowInput(int input) {
		writeMemory(0xFFF5, input);
	}

	public void advanceFrame() {
		advanceFrame(0);
	}

	public int advanceFrame(int joypad) {
        if(showDisplay) {
			try {
	        	Thread.sleep(17);
	        }catch(InterruptedException e) {};
        }
		currentJoypad = joypad;
		samples.setInt(0, NUM_SAMPLES_PER_FRAME - frameOverflow);
		int ret = lib.gambatte_runfor(gbHandle, videoBuffer, VIDEO_BUFFER_WIDTH, audioBuffer, samples);
		if(display != null) {
			display.getRenderContext().drawBuffer(videoBuffer.getIntArray(0, VIDEO_BUFFER_SIZE));
			display.swapBuffers();
		}
		int hitAddress = lib.gambatte_gethitinterruptaddress(gbHandle);
		int cyclesPassed = samples.getInt(0);
		cycleCount += cyclesPassed;
		if(ret >= 0 || frameOverflow >= NUM_SAMPLES_PER_FRAME) {
			frameOverflow = 0;
		}
		currentJoypad = 0;
		return hitAddress;
	}

	public int advanceToAddress(int... addresses) {
		return advanceWithJoypadToAddress(0, addresses);
	}

	public int advanceWithJoypadToAddress(int joypad, int... addresses) {
		Arrays.sort(addresses);
		addressBuffer.clear();
		addressBuffer.write(0, addresses, 0, addresses.length);
		lib.gambatte_setinterruptaddresses(gbHandle, addressBuffer, addresses.length);
		int hitAddress;
		do {
			hitAddress = advanceFrame(joypad);
			
		} while(Arrays.binarySearch(addresses, hitAddress) < 0);
		currentJoypad = 0;
		lib.gambatte_setinterruptaddresses(gbHandle, addressBuffer, 0);
		return hitAddress;
	}

	public byte[] saveState() {
		if(saveStateSize == 0) {
			saveStateSize = lib.gambatte_savestate(gbHandle, null, VIDEO_BUFFER_WIDTH, null);
			saveStateBuffer = new Memory(saveStateSize);
		}
		lib.gambatte_savestate(gbHandle, null, VIDEO_BUFFER_WIDTH, saveStateBuffer);
		saveStateBuffer.setLong(0x5300, cycleCount);
		return saveStateBuffer.getByteArray(0, saveStateSize);
	}

	public void loadState(byte buffer[]) {
		if(saveStateSize != buffer.length) {
			saveStateSize = buffer.length;
			saveStateBuffer = new Memory(buffer.length);
		}
		saveStateBuffer.write(0, buffer, 0, buffer.length);
		boolean success = lib.gambatte_loadstate(gbHandle, saveStateBuffer, saveStateSize);
		cycleCount = saveStateBuffer.getLong(0x5300);
		if(!success) {
			throw new RuntimeException("Unable to load save state!");
		}
	}

	public long getCycleCount() {
		return cycleCount;
	}

	public double getGbpTime() {
		double cc = (double) getCycleCount();
		return ((cc / 2097152.0) + 2.135);
	}

	public int getFrameCount() {
		double cc = getCycleCount();
		int frameCount = (int) ((cc / 2097152.0) * 59.7275);
		return frameCount;
	}

	public int getCurrentJoypad() {
		return currentJoypad;
	}

	public int getRdiv() {
		return readMemory(0xFF04);
	}
	
}
