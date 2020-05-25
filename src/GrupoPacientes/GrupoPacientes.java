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
import java.util.List;

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
    //duplas <idPaciente, Id cita>
    private HashMap<String, String> citasPacientes;   
    
    private GUIGrupoPacientes gui;

    public GrupoPacientes(String ipServidorCitas, int puerto, HashMap<String, Paciente> pacientes
        , String idGrupo) throws RemoteException {
        
        this.ipServidorCitas = ipServidorCitas;
        this.puertoServidorCitas = puerto;
        this.pacientes = pacientes;
        this.idGrupo = idGrupo;
        
        this.citasPacientes = new HashMap<>();
        
        //se consigue la ip de la máquina en que se está ejecutando esta función
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            System.out.println("Error al conseguir la dirección IP de la máquina actual");
            System.exit(1);
        }        
        this.ipGrupoPacientes = inetAddress.getHostAddress();
        
        Paciente pacienteTemp;
        for (HashMap.Entry<String, Paciente> entry : this.pacientes.entrySet()) { 
            pacienteTemp = entry.getValue();
            pacienteTemp.setIpGrupo(this.ipGrupoPacientes);
            entry.setValue(pacienteTemp);
        }
        
        this.registrarServicioRegistro();
        this.crearGUI();
        
        //BORRAR        
        /*for (HashMap.Entry<String, Paciente> entry : this.pacientes.entrySet()) { 
            System.out.println("Paciente "+entry.getValue().getNombre());
            this.probarPuntaje(entry.getValue());
        }*/
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
            System.out.println("\nServidor del grupo de pacientes activo: ServicioPacientes"+this.idGrupo+"\n");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
    }
    
    public void crearGUI(){
        this.gui = new GUIGrupoPacientes();        
        this.gui.setLocationRelativeTo(null); //ubicarla en centro de pantalla
        this.gui.setVisible(true);
        this.gui.addRowToJTablePacientes(this.pacientes, citasPacientes);
    }
    
    public void solicitarCitas() {
        
        try {
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
            InterfaceServidorCitas serverInterface =
                    (InterfaceServidorCitas) Naming.lookup(nombreServicio);
            Cita cita = serverInterface.obtenerCita(paciente, this.ipGrupoPacientes, this.idGrupo);
            System.out.println("Solicitada cita para paciente "+paciente.getNombre());
        } catch (Exception e) {
            System.out.println("3Error: "+e.toString());
        }
        
    }

    @Override
    public void informarAsignacionCita(List<Cita> Calendario) throws RemoteException {
        for (int i = 0; i < Calendario.size(); i++) {
            this.citasPacientes.put(Calendario.get(i).getDocumentoPaciente(), Calendario.get(i).getIdCita());
          //  System.out.println("--->"+Calendario.get(i).getDocumentoPaciente()); 
        }
        
        this.gui.addRowToJTablePacientes(this.pacientes, this.citasPacientes);
        System.out.println("Asignación de cita recibida desde servidor de citas:");        
    }
    
    //BORRAR
    /*private void probarPuntaje(Paciente paciente){
        try {
            String nombreServicio = "//" + "192.168.0.9" + ":" + "7770" + "/ServicioINS";
            InterfaceINS serverInterface = (InterfaceINS) Naming.lookup(nombreServicio);
            int puntaje = serverInterface.evaluarPaciente(paciente);
            //return serverInterface.evaluarPaciente(nombrePaciente);   
            System.out.println("Puntaje paciente " + paciente.getNombre() + " es " + puntaje);
            //return puntaje;

        } catch (Exception e) {
            System.out.println(e);
            //return -1;
        }
    }*/

    @Override
    public void informarProblemaCita(String documentoPaciente, String mensaje) throws RemoteException {
        this.citasPacientes.put(documentoPaciente, mensaje);
        this.gui.addRowToJTablePacientes(this.pacientes, this.citasPacientes);
        System.out.println("Mensaje recibido desde servidor de citas:");   
    }
}
