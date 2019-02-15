package com.xchovanecv1;

import java.io.*;

//TODO Prazdny riadok v kodovom subore, ignorovat / chybova hlaska
public class Main {

    public static void main(String[] args) {
        Runtime prg = new Runtime(args[0]);

        prg.parseInputProgram();

        prg.runInstance();

    }
}
