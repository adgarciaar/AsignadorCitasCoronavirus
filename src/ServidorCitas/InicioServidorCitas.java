/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServidorCitas;

import GUI.GUIServidorCitas;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

/**
 *
 * @author adgar
 */
public class InicioServidorCitas {
    
    public void iniciarServidor(int puerto, String ipINS, int puertoINS){
        
        InterfaceServidorCitas servicio = null;
        try {
            servicio = new ServidorCitas(puerto, ipINS, puertoINS);
        } catch (RemoteException ex) {
            System.out.println(ex.toString());
        }
        
        try {
            Registry r = java.rmi.registry.LocateRegistry.createRegistry(puerto);       
            r.bind("ServAsignacionCitas", servicio);
            System.out.println("Servidor de citas activo");
            
            GUIServidorCitas gui = new GUIServidorCitas(servicio);            
            servicio.referenciarGUI(gui);
            gui.setLocationRelativeTo(null); //ubicarla en centro de pantalla
            gui.setVisible(true);
            
        } catch (Exception e) {
            System.out.println(e);
        }        
        
    }
    
}
