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
public class DetalleFactura {
    private int idDetalleFactura;
    private double precioUnitario;
    private int cantidad;
    private int idFactura;
    private int idProducto;

    public DetalleFactura() {
    }

    public DetalleFactura(int idDetalleFactura, double precioUnitario, int cantidad, int idFactura, int idProducto) {
        this.idDetalleFactura = idDetalleFactura;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.idFactura = idFactura;
        this.idProducto = idProducto;
    }

    public int getIdDetalleFactura() {
        return idDetalleFactura;
    }

    public void setIdDetalleFactura(int idDetalleFactura) {
        this.idDetalleFactura = idDetalleFactura;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
    
    
}
