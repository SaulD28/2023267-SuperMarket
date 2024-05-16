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
public class CargoEmpleado {
    private int idCargoEmpleado;
    private String nombreCargo;
    private String descripcionCargo;

    public CargoEmpleado() {
    }

    public CargoEmpleado(int idCargoEmpleado, String nombreCargo, String descripcionCargo) {
        this.idCargoEmpleado = idCargoEmpleado;
        this.nombreCargo = nombreCargo;
        this.descripcionCargo = descripcionCargo;
    }

    public int getIdCargoEmpleado() {
        return idCargoEmpleado;
    }

    public void setIdCargoEmpleado(int idCargoEmpleado) {
        this.idCargoEmpleado = idCargoEmpleado;
    }

    public String getNombreCargo() {
        return nombreCargo;
    }

    public void setNombreCargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
    }

    public String getDescripcionCargo() {
        return descripcionCargo;
    }

    public void setDescripcionCargo(String descripcionCargo) {
        this.descripcionCargo = descripcionCargo;
    }
     
    
}
