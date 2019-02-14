package com.xchovanecv1.instructions;

import com.xchovanecv1.Enviroment;

public class Write extends Instruction{

    public Write(String line) {
        super(line);
    }

    public void exec(Enviroment env) {
        super.exec(env);
        String var_name = this.ops.get(1);
        if(env.var_exists(var_name)) {
            Integer val = env.var_get(var_name);
            System.out.println("Obsah premennej " + var_name + ": " + val);
        } else {
            env.error("Instrukcia WRITE, premenna "+var_name+" neexistuje!");
        }
    }
}
