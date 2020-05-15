/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pacientes;

import Pacientes.GrupoPacientes;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adgar
 */
public class InicioGrupoPacientes {
    
    public static void main(String args[]) {
        String ipServidor = "localhost";
        int puerto = 7771;
        
        String idGrupo = "Grupo1";
        List<String> pacientes = new ArrayList<>();
        pacientes.add("Adrian");
        pacientes.add("Juan");
        pacientes.add("Christian");
        
        GrupoPacientes grupoPacientes = null;
        try {
            grupoPacientes = new GrupoPacientes(ipServidor, puerto, pacientes, idGrupo);
            grupoPacientes.registrarPacientes();
        } catch (RemoteException ex) {
            System.out.println(ex.toString());
        }
        
    }
    
}
