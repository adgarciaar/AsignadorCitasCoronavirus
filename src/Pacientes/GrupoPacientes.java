/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pacientes;

import ServidorCitas.InterfaceServidorCitas;
import java.rmi.Naming;
import java.util.List;

/**
 *
 * @author adgar
 */
public class GrupoPacientes {
    
    private String ipServidorCitas;
    private int puerto;
    
    private List<String> pacientes;

    public GrupoPacientes(String ipServidorCitas, int puerto, List<String> pacientes) {
        this.ipServidorCitas = ipServidorCitas;
        this.puerto = puerto;
        this.pacientes = pacientes;
    }
    
    public void registrarPacientes() {
        
        try {
            String nombreServicio = "//"+this.ipServidorCitas+":"+this.puerto+"/ServAsignacionCitas";
            InterfaceServidorCitas serverInterface = (InterfaceServidorCitas) Naming.lookup(nombreServicio);
            serverInterface.registrarPacientes(this.pacientes);
            //String miRetorno = serverInterface.registrarEPS(this.nombre);
            //System.out.println(miRetorno);
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
    
    public void pedirCita(){
        
        try {
            String nombreServicio = "//"+this.ipServidorCitas+":"+this.puerto+"/ServAsignacionCitas";
            InterfaceServidorCitas serverInterface = (InterfaceServidorCitas) Naming.lookup(nombreServicio);
            boolean sePuede = serverInterface.evaluarUnPaciente("Adrian");
            //String miRetorno = serverInterface.registrarEPS(this.nombre);
            System.out.println("Se puede? "+sePuede);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
