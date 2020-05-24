/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServidorCitas;

import Entidades.Cita;
import Entidades.Paciente;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author adgar
 */
public interface InterfaceServidorCitas extends Remote{
    
    public Cita obtenerCita(Paciente paciente, String ipGrupo, String idGrupo) throws RemoteException; 
    public boolean registrarEPS(String nombreEPS, String ipEPS) throws RemoteException;
    public void crearGUI() throws RemoteException;
    
}
