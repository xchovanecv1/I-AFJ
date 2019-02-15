package com.xchovanecv1.instructions;

import com.xchovanecv1.Enviroment;
import com.xchovanecv1.exceptions.InvalidNumericValue;

public class Set extends Instruction{
    public Set(String line) {
        super(line);
    }

    public void exec(Enviroment env) {
        super.exec(env);
        String op1 = this.ops.get(1);
        String op2 = this.ops.get(2);
        try {
            Integer val2 = env.resolveParameterValue(op2);

            env.var_SetOrCreate(op1, val2);
        } catch (InvalidNumericValue invalidNumericValue) {

        }
    }
}
