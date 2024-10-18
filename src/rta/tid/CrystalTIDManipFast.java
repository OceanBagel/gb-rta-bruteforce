package rta.tid;

import rta.CrystalAddr;

import rta.gambatte.Gb;
import rta.gambatte.LoadFlags;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.text.DecimalFormat;

public class CrystalTIDManipFast {
    private static final int NO_INPUT = 0x00;
    private static final int A = 0x01;
    private static final int B = 0x02;
    private static final int SELECT = 0x04;
    private static final int START = 0x08;
    private static final int UP = 0x40;
    private static final int LEFT = 0x20;
    private static final int DOWN = 0x80;
    


    // Change this to increase/decrease number of intro sequence combinations processed
    private static final int MAX_COST = 700;
    
    // Change this to skip the lower cost manips (note: skipping will still take time)
    private static final int START_COST = 0;
    
    // Change this to 0 to disable soft resets before the manip
    private static final int MAX_RESETS = (MAX_COST / 47) + 1;

    // Set to 0 to disable frame strats, otherwise set to the frame to test (only 6 is implemented)
    private static int frameStrats = 0;

    // Output filename, will be overwritten without prompting
    private static String FILE_NAME = "crystal_tids_nobasecost.txt";



    private static Strat gfSkip =
	new Strat("_gfskip", 0,
	new Integer[] {CrystalAddr.readJoypadAddr},
	new Integer[] {START},
	new Integer[] {1});

