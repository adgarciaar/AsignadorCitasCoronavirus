/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServidorCitas;

import Entidades.Message;
import INS.InterfaceINS;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adgar
 */
public class ServidorCitas extends UnicastRemoteObject implements InterfaceServidorCitas {
    
    private int puerto;
    private List<String> miLista;
    private List<String> listaPacientes;
    
    private String ipServidorINS;
    private int puertoINS;

    public ServidorCitas(int puerto) throws RemoteException{
        this.puerto = puerto;
        this.miLista = new ArrayList<>();
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
    public String registrarEPS(String nombreEPS) throws RemoteException {
        synchronized(this) {
            this.miLista.add(nombreEPS);
            System.out.println(this.miLista);
        }
        return "Registrada la EPS";
    }

    @Override
    public void registrarPacientes(List<String> pacientes) throws RemoteException {
        synchronized(this) {
            this.listaPacientes.addAll(pacientes);
            System.out.println(this.listaPacientes);
        }
    }

    @Override
    public boolean evaluarUnPaciente(String nombrePaciente) throws RemoteException {
        
        try {
            String nombreServicio = "//"+this.ipServidorINS+":"+this.puertoINS+"/ServicioINS";
            InterfaceINS serverInterface = (InterfaceINS) Naming.lookup(nombreServicio);
            serverInterface.evaluarPaciente(nombrePaciente);      
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        
    }
    
}
