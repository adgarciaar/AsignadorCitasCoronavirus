/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GrupoPacientes;

import Entidades.Paciente;
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
public class GrupoPacientes extends UnicastRemoteObject implements InterfaceGrupoPacientes {
    
    private String ipGrupoPacientes;
    private String ipServidorCitas;
    private int puertoServidorCitas;
    private String idGrupo;
    HashMap<String, Paciente> pacientes;

    public GrupoPacientes(String ipServidorCitas, int puerto, HashMap<String, Paciente> pacientes
        , String idGrupo) throws RemoteException {
        
        this.ipServidorCitas = ipServidorCitas;
        this.puertoServidorCitas = puerto;
        this.pacientes = pacientes;
        this.idGrupo = idGrupo;
        
        //se consigue la ip de la máquina en que se está ejecutando esta función
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            System.out.println("Error al conseguir la dirección IP de la máquina actual");
            System.exit(1);
        }        
        this.ipGrupoPacientes = inetAddress.getHostAddress();
        
    }
    
    private void registrarServicioRegistro(){
        //Registrar el servicio de esa EPS
        try {
            Registry r = null;
            if(this.ipServidorCitas.equals(this.ipGrupoPacientes)){
                //para poder ejecutarlos en misma máquina
                r = java.rmi.registry.LocateRegistry.getRegistry(this.puertoServidorCitas);
            }else{
                r = java.rmi.registry.LocateRegistry.createRegistry(this.puertoServidorCitas);
            }
            //Registry r = java.rmi.registry.LocateRegistry.createRegistry(this.puertoServidorCitas);
            r.bind("ServicioPacientes" + this.idGrupo, this);
            System.out.println("Servidor del grupo de pacientes activo");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    public void registrarPacientes() {
        
        try {
            String nombreServicio = "//"+this.ipServidorCitas+":"+this.puertoServidorCitas+"/ServAsignacionCitas";
            InterfaceServidorCitas serverInterface = (InterfaceServidorCitas) Naming.lookup(nombreServicio);
            boolean retorno = serverInterface.registrarPacientes(this.pacientes, this.ipGrupoPacientes);
            if(retorno){
                System.out.println("Los pacientes se registraron exitosamente");
                this.registrarServicioRegistro();
            }else{
                System.out.println("Error: no se pudieron registrar los pacientes");
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println(this.pacientes.size());
            System.out.println("Error: "+e.toString());
        }
        
    }

    @Override
    public void informarAsignacionCita() throws RemoteException {
        System.out.println("Cita asignada");
    }
    
    /*public void pedirCita(){
        
        try {
            String nombreServicio = "//"+this.ipServidorCitas+":"+this.puertoServidorCitas+"/ServAsignacionCitas";
            InterfaceServidorCitas serverInterface = (InterfaceServidorCitas) Naming.lookup(nombreServicio);
            boolean sePuede = serverInterface.evaluarUnPaciente("Adrian");
            //String miRetorno = serverInterface.registrarEPS(this.nombre);
            System.out.println("Se puede? "+sePuede);
        } catch (Exception e) {
            System.out.println(e);
        }
    }*/
    
}
