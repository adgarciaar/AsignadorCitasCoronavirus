/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServidorCitas;

import EPS.InterfaceEPS;
import Entidades.Cita;
import Entidades.MarcasTiempoEPS;
import Entidades.Paciente;
import Entidades.Transaccion;
import GUI.GUIServidorCitas;
import GrupoPacientes.InterfaceGrupoPacientes;
import INS.InterfaceINS;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 *
 * @author adgar
 */
public class ServidorCitas extends UnicastRemoteObject implements InterfaceServidorCitas {

    private int puerto;
    //mapa con duplas <EPS, IP de la máquina>
    private HashMap<String, String> listaEPSs;
    //maoa con duplas <EPS, MarcaTiempoEPS>
    private HashMap<String, MarcasTiempoEPS> marcasTiempoEPSs;
    //mapa con duplas <Documento paciente, Paciente>
    private HashMap<String, Paciente> listaPacientes;
    private HashMap<String, String> listaGruposPacientes;
    private String ipServidorINS;
    private int puertoINS;

    private GUIServidorCitas gui;

    private ArrayList<Transaccion> transacciones;

    public ServidorCitas(int puerto, String ipServidorINS, int puertoINS) throws RemoteException {
        this.puerto = puerto;        
        this.ipServidorINS = ipServidorINS;
        this.puertoINS = puertoINS;

        this.listaGruposPacientes = new HashMap<>();
        this.listaEPSs = new HashMap<>();
        this.marcasTiempoEPSs = new HashMap<>();       
        this.listaPacientes = new HashMap<>();  
        this.transacciones = new ArrayList<>();
    }

    @Override
    public boolean registrarEPS(String nombreEPS, String ipEPS) throws RemoteException {

        synchronized (this.listaEPSs) {

            if (this.listaEPSs.get(nombreEPS) == null) { //no está registrada                
                this.listaEPSs.put(nombreEPS, ipEPS);
                this.marcasTiempoEPSs.put(nombreEPS, new MarcasTiempoEPS());
                System.out.println("Registrada la EPS " + nombreEPS);
                this.gui.addRowToJTableEPS(nombreEPS, ipEPS);
                
                return true;
            } else {
                System.out.println("La EPS " + nombreEPS + " ya se está registrada");
                return false;
            }
        }
    }

    public int evaluarPaciente(Paciente paciente) {

        try {
            String nombreServicio = "//" + this.ipServidorINS + ":" + this.puertoINS + "/ServicioINS";
            InterfaceINS serverInterface = (InterfaceINS) Naming.lookup(nombreServicio);
            int puntaje = serverInterface.evaluarPaciente(paciente);            
            System.out.println("Puntaje paciente " + paciente.getNombre() + " es " + puntaje);
            return puntaje;

        } catch (Exception e) {
            System.out.println(e);
            return -1;
        }
    }

    public boolean verificarEPSPaciente(String documentoPaciente, String nombreEPS, String ipEPS) {

        try {
            String nombreServicio = "//" + ipEPS + ":" + this.puerto + "/ServicioEPS" + nombreEPS;
            InterfaceEPS serverInterface = (InterfaceEPS) Naming.lookup(nombreServicio);
            return serverInterface.pacienteTieneCobertura(documentoPaciente);
        } catch (Exception e) {
            System.out.println("1"+e.toString());
            return false;
        }

    }
   
    public void enviarCitaGrupoPacientes(List<Cita> Calendario, String ipGrupo, String idGrupo){
        
        try {
            String nombreServicio = "//"+ipGrupo+":"+this.puerto+"/ServicioPacientes"+idGrupo;            
            InterfaceGrupoPacientes serverInterface = 
                    (InterfaceGrupoPacientes) Naming.lookup(nombreServicio);
            
            serverInterface.informarAsignacionCita(Calendario);
        } catch (Exception e) {
            System.out.println("Error al informar grupo de paciente: "+e.toString());
        }
        
    }
    
    public void enviarMensajeGrupoPacientes(String documentoPaciente, 
            String mensaje, String ipGrupo, String idGrupo){
        
        try {
            String nombreServicio = "//"+ipGrupo+":"+this.puerto+"/ServicioPacientes"+idGrupo;            
            InterfaceGrupoPacientes serverInterface = 
                    (InterfaceGrupoPacientes) Naming.lookup(nombreServicio);
            
            serverInterface.informarProblemaCita(documentoPaciente, mensaje);
        } catch (Exception e) {
            System.out.println("Error al informar grupo de paciente: "+e.toString());
        }
        
    }
 
    @Override
    public void crearGUI() throws RemoteException {
        this.gui = new GUIServidorCitas();
        this.gui.setLocationRelativeTo(null); //ubicarla en centro de pantalla
        this.gui.setVisible(true);
    }

