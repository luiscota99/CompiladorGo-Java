/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compi;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Luis Cota
 */
public class Sytaxis {
    
    private final int ID = 100, Entero = 101, Decimal = 102, PuntoComa = 104, Coma = 105, Parentecis1 = 107,
                      Parentecis2 = 108, Llave1 = 109, Llave2 = 110, Corchete1 = 111, Corchete2 = 112 ,Mas = 113, Menos = 114, Por = 115,
                      Entre = 116, Modulo = 117, IgualA = 118, DiferenteA = 119, MenorA = 120, MayorA = 121,
                      MenorIgualA = 122, MayorIgualA = 123, Igual = 124, Cadena = 125, And = 126, Or = 127, Not = 128, If = 200, Else = 201,
                      For = 202, Break = 203, Continue = 204, True = 205, False = 206, Scan = 207, Print = 208,
                      Package = 209, Main = 210, Func = 211, Var = 212, Int = 213, String = 214, Bool = 215, Float = 216, MenosU = 150;  
    
    
    String Error = "";
    String t = "";
    int currentIndex = 0;
    private boolean banderaBlock1Cases = false;  
    
    int contadorTIF = 1;
    int contadorTFOR = 1;
    
    Deque<String> Pila = new ArrayDeque<>(); 
    List<Variable> Variables = new ArrayList<>();
    List<Integer> PosFijo = new ArrayList<>();
    List<PolishItem> Polish = new ArrayList<>();
    //List<PolishItem> IfForList = new ArrayList<>();
    Deque<Integer> PilaOperador = new ArrayDeque<>();
    Deque<PolishItem> PilaPolOperador = new ArrayDeque<>();
    //Deque<Integer> PilaOperando = new ArrayDeque<>(); 
    
    void SyntaxisGO(List<Item> list){
        t = "";
        Error = "";
        currentIndex = 0;
        banderaBlock1Cases = false;
        Variables.clear();
        PosFijo.clear();
        Polish.clear();
        Pila.clear();
        contadorTIF = 1;
        contadorTFOR = 1;
        if(getItemTipo(currentIndex, list) == Package){
            currentIndex++;
            if(getItemTipo(currentIndex, list) == Main){
                //
                Block(list);
                //
                
            }
            else{
                Error = "Error de sintaxis 510:\n\rSe espera la palabra Main";
            }
        }
        else{
            Error = "Error de sintaxis 509:\n\rSe espera la palabra Package";
        }
    }
    
    private void Block(List<Item> list){
        currentIndex++;
        if(getItemTipo(currentIndex, list) == Func){
            currentIndex++;
            if(getItemTipo(currentIndex, list) == Main){
                currentIndex++;
                if(getItemTipo(currentIndex, list) == Parentecis1){
                    currentIndex++;
                    if(getItemTipo(currentIndex, list) == Parentecis2){
                        currentIndex++;
                        if(getItemTipo(currentIndex, list) == Llave1){
                            //
                            block1List(list);
                            //
                            if(Error.equals("")){
                                currentIndex++;
                                if(getItemTipo(currentIndex, list) == Llave2){
                                    currentIndex++;
                                    if(getItemTipo(currentIndex, list) != -1){
                                        Error = "Error de sintaxis 526:\n\rse espera fin de archivo";
                                    }
                                }
                                else{
                                    Error = "Error de sintaxis 525:\n\rse espera el simbolo }";
                                }
                            }
                        }
                        else{
                            Error = "Error de sintaxis 514:\n\rse espera el simbolo {";
                        }
                    }
                    else{
                        Error = "Error de sintaxis 513:\n\rSe espera el simbolo )";
                    }
                }
                else{
                    Error = "Error de sintaxis 512:\n\rSe espera el simbolo (";
                }
            }
            else{
                Error = "Error de sintaxis 510:\n\rSe espera la palabra main";
            }
        }
        else{
            Error = "Error de sintaxis 511:\n\rSe espera la palabra func";
        }
    }
    
    private void Block1(List<Item> list){
        currentIndex++;
        if(getItemTipo(currentIndex, list) == Var){
            banderaBlock1Cases = true;
            //
            VarDeclaration(list);
            //
        }
        else{
            switch(getItemTipo(currentIndex, list)){
                case If:
                case For:
                case Scan:
                case Print:
                case ID:
                    banderaBlock1Cases = true;
                    currentIndex--;
                    //
                    statementList(list);
                    //
                    break;
                default:
                    if(!banderaBlock1Cases) Error = "Error de sintaxis 515:\n\rFalta declaracion de variables o un statement";
                    break;
            }
        }
    }
    
