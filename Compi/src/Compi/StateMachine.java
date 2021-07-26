/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compi;

import java.awt.Color;
import java.io.FileInputStream;
import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author Luis Cota
 */
public class StateMachine {
    
    private final TransitionMatrix TM = new TransitionMatrix();
    
    List<Item> Items = new ArrayList<>();
    
    String Error = "";
    
    private final int Letra = 0, Digito = 1, Slash = 2, Asterisco = 3, Punto = 4, PuntoComa = 5, Coma = 6, DosPuntos = 7, Igual = 8, 
        Parentecis1 = 9, Parentecis2 = 10, Llave1 = 11, Llave2 = 12, Corchete1 = 13, Corchete2 = 14, Mas = 15, Menos = 16, 
        Porcentaje = 17, Mayor = 18, Menor = 19, Not = 20, Comillas = 21, Ampersand = 22, Or = 23, NL = 24, EOF = 25, EB = 26 ,OC = 27;
    
    private final String PalabrasRecerbadas[] = {"if", "else", "for", "break", "continue", "true", "false", "scan", "print", 
                                   "package", "main", "func", "var", "int", "string", "bool", "float"}; 
    
    Color Palabras = Color.BLACK, Numero = Color.BLUE, Puntuacion = Color.GREEN, 
          Agrupacion = Color.GRAY, Comparacion = Color.CYAN, Comilla = Color.ORANGE;
    
