/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EPS;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author adgar
 */
public interface InterfaceEPS extends Remote{
    
    public void avisar() throws RemoteException;
    public boolean pacienteTieneCobertura(String documentoPaciente) throws RemoteException;
    
}
