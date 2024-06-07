package org.edisondonis.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.edisondonis.dao.Conexion;
import org.edisondonis.models.Compras;
import org.edisondonis.models.DetalleCompra;
import org.edisondonis.models.Productos;
import org.edisondonis.models.Proveedores;
import org.edisondonis.models.TipoProducto;
import org.edisondonis.system.Main;

/**
 *
 * @author picor
 */
public class MenuDetalleCompraController {
    private Main escenarioPrincipal;
    private enum operaciones{AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <DetalleCompra> listaDetalleCompra;
    private ObservableList <Productos> listaProductos;
    private ObservableList <Compras> listaCompras;
    
    @FXML private TextField txtCodigoDetalleCompra;
    @FXML private TextField txtCostoUnitario;
    @FXML private TextField txtCantidad;
    @FXML private ComboBox cmbCodigoProducto;
    @FXML private ComboBox cmbNumeroDocumento;
    @FXML private TableView tblDetalleCompra;
    @FXML private TableColumn colCodigoDetalleCompra;
    @FXML private TableColumn colCostounitario;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colNumeroDocumento;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        cargarDatos();
    }
    
    public void cargarDatos(){
        
    }
    
    public void seleccionarElementos(){
        
    }
    
     public void guardar (){
            DetalleCompra registro = new DetalleCompra();
            registro.setIdDetalleCompra((txtCodigoDetalleCompra.getText()));
            registro.setIdProducto(((Productos)cmbCodigoProducto.getSelectionModel().getSelectedItem()).getIdProducto());
            registro.setNumeroDocumento(((Compras)cmbNumeroDocumento.getSelectionModel().getSelectedItem()).getNumeroDocumento());
            registro.setCostoUnitario(Double.parseDouble(txtCostoUnitario.getText()));
            registro.setCantidad(Double.parseDouble(txtCantidad.getText()));
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall ("call sp_agregarDetalleCompra(?, ?, ?, ?, ?, ?, ?, ?)");
                procedimiento.setInt(1, registro.getIdDetalleCompra());
                procedimiento.setDouble(2, registro.getCostoUnitario());
                procedimiento.setInt(3, registro.getCantidad());
                procedimiento.setInt(4, registro.getIdProducto());
                procedimiento.setInt(5, registro.getNumeroDocumento());
                procedimiento.execute();
        
        listaDetalleCompra.add(registro);

         }catch (Exception e){
             e.printStackTrace();
         }
     
     }
    
         public void desactivarControles(){
        txtCodigoDetalleCompra.setEditable(false);
        txtCostoUnitario.setEditable(false);
        txtCantidad.setEditable(false);
        cmbCodigoProducto.setEditable(false);
        cmbNumeroDocumento.setEditable(false);
    }    
    
        public void activarControles(){
        txtCodigoDetalleCompra.setEditable(true);
        txtCostoUnitario.setEditable(true);
        txtCantidad.setEditable(true);
        cmbCodigoProducto.setEditable(true);
        cmbNumeroDocumento.setEditable(true);
    }     
    
    public void limpiarControles(){
        txtCodigoDetalleCompra.clear();
        txtCostoUnitario.clear();
        txtCantidad.clear();
        cmbCodigoProducto.getSelectionModel().getSelectedItem();
        cmbNumeroDocumento.getSelectionModel().getSelectedItem();
    }    
    
     public void agregar (){
         switch(tipoDeOperacion){
             case NINGUNO:
             activarControles();
             btnAgregar.setText("Guardar");
             btnEliminar.setText("Cancelar");
             btnEditar.setDisable(true);
             btnReporte.setDisable(true);   
             tipoDeOperacion = operaciones.ACTUALIZAR;
             break;
             case ACTUALIZAR:
             guardar ();
             desactivarControles();
             limpiarControles ();
             btnAgregar.setText("Agregar");
             btnEliminar.setText("Eliminar");
             btnEditar.setDisable(false);
             btnReporte.setDisable(false);
             tipoDeOperacion = operaciones.NINGUNO;
             cargarDatos();
             break;
         }
    
    }
    
     public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
}
