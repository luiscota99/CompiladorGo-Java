/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compi;

/**
 *
 * @author Luis Cota
 */
public class Variable {
 
    private String variable;
    private int tipo;
    
    public void setVariable(String variable){
        this.variable = variable;
    }
    
    public void setTipo(int tipo){
        this.tipo = tipo;
    }
    
    public String getVariable(){
        return variable;
    }
    
    public int getTipo(){
        return tipo;
    }
    
}
