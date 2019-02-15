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
    private Runtime pg;

    public Enviroment(Runtime pg) {
        this.pg = pg;
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
        if(this.var_validName(name)) {
            variables.put(name, value);
        } else {
            this.error("Hodnota "+name+" nie je vaidlne meno premennej!");
        }
    }

    public Integer var_get(String name) {
        return this.variables.get(name);
    }

    public boolean var_exists(String name) {
        return this.variables.get(name) == null ? false : true;
    }

    public boolean var_validName(String name) {
        int chrvl = name.charAt(0);
        if((chrvl >= 'A' && chrvl <= 'Z') || (chrvl >= 'a' && chrvl <= 'z')) {
            return true;
        }
        return false;
    }

    public Integer resolveParameterValue(String in) throws InvalidNumericValue {
        // Check if in is valid variable name
        if(var_validName(in)) {
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

    public void setExecLine(Integer SP) {
        if(SP > 0 && SP <= this.pg.getInstructions().size()) {
            this.SP = SP - 1;
        } else {
            this.error("Skok na neexistujuci riadok");
        }
    }

    public void moveSP(Integer steps) {
        this.setSP(this.getSP() + steps);
    }

    public void error(String text) {
        System.out.println("[ERR:"+(this.getSP())+"] "+ text);
        this.halt = true;
    }
}
