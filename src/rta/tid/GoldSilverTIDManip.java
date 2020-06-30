package rta.tid;

import rta.GoldSilverAddr;

import rta.gambatte.Gb;
import rta.gambatte.LoadFlags;

import java.io.*;
import java.util.*;
import java.io.File;
import java.io.IOException;

public class GoldSilverTIDManip {
	private static final int NO_INPUT = 0x00;
	private static final int A = 0x01;
	private static final int START = 0x08;
	private static final int LEFT = 0x20;
	private static final int DOWN = 0x80;
	
	private static final String gameName;

	private static final int GOLD_BASE_COST = 332 + 32;
	private static final int SILVER_BASE_COST = 332 + 32 + 2;
	
	private static final int GOLD_TITLE_COST = 55;
	private static final int SILVER_TITLE_COST = 55 + 2;
	
	private static int BASE_COST;
	private static int titleCost;
	
    static
{
    /* Change this to "gold" or "silver" before running */
    gameName = "silver";
    
	if(gameName.equals("gold"))
	{
		BASE_COST = GOLD_BASE_COST;
		titleCost = GOLD_TITLE_COST;
	}
	else {
		BASE_COST = SILVER_BASE_COST;
    		titleCost = SILVER_TITLE_COST;
	}
}

    // Change this to increase/decrease number of intro sequence combinations processed
    private static final int MAX_COST = BASE_COST + 211 + 100; // TODO: tune these numbers?

    private static Strat gfSkip0 =
	new Strat("_gfskip0", 0 + 0,
	new Integer[] {GoldSilverAddr.readJoypadAddr},
	new Integer[] {START},
	new Integer[] {1});
	
	private static Strat gfSkip1 =
	new Strat("_gfskip1", 0 + 1,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {1, 1});
	
	private static Strat gfSkip2 =
	new Strat("_gfskip2", 0 + 2,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {2, 1});
	
	private static Strat gfSkip3 =
	new Strat("_gfskip3", 0 + 3,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {3, 1});
	
	private static Strat gfSkip4 =
	new Strat("_gfskip4", 0 + 4,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {4, 1});

	private static Strat gfSkip5 =
	new Strat("_gfskip5", 0 + 5,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {5, 1});
	
	private static Strat gfSkip6 =
	new Strat("_gfskip6", 0 + 6,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {6, 1});
	
	private static Strat gfSkip7 =
	new Strat("_gfskip7", 0 + 7,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {7, 1});
	
	private static Strat gfSkip8 =
	new Strat("_gfskip8", 0 + 8,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {8, 1});
	
	private static Strat gfSkip9 =
	new Strat("_gfskip9", 0 + 9,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {9, 1});
	
	private static Strat gfSkip10 =
	new Strat("_gfskip10", 0 + 10,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {10, 1});
	
	private static Strat gfSkip11 =
	new Strat("_gfskip11", 0 + 11,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {11, 1});
	
	private static Strat gfSkip12 =
	new Strat("_gfskip12", 0 + 12,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {12, 1});
	
	private static Strat gfSkip13 =
	new Strat("_gfskip13", 0 + 13,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {13, 1});
	
	private static Strat gfSkip14 =
	new Strat("_gfskip14", 0 + 14,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {14, 1});
	
	private static Strat gfSkip15 =
	new Strat("_gfskip15", 0 + 15,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {15, 1});
	
	private static Strat gfSkip16 =
	new Strat("_gfskip16", 0 + 16,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {16, 1});
	
	private static Strat gfSkip17 =
	new Strat("_gfskip17", 0 + 17,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {17, 1});
	
	private static Strat gfSkip18 =
	new Strat("_gfskip18", 0 + 18,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {18, 1});
	
	private static Strat gfSkip19 =
	new Strat("_gfskip19", 0 + 19,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {19, 1});
	
	private static Strat gfSkip20 =
	new Strat("_gfskip20", 0 + 20,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {20, 1});
	
