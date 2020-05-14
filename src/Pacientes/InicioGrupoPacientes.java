/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pacientes;

import Pacientes.GrupoPacientes;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adgar
 */
public class InicioGrupoPacientes {
    
    public static void main(String args[]) {
        String ipServidor = "localhost";
        int puerto = 7771;
        
        List<String> pacientes = new ArrayList<>();
        pacientes.add("Adrian");
        pacientes.add("Juan");
        pacientes.add("Christian");
        
        GrupoPacientes grupoPacientes = new GrupoPacientes(ipServidor, puerto, pacientes);
        grupoPacientes.informarPacientes();
        grupoPacientes.pedirCita();
    }
    
}
