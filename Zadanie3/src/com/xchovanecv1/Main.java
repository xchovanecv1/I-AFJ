package com.xchovanecv1;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException {
        List<Graph> graphs = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            for(String line; (line = br.readLine()) != null; ) {
                String[] parts = line.split(",");
                if(parts.length == 1) {
                    Character ch = Character.MIN_VALUE;
                    if(!parts[0].isEmpty()) {
                        ch = parts[0].charAt(0);
                    }
                    graphs.add(Graph.createSingleton(ch));
                } else {
                    if(parts[0].contains("I")) {
                        int num = Integer.parseInt(parts[1]) -1;
                        Graph buf = graphs.get(num).iteration();
                        graphs.add(buf);
                    } else if(parts[0].contains("C")) {
                        int i = Integer.parseInt(parts[1]) - 1;
                        int j = Integer.parseInt(parts[2]) - 1;
                        Graph first = graphs.get(i);
                        Graph second = graphs.get(j);
                        Graph fin = first.concatenation(second);
                        graphs.add(fin);
                    } else if(parts[0].contains("U")) {
                        int i = Integer.parseInt(parts[1]) - 1;
                        int j = Integer.parseInt(parts[2]) - 1;
                        Graph first = graphs.get(i);
                        Graph second = graphs.get(j);
                        Graph fin = first.union(second);
                        graphs.add(fin);
                    }
                }
                System.out.println(parts.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        graphs.get(0).representGraph();
        Graph NKA = graphs.get(graphs.size()-1);

        Graph dka = NKA.convertToDFA();
        dka.normalizeNaming("q");

        System.out.println("-------------------");
        dka.representGraph();
        dka.saveToFile("output.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Zadajte vstupnu hodnotu pre automat: ");
        String s = null;
        try {
            s = br.readLine();
            boolean out = dka.run(s);
            if(out) {
                System.out.println("Automat slovo "+s+" AKCEPTUJE!");
            } else {
                System.out.println("Automat slovo "+s+" NEAKCEPTUJE!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

/*
        NKA.parseInputFile(args[0]);

        //NKA.representGraph();

        Graph DKA = NKA.convertToDFA();

        DKA.saveToFile(args[1]);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Zadajte vstupnu hodnotu pre automat: ");
        String s = null;
        try {
            s = br.readLine();
            boolean out = DKA.run(s);
            if(out) {
                System.out.println("Automat slovo "+s+" AKCEPTUJE!");
            } else {
                System.out.println("Automat slovo "+s+" NEAKCEPTUJE!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }
}
