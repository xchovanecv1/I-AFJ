package com.xchovanecv1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class State {

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
                if(isAcceptable()) {
                    System.out.println("["+ name +"] ---[ "+ entry.getKey() +" ] ---> "+ s.getName());
                } else {
                    System.out.println(" "+ name + "  ---[ " + entry.getKey() + " ] ---> " + s.getName());
                }
            }
        }
    }

    public State nextState(Character ch) {
        ArrayList<State> bf = nextState.getOrDefault(ch, null);
        if(bf == null) return null;

        Random random = new Random(System.currentTimeMillis());
        int rnd = random.nextInt(bf.size());
        System.out.println("Rnd pos: "+rnd);

        return bf.get(rnd);
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
}
