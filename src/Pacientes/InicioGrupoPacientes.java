/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pacientes;

import Entidades.Paciente;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author adgar
 */
public class InicioGrupoPacientes {
    
    public static void main(String args[]) {
        
        String ipServidor = "localhost";
        ipServidor = "192.168.0.7";
        int puerto = 7771;
        
        String idGrupo = "Grupo1";
        //duplas <Documento paciente, Paciente>
        HashMap<String, Paciente> pacientes = new HashMap<>();       
        
        Paciente paciente = new Paciente();        
        paciente.setDocumento("ID1");
        paciente.setNombre("Adrian");
        paciente.setEdad(22);
        
        pacientes.put(paciente.getDocumento(), paciente);
        
        Paciente paciente1 = new Paciente();  
        paciente.setDocumento("ID2");
        paciente.setNombre("Juan");
        paciente.setEdad(22);
        pacientes.put(paciente1.getDocumento(), paciente1);
        
        Paciente paciente2 = new Paciente();  
        paciente.setDocumento("ID3");
        paciente.setNombre("Christian");
        paciente.setEdad(21);
        pacientes.put(paciente2.getDocumento(), paciente2);
        
        GrupoPacientes grupoPacientes = null;
        try {
            grupoPacientes = new GrupoPacientes(ipServidor, puerto, pacientes, idGrupo);
            grupoPacientes.registrarPacientes();
        } catch (RemoteException ex) {
            System.out.println(ex.toString());
        }
        
    }
    
}
