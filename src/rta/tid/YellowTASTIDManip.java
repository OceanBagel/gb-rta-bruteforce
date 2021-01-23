package rta.tid;

import rta.YellowAddr;

import rta.gambatte.Gb;
import rta.gambatte.LoadFlags;

import java.io.*;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YellowTASTIDManip {
    private static final int NO_INPUT = 0x00;
    private static final int A = 0x01;;
    private static final int START = 0x08;

    /* Change this to increase/decrease number of intro sequence combinations processed */
    private static final int BASE_COST = 61 + 147 + 90 + 20 + 20;
    private static final int MAX_COST = BASE_COST + 100;

    
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

    static class IntroSequence extends ArrayList<Strat> implements Comparable<IntroSequence> {
        IntroSequence(Strat... strats) {
            super(Arrays.asList(strats));
        }
        IntroSequence(IntroSequence other) {
            super(other);
        }
        @Override public String toString() {
            String ret = "yellow";
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

    public static void main(String[] args) throws IOException, InterruptedException {
        if (!new File("roms").exists()) {
            new File("roms").mkdir();
            System.err.println("I need ROMs to simulate!");
            System.exit(0);
        }

        File file = new File("yellow_tas_tids.txt");
        PrintWriter writer = new PrintWriter(file);

        ArrayList<IntroSequence> newGameSequences = new ArrayList<>();
        
        int maxdelay = (MAX_COST-BASE_COST)/4;
        
        ArrayList<Strat> gfStrats = new ArrayList<>();
        for(int i=0; i<=maxdelay; i++) {
            Integer[] addr = new Integer[i+1];
            Integer[] input = new Integer[i+1];
            Integer[] advFrames = new Integer[i+1];
            for(int j=0; j<i; j++) {
                addr[j] = YellowAddr.joypadAddr;
                input[j] = NO_INPUT;
                advFrames[j] = 1;
            }
            addr[i] = YellowAddr.joypadAddr;
            input[i] = START;
            advFrames[i] = 1;
            gfStrats.add(new Strat("_gfskip" + i, 61+i, addr, input, advFrames));
        }
        
        ArrayList<Strat> introStrats = new ArrayList<>();
        for(int i=0; i<=maxdelay; i++) {
            Integer[] addr = new Integer[i+1];
            Integer[] input = new Integer[i+1];
            Integer[] advFrames = new Integer[i+1];
            for(int j=0; j<i; j++) {
                addr[j] = YellowAddr.joypadAddr;
                input[j] = NO_INPUT;
                advFrames[j] = 1;
            }
            addr[i] = YellowAddr.joypadAddr;
            input[i] = A;
            advFrames[i] = 1;
            introStrats.add(new Strat("_intro" + i, 147+i, addr, input, advFrames));
        }
        
        ArrayList<Strat> titleStrats = new ArrayList<>();
        for(int i=0; i<=maxdelay; i++) {
            Integer[] addr = new Integer[i+1];
            Integer[] input = new Integer[i+1];
            Integer[] advFrames = new Integer[i+1];
            for(int j=0; j<i; j++) {
                addr[j] = YellowAddr.joypadAddr;
                input[j] = NO_INPUT;
                advFrames[j] = 1;
            }
            addr[i] = YellowAddr.joypadAddr;
            input[i] = START;
            advFrames[i] = 1;
            titleStrats.add(new Strat("_title" + i, 90+i, addr, input, advFrames));
        }

        ArrayList<IntroSequence> s3seqs = new ArrayList<>();
            s3seqs.addAll(permute(gfStrats, introStrats));

        while(!s3seqs.isEmpty()) {
            ArrayList<IntroSequence> s4seqs = new ArrayList<>();
            for(IntroSequence s3 : s3seqs) {
            	s4seqs.addAll(permute(s3, titleStrats));
            }
            
            ArrayList<Strat> ngStrats = new ArrayList<>();
            for(int i=0; i<=maxdelay; i++) {
                Integer[] addr = new Integer[i+2];
                Integer[] input = new Integer[i+2];
                Integer[] advFrames = new Integer[i+2];
                for(int j=0; j<i; j++) {
                    addr[j] = YellowAddr.joypadAddr;
                    input[j] = NO_INPUT;
                    advFrames[j] = 1;
                }
                addr[i] = YellowAddr.joypadAddr;
                input[i] = A;
                advFrames[i] = 1;
                addr[i+1] = YellowAddr.postTIDAddr;
                input[i+1] = NO_INPUT;
                advFrames[i+1] = 0;
                ngStrats.add(new Strat("_newgame" + i, 90+i, addr, input, advFrames));
            }

            for(IntroSequence s4 : s4seqs) {
                newGameSequences.addAll(permute(s4, ngStrats));
            }
        }

        ArrayList<IntroSequence> introSequences = new ArrayList<>(newGameSequences);
        while(!newGameSequences.isEmpty()) {
            Collections.sort(newGameSequences);
        }
        
        System.out.println("Number of intro sequences: " + introSequences.size());
        Collections.sort(introSequences);

        // Init gambatte with 1 session
        // TODO: Parallelism?
	Gb gb = new Gb();
	gb.loadBios("roms/gbc_bios.bin");
	gb.loadRom("roms/pokeyellow.gbc",
	LoadFlags.DEFAULT_LOAD_FLAGS);
	gb.advanceToAddress(YellowAddr.initAddr);
	byte[] PostBios = gb.saveState();
        for(IntroSequence seq : introSequences) {
            seq.execute(gb);
            int tid = readTID(gb);
            writer.println(
                    seq.toString() + ": "
                            + String.format("0x%4s", Integer.toHexString(tid).toUpperCase()).replace(' ', '0')
                            + " (" + String.format("%5s)", tid).replace(' ', '0')
                            + ", Offset: " + String.format("%.02f", (gb.getGbpTime() - 0.47)));
            gb.loadState(PostBios);
            writer.flush();
            System.out.printf("Current Cost: %d%n", seq.cost());
        }
        writer.close();
    }

    private static int readTID(Gb gb) {
        return (gb.readMemory(0xD358) << 8) | gb.readMemory(0xD359);
    }
}