	private static Strat gfSkip21 =
	new Strat("_gfskip21", 0 + 21,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {21, 1});
	
	private static Strat gfSkip22 =
	new Strat("_gfskip22", 0 + 22,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {22, 1});
	
	private static Strat gfSkip23 =
	new Strat("_gfskip23", 0 + 23,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {22, 1});
	
	private static Strat gfSkip24 =
	new Strat("_gfskip24", 0 + 24,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {24, 1});
	
	private static Strat gfSkip25 =
	new Strat("_gfskip25", 0 + 25,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {25, 1});
	
	private static Strat gfSkip26 =
	new Strat("_gfskip26", 0 + 26,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {26, 1});
	
	private static Strat gfSkip27 =
	new Strat("_gfskip27", 0 + 27,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {27, 1});
	
	private static Strat gfSkip28 =
	new Strat("_gfskip28", 0 + 28,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {28, 1});
	
	private static Strat gfSkip29 =
	new Strat("_gfskip29", 0 + 29,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {29, 1});
	
	private static Strat gfSkip30 =
	new Strat("_gfskip30", 0 + 30,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {30, 1});
	
	private static Strat gfSkip31 =
	new Strat("_gfskip31", 0 + 31,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {31, 1});
	
	private static Strat gfSkip32 =
	new Strat("_gfskip32", 0 + 32,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {32, 1});
	
	private static Strat gfSkip33 =
	new Strat("_gfskip33", 0 + 33,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {33, 1});
	
	private static Strat gfSkip34 =
	new Strat("_gfskip34", 0 + 34,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {34, 1});
	
	private static Strat gfSkip35 =
	new Strat("_gfskip35", 0 + 35,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {35, 1});
	
	private static Strat gfSkip36 =
	new Strat("_gfskip36", 0 + 36,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {36, 1});
	
	private static Strat gfSkip37 =
	new Strat("_gfskip37", 0 + 37,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {37, 1});
	
	private static Strat gfSkip38 =
	new Strat("_gfskip38", 0 + 38,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {38, 1});
	
	private static Strat gfSkip39 =
	new Strat("_gfskip39", 0 + 39,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {39, 1});
	
	private static Strat gfSkip40 =
	new Strat("_gfskip40", 0 + 40,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {40, 1});
	
	private static Strat gfSkip41 =
	new Strat("_gfskip41", 0 + 41,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {41, 1});
	
	private static Strat gfSkip42 =
	new Strat("_gfskip42", 0 + 42,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {42, 1});
	
	private static Strat gfSkip43 =
	new Strat("_gfskip43", 0 + 43,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {43, 1});
	
	private static Strat gfSkip44 =
	new Strat("_gfskip44", 0 + 44,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {44, 1});
	
	private static Strat gfSkip45 =
	new Strat("_gfskip45", 0 + 45,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {45, 1});
	
	private static Strat gfSkip46 =
	new Strat("_gfskip46", 0 + 46,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {46, 1});
	
	private static Strat gfSkip47 =
	new Strat("_gfskip47", 0 + 47,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {47, 1});
	
	private static Strat gfSkip48 =
	new Strat("_gfskip48", 0 + 48,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {48, 1});
	
	private static Strat gfSkip49 =
	new Strat("_gfskip49", 0 + 49,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {49, 1});
	
	private static Strat gfSkip50 =
	new Strat("_gfskip50", 0 + 50,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {50, 1});
	
	private static Strat gfSkip51 =
	new Strat("_gfskip51", 0 + 51,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {51, 1});
	
	private static Strat gfSkip52 =
	new Strat("_gfskip52", 0 + 52,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {52, 1});
	
	private static Strat gfSkip53 =
	new Strat("_gfskip53", 0 + 53,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {53, 1});
	
	private static Strat gfSkip54 =
	new Strat("_gfskip54", 0 + 54,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {54, 1});
	