    private void block1List(List<Item> list){
        //
        Block1(list);
        //
        if(Error.equals("")){
            currentIndex++;
            switch(getItemTipo(currentIndex, list)){
                case If:
                case For:
                case Scan:
                case Print:
                case ID:
                case Var:
                    currentIndex--;
                    //
                    block1List(list);
                    //
                    break;
                default:
                    currentIndex--;
            }
        }
    }
    
    private void VarDeclaration(List<Item> list){
        currentIndex++;
        if(getItemTipo(currentIndex, list) == ID){
            if(!checarVariable(list.get(currentIndex).getText())){
                Pila.push(list.get(currentIndex).getText());
            }
            else{
                Error = "Variable duplicada";
            }
            currentIndex++;
            if(getItemTipo(currentIndex, list) == Coma){
                //
                varIDList(list);
                //
                if(Error.equals("")){
                    currentIndex++;
                    if(getItemTipo(currentIndex, list) >= Int && getItemTipo(currentIndex, list) <= Float){
                        llenarVariables(list.get(currentIndex).getTipo()); 
                        currentIndex++;
                        if(getItemTipo(currentIndex, list) == PuntoComa){
                            currentIndex++;
                            if(getItemTipo(currentIndex, list) == Var){
                                //
                                VarDeclaration(list);
                                //
                            }
                            else{
                                currentIndex--;
                            }
                        }
                        else{
                            Error = "Error de sintaxis 519:\n\rSe espera el simbolo ;";
                        }
                    }
                    else{
                        Error = "Error de sintaxis 518:\n\rSe espera un Tipo";
                    }
                }
            }
            else if(getItemTipo(currentIndex, list) >= Int && getItemTipo(currentIndex, list) <= Float){
                llenarVariables(list.get(currentIndex).getTipo());
                currentIndex++;
                if(getItemTipo(currentIndex, list) == PuntoComa){
                    currentIndex++;
                    if(getItemTipo(currentIndex, list) == Var){
                        //
                        VarDeclaration(list);
                        //
                    }
                    else{
                        currentIndex--;
                    }
                }
                else{
                    Error = "Error de sintaxis 519:\n\rSe espera el simbolo ;";
                }
            }
            else{
                Error = "Error de sintaxis 518:\n\rSe espera un Tipo";
            }
        }
        else{
            Error = "Error de sintaxis 516:\n\rSe espera un ID";
        }
    }
    
    private void varIDList(List<Item> list){
        currentIndex++;
        if(getItemTipo(currentIndex, list) == ID){
            if(!checarVariable(list.get(currentIndex).getText())){
                for(String var : Pila){
                    if(list.get(currentIndex).getText().equals(var)){
                        Error = "Variable duplicada";
                    }
                }
                if(Error.equals("")){
                    Pila.push(list.get(currentIndex).getText());
                }
            }
            else{
                Error = "Variable duplicada";
            }
            currentIndex++;
            if(getItemTipo(currentIndex, list) == Coma){
                //
                varIDList(list);
                //
            }
            else{
                currentIndex--;
            }
        }
        else{
            Error = "Error de sintaxis 517:\n\rSe espera un ID";
        }
    }
    
    private void Statement(List<Item> list){
        currentIndex++;
        switch(getItemTipo(currentIndex, list)){
            case If:
                int currentTIF = contadorTIF;
                contadorTIF++;
                currentIndex++;
                if(getItemTipo(currentIndex, list) == Parentecis1){
                    PosFijo.clear();
                    //
                    LogicExpression(list);
                    //
                    if(Error.equals("")){
                        currentIndex++;
                        if(getItemTipo(currentIndex, list) == Parentecis2){
                            vaciarPilas();
                            PosFijo.add(Bool);
                            PosFijo.add(Igual);
                            if(!checarPosFijo()){
                                Error = "Tipos no compatibles";
                            }
                            Polish.add(createPolItem("BRF-A"+currentTIF, "", "brincoF"));
                            currentIndex++;
                            if(getItemTipo(currentIndex, list) == Llave1){
                                //
                                statementList(list);
                                //
                                if(Error.equals("")){
                                    Polish.add(createPolItem("BRI-B"+currentTIF, "", "brincoI"));
                                    currentIndex++;
                                    if(getItemTipo(currentIndex, list) == Llave2){
                                        Polish.add(createPolItem("", "A"+currentTIF, ""));
                                        currentIndex++;
                                        if(getItemTipo(currentIndex, list) == Else){
                                            currentIndex++;
                                            if(getItemTipo(currentIndex, list) == Llave1){
                                                //Polish.add(createPolItem("", "A"+currentTIF));
                                                //
                                                statementList(list);
                                                //
                                                if(Error.equals("")){
                                                    currentIndex++;
                                                    if(getItemTipo(currentIndex, list) != Llave2){
                                                        Error = "Error de sintaxis 525:\n\rse espera el simbolo }";
                                                    }
                                                    else{
                                                        Polish.add(createPolItem("", "B"+currentTIF, ""));
                                                    }
                                                }
                                            }
                                            else{
                                                Error = "Error de sintaxis 514:\n\rse espera el simbolo {";
                                            }
                                        }
                                        else{
                                            Polish.add(createPolItem("", "B"+currentTIF, ""));
                                            currentIndex--;
                                        }
                                    }
                                    else{
                                        Error = "Error de sintaxis 525:\n\rse espera el simbolo }";
                                    }
                                }
                            }
                            else{
                                Error = "Error de sintaxis 514:\n\rse espera el simbolo {";
                            }
                        }
                        else{
                            Error = "Error de sintaxis 513:\n\rSe espera el simbolo )";
                        }
                    }
                }
                else{
                    Error = "Error de sintaxis 512:\n\rSe espera el simbolo (";
                }
                break;
            case For:
                t = "";
                List<PolishItem> tempList = new ArrayList<>();
                int currentTFOR = contadorTFOR;
                contadorTFOR++;
                currentIndex++;
                if(getItemTipo(currentIndex, list) == Parentecis1){
                    currentIndex++;
                    if(getItemTipo(currentIndex, list) == ID){
                        if(!checarVariable(list.get(currentIndex).getText())){
                            Pila.push(list.get(currentIndex).getText());
                            llenarVariables(Int);
                            t = list.get(currentIndex).getText();
                            Polish.add(createPolItem(list.get(currentIndex).getText(), "", "variable"));
                        }
                        else{
                            Error = "Variable duplicada";
                        }
                        currentIndex++;
                        if(getItemTipo(currentIndex, list) == Igual){
                            currentIndex++;
                            if(getItemTipo(currentIndex, list) == Entero){
                                Polish.add(createPolItem(list.get(currentIndex).getText(), "", "numero"));
                                Polish.add(createPolItem(":=", "", "operador"));
                                currentIndex++;
                                if(getItemTipo(currentIndex, list) == PuntoComa){
                                    PosFijo.clear();
                                    Polish.add(createPolItem("", "H"+currentTFOR, ""));
                                    //
                                    LogicExpression(list);
                                    //
                                    if(Error.equals("")){
                                        currentIndex++;
                                        if(getItemTipo(currentIndex, list) == PuntoComa){
                                            vaciarPilas();
                                            PosFijo.add(Bool);
                                            PosFijo.add(Igual);
                                            if(!checarPosFijo()){
                                                Error = "Tipos no compatibles";
                                            }
                                            Polish.add(createPolItem("BRF-G"+currentTFOR,"", "brincoF"));
                                            currentIndex++;
                                            if(getItemTipo(currentIndex, list) == ID){
                                                if(!list.get(currentIndex).getText().equals(t)){
                                                    Error = "La variable debe de ser "+t;
                                                }
                                                tempList.add(createPolItem(list.get(currentIndex).getText(), "", "variable"));
                                                //Polish.add(createPolItem(list.get(currentIndex).getText(), ""));
                                                currentIndex++;
                                                if(getItemTipo(currentIndex, list) == Igual){
                                                    currentIndex++;
                                                    if(getItemTipo(currentIndex, list) == ID){
                                                        if(!list.get(currentIndex).getText().equals(t)){
                                                            Error = "La variable debe de ser "+t;
                                                        }
                                                        tempList.add(createPolItem(list.get(currentIndex).getText(), "", "variable"));
                                                        //Polish.add(createPolItem(list.get(currentIndex).getText(), ""));
                                                        currentIndex++;
                                                        if(getItemTipo(currentIndex, list) == Mas){
                                                            currentIndex++;
                                                            if(getItemTipo(currentIndex, list) == Entero){
                                                                tempList.add(createPolItem(list.get(currentIndex).getText(), "", "numero"));
                                                                tempList.add(createPolItem("+", "", "operador"));
                                                                tempList.add(createPolItem(":=", "", "operador"));
                                                                /*Polish.add(createPolItem(list.get(currentIndex).getText(), ""));
                                                                Polish.add(createPolItem("+", ""));
                                                                Polish.add(createPolItem(":=", ""));*/
                                                                currentIndex++;
                                                                if(getItemTipo(currentIndex, list) == Parentecis2){
                                                                    currentIndex++;
                                                                    if(getItemTipo(currentIndex, list) == Llave1){
                                                                        //
                                                                        forCasesList(list);
                                                                        //
                                                                        Polish.addAll(tempList);
                                                                        Polish.add(createPolItem("BRI-H"+currentTFOR, "", "brincoI"));
                                                                        if(Error.equals("")){
                                                                            currentIndex++;
                                                                            if(getItemTipo(currentIndex, list) != Llave2){
                                                                                Error = "Error de sintaxis 525:\n\rse espera el simbolo }";
                                                                            }
                                                                            else{
                                                                                Polish.add(createPolItem("", "G"+currentTFOR, ""));
                                                                            }
                                                                        }
                                                                    }
                                                                    else{
                                                                        Error = "Error de sintaxis 514:\n\rse espera el simbolo {";
                                                                    }
                                                                }
                                                                else{
                                                                    Error = "Error de sintaxis 513:\n\rSe espera el simbolo )";
                                                                }
                                                            }
                                                            else{
                                                                Error = "Error de sintaxis 521:\n\rSe espera un numero entero";
                                                            }
                                                        }
                                                        else{
                                                            Error = "Error de sintaxis 524:\n\rSe espera el simbolo +";
                                                        }
                                                    }
                                                    else{
                                                        Error = "Error de sintaxis 516:\n\rSe espera un ID";
                                                    }
                                                }
                                                else{
                                                    Error = "Error de sintaxis 522:\n\rSe espera el simbolo :=";
                                                }
                                            }
                                            else{
                                                Error = "Error de sintaxis 516:\n\rSe espera un ID";
                                            }
                                        }
                                        else{
                                            Error = "Error de sintaxis 519:\n\rSe espera el simbolo ;";
                                        }
                                    }
                                }
                                else{
                                    Error = "Error de sintaxis 519:\n\rSe espera el simbolo ;";
                                }
                            }
                            else{
                                Error = "Error de sintaxis 521:\n\rSe espera un numero entero";
                            }
                        }
                        else{
                            Error = "Error de sintaxis 522:\n\rSe espera el simbolo :=";
                        }
                    }
                    else{
                        Error = "Error de sintaxis 516:\n\rSe espera un ID";
                    }
                }
                else{
                    Error = "Error de sintaxis 512:\n\rSe espera el simbolo (";
                }
                break;
            case Scan:
                currentIndex++;
                if(getItemTipo(currentIndex, list) == Parentecis1){
                    currentIndex++;
                    if(getItemTipo(currentIndex, list) == ID){
                        if(!checarVariable(list.get(currentIndex).getText())){
                            Error = "Falta declaracion de la variable";
                        }
                        else{
                            Polish.add(createPolItem(list.get(currentIndex).getText(), "", "variable"));
                        }
                        currentIndex++;
                        if(getItemTipo(currentIndex, list) == Parentecis2){
                            currentIndex++;
                            if(getItemTipo(currentIndex, list) != PuntoComa){
                                Error = "Error de sintaxis 519:\n\rSe espera el simbolo ;";
                            }
                            else{
                                Polish.add(createPolItem("Scan", "", "operador"));
                            }
                        }
                        else{
                            Error = "Error de sintaxis 513:\n\rSe espera el simbolo )";
                        }
                    }
                    else{
                        Error = "Error de sintaxis 516:\n\rSe espera un ID";
                    }
                }
                else{
                    Error = "Error de sintaxis 512:\n\rSe espera el simbolo (";
                }
                break;
            case Print:
                currentIndex++;
                if(getItemTipo(currentIndex, list) == Parentecis1){
                    currentIndex++;
                    if(getItemTipo(currentIndex, list) == ID || getItemTipo(currentIndex, list) == Cadena){
                        if(getItemTipo(currentIndex, list) == ID){
                            if(!checarVariable(list.get(currentIndex).getText())){
                                Error = "Falta declaracion de la variable";
                            }
                            else{
                                Polish.add(createPolItem(list.get(currentIndex).getText(), "", "variable"));
                            }
                        }
                        else{
                            Polish.add(createPolItem(list.get(currentIndex).getText(), "", "cadena"));
                        }
                        currentIndex++;
                        if(getItemTipo(currentIndex, list) == Parentecis2){
                            currentIndex++;
                            if(getItemTipo(currentIndex, list) != PuntoComa){
                                Error = "Error de sintaxis 519:\n\rSe espera el simbolo ;";
                            }
                            else{
                                Polish.add(createPolItem("Print", "", "operador"));
                            }
                        }
                        else{
                            Error = "Error de sintaxis 513:\n\rSe espera el simbolo )";
                        }
                    }
                    else{
                        Error = "Error de sintaxis 526:\n\rSe espera el un ID o un String";
                    }
                }
                else{
                    Error = "Error de sintaxis 512:\n\rSe espera el simbolo (";
                }
                break;
            case ID:
                if(!checarVariable(list.get(currentIndex).getText())){
                    Error = "Falta declaracion de la variable";
                }
                else{
                    PosFijo.clear();
                    PosFijo.add(obtenerVariable(list.get(currentIndex).getText()));
                    Polish.add(createPolItem(list.get(currentIndex).getText(), "", "variable"));
                    //PilaOperando.push(obtenerVariable(list.get(currentIndex).getText()));
                }
                currentIndex++;
                if(getItemTipo(currentIndex, list) == Igual){
                    //
                    NumericExpression(list);
                    //
                    if(Error.equals("")){
                        currentIndex++;
                        if(getItemTipo(currentIndex, list) != PuntoComa){
                            Error = "Error de sintaxis 519:\n\rSe espera el simbolo ;";
                        }
                        else{
                            vaciarPilas();
                            //PosFijo.add(PilaOperando.pop());
                            PosFijo.add(Igual);
                            Polish.add(createPolItem(":=", "", "operador"));
                            if(!checarPosFijo()){
                                Error = "Tipos no compatibles";
                            }
                        }
                    }
                }
                else{
                    Error = "Error de sintaxis 522:\n\rSe espera el simbolo :=";
                }
                break;
            default:
                currentIndex--;
                break;
        }
    }
    
    private void statementList(List<Item> list){
        //
        Statement(list);
        //
        if(Error.equals("")){
            currentIndex++;
            switch(getItemTipo(currentIndex, list)){
                case If:
                case For:
                case Scan:
                case Print:
                case ID:
                    currentIndex--;
                    //
                    statementList(list);
                    //
                    break;
                default:
                    currentIndex--;
                    break;
            }    
        }
    }
    
    private void forCases(List<Item> list){
        currentIndex++;
        switch(getItemTipo(currentIndex, list)){
            case Break:
            case Continue:
                currentIndex++;
                if(getItemTipo(currentIndex, list) != PuntoComa){
                    Error = "Error de sintaxis 519:\n\rSe espera el simbolo ;";
                }
                break;
            case If:
            case For:
            case Scan:
            case Print:
            case ID:
                currentIndex--;
                //
                Statement(list);
                //
                break;
            default:
                currentIndex--;
                break;
        }
    }
    
    private void forCasesList(List<Item> list){
        //
        forCases(list);
        //
        if(Error.equals("")){
            currentIndex++;
            switch(getItemTipo(currentIndex, list)){
                case If:
                case For:
                case Break:
                case Continue:
                case Scan:
                case Print:
                case ID:
                    currentIndex--;
                    //
                    forCasesList(list);
                    //
                    break;
                default:
                    currentIndex--;
                    break;
            }    
        }
    }
    
    private void LogicExpression(List<Item> list){
        currentIndex++;
        switch(getItemTipo(currentIndex, list)){
            case Parentecis1:
            case Menos:
            case Entero:
            case Decimal:
            case Cadena:
                currentIndex--;
                //
                RelExpression(list);
                //
                if(Error.equals("")){
                    //
                    LogicExpression1(list);
                    //
                }
                break;
            case Not:
                operadoresNumericos(list);
                //
                LogicExpression(list);
                //
                if(Error.equals("")){
                    //
                    LogicExpression1(list);
                    //
                }
                break;
            case Corchete1:
                PilaOperador.push(Corchete1);
                PilaPolOperador.push(createPolItem(list.get(currentIndex).getText(), "", ""));
                //
                LogicExpression(list);
                //
                if(Error.equals("")){
                    currentIndex++;
                    if(getItemTipo(currentIndex, list) == Corchete2){
                        while(PilaOperador.peek() != Corchete1){
                            PosFijo.add(PilaOperador.pop());
                        }
                        PilaOperador.pop();
                        while(!PilaPolOperador.peek().getTexto().equals("[")){
                            Polish.add(PilaPolOperador.pop());
                        }
                        PilaPolOperador.pop();
                        //
                        LogicExpression1(list);
                        //
                    }
                    else{
                        Error = "Error de sintaxis 529:\n\rSe espera el simbolo ]";
                    }
                }
                break;
            case True:
            case False:
            case ID:
                currentIndex++;
                if((getItemTipo(currentIndex, list) >= Mas && getItemTipo(currentIndex, list) <= Entre) ||
                    (getItemTipo(currentIndex, list) >= IgualA && getItemTipo(currentIndex, list) <= MayorIgualA)){    
                    currentIndex -= 2;
                    //
                    RelExpression(list);
                    //
                    if(Error.equals("")){
                        //
                        LogicExpression1(list);
                        //
                    }
                }
                else if(getItemTipo(currentIndex, list) == And || getItemTipo(currentIndex, list) == Or || 
                    getItemTipo(currentIndex, list) == Parentecis2 || getItemTipo(currentIndex, list) == Corchete2){
                    currentIndex--;
                    if(getItemTipo(currentIndex, list) == ID){
                        if(!checarVariable(list.get(currentIndex).getText())){
                            Error = "Falta declaracion de la variable";
                        }
                        else{
                            PosFijo.add(obtenerVariable(list.get(currentIndex).getText()));
                            Polish.add(createPolItem(list.get(currentIndex).getText(), "", "variable"));
                        }
                    }
                    else{
                        PosFijo.add(getItemTipo(currentIndex, list));
                        Polish.add(createPolItem(list.get(currentIndex).getText(), "", "booleano"));
                    }
                    //
                    LogicExpression1(list);
                    //
                }
                else{ 
                    Error = "Error de sintaxis 531:\n\rSe espera el simbolo ) o una operacion relacional";
                }
                break;
            default:
                Error = "Error de sintaxis 527:\n\rSe espera una exprecion Logica";
        }
    }
    
    private void LogicExpression1(List<Item> list){
        currentIndex++;
        if(getItemTipo(currentIndex, list) == And || getItemTipo(currentIndex, list) == Or){
            operadoresNumericos(list);
            //
            LogicExpression(list);
            //
            if(Error.equals("")){
                //
                LogicExpression1(list);
                //
            }
        }
        else{
            currentIndex--;
        }
    }
    
    private void RelExpression(List<Item> list){
        //
        NumericExpression(list);
        //
        if(Error.equals("")){
            currentIndex++;
            if(getItemTipo(currentIndex, list) >= IgualA && getItemTipo(currentIndex, list) <= MayorIgualA){
                operadoresNumericos(list);
                //
                NumericExpression(list);
                //
            }
            else{
                Error = "Error de sintaxis 523:\n\rSe espera un operador relacional";
            }
        }
    }
    
    private void NumericExpression(List<Item> list){
        currentIndex++;
        switch(getItemTipo(currentIndex, list)){
            case Parentecis1:
                PilaOperador.push(Parentecis1);
                PilaPolOperador.push(createPolItem(list.get(currentIndex).getText(), "", ""));
                //
                NumericExpression(list);
                //
                if(Error.equals("")){
                    currentIndex++;
                    if(getItemTipo(currentIndex, list) == Parentecis2){
                        while(PilaOperador.peek() != Parentecis1){
                            PosFijo.add(PilaOperador.pop());
                        }
                        PilaOperador.pop();
                        while(!PilaPolOperador.peek().getTexto().equals("(")){
                            Polish.add(PilaPolOperador.pop());
                        }
                        PilaPolOperador.pop();
                        //
                        NumericExpression1(list);
                        //
                    }
                    else{
                        Error = "Error de sintaxis 513:\n\rSe espera el simbolo )";
                    }
                }
                break;
            case Menos:
                list.get(currentIndex).setTipo(MenosU);
                list.get(currentIndex).setText("@");
                operadoresNumericos(list);
                //
                NumericExpression(list);
                //
                if(Error.equals("")){
                    //
                    NumericExpression1(list);
                    //
                }
                break;
            case ID:
            case Entero:
            case Decimal:
            case Cadena:
            case True:
            case False:
                if(getItemTipo(currentIndex, list) == ID){
                    if(!checarVariable(list.get(currentIndex).getText())){
                        Error = "Falta declaracion de la variable";
                    }
                    else{
                        PosFijo.add(obtenerVariable(list.get(currentIndex).getText()));
                        Polish.add(createPolItem(list.get(currentIndex).getText(), "", "variable"));
                    }
                }
                else{
                    PosFijo.add(getItemTipo(currentIndex, list));
                    if(getItemTipo(currentIndex, list) == Entero || getItemTipo(currentIndex, list) == Decimal){
                        Polish.add(createPolItem(list.get(currentIndex).getText(), "", "numero"));
                    }
                    else if(getItemTipo(currentIndex, list) == Cadena){
                        Polish.add(createPolItem(list.get(currentIndex).getText(), "", "cadena"));
                    }
                    else{
                        Polish.add(createPolItem(list.get(currentIndex).getText(), "", "booleano"));
                    }
                }
                //
                NumericExpression1(list);
                //
                break;
            default:
                Error = "Error de sintaxis 528:\n\rSe espera una exprecion numerica";
        }
    }
    
    private void NumericExpression1(List<Item> list){
        currentIndex++;
        if(getItemTipo(currentIndex, list) >= Mas && getItemTipo(currentIndex, list) <= Entre){
            operadoresNumericos(list);
            //
            NumericExpression(list);
            //
            if(Error.equals("")){
                //
                NumericExpression1(list);
                //
            }
        }
        else{
            currentIndex--;
        }
    }
    
    private int getItemTipo(int index, List<Item> list){
        try{
            return list.get(index).getTipo();
        }
        catch(Exception e){
            return -1;
        }
    }
    
    private void llenarVariables(int tipo){
        for(String p : Pila){
            Variables.add(createVariable(Pila.pop(), tipo));
        }
    }
    
    private Boolean checarVariable(String variable){
        for(Variable var : Variables){
            if(var.getVariable().equals(variable)) return true;
        }
        return false;
    }
    
    private int obtenerVariable(String variable){
        for(Variable var : Variables){
            if(var.getVariable().equals(variable)) return var.getTipo();
        }
        return -1;
    }
    
    private Variable createVariable(String variable, int tipo){
        Variable Var = new Variable();
        Var.setVariable(variable);
        Var.setTipo(tipo);
        return Var;
    }
    
    private PolishItem createPolItem(String texto, String ticket, String tipo){
        PolishItem Pol = new PolishItem();
        Pol.setTexto(texto);
        Pol.setTicket(ticket);
        Pol.setTipo(tipo);
        return Pol;
    }
    
    private int prioridad(int operador){
        switch(operador){
            case Mas:
            case Por:
                return operador+1;
            case Menos:
            case Entre:
                return operador;
            case Not:
                return operador-27;
            case IgualA:
                return operador-8;
            case DiferenteA:
                return operador-9;
            case MenorA:
                return operador-10;
            case MayorA:
                return operador-11;
            case MenorIgualA:
                return operador-12;
            case MayorIgualA:
                return operador-13;
            case And:
                return operador-25;
            case Or:
                return operador-26;
            case MenosU:
                return MenosU;
            default:
                return operador;
        }
    }
    
    private void operadoresNumericos(List<Item> list){
        if(!PilaOperador.isEmpty() && PilaOperador.peek() != Parentecis1 && PilaOperador.peek() != Corchete1){
            if(prioridad(PilaOperador.peek()) < prioridad(getItemTipo(currentIndex, list))){
                PilaOperador.push(getItemTipo(currentIndex, list));
                PilaPolOperador.push(createPolItem(list.get(currentIndex).getText(), "", "operador"));
            }
            else{
                PosFijo.add(PilaOperador.pop());
                Polish.add(PilaPolOperador.pop());
                operadoresNumericos(list);
            }
        }
        else{
            PilaOperador.push(getItemTipo(currentIndex, list));
            PilaPolOperador.push(createPolItem(list.get(currentIndex).getText(), "", "operador"));
        }
    }
    
    private void vaciarPilas(){
        while(!PilaOperador.isEmpty()){
            PosFijo.add(PilaOperador.pop());
        }
        while(!PilaPolOperador.isEmpty()){
            Polish.add(PilaPolOperador.pop());
        }
    }
    
