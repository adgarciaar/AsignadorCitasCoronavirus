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
    
    public static void main(String args[]) {
        
        int puerto = 7771;
        
        InterfaceServidorCitas servicio = null;
        try {
            servicio = new ServidorCitas(puerto);
        } catch (RemoteException ex) {
            System.out.println(ex.toString());
        }
        
        try {
            Registry r = java.rmi.registry.LocateRegistry.createRegistry(puerto);       
            r.bind("ServAsignacionCitas", servicio);
            System.out.println("Servidor de citas activo");
            
            GUIServidorCitas gui = new GUIServidorCitas(servicio);
            servicio.referenciarGUI(gui);
            gui.setVisible(true);
            
        } catch (Exception e) {
            System.out.println(e);
        }        
        
    }
    
}
