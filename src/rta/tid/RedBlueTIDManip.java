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
	private static final int MAX_COST = 793 + 25;
	
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
	
	private static PalStrat pal1funky =
		new PalStrat("_pal1funky", 2,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP, DOWN},
		new Integer[] {72, 1, 1});
	
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
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, DOWN, UP},
		new Integer[] {76, 1, 1});
	
	private static PalStrat pal4 =
		new PalStrat("_pal4", 8,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP},
		new Integer[] {80, 1});
		
	private static PalStrat pal4funky =
		new PalStrat("_pal4funky", 8,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP, DOWN},
		new Integer[] {78, 1, 1});
	
	private static PalStrat pal5 =
		new PalStrat("_pal5", 10,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP},
		new Integer[] {82, 1});
	
	private static PalStrat pal5funky =
		new PalStrat("_pal5funky", 10,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP, DOWN},
		new Integer[] {80, 1, 1});
	
	private static PalStrat pal6 =
		new PalStrat("_pal6", 12,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP},
		new Integer[] {84, 1});
		
	private static PalStrat pal6funky =
		new PalStrat("_pal6funky", 12,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP, DOWN},
		new Integer[] {82, 1, 1});
	
	private static PalStrat pal7 =
		new PalStrat("_pal7", 14,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP},
		new Integer[] {86, 1});
			
	private static PalStrat pal7funky =
		new PalStrat("_pal7funky", 14,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP, DOWN},
		new Integer[] {84, 1, 1});
	
	private static PalStrat pal8 =
		new PalStrat("_pal8", 16,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP},
		new Integer[] {88, 1});
			
	private static PalStrat pal8funky =
		new PalStrat("_pal8funky", 16,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP, DOWN},
		new Integer[] {86, 1, 1});
	
	private static PalStrat pal9 =
		new PalStrat("_pal9", 18,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP},
		new Integer[] {90, 1});
			
	private static PalStrat pal9funky =
		new PalStrat("_pal9funky", 18,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP, DOWN},
		new Integer[] {88, 1, 1});
	
	private static PalStrat pal10 =
		new PalStrat("_pal10", 20,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP},
		new Integer[] {92, 1});
			
	private static PalStrat pal10funky =
		new PalStrat("_pal10funky", 20,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP, DOWN},
		new Integer[] {90, 1, 1});
	
	private static PalStrat pal11 =
		new PalStrat("_pal11", 22,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP},
		new Integer[] {94, 1});
			
	private static PalStrat pal11funky =
		new PalStrat("_pal11funky", 22,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP, DOWN},
		new Integer[] {92, 1, 1});
	
	private static PalStrat pal12 =
		new PalStrat("_pal12", 24,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP},
		new Integer[] {96, 1});
			
	private static PalStrat pal12funky =
		new PalStrat("_pal12funky", 24,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr},
		new Integer[] {NO_INPUT, UP, DOWN},
		new Integer[] {94, 1, 1});
	
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
	
	private static PalStrat holdpal1funky =
		new PalStrat("_pal(hold1)funky", 2,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, UP, DOWN, DOWN},
		new Integer[] {72, 1, 0, 0});
	
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
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, UP, DOWN, DOWN},
		new Integer[] {76, 1, 0, 0});
	
	private static PalStrat holdpal4 =
		new PalStrat("_pal(hold4)", 8,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, UP, UP},
		new Integer[] {80, 0, 0});
	
	private static PalStrat holdpal4funky =
		new PalStrat("_pal(hold4)funky", 8,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, DOWN, UP, UP},
		new Integer[] {78, 1, 0, 0});
	
	private static PalStrat holdpal5 =
		new PalStrat("_pal(hold5)", 10,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, UP, UP},
		new Integer[] {82, 0, 0});
	
	private static PalStrat holdpal5funky =
		new PalStrat("_pal(hold5)funky", 10,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, DOWN, UP, UP},
		new Integer[] {80, 1, 0, 0});
	
	private static PalStrat holdpal6 =
		new PalStrat("_pal(hold6)", 12,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, UP, UP},
		new Integer[] {84, 0, 0});
		
	private static PalStrat holdpal6funky =
		new PalStrat("_pal(hold6)funky", 12,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, DOWN, UP, UP},
		new Integer[] {82, 1, 0, 0});
		
	private static PalStrat holdpal7 =
		new PalStrat("_pal(hold7)", 14,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, UP, UP},
		new Integer[] {86, 0, 0});
			
	private static PalStrat holdpal7funky =
		new PalStrat("_pal(hold7)funky", 14,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, DOWN, UP, UP},
		new Integer[] {84, 1, 0, 0});
	
	private static PalStrat holdpal8 =
		new PalStrat("_pal(hold8)", 16,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, UP, UP},
		new Integer[] {88, 0, 0});
				
	private static PalStrat holdpal8funky =
		new PalStrat("_pal(hold8)funky", 16,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, DOWN, UP, UP},
		new Integer[] {86, 1, 0, 0});
	
	private static PalStrat holdpal9 =
		new PalStrat("_pal(hold9)", 18,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, UP, UP},
		new Integer[] {90, 0, 0});
					
	private static PalStrat holdpal9funky =
		new PalStrat("_pal(hold9)funky", 18,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, DOWN, UP, UP},
		new Integer[] {88, 1, 0, 0});
	
	private static PalStrat holdpal10 =
		new PalStrat("_pal(hold10)", 20,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, UP, UP},
		new Integer[] {92, 0, 0});
						
	private static PalStrat holdpal10funky =
		new PalStrat("_pal(hold10)funky", 20,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, DOWN, UP, UP},
		new Integer[] {90, 1, 0, 0});
		
	private static PalStrat holdpal11 =
		new PalStrat("_pal(hold11)", 22,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, UP, UP},
		new Integer[] {94, 0, 0});
							
	private static PalStrat holdpal11funky =
		new PalStrat("_pal(hold11)funky", 22,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, DOWN, UP, UP},
		new Integer[] {92, 1, 0, 0});
	
	private static PalStrat holdpal12 =
		new PalStrat("_pal(hold12)", 24,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, UP, UP},
		new Integer[] {96, 0, 0});
						
	private static PalStrat holdpal12funky =
		new PalStrat("_pal(hold12)funky", 24,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {NO_INPUT, DOWN, UP, UP},
		new Integer[] {94, 1, 0, 0});
	
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

	private static PalStrat cheatpal8 =
		new PalStrat("_pal(ab8)", 16,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, UP | A, UP | A},
		new Integer[] {88, 0, 0});
	
	private static PalStrat cheatpal9 =
		new PalStrat("_pal(ab9)", 18,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, UP | A, UP | A},
		new Integer[] {90, 0, 0});
	
	private static PalStrat cheatpal10 =
		new PalStrat("_pal(ab10)", 20,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, UP | A, UP | A},
		new Integer[] {92, 0, 0});
	
	private static PalStrat cheatpal11 =
		new PalStrat("_pal(ab11)", 22,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, UP | A, UP | A},
		new Integer[] {94, 0, 0});
	
	private static PalStrat cheatpal12 =
		new PalStrat("_pal(ab12)", 24,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, UP | A, UP | A},
		new Integer[] {96, 0, 0});
	
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
	
	private static PalStrat supercheatpal8 =
		new PalStrat("_pal(rel8)", 16,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, NO_INPUT, UP | A, UP | A},
		new Integer[] {1, 87, 0, 0});
	
	private static PalStrat supercheatpal9 =
		new PalStrat("_pal(rel9)", 18,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, NO_INPUT, UP | A, UP | A},
		new Integer[] {1, 89, 0, 0});
	
	private static PalStrat supercheatpal10 =
		new PalStrat("_pal(rel10)", 20,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, NO_INPUT, UP | A, UP | A},
		new Integer[] {1, 91, 0, 0});
	
	private static PalStrat supercheatpal11 =
		new PalStrat("_pal(rel11)", 22,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, NO_INPUT, UP | A, UP | A},
		new Integer[] {1, 93, 0, 0});
	
	private static PalStrat supercheatpal12 =
		new PalStrat("_pal(rel12)", 24,
		new Integer[] {RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.biosReadKeypadAddr, RedBlueAddr.initAddr},
		new Integer[] {UP, NO_INPUT, UP | A, UP | A},
		new Integer[] {1, 95, 0, 0});

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
	
	private static Strat gfSkip16 =
		new Strat("_gfskip16", 16,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {16, 1});
	
	private static Strat gfSkip17 =
		new Strat("_gfskip17", 17,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {17, 1});
	
	private static Strat gfSkip18 =
		new Strat("_gfskip18", 18,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {18, 1});
	
	private static Strat gfSkip19 =
		new Strat("_gfskip19", 19,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {19, 1});
	
	private static Strat gfSkip20 =
		new Strat("_gfskip20", 20,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {20, 1});
	
	private static Strat gfSkip21 =
		new Strat("_gfskip21", 21,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {21, 1});
	
	private static Strat gfSkip22 =
		new Strat("_gfskip22", 22,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {22, 1});
	
	private static Strat gfSkip23 =
		new Strat("_gfskip23", 23,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {23, 1});
	
	private static Strat gfSkip24 =
		new Strat("_gfskip24", 24,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {24, 1});
	
	private static Strat gfSkip25 =
		new Strat("_gfskip25", 25,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {25, 1});

	private static List<Strat> gf = Arrays.asList(
		gfSkip0, gfSkip1, gfSkip2, gfSkip3, gfSkip4, gfSkip5, gfSkip6, gfSkip7, gfSkip8, gfSkip9, gfSkip10,
		gfSkip11, gfSkip12, gfSkip13, gfSkip14, gfSkip15, gfSkip16, gfSkip17, gfSkip18, gfSkip19, gfSkip20,
		gfSkip21, gfSkip22, gfSkip23, gfSkip24, gfSkip25);

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
	
	private static Strat nido16 =
		new Strat("_hop16", 172 + TITLE_BASE_COST + 16,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {16, 1});
	
	private static Strat nido17 =
		new Strat("_hop17", 172 + TITLE_BASE_COST + 17,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {17, 1});
	
	private static Strat nido18 =
		new Strat("_hop18", 172 + TITLE_BASE_COST + 18,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {18, 1});
	
	private static Strat nido19 =
		new Strat("_hop19", 172 + TITLE_BASE_COST + 19,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {19, 1});
	
	private static Strat nido20 =
		new Strat("_hop20", 172 + TITLE_BASE_COST + 20,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {20, 1});
	
	private static Strat nido21 =
		new Strat("_hop21", 172 + TITLE_BASE_COST + 21,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {21, 1});
	
	private static Strat nido22 =
		new Strat("_hop22", 172 + TITLE_BASE_COST + 22,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {22, 1});
	
	private static Strat nido23 =
		new Strat("_hop23", 172 + TITLE_BASE_COST + 23,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {23, 1});
	
	private static Strat nido24 =
		new Strat("_hop24", 172 + TITLE_BASE_COST + 24,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {24, 1});
	
	private static Strat nido25 =
		new Strat("_hop25", 172 + TITLE_BASE_COST + 25,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr},
		new Integer[] {NO_INPUT, UP | SELECT | B},
		new Integer[] {25, 1});
	
	private static List<Strat> nido = Arrays.asList(
		nido0, nido1, nido2, nido3, nido4, nido5, nido6, nido7, nido8, nido9, nido10,
		nido11, nido12, nido13, nido14, nido15, nido16, nido17, nido18, nido19, nido20,
		nido21, nido22, nido23, nido24, nido25);

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
	
	private static Strat newGame16 =
		new Strat("_newgame16", 20 + 20 + 16,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {16, 0, 0});
	
	private static Strat newGame17 =
		new Strat("_newgame17", 20 + 20 + 17,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {17, 0, 0});
	
	private static Strat newGame18 =
		new Strat("_newgame18", 20 + 20 + 18,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {18, 0, 0});
	
	private static Strat newGame19 =
		new Strat("_newgame19", 20 + 20 + 19,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {19, 0, 0});
	
	private static Strat newGame20 =
		new Strat("_newgame20", 20 + 20 + 20,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {20, 0, 0});
	
	private static Strat newGame21 =
		new Strat("_newgame21", 20 + 20 + 21,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {21, 0, 0});
	
	private static Strat newGame22 =
		new Strat("_newgame22", 20 + 20 + 22,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {22, 0, 0});
	
	private static Strat newGame23 =
		new Strat("_newgame23", 20 + 20 + 23,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {23, 0, 0});
	
	private static Strat newGame24 =
		new Strat("_newgame24", 20 + 20 + 24,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {24, 0, 0});
	
	private static Strat newGame25 =
		new Strat("_newgame25", 20 + 20 + 25,
		new Integer[] {RedBlueAddr.joypadAddr, RedBlueAddr.joypadAddr, RedBlueAddr.postTIDAddr},
		new Integer[] {NO_INPUT, A, A},
		new Integer[] {25, 0, 0});

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
        ArrayList<Strat> title16 = new ArrayList<>();
        ArrayList<Strat> title17 = new ArrayList<>();
        ArrayList<Strat> title18 = new ArrayList<>();
        ArrayList<Strat> title19 = new ArrayList<>();
        ArrayList<Strat> title20 = new ArrayList<>();
        ArrayList<Strat> title21 = new ArrayList<>();
        ArrayList<Strat> title22 = new ArrayList<>();
        ArrayList<Strat> title23 = new ArrayList<>();
        ArrayList<Strat> title24 = new ArrayList<>();
        ArrayList<Strat> title25 = new ArrayList<>();
        
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
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 16;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title16.add(new Strat("_title16", CRY_BASE_COST + 270*i + 16, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 17;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title17.add(new Strat("_title17", CRY_BASE_COST + 270*i + 17, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 18;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title18.add(new Strat("_title18", CRY_BASE_COST + 270*i + 18, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 19;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title19.add(new Strat("_title19", CRY_BASE_COST + 270*i + 19, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 20;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title20.add(new Strat("_title20", CRY_BASE_COST + 270*i + 20, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 21;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title21.add(new Strat("_title21", CRY_BASE_COST + 270*i + 21, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 22;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title22.add(new Strat("_title22", CRY_BASE_COST + 270*i + 22, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 23;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title23.add(new Strat("_title23", CRY_BASE_COST + 270*i + 23, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 24;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title24.add(new Strat("_title24", CRY_BASE_COST + 270*i + 24, addr, input, advFrames));
        }
        
        for(int i=0; maxTitle>=0 && i<=maxTitle/270; i++) {
            Integer[] addr = new Integer[i+2], input = new Integer[i+2], advFrames = new Integer[i+2];
            
            addr[i] = RedBlueAddr.joypadAddr;
            input[i] = NO_INPUT;
            advFrames[i] = 25;
            addr[i+1] = RedBlueAddr.joypadAddr;
            input[i+1] = START;
            advFrames[i+1] = 1;
            title25.add(new Strat("_title25", CRY_BASE_COST + 270*i + 25, addr, input, advFrames));
        }
        
        ArrayList<IntroSequence> newGameSequences = new ArrayList<>();
        
        ArrayList<IntroSequence> s3seqs = new ArrayList<>();
        s3seqs.addAll(permute(gf, nido));
       
        while(!s3seqs.isEmpty()) {
            ArrayList<IntroSequence> s4seqs = new ArrayList<>();
            for(IntroSequence s3 : s3seqs) {
            	
                int ngcost0 = s3.cost() + CRY_BASE_COST + 20 + 20 + 0;
                int ngmax0 = (MAX_COST - ngcost0 - 492);
                
                int ngcost1 = s3.cost() + CRY_BASE_COST + 20 + 20 + 1;
                int ngmax1 = (MAX_COST - ngcost1 - 492);
                
                int ngcost2 = s3.cost() + CRY_BASE_COST + 20 + 20 + 2;
                int ngmax2 = (MAX_COST - ngcost2 - 492);
                
                int ngcost3 = s3.cost() + CRY_BASE_COST + 20 + 20 + 3;
                int ngmax3 = (MAX_COST - ngcost3 - 492);
                
                int ngcost4 = s3.cost() + CRY_BASE_COST + 20 + 20 + 4;
                int ngmax4 = (MAX_COST - ngcost4 - 492);
                
                int ngcost5 = s3.cost() + CRY_BASE_COST + 20 + 20 + 5;
                int ngmax5 = (MAX_COST - ngcost5 - 492);
                
                int ngcost6 = s3.cost() + CRY_BASE_COST + 20 + 20 + 6;
                int ngmax6 = (MAX_COST - ngcost6 - 492);
                
                int ngcost7 = s3.cost() + CRY_BASE_COST + 20 + 20 + 7;
                int ngmax7 = (MAX_COST - ngcost7 - 492);
                
                int ngcost8 = s3.cost() + CRY_BASE_COST + 20 + 20 + 8;
                int ngmax8 = (MAX_COST - ngcost8 - 492);
                
                int ngcost9 = s3.cost() + CRY_BASE_COST + 20 + 20 + 9;
                int ngmax9 = (MAX_COST - ngcost9 - 492);
                
                int ngcost10 = s3.cost() + CRY_BASE_COST + 20 + 20 + 10;
                int ngmax10 = (MAX_COST - ngcost10 - 492);
                
                int ngcost11 = s3.cost() + CRY_BASE_COST + 20 + 20 + 11;
                int ngmax11 = (MAX_COST - ngcost11 - 492);
                
                int ngcost12 = s3.cost() + CRY_BASE_COST + 20 + 20 + 12;
                int ngmax12 = (MAX_COST - ngcost12 - 492);
                
                int ngcost13 = s3.cost() + CRY_BASE_COST + 20 + 20 + 13;
                int ngmax13 = (MAX_COST - ngcost13 - 492);
                
                int ngcost14 = s3.cost() + CRY_BASE_COST + 20 + 20 + 14;
                int ngmax14 = (MAX_COST - ngcost14 - 492);
                
                int ngcost15 = s3.cost() + CRY_BASE_COST + 20 + 20 + 15;
                int ngmax15 = (MAX_COST - ngcost15 - 492);
                
                int ngcost16 = s3.cost() + CRY_BASE_COST + 20 + 20 + 16;
                int ngmax16 = (MAX_COST - ngcost16 - 492);
                
                int ngcost17 = s3.cost() + CRY_BASE_COST + 20 + 20 + 17;
                int ngmax17 = (MAX_COST - ngcost17 - 492);
                
                int ngcost18 = s3.cost() + CRY_BASE_COST + 20 + 20 + 18;
                int ngmax18 = (MAX_COST - ngcost18 - 492);
                
                int ngcost19 = s3.cost() + CRY_BASE_COST + 20 + 20 + 19;
                int ngmax19 = (MAX_COST - ngcost19 - 492);
                
                int ngcost20 = s3.cost() + CRY_BASE_COST + 20 + 20 + 20;
                int ngmax20 = (MAX_COST - ngcost20 - 492);
                
                int ngcost21 = s3.cost() + CRY_BASE_COST + 20 + 20 + 21;
                int ngmax21 = (MAX_COST - ngcost21 - 492);
                
                int ngcost22 = s3.cost() + CRY_BASE_COST + 20 + 20 + 22;
                int ngmax22 = (MAX_COST - ngcost22 - 492);
                
                int ngcost23 = s3.cost() + CRY_BASE_COST + 20 + 20 + 23;
                int ngmax23 = (MAX_COST - ngcost23 - 492);
                
                int ngcost24 = s3.cost() + CRY_BASE_COST + 20 + 20 + 24;
                int ngmax24 = (MAX_COST - ngcost24 - 492);
                
                int ngcost25 = s3.cost() + CRY_BASE_COST + 20 + 20 + 25;
                int ngmax25 = (MAX_COST - ngcost25 - 492);

                for(int i=0; ngmax0>=0 && i<=ngmax0/270; i++) {
                    s4seqs.add(append(s3, title0.get(i)));
                } 

                for(int i=0; ngmax1>=0 && i<=ngmax1/270; i++) {
                    s4seqs.add(append(s3, title1.get(i)));
                }  

                for(int i=0; ngmax2>=0 && i<=ngmax2/270; i++) {
                    s4seqs.add(append(s3, title2.get(i)));
                } 

                for(int i=0; ngmax3>=0 && i<=ngmax3/270; i++) {
                    s4seqs.add(append(s3, title3.get(i)));
                } 

                for(int i=0; ngmax4>=0 && i<=ngmax4/270; i++) {
                    s4seqs.add(append(s3, title4.get(i)));
                } 

                for(int i=0; ngmax5>=0 && i<=ngmax5/270; i++) {
                    s4seqs.add(append(s3, title5.get(i)));
                } 

                for(int i=0; ngmax6>=0 && i<=ngmax6/270; i++) {
                    s4seqs.add(append(s3, title6.get(i)));
                } 

                for(int i=0; ngmax7>=0 && i<=ngmax7/270; i++) {
                    s4seqs.add(append(s3, title7.get(i)));
                } 

                for(int i=0; ngmax8>=0 && i<=ngmax8/270; i++) {
                    s4seqs.add(append(s3, title8.get(i)));
                } 

                for(int i=0; ngmax9>=0 && i<=ngmax9/270; i++) {
                    s4seqs.add(append(s3, title9.get(i)));
                } 

                for(int i=0; ngmax10>=0 && i<=ngmax10/270; i++) {
                    s4seqs.add(append(s3, title10.get(i)));
                } 

                for(int i=0; ngmax11>=0 && i<=ngmax11/270; i++) {
                    s4seqs.add(append(s3, title11.get(i)));
                } 

                for(int i=0; ngmax12>=0 && i<=ngmax12/270; i++) {
                    s4seqs.add(append(s3, title12.get(i)));
                } 

                for(int i=0; ngmax13>=0 && i<=ngmax13/270; i++) {
                    s4seqs.add(append(s3, title13.get(i)));
                } 

                for(int i=0; ngmax14>=0 && i<=ngmax14/270; i++) {
                    s4seqs.add(append(s3, title14.get(i)));
                } 

                for(int i=0; ngmax15>=0 && i<=ngmax15/270; i++) {
                    s4seqs.add(append(s3, title15.get(i)));
                } 

                for(int i=0; ngmax16>=0 && i<=ngmax16/270; i++) {
                    s4seqs.add(append(s3, title16.get(i)));
                } 

                for(int i=0; ngmax17>=0 && i<=ngmax17/270; i++) {
                    s4seqs.add(append(s3, title17.get(i)));
                } 

                for(int i=0; ngmax18>=0 && i<=ngmax18/270; i++) {
                    s4seqs.add(append(s3, title18.get(i)));
                } 

                for(int i=0; ngmax19>=0 && i<=ngmax19/270; i++) {
                    s4seqs.add(append(s3, title19.get(i)));
                } 

                for(int i=0; ngmax20>=0 && i<=ngmax20/270; i++) {
                    s4seqs.add(append(s3, title20.get(i)));
                } 

                for(int i=0; ngmax21>=0 && i<=ngmax21/270; i++) {
                    s4seqs.add(append(s3, title21.get(i)));
                } 

                for(int i=0; ngmax22>=0 && i<=ngmax22/270; i++) {
                    s4seqs.add(append(s3, title22.get(i)));
                }

                for(int i=0; ngmax23>=0 && i<=ngmax23/270; i++) {
                    s4seqs.add(append(s3, title23.get(i)));
                } 

                for(int i=0; ngmax24>=0 && i<=ngmax24/270; i++) {
                    s4seqs.add(append(s3, title24.get(i)));
                } 
                
                for(int i=0; ngmax25>=0 && i<=ngmax25/270; i++) {
                    s4seqs.add(append(s3, title25.get(i)));
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
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame16);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame17);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame18);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame19);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame20);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame21);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame22);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame23);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame24);
                newGameSequences.add(seq);
            }
            for (IntroSequence s4 : s4seqs) {
                IntroSequence seq = append(s4, newGame25);
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
            introSequences.add(append(pal8, seq));
            introSequences.add(append(pal9, seq));
            introSequences.add(append(pal10, seq));
            introSequences.add(append(pal11, seq));
            introSequences.add(append(pal12, seq));
            
            introSequences.add(append(pal1funky, seq));
            introSequences.add(append(pal2funky, seq));
            introSequences.add(append(pal3funky, seq));
            introSequences.add(append(pal4funky, seq));
            introSequences.add(append(pal5funky, seq));
            introSequences.add(append(pal6funky, seq));
            introSequences.add(append(pal7funky, seq));
            introSequences.add(append(pal8funky, seq));
            introSequences.add(append(pal9funky, seq));
            introSequences.add(append(pal10funky, seq));
            introSequences.add(append(pal11funky, seq));
            introSequences.add(append(pal12funky, seq));
            
            introSequences.add(append(holdpal0, seq));
            introSequences.add(append(holdpal1, seq));
            introSequences.add(append(holdpal2, seq));
            introSequences.add(append(holdpal3, seq));
            introSequences.add(append(holdpal4, seq));
            introSequences.add(append(holdpal5, seq));
            introSequences.add(append(holdpal6, seq));
            introSequences.add(append(holdpal7, seq));
            introSequences.add(append(holdpal8, seq));
            introSequences.add(append(holdpal9, seq));
            introSequences.add(append(holdpal10, seq));
            introSequences.add(append(holdpal11, seq));
            introSequences.add(append(holdpal12, seq));
            
            introSequences.add(append(holdpal1funky, seq));
            introSequences.add(append(holdpal2funky, seq));
            introSequences.add(append(holdpal3funky, seq));
            introSequences.add(append(holdpal4funky, seq));
            introSequences.add(append(holdpal5funky, seq));
            introSequences.add(append(holdpal6funky, seq));
            introSequences.add(append(holdpal7funky, seq));
            introSequences.add(append(holdpal8funky, seq));
            introSequences.add(append(holdpal9funky, seq));
            introSequences.add(append(holdpal10funky, seq));
            introSequences.add(append(holdpal11funky, seq));
            introSequences.add(append(holdpal12funky, seq));
            
            introSequences.add(append(cheatpal0, seq));
            introSequences.add(append(cheatpal1, seq));
            introSequences.add(append(cheatpal2, seq));
            introSequences.add(append(cheatpal3, seq));
            introSequences.add(append(cheatpal4, seq));
            introSequences.add(append(cheatpal5, seq));
            introSequences.add(append(cheatpal6, seq));
            introSequences.add(append(cheatpal7, seq));
            introSequences.add(append(cheatpal8, seq));
            introSequences.add(append(cheatpal9, seq));
            introSequences.add(append(cheatpal10, seq));
            introSequences.add(append(cheatpal11, seq));
            introSequences.add(append(cheatpal12, seq));
            
            introSequences.add(append(supercheatpal0, seq));
            introSequences.add(append(supercheatpal1, seq));
            introSequences.add(append(supercheatpal2, seq));
            introSequences.add(append(supercheatpal3, seq));
            introSequences.add(append(supercheatpal4, seq));
            introSequences.add(append(supercheatpal5, seq));
            introSequences.add(append(supercheatpal6, seq));
            introSequences.add(append(supercheatpal7, seq));
            introSequences.add(append(supercheatpal8, seq));
            introSequences.add(append(supercheatpal9, seq));
            introSequences.add(append(supercheatpal10, seq));
            introSequences.add(append(supercheatpal11, seq));
            introSequences.add(append(supercheatpal12, seq));

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