	private static Strat gfSkip55 =
	new Strat("_gfskip55", 0 + 55,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {55, 1});
	
	private static Strat gfSkip56 =
	new Strat("_gfskip56", 0 + 56,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {56, 1});
	
	private static Strat gfSkip57 =
	new Strat("_gfskip57", 0 + 57,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {57, 1});
	
	private static Strat gfSkip58 =
	new Strat("_gfskip58", 0 + 58,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {58, 1});
	
	private static Strat gfSkip59 =
	new Strat("_gfskip59", 0 + 59,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {59, 1});
	
	private static Strat gfSkip60 =
	new Strat("_gfskip60", 0 + 60,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {60, 1});
	
	private static Strat gfSkip61 =
	new Strat("_gfskip61", 0 + 61,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {61, 1});
	
	private static Strat gfSkip62 =
	new Strat("_gfskip62", 0 + 62,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {62, 1});
	
	private static Strat gfSkip63 =
	new Strat("_gfskip63", 0 + 63,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {63, 1});
	
	private static Strat gfSkip64 =
	new Strat("_gfskip64", 0 + 64,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {64, 1});
	
	private static Strat gfSkip65 =
	new Strat("_gfskip65", 0 + 65,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {65, 1});
	
	private static Strat gfSkip66 =
	new Strat("_gfskip66", 0 + 66,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {66, 1});
	
	private static Strat gfSkip67 =
	new Strat("_gfskip67", 0 + 67,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {67, 1});
	
	private static Strat gfSkip68 =
	new Strat("_gfskip68", 0 + 68,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {68, 1});
	
	private static Strat gfSkip69 =
	new Strat("_gfskip69", 0 + 69,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {69, 1});
	
	private static Strat gfSkip70 =
	new Strat("_gfskip70", 0 + 70,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {70, 1});
	
	private static Strat gfSkip71 =
	new Strat("_gfskip71", 0 + 71,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {71, 1});
	
	private static Strat gfSkip72 =
	new Strat("_gfskip72", 0 + 72,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {72, 1});
	
	private static Strat gfSkip73 =
	new Strat("_gfskip73", 0 + 73,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {73, 1});
	
	private static Strat gfSkip74 =
	new Strat("_gfskip74", 0 + 74,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {74, 1});
	
	private static Strat gfSkip75 =
	new Strat("_gfskip75", 0 + 75,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {75, 1});
	
	private static Strat gfSkip76 =
	new Strat("_gfskip76", 0 + 76,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {76, 1});
	
	private static Strat gfSkip77 =
	new Strat("_gfskip77", 0 + 77,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {77, 1});
	
	private static Strat gfSkip78 =
	new Strat("_gfskip78", 0 + 78,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {78, 1});
	
	private static Strat gfSkip79 =
	new Strat("_gfskip79", 0 + 79,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {69, 1});
	
	private static Strat gfSkip80 =
	new Strat("_gfskip80", 0 + 80,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {80, 1});
	
	private static Strat gfSkip81 =
	new Strat("_gfskip81", 0 + 81,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {81, 1});
	
	private static Strat gfSkip82 =
	new Strat("_gfskip82", 0 + 82,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {82, 1});
	
	private static Strat gfSkip83 =
	new Strat("_gfskip83", 0 + 83,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {83, 1});
	
	private static Strat gfSkip84 =
	new Strat("_gfskip84", 0 + 84,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {84, 1});
	
	private static Strat gfSkip85 =
	new Strat("_gfskip85", 0 + 85,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {85, 1});
	
	private static Strat gfSkip86 =
	new Strat("_gfskip86", 0 + 86,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {86, 1});
	
	private static Strat gfSkip87 =
	new Strat("_gfskip87", 0 + 87,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {87, 1});
	
	private static Strat gfSkip88 =
	new Strat("_gfskip88", 0 + 88,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {88, 1});
	
	private static Strat gfSkip89 =
	new Strat("_gfskip89", 0 + 89,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {69, 1});
	
