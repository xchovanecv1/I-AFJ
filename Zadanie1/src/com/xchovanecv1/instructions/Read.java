package com.xchovanecv1.instructions;

import com.xchovanecv1.Enviroment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Read extends Instruction{

    public Read(String line) {
        super(line);
    }

    public void exec(Enviroment env) {
        super.exec(env);
        String var_name = this.ops.get(1);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Zadajte hodnotu premennej "+ var_name +": ");

        String s = null;
        Integer val = 0;
        try {
            s = br.readLine();
            val = Integer.parseInt(s);
        } catch (IOException e) {
            e.printStackTrace();
            env.HALT();
        } catch (NumberFormatException e) {
            env.error("Zadana hodnota nema spravny format!");
        }
        env.var_SetOrCreate(var_name, val);
        //env.listVariables();
    }
}
