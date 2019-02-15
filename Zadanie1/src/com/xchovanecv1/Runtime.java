package com.xchovanecv1;

import com.xchovanecv1.exceptions.InvalidInstrucrtion;
import com.xchovanecv1.instructions.Instruction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Runtime {

    private String inFile;
    private List<Instruction> instructions;
    private Enviroment env;

    public Runtime(String filename) {
        this.setInFile(filename);
        instructions = new ArrayList<Instruction>();
        this.env = new Enviroment(this);
    }

    public void parseInputProgram() {
        Integer ln = 1;
        try(BufferedReader br = new BufferedReader(new FileReader(getInFile()))) {
            for(String line; (line = br.readLine()) != null; ) {
                Instruction bf = Instruction.createInstruction(line);
                instructions.add(bf);
                ln++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidInstrucrtion invalidInstrucrtion) {
            System.out.println("[ERR Line:"+ln+"] Nerozpoznana instrukcia: "+ invalidInstrucrtion.getMessage());
            env.HALT();
        }
    }

    public void runInstance() {
        while(env.isRunnable(instructions.size())) {
            instructions.get(this.env.getSP()).exec(this.env);
        }
    }

    public String getInFile() {
        return inFile;
    }

    public void setInFile(String inFile) {
        this.inFile = inFile;
    }

    public List<Instruction> getInstructions() {
        return this.instructions;
    }
}
