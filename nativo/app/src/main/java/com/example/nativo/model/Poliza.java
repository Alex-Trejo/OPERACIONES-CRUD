package com.example.nativo.model;

public class Poliza {

    private String nombre;
    private String cedula;
    private double valorAuto;
    private int accidentes;
    private String modelo;
    private String edad;
    private double costoPoliza;
    private int id;


    //constructor


    public Poliza(int id,String nombre, String cedula, double valorAuto, int accidentes, String modelo, String edad, double costoPoliza) {
        this.id = id;
        this.nombre = nombre;
        this.cedula = cedula;
        this.valorAuto = valorAuto;
        this.accidentes = accidentes;
        this.modelo = modelo;
        this.edad = edad;
        this.costoPoliza = costoPoliza;
    }


    @Override
    public String toString() {
        return "📌 <b>Nombre:</b> " + nombre +
                "<br>📋 <b>Cédula:</b> " + cedula +
                "<br>🚗 <b>Valor Auto:</b> <font color='#4CAF50'>$" + valorAuto + "</font>" +
                "<br>⚠️ <b>Accidentes:</b> " + accidentes +
                "<br>📅 <b>Modelo:</b> " + modelo +
                "<br>🎂 <b>Edad:</b> " + edad +
                "<br>💰 <b>Costo:</b> <font color='#FF5722'>$" + costoPoliza + "</font>";
    }



    //metodos de acceso

    public int getId() {
        return id;
    }

    public String getCedula() {
        return cedula;

    }

    public double getValorAuto() {
        return valorAuto;
    }

    public void setValorAuto(double valorAuto) {
        this.valorAuto = valorAuto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAccidentes() {
        return accidentes;
    }

    public void setAccidentes(int accidentes) {
        this.accidentes = accidentes;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public double getCostoPoliza() {
        return costoPoliza;
    }

    public void setCostoPoliza(double costoPoliza) {
        this.costoPoliza = costoPoliza;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
}
