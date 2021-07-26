/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compi;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 *
 * @author Luis Cota
 */
public class CreateCode {
    
    String masVars = "";
    String concatenado = "";
    List<PolishItem> listVars = new ArrayList<>();
    
    private Variable createVariable(String variable, int tipo){
        Variable Var = new Variable();
        Var.setVariable(variable);
        Var.setTipo(tipo);
        return Var;
    }
    
    private PolishItem createVarsItem(String variable, String texto){
        PolishItem Var = new PolishItem();
        Var.setTexto(variable);
        Var.setTicket(texto);
        Var.setTipo("");
        return Var;
    }
    
    Boolean checarString(List<Variable> list, String v){
        for(Variable var : list){
            if(v.equals(var.getVariable())){
                if(var.getTipo() == 214) return true;
            }
        }
        return false;
    }
    
    String getStringVal(String v){
        for(PolishItem var : listVars){
            if(v.equals(var.getTexto())){
                return var.getTicket();
            }
        }
        return "";
    }
    
    Boolean existStringVal(String v){
        for(PolishItem var : listVars){
            if(v.equals(var.getTexto())){
                return true;
            }
        }
        return false;
    }
    
    String putVariables(List<Variable> list, String text){
        String complete = text;
        for(Variable var : list){
            if(var.getTipo() == 214){
                complete += "\t\t\t"+var.getVariable()+" LABEL BYTE"+"\n";
            }
            else{
                complete += "\t\t\t"+var.getVariable()+" dw "+"?"+"\n";
            }
        }
        return complete;
    }
    
