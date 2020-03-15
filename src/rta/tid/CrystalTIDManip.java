package rta.tid;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import rta.CrystalAddr;
import rta.gambatte.Gb;
import rta.gambatte.LoadFlags;

public class CrystalTIDManip {
    private static final int NO_INPUT = 0x00;
    private static final int A = 0x01;
    private static final int B = 0x02;
    private static final int START = 0x08;

    // Change this to increase/decrease number of intro sequence combinations
    // processed
    private static final int MAX_COST = 3600;

    private static final int BASE_COST = 859;

    private static final int NUM_THREADS = 8;

    private static Strat gfSkip = new Strat("_gfskip", 0, new Integer[] { CrystalAddr.readJoypadAddr },
            new Integer[] { START }, new Integer[] { 1 });
    // private static Strat gfWait = new Strat("_gfwait", 384, new Integer[]
    // {CrystalAddr.introScene0Addr, CrystalAddr.readJoypadAddr}, new Integer[]
    // {NO_INPUT, START}, new Integer[] {0, 1});
    // private static Strat intro0 = new Strat("_intro0", 450, new Integer[]
    // {CrystalAddr.introScene1Addr, CrystalAddr.readJoypadAddr}, new Integer[]
    // {NO_INPUT, START}, new Integer[] {0, 1});
    private static Strat intro1 = new Strat("_intro1", 294,
            new Integer[] { CrystalAddr.introScene3Addr, CrystalAddr.readJoypadAddr },
            new Integer[] { NO_INPUT, START }, new Integer[] { 0, 1 });
    private static Strat intro2 = new Strat("_intro2", 407,
            new Integer[] { CrystalAddr.introScene4Addr, CrystalAddr.readJoypadAddr },
            new Integer[] { NO_INPUT, START }, new Integer[] { 0, 1 });
    private static Strat intro3 = new Strat("_intro3", 546,
            new Integer[] { CrystalAddr.introScene5Addr, CrystalAddr.readJoypadAddr },
            new Integer[] { NO_INPUT, START }, new Integer[] { 0, 1 });
    private static Strat intro4 = new Strat("_intro4", 883,
            new Integer[] { CrystalAddr.introScene9Addr, CrystalAddr.readJoypadAddr },
            new Integer[] { NO_INPUT, START }, new Integer[] { 0, 1 });
    private static Strat intro5 = new Strat("_intro5", 1042,
            new Integer[] { CrystalAddr.introScene11Addr, CrystalAddr.readJoypadAddr },
            new Integer[] { NO_INPUT, START }, new Integer[] { 0, 1 });
    private static Strat intro6 = new Strat("_intro6", 1215,
            new Integer[] { CrystalAddr.introScene13Addr, CrystalAddr.readJoypadAddr },
            new Integer[] { NO_INPUT, START }, new Integer[] { 0, 1 });
    // private static Strat intro16 = new Strat("_intro7", 2085, new Integer[]
    // {CrystalAddr.introScene17Addr, CrystalAddr.readJoypadAddr}, new Integer[]
    // {NO_INPUT, START}, new Integer[] {0, 1});
    // private static Strat intro18 = new Strat("_intro8", 2254, new Integer[]
    // {CrystalAddr.introScene19Addr, CrystalAddr.readJoypadAddr}, new Integer[]
    // {NO_INPUT, START}, new Integer[] {0, 1});
    // private static Strat intro25 = new Strat("_intro9", 2565, new Integer[]
    // {CrystalAddr.introScene26Addr, CrystalAddr.readJoypadAddr}, new Integer[]
    // {NO_INPUT, START}, new Integer[] {0, 1});
    private static Strat introwait = new Strat("_introwait", 5196, new Integer[] { CrystalAddr.titleScreenAddr },
            new Integer[] { NO_INPUT }, new Integer[] { 0 });

    // private static Strat titleSkip = new Strat("_title", 54, new Integer[]
    // {CrystalAddr.readJoypadAddr}, new Integer[] {START}, new Integer[] {1});
    private static Strat titleSkip = new Strat("", 52, new Integer[] { CrystalAddr.readJoypadAddr },
            new Integer[] { START }, new Integer[] { 1 });
    private static Strat newGame = new Strat("_newgame", 8, new Integer[] { CrystalAddr.readJoypadAddr },
            new Integer[] { A }, new Integer[] { 52 });
    private static Strat backout = new Strat("_backout", 57, new Integer[] { CrystalAddr.readJoypadAddr },
            new Integer[] { B }, new Integer[] { 1 });

