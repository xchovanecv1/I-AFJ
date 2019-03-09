package com.xchovanecv1;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        Graph NKA = new Graph();

        NKA.parseInputFile(args[0]);

        NKA.representGraph();

        System.out.println(NKA.run("aaaaa"));
    }
}
