/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GrupoPacientes;

import Entidades.Cita;
import Entidades.Paciente;
import GUI.GUIGrupoPacientes;
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
    //duplas <idPaciente, Objeto Paciente>
    private HashMap<String, Paciente> pacientes;
    //duplas <idPaciente, string con situación>
    private HashMap<String, String> situacionPacientes;   
    
    private GUIGrupoPacientes gui;

    public GrupoPacientes(String ipServidorCitas, int puerto, HashMap<String, Paciente> pacientes
        , String idGrupo) throws RemoteException {
        
        this.ipServidorCitas = ipServidorCitas;
        this.puertoServidorCitas = puerto;
        this.pacientes = pacientes;
        this.idGrupo = idGrupo;
        
        this.situacionPacientes = new HashMap<>();
        
        //se consigue la ip de la máquina en que se está ejecutando esta función
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            System.out.println("Error al conseguir la dirección IP de la máquina actual");
            System.exit(1);
        }        
        this.ipGrupoPacientes = inetAddress.getHostAddress();
        
        this.registrarServicioRegistro();
        this.crearGUI();
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
            r.rebind("ServicioPacientes" + this.idGrupo, this);
            System.out.println("Servidor del grupo de pacientes activo: ServicioPacientes"+this.idGrupo);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
    }
    
    public void crearGUI(){
        this.gui = new GUIGrupoPacientes();        
        this.gui.setLocationRelativeTo(null); //ubicarla en centro de pantalla
        this.gui.setVisible(true);
        this.gui.addRowToJTablePacientes(this.pacientes, situacionPacientes);
    }
    
    public void solicitarCitas() {
        
        try {
            
            /*
            this.registrarServicioRegistro();
            
            String nombreServicio = "//"+this.ipServidorCitas+":"
                    +this.puertoServidorCitas+"/ServAsignacionCitas";
            
            InterfaceServidorCitas serverInterface = (
                    InterfaceServidorCitas) Naming.lookup(nombreServicio);
            
            serverInterface.registrarPacientes(this.pacientes, 
                    this.ipGrupoPacientes, this.idGrupo);
            */
            
            for (HashMap.Entry<String, Paciente> entry : this.pacientes.entrySet()) { 
                //iniciar un hilo para solicitar la cita, por cada paciente
                Runnable tarea = () -> { this.solicitarCitaPaciente(entry.getValue()) ;};      
                Thread hilo = new Thread(tarea);
                hilo.start();
            }
            
        } catch (Exception e) {
            System.out.println(this.pacientes.size());
            System.out.println("Error: "+e.toString());
        }
        
    }
    
    private void solicitarCitaPaciente(Paciente paciente){
        
        String nombreServicio = "//"+this.ipServidorCitas+":"
                    +this.puertoServidorCitas+"/ServAsignacionCitas";
            
        try {
            //InterfaceServidorCitas serverInterface =
             //       (InterfaceServidorCitas) Naming.lookup(nombreServicio);
            //Cita cita = serverInterface.obtenerCita(paciente);
            System.out.println("Solicitada cita para paciente "+paciente.getNombre());
        } catch (Exception e) {
            System.out.println("Error: "+e.toString());
        }
        
    }

    @Override
    public void informarAsignacionCita() throws RemoteException {
        System.out.println("Cita asignada");
    }

    @Override
    public void recibirMensaje(String mensaje) throws RemoteException {
        this.gui.addRowToJTablePacientes(this.pacientes, situacionPacientes);
        System.out.println("Mensaje recibido desde servidor de citas:");
        System.out.println(mensaje);
    }
    
}
