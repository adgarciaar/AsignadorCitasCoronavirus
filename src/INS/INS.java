/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package INS;

import Entidades.Paciente;
import GUI.GUI_INS;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author adgar
 */
public class INS extends UnicastRemoteObject implements InterfaceINS {
    
    private int puerto;
    private HashMap<String, String> reportes;
    
    private GUI_INS gui;

    public INS(int puerto) throws RemoteException{
        this.puerto = puerto;
        reportes = new HashMap<>();
    }

    @Override
    public int evaluarPaciente(Paciente paciente) throws RemoteException {
        
        int puntaje = 0;  
        String sintoma;
        List<String> sintomas = paciente.getSintomas();
        for(int i=0; i<sintomas.size(); i++){
            sintoma = sintomas.get(i);
            switch (sintoma) {
                case "bien":
                    puntaje = puntaje + 1;
                    break;
                case "enfermo":
                    puntaje = puntaje + 70;
                    break;
            }
        }
        
        this.gui.addRowToJTablePacientes(paciente.getDocumento(), paciente.getNombre(), puntaje);
        
        System.out.println("Paciente "+paciente.getNombre()+" con documento "
                +paciente.getDocumento()+" fue evaluado y "
                + "obtuvo "+puntaje+" puntaje");
        
        return puntaje;
    }

    @Override
    public void referenciarGUI(GUI_INS gui) throws RemoteException {
        this.gui = gui;
    }
    
}
