/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.edisondonis.models;

/**
 *
 * @author picor
 */
public class TelefonoProveedor {
    private int idTelefonoProveedor;
    private String numeroPrincipal;
    private String numeroSecundario;
    private String observaciones;
    private int idProveedor;

    public TelefonoProveedor() {
    }

    public TelefonoProveedor(int idTelefonoProveedor, String numeroPrincipal, String numeroSecundario, String observaciones, int idProveedor) {
        this.idTelefonoProveedor = idTelefonoProveedor;
        this.numeroPrincipal = numeroPrincipal;
        this.numeroSecundario = numeroSecundario;
        this.observaciones = observaciones;
        this.idProveedor = idProveedor;
    }

    public int getIdTelefonoProveedor() {
        return idTelefonoProveedor;
    }

    public void setIdTelefonoProveedor(int idTelefonoProveedor) {
        this.idTelefonoProveedor = idTelefonoProveedor;
    }

    public String getNumeroPrincipal() {
        return numeroPrincipal;
    }

    public void setNumeroPrincipal(String numeroPrincipal) {
        this.numeroPrincipal = numeroPrincipal;
    }

    public String getNumeroSecundario() {
        return numeroSecundario;
    }

    public void setNumeroSecundario(String numeroSecundario) {
        this.numeroSecundario = numeroSecundario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }
    
    
}