    private static List<Strat> intro = Arrays.asList(gfSkip, intro1, intro2, intro3, intro4, intro5, intro6, introwait);

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
            for (int i = 0; i < addr.length; i++) {
                gb.advanceWithJoypadToAddress(input[i], addr[i]);
                for (int j = 0; j < advanceFrames[i]; j++) {
                    gb.advanceFrame(input[i]);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if (!new File("roms").exists()) {
            new File("roms").mkdir();
            System.err.println("I need ROMs to simulate!");
            System.exit(0);
        }

        File file = new File("crystal_clear_full_tids.txt");
        PrintWriter writer = new PrintWriter(file);

        // Init gambattes as required
        Gb[] gbs = new Gb[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            gbs[i] = new Gb();
            gbs[i].loadBios("roms/gbc_bios.bin");
            gbs[i].loadRom("roms/pokecrystal.gbc", LoadFlags.DEFAULT_LOAD_FLAGS);
            gbs[i].advanceToAddress(0x0383);
        }
        Gb gb = gbs[0];
        byte[] postBios = gb.saveState();

        // start with intros
        int numIntros = intro.size();
        byte[][] introStates = new byte[numIntros][];
        int[] baseCosts = new int[numIntros];

        for (int i = 0; i < intro.size(); i++) {
            Strat s = intro.get(i);
            baseCosts[i] = s.cost + titleSkip.cost + BASE_COST + 8;
            if (baseCosts[i] > MAX_COST) {
                numIntros = i;
                break;
            }
            gb.loadState(postBios);
            s.execute(gb);
            titleSkip.execute(gb);
            introStates[i] = gb.saveState();
        }

        System.out.println("saved " + numIntros + " intro states");

        // calc results count
        int numResults = 0;
        for (int introN = 0; introN < numIntros; introN++) {
            int maxBackouts = (MAX_COST - baseCosts[introN]) / 109;
            for (int bo = 0; bo <= maxBackouts; bo++) {
                int maxWait = (MAX_COST - baseCosts[introN] - bo * 109) / 4;
                for (int w = 0; w <= maxWait; w++) {
                    // do backout2 and advance now because of memory
                    int maxBo2s = (MAX_COST - baseCosts[introN] - bo * 109 - w * 4) / 109;
                    if (w == 0) {
                        maxBo2s = 0; // stop double dipping
                    }
                    numResults += maxBo2s + 1;
                }
            }
        }

        System.out.println("calculated " + numResults + " results are coming");

        // backouts
        byte[][][] backout1States = new byte[numIntros][][];
        int totalB1States = 0;
        for (int introN = 0; introN < numIntros; introN++) {
            int maxBackouts = (MAX_COST - baseCosts[introN]) / 109;
            backout1States[introN] = new byte[maxBackouts + 1][];
            backout1States[introN][0] = introStates[introN];
            gb.loadState(introStates[introN]);
            totalB1States += maxBackouts + 1;
            for (int bo = 0; bo < maxBackouts; bo++) {
                backout.execute(gb);
                titleSkip.execute(gb);
                backout1States[introN][bo + 1] = gb.saveState();
            }
        }
        introStates = null; // dealloc

        System.out.println("saved " + totalB1States + " backout 1 states");

        // waits
        Integer[] addr = new Integer[1];
        Integer[] input = new Integer[1];
        Integer[] advFrames = new Integer[1];
        addr[0] = CrystalAddr.mainMenuJoypadAddr;
        input[0] = NO_INPUT;
        advFrames[0] = 1;
        List<Result> allResults = new ArrayList<>();
        final Strat wait1 = new Strat("", 4, addr, input, advFrames);
        final int nR = numResults;

        boolean[] threadsRunning = new boolean[NUM_THREADS];
        for (int introN = 0; introN < numIntros; introN++) {
            int mbs = backout1States[introN].length;
            String introSeq = "crystal" + intro.get(introN).name;
            for (int bo = 0; bo < mbs; bo++) {
                int maxWait = (MAX_COST - baseCosts[introN] - bo * 109) / 4;
                // gb.loadState(backout1States[introN][bo]);

                String boSeq = bo > 0 ? introSeq + "_backout" + bo : introSeq;
                boolean started = false;
                while (!started) {
                    synchronized (threadsRunning) {
                        int threadIndex = -1;
                        for (int i = 0; i < NUM_THREADS; i++) {
                            if (!threadsRunning[i]) {
                                threadIndex = i;
                                break;
                            }
                        }
                        if (threadIndex >= 0) {
                            started = true;
                            final int num = threadIndex;
                            threadsRunning[threadIndex] = true;
                            final int iN = introN;
                            final int bO = bo;
                            System.out.println("starting " + boSeq + " on thread " + threadIndex);
                            Runnable run = () -> {
                                List<Result> results = new ArrayList<>();
                                Gb gbi = gbs[num];
                                byte[] baseState = backout1States[iN][bO];
                                gbi.loadState(baseState);
                                for (int w = 0; w <= maxWait; w++) {
                                    // do backout2 and advance now because of
                                    // memory
                                    int maxBo2s = (MAX_COST - baseCosts[iN] - bO * 109 - w * 4) / 109;
                                    if (w == 0) {
                                        maxBo2s = 0; // stop double dipping
                                    }
                                    byte[] baseBo2State = baseState;
                                    String waitSeq = w > 0 ? boSeq + "_wait" + w : boSeq;
                                    // System.out.printf("processing
                                    // intro+backout+wait %s with %d bo2s\n",
                                    // waitSeq, maxBo2s);
                                    for (int bo2 = 0; bo2 <= maxBo2s; bo2++) {
                                        newGame.execute(gbi);
                                        int tid = readTID(gbi);
                                        int lid = readLID(gbi);
                                        int cost = baseCosts[iN] + bO * 109 + w * 4 + bo2 * 109;
                                        String seq = bo2 > 0 ? waitSeq + "_backout" + bo2 : waitSeq;
                                        results.add(new Result(seq, cost, tid, lid));

                                        if (bo2 < maxBo2s) {
                                            gbi.loadState(baseBo2State);
                                            backout.execute(gbi);
                                            titleSkip.execute(gbi);
                                            baseBo2State = gbi.saveState();
                                        }
                                    }
                                    if (w < maxWait) {
                                        gbi.loadState(baseState);
                                        wait1.execute(gbi);
                                        baseState = gbi.saveState();
                                    }
                                }
                                synchronized (threadsRunning) {
                                    allResults.addAll(results);
                                    threadsRunning[num] = false;
                                    System.out.println(allResults.size() + "/" + nR);
                                }
                            };
                            new Thread(run).start();
                        }
                    }
                    if (!started) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }

        }

        int threadCount = 999;
        while (threadCount > 0) {
            synchronized (threadsRunning) {
                threadCount = 0;
                for (int i = 0; i < NUM_THREADS; i++) {
                    if (threadsRunning[i]) {
                        threadCount++;
                    }
                }
            }
            if (threadCount > 0) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
        }

        System.out.println("got " + allResults.size() + " results");
        allResults.sort(new Comparator<Result>() {

            @Override
            public int compare(Result o1, Result o2) {
                // TODO Auto-generated method stub
                return o1.cost - o2.cost;
            }
        });
        for (Result r : allResults) {
            writer.println(String.format("%s_newgame: TID = 0x%04X (%05d), LID = 0x%04X (%05d), Cost: %d", r.seq, r.tid,
                    r.tid, r.lid, r.lid, r.cost));
        }
        writer.close();
    }

    private static int readTID(Gb gb) {
        return (gb.readMemory(0xD47B) << 8) | gb.readMemory(0xD47C);
    }

    private static int readLID(Gb gb) {
        return (gb.readMemory(0xDC9F) << 8) | gb.readMemory(0xDCA0);
    }

    static class Result {
        String seq;
        int cost;
        int tid;
        int lid;

        public Result(String seq, int cost, int tid, int lid) {
            super();
            this.seq = seq;
            this.cost = cost;
            this.tid = tid;
            this.lid = lid;
        }
    }
}
