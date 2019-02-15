package com.xchovanecv1.instructions;

import com.xchovanecv1.Enviroment;

import java.util.ArrayList;
import java.util.List;

public class Instruction {

    protected List<String> ops;

    public Instruction(String line) {
        this.ops = new ArrayList<>();
        String[] ops = line.split(",");
        for(String op: ops) {
            this.ops.add(op);
        }
    }

    public String toString() {
        String out = "OP: "+ ops.get(0);
        for(int i=1; i < ops.size(); i++) {
            out += ", " + ops.get(i);
        }
        return out;
    }

    public void exec(Enviroment env) {
        env.moveSP(1);
    }

    public static Instruction createInstruction(String line) {
        String[] ops = line.split(",");
        if(ops.length > 0) {
            if(ops[0].contains("READ")) {
                return new Read(line);
            }
            if(ops[0].contains("WRITE")) {
                return new Write(line);
            }
            if(ops[0].contains("NOP")) {
                return new NOP(line);
            }
            if(ops[0].contains("JUMPF")) {
                return new JumpIfFalse(line);
            }
            if(ops[0].contains("JUMPT")) {
                return new JumpIfTrue(line);
            }
            if(ops[0].contains("JUMP")) {
                return new Jump(line);
            }
            if(ops[0].contains("+")) {
                return new Sum(line);
            }
            if(ops[0].contains("-")) {
                return new Diff(line);
            }
            if(ops[0].contains("*")) {
                return new Multiply(line);
            }
            if(ops[0].contains("<=")) {
                return new LessOrEqual(line);
            }
            if(ops[0].contains("<")) {
                return new Less(line);
            }
            if(ops[0].contains(">=")) {
                return new MoreOrEqual(line);
            }
            if(ops[0].contains(">")) {
                return new More(line);
            }
            if(ops[0].contains("==")) {
                return new Equals(line);
            }
            if(ops[0].contains("=")) {
                return new Set(line);
            }
        }
        return new Instruction(line);
    }
}
