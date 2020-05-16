/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EPS;

import java.rmi.RemoteException;
import java.util.HashMap;

/**
 *
 * @author adgar
 */
public class InicioEPS {
    
    public static void main(String args[]) {
        
        String ipServidorCitas = "localhost";
        ipServidorCitas = "192.168.0.7";
        int puertoServidorCitas = 7771;
        String nombreEPS = "MiEPS";
        
        HashMap<String, String> pacientesConServicio;
        pacientesConServicio = new HashMap<>();
        
        pacientesConServicio.put("ID1", "Adrian");
        pacientesConServicio.put("ID2", "Juan");
        
        EPS cliente = null;
        try {
            cliente = new EPS(ipServidorCitas, puertoServidorCitas, nombreEPS, pacientesConServicio);
            cliente.registrarEPS();
        } catch (RemoteException ex) {
            System.out.println(ex.toString());
        }        
    }
    
}
