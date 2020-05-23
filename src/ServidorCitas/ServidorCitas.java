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
import java.util.List;
import java.util.Random;

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
    //mapa con duplas <Documento paciente, IP de la máquina de su grupo>
    private HashMap<String, String> listaIPsPacientes;
    //mapa con duplas <Documento paciente, Paciente>
    private HashMap<String, Paciente> listaPacientes;
    //mapa con duplas <Documento paciente, Id de su grupo>
    private HashMap<String, String> listaPacientesGrupos;

    private String ipServidorINS;
    private int puertoINS;

    private GUIServidorCitas gui;

    private ArrayList<Transaccion> transacciones;

    public ServidorCitas(int puerto, String ipServidorINS, int puertoINS) throws RemoteException {
        this.puerto = puerto;
        //this.ipServidorINS = "localhost";
        //this.puertoINS = 7770;
        this.ipServidorINS = ipServidorINS;
        this.puertoINS = puertoINS;

        this.listaEPSs = new HashMap<>();
        this.marcasTiempoEPSs = new HashMap<>();
        this.listaIPsPacientes = new HashMap<>();
        this.listaPacientes = new HashMap<>();
        this.listaPacientesGrupos = new HashMap<>();

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

    @Override
    public void registrarPacientes(HashMap<String, Paciente> pacientes,
            String ipGrupo, String idGrupo) throws RemoteException {

        /*
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
         */
    }

    public int evaluarPaciente(Paciente paciente) {

        try {
            String nombreServicio = "//" + this.ipServidorINS + ":" + this.puertoINS + "/ServicioINS";
            InterfaceINS serverInterface = (InterfaceINS) Naming.lookup(nombreServicio);
            int puntaje = serverInterface.evaluarPaciente(paciente);
            //return serverInterface.evaluarPaciente(nombrePaciente);   
            System.out.println("Puntaje paciente " + paciente.getNombre() + " es " + puntaje);
            return puntaje;

        } catch (Exception e) {
            System.out.println(e);
            return -1;
        }
    }

    public void verificarEPSPacientes(HashMap<String, Paciente> pacientes) {

        /*
        synchronized(this) {
            for (HashMap.Entry<String, Paciente> entry : pacientes.entrySet()) {                
                String documentoPaciente = entry.getKey();
                Paciente paciente = entry.getValue();
                String EPSPaciente = paciente.getEPS();
                String ipEPS = this.listaEPSs.get(EPSPaciente);    
                if(this.verificarEPSPaciente(documentoPaciente, EPSPaciente, ipEPS)){
                    System.out.println("Paciente con documento "+documentoPaciente+" tiene EPS válida");
                    this.evaluarPaciente(paciente);
                }else{
                    String mensaje = "Paciente con documento "+documentoPaciente+" no tiene EPS válida";
                    System.out.println(mensaje);
                    String ipGrupo = this.listaIPsPacientes.get(documentoPaciente);
                    String idGrupo = this.listaPacientesGrupos.get(documentoPaciente);
                    this.enviarMensajeGrupoPacientes(mensaje, ipGrupo, idGrupo);
                }                
            }
            this.asignarCitas();
        }
         */
    }

    public boolean verificarEPSPaciente(String documentoPaciente, String nombreEPS, String ipEPS) {

        try {
            String nombreServicio = "//" + ipEPS + ":" + this.puerto + "/ServicioEPS" + nombreEPS;
            InterfaceEPS serverInterface = (InterfaceEPS) Naming.lookup(nombreServicio);
            return serverInterface.pacienteTieneCobertura(documentoPaciente);
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }

    }

    /*
    public void enviarMensajeGrupoPacientes(String mensaje, String ipGrupo, String idGrupo){
        
        try {
            String nombreServicio = "//"+ipGrupo+":"+this.puerto+"/ServicioPacientes"+idGrupo;            
            InterfaceGrupoPacientes serverInterface = 
                    (InterfaceGrupoPacientes) Naming.lookup(nombreServicio);
            
            serverInterface.recibirMensaje(mensaje);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
    }*/
 /*public void asignarCitas(){
        System.out.println("Asignando citas");
    }*/
    @Override
    public void crearGUI() throws RemoteException {
        this.gui = new GUIServidorCitas();
        this.gui.setLocationRelativeTo(null); //ubicarla en centro de pantalla
        this.gui.setVisible(true);
    }
    
    /*
    public void obtenerCitaSinMarcas(Paciente paciente){
        Cita cita = null;

        Transaccion transaccion = new Transaccion(paciente);

        synchronized (this.transacciones) {
            this.transacciones.add(transaccion);
            this.transacciones.sort(null);
        }

        boolean transaccionConsumada = false;   
        
        
        String ipEPSPaciente = this.listaEPSs.get(paciente.getEPS());
        synchronized (this) {
            boolean pacienteCuentaConEPS
                    = this.verificarEPSPaciente(paciente.getDocumento(),
                            paciente.getEPS(), ipEPSPaciente);

            if (pacienteCuentaConEPS) {

                List<Cita> Calendario;
                Calendario = traerCalendario(ipEPSPaciente, paciente.getEPS());
                int prioridad = evaluarPaciente(paciente);
                //int hora = Calendario.get(Calendario.size() - 1).getHora();
                //int dia = Calendario.get(Calendario.size() - 1).getDia();
                Random rand = new Random();
                int hora = rand.nextInt(100); 
                int dia = rand.nextInt(100); 
                Calendario.add(new Cita(paciente.getDocumento(), prioridad, dia, hora)); 

                if (prioridad >= 10 && prioridad < 90) {

                    //Calendario.add(new Cita(paciente.getDocumento(), prioridad, dia, hora));
                    actualizarCalendarioEPS(Calendario, ipEPSPaciente, paciente.getEPS());
                }

                //this.transacciones.notifyAll();
            } else { //Abortar la transacción
                System.out.println("Transacción abortada");
                transaccionConsumada = true;
            }
        }
    }
    */

    @Override
    public Cita obtenerCita(Paciente paciente) {

        Cita cita = null;

        Transaccion transaccion = new Transaccion(paciente);

        synchronized (this.transacciones) {
            this.transacciones.add(transaccion);
            this.transacciones.sort(null);
        }

        boolean transaccionConsumada = false;  
        
        while (!transaccionConsumada) {

            synchronized (this.transacciones) {
                //ver si esta transaccion es la siguiente en la lista
                if (transaccion.getTimeStamp().equals(this.transacciones.get(0).getTimeStamp())) {

                    String ipEPSPaciente = this.listaEPSs.get(paciente.getEPS());
                    boolean pacienteCuentaConEPS
                            = this.verificarEPSPaciente(paciente.getDocumento(),
                                    paciente.getEPS(), ipEPSPaciente);
                    
                    System.out.println("Paciente "+paciente.getNombre()+" eps: "+pacienteCuentaConEPS);

                    if (pacienteCuentaConEPS) {
                        System.out.println("Se va a consumar para "+paciente.getNombre());
                        //boolean consumada = this.consumarCita(this.transacciones.get(0));
                        boolean consumada = this.consumarCita(transaccion);
                        this.transacciones.remove(0);
                        if (!consumada) {
                            transacciones.add(new Transaccion(paciente));
                        }
                        transaccionConsumada = consumada;
                        this.transacciones.notify();
                    } else { //Abortar la transacción
                        System.out.println("Transacción abortada para paciente "+paciente.getNombre());
                        transaccionConsumada = true;
                    }
                } else {
                    try {
                        this.transacciones.wait();
                    } catch (InterruptedException e) {
                        System.out.println("Error: " + e.toString());
                    }

                }
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
                System.out.println("Inconsistencia de concurrencia lectura del paciente:" + paciente.getDocumento());
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
                
                /*Random rand = new Random();
                int hora = rand.nextInt(200); 
                int dia = rand.nextInt(200); */
                
                int hora = 0;
                int dia = 0;
                if (!Calendario.isEmpty()) {
                    hora = Calendario.get(Calendario.size() - 1).getHora();
                    dia = Calendario.get(Calendario.size() - 1).getDia();
                }
                
                if (hora == 8) {
                    dia++;
                }else
                    hora++;

                if (prioridad >= 70 && prioridad < 90) {

                    Calendario.add(new Cita(paciente.getDocumento(), prioridad, dia, hora));

                }

                if (prioridad >= 90) {

                    int index = buscarMenorPrioridad(Calendario);
                    Calendario.add(new Cita(paciente.getDocumento(), prioridad, dia, hora));
                    Collections.swap(Calendario, index, Calendario.size() - 1);

                }

                Collections.sort(Calendario);
                
                
                actualizarCalendarioEPS(Calendario, ipEPS, EPSPaciente); //borrar esta
                
                /*if (puedeConsumar(ipEPS, EPSPaciente)) {

                    actualizarCalendarioEPS(Calendario, ipEPS, EPSPaciente);

                } else {
                    try {
                        this.wait();
                    } catch (Exception e) {
                        System.out.println("Thread interrupted.");
                    }
                }*/

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
            System.out.println(e.toString());
        }
    }
    
    private boolean puedeConsumar(String ipEPS, String EPSPaciente) {
        try {
            String nombreServicio = "//" + ipEPS + ":" + this.puerto + "/ServicioEPS" + EPSPaciente;
            InterfaceEPS serverInterface = (InterfaceEPS) Naming.lookup(nombreServicio);
            return serverInterface.puedeConsumar();
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
}
