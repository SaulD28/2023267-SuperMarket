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
public class EmailProveedor {
    private int idEmailProveedor;
    private String emailProveedor;
    private String descripcion;
    private int idProveedor;

    public EmailProveedor() {
    }

    public EmailProveedor(int idEmailProveedor, String emailProveedor, String descripcion, int idProveedor) {
        this.idEmailProveedor = idEmailProveedor;
        this.emailProveedor = emailProveedor;
        this.descripcion = descripcion;
        this.idProveedor = idProveedor;
    }

    public int getIdEmailProveedor() {
        return idEmailProveedor;
    }

    public void setIdEmailProveedor(int idEmailProveedor) {
        this.idEmailProveedor = idEmailProveedor;
    }

    public String getEmailProveedor() {
        return emailProveedor;
    }

    public void setEmailProveedor(String emailProveedor) {
        this.emailProveedor = emailProveedor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }
    
    
}
