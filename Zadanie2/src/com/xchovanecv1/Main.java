package com.xchovanecv1;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException {
        Graph NKA = new Graph();

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

    }
}