    @Override
    public Cita obtenerCita(Paciente paciente, String ipGrupo, String idGrupo) {

        Cita cita = null;

        Transaccion transaccion = new Transaccion(paciente);

        synchronized (this.transacciones) {
            this.transacciones.add(transaccion);
            this.transacciones.sort(null);  
            this.gui.addRowToJTablePacientes(paciente);
            this.listaPacientes.put(paciente.getDocumento(), paciente);
            this.listaGruposPacientes.put(ipGrupo, idGrupo);
        }
        
        /*synchronized(this.listaPacientes){
            this.listaPacientes.put(paciente.getDocumento(), paciente);
            this.gui.addRowToJTablePacientes(this.listaPacientes);
        }*/

        boolean transaccionConsumada = false;  
        
        while (!transaccionConsumada) {

            synchronized (this.transacciones) {
                //ver si esta transaccion es la siguiente en la lista
                //if (transaccion.getTimeStamp().equals(this.transacciones.get(0).getTimeStamp())) {

                    String ipEPSPaciente = this.listaEPSs.get(paciente.getEPS());
                    boolean pacienteCuentaConEPS
                            = this.verificarEPSPaciente(paciente.getDocumento(),
                                    paciente.getEPS(), ipEPSPaciente);
                    
                    //System.out.println("Paciente "+paciente.getNombre()+" eps: "+pacienteCuentaConEPS);

                    if (pacienteCuentaConEPS) {
                        //System.out.println("Se va a consumar para "+paciente.getNombre());
                        //boolean consumada = this.consumarCita(this.transacciones.get(0));
                        boolean consumada = this.consumarCita(transaccion);
                        this.transacciones.clear();
                        if (!consumada) {
                            transaccion = new Transaccion(paciente); 
                            this.transacciones.add(transaccion);
                            /*for(int i = 0; i < this.transacciones.size();i++ ){
                                Paciente paciente1 = (Paciente) this.transacciones.get(i).getObjeto();
                                System.out.println( paciente1.getNombre() +"  "+ this.transacciones.get(i).getTimeStamp() );
                            }*/
                            continue;
                        }
                        transaccionConsumada = consumada;
                      //  this.transacciones.notify();
                      System.out.println("Transacción consumada para paciente "+paciente.getNombre());
                    } else { //Abortar la transacción
                        this.enviarMensajeGrupoPacientes(paciente.getDocumento(),
                            "No activo en EPS", ipGrupo, idGrupo);
                        System.out.println("---Transacción abortada para paciente "+paciente.getNombre());
                        transaccionConsumada = true;
                        this.enviarMensajeGrupoPacientes(paciente.getDocumento(),
                            "No activo en EPS", ipGrupo, idGrupo);
                    }
               /* } else {
                    System.out.println("--Transacción abortada para paciente "+paciente.getNombre());
                    if (!this.transacciones.isEmpty()){                        
                        this.transacciones.clear();                       
                    }
                    transaccion = new Transaccion(paciente); 
                    this.transacciones.add(transaccion);
                    
                    transaccionConsumada = false;

                }*/
            }

        }

        return null;
    }

