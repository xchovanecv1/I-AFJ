package com.xchovanecv1;

import java.io.*;
import java.util.*;

public class Graph {
    State intial = null;
    HashMap<String, State> states = new HashMap<>();
    Set<Character> abcd = new HashSet<>();
    int duplicated = 1;

    public void normalizeNaming(String prefix) {
        int count = 0;
        HashMap<String, State> newStates = new HashMap<>();

        for(HashMap.Entry<String, State> entry : states.entrySet()) {
            String name = prefix+""+count;
            entry.getValue().setName(name);
            newStates.put(name, entry.getValue());
            count++;
        }
        this.states = newStates;
    }

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
                            //System.out.println(state_bf);
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
                        //System.out.println(line);
                    }
                }
                ln_num++;
                //System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Graph iteration() {
        Graph out = this.duplicate();

        State newInitial = new State(out.duplicated+"_"+out.getIntial().getName());
        newInitial.setInitial(true);
        newInitial.setAcceptable(true);

        out.duplicated += 1;

        for (Map.Entry<String, State> entry : out.states.entrySet()) {
            if(entry.getValue().isAcceptable()) {
                entry.getValue().addConnection(Character.MIN_VALUE, out.getIntial());
            }
        }
        newInitial.addConnection(Character.MIN_VALUE, out.getIntial());
        out.addState(newInitial);

        out.getIntial().setInitial(false);
        out.setIntial(newInitial);
/*
        State newInitial = new State(out.duplicated+"_"+out.getIntial().getName());
        newInitial.setInitial(true);
        out.duplicated += 1;

        newInitial.addConnection(Character.MIN_VALUE, out.getIntial());
        newInitial.addConnection(Character.MIN_VALUE, con.getIntial());

        out.getIntial().setInitial(false);
        con.getIntial().setInitial(false);

        out.addState(newInitial);

        out.setIntial(newInitial);
*/
        return out;
    }

    public Graph union(Graph state) {
        Graph out = this.duplicate();
        Graph con = state.duplicate(out.duplicated+"");
        out.duplicated += 1;


        out.abcd.addAll(con.abcd);
        out.states.putAll(con.states);

        State newInitial = new State(out.duplicated+"_"+out.getIntial().getName());
        newInitial.setInitial(true);
        out.duplicated += 1;

        newInitial.addConnection(Character.MIN_VALUE, out.getIntial());
        newInitial.addConnection(Character.MIN_VALUE, con.getIntial());

        out.getIntial().setInitial(false);
        con.getIntial().setInitial(false);

        out.addState(newInitial);

        out.setIntial(newInitial);

        return out;
    }

    public Graph concatenation(Graph state) {
        Graph out = this.duplicate();
        Graph con = state.duplicate(out.duplicated+"");
        out.duplicated += 1;

        List<State> accStates = new ArrayList<>();
        for (Map.Entry<String, State> entry : out.states.entrySet()) {
            if(entry.getValue().isAcceptable()) {
                accStates.add(entry.getValue());
                entry.getValue().setAcceptable(false);
            }
        }

        out.abcd.addAll(con.abcd);
        out.states.putAll(con.states);

        for (State st : accStates) {
            st.addConnection(Character.MIN_VALUE, con.getIntial());
        }

        con.getIntial().setInitial(false);
        con.setIntial(null);

        return out;
    }


    public Graph duplicate() {
        return this.duplicate("");
    }
    public Graph duplicate(String prefix) {
        if(!prefix.isEmpty()) prefix = prefix + "_";
        Graph out = new Graph();

        out.setABCD(this.abcd);
        out.duplicated = this.duplicated;

        for (Map.Entry<String, State> entry : states.entrySet())
        {
            State st = entry.getValue().duplicate();
            st.setName(prefix + st.getName());

            if(this.intial == entry.getValue()) {
                out.setIntial(st);
            }
            out.addState(st);
        }

        for (Map.Entry<String, State> entry : states.entrySet())
        {
            State bf = entry.getValue();
            State newbf = out.getState(prefix + entry.getKey());

            for (Map.Entry<Character, ArrayList<State>> cons : bf.getNextState().entrySet()) {
                Character trans = cons.getKey();

                for(State stat : cons.getValue()) {
                    State conState = out.getState(prefix + stat.getName());

                    newbf.addConnection(trans, conState);
                }
            }
        }

        return out;
    }

    public Graph() {

    }

    public static Graph createSingleton(Character in) {
        Graph out = new Graph();
        State initial = new State("q0");
        initial.setInitial(true);

        if(in == null) {
            out.addState(initial);
        } else {
            if(in == ' ') {
                initial.setAcceptable(true);
            } else {
                State fin = new State("qf");
                out.addState(fin);
                fin.setAcceptable(true);
                initial.addConnection(in, fin);
                out.abcd.add(in);
            }
            out.addState(initial);
        }
        out.setIntial(initial);

        return out;
    }

    public State getState(String name) {
        return states.getOrDefault(name, null);
    }

    public boolean run(String in) {

        State at = this.intial;
        for(int i=0; i < in.length(); i++) {
            Character c = in.charAt(i);
            if(at == null) return false;

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
                //System.out.println("Checkin: From:"+woringState.toString()+" ["+c+"] To:"+newState.toString());
                if(!states.contains(ch_s)) {
                    states.add(ch_s);
                    check.add(ch_s);
                }
                woringState.addConnection(c, newState);
            }
        }
        /*System.out.println("Final");
        for(Set<State> stat : states) {
            State.printStates(stat);
        }
        System.out.println("Final Automaton");

        fin.representGraph();
*/
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

    public void representGraphStates() {
        for (Map.Entry<String, State> entry : states.entrySet())
        {
            System.out.println(entry.getValue().toString());
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

    public void setABCD(Set<Character> in) {
        this.abcd.addAll(in);
    }

    public State getIntial() {
        return intial;
    }

    public void setIntial(State intial) {
        this.intial = intial;
    }
}
