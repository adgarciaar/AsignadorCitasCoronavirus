/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServidorCitas;

import EPS.InterfaceEPS;
import Entidades.Message;
import INS.InterfaceINS;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author adgar
 */
public class ServidorCitas extends UnicastRemoteObject implements InterfaceServidorCitas {
    
    private int puerto;
    private HashMap<String, String> listaEPSs;
    private List<String> listaPacientes;
    
    private String ipServidorINS;
    private int puertoINS;

    public ServidorCitas(int puerto) throws RemoteException{
        this.puerto = puerto;
        this.listaEPSs = new HashMap<>();
        this.listaPacientes = new ArrayList<>();
        
        this.ipServidorINS = "localhost";
        this.puertoINS = 7770;
    }

    @Override
    public Message retornarMensaje() throws RemoteException {
        Message mensaje = new Message();
        mensaje.setCodigo(432);
        mensaje.setTexto("Adrian");
        return mensaje;
    }

    @Override
    public boolean registrarEPS(String nombreEPS) throws RemoteException {
        
        synchronized(this) {
            
            if(this.listaEPSs.get(nombreEPS) == null){ //no está registrada
                
                this.listaEPSs.put(nombreEPS, "EPS");
                System.out.println("Registrada la EPS "+nombreEPS);
                return true;
            }else{
                System.out.println("La EPS "+nombreEPS+" ya se está registrada");
                return false;
            }
        }       
    }

    @Override
    public void registrarPacientes(List<String> pacientes) throws RemoteException {
        synchronized(this) {
            this.listaPacientes.addAll(pacientes);
            System.out.println(this.listaPacientes);
        }
        
        //llamado a EPS
        String servidorEPS = "192.168.0.10";
        try {
            String nombreServicio = "//"+servidorEPS+":"+this.puerto+"/ServicioEPS"+"MiEPS";
            InterfaceEPS serverInterface = (InterfaceEPS) Naming.lookup(nombreServicio); 
            serverInterface.avisar();
        } catch (Exception e) {
            System.out.println(e.toString());            
        }
        
    }

    @Override
    public boolean evaluarUnPaciente(String nombrePaciente) throws RemoteException {
        
        try {
            String nombreServicio = "//"+this.ipServidorINS+":"+this.puertoINS+"/ServicioINS";
            InterfaceINS serverInterface = (InterfaceINS) Naming.lookup(nombreServicio);
            return serverInterface.evaluarPaciente(nombrePaciente);   
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        
    }
    
}
