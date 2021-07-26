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
public class Item {
    
    private String texto;
    private int tipo;
    private int renglon;
    
    public void setText(String texto){
        this.texto = texto;
    }
    
    public void setTipo(int tipo){
        this.tipo = tipo;
    }
    
    public void setRenglon(int renglon){
        this.renglon = renglon;
    }
    
    public String getText(){
        return texto;
    }
    
    public int getTipo(){
        return tipo;
    }
    
    public int getRenglon(){
        return renglon;
    }
    
}
