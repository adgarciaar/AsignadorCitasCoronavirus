/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServidorCitas;

import EPS.InterfaceEPS;
import Entidades.Message;
import GUI.GUIServidorCitas;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author adgar
 */
public interface InterfaceServidorCitas extends Remote{
    
    public Message retornarMensaje() throws RemoteException;
    public boolean registrarPacientes(List<String> pacientes, String ipGrupo) throws RemoteException;
    public boolean registrarEPS(String nombreEPS, String ipEPS) throws RemoteException;
    public boolean evaluarUnPaciente(String nombrePaciente) throws RemoteException;
    public void asignarCitas() throws RemoteException;
    public void referenciarGUI(GUIServidorCitas gui) throws RemoteException;
    
}
