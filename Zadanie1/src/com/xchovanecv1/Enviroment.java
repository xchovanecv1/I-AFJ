package com.xchovanecv1;

import com.xchovanecv1.exceptions.InvalidNumericValue;
import com.xchovanecv1.exceptions.VariableNonExists;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Enviroment {

    private Integer SP;
    private HashMap<String, Integer> variables;
    private boolean halt = false;

    public Enviroment() {
        this.clear();
    }

    public void clear() {
        this.setSP(0);
        this.variables = new HashMap<>();
    }

    public boolean isRunnable(Integer in_count) {
        if(this.halt) return false;
        if(this.getSP() < 0 || this.getSP() >= in_count) return false;
        return true;
    }

    public void var_SetOrCreate(String name, Integer value) {
        variables.put(name, value);
    }

    public Integer var_get(String name) {
        return this.variables.get(name);
    }

    public boolean var_exists(String name) {
        return this.variables.get(name) == null ? false : true;
    }

    public Integer resolveParameterValue(String in) throws InvalidNumericValue {
        // Check if in is valid variable name
        int chrvl = in.charAt(0);
        if((chrvl >= 'A' && chrvl <= 'Z') || (chrvl >= 'a' && chrvl <= 'z')) {
            // Check if variable exists
            if(this.var_exists(in)) {
                return this.var_get(in);
            } else {
                this.error("Premenna "+in+" neexistuje!");
            }
        } else {
            try {
                Integer val = Integer.parseInt(in);
                return val;
            } catch (NumberFormatException ex) {
                this.error("Hodnota "+in+" nema spravny ciselny format!");
                throw new InvalidNumericValue(in);
            }
        }
        return 0;
    }

    public void listVariables() {
        Iterator it = this.variables.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
        }
    }

    public Integer getSP() {
        return SP;
    }

    public void setSP(Integer SP) {
        this.SP = SP;
    }

    public void moveSP(Integer steps) {
        this.setSP(this.getSP() + steps);
    }

    public void error(String text) {
        System.out.println("[ERR:"+(this.getSP())+"] "+ text);
        this.halt = true;
    }
}