    private static Strat gfWait =
	new Strat("_gfwait", 384,
	new Integer[] {CrystalAddr.introScene0Addr, CrystalAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {0, 1});

    private static Strat intro0 =
	new Strat("_intro0", 450,
	new Integer[] {CrystalAddr.introScene1Addr, CrystalAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {0, 1});

    private static Strat intro2 =
	new Strat("_intro1", 624,
	new Integer[] {CrystalAddr.introScene3Addr, CrystalAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {0, 1});

    private static Strat intro4 =
	new Strat("_intro2", 819,
	new Integer[] {CrystalAddr.introScene5Addr, CrystalAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {0, 1});

    private static Strat intro6 =
	new Strat("_intro3", 1052,
	new Integer[] {CrystalAddr.introScene7Addr, CrystalAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {0, 1});

    private static Strat intro10 =
	new Strat("_intro4", 1396,
	new Integer[] {CrystalAddr.introScene11Addr, CrystalAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {0, 1});

    private static Strat intro12 =
	new Strat("_intro5", 1674,
	new Integer[] {CrystalAddr.introScene13Addr, CrystalAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {0, 1});

    private static Strat intro14 =
	new Strat("_intro6", 1871,
	new Integer[] {CrystalAddr.introScene15Addr, CrystalAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {0, 1});

    private static Strat intro16 =
	new Strat("_intro7", 2085,
	new Integer[] {CrystalAddr.introScene17Addr, CrystalAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {0, 1});

    private static Strat intro18 =
	new Strat("_intro8", 2254,
	new Integer[] {CrystalAddr.introScene19Addr, CrystalAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {0, 1});

    private static Strat intro25 =
	new Strat("_intro9", 2565,
	new Integer[] {CrystalAddr.introScene26Addr, CrystalAddr.readJoypadAddr},
	new Integer[] {NO_INPUT, START},
	new Integer[] {0, 1});

    private static Strat introwait =
	new Strat("_introwait", 2827,
	new Integer[] {CrystalAddr.titleScreenAddr},
	new Integer[] {NO_INPUT},
	new Integer[] {0});

    private static Strat titleSkip =
	new Strat("", 54,
	new Integer[] {CrystalAddr.readJoypadAddr},
	new Integer[] {START},
	new Integer[] {1});

    // private static Strat titleUsb =
	// new Strat("_title(usb)", 54,
	// new Integer[] {CrystalAddr.readJoypadAddr},
	// new Integer[] {UP | SELECT | B},
	// new Integer[] {1});

    // private static Strat csCancelA =
	// new Strat("_cscancel(a)", 69,
	// new Integer[] {CrystalAddr.printLetterDelayAddr, CrystalAddr.noYesBoxAddr, CrystalAddr.readJoypadAddr},
	// new Integer[] {B, B, A},
	// new Integer[] {0, 0, 1});

    // private static Strat csCancelB =
	// new Strat("_cscancel(b)", 69,
	// new Integer[] {CrystalAddr.printLetterDelayAddr, CrystalAddr.noYesBoxAddr, CrystalAddr.readJoypadAddr},
	// new Integer[] {A, A, B},
	// new Integer[] {0, 0, 1});
    
    private static Strat reset =
	new Strat("_reset", 47,
	new Integer[] {CrystalAddr.initAddr},
	new Integer[] {START | SELECT | A | B},
	new Integer[] {17});

    private static Strat newGame =
	new Strat("_newgame", 8,
	new Integer[] {CrystalAddr.readJoypadAddr, CrystalAddr.postLID}, // TID is already genned
	new Integer[] {A, NO_INPUT},
	new Integer[] {1, 0});

    private static Strat backout =
	new Strat("_backout", 44,
	new Integer[] {CrystalAddr.readJoypadAddr},
	new Integer[] {B},
	new Integer[] {1});

    private static Strat opt = 
    new Strat("(opt)", 95,
	new Integer[] {CrystalAddr.readJoypadAddr, CrystalAddr.readJoypadAddr},
	new Integer[] {A, START},
	new Integer[] {1, 1});

    private static Strat setopt = 
    new Strat("(setopt)", 96,
	new Integer[] {CrystalAddr.readJoypadAddr, CrystalAddr.readJoypadAddr},
	new Integer[] {LEFT | A, START},
	new Integer[] {1, 1});

    private static Strat wait = 
    new Strat("_wait", 4,
	new Integer[] {CrystalAddr.mainMenuJoypadAddr},
	new Integer[] {NO_INPUT},
	new Integer[] {1});

    private static Strat downwait = 
    new Strat("_wait", 4,
	new Integer[] {CrystalAddr.mainMenuJoypadAddr},
	new Integer[] {DOWN},
	new Integer[] {1});

    private static Strat setopt_frame = 
    new Strat("(setoptframe", 96,
	new Integer[] {CrystalAddr.readJoypadAddr, CrystalAddr.readJoypadAddr},
	new Integer[] {LEFT | A | DOWN, DOWN},
	new Integer[] {1, 31});

    private static Strat framewait = 
    new Strat("_wait", 3,
	new Integer[] {CrystalAddr.readJoypadAddr},
	new Integer[] {NO_INPUT},
	new Integer[] {1});

    private static Strat frame6backout = 
    new Strat("_type6)", 31,
	new Integer[] {CrystalAddr.readJoypadAddr, CrystalAddr.readJoypadAddr, CrystalAddr.readJoypadAddr, CrystalAddr.readJoypadAddr, CrystalAddr.readJoypadAddr, CrystalAddr.readJoypadAddr},
	new Integer[] {LEFT, NO_INPUT, LEFT, NO_INPUT, LEFT, START},
	new Integer[] {1, 1, 1, 1, 1, 1});

    private static List<Strat> intro = Arrays.asList(gfSkip, gfWait, intro0, intro2, intro4, intro6, intro10, intro12, intro14, intro16, intro18, intro25, introwait);

    private static List<String> waitOptions = Arrays.asList("", "(opt)", "(setopt)");

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
            String ret = "crystal";
            
            for(int i=0; i<this.size(); i++) {
                Strat s = this.get(i);
                if(s.name.equals(("_backout"))) {
                    int backoutCounter = 0;
                    while(s.name.equals("_backout")) {
                        backoutCounter += 1;
                        i += 2;
                        s = this.get(i);
                    }
                    ret += "_backout" + backoutCounter;
                }
                if(s.name.equals(("_reset"))) {
                    int resetCounter = 0;
                    while(s.name.equals("_reset")) {
                        resetCounter += 1;
                        i += 1;
                        s = this.get(i);
                    }
                    ret += "_reset" + resetCounter;                
                }
                ret += s.name;
            }
            return ret;
        }
        void execute(Gb gb, Map<String, byte[]> stateMap) {
            for(Strat s : this) {
                s.execute(gb); //Execute all remaining strats
            }
        }
        int cost() {
            return this.stream().mapToInt((Strat s) -> s.cost).sum();
        }
        @Override public int compareTo(IntroSequence o) {
            return this.cost() - o.cost();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if (!new File("roms").exists()) {
            new File("roms").mkdir();
            System.err.println("I need ROMs to simulate!");
            System.exit(0);
        }

        File file = new File(FILE_NAME);
        PrintWriter writer = new PrintWriter(file);

        int maxwaits = (MAX_COST - 54 - 8)/4;
        
        String pattern = "#,##0.#";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        // Init gambatte with 1 session
        Gb gb = new Gb();
        gb.showDisplay = false; //Change this to true for display or false (or comment out) for no display
        if(gb.showDisplay) {
        	gb.createDisplay(8);}
        gb.loadBios("roms/gbc_bios.bin");
        gb.loadRom("roms/pokecrystal.gbc", LoadFlags.DEFAULT_LOAD_FLAGS);
        gb.advanceToAddress(CrystalAddr.initAddr);

        
        int lastCost = 0;
        int numLines = 0;
        int diffLines = 0;
        long elapsedTime = 0;
        long startTime = System.currentTimeMillis();
        DecimalFormat hoursFormat = new DecimalFormat("#0");
        DecimalFormat minsSecondsFormat = new DecimalFormat("00");
        
        byte[] resetState;
        byte[] backout1State;
        byte[] waitState;
        byte[] wait2State;
        byte[] backout2State;

        String stratName;
        int truecost;

        // Set catchup to true and set the values in the corresponding blocks below if resuming from an interruption
        boolean catchup = false;

        for (int resetCount = 0; resetCount <= MAX_RESETS; resetCount++) {
            if (catchup) {
                resetCount = 31;
                for (int i = 0; i < resetCount; i++) {
                    reset.execute(gb);
                }
            }
            resetState = gb.saveState();

            for (Strat introStrat : intro) {
                if (catchup) {
                    if (introStrat != gfSkip) {
                        continue;
                    }                    
                }
                introStrat.execute(gb);
                titleSkip.execute(gb);

                for (int backout1Count = 0; backout1Count <= (MAX_COST + 8); backout1Count++) {
                    if (catchup) {
                        backout1Count = 0;
                        for (int i = 0; i < backout1Count; i++) {
                            backout.execute(gb);
                            titleSkip.execute(gb);
                        }
                    }
                    backout1State = gb.saveState();

                    // Calculate the partial cost to check if it's over the max
                    truecost =  resetCount * reset.cost + 
                                introStrat.cost + titleSkip.cost +
                                (backout.cost + titleSkip.cost) * backout1Count +
                                newGame.cost;

                    if (truecost > MAX_COST) // Break in this case because it went too far
                        break;

                    for (int waitCount = 0; waitCount <= maxwaits; waitCount++) {
                        if (catchup) {
                            waitCount = 146;
                            for (int i = 0; i < waitCount-1; i++) { //One wait will be done in the opt section
                                wait.execute(gb);
                            }
                        }
                        waitState = gb.saveState();


                        // Calculate the partial cost to check if it's over the max
                        truecost =  resetCount * reset.cost + 
                                    introStrat.cost + titleSkip.cost +
                                    (backout.cost + titleSkip.cost) * backout1Count +
                                    waitCount * 4 +
                                    newGame.cost;

                        if (truecost > MAX_COST) // Break in this case because it went too far
                            break;

                        if (frameStrats == 0) {
                            for (String optStrat : waitOptions) {
                                if (catchup) {
                                    if (!optStrat.equals("(opt)")) {
                                        continue;
                                    }                    
                                }
                                if (optStrat.equals("(opt)")) {
                                    if (waitCount == 0) { //Invalid strat, can't do opt without a wait
                                        break;
                                    }
                                    downwait.execute(gb);
                                    opt.execute(gb);
                                }
                                else if (optStrat.equals("(setopt)")) {
                                    if (waitCount == 0) { //Invalid strat, can't do opt without a wait
                                        break;
                                    }
                                    downwait.execute(gb);
                                    setopt.execute(gb);
                                }
                                else if (waitCount > 0) { //still need to do a wait if waitcount is 1 or higher
                                    wait.execute(gb);
                                }

                                for (int backout2Count = 0; backout2Count <= (MAX_COST + 8); backout2Count++) {
                                    if (catchup) {
                                        backout2Count = 18;
                                        for (int i = 0; i < backout2Count; i++) {
                                            backout.execute(gb);
                                            titleSkip.execute(gb);
                                        }
                                        catchup = false;
                                    }
                                    backout2State = gb.saveState();

                                    newGame.execute(gb);

                                    stratName = "crystal" + 
                                                (resetCount==0 ? "": "_reset" + resetCount) + 
                                                (introStrat.name) + 
                                                (backout1Count==0 ? "": "_backout" + backout1Count) + 
                                                (waitCount==0 ? "": "_wait" + waitCount) +
                                                optStrat +
                                                (backout2Count==0 ? "": "_backout" + backout2Count) +
                                                "_newgame";

                                    truecost = resetCount * reset.cost + 
                                            introStrat.cost + titleSkip.cost +
                                            (backout.cost + titleSkip.cost) * backout1Count +
                                            waitCount * 4 +
                                            //opt strat added next
                                            (backout.cost + titleSkip.cost) * backout2Count +
                                            newGame.cost;

                                    if (optStrat.equals("(opt)")) {
                                        truecost += opt.cost;
                                    }

                                    if (optStrat.equals("(setopt)")) {
                                        truecost += setopt.cost;
                                    }

                                    if (truecost < START_COST) { // Continue in this case because it needs a longer manip to meet cost
                                        continue;
                                    }

                                    if (truecost > MAX_COST) { // Break in this case because it went too far
                                        break;
                                    }

                                    numLines += 1;
                                    diffLines += 1;
                                    
                                    int tid = readTID(gb);
                                    int lid = readLID(gb);
                                    writer.println( stratName
                                                    + ": TID = " + String.format("0x%4s", Integer.toHexString(tid).toUpperCase()).replace(' ', '0') + " (" + String.format("%5s)", tid).replace(' ', '0')
                                                    + ", LID = " + String.format("0x%4s", Integer.toHexString(lid).toUpperCase()).replace(' ', '0') + " (" + String.format("%5s)", lid).replace(' ', '0')
                                                    + ", Cost: " + String.format("%.03f", (gb.getGbpTime() - 0.21)));
                                                    
                                    writer.flush();
                                    if(numLines % 10000 == 0) {
                                        lastCost = truecost;
                                        elapsedTime = System.currentTimeMillis() - startTime;
                                        startTime = System.currentTimeMillis();
                                        System.out.println("Current cost: " + decimalFormat.format(lastCost) +
                                                            ", total sequences: " + decimalFormat.format(numLines) +
                                                            ", sequences since last update: " + decimalFormat.format(diffLines) +
                                                            ", time since last update: " + hoursFormat.format(TimeUnit.MILLISECONDS.toHours(elapsedTime)) +
                                                            ":" + minsSecondsFormat.format(TimeUnit.MILLISECONDS.toMinutes(elapsedTime)%60) +
                                                            ":" + minsSecondsFormat.format(TimeUnit.MILLISECONDS.toSeconds(elapsedTime)%60) +
                                                            ", current rate: " + decimalFormat.format(diffLines/(elapsedTime/3600000.0)) + " sequences per hour");
                                        diffLines = 0;
                                    }
                
                                    if (backout2Count + 1 <= (MAX_COST + 8)) {
                                        gb.loadState(backout2State);
                                        backout.execute(gb);
                                        titleSkip.execute(gb);
                                    }
                                }

                                gb.loadState(waitState);
                            }
                        }
                        else if (frameStrats == 6) {
                            if (waitCount == 0) { //Invalid strat, can't do opt without a wait
                                continue;
                            }
                            downwait.execute(gb);
                            setopt_frame.execute(gb);

                            for (int wait2Count = 1; wait2Count <= maxwaits; wait2Count++) {
                                if (catchup) {
                                    wait2Count = 146;
                                
                                    for (int i = 0; i < waitCount-1; i++) { //One wait will be done in the opt section
                                        framewait.execute(gb);
                                    }
                                }
                                wait2State = gb.saveState();
                                
                                // Calculate the partial cost to check if it's over the max
                                truecost =  resetCount * reset.cost + 
                                introStrat.cost + titleSkip.cost +
                                (backout.cost + titleSkip.cost) * backout1Count +
                                waitCount * 4 +
                                setopt_frame.cost +
                                wait2Count * 3 +
                                frame6backout.cost +
                                newGame.cost;

                                if (truecost > MAX_COST) { // Break in this case because it went too far
                                    break;
                                }

                                framewait.execute(gb);
                                frame6backout.execute(gb);

                                for (int backout2Count = 0; backout2Count <= (MAX_COST + 8); backout2Count++) {
                                    if (catchup) {
                                        backout2Count = 18;
                                        for (int i = 0; i < backout2Count; i++) {
                                            backout.execute(gb);
                                            titleSkip.execute(gb);
                                        }
                                        catchup = false;
                                    }
                                    backout2State = gb.saveState();

                                    newGame.execute(gb);

                                    stratName = "crystal" + 
                                                (resetCount==0 ? "": "_reset" + resetCount) + 
                                                (introStrat.name) + 
                                                (backout1Count==0 ? "": "_backout" + backout1Count) + 
                                                (waitCount==0 ? "": "_wait" + waitCount) +
                                                ("(setoptframe") +
                                                ("_wait" + wait2Count) +
                                                ("_type6)") +
                                                (backout2Count==0 ? "": "_backout" + backout2Count) +
                                                "_newgame";

                                    truecost = resetCount * reset.cost + 
                                            introStrat.cost + titleSkip.cost +
                                            (backout.cost + titleSkip.cost) * backout1Count +
                                            waitCount * 4 +
                                            setopt_frame.cost +
                                            wait2Count * 3 +
                                            frame6backout.cost +
                                            (backout.cost + titleSkip.cost) * backout2Count +
                                            newGame.cost;

                                    if (truecost < START_COST) { // Continue in this case because it needs a longer manip to meet cost
                                        continue;
                                    }

                                    if (truecost > MAX_COST) { // Break in this case because it went too far
                                        break;
                                    }

                                    numLines += 1;
                                    diffLines += 1;
                                    
                                    int tid = readTID(gb);
                                    int lid = readLID(gb);
                                    writer.println( stratName
                                                    + ": TID = " + String.format("0x%4s", Integer.toHexString(tid).toUpperCase()).replace(' ', '0') + " (" + String.format("%5s)", tid).replace(' ', '0')
                                                    + ", LID = " + String.format("0x%4s", Integer.toHexString(lid).toUpperCase()).replace(' ', '0') + " (" + String.format("%5s)", lid).replace(' ', '0')
                                                    + ", Cost: " + String.format("%.03f", (gb.getGbpTime() - 0.21)));
                                                    
                                    writer.flush();
                                    if(numLines % 100000 == 0) {
                                        lastCost = truecost;
                                        elapsedTime = System.currentTimeMillis() - startTime;
                                        startTime = System.currentTimeMillis();
                                        System.out.println("Current cost: " + decimalFormat.format(lastCost) +
                                                            ", total sequences: " + decimalFormat.format(numLines) +
                                                            ", sequences since last update: " + decimalFormat.format(diffLines) +
                                                            ", time since last update: " + hoursFormat.format(TimeUnit.MILLISECONDS.toHours(elapsedTime)) +
                                                            ":" + minsSecondsFormat.format(TimeUnit.MILLISECONDS.toMinutes(elapsedTime)%60) +
                                                            ":" + minsSecondsFormat.format(TimeUnit.MILLISECONDS.toSeconds(elapsedTime)%60) +
                                                            ", current rate: " + decimalFormat.format(diffLines/(elapsedTime/3600000.0)) + " sequences per hour");
                                        diffLines = 0;
                                    }
                
                                    if (backout2Count + 1 <= (MAX_COST + 8)) {
                                        gb.loadState(backout2State);
                                        backout.execute(gb);
                                        titleSkip.execute(gb);
                                    }
                                }


                                




                                if (wait2Count + 1 <= maxwaits && waitCount != 0) { // One wait will always happen with opt strats
                                    gb.loadState(wait2State);
                                    framewait.execute(gb);
                                }
                                                       



                            }

                            gb.loadState(waitState);


                        }

                        if (waitCount + 1 <= maxwaits && waitCount != 0) { // One wait will always happen with opt strats
                            gb.loadState(waitState);
                            wait.execute(gb);
                        }
                    }

                    if (backout1Count + 1 <= (MAX_COST + 8)) {
                        gb.loadState(backout1State);
                        backout.execute(gb);
                        titleSkip.execute(gb);
                    }
                }

                gb.loadState(resetState);
            }

            if (resetCount + 1 <= MAX_RESETS) {
                gb.loadState(resetState);
                reset.execute(gb);
            }


            
        }
        
        writer.close();
    }

    private static int readTID(Gb gb) {
        return (gb.readMemory(0xD47B) << 8) | gb.readMemory(0xD47C);
    }

    private static int readLID(Gb gb) {
        return (gb.readMemory(0xDC9F) << 8) | gb.readMemory(0xDCA0);
    }
}
