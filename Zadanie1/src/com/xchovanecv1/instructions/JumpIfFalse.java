package com.xchovanecv1.instructions;

import com.xchovanecv1.Enviroment;
import com.xchovanecv1.exceptions.InvalidNumericValue;

public class JumpIfFalse extends Instruction{
    public JumpIfFalse(String line) {
        super(line);
    }

    public void exec(Enviroment env) {
        super.exec(env);
        String op1 = this.ops.get(1);
        String op2 = this.ops.get(2);
        try {
            Integer val1 = env.resolveParameterValue(op1);
            Integer val2 = env.resolveParameterValue(op2);
            if(val1 == 0) {
                env.setExecLine(val2);
            }
        } catch (InvalidNumericValue invalidNumericValue) {

        }
    }
}
