/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServidorCitas;

import EPS.InterfaceEPS;
import Entidades.Paciente;
import GUI.GUIServidorCitas;
import GrupoPacientes.InterfaceGrupoPacientes;
import INS.InterfaceINS;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 *
 * @author adgar
 */
public class ServidorCitas extends UnicastRemoteObject implements InterfaceServidorCitas {
    
    private int puerto;
    //mapa con duplas <EPS, IP de la máquina>
    private HashMap<String, String> listaEPSs;
    //mapa con duplas <Documento paciente, IP de la máquina de su grupo>
    private HashMap<String, String> listaIPsPacientes;
    //mapa con duplas <Documento paciente, Paciente>
    private HashMap<String, Paciente> listaPacientes;
    //mapa con duplas <Documento paciente, Id de su grupo>
    private HashMap<String, String> listaPacientesGrupos;
    
    private String ipServidorINS;
    private int puertoINS;
    
    GUIServidorCitas gui;

    public ServidorCitas(int puerto) throws RemoteException{
        this.puerto = puerto;
        this.listaEPSs = new HashMap<>();
        this.listaIPsPacientes = new HashMap<>();
        this.listaPacientes = new HashMap<>();
        this.listaPacientesGrupos = new HashMap<>();
        
        this.ipServidorINS = "localhost";
        this.puertoINS = 7770;
    }

    @Override
    public boolean registrarEPS(String nombreEPS, String ipEPS) throws RemoteException {
        
        synchronized(this) {
            
            if(this.listaEPSs.get(nombreEPS) == null){ //no está registrada                
                this.listaEPSs.put(nombreEPS, ipEPS);
                System.out.println("Registrada la EPS "+nombreEPS);
                this.gui.addRowToJTableEPS(this.listaEPSs);
                return true;
            }else{
                System.out.println("La EPS "+nombreEPS+" ya se está registrada");
                return false;
            }
        }       
    }

    @Override
    public void registrarPacientes(HashMap<String, Paciente> pacientes, 
            String ipGrupo, String idGrupo) throws RemoteException {
        
        boolean registroCorrecto = true;
        
        synchronized(this) {
            
            String documentoPaciente;
            
            for (HashMap.Entry<String, Paciente> entry : pacientes.entrySet()) { 
                
                documentoPaciente = entry.getKey();
                Paciente paciente = entry.getValue();
                
                if( this.listaIPsPacientes.get( documentoPaciente ) == null ){
                    //el paciente no se ha registrado previamente
                    this.listaIPsPacientes.put(documentoPaciente, ipGrupo);
                    this.listaPacientes.put(documentoPaciente, paciente);
                    this.listaPacientesGrupos.put(documentoPaciente, idGrupo);
                }else{
                    //retorno = false;
                    break;
                }             
            }
            
            if(registroCorrecto){
                
                System.out.println("Se agregaron los pacientes: "+pacientes);
                this.gui.addRowToJTablePacientes(this.listaIPsPacientes);
                
                this.verificarEPSPacientes(pacientes);
                //return registroCorrecto;
                
            }else{
                //hacer rollback (remover los que se alcanzaron a agregar)
                for (HashMap.Entry<String, Paciente> entry : pacientes.entrySet()) { 
                    documentoPaciente = entry.getKey();
                    if( this.listaIPsPacientes.get( documentoPaciente ) != null ){
                        this.listaIPsPacientes.remove(documentoPaciente);
                        this.listaPacientes.remove(documentoPaciente);
                    }
                }
                System.out.println("Error: pacientes ya registrados. Aplicado rollback");
                //return false;
            }
            //this.listaPacientes.addAll(pacientes);
        }        
    }

    @Override
    public boolean evaluarPacientes() throws RemoteException {
        
        try {
            String nombreServicio = "//"+this.ipServidorINS+":"+this.puertoINS+"/ServicioINS";
            InterfaceINS serverInterface = (InterfaceINS) Naming.lookup(nombreServicio);
            //return serverInterface.evaluarPaciente(nombrePaciente);   
            //return serverInterface.evaluarPaciente("Algo");
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        
    }
    
    public void verificarEPSPacientes(HashMap<String, Paciente> pacientes){
        synchronized(this) {
            for (HashMap.Entry<String, Paciente> entry : pacientes.entrySet()) {                
                String documentoPaciente = entry.getKey();
                Paciente paciente = entry.getValue();
                String EPSPaciente = paciente.getEPS();
                String ipEPS = this.listaEPSs.get(EPSPaciente);    
                if(this.verificarEPSPaciente(documentoPaciente, EPSPaciente, ipEPS)){
                    System.out.println("Paciente con documento "+documentoPaciente+" tiene EPS válida");
                }else{
                    String mensaje = "Paciente con documento "+documentoPaciente+" no tiene EPS válida";
                    System.out.println(mensaje);
                    String ipGrupo = this.listaIPsPacientes.get(documentoPaciente);
                    String idGrupo = this.listaPacientesGrupos.get(documentoPaciente);
                    this.enviarMensajeGrupoPacientes(mensaje, ipGrupo, idGrupo);
                }
            }
        }
    }
    
    public boolean verificarEPSPaciente(String documentoPaciente, String nombreEPS, String ipEPS){     
        
        try {
            String nombreServicio = "//"+ipEPS+":"+this.puerto+"/ServicioEPS"+nombreEPS;
            InterfaceEPS serverInterface = (InterfaceEPS) Naming.lookup(nombreServicio); 
            return serverInterface.pacienteTieneCobertura(documentoPaciente);            
        } catch (Exception e) {
            System.out.println(e.toString());        
            return false;
        }
        
    }
    
    public void enviarMensajeGrupoPacientes(String mensaje, String ipGrupo, String idGrupo){
        
        try {
            String nombreServicio = "//"+ipGrupo+":"+this.puerto+"/ServicioPacientes"+idGrupo;
            //System.out.println(nombreServicio);
            InterfaceGrupoPacientes serverInterface = 
                    (InterfaceGrupoPacientes) Naming.lookup(nombreServicio);
            
            serverInterface.recibirMensaje(mensaje);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
    }
    
    @Override
    public void asignarCitas() throws RemoteException {
        System.out.println("Asignando citas");
    }

    @Override
    public void referenciarGUI(GUIServidorCitas gui) throws RemoteException{
       this.gui = gui;
    }
    
}
