package com.xchovanecv1.instructions;

import com.xchovanecv1.Enviroment;
import com.xchovanecv1.exceptions.InvalidNumericValue;

public class Jump extends Instruction{
    public Jump(String line) {
        super(line);
    }

    public void exec(Enviroment env) {
        super.exec(env);
        String op1 = this.ops.get(1);
        try {
            Integer val1 = env.resolveParameterValue(op1);
            env.setExecLine(val1);
        } catch (InvalidNumericValue invalidNumericValue) {

        }
    }
}
