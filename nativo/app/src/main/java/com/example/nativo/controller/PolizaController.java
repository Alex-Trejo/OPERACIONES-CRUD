package com.example.nativo.controller;

public class PolizaController {
    public static String calcularCostoPoliza(String nombre,double valorAuto, int accidentes, String modelo, String edad) {
        //calculo del cargo por valor del auto 3.5%
        double cargoxvalor = valorAuto * 0.035;


        //cargo por modelo A -> 1.1% ; B -> 1.2% ; C -> 1.5%
        //Caluclo del cargo por modelo
        double cargoxmodelo = 0;

        if (modelo.equals("A")) {
            cargoxmodelo = valorAuto * 0.011;

        } else if (modelo.equals("B")) {
            cargoxmodelo = valorAuto * 0.012;
        } else if (modelo.equals("C")) {
            cargoxmodelo = valorAuto * 0.015;
        } else {
            cargoxmodelo = 0;
        }

        //CALCULO POR EDAD la compañia no asegura automoviles a perosnas con edad fuera de estos rangos
        //>= 18 Y < 24 -> 360
        // >= 24 Y < 53 -> 240
        // >= 53 -> 430

        double cargoxedad = 0;

        if (edad.equals("18-23")) { // Rango correcto para < 24
            cargoxedad = 360;
        } else if (edad.equals("24-52")) { // Rango correcto para < 53
            cargoxedad = 240;
        } else if (edad.equals("53+")) { // Desde 53 en adelante
            cargoxedad = 430;
        } else {
            cargoxedad = 0; // Fuera de los rangos asegurables
        }



        //cargo por accidente
        //el cargo por accidente es de 17 por los primros tres accidentes y 21 por cada accidente extra

        double cargoxaccidente = 0;
        if (accidentes >= 1 && accidentes <= 3) {
            cargoxaccidente = 17 * accidentes;
        } else if (accidentes > 3) {
            cargoxaccidente = (17 * 3) + (21 * (accidentes - 3)); // Se suman los primeros 3 accidentes a 17
        } else {
            cargoxaccidente = 0;
        }



        //costo total de la poliza
        double costoTotal = cargoxvalor + cargoxmodelo + cargoxedad + cargoxaccidente;


        return String.valueOf(costoTotal);

    }
}
