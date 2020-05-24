/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adgar
 */
public class Paciente implements Serializable{
    
    private String documento;
    private String nombre;
    private int edad;
    private String EPS;
    private List<String> sintomas;
    private List<String> patologias_antecedentes;
    private String idGrupo;
    private String ipGrupo;

    public Paciente() {
        this.sintomas = new ArrayList<>();
        this.patologias_antecedentes = new ArrayList<>();
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public List<String> getSintomas() {
        return sintomas;
    }

    public void setSintomas(List<String> sintomas) {
        this.sintomas = sintomas;
    }
    
    public void agregarSintoma(String sintoma){
        this.sintomas.add(sintoma);
    }
    
    public void agregarPatologia(String patologia){
        this.patologias_antecedentes.add(patologia);
    }

    public String getEPS() {
        return EPS;
    }

    public void setEPS(String EPS) {
        this.EPS = EPS;
    }

    public List<String> getPatologias_antecedentes() {
        return patologias_antecedentes;
    }

    public void setPatologias_antecedentes(List<String> patologias_antecedentes) {
        this.patologias_antecedentes = patologias_antecedentes;
    }

    public ChronoLocalDateTime<?> getTimeStampEscritura() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getIpGrupo() {
        return ipGrupo;
    }

    public void setIpGrupo(String ipGrupo) {
        this.ipGrupo = ipGrupo;
    }
    
}
