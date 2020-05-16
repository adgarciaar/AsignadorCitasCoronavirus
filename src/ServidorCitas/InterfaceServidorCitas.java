/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServidorCitas;

import Entidades.Paciente;
import GUI.GUIServidorCitas;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 *
 * @author adgar
 */
public interface InterfaceServidorCitas extends Remote{
    
    public void registrarPacientes(HashMap<String, Paciente> pacientes, 
            String ipGrupo, String idGrupo) throws RemoteException;
    public boolean registrarEPS(String nombreEPS, String ipEPS) throws RemoteException;
    public boolean evaluarPacientes() throws RemoteException;
    public void asignarCitas() throws RemoteException;
    public void referenciarGUI(GUIServidorCitas gui) throws RemoteException;
    
}