	private static Strat gfSkip90 =
	new Strat("_gfskip90", 0 + 90,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {90, 1});
	
	private static Strat gfSkip91 =
	new Strat("_gfskip91", 0 + 91,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {91, 1});
	
	private static Strat gfSkip92 =
	new Strat("_gfskip92", 0 + 92,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {92, 1});
	
	private static Strat gfSkip93 =
	new Strat("_gfskip93", 0 + 93,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {93, 1});
	
	private static Strat gfSkip94 =
	new Strat("_gfskip94", 0 + 94,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {94, 1});
	
	private static Strat gfSkip95 =
	new Strat("_gfskip95", 0 + 95,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {95, 1});
	
	private static Strat gfSkip96 =
	new Strat("_gfskip96", 0 + 96,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {96, 1});
	
	private static Strat gfSkip97 =
	new Strat("_gfskip97", 0 + 97,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {97, 1});
	
	private static Strat gfSkip98 =
	new Strat("_gfskip98", 0 + 98,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {98, 1});
	
	private static Strat gfSkip99 =
	new Strat("_gfskip99", 0 + 99,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {99, 1});
	
	private static Strat gfSkip100 =
	new Strat("_gfskip100", 0 + 100,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {100, 1});

    private static Strat titleSkip0 =
	new Strat("_title0", titleCost + 0,
	new Integer[] {GoldSilverAddr.readJoypadAddr},
	new Integer[] {START},
	new Integer[] {1});
	
	private static Strat titleSkip1 =
	new Strat("_title1", titleCost + 1,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {1, 1});
	
	private static Strat titleSkip2 =
	new Strat("_title2", titleCost + 2,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {2, 1});

	private static Strat titleSkip3 =
	new Strat("_title3", titleCost + 3,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {3, 1});
	
    private static Strat newGame =
	new Strat("_newgame", 8,
	new Integer[] {GoldSilverAddr.readJoypadAddr, GoldSilverAddr.postTIDAddr},
	new Integer[] {A, NO_INPUT},
	new Integer[] {1, 0});

    private static List<Strat> intro = Arrays.asList(
	gfSkip0, gfSkip1, gfSkip2, gfSkip3, gfSkip4, gfSkip5, gfSkip6, gfSkip7, gfSkip8, gfSkip9, gfSkip10,
	gfSkip11, gfSkip12, gfSkip13, gfSkip14, gfSkip15, gfSkip16, gfSkip17, gfSkip18, gfSkip19, gfSkip20,
	gfSkip21, gfSkip22, gfSkip23, gfSkip24, gfSkip25, gfSkip26, gfSkip27, gfSkip28, gfSkip29, gfSkip30,
	gfSkip31, gfSkip32, gfSkip33, gfSkip34, gfSkip35, gfSkip36, gfSkip37, gfSkip38, gfSkip39, gfSkip40,
	gfSkip41, gfSkip42, gfSkip43, gfSkip44, gfSkip45, gfSkip46, gfSkip47, gfSkip48, gfSkip49, gfSkip50,
	gfSkip51, gfSkip52, gfSkip53, gfSkip54, gfSkip55, gfSkip56, gfSkip57, gfSkip58, gfSkip59, gfSkip60,
	gfSkip61, gfSkip62, gfSkip63, gfSkip64, gfSkip65, gfSkip66, gfSkip67, gfSkip68, gfSkip69, gfSkip70,
	gfSkip71, gfSkip72, gfSkip73, gfSkip74, gfSkip75, gfSkip76, gfSkip77, gfSkip78, gfSkip79, gfSkip80,
	gfSkip81, gfSkip82, gfSkip83, gfSkip84, gfSkip85, gfSkip86, gfSkip87, gfSkip88, gfSkip89, gfSkip90,
	gfSkip91, gfSkip92, gfSkip93, gfSkip94, gfSkip95, gfSkip96, gfSkip97, gfSkip98, gfSkip99, gfSkip100);

