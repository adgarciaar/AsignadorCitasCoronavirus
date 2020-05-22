/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EPS;

import Entidades.Cita;
import Entidades.Paciente;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author adgar
 */
public interface InterfaceEPS extends Remote{
    
    public Cita programarCita(Paciente paciente) throws RemoteException;
    public boolean pacienteTieneCobertura(String documentoPaciente) throws RemoteException;
    public void avisar() throws RemoteException;
    public List <Cita> entregarCalendario() throws RemoteException;
    public void actualizarCalendaro(List <Cita> citas) throws RemoteException;
    public boolean puedeConsumar();
    
    
}
