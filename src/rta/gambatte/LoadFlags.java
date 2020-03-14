package rta.gambatte;

public class LoadFlags {

	public static final int CGB_MODE = 1;
	public static final int GBA_FLAG = 2;
	public static final int MULTICART_COMPAT = 4;
	public static final int SGB_MODE = 8;
	public static final int READONLY_SAV = 16;
	public static final int DEFAULT_LOAD_FLAGS = CGB_MODE | GBA_FLAG | READONLY_SAV;
}