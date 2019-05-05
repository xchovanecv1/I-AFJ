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
        System.out.println("Input regex: "+input);
        int occr;
        while((occr = input.indexOf('(')) != -1) {
            int end = findCounterpart(input, occr+1);
            //System.out.println("From" + input +" Occured  " + occr + " End: " + end);
            if(end != -1) {
                String cont = input.substring(occr+1, end);
                String tokenN = "#"+tokenMap.size()+"#";
                System.out.println(occr);
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
                System.out.println("Prefix " + prefix + " Sufix: "+ sufix + " Final: " + input);

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
        System.out.println("Cicky " + proccessText);
        if(proccessText.length() > 0) {
            System.out.println("tu");
            String[] parts = proccessText.split("\\|");
            boolean iterationHanging = false;
            for(int p = (parts.length-1); p >=0; p--) {
                Queue<Graph> Q = new LinkedList<Graph>();
                System.err.println("Checkin "+p+ " Len" + (parts[p].length()-1));
                int pos = parts[p].length()-1;
                for(int i=pos; i >=0; i--) {
                    char harakter = parts[p].charAt(i);
                    Graph actual = null;
                    if(harakter == '#') {
                        int start = findEndTokenReverse(parts[p], i);
                        String tokenName = parts[p].substring(start, i+1);
                        System.out.println(tokenName);
                        i = start;
                        System.out.println("Titis "+ tokenName);
                        actual = process(tokenMap.get(tokenName));
                    } else if(isHarakter(harakter)) {
                        actual = Graph.createSingleton(harakter);
                    } else if(harakter == '*') {
                        System.err.println("Iteratin");
                        iterationHanging = true;
                        continue;
                        /*
                        Graph toIterate = Q.poll();
                        toIterate.representGraph();
                        actual = toIterate.iteration();*/
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
                    System.err.println(proccessText.charAt(i));
                }
                union.add(Q.poll());
            }
        }
        System.err.println("Union to " + union.size());
        Graph ret = union.poll();
        Graph toUnion;
        while((toUnion = union.poll()) != null) {
            ret = ret.union(toUnion);
        }
        ret.representGraph();
        return ret;
    }

    public void parse(String input) {

        baseInput = tokenize(input);

        if(toCheck.size() > 0) {
            String tokenKey;
            while((tokenKey = toCheck.poll()) != null){
                System.out.println("Inside");
                System.out.println(tokenKey);
                System.out.println(tokenMap.get(tokenKey));
                String newVal = tokenize(tokenMap.get(tokenKey));
                System.out.println(newVal);
                tokenMap.put(tokenKey, newVal);
            }
        }

        System.out.println("Zaciname: " + baseInput);

        for (Map.Entry<String, String> entry : tokenMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

}
