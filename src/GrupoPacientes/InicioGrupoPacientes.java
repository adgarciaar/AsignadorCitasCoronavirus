/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GrupoPacientes;

import Entidades.Paciente;
import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author adgar
 */
public class InicioGrupoPacientes {    
    
    public void iniciarGrupo(String ipServidorCitas, int puertoServidorCitas, String rutaArchivo) {
        
        List<String> instruccionesConfiguracion = new ArrayList<>();
        
        try {
            
            File myObj = new File(rutaArchivo);
            try (Scanner myReader = new Scanner(myObj)) {
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    instruccionesConfiguracion.add(data.trim());
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error: No se encontró archivo de configuración");
            System.exit(1);
        }
        
        
        String idGrupo = instruccionesConfiguracion.get(0);
        
        int numeroPacientes = Integer.parseInt(instruccionesConfiguracion.get(1));
        
        //duplas <Documento paciente, Paciente>
        HashMap<String, Paciente> pacientes = new HashMap<>();     
        
        String[] datosPaciente;      
        String[] sintomasPaciente;  
        String[] patologiasAntecedentesPaciente;
        int numeroSintomas, numeroPatologias;
        Paciente paciente;
        
        System.out.println("Grupo de pacientes "+idGrupo+"\n");
        System.out.println("Pacientes en este grupo");
        
        for (int i = 2; i < 2 + numeroPacientes*3; i+=3) {      
            
            datosPaciente = instruccionesConfiguracion.get(i).split("\t");
            
            sintomasPaciente = instruccionesConfiguracion.get(i+1).split("\t");
            numeroSintomas = Integer.parseInt(sintomasPaciente[0]);
            
            patologiasAntecedentesPaciente = instruccionesConfiguracion.get(i+2).split("\t");
            numeroPatologias = Integer.parseInt(patologiasAntecedentesPaciente[0]);
            
            paciente = new Paciente();
            paciente.setDocumento(datosPaciente[0]);
            paciente.setNombre(datosPaciente[1]);
            System.out.println("\n"+datosPaciente[1]);
            paciente.setEdad( Integer.parseInt(datosPaciente[2]) );
            paciente.setEPS( datosPaciente[3] );
            
            for (int j = 1; j < 1 + numeroSintomas; j++) {   
                paciente.agregarSintoma( sintomasPaciente[j] );
                System.out.println("Síntoma: "+sintomasPaciente[j]);
            }
            for (int j = 1; j < 1 + numeroPatologias; j++) {   
                paciente.agregarPatologia( patologiasAntecedentesPaciente[j] );
                System.out.println("Patología/Antecedente: "+patologiasAntecedentesPaciente[j]);
            }
            
            pacientes.put(paciente.getDocumento(), paciente);            
        }
        
        //System.out.println("\n"+pacientes+"\n");
        
        try {
            GrupoPacientes grupoPacientes = new GrupoPacientes(ipServidorCitas, puertoServidorCitas, pacientes, idGrupo);           
            grupoPacientes.solicitarCitas();
        } catch (RemoteException e) {
            System.out.println("Error: "+e.toString());
        }
        
    }
    
}
