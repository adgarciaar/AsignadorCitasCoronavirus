/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EPS;

import ServidorCitas.InterfaceServidorCitas;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 *
 * @author adgar
 */
public class EPS extends UnicastRemoteObject implements InterfaceEPS {
    
    private String ipServidorCitas;
    private int puertoServidorCitas;
    
    private String nombre;
    private HashMap<String, String> pacientesConServicio; 

    public EPS(String ipServidorCitas, int puerto, String nombre, 
            HashMap<String, String> pacientesConServicio) throws RemoteException {
        
        this.ipServidorCitas = ipServidorCitas;
        this.puertoServidorCitas = puerto;
        this.nombre = nombre;
        this.pacientesConServicio = pacientesConServicio;
    }
    
    public void registrarEPS() {
        
        try {
            String nombreServicio = "//"+this.ipServidorCitas+":"
                    +this.puertoServidorCitas+"/ServAsignacionCitas";
            
            InterfaceServidorCitas serverInterface 
                    = (InterfaceServidorCitas) Naming.lookup(nombreServicio);
            
            boolean retorno = serverInterface.registrarEPS(this.nombre);
            
            if(retorno){
                
                //Registrar el servicio de esa EPS
                try {
                    Registry r = java.rmi.registry.LocateRegistry.createRegistry(this.puertoServidorCitas);       
                    r.bind("ServicioEPS"+this.nombre, this);
                    System.out.println("Servidor de la EPS activo");
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
                
                System.out.println("EPS ha sido registrada en el servidor de citas");
            }else{
                System.out.println("EPS no pudo registrarse en el servidor de citas");
                System.exit(1);
            }
            
        } catch (Exception e) {
            System.out.println("Se presentó un error: "+e.toString());
            System.exit(1);
        }
        
    }

    @Override
    public void avisar() throws RemoteException {
        System.out.println("Avisado en EPS");
    }
    
}
