/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
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
    private List<String> sintomas;

    public Paciente() {
        this.sintomas = new ArrayList<>();
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
    
    public boolean tieneSintoma(String sintoma){
        boolean retorno = false;
        for (int i = 0; i < this.sintomas.size(); i++) {
            if(sintoma.equals(this.sintomas.get(i))){
                retorno = true;
                break;
            }
        }
        return retorno;
    }
    
}
