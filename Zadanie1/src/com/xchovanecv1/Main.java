package com.xchovanecv1;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        Runtime prg = new Runtime(args[0]);

        prg.parseInputProgram();

        prg.runInstance();

    }
}
