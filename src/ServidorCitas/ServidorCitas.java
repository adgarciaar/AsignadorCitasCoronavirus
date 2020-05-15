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
    //mapa con duplas <EPS, IP de la máquina>
    private HashMap<String, String> listaEPSs;
    //mapa con duplas <Documento paciente, IP de la máquina de su grupo>
    private HashMap<String, String> listaPacientes;
    
    private String ipServidorINS;
    private int puertoINS;

    public ServidorCitas(int puerto) throws RemoteException{
        this.puerto = puerto;
        this.listaEPSs = new HashMap<>();
        this.listaPacientes = new HashMap<>();
        
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
    public boolean registrarEPS(String nombreEPS, String ipEPS) throws RemoteException {
        
        synchronized(this) {
            
            if(this.listaEPSs.get(nombreEPS) == null){ //no está registrada                
                this.listaEPSs.put(nombreEPS, ipEPS);
                System.out.println("Registrada la EPS "+nombreEPS);
                return true;
            }else{
                System.out.println("La EPS "+nombreEPS+" ya se está registrada");
                return false;
            }
        }       
    }

    @Override
    public boolean registrarPacientes(List<String> pacientes, String ipGrupo) throws RemoteException {
        
        boolean retorno = true;
        
        synchronized(this) {
            
            /*for (HashMap.Entry<String, String> entry : this.listaPacientes.entrySet()) {                
            }*/
            String documentoPaciente;
            for(int i=0; i<pacientes.size(); i++){
                documentoPaciente = pacientes.get(i);
                if( this.listaPacientes.get( documentoPaciente ) == null ){
                    //el paciente no se ha registrado previamente
                    this.listaPacientes.put(documentoPaciente, ipGrupo);
                }else{
                    retorno = false;
                    break;
                }             
            }   
            
            if(retorno){
                System.out.println("Se agregaron los pacientes: "+pacientes);
                return retorno;
            }else{
                //hacer rollback (remover los que se alcanzaron a agregar)
                for(int i=0; i<pacientes.size(); i++){
                    documentoPaciente = pacientes.get(i);
                    if( this.listaPacientes.get( documentoPaciente ) != null ){
                        this.listaPacientes.remove(documentoPaciente);
                    }
                }
                System.out.println("Error: pacientes ya registrados. Aplicado rollback");
                return false;
            }
            //this.listaPacientes.addAll(pacientes);
        }
        
        //llamado a EPS
        /*
        String servidorEPS = "192.168.0.10";
        try {
            String nombreServicio = "//"+servidorEPS+":"+this.puerto+"/ServicioEPS"+"MiEPS";
            InterfaceEPS serverInterface = (InterfaceEPS) Naming.lookup(nombreServicio); 
            serverInterface.avisar();
        } catch (Exception e) {
            System.out.println(e.toString());        
            return false;
        }
        */
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
