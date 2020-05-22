/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EPS;

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
public class InicioEPS {
    
    public void iniciarEPS(String ipServidorCitas, int puertoServidorCitas, String rutaArchivo) {
        
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
        
        String nombreEPS = instruccionesConfiguracion.get(0);
        
        int numeroCitas = Integer.parseInt(instruccionesConfiguracion.get(1));
        
        int numeroClientesEPS = Integer.parseInt(instruccionesConfiguracion.get(2));
        
        HashMap<String, String> pacientesConServicio = new HashMap<>();;
        String[] datosPaciente;        
        for (int i = 3; i < 3 + numeroClientesEPS; i++) {
            datosPaciente = instruccionesConfiguracion.get(i).split("\t");
            pacientesConServicio.put(datosPaciente[0], datosPaciente[1]); //id, nombre
            System.out.println(datosPaciente[0]+" "+datosPaciente[1]);
        }
        
        try {
            EPS cliente = new EPS(ipServidorCitas, puertoServidorCitas, nombreEPS, 
                    pacientesConServicio, numeroCitas);
            cliente.registrarEPS();
        } catch (RemoteException ex) {
            System.out.println(ex.toString());
        }        
    }
    
}
