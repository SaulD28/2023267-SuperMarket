/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.edisondonis.controller;


import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.edisondonis.dao.Conexion;
import org.edisondonis.models.Productos;
import org.edisondonis.models.Proveedores;
import org.edisondonis.models.TipoProducto;
import org.edisondonis.system.Main;


public class MenuProductosController implements Initializable {
    private Main escenarioPrincipal;
    private enum operaciones{AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Productos> listaProductos;
    private ObservableList <Proveedores> listaProveedores;
    private ObservableList <TipoProducto> listaTipoDeProducto;
    
    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtDescripcionProducto;
    @FXML private TextField txtPrecioUnitario;
    @FXML private TextField txtPrecioDocena;
    @FXML private TextField txtPrecioMayor;
    @FXML private TextField txtExistencia;
    @FXML private ComboBox cmbCodigoProveedor;
    @FXML private ComboBox cmbCodigoTipoProducto;
    @FXML private TableView tblProductos;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colDescripcionProducto;
    @FXML private TableColumn colPrecioUnitario;
    @FXML private TableColumn colPrecioDocena;
    @FXML private TableColumn colPrecioMayor;
    @FXML private TableColumn colExistencia;
    @FXML private TableColumn colCodigoProveedor;
    @FXML private TableColumn colCodigoTipoProducto;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargaDatos();
        cmbCodigoProveedor.setItems(getProveedores());
        cmbCodigoTipoProducto.setItems(getTipoProducto());
    }


    
    public void cargaDatos(){
    tblProductos.setItems(getProducto());
    colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Productos, String>("idProducto"));
    colDescripcionProducto.setCellValueFactory(new PropertyValueFactory<Productos, String>("descripcionProducto"));
    colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioUnitario"));
    colPrecioDocena.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioDocena"));
    colPrecioMayor.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioMayor"));
    colExistencia.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("existencia"));
    colCodigoProveedor.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("idProveedor"));
    colCodigoTipoProducto.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("idTipoProducto"));
    
    
    }
    public void selecionarElementos(){
       txtCodigoProducto.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getIdProducto());
       txtDescripcionProducto.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getDescripcionProducto());
       txtPrecioUnitario.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
       txtPrecioDocena.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioDocena()));
       txtPrecioMayor.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioMayor()));
       txtExistencia.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getExistencia()));
       cmbCodigoProveedor.getSelectionModel().select(buscarTipoProducto(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getIdTipoProducto()));
    }
    
    public TipoProducto buscarTipoProducto (int codigoTipoProducto ){
        TipoProducto resultado = null;
        try{
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarTipoProducto(?)}");
         procedimiento.setInt(1, codigoTipoProducto);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new TipoProducto(registro.getInt("idTipoProducto"),
                                            registro.getString("descripcionProducto")
             );
         }
        }catch (Exception e){
            e.printStackTrace();
        }    
    
        return resultado;
    }
    
    
    public ObservableList<Productos> getProducto(){
    ArrayList<Productos> lista = new ArrayList<Productos>();
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos()}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Productos (resultado.getString("idProducto"),
                                    resultado.getString("descripcionProducto"),
                                    resultado.getDouble("precioUnitario"),
                                    resultado.getDouble("precioDocena"),
                                    resultado.getDouble("precioMayor"),
                                    resultado.getInt("existencia"),
                                    resultado.getInt("idTipoProducto"),
                                    resultado.getInt("idProveedor")            
            ));
        }
    }catch (Exception e){
        e.printStackTrace();
    }
    
    
    return listaProductos = FXCollections.observableArrayList(lista);
        
    }
    public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> listaProveedores = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProveedor()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaProveedores.add(new Proveedores(resultado.getInt("idProveedor"),
                        resultado.getString("NITProveedor"),
                        resultado.getString("nombresProveedor"),
                        resultado.getString("apellidosProveedor"),
                        resultado.getString("direccionProveedor"),
                        resultado.getString("razonSocial"),
                        resultado.getString("contactoPrincipal"),
                        resultado.getString("paginaWeb"),
                        resultado.getString("telefonoProveedor"),
                        resultado.getString("emailProveedor")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProveedores = FXCollections.observableList(listaProveedores);
    }
     public ObservableList<TipoProducto> getTipoProducto() {
        ArrayList<TipoProducto> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoProducto()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoProducto(resultado.getInt("idTipoProducto"),
                        resultado.getString("descripcionProducto")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaTipoDeProducto = FXCollections.observableList(lista);
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
             cargaDatos();
             break;
         }
    
    }
   
     public Main getEscenarioPrincipal(){
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
}
