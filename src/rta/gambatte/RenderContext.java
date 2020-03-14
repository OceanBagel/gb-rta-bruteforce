package rta.gambatte;

public class RenderContext extends Bitmap {

	public RenderContext(int width, int height) {
		super(width, height);
	}
	
	public void drawBuffer(int buffer[]) {
		for(int i = 0; i < buffer.length; i++) {
			int pixel = buffer[i];
			byte r = (byte)((pixel >> 0) & 0xFF);
			byte g = (byte)((pixel >> 8) & 0xFF);
			byte b = (byte)((pixel >> 16) & 0xFF);
			setComponent(i * 4 + 1, r);
			setComponent(i * 4 + 2, g);
			setComponent(i * 4 + 3, b);
		}
	}
}
