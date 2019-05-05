package com.xchovanecv1;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Parser {

    String baseInput;
    Map<String, String> tokenMap = new HashMap<>();
    Queue<String> toCheck = new LinkedList<>();

    public int findCounterpart(String input, int pos) {
        int count = 1;
        for(int i = pos; i < input.length(); i++) {
            char chr = input.charAt(i);
            if(chr == '(') {
                count++;
            } else if(chr == ')') {
                count--;
            }
            if(count == 0) return i;
        }
        return -1;
    }

    public String tokenize(String regex) {
        String input = regex;
        int occr;
        while((occr = input.indexOf('(')) != -1) {
            int end = findCounterpart(input, occr+1);
            if(end != -1) {
                String cont = input.substring(occr+1, end);
                String tokenN = "#"+tokenMap.size()+"#";
                toCheck.add(tokenN);
                tokenMap.put(tokenN, cont);
                String prefix = "";
                String sufix = "";

                if(occr != 0) {
                    prefix = input.substring(0, occr);
                }
                if(end != input.length()-1) {
                    sufix = input.substring(end+1);
                }
                input = prefix + tokenN + sufix;
            } else {
                System.err.println("Chybne ozatvorkovanie");
                break;
            }
        }
        return input;
    }

    private int findEndTokenReverse(String input, int pos) {
        for(int i = pos-1; i >= 0; i--) {
            if(input.charAt(i) == '#') return i;
        }
        return -1;
    }
    private boolean isHarakter(char input) {
        if((input >= 'a' && input <= 'z') ||
                (input >= 'A' && input <= 'Z') ||
                (input == ' ')) {
            return true;
        }
        return false;
    }
    public Graph process() {
        return process(this.baseInput);
    }
    public Graph process(String input) {

        Queue<Graph> union = new LinkedList<Graph>();
        String proccessText = input;
        if(proccessText.length() > 0) {
            String[] parts = proccessText.split("\\|");
            boolean iterationHanging = false;
            for(int p = (parts.length-1); p >=0; p--) {
                Queue<Graph> Q = new LinkedList<Graph>();
                int pos = parts[p].length()-1;
                for(int i=pos; i >=0; i--) {
                    char harakter = parts[p].charAt(i);
                    Graph actual = null;
                    if(harakter == '#') {
                        int start = findEndTokenReverse(parts[p], i);
                        String tokenName = parts[p].substring(start, i+1);
                        i = start;
                        actual = process(tokenMap.get(tokenName));
                    } else if(isHarakter(harakter)) {
                        actual = Graph.createSingleton(harakter);
                    } else if(harakter == '*') {
                        iterationHanging = true;
                        continue;
                    }
                    if(iterationHanging) {
                        actual.representGraph();
                        actual = actual.iteration();
                        iterationHanging = false;
                    }
                    if(Q.size() > 0) {
                        Graph toConcat = Q.poll();
                        Q.add(actual.concatenation(toConcat));
                    } else {
                        Q.add(actual);
                    }
                }
                union.add(Q.poll());
            }
        }
        Graph ret = union.poll();
        Graph toUnion;
        while((toUnion = union.poll()) != null) {
            ret = ret.union(toUnion);
        }
        return ret;
    }

    public void parse(String input) {

        baseInput = tokenize(input);

        if(toCheck.size() > 0) {
            String tokenKey;
            while((tokenKey = toCheck.poll()) != null){
                String newVal = tokenize(tokenMap.get(tokenKey));
                tokenMap.put(tokenKey, newVal);
            }
        }
    }

}
