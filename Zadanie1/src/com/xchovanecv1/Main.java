package com.xchovanecv1;

import java.io.*;

//TODO Zistit čo pri vypise premennych ktore neboli definovane
public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println(args[0]);
        //InputStream input = Main.class.getResourceAsStream(args[0]);
        Runtime prg = new Runtime(args[0]);

        prg.parseInputProgram();

        prg.runInstance();
/*
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter a string: ");
        String s = null;
        try {
            s = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("You entered " + s);*/

    }
}
