package com.xchovanecv1;

import java.io.*;
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
        if(at == null) return false;
        return at.isAcceptable();
    }

    public void saveToFile(String name) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(name));

        writer.write(""+states.size()+"\n");
        writer.write(""+abcd.size()+"\n");

        for (Map.Entry<String, State> entry : states.entrySet())
        {
            writer.write(entry.getValue().formatFileLine()+"\n");
        }
        for (Character ch : abcd)
        {
            writer.write(ch.charValue()+"\n");
        }
        for (Map.Entry<String, State> entry : states.entrySet())
        {
            for (Map.Entry<Character, ArrayList<State>> state_con : entry.getValue().getNextState().entrySet())
            {
                String chr = state_con.getKey().toString();
                if(state_con.getKey() == Character.MIN_VALUE) chr = "";
                for(State s: state_con.getValue()) {

                    writer.write(entry.getKey()+ ","+chr+","+s.getName()+"\n");
                }
            }
        }
        writer.close();
    }

    public static Set<State> eClosure(Set<State> in) {
        TreeSet<State> ret = new TreeSet<>();
        TreeSet<State> checked = new TreeSet<>();

        Queue<State> check = new LinkedList<>();

        ((LinkedList<State>) check).addAll(in);
        // Povodne stavy su zahrntue
        ret.addAll(in);
        State st;

        while((st = check.poll()) != null) {
            checked.add(st);
            List<State> toCheck = st.nextStates(Character.MIN_VALUE);
            if(toCheck == null) continue;
            for(State nw: toCheck) {
                if(!checked.contains(nw)) {
                    ret.add(nw);
                    check.add(nw);
                }
            }
        }

        return ret;
    }

    public Graph convertToDFA() {

        Graph fin = new Graph();
        fin.setABCD(this.abcd);

        List<Set<State>> states = new ArrayList<>();
        Queue<Set<State>> check = new LinkedList<>();

        Set<State> start= new TreeSet<>();
        start.add(intial);
        start = Graph.eClosure(start);
        check.add(start);

        State newInitial = State.concatState(start);
        newInitial.setInitial(true);
        fin.addState(newInitial);
        fin.setIntial(newInitial);

        states.add(start);
        Set<State> st;

        State woringState = null;
        while((st = check.poll()) != null)
        {
            //states.add(st);
            woringState = fin.getOrCreateState(st);

            for(Character c: abcd) {
                Set<State> ch_s = State.tranistStates(st, c);
                ch_s = Graph.eClosure(ch_s);
                if(ch_s == null || ch_s.isEmpty()) continue;
                State newState = fin.getOrCreateState(ch_s);
                System.out.println("Checkin: From:"+woringState.toString()+" ["+c+"] To:"+newState.toString());
                if(!states.contains(ch_s)) {
                    states.add(ch_s);
                    check.add(ch_s);
                }
                woringState.addConnection(c, newState);
            }
        }
        System.out.println("Final");
        for(Set<State> stat : states) {
            State.printStates(stat);
        }
        System.out.println("Final Automaton");

        fin.representGraph();

        return fin;
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

    public State getOrCreateState(Set<State> in) {
        String stateName = State.concatStateName(in);
        State ret;
        if(states.containsKey(stateName)){
            ret = states.get(stateName);
        } else {
            ret = State.concatState(in);
            this.addState(ret);
        }
        return ret;
    }

    public void addState(State in) {
        states.put(in.getName(), in);
    }

    public void setABCD(List<Character> in) {
        this.abcd.addAll(in);
    }

    public State getIntial() {
        return intial;
    }

    public void setIntial(State intial) {
        this.intial = intial;
    }
}