    String makeCode(List<PolishItem> list, String text, List<Variable> list2){
        masVars = "";
        concatenado = "";
        listVars.clear();
        boolean paso = false;
        int contador = 1;
        String complete = text;
        Deque<Variable> Pila = new ArrayDeque<>();
        for(PolishItem pol : list){
            if(pol.getTicket().equals("")){
                switch(pol.getTipo()){
                    case "numero":
                    case "variable":
                        Pila.push(createVariable(pol.getTexto(), 0));
                        break;
                    case "cadena":
                        Pila.push(createVariable(pol.getTexto(), 1));
                        break;
                    case "booleano":
                        if(pol.getTexto().equals("true")){
                            Pila.push(createVariable("1", 0));
                        }
                        else{
                            Pila.push(createVariable("0", 0));
                        }
                        break;
                    case "operador":
                        if(pol.getTexto().equals("!")){
                            if(Pila.peek().getVariable().equals("1")){
                                Pila.pop();
                                Pila.push(createVariable("0", 0));
                            }
                            else if(Pila.peek().getVariable().equals("0")){
                                Pila.pop();
                                Pila.push(createVariable("1", 0));
                            }
                            else{
                                complete += "NOTVAR " + Pila.peek().getVariable() + "\n";
                            }
                        }
                        else if(pol.getTexto().equals("@")){
                            String s = Pila.pop().getVariable();
                            if(s.startsWith("-")){
                                Pila.push(createVariable(s.substring(1), 0));
                            }
                            else{
                                Pila.push(createVariable("-" + s, 0));
                            }
                        }
                        else if(pol.getTexto().equals("Print")){
                            if(Pila.peek().getTipo() == 1){
                                complete += "PRINTN2 " + "'" + Pila.peek().getVariable().substring(1, Pila.pop().getVariable().length() - 1) + "'\n";
                            }
                            else{
                                if(existStringVal(Pila.peek().getVariable())){
                                    complete += "PRINTN2 " + "'" + getStringVal(Pila.pop().getVariable()) + "'\n";
                                }
                                else{
                                    complete += "ITOA2 " + Pila.pop().getVariable() + "\n";
                                }
                            }
                        }
                        else if(pol.getTexto().equals("Scan")){
                            complete += "SCANN "+ Pila.pop().getVariable() + "\n";
                        }
                        else if(pol.getTexto().equals(":=")){
                            if(checarString(list2, Pila.peek().getVariable()) || Pila.peek().getTipo() == 1){
                                if(!paso){
                                    concatenado = Pila.peek().getVariable().substring(1, Pila.pop().getVariable().length() - 1);
                                    paso = false;
                                }
                                listVars.add(createVarsItem(Pila.peek().getVariable(), concatenado));
                                //complete += "PRINTN " + "'" + concatenado + "'" + "\n";
                                concatenado = "";
                            }
                            else{
                                String op2 = Pila.pop().getVariable();
                                String op1 = Pila.pop().getVariable();
                                complete += "I_ASIGNAR " + op1 + ", " + op2 + "\n";
                            }
                        }
                        else if(pol.getTexto().equals("+")){
                            Variable va = Pila.pop();
                            if(checarString(list2, va.getVariable())){
                                if(paso){
                                    concatenado += getStringVal(va.getVariable());
                                }
                                else{
                                    paso = true;
                                    String op2 = getStringVal(va.getVariable());
                                    String op1;
                                    if(checarString(list2, Pila.peek().getVariable())){
                                        op1 = getStringVal(Pila.pop().getVariable());
                                    }
                                    else{
                                        op1 = Pila.peek().getVariable().substring(1, Pila.pop().getVariable().length() - 1);
                                    }
                                    concatenado += "" + op1 + op2;
                                }
                            }
                            else{
                                Pila.push(va);
                                if(Pila.peek().getTipo() == 1){
                                    if(paso){
                                        concatenado += Pila.peek().getVariable().substring(1, Pila.pop().getVariable().length() - 1);
                                    }
                                    else{
                                        paso = true;
                                        String op2 = Pila.peek().getVariable().substring(1, Pila.pop().getVariable().length() - 1);
                                        String op1;
                                        if(checarString(list2, Pila.peek().getVariable())){
                                            op1 = getStringVal(Pila.pop().getVariable());
                                        }
                                        else{
                                            op1 = Pila.peek().getVariable().substring(1, Pila.pop().getVariable().length() - 1);
                                        }
                                        concatenado += "" + op1 + op2;
                                    }
                                }
                                else{
                                    String op2 = Pila.pop().getVariable();
                                    String op1 = Pila.pop().getVariable();
                                    complete += "SUMAR " + op1 + ", " + op2 + ", temp"+ contador +"\n";
                                    Pila.push(createVariable("temp" + contador, 0));
                                    masVars += "\t\t\t"+"temp"+contador+" dw "+"?"+"\n";
                                    contador++;
                                }
                            }
                        }
                        else{
                            String op2 = Pila.pop().getVariable();
                            String op1 = Pila.pop().getVariable();
                            String operacion = "";
                            switch(pol.getTexto()){
                                case "-":
                                    operacion = "RESTA ";
                                    break;
                                case "*":
                                    operacion = "MULTI ";
                                    break;
                                case "/":
                                    operacion = "DIVIDE ";
                                    break;
                                case "<":
                                    operacion = "I_MENOR ";
                                    break;
                                case "<=":
                                    operacion = "I_MENORIGUAL ";
                                    break;
                                case ">":
                                    operacion = "I_MAYOR ";
                                    break;
                                case ">=":
                                    operacion = "I_MAYORIGUAL ";
                                    break;
                                case "==":
                                    operacion = "I_IGUAL ";
                                    break;
                                case "!=":
                                    operacion = "I_DIFERENTES ";
                                    break;
                                case "&&":
                                    operacion = "ANDM ";
                                    break;
                                case "||":
                                    operacion = "ORM ";
                                    break;
                            }
                            complete += operacion + op1 + ", " + op2 + ", temp"+ contador +"\n";
                            Pila.push(createVariable("temp" + contador, 0));
                            masVars += "\t\t\t"+"temp"+contador+" dw "+"?"+"\n";
                            contador++;
                        }
                    break;
                    case "brincoF":
                        complete += "JF " + Pila.pop().getVariable() + ", " + pol.getTexto().substring(4) + "\n";
                        break;
                    case "brincoI":
                        complete += "JMP " + pol.getTexto().substring(4) + "\n";
                        break;
                }
            }
            else{
                complete += pol.getTicket() + ":\n";
            }
        }
        return complete;
    }
    
}
