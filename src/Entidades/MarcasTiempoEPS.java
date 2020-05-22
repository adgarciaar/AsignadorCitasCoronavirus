/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.time.LocalDateTime;
/**
 *
 * @author juanito
 */
public class MarcasTiempoEPS {
    private LocalDateTime marcaLectura;
    private LocalDateTime marcaEscritura;

    public MarcasTiempoEPS() {
        this.marcaLectura = LocalDateTime.now();
        this.marcaEscritura = LocalDateTime.now();
        
    }
       

    public LocalDateTime getMarcaLectura() {
        return marcaLectura;
    }

    public void setMarcaLectura(LocalDateTime marcaLectura) {
        this.marcaLectura = marcaLectura;
    }

    public LocalDateTime getMarcaEscritura() {
        return marcaEscritura;
    }

    public void setMarcaEscritura(LocalDateTime marcaEscritura) {
        this.marcaEscritura = marcaEscritura;
    }
    
}
