/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GrupoPacientes;

import Entidades.Cita;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author adgar
 */
public interface InterfaceGrupoPacientes extends Remote {
    
    public void informarAsignacionCita(List<Cita> Calendario) throws RemoteException;
    public void informarProblemaCita(String documentoPaciente,String mensaje) throws RemoteException;
    
}
