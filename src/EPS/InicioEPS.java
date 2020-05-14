/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EPS;

import EPS.EPS;

/**
 *
 * @author adgar
 */
public class InicioEPS {
    
    public static void main(String args[]) {
        String ipServidor = "localhost";
        int puerto = 7771;
        EPS cliente = new EPS(ipServidor, puerto);
        cliente.registrarEPS();
    }
    
}
