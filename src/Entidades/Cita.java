/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.HashMap;



/**
 *
 * @author adgar
 */
public class Cita implements Serializable , Comparable<Cita>{
    
    private String idCita;
    private String documentoPaciente;
    private int prioridad;
    private int dia;
    private int hora;
    
    

    public Cita() {
        
    }

    public Cita(String documentoPaciente, int prioridad, int dia, int hora) {
        this.documentoPaciente = documentoPaciente;
        this.prioridad = prioridad;
        this.dia = dia;
        this.hora = hora;
        this.idCita = "Cita día: "+this.dia+" hora: "+ ajustarHoraCita(this.hora);
       
    }
    
    public String ajustarHoraCita(int hora){
        
        HashMap<Integer, String> horarios = new HashMap<Integer, String>();
        horarios.put(1,"06:00");
        horarios.put(2,"07:00");
        horarios.put(3,"08:00");
        horarios.put(4,"09:00");
        horarios.put(5,"10:00");
        horarios.put(6,"11:00");
        horarios.put(7,"12:00");
        horarios.put(8,"13:00");
        
        return horarios.get(hora);

    }

    public String getIdCita() {
        return idCita;
    }

    public void setIdCita(String idCita) {
        this.idCita = idCita;
    }

    public String getDocumentoPaciente() {
        return documentoPaciente;
    }

    public void setDocumentoPaciente(String documentoPaciente) {
        this.documentoPaciente = documentoPaciente;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }
    
    

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
        this.idCita = "Cita día: "+this.dia+" hora: "+ ajustarHoraCita(this.hora);
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
        this.idCita = "Cita día: "+this.dia+" hora: "+ ajustarHoraCita(this.hora);
    }

    @Override
    public int compareTo(Cita o) {
        
        if (this.dia != o.getDia()) {
            return this.dia - o.getDia();
        }
        else
        return this.hora - o.getHora();
    }
    
    
}
