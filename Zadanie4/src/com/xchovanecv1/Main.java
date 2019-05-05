package com.xchovanecv1;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Parser parser = new Parser();

        try(BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            String line = br.readLine();
            parser.parse(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Graph NKA = parser.process();
        Graph dka = NKA.convertToDFA();
        dka.normalizeNaming("q");
        dka.representGraph();
        dka.saveToFile(args[1]);

        System.out.println("-------------------");
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

    }
}
