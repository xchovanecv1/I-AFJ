package com.xchovanecv1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graph {

    State intial = null;
    HashMap<String, State> states = new HashMap<>();
    List<Character> abcd = new ArrayList<>();

    public void parseInputFile(String name) {
        int state_num   = 0;
        int char_num    = 0;

        int ln_num      = 0;

        try(BufferedReader br = new BufferedReader(new FileReader(name))) {
            for(String line; (line = br.readLine()) != null; ) {
                if(ln_num == 0) {
                    state_num = Integer.parseInt(line);
                } else if(ln_num == 1) {
                    char_num = Integer.parseInt(line);
                } else {
                    // TODO kontrola spravnosti state_num a char_num

                    int state_lim   = state_num + 2;
                    int abcd_lim    = state_lim + char_num;
                    // Spracovanie moznych stavov
                    if(ln_num > 1 && ln_num < state_lim) {
                        State state_bf = State.parseStateLine(line);
                        if(state_bf != null)
                        {
                            System.out.println(state_bf);
                            states.put(state_bf.getName(), state_bf);
                            if(state_bf.isInitial()) {
                                this.intial = state_bf;
                            }
                        } else {
                            this.error(line, "Neplatny format stavu");
                            break;

                        }
                    } else

                    // Spracovanie abecedy
                    if(ln_num >= state_lim && ln_num < abcd_lim) {
                        Character bf_char = line.charAt(0);
                        abcd.add(bf_char);
                    } else {
                        String[] parts = line.split(",");
                        if(parts.length == 3) {
                            Character trans = Character.MIN_VALUE;
                            if(parts[1].length() > 0) trans = parts[1].charAt(0);

                            if(trans != Character.MIN_VALUE && !abcd.contains(trans)) {
                                this.error(line, "Znak "+trans+" sa nenachadza v definovanej abecede "+ Arrays.toString(abcd.toArray()));
                            }

                            State from = states.get(parts[0]);
                            State to = states.get(parts[2]);

                            if(from == null) {
                                this.error(line, "Nedefinovany stav "+parts[0]);
                                break;
                            }
                            if(to == null) {
                                this.error(line, "Nedefinovany stav "+parts[2]);
                                break;
                            }

                            from.addConnection(trans, to);

                        } else {
                            this.error(line, "Neplatny format hrany");
                            break;
                        }
                        System.out.println(line);
                    }
                }
                ln_num++;
                //System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean run(String in) {

        State at = this.intial;
        for(int i=0; i < in.length(); i++) {
            Character c = in.charAt(i);
            if(at == null) return false;
            System.out.println(at.getName()+" - "+ c);
            if(abcd.contains(c)) {
                at = at.nextState(c);
            } else {
                this.error("Znak "+c+" nie je v abecede");
                return false;
            }
        }

        return at.isAcceptable();
    }

    public static void error(String line, String reason) {
        System.err.println("Nastala chyba pri spracovani vstupu:");
        System.err.println(line);
        System.err.println(reason);
    }
    public static void error(String reason) {
        System.err.println("Nastala chyba:");
        System.err.println(reason);
    }

    public void representGraph() {
        for (Map.Entry<String, State> entry : states.entrySet())
        {
            entry.getValue().printConnections();
        }
    }
}
