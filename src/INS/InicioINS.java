/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package INS;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;

/**
 *
 * @author adgar
 */
public class InicioINS {
    
    public static void main(String args[]) {
        
        int puerto = 7770;
        
        InterfaceINS servicio = null;
        try {
            servicio = new INS(puerto);
        } catch (RemoteException ex) {
            System.out.println(ex.toString());
        }
        
        try {
            Registry r = java.rmi.registry.LocateRegistry.createRegistry(puerto);       
            r.bind("ServicioINS", servicio);
            System.out.println("Servidor INS activo");
        } catch (Exception e) {
            System.out.println(e);
        }        
        
    }
    
}
