/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author adgar
 */
public class Transaccion implements Serializable, Comparable<Transaccion> {

    private String id;
    private LocalDateTime timeStamp;
    private Object objeto;

    public Transaccion(Object obj) {
        this.setObjeto(obj);
        this.timeStamp = LocalDateTime.now();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
   
    public int comparar(Transaccion transaccion) {        
        return this.timeStamp.compareTo(transaccion.getTimeStamp());
    }

    public Object getObjeto() {
        return this.objeto;
    }

    public void setObjeto(Object objeto) {
        this.objeto = objeto;
    }

    @Override
    public int compareTo(Transaccion o) {
        return this.timeStamp.compareTo(o.getTimeStamp());
    }

}
