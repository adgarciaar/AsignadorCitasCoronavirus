/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GrupoPacientes;

import Entidades.Paciente;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 *
 * @author adgar
 */
public class InicioGrupoPacientes {
    
    //public static void main(String args[]) {
    public void iniciarGrupo(String ipServidorCitas, int puertoServidorCitas) {
        
        //String ipServidor = "localhost";
        //ipServidor = "192.168.0.7";
        //int puerto = 7771;
        
        String idGrupo = "Grupo1";
        //duplas <Documento paciente, Paciente>
        HashMap<String, Paciente> pacientes = new HashMap<>();       
        
        Paciente paciente = new Paciente();        
        paciente.setDocumento("ID1");
        paciente.setNombre("Adrian");
        paciente.setEdad(22);
        paciente.setEPS("MiEPS");
        paciente.agregarSintoma("bien");
        paciente.agregarSintoma("enfermo");       
        pacientes.put(paciente.getDocumento(), paciente);
        
        Paciente paciente1 = new Paciente();  
        paciente1.setDocumento("ID2");
        paciente1.setNombre("Juan");
        paciente1.setEdad(22);
        paciente1.setEPS("MiEPS");
        paciente1.agregarSintoma("bien");
        paciente1.agregarSintoma("enfermo");
        pacientes.put(paciente1.getDocumento(), paciente1);
        
        Paciente paciente2 = new Paciente();  
        paciente2.setDocumento("ID3");
        paciente2.setNombre("Christian");
        paciente2.setEdad(21);
        paciente2.setEPS("MiEPS");
        paciente2.agregarSintoma("bien");
        paciente2.agregarSintoma("enfermo");
        pacientes.put(paciente2.getDocumento(), paciente2);
        
        //System.out.println(pacientes);
        
        GrupoPacientes grupoPacientes = null;
        try {
            grupoPacientes = new GrupoPacientes(ipServidorCitas, puertoServidorCitas, pacientes, idGrupo);           
            grupoPacientes.registrarPacientes();
        } catch (RemoteException ex) {
            System.out.println(ex.toString());
        }
        
    }
    
}