	// TODO: don't add each frame of delay manually?

    static class Strat {
        String name;
        int cost;
        Integer[] addr;
        Integer[] input;
        Integer[] advanceFrames;
        Strat(String name, int cost, Integer[] addr, Integer[] input, Integer[] advanceFrames) {
            this.addr = addr;
            this.cost = cost;
            this.name = name;
            this.input = input;
            this.advanceFrames = advanceFrames;
        }
        public void execute(Gb gb) {
            for(int i=0; i<addr.length; i++) {
                gb.advanceWithJoypadToAddress(input[i], addr[i]);
                for(int j=0; j<advanceFrames[i]; j++) {
                    gb.advanceFrame(input[i]);
                }
            }
        }
    }

    static class IntroSequence extends ArrayList<Strat> implements Comparable<IntroSequence> {
        IntroSequence(Strat... strats) {
            super(Arrays.asList(strats));
        }
        IntroSequence(IntroSequence other) {
            super(other);
        }
        @Override public String toString() {
            String ret = gameName;
            for(int i=0; i<this.size(); i++) {
                Strat s = this.get(i);
                ret += s.name;
            }
            return ret;
        }
        void execute(Gb gb) {
            for(Strat s : this) {
                s.execute(gb);
            }
        }
        int cost() {
            return this.stream().mapToInt((Strat s) -> s.cost).sum();
        }
        @Override public int compareTo(IntroSequence o) {
            return this.cost() - o.cost();
        }
    }

    private static IntroSequence append(IntroSequence seq, Strat... strats) {
        IntroSequence newSeq = new IntroSequence(seq);
        newSeq.addAll(Arrays.asList(strats));
        return newSeq;
    }

//  private static void addWaitPermutations(ArrayList<IntroSequence> introSequences, IntroSequence introSequence) {
//      int ngmax = (MAX_COST - (introSequence.cost() + BASE_COST + 8));
//      for(int i=0; ngmax>=0 && i<=ngmax/72; i++) {
//          introSequences.add(append(introSequence, newGame));
//      }
//  }

