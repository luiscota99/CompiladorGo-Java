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
public class PolishItem {
    private String texto;
    private String ticket;
    private String tipo;
    
    public void setTexto(String texto){
        this.texto = texto;
    }
    
    public void setTicket(String ticket){
        this.ticket = ticket;
    }
    
    public void setTipo(String tipo){
        this.tipo = tipo;
    }
    
    public String getTexto(){
        return texto;
    }
    
    public String getTicket(){
        return ticket;
    }
    
    public String getTipo(){
        return tipo;
    }
}
