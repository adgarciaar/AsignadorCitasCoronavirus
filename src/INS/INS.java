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
    //duplas <Síntoma, Gravedad>
    private HashMap<String, String> gravedadSintomas;
    //duplas <Patología, Gravedad>
    private HashMap<String, String> gravedadPatologiasAntecedentes;
    
    private GUI_INS gui;

    public INS(int puerto) throws RemoteException{
        this.puerto = puerto;
        this.reportes = new HashMap<>();
        this.gravedadSintomas = new HashMap<>();
        this.gravedadPatologiasAntecedentes = new HashMap<>();
        this.establecerGravedadSintomasPatologiasAntecedentes();
    }
    
    private void establecerGravedadSintomasPatologiasAntecedentes(){
        
        //síntomas
        
        //leves
        this.gravedadSintomas.put("Fiebre", "Leve");
        this.gravedadSintomas.put("Tos", "Leve");
        this.gravedadSintomas.put("Cansancio", "Leve");
        this.gravedadSintomas.put("Dolor", "Leve");
        //graves
        this.gravedadSintomas.put("Falta de aire y dificultad para respirar", "Grave");
        this.gravedadSintomas.put("Insuficiencia pulmonar", "Grave");
        this.gravedadSintomas.put("Shock séptico", "Grave");
        this.gravedadSintomas.put("Falla multiorgánica", "Grave");
        
        //patologías y antecedentes
        
        this.gravedadPatologiasAntecedentes.put("Sinusitis", "Leve");
        this.gravedadPatologiasAntecedentes.put("Rinitis", "Leve");
        this.gravedadPatologiasAntecedentes.put("Obesidad leve", "Leve");
        this.gravedadPatologiasAntecedentes.put("Embarazo", "Moderado");        
        //graves
        this.gravedadPatologiasAntecedentes.put("Diabetes", "Grave");
        this.gravedadPatologiasAntecedentes.put("EPOC", "Grave");
        this.gravedadPatologiasAntecedentes.put("Asma", "Grave");
        this.gravedadPatologiasAntecedentes.put("Cáncer", "Grave");
        this.gravedadPatologiasAntecedentes.put("Accidente cerebrovascular", "Grave");
        this.gravedadPatologiasAntecedentes.put("Presión arterial alta", "Grave");
        this.gravedadPatologiasAntecedentes.put("Enfermedad renal crónica", "Grave");
        this.gravedadPatologiasAntecedentes.put("Enfermedad hepática", "Grave");
        this.gravedadPatologiasAntecedentes.put("Cirugía del corazón", "Grave");
        this.gravedadPatologiasAntecedentes.put("Cirugía de pulmón", "Grave");
        this.gravedadPatologiasAntecedentes.put("Sistema inmunitario deprimido", "Grave");
        this.gravedadPatologiasAntecedentes.put("Obesidad grave", "Grave");
    }
    
    public void crearGUI() throws RemoteException{
        this.gui = new GUI_INS();
        gui.setLocationRelativeTo(null); //ubicarla en centro de pantalla
        gui.setVisible(true);
    }

    @Override
    public int evaluarPaciente(Paciente paciente) throws RemoteException {
        
        int puntaje = 0;  
        
        //60 puntos-------------
        
        //20 puntos -> síntomas leves
        //5 por cada síntoma leve
        
        //20 puntos -> edad
        //dependiendo del rango de edad
        
        //20 puntos -> patologías leves y moderadas
        //5 por cada patología leve y 10 para moderada, máximo 20 puntos
        
        //40 puntos-------------
        
        //puntos dependiendo de rango de edad, por encima de 65 años
        //12 por síntoma grave
        //12 por patologías/antecedentes graves
        //máximo 40 puntos
        
        String sintoma, patologia_antecedente;
        
        List<String> sintomas = paciente.getSintomas();
        List<String> patologias_antecedentes = paciente.getPatologias_antecedentes();
        int edad = paciente.getEdad();
        
        //60 puntos-------------
        
        int puntajeInicial = 0;
        
        //Síntomas leves (máximo 20 puntos)
        String tipoSintoma;
        for(int i=0; i<sintomas.size(); i++){
            sintoma = sintomas.get(i);
            //System.out.println(sintoma);
            tipoSintoma = this.gravedadSintomas.get(sintoma);  
            //System.out.println("retorno es "+tipoSintoma);
            if( tipoSintoma != null ){
                if(tipoSintoma.equals("Leve")){                    
                    puntajeInicial = puntajeInicial+5;
                }
            }
        }
        System.out.println("Síntomas leves: "+puntajeInicial);
        //Edad (máximo 20 puntos)
        if (edad <= 5) {
            puntajeInicial = puntajeInicial+20;
        }
        if (edad > 5 && edad < 20) {
            puntajeInicial = puntajeInicial+5;
        }
        if (edad >= 20 && edad < 40) {
            puntajeInicial = puntajeInicial+8;
        }
        if (edad >= 40 && edad < 65) {
            puntajeInicial = puntajeInicial+14;
        }
        if (edad >= 65) {
            puntajeInicial = puntajeInicial+20;
        }
        System.out.println("Edad: "+puntajeInicial);
        
        //Patologías leves y moderadas (máximo 20 puntos)        
        String tipoPatologiaAntecedente;
        int puntajePatologiasLevesModeradas = 0;
        for(int i=0; i<patologias_antecedentes.size(); i++){
            patologia_antecedente = patologias_antecedentes.get(i);
            tipoPatologiaAntecedente = this.gravedadPatologiasAntecedentes.get(patologia_antecedente);
            if( tipoPatologiaAntecedente != null ){
                if(tipoPatologiaAntecedente.equals("Leve")){
                    puntajePatologiasLevesModeradas = puntajePatologiasLevesModeradas + 5;
                }
                if(tipoPatologiaAntecedente.equals("Moderado")){
                    puntajePatologiasLevesModeradas = puntajePatologiasLevesModeradas + 10;
                }
            }
        }   
        if(puntajePatologiasLevesModeradas > 20){
            puntajePatologiasLevesModeradas = 20;
        }
        puntajeInicial = puntajeInicial + puntajePatologiasLevesModeradas;   
        
        System.out.println("Puntaje inicial es "+puntajeInicial);
        
        puntaje = puntaje + puntajeInicial;
        
        
        //40 puntos-------------
        
        int puntajeAdicional = 0;
        
        //Edad
        if (edad >= 65) {
            if (edad >= 65 && edad < 75) {
                puntajeAdicional = puntajeAdicional + 8;
            }
            if (edad >= 75 && edad < 85) {
                puntajeAdicional = puntajeAdicional + 12;
            }
            if (edad >= 85) {
                puntajeAdicional = puntajeAdicional + 16;
            }
        }
        
        //Síntomas graves
        for(int i=0; i<sintomas.size(); i++){
            sintoma = sintomas.get(i);
            tipoSintoma = this.gravedadSintomas.get(sintoma);            
            if( tipoSintoma != null ){
                if(tipoSintoma.equals("Grave")){
                    puntajeAdicional = puntajeAdicional + 12;
                }
            }
        }
        
        //Patologías/antecedentes graves
        for(int i=0; i<patologias_antecedentes.size(); i++){
            patologia_antecedente = patologias_antecedentes.get(i);
            tipoPatologiaAntecedente = this.gravedadPatologiasAntecedentes.get(patologia_antecedente);
            if( tipoPatologiaAntecedente != null ){
                if(tipoPatologiaAntecedente.equals("Grave")){
                    puntajeAdicional = puntajeAdicional + 12;
                }                
            }
        }
        
        if(puntajeAdicional > 40){
            puntajeAdicional = 40;
        }
        puntaje = puntaje + puntajeAdicional;   
        
        
        this.gui.addRowToJTablePacientes(paciente.getDocumento(), paciente.getNombre(), puntaje);
        
        System.out.println("Paciente "+paciente.getNombre()+" con documento "
                +paciente.getDocumento()+" fue evaluado y "
                + "obtuvo "+puntaje+" puntaje");
        
        //Retardo forzado de 1 segundo
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Error: "+e.toString());
        }
        
        return puntaje;
    }
    
}