    void StateM(FileInputStream fi, JTextPane jp){
        Color color;
        boolean regreso = false;
        int i = 0, renglon = 1, estado = 0 ,b;
        String mensaje = "", texto = "";
        long s;
        FileChannel fc = fi.getChannel();
        appendToPane(jp, renglon+"\t", Color.BLACK);
        try{
            while((b = fi.read()) != -1){
                s = fc.position();
                if(isLetter((char)b)){
                    estado = TM.Matrix[i][Letra];
                    color = getColor(i, Palabras);
                    i = estado;
                    texto += ""+(char)b;
                    regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                }
                else if(isDigit((char)b)){
                    estado = TM.Matrix[i][Digito];
                    color = getColor(i, Numero);
                    i = estado;
                    texto += ""+(char)b;
                    regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                }
                else{
                    switch((char)b){
                        case '/':
                            estado = TM.Matrix[i][Slash];
                            color = getColor(i, Color.MAGENTA);
                            i = estado;
                            texto += ""+(char)b;
                            if(estado == 0) texto = "";
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case '*':
                            estado = TM.Matrix[i][Asterisco];
                            color = getColor(i, Color.MAGENTA);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case '.':
                            estado = TM.Matrix[i][Punto];
                            color = getColor(i, Puntuacion);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case ';':
                            estado = TM.Matrix[i][PuntoComa];
                            color = getColor(i, Puntuacion);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case ',':
                            estado = TM.Matrix[i][Coma];
                            color = getColor(i, Puntuacion);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case ':':
                            estado = TM.Matrix[i][DosPuntos];
                            color = getColor(i, Color.MAGENTA);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case '=':
                            estado = TM.Matrix[i][Igual];
                            color = getColor(i, Comparacion);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case '(':
                            estado = TM.Matrix[i][Parentecis1];
                            color = getColor(i, Agrupacion);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case ')':
                            estado = TM.Matrix[i][Parentecis2];
                            color = getColor(i, Agrupacion);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case '{':
                            estado = TM.Matrix[i][Llave1];
                            color = getColor(i, Agrupacion);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case '}':
                            estado = TM.Matrix[i][Llave2];
                            color = getColor(i, Agrupacion);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case '[':
                            estado = TM.Matrix[i][Corchete1];
                            color = getColor(i, Agrupacion);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case ']':
                            estado = TM.Matrix[i][Corchete2];
                            color = getColor(i, Agrupacion);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case '+':
                            estado = TM.Matrix[i][Mas];
                            color = getColor(i, Color.MAGENTA);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case '-':
                            estado = TM.Matrix[i][Menos];
                            color = getColor(i, Color.MAGENTA);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case '%':
                            estado = TM.Matrix[i][Porcentaje];
                            color = getColor(i, Color.MAGENTA);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case '>':
                            estado = TM.Matrix[i][Mayor];
                            color = getColor(i, Comparacion);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case '<':
                            estado = TM.Matrix[i][Menor];
                            color = getColor(i, Comparacion);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case '!':
                            estado = TM.Matrix[i][Not];
                            color = getColor(i, Comparacion);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case '"':
                            estado = TM.Matrix[i][Comillas];
                            color = getColor(i, Comilla);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case '&':
                            estado = TM.Matrix[i][Ampersand];
                            color = getColor(i, Comparacion);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case '|':
                            estado = TM.Matrix[i][Or];
                            color = getColor(i, Comparacion);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case '\n':
                            renglon++;
                            color = getColor(i, Color.MAGENTA);
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            appendToPane(jp, renglon+"\t", Color.BLACK);
                            break;
                        case ' ':
                            estado = TM.Matrix[i][EB];
                            color = getColor(i, Color.MAGENTA);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case '\r':
                            estado = TM.Matrix[i][NL];
                            color = getColor(i, Color.MAGENTA);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        case '\t':
                            estado = TM.Matrix[i][EB];
                            color = getColor(i, Color.MAGENTA);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                            break;
                        default:
                            estado = TM.Matrix[i][OC];
                            color = getColor(i, Color.MAGENTA);
                            i = estado;
                            texto += ""+(char)b;
                            regreso = checarRegreso(jp, ""+(char)b, color, regreso);
                    }
                    
                }
                if(estado == 0) texto = "";
                if(estado >= 100 && estado < 500) i = 0;
                switch(estado){
                    case 100:
                    case 101:
                    case 102:
                    case 116:
                    case 120:
                    case 121:
                    case 128:
                        Items.add(createItem(texto.substring(0, texto.length()-1), estado, renglon));
                        texto = "";
                        fc.position(s-1);
                        regreso = true;
                        break;
                    case 103:
                    case 104:
                    case 105:
                    case 107:
                    case 108:
                    case 109:
                    case 110:
                    case 111:
                    case 112:
                    case 113:
                    case 114:
                    case 115:
                    case 117:
                    case 118:
                    case 119:
                    case 122:
                    case 123:
                    case 124:
                    case 125:
                    case 126:
                    case 127:
                        Items.add(createItem(texto, estado, renglon));
                        texto = "";
                        break;
                }
                if(estado >= 500) break;
            }
            if(estado < 100) estado = TM.Matrix[i][EOF];
            if(estado >= 500){
                switch(estado){
                    case 500:
                        mensaje = "Error Lexico:\n\rNumero decimal no terminado";
                        break;
                    case 501:
                        mensaje = "Error Lexico:\n\rComentario no cerrado";
                        break;
                    case 502:
                        mensaje = "Error Lexico:\n\rSe espera '=' despues de ':'";
                        break;
                    case 503:
                        mensaje = "Error Lexico:\n\rComillas no cerradas";
                        break;
                    case 504:
                        mensaje = "Error Lexico:\n\rSe espera '&' despues de '&'";
                        break;
                    case 505:
                        mensaje = "Error Lexico:\n\rSe espera '|' despues de '|'";
                        break;
                    case 506:
                        mensaje = "Error Lexico:\n\rSe espera '=' despues de '='";
                        break;
                    case 507:
                        mensaje = "Error Lexico:\n\rCaracter no valido";
                        break;
                }
            }
            else{
                switch(estado){
                case 100:
                case 101:
                case 102:
                case 116:
                case 121:
                case 120:
                case 128:
                    Items.add(createItem(texto, estado, renglon));
                    break;
                }
            }
        }
        catch(Exception e){}
        PalabrasRecerbadas();
        Error = mensaje;
    }
    
    private void PalabrasRecerbadas(){
        int index;
        for(Item item : Items){
            index = Arrays.asList(PalabrasRecerbadas).indexOf(item.getText().toLowerCase());
            if(index != -1){
                if(index >= 0 && index <= 16){
                    item.setTipo(index+200);
                }
            }
        }
    }
    
    private Item createItem(String texto, int tipo, int renglon){
        Item item = new Item();
        item.setText(texto);
        item.setTipo(tipo);
        item.setRenglon(renglon);
        return item;
    }
    
    private boolean checarRegreso(JTextPane tp, String msg, Color c, boolean regreso){
        if(!regreso){
            appendToPane(tp, msg, c);
            return false;
        }
        else{
            return false;
        }
    }
    
    private Color getColor(int i, Color c){
        switch(i){
            case 0:
                return c;
            case 1:
                return Palabras;
            case 2:
            case 3:
            case 4:
                return Numero;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return Color.MAGENTA;
            case 10:
            case 11:
            case 12:
            case 14:
            case 15:
            case 16:
                return Comparacion;
            case 13:
                return Comilla;
            default:
                return Color.MAGENTA;
        }
    }
    
    private void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }
}