    private boolean checarPosFijo(){
        Deque<Integer> Pila = new ArrayDeque<>();
        for(Integer i : PosFijo){
              switch(i){
                case Entero:
                case Int:
                    Pila.push(Int);
                    break;
                case Decimal:
                case Float:
                    Pila.push(Float);
                    break;
                case Bool:
                case True:
                case False:
                    Pila.push(Bool);
                    break;
                case String:
                case Cadena:
                    Pila.push(String);
                    break;
                case Mas:
                case Menos:
                case Por:
                case Entre:
                case Igual:
                case MenorA:
                case MayorA:
                case MenorIgualA:
                case MayorIgualA:
                case IgualA:
                case And:
                case Or:
                    int op2 = Pila.pop();
                    int op1 = Pila.pop();
                    if(productoOperacion(op1, op2, i) != -1){
                        Pila.push(productoOperacion(op1, op2, i));
                    }
                    else{
                        return false;
                    }
                    break;
                case Not:
                    if(productoOpUnitario(Pila.pop(), Not) != -1){
                        Pila.push(Bool);
                    }
                    else{
                        return false;
                    }
                    break;
                case MenosU:
                    if(productoOpUnitario(Pila.peek(), MenosU) != -1){
                        Pila.push(productoOpUnitario(Pila.pop(), MenosU));
                    }
                    else{
                        return false;
                    }
                    break;
                default:
                    return false;
            }  
        }
        return true;
    }
    
    private int productoOperacion(int op1, int op2, int operando){
        int a = -1;
        switch(operando){
            case Mas:
                switch(op1){
                    case Int:
                        switch(op2){
                            case Int:
                                a = Int;
                                break;
                            case Float:
                                a = Float;
                                break;
                            default:
                                a = -1;
                                break;
                        }    
                        break;
                    case String:
                        a = String;
                        break;
                    case Float:
                        switch(op2){
                            case Int:
                                a = Float;
                                break;
                            case Float:
                                a = Float;
                                break;
                            default:
                                a = -1;
                                break;
                        } 
                        break;
                    default:
                        a = -1;
                        break;
                }
                break;
            case Menos:
                switch(op1){
                    case Int:
                        switch(op2){
                            case Int:
                                a = Int;
                                break;
                            case Float:
                                a = Float;
                                break;
                            default:
                                a = -1;
                                break;
                        }    
                        break;
                    case String:
                        if(op2 == String){
                            a = String;
                        }
                        else{
                            a = -1;
                        }
                        break;
                    case Float:
                        switch(op2){
                            case Int:
                                a = Float;
                                break;
                            case Float:
                                a = Float;
                                break;
                            default:
                                a = -1;
                                break;
                        } 
                        break;
                    default:
                        a = -1;
                        break;
                }
                break;
            case Por:
                switch(op1){
                    case Int:
                        switch(op2){
                            case Int:
                                a = Int;
                                break;
                            case Float:
                                a = Float;
                                break;
                            default:
                                a = -1;
                                break;
                        }    
                        break;
                    case Float:
                        switch(op2){
                            case Int:
                                a = Float;
                                break;
                            case Float:
                                a = Float;
                                break;
                            default:
                                a = -1;
                                break;
                        } 
                        break;
                    default:
                        a = -1;
                        break;
                }
                break;
            case Entre:
                switch(op1){
                    case Int:
                        switch(op2){
                            case Int:
                                a = Float;
                                break;
                            case Float:
                                a = Float;
                                break;
                            default:
                                a = -1;
                                break;
                        }    
                        break;
                    case Float:
                        switch(op2){
                            case Int:
                                a = Float;
                                break;
                            case Float:
                                a = Float;
                                break;
                            default:
                                a = -1;
                                break;
                        } 
                        break;
                    default:
                        a = -1;
                        break;
                }
                break;
            case IgualA:
            case DiferenteA:
                if(op1 == op2){
                    a = Bool;
                }
                else{
                    a = -1;
                }
                break;
            case MenorA:
            case MayorA:
            case MenorIgualA:
            case MayorIgualA:
                if(op1 == op2 && op1 != Bool && op1 != String){
                    a = Bool;
                }
                else{
                    a = -1;
                }
                break;
            case And:
            case Or:
                if(op1 == Bool && op2 == Bool){
                    a = Bool;
                }
                else{
                    a = -1;
                }
                break;
            case Igual:
                if(op1 == op2){
                    a = op1;
                }
                else{
                    if(op1 == Float && op2 == Int){
                        a = Float;
                    }
                    else{
                        a = -1;
                    }
                }
                break;
        }
        return a;
    }
    
    private int productoOpUnitario(int operador, int operando){
        if(operando == Not && operador == Bool){
            return Bool;
        }
        else{
            if(operando == MenosU){
                switch(operador){
                    case Int:
                    case Float:
                        return operador;
                    default:
                        return -1;
                }
            }
            else{
                return -1;
            }
        }
    }
    
}
