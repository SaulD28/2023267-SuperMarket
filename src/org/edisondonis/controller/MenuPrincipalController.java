/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.edisondonis.controller;



import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import org.edisondonis.system.Main;

/* Herencia multiple concepto, interfacez. POO

 */
public class MenuPrincipalController implements Initializable {
    private Main escenarioPrincipal;
    
    @FXML private MenuItem btnMenuClientes;
    @FXML private MenuItem btnMenuProveedores;
    @FXML private MenuItem btnMenuCompras;
    @FXML private MenuItem btnMenuTipoProducto;
    @FXML private MenuItem btnMenuCargoEmpleado;
    @FXML private MenuItem btnProgramador;


    @Override
    public void initialize (URL location, ResourceBundle resources)  {
    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    
    
    @FXML
    public void clicClientes (ActionEvent event){
        if(event.getSource() == btnMenuClientes){
            escenarioPrincipal.menuClientesView();
        }
    }
    
    @FXML
    public void programador (ActionEvent event){
        if(event.getSource() == btnProgramador){
            escenarioPrincipal.menuProgramadorView();
        }
    }
    
    @FXML
    public void clicProveedores (ActionEvent event){
        if(event.getSource() == btnMenuProveedores){
            escenarioPrincipal.menuProveedoresView();
        }
    }
    
    @FXML
    public void clicCompras (ActionEvent event){
        if(event.getSource() == btnMenuCompras){
            escenarioPrincipal.menuComprasView();
        }
    }
    
    @FXML
    public void clicTipoProducto (ActionEvent event){
        if(event.getSource() == btnMenuTipoProducto){
            escenarioPrincipal.menuTipoProductoView();
        }
    }
    
     @FXML
    public void clicCargoEmpleado (ActionEvent event){
        if(event.getSource() == btnMenuCargoEmpleado){
            escenarioPrincipal.menuCargoEmpleadoView();
        }
    }
    
}


