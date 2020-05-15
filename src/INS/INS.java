/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package INS;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author adgar
 */
public class INS extends UnicastRemoteObject implements InterfaceINS {
    
    private int puerto;

    public INS(int puerto) throws RemoteException{
        this.puerto = puerto;
    }

    @Override
    public boolean evaluarPaciente(String nombrePaciente) throws RemoteException {
        System.out.println("Paciente "+nombrePaciente+" fue evaluado");
        return false;
    }
    
}
