package org.edisondonis.models;


public class DetalleCompra {
    private int idDetalleCompra;
    private double costoUnitario;
    private int cantidad;
    private int idProducto;
    private int numeroDocumento;

    public DetalleCompra() {
    }

    public DetalleCompra(int idDetalleCompra, double costoUnitario, int cantidad, int idProducto, int numeroDocumento) {
        this.idDetalleCompra = idDetalleCompra;
        this.costoUnitario = costoUnitario;
        this.cantidad = cantidad;
        this.idProducto = idProducto;
        this.numeroDocumento = numeroDocumento;
    }

    public int getIdDetalleCompra() {
        return idDetalleCompra;
    }

    public void setIdDetalleCompra(int idDetalleCompra) {
        this.idDetalleCompra = idDetalleCompra;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(double costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(int numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
    
    
}