    public List<Cita> traerCalendario(String ipEPS, String nombreEPS) {
        try {
            String nombreServicio = "//" + ipEPS + ":" + this.puerto + "/ServicioEPS" + nombreEPS;
            InterfaceEPS serverInterface = (InterfaceEPS) Naming.lookup(nombreServicio);
            return serverInterface.entregarCalendario();
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

     public boolean consumarCita(Transaccion transaccion) {
        synchronized (this) {            
            Paciente paciente = (Paciente) transaccion.getObjeto();
            System.out.println("Consumando para paciente "+paciente.getNombre());
            String EPSPaciente = paciente.getEPS();
            String ipEPS = this.listaEPSs.get(EPSPaciente);
            List<Cita> Calendario;
            MarcasTiempoEPS marcasEPS = this.marcasTiempoEPSs.get(EPSPaciente);
            if (transaccion.getTimeStamp().isAfter(marcasEPS.getMarcaEscritura())) {
                //solicitar info citas eps
                Calendario = traerCalendario(ipEPS, EPSPaciente);

                marcasEPS.setMarcaLectura(transaccion.getTimeStamp());
            } else {
                System.out.println("Inconsistencia de concurrencia lectura del paciente:" + paciente.getNombre());
                return false;
            }

            if (transaccion.getTimeStamp().compareTo(marcasEPS.getMarcaLectura()) >= 0
                    && transaccion.getTimeStamp().isAfter(marcasEPS.getMarcaEscritura())) {

                //aumentar secuencia cita eps
                //calcular prioridad
                int prioridad = evaluarPaciente(paciente);
                if (prioridad < 0) {
                    return false;
                }

                /*int hora = Calendario.get(Calendario.size() - 1).getHora();
                int dia = Calendario.get(Calendario.size() - 1).getDia();*/
                
                int hora = 0;
                int dia = 1;
                if (!Calendario.isEmpty()) {
                    hora = Calendario.get(Calendario.size() - 1).getHora();
                    dia = Calendario.get(Calendario.size() - 1).getDia();
                }
                
                if (hora == 8) {
                    dia++;
                }else
                    hora++;
                
                if(prioridad < 70){
                    String ipGrupo = paciente.getIpGrupo();
                    String idGrupo = paciente.getIdGrupo(); 
                    this.enviarMensajeGrupoPacientes(paciente.getDocumento(),
                            "No elegible para cita", ipGrupo, idGrupo);
                    return true;
                }

                if (prioridad >= 70 && prioridad < 90) {
                    
                    Cita nuevaCita = new Cita(paciente.getDocumento(), prioridad, dia, hora);
                    Calendario.add(nuevaCita);
                    String ipGrupo = paciente.getIpGrupo();
                    String idGrupo = paciente.getIdGrupo();     

                    
                    //this.enviarCitaGrupoPacientes(Calendario, ipGrupo, idGrupo);
                    
                }

                if (prioridad >= 90) {
                    
                    if (!Calendario.isEmpty()) {
                        int horaAux,diaAux;
                        Cita citaAux;
                        int index = buscarMenorPrioridad(Calendario);
                        citaAux = Calendario.get(index);
                        horaAux = citaAux.getHora();
                        diaAux = citaAux.getDia();
                        
                        Cita nuevaCita = new Cita(paciente.getDocumento(), prioridad, diaAux, horaAux);
                        Calendario.add(nuevaCita);
                        citaAux.setDia(dia);
                        citaAux.setHora(hora);
                        
                        String ipGrupo = paciente.getIpGrupo();
                        String idGrupo = paciente.getIdGrupo();       
                        

                        //this.enviarCitaGrupoPacientes(Calendario, ipGrupo, idGrupo);
                        
                    }else{
                        Cita nuevaCita = new Cita(paciente.getDocumento(), prioridad, dia, hora);
                        Calendario.add(nuevaCita);
                        String ipGrupo = paciente.getIpGrupo();
                        String idGrupo = paciente.getIdGrupo();  

                    }
                }

                

                
               // actualizarCalendarioEPS(Calendario, ipEPS, EPSPaciente); //borrar esta
               
               if (transaccion.getTimeStamp().compareTo(marcasEPS.getMarcaLectura()) >= 0
                    && transaccion.getTimeStamp().compareTo(marcasEPS.getMarcaEscritura())>= 0) {
                   for (String i : listaGruposPacientes.keySet()) {
                            this.enviarCitaGrupoPacientes(Calendario, i, listaGruposPacientes.get(i));
                        }
                Collections.sort(Calendario);
                if (puedeConsumar(ipEPS, EPSPaciente)) {

                    actualizarCalendarioEPS(Calendario, ipEPS, EPSPaciente);
                    System.out.println("respuesta OK");

                } else {
                    System.out.println("respuesta CANCEl");
                    return false;
                }
                marcasEPS.setMarcaEscritura(transaccion.getTimeStamp());
               }else 
                   return false;

                //notificar cita consumada
            } else {
                System.out.println("Inconsistencia de concurrencia escritura del paciente:" + paciente.getDocumento());
                return false;
            }

        }
        return true;
    }

    private int buscarMenorPrioridad(List<Cita> Calendario) {
        int index = 0;
        int i = 0;
        boolean encontro = false;
        while (!encontro) {
            if (Calendario.get(i).getPrioridad() < 90) {
                index = i;
                encontro = true;
            }
            i++;

        }
        return index;
    }

    private void actualizarCalendarioEPS(List<Cita> Calendario, String ipEPS, String EPSPaciente) {
        try {
            String nombreServicio = "//" + ipEPS + ":" + this.puerto + "/ServicioEPS" + EPSPaciente;
            InterfaceEPS serverInterface = (InterfaceEPS) Naming.lookup(nombreServicio);
            serverInterface.actualizarCalendaro(Calendario);
        } catch (Exception e) {
            System.out.println("2"+e.toString());
        }
    }
    
    private boolean puedeConsumar(String ipEPS, String EPSPaciente) {
        try {
            String nombreServicio = "//" + ipEPS + ":" + this.puerto + "/ServicioEPS" + EPSPaciente;
            InterfaceEPS serverInterface = (InterfaceEPS) Naming.lookup(nombreServicio);
            return serverInterface.puedeConsumar();
        } catch (Exception e) {
            System.out.println("3"+e.toString());
            return false;
        }
    }
    
}
