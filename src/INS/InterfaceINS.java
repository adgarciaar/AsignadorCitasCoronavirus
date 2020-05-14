/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package INS;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author adgar
 */
public interface InterfaceINS extends Remote {
    
    public boolean evaluarPaciente(String nombrePaciente) throws RemoteException;
    
}
