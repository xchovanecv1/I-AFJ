package com.xchovanecv1;

import java.util.*;

public class State implements Comparable<State>{

    private String name;
    private boolean acceptable  = false;
    private boolean initial     = false;

    private Map<Character, ArrayList<State>> nextState = new HashMap<>();

    State(String name) {
        this.name = name;
    }

    public static State parseStateLine(String in) {
        String[] bf = in.split(" ");
        State ret = null;
        if(bf.length > 0) {
            ret = new State(bf[0]);
            if(bf.length == 2) {
                String params = bf[1];
                if(params.contains("I")) {
                    ret.setInitial(true);
                }
                if(params.contains("F")) {
                    ret.setAcceptable(true);
                }
            }
        }
        return ret;
    }

    public void addConnection(Character trans, State next) {
        ArrayList st_list;
        if(!nextState.containsKey(trans)) {
            st_list = new ArrayList<>();
            nextState.put(trans, st_list);
        } else {
            st_list = nextState.get(trans);
        }

        if(!st_list.contains(next)) {
            st_list.add(next);
        }
    }

    public void printConnections() {
        for (Map.Entry<Character, ArrayList<State>> entry : nextState.entrySet())
        {
            for(State s : entry.getValue()) {
                String state1 = " "+name+" ";
                if(isAcceptable()) state1 = "[" + name+"]";

                String state2 = " "+s.getName()+" ";
                if(s.isAcceptable()) state2 = "[" + s.getName()+"]";

                String chr = entry.getKey().toString();
                if(entry.getKey() == Character.MIN_VALUE) chr = " ";

                System.out.println(state1+" ---[ "+ chr +" ] ---> "+ state2);
            }
        }
    }

    public static void printStates(Set<State> in) {
        System.out.print("[ ");
        for(State s: in) {
            System.out.print(s.getName()+" ");
        }
        System.out.println("]");
    }

    public static Set<State> tranistStates(Set<State> in, Character trans) {
        Set<State> ret = new TreeSet<>();

        for(State s: in) {
            List<State> states = s.nextStates(trans);
            if(states == null) continue;

            ret.addAll(states);
        }
        return ret;
    }

    public State nextState(Character ch) {
        ArrayList<State> bf = nextState.getOrDefault(ch, null);
        if(bf == null) return null;

        Random random = new Random(System.currentTimeMillis());
        int rnd = random.nextInt(bf.size());
        System.out.println("Rnd pos: "+rnd);

        return bf.get(rnd);
    }

    public String formatFileLine() {
        String ret = name;
        String params = "";
        if (isInitial()) {
            params += "I";
        }
        if (isAcceptable()) {
            params += "F";
        }
        if (!params.isEmpty()) {
            ret += " " + params;
        }
        return ret;
    }

    public static String concatStateName(Set<State> in) {
        String nm = "";
        for(State s: in) {
            nm += s.getName();
        }
        return nm;
    }

    public static State concatState(Set<State> in) {
        String nm = "";
        boolean isFinal = false;
        for(State s: in) {
            nm += s.getName();
            if(s.isAcceptable()) isFinal = true;
        }
        State ret = new State(nm);
        ret.setAcceptable(isFinal);

        return ret;
    }

    public List<State> nextStates(Character ch) {
        return nextState.getOrDefault(ch, null);
    }

    @Override
    public String toString() {
        return "[State] "+name+" Initial: "+isInitial()+" Final:"+isAcceptable();
    }

    public boolean isAcceptable() {
        return acceptable;
    }

    public void setAcceptable(boolean acceptable) {
        this.acceptable = acceptable;
    }

    public boolean isInitial() {
        return initial;
    }

    public void setInitial(boolean initial) {
        this.initial = initial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Character, ArrayList<State>> getNextState() {
        return nextState;
    }

    public void setNextState(Map<Character, ArrayList<State>> nextState) {
        this.nextState = nextState;
    }

    @Override
    public int compareTo(State o) {
        return this.name.compareTo(o.getName());
    }
}
