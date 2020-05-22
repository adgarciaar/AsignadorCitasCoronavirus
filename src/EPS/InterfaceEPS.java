/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EPS;

import Entidades.Cita;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author adgar
 */
public interface InterfaceEPS extends Remote{
    
    public boolean pacienteTieneCobertura(String documentoPaciente) throws RemoteException;
    public List<Cita> entregarCalendario() throws RemoteException;
    public void actualizarCalendaro(List <Cita> citas) throws RemoteException;
    public boolean puedeConsumar() throws RemoteException;    
    
}
