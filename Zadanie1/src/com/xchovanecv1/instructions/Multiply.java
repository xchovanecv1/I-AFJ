package com.xchovanecv1.instructions;

import com.xchovanecv1.Enviroment;
import com.xchovanecv1.exceptions.InvalidNumericValue;

public class Multiply extends Instruction {
    public Multiply(String line) {
        super(line);
    }

    public void exec(Enviroment env) {
        super.exec(env);
        String op1 = this.ops.get(1);
        String op2 = this.ops.get(2);
        String op3 = this.ops.get(3);
        try {
            Integer val1 = env.resolveParameterValue(op1);
            Integer val2 = env.resolveParameterValue(op2);

            Integer res = val1 * val2;

            env.var_SetOrCreate(op3, res);

        } catch (InvalidNumericValue invalidNumericValue) {

        }
    }
}
