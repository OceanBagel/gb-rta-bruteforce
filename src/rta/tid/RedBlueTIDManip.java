package rta.tid;

import rta.RedBlueAddr;

import rta.gambatte.Gb;
import rta.gambatte.LoadFlags;

import java.io.*;
import java.util.*;
import java.io.File;
import java.io.IOException;

public class RedBlueTIDManip {
    private static final int NO_INPUT = 0x00;
    private static final int A = 0x01;
    private static final int B = 0x02;
    private static final int SELECT = 0x04;
    private static final int START = 0x08;
    private static final int UP = 0x40;
    private static final int DOWN = 0x80;
    

    /* Change this to "blue" or "red" before running */
    private static final String gameName = "red";
    
//    private static final int BASE_COST = 492;
//    private static final int HOP_BASE_COST = 172;
//    private static final int NG_WINDOW_COST = 20;
//    private static final int START_NG_COST = 20;
//    private static final int TAS_DELAY_COST = 10;
    
	private static int TITLE_BASE_COST = (gameName.equals("blue") ? 0 : 1);
	private static int CRY_BASE_COST = (gameName.equals("blue") ? 96 : 88);
    
    /* Change this to increase/decrease number of intro sequence combinations processed */
	private static final int MAX_COST = 793 + 15;
	
//    private static final int MAX_COST = BASE_COST + HOP_BASE_COST + TITLE_BASE_COST + CRY_BASE_COST + NG_WINDOW_COST + START_NG_COST + TAS_DELAY_COST;
	
	private static PalStrat nopal =
		new PalStrat("_nopal", 0,
		new Integer[] {RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT},
		new Integer[] {1});
		
	private static PalStrat abss =
		new PalStrat("_nopal(ab)", 0,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {A, A},
		new Integer[] {0, 0});

	private static PalStrat pal0 =
		new PalStrat("_pal0", 0,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {UP},
		new Integer[] {1});
	
