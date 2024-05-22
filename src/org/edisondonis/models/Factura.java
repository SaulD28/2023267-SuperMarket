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
public class Factura {
    private int idFactura;
    private String estado;
    private double totalFactura;
    private String fechaFactura;
    private int idCliente;
    private int idEmpleado;

    public Factura() {
    }

    public Factura(int idFactura, String estado, double totalFactura, String fechaFactura, int idCliente, int idEmpleado) {
        this.idFactura = idFactura;
        this.estado = estado;
        this.totalFactura = totalFactura;
        this.fechaFactura = fechaFactura;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getTotalFactura() {
        return totalFactura;
    }

    public void setTotalFactura(double totalFactura) {
        this.totalFactura = totalFactura;
    }

    public String getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(String fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
    
    
}
