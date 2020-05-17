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
        
        paciente = new Paciente();
        paciente.setDocumento("ID2");
        paciente.setNombre("Juan");
        paciente.setEdad(22);
        paciente.setEPS("MiEPS");
        paciente.agregarSintoma("bien");
        paciente.agregarSintoma("enfermo");
        pacientes.put(paciente.getDocumento(), paciente);
        
        paciente = new Paciente();
        paciente.setDocumento("ID3");
        paciente.setNombre("Christian");
        paciente.setEdad(21);
        paciente.setEPS("MiEPS");        
        paciente.agregarSintoma("enfermo");
        pacientes.put(paciente.getDocumento(), paciente);
        
        paciente = new Paciente();
        paciente.setDocumento("ID4");
        paciente.setNombre("Andrea");
        paciente.setEdad(20);
        paciente.setEPS("MiEPS");
        paciente.agregarSintoma("bien");        
        pacientes.put(paciente.getDocumento(), paciente);
        
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