    private static void addSetOptPermutations(ArrayList<IntroSequence> introSequences, IntroSequence introSequence) {
            introSequences.add(append(introSequence, newGame));
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if (!new File("roms").exists()) {
            new File("roms").mkdir();
            System.err.println("I need ROMs to simulate!");
            System.exit(0);
        }

        File file = new File(gameName + "_tids.txt");
        PrintWriter writer = new PrintWriter(file);
        
        int maxwaits = (MAX_COST - BASE_COST - titleCost - 8)/4;

        ArrayList<Strat> setopt0Strats = new ArrayList<>();
        for(int i=1; i<=maxwaits; i++) {
            Integer[] addr = new Integer[i+2];
            Integer[] input = new Integer[i+2];
            Integer[] advFrames = new Integer[i+2];
            addr[0] = GoldSilverAddr.readJoypadAddr;
            input[0] = DOWN;
            advFrames[0] = 1;
            for(int j=1; j<i; j++) {
                addr[j] = GoldSilverAddr.mainMenuJoypadAddr;
                input[j] = NO_INPUT;
                advFrames[j] = 1;
            }
            addr[i] = GoldSilverAddr.readJoypadAddr;
            input[i] = LEFT | A;
            advFrames[i] = 1;
            addr[i+1] = GoldSilverAddr.readJoypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            setopt0Strats.add(new Strat("_wait" + i + "(setopt0)", i*4 + 70 + 1, addr, input, advFrames));
        }
        
	ArrayList<Strat> setopt1Strats = new ArrayList<>();
        for(int i=1; i<=maxwaits; i++) {
            Integer[] addr = new Integer[i+3];
            Integer[] input = new Integer[i+3];
            Integer[] advFrames = new Integer[i+3];
            addr[0] = GoldSilverAddr.readJoypadAddr;
            input[0] = DOWN;
            advFrames[0] = 1;
            for(int j=1; j<i; j++) {
                addr[j] = GoldSilverAddr.mainMenuJoypadAddr;
                input[j] = NO_INPUT;
                advFrames[j] = 1;
            }
            addr[i] = GoldSilverAddr.readJoypadAddr;
            input[i] = LEFT | A;
            advFrames[i] = 1;
            addr[i+1] = GoldSilverAddr.readJoypadAddr;
            input[i+1] = NO_INPUT;
            advFrames[i+1] = 1;
            addr[i+2] = GoldSilverAddr.readJoypadAddr;
            input[i+2] = START;
            advFrames[i+2] = 1;
            setopt1Strats.add(new Strat("_wait" + i + "(setopt1)", i*4 + 70 + 1 + 3, addr, input, advFrames));
        }

        ArrayList<IntroSequence> introSequences = new ArrayList<>();
        for(Strat s : intro) {
			
            IntroSequence introSequence0 = new IntroSequence(s, titleSkip0);
            IntroSequence introSequence1 = new IntroSequence(s, titleSkip1);
            IntroSequence introSequence2 = new IntroSequence(s, titleSkip2);
            IntroSequence introSequence3 = new IntroSequence(s, titleSkip3);
            
			// 0 frames of delay
			
                for(Strat s2 : setopt0Strats) {
                    IntroSequence base = append(introSequence0, s2);
                    addSetOptPermutations(introSequences, base);
                }
		for(Strat s3 : setopt1Strats) {
                    IntroSequence base = append(introSequence0, s3);
                    addSetOptPermutations(introSequences, base);
                }	
			
			// 1 frame of delay
			
                for(Strat s2 : setopt0Strats) {
                    IntroSequence base = append(introSequence1, s2);
                    addSetOptPermutations(introSequences, base);
                }
		for(Strat s3 : setopt1Strats) {
                    IntroSequence base = append(introSequence1, s3);
                    addSetOptPermutations(introSequences, base);
                }

			// 2 frames of delay
			
                for(Strat s2 : setopt0Strats) {
                    IntroSequence base = append(introSequence2, s2);
                    addSetOptPermutations(introSequences, base);
                }
		for(Strat s3 : setopt1Strats) {
                    IntroSequence base = append(introSequence2, s3);
                    addSetOptPermutations(introSequences, base);
                }
			
			// 3 frames of delay
			
                for(Strat s2 : setopt0Strats) {
                    IntroSequence base = append(introSequence3, s2);
                    addSetOptPermutations(introSequences, base);
                }
		for(Strat s3 : setopt1Strats) {
                    IntroSequence base = append(introSequence3, s3);
                    addSetOptPermutations(introSequences, base);
                }
        }

        System.out.println("Number of intro sequences: " + introSequences.size());
        Collections.sort(introSequences);

        // Init gambatte with 1 session
	// TODO: Parallelism?
        Gb gb = new Gb();
        gb.loadBios("roms/gbc_bios.bin");
        gb.loadRom("roms/poke" + gameName + ".gbc", LoadFlags.DEFAULT_LOAD_FLAGS);
        byte[] Bios = gb.saveState();
        for(IntroSequence seq : introSequences) {
            seq.execute(gb);
            int tid = readTID(gb);
            writer.println(
                    seq.toString()
                            + ": TID = " + String.format("0x%4s", Integer.toHexString(tid).toUpperCase()).replace(' ', '0') + " (" + String.format("%5s)", tid).replace(' ', '0')
                            + ", Cost: " + String.format("%.02f", (gb.getGbpTime() - 0.21))); // TODO: tune cost towards TAS timing?
            gb.loadState(Bios);
            writer.flush();
            System.out.printf("Current Cost: %d%n", seq.cost());
        }
        writer.close();
    }

    private static int readTID(Gb gb) {
        return (gb.readMemory(0xD1A1) << 8) | gb.readMemory(0xD1A2);
    }
}