	private static PalStrat pal1 =
		new PalStrat("_pal1", 2,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP},
		new Integer[] {74, 1});
	
	private static PalStrat pal2 =
		new PalStrat("_pal2", 4,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP},
		new Integer[] {76, 1});
	
	private static PalStrat pal2funky =
		new PalStrat("_pal2funky", 4,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP, DOWN},
		new Integer[] {74, 1, 1});
	
	private static PalStrat pal3 =
		new PalStrat("_pal3", 6,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP},
		new Integer[] {78, 1});
	
	private static PalStrat pal3funky =
		new PalStrat("_pal3funky", 6,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, NO_INPUT, DOWN, UP},
		new Integer[] {74, 1, 1, 1});
	
	private static PalStrat pal4 =
		new PalStrat("_pal4", 8,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP},
		new Integer[] {80, 1});
		
	private static PalStrat pal4funky =
		new PalStrat("_pal4funky", 8,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, NO_INPUT, NO_INPUT, UP, DOWN},
		new Integer[] {74, 1, 1, 1, 1});
	
	private static PalStrat pal5 =
		new PalStrat("_pal5", 10,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP},
		new Integer[] {82, 1});
	
	private static PalStrat pal5funky =
		new PalStrat("_pal5funky", 10,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, NO_INPUT, NO_INPUT, NO_INPUT, UP, DOWN},
		new Integer[] {74, 1, 1, 1, 1, 1});
	
	private static PalStrat pal6 =
		new PalStrat("_pal6", 12,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP},
		new Integer[] {84, 1});
		
	private static PalStrat pal6funky =
		new PalStrat("_pal6funky", 12,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, NO_INPUT, NO_INPUT, NO_INPUT, NO_INPUT, UP, DOWN},
		new Integer[] {74, 1, 1, 1, 1, 1, 1});
	
	private static PalStrat pal7 =
		new PalStrat("_pal7", 14,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP},
		new Integer[] {86, 1});
			
	private static PalStrat pal7funky =
		new PalStrat("_pal7funky", 14,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, NO_INPUT, NO_INPUT, NO_INPUT, NO_INPUT, NO_INPUT, UP, DOWN},
		new Integer[] {74, 1, 1, 1, 1, 1, 1, 1});
	
	private static PalStrat holdpal0 =
		new PalStrat("_pal(hold0)", 0,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, UP},
		new Integer[] {0, 0});
	
	private static PalStrat holdpal1 =
		new PalStrat("_pal(hold1)", 2,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, UP, UP},
		new Integer[] {74, 0, 0});
	
	private static PalStrat holdpal2 =
		new PalStrat("_pal(hold2)", 4,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, UP, UP},
		new Integer[] {76, 0, 0});
	
	private static PalStrat holdpal2funky =
		new PalStrat("_pal(hold2)funky", 4,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, UP, DOWN, DOWN},
		new Integer[] {74, 1, 0, 0});
	
	private static PalStrat holdpal3 =
		new PalStrat("_pal(hold3)", 6,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, UP, UP},
		new Integer[] {78, 0, 0});
	
	private static PalStrat holdpal3funky =
		new PalStrat("_pal(hold3)funky", 6,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, NO_INPUT, UP, DOWN, DOWN},
		new Integer[] {74, 1, 1, 0, 0});
	
	private static PalStrat holdpal4 =
		new PalStrat("_pal(hold4)", 8,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, UP, UP},
		new Integer[] {80, 0, 0});
	
	private static PalStrat holdpal4funky =
		new PalStrat("_pal(hold4)funky", 8,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, NO_INPUT, NO_INPUT, DOWN, UP, UP},
		new Integer[] {74, 1, 1, 1, 0, 0});
	
	private static PalStrat holdpal5 =
		new PalStrat("_pal(hold5)", 10,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, UP, UP},
		new Integer[] {82, 0, 0});
	
	private static PalStrat holdpal5funky =
		new PalStrat("_pal(hold5)funky", 10,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, NO_INPUT, NO_INPUT, NO_INPUT, DOWN, UP, UP},
		new Integer[] {74, 1, 1, 1, 1, 0, 0});
	
	private static PalStrat holdpal6 =
		new PalStrat("_pal(hold6)", 12,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, UP, UP},
		new Integer[] {84, 0, 0});
		
	private static PalStrat holdpal6funky =
		new PalStrat("_pal(hold6)funky", 12,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, NO_INPUT, NO_INPUT, NO_INPUT, NO_INPUT, DOWN, UP, UP},
		new Integer[] {74, 1, 1, 1, 1, 1, 0, 0});
		
	private static PalStrat holdpal7 =
		new PalStrat("_pal(hold7)", 14,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, UP, UP},
		new Integer[] {86, 0, 0});
			
	private static PalStrat holdpal7funky =
		new PalStrat("_pal(hold7)funky", 14,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, NO_INPUT, NO_INPUT, NO_INPUT, NO_INPUT, NO_INPUT, DOWN, UP, UP},
		new Integer[] {74, 1, 1, 1, 1, 1, 1, 0, 0});
	
	private static PalStrat cheatpal0 =
		new PalStrat("_pal(ab0)", 0,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, UP | A, UP | A},
		new Integer[] {72, 0, 0});
	
	private static PalStrat cheatpal1 =
		new PalStrat("_pal(ab1)", 2,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, UP | A, UP | A},
		new Integer[] {74, 0, 0});
	
	private static PalStrat cheatpal2 =
		new PalStrat("_pal(ab2)", 4,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, UP | A, UP | A},
		new Integer[] {76, 0, 0});
	
	private static PalStrat cheatpal3 =
		new PalStrat("_pal(ab3)", 6,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, UP | A, UP | A},
		new Integer[] {78, 0, 0});
	
	private static PalStrat cheatpal4 =
		new PalStrat("_pal(ab4)", 8,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, UP | A, UP | A},
		new Integer[] {80, 0, 0});
	
	private static PalStrat cheatpal5 =
		new PalStrat("_pal(ab5)", 10,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, UP | A, UP | A},
		new Integer[] {82, 0, 0});
	
	private static PalStrat cheatpal6 =
		new PalStrat("_pal(ab6)", 12,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, UP | A, UP | A},
		new Integer[] {84, 0, 0});
	
	private static PalStrat cheatpal7 =
		new PalStrat("_pal(ab7)", 14,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, UP | A, UP | A},
		new Integer[] {86, 0, 0});
	
	private static PalStrat supercheatpal0 =
		new PalStrat("_pal(rel0)", 0,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, NO_INPUT, UP | A, UP | A},
		new Integer[] {1, 71, 0, 0});
	
	private static PalStrat supercheatpal1 =
		new PalStrat("_pal(rel1)", 2,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, NO_INPUT, UP | A, UP | A},
		new Integer[] {1, 73, 0, 0});
	
	private static PalStrat supercheatpal2 =
		new PalStrat("_pal(rel2)", 4,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, NO_INPUT, UP | A, UP | A},
		new Integer[] {1, 75, 0, 0});
	
	private static PalStrat supercheatpal3 =
		new PalStrat("_pal(rel3)", 6,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, NO_INPUT, UP | A, UP | A},
		new Integer[] {1, 77, 0, 0});
	
	private static PalStrat supercheatpal4 =
		new PalStrat("_pal(rel4)", 8,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, NO_INPUT, UP | A, UP | A},
		new Integer[] {1, 79, 0, 0});
	
	private static PalStrat supercheatpal5 =
		new PalStrat("_pal(rel5)", 10,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, NO_INPUT, UP | A, UP | A},
		new Integer[] {1, 81, 0, 0});
	
	private static PalStrat supercheatpal6 =
		new PalStrat("_pal(rel6)", 12,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, NO_INPUT, UP | A, UP | A},
		new Integer[] {1, 83, 0, 0});
	
	private static PalStrat supercheatpal7 =
		new PalStrat("_pal(rel7)", 14,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, NO_INPUT, UP | A, UP | A},
		new Integer[] {1, 85, 0, 0});

	private static Strat gfSkip0 =
		new Strat("_gfskip0", 0,
		new Integer[] {RedBlueAddr.joypadAddr},
		new Integer[] {UP | SELECT | B},
		new Integer[] {1});
	
	private static Strat gfSkip1 =
		new Strat("_gfskip1", 1,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {1, 1});
	
	private static Strat gfSkip2 =
		new Strat("_gfskip2", 2,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {2, 1});
	
	private static Strat gfSkip3 =
		new Strat("_gfskip3", 3,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {3, 1});
	
	private static Strat gfSkip4 =
		new Strat("_gfskip4", 4,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {4, 1});
	
	private static Strat gfSkip5 =
		new Strat("_gfskip5", 5,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {5, 1});
	
	private static Strat gfSkip6 =
		new Strat("_gfskip6", 6,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {6, 1});
	
	private static Strat gfSkip7 =
		new Strat("_gfskip7", 7,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {7, 1});
	
	private static Strat gfSkip8 =
		new Strat("_gfskip8", 8,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {8, 1});
	
	private static Strat gfSkip9 =
		new Strat("_gfskip9", 9,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {9, 1});
	
	private static Strat gfSkip10 =
		new Strat("_gfskip10", 10,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {10, 1});
	
	private static Strat gfSkip11 =
		new Strat("_gfskip11", 11,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {11, 1});
	
	private static Strat gfSkip12 =
		new Strat("_gfskip12", 12,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {12, 1});
	
	private static Strat gfSkip13 =
		new Strat("_gfskip13", 13,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {13, 1});
	
	private static Strat gfSkip14 =
		new Strat("_gfskip14", 14,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {14, 1});
	
	private static Strat gfSkip15 =
		new Strat("_gfskip15", 15,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {15, 1});

	private static List<Strat> gf = Arrays.asList
		(gfSkip0, gfSkip1, gfSkip2, gfSkip3, gfSkip4, gfSkip5, gfSkip6, gfSkip7, gfSkip8, gfSkip9,
		gfSkip10, gfSkip11, gfSkip12, gfSkip13, gfSkip14, gfSkip15);

	private static Strat nido0 =
		new Strat("_hop0", 172 + TITLE_BASE_COST,
		new Integer[] {RedBlueAddr.joypadAddr},
		new Integer[] {UP | SELECT | B},
		new Integer[] {1});
	
	private static Strat nido1 =
		new Strat("_hop1", 172 + TITLE_BASE_COST + 1,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {1, 1});
	
	private static Strat nido2 =
		new Strat("_hop2", 172 + TITLE_BASE_COST + 2,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {2, 1});
	
	private static Strat nido3 =
		new Strat("_hop3", 172 + TITLE_BASE_COST + 3,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {3, 1});
	
	private static Strat nido4 =
		new Strat("_hop4", 172 + TITLE_BASE_COST + 4,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {4, 1});
	
	private static Strat nido5 =
		new Strat("_hop5", 172 + TITLE_BASE_COST + 5,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {5, 1});
	
	private static Strat nido6 =
		new Strat("_hop6", 172 + TITLE_BASE_COST + 6,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {6, 1});
	
	private static Strat nido7 =
		new Strat("_hop7", 172 + TITLE_BASE_COST + 7,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {7, 1});
	
	private static Strat nido8 =
		new Strat("_hop8", 172 + TITLE_BASE_COST + 8,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {8, 1});
	
	private static Strat nido9 =
		new Strat("_hop9", 172 + TITLE_BASE_COST + 9,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {9, 1});
	
	private static Strat nido10 =
		new Strat("_hop10", 172 + TITLE_BASE_COST + 10,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {10, 1});
	
	private static Strat nido11 =
		new Strat("_hop11", 172 + TITLE_BASE_COST + 11,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {11, 1});
	
	private static Strat nido12 =
		new Strat("_hop12", 172 + TITLE_BASE_COST + 12,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {12, 1});
	
	private static Strat nido13 =
		new Strat("_hop13", 172 + TITLE_BASE_COST + 13,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {13, 1});
	
	private static Strat nido14 =
		new Strat("_hop14", 172 + TITLE_BASE_COST + 14,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {14, 1});
	
	private static Strat nido15 =
		new Strat("_hop15", 172 + TITLE_BASE_COST + 15,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {15, 1});
	
	private static List<Strat> nido = Arrays.asList
		(nido0, nido1, nido2, nido3, nido4, nido5, nido6, nido7, nido8, nido9,
		nido10, nido11, nido12, nido13, nido14, nido15);

	private static Strat newGame0 =
		new Strat("_newgame0", 20 + 20,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {A, A},
		new Integer[] {0, 0});
	
	private static Strat newGame1 =
		new Strat("_newgame1", 20 + 20 + 1,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {1, 0, 0});
	
	private static Strat newGame2 =
		new Strat("_newgame2", 20 + 20 + 2,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {2, 0, 0});
	
	private static Strat newGame3 =
		new Strat("_newgame3", 20 + 20 + 3,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {3, 0, 0});
	
	private static Strat newGame4 =
		new Strat("_newgame4", 20 + 20 + 4,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {4, 0, 0});
	
	private static Strat newGame5 =
		new Strat("_newgame5", 20 + 20 + 5,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {5, 0, 0});
	
	private static Strat newGame6 =
		new Strat("_newgame6", 20 + 20 + 6,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {6, 0, 0});
	
	private static Strat newGame7 =
		new Strat("_newgame7", 20 + 20 + 7,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {7, 0, 0});
	
	private static Strat newGame8 =
		new Strat("_newgame8", 20 + 20 + 8,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {8, 0, 0});
	
	private static Strat newGame9 =
		new Strat("_newgame9", 20 + 20 + 9,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {9, 0, 0});
	
	private static Strat newGame10 =
		new Strat("_newgame10", 20 + 20 + 10,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {10, 0, 0});
	
	private static Strat newGame11 =
		new Strat("_newgame11", 20 + 20 + 11,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {11, 0, 0});
	
	private static Strat newGame12 =
		new Strat("_newgame12", 20 + 20 + 12,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {12, 0, 0});
	
	private static Strat newGame13 =
		new Strat("_newgame13", 20 + 20 + 13,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {13, 0, 0});
	
	private static Strat newGame14 =
		new Strat("_newgame14", 20 + 20 + 14,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {14, 0, 0});
	
	private static Strat newGame15 =
		new Strat("_newgame15", 20 + 20 + 15,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {15, 0, 0});

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
                gb.advanceToAddress(addr[i]);
                gb.injectRBInput(input[i]);
                for(int j=0; j<advanceFrames[i]; j++) {
                    gb.advanceFrame();
                }
            }
        }
    }

    private static class PalStrat extends Strat {
        PalStrat(String name, int cost, Integer[] addr, Integer[] input, Integer[] advanceFrames) {
            super(name, cost, addr, input, advanceFrames);
        }
        @Override public void execute(Gb gb) {
            for (int i = 0; i < addr.length; i++) {
                gb.advanceWithJoypadToAddress(input[i], addr[i]);
                gb.advanceFrame(input[i]);
                for (int j = 0; j < advanceFrames[i]; j++) {
                    gb.advanceFrame(input[i]);
                }
            }
        }
    }

    @SuppressWarnings("serial")
	static class IntroSequence extends ArrayList<Strat> implements Comparable<IntroSequence> {
        IntroSequence(Strat... strats) {
            super(Arrays.asList(strats));
        }
        IntroSequence(IntroSequence other) {
            super(other);
        }
        @Override public String toString() {
            String ret = gameName;
            for(Strat s : this) {
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

    private static ArrayList<IntroSequence> permute(List<? extends Strat> sl1, List<? extends Strat> sl2) {
        ArrayList<IntroSequence> seqs = new ArrayList<>();
        for(Strat s1 : sl1) {
            for(Strat s2 : sl2) {
                IntroSequence seq = new IntroSequence();
                seq.add(s1);
                seq.add(s2);
                seqs.add(seq);
            }
        }
        return seqs;
    }

    private static IntroSequence append(IntroSequence seq, Strat... strats) {
        IntroSequence newSeq = new IntroSequence(seq);
        newSeq.addAll(Arrays.asList(strats));
        return newSeq;
    }

    private static IntroSequence append(Strat strat, IntroSequence seq) {
        IntroSequence newSeq = new IntroSequence(strat);
        newSeq.addAll(seq);
        return newSeq;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if (!new File("roms").exists()) {
            new File("roms").mkdir();
            System.err.println("I need ROMs to simulate!");
            System.exit(0);
        }


        File file = new File(gameName + "_tas_tids.txt");
        PrintWriter writer = new PrintWriter(file);

        ArrayList<Strat> title0 = new ArrayList<>();
        ArrayList<Strat> title1 = new ArrayList<>();
        ArrayList<Strat> title2 = new ArrayList<>();
        ArrayList<Strat> title3 = new ArrayList<>();
        ArrayList<Strat> title4 = new ArrayList<>();
        ArrayList<Strat> title5 = new ArrayList<>();
        ArrayList<Strat> title6 = new ArrayList<>();
        ArrayList<Strat> title7 = new ArrayList<>();
        ArrayList<Strat> title8 = new ArrayList<>();
        ArrayList<Strat> title9 = new ArrayList<>();
        ArrayList<Strat> title10 = new ArrayList<>();
        ArrayList<Strat> title11 = new ArrayList<>();
        ArrayList<Strat> title12 = new ArrayList<>();
        ArrayList<Strat> title13 = new ArrayList<>();
        ArrayList<Strat> title14 = new ArrayList<>();
        ArrayList<Strat> title15 = new ArrayList<>();
        
        int maxTitle = (MAX_COST - (492 + 172 + CRY_BASE_COST + TITLE_BASE_COST + 20 + 20));
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+1], input = new Integer[i+1], advFrames = new Integer[i+1];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = START;
            advFrames[i] = 1;
            title0.add(new Strat("_title0", CRY_BASE_COST + 270*i, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 1;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title1.add(new Strat("_title1", CRY_BASE_COST + 270*i + 1, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 2;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title2.add(new Strat("_title2", CRY_BASE_COST + 270*i + 2, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 3;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title3.add(new Strat("_title3", CRY_BASE_COST + 270*i + 3, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 4;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title4.add(new Strat("_title4", CRY_BASE_COST + 270*i + 4, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 5;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title5.add(new Strat("_title5", CRY_BASE_COST + 270*i + 5, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 6;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title6.add(new Strat("_title6", CRY_BASE_COST + 270*i + 6, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 7;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title7.add(new Strat("_title7", CRY_BASE_COST + 270*i + 7, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 8;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title8.add(new Strat("_title8", CRY_BASE_COST + 270*i + 8, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 9;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title9.add(new Strat("_title9", CRY_BASE_COST + 270*i + 9, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 10;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title10.add(new Strat("_title10", CRY_BASE_COST + 270*i + 10, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 11;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title11.add(new Strat("_title11", CRY_BASE_COST + 270*i + 11, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 12;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title12.add(new Strat("_title12", CRY_BASE_COST + 270*i + 12, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 13;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title13.add(new Strat("_title13", CRY_BASE_COST + 270*i + 13, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 14;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title14.add(new Strat("_title14", CRY_BASE_COST + 270*i + 14, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 15;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title15.add(new Strat("_title15", CRY_BASE_COST + 270*i + 15, addr, input, advFrames));
        }
        
        ArrayList<IntroSequence> newGameSequences = new ArrayList<>();
        
        ArrayList<IntroSequence> s3seqs = new ArrayList<>();
        s3seqs.addAll(permute(gf, nido));
       
        while(!s3seqs.isEmpty()) {
            ArrayList<IntroSequence> s4seqs = new ArrayList<>();
            for(IntroSequence s3 : s3seqs) {
                int ngcost = s3.cost() + CRY_BASE_COST + 20 + 20;
                int ngmax = (MAX_COST - ngcost - 492);
                for(int i=0; ngmax>=0 && i<=ngmax/270; i++) {
                    s4seqs.add(append(s3, title0.get(i)));
                    s4seqs.add(append(s3, title1.get(i)));
                    s4seqs.add(append(s3, title2.get(i)));
                    s4seqs.add(append(s3, title3.get(i)));
                    s4seqs.add(append(s3, title4.get(i)));
                    s4seqs.add(append(s3, title5.get(i)));
                    s4seqs.add(append(s3, title6.get(i)));
                    s4seqs.add(append(s3, title7.get(i)));
                    s4seqs.add(append(s3, title8.get(i)));
                    s4seqs.add(append(s3, title9.get(i)));
                    s4seqs.add(append(s3, title10.get(i)));
                    s4seqs.add(append(s3, title11.get(i)));
                    s4seqs.add(append(s3, title12.get(i)));
                    s4seqs.add(append(s3, title13.get(i)));
                    s4seqs.add(append(s3, title14.get(i)));
                    s4seqs.add(append(s3, title15.get(i)));
                }
            }
            
            s3seqs.clear();
        
        while(!s4seqs.isEmpty()) {
            ArrayList<IntroSequence> s4tmp = new ArrayList<>();
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame0);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame1);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame2);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame3);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame4);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame5);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame6);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame7);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame8);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame9);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame10);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame11);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame12);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame13);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame14);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame15);
                newGameSequences.add(seq);
            }
            s4seqs = new ArrayList<>(s4tmp);
        }
    }
        
        
        ArrayList<IntroSequence> introSequencesTmp = new ArrayList<>(newGameSequences);

        List<IntroSequence> introSequences = new ArrayList<>();
        for(IntroSequence seq : introSequencesTmp) {
        	
            introSequences.add(append(nopal, seq));
            introSequences.add(append(abss, seq));
            
            introSequences.add(append(pal0, seq));
            introSequences.add(append(pal1, seq));
            introSequences.add(append(pal2, seq));
            introSequences.add(append(pal3, seq));
            introSequences.add(append(pal4, seq));
            introSequences.add(append(pal5, seq));
            introSequences.add(append(pal6, seq));
            introSequences.add(append(pal7, seq));
            
            introSequences.add(append(pal2funky, seq));
            introSequences.add(append(pal3funky, seq));
            introSequences.add(append(pal4funky, seq));
            introSequences.add(append(pal5funky, seq));
            introSequences.add(append(pal6funky, seq));
            introSequences.add(append(pal7funky, seq));
            
            introSequences.add(append(holdpal0, seq));
            introSequences.add(append(holdpal1, seq));
            introSequences.add(append(holdpal2, seq));
            introSequences.add(append(holdpal3, seq));
            introSequences.add(append(holdpal4, seq));
            introSequences.add(append(holdpal5, seq));
            introSequences.add(append(holdpal6, seq));
            introSequences.add(append(holdpal7, seq));
            
            introSequences.add(append(holdpal2funky, seq));
            introSequences.add(append(holdpal3funky, seq));
            introSequences.add(append(holdpal4funky, seq));
            introSequences.add(append(holdpal5funky, seq));
            introSequences.add(append(holdpal6funky, seq));
            introSequences.add(append(holdpal7funky, seq));
            
            introSequences.add(append(cheatpal0, seq));
            introSequences.add(append(cheatpal1, seq));
            introSequences.add(append(cheatpal2, seq));
            introSequences.add(append(cheatpal3, seq));
            introSequences.add(append(cheatpal4, seq));
            introSequences.add(append(cheatpal5, seq));
            introSequences.add(append(cheatpal6, seq));
            introSequences.add(append(cheatpal7, seq));
            
            introSequences.add(append(supercheatpal0, seq));
            introSequences.add(append(supercheatpal1, seq));
            introSequences.add(append(supercheatpal2, seq));
            introSequences.add(append(supercheatpal3, seq));
            introSequences.add(append(supercheatpal4, seq));
            introSequences.add(append(supercheatpal5, seq));
            introSequences.add(append(supercheatpal6, seq));
            introSequences.add(append(supercheatpal7, seq));

        }
        
        introSequencesTmp.clear();

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
                            + ", Cost: " + (gb.getCycleCount() - 29381434) + " Cycles");
            gb.loadState(Bios);
            writer.flush();
            System.out.printf("Current Cost: %d%n", seq.cost());
        }
        writer.close();
    }

    private static int readTID(Gb gb) {
        return (gb.readMemory(0xD359) << 8) | gb.readMemory(0xD35A);
    }
}
