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
    @FXML private MenuItem btnMenuDetalleCompra;
    @FXML private MenuItem btnMenuDetalleFactura;
    @FXML private MenuItem btnMenuEmailProveedor;
    @FXML private MenuItem btnMenuEmpleados;
    @FXML private MenuItem btnMenuFactura;
    @FXML private MenuItem btnMenuProductos;
    @FXML private MenuItem btnMenuTelefonoProveedor;


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
    public void btnMenu (ActionEvent event){
        if(event.getSource() == btnMenuClientes){
            escenarioPrincipal.menuClientesView();
        }else if(event.getSource() == btnProgramador){
            escenarioPrincipal.menuProgramadorView();
        }else if(event.getSource() == btnMenuProveedores){
            escenarioPrincipal.menuProveedoresView();
        }else if(event.getSource() == btnMenuCompras){
            escenarioPrincipal.menuComprasView();
        }else if(event.getSource() == btnMenuTipoProducto){
            escenarioPrincipal.menuTipoProductoView();
        }else if(event.getSource() == btnMenuCargoEmpleado){
            escenarioPrincipal.menuCargoEmpleadoView();
        }else if(event.getSource() == btnMenuDetalleCompra){
            escenarioPrincipal.menuDetalleCompraView();
        }else if(event.getSource() == btnMenuDetalleFactura){
            escenarioPrincipal.menuDetalleFacturaView();
        }else if(event.getSource() == btnMenuEmailProveedor){
            escenarioPrincipal.menuEmailProveedorView();
        }else if(event.getSource() == btnMenuEmpleados){
            escenarioPrincipal.menuEmpleadosView();
        }else if(event.getSource() == btnMenuFactura){
            escenarioPrincipal.menuFacturaView();
        }else if(event.getSource() == btnMenuProductos){
            escenarioPrincipal.menuProductosView();
        }else if(event.getSource() == btnMenuTelefonoProveedor){
            escenarioPrincipal.menuTelefonoProveedorView();
        }
    }
}


