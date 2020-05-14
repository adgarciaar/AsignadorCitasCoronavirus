/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EPS;

import ServidorCitas.InterfaceServidorCitas;
import java.rmi.Naming;

/**
 *
 * @author adgar
 */
public class EPS {
    
    private String ipServidorCitas;
    private int puerto;
    private String nombre;

    public EPS(String ipServidor, int puerto) {
        this.ipServidorCitas = ipServidor;
        this.puerto = puerto;
        this.nombre = "Nueva EPS";
    }
    
    public void registrarEPS() {
        
        try {
            String nombreServicio = "//"+this.ipServidorCitas+":"+this.puerto+"/ServAsignacionCitas";
            InterfaceServidorCitas serverInterface = (InterfaceServidorCitas) Naming.lookup(nombreServicio);
            String miRetorno = serverInterface.registrarEPS(this.nombre);
            System.out.println(miRetorno);
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
    
}
