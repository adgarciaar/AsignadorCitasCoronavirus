/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EPS;

import ServidorCitas.InterfaceServidorCitas;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
    
    private String ipEPS;
    private String ipServidorCitas;
    private int puertoServidorCitas;
    
    private String nombre;
    //mapa con duplas <Documento paciente, Nombre paciente>
    private HashMap<String, String> pacientesConServicio; 

    public EPS(String ipServidorCitas, int puerto, String nombre, 
            HashMap<String, String> pacientesConServicio) throws RemoteException {
        
        this.ipServidorCitas = ipServidorCitas;
        this.puertoServidorCitas = puerto;
        this.nombre = nombre;
        this.pacientesConServicio = pacientesConServicio;
        
        //se consigue la ip de la máquina en que se está ejecutando esta función
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            System.out.println("Error al conseguir la dirección IP de la máquina actual");
            System.exit(1);
        }        
        this.ipEPS = inetAddress.getHostAddress();
        
    }
    
    private void registrarServicioRegistro(){
        //Registrar el servicio de esa EPS
        try {
            Registry r = null;
            if(this.ipServidorCitas.equals(this.ipEPS)){
                //para poder ejecutarlos en misma máquina
                //System.out.println("estan en misma máquina");
                r = java.rmi.registry.LocateRegistry.getRegistry(this.puertoServidorCitas);
            }else{
                //System.out.println("no estan en misma máquina");
                r = java.rmi.registry.LocateRegistry.createRegistry(this.puertoServidorCitas);
            }
            //Registry r = java.rmi.registry.LocateRegistry.createRegistry(this.puertoServidorCitas);
            r.bind("ServicioEPS" + this.nombre, this);
            System.out.println("Servidor de la EPS activo: ServicioEPS"+this.nombre);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    public void registrarEPS() {
        
        try {
            String nombreServicio = "//"+this.ipServidorCitas+":"
                    +this.puertoServidorCitas+"/ServAsignacionCitas";
            
            InterfaceServidorCitas serverInterface 
                    = (InterfaceServidorCitas) Naming.lookup(nombreServicio);
            
            boolean retorno = serverInterface.registrarEPS(this.nombre, this.ipEPS);
            
            if(retorno){
                this.registrarServicioRegistro(); //Registrar el servicio de esta EPS            
                System.out.println("EPS ha sido registrada en el servidor de citas");
            }else{
                System.out.println("Error: EPS no pudo registrarse en el servidor de citas");
                System.exit(1);
            }
            
        } catch (Exception e) {
            System.out.println("Error: "+e.toString());
            System.exit(1);
        }
        
    }

    @Override
    public void avisar() throws RemoteException {
        System.out.println("Avisado en EPS");
    }

    @Override
    public boolean pacienteTieneCobertura(String documentoPaciente) throws RemoteException {        
        if( this.pacientesConServicio.get(documentoPaciente) != null ){
            System.out.println("Paciente con documento "+documentoPaciente+" tiene cobertura");
            return true;
        }else{
            System.out.println("Paciente con documento "+documentoPaciente+" no tiene cobertura");
            return false;
        }        
    }
    
}
