package org.edisondonis.controller;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javax.swing.JOptionPane;
import org.edisondonis.dao.Conexion;
import org.edisondonis.models.Clientes;
import org.edisondonis.system.Main;



public class MenuClientesController implements Initializable {
    private Main escenarioPrincipal;
    private enum operaciones{AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Clientes> listaClientes;
    
    @FXML private Button btnRegresar;
    @FXML private TextField txtCodigoClientes;
    @FXML private TextField txtNitClientes;
    @FXML private TextField txtNombreClientes;
    @FXML private TextField txtApellidoClientes;
    @FXML private TextField txtDireccionClientes;
    @FXML private TextField txtTelefonoClientes;
    @FXML private TextField txtCorreoClientes;
    @FXML private TableView tblClientes;
    @FXML private TableColumn colCodigoClientes;
    @FXML private TableColumn colNombreClientes;
    @FXML private TableColumn colApellidoClientes;
    @FXML private TableColumn colNitClientes;
    @FXML private TableColumn colDireccionClientes;
    @FXML private TableColumn colTelefonoClientes;
    @FXML private TableColumn colCorreoClientes;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblClientes.setItems(getClientes());
        colCodigoClientes.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("idCliente"));
        colNitClientes.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("NitCliente"));
        colNombreClientes.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("nombreCliente"));
        colApellidoClientes.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("apellidoCliente"));
        colDireccionClientes.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("direccionCliente"));
        colTelefonoClientes.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("telefonoCliente"));
        colCorreoClientes.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("correoCliente"));
        
    }
    
    public void seleccionarElemento(){
        txtCodigoClientes.setText(String.valueOf(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getClass()));
        txtNombreClientes.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getNombreCliente());
        txtApellidoClientes.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getApellidoCliente());
        txtNitClientes.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getNitCliente());
        txtTelefonoClientes.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getTelefonoCliente());
        txtDireccionClientes.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getDireccionCliente());
        txtCorreoClientes.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCorreoCliente());
        
    }
    
    public ObservableList<Clientes> getClientes (){
        ArrayList<Clientes> lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarClientes()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Clientes(resultado.getInt("idCliente"),
                resultado.getString("NitCliente"),
                resultado.getString("nombreCliente"),
                resultado.getString("apellidoCliente"),
                resultado.getString("direccionCliente"),
                resultado.getString("telefonoCliente"),
                resultado.getString("correoCliente")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaClientes = FXCollections.observableArrayList(lista);
                
    }
    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void agregar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if(tblClientes.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "CONFIRMAR SI ELIMINAR EL REGISTRO"
                            , "ELIMINAR CLIENTES", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarClientes(?)");
                            procedimiento.setInt(1, ((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getIdCliente());
                            procedimiento.execute();
                            listaClientes.remove(tblClientes.getSelectionModel().getSelectedItem());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else
                    JOptionPane.showMessageDialog(null, "DEBE DE SELECCIONAR UN ELEMENTO");
        }
    }
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblClientes.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("ACTUALIZAR");
                    btnReporte.setText("CANCELAR");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    txtCodigoClientes.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else
                    JOptionPane.showMessageDialog(null, "DEBE DE SELECCIONAR ALGUN ELEMENTO");
                break;
            case ACTUALIZAR:
                    actualizar();
                    btnEditar.setText("EDITAR");
                    btnReporte.setText("REPORTE");
                    btnAgregar.setDisable(false);
                    btnEliminar.setDisable(false);
                    desactivarControles();
                    tipoDeOperaciones = operaciones.NINGUNO;
                    cargarDatos();
                    limpiarControles();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarClientes(?, ?, ?, ?, ?, ?, ?)");
            Clientes registro = (Clientes)tblClientes.getSelectionModel().getSelectedItem();
            registro.setNitCliente(txtNitClientes.getText());
            registro.setNombreCliente(txtNombreClientes.getText());
            registro.setApellidoCliente(txtApellidoClientes.getText());
            registro.setDireccionCliente(txtDireccionClientes.getText());
            registro.setTelefonoCliente(txtTelefonoClientes.getText());
            registro.setCorreoCliente(txtCorreoClientes.getText());
            procedimiento.setInt(1, registro.getIdCliente());
            procedimiento.setString(2, registro.getNitCliente());
            procedimiento.setString(3, registro.getNombreCliente());
            procedimiento.setString(4, registro.getApellidoCliente());
            procedimiento.setString(5, registro.getDireccionCliente());
            procedimiento.setString(6, registro.getTelefonoCliente());
            procedimiento.setString(7, registro.getCorreoCliente());
            procedimiento.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("EDITAR");
                btnReporte.setText("REPORTE");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
    public void desactivarControles(){
        txtCodigoClientes.setEditable(false);
        txtNitClientes.setEditable(false);
        txtNombreClientes.setEditable(false);
        txtApellidoClientes.setEditable(false);
        txtTelefonoClientes.setEditable(false);
        txtDireccionClientes.setEditable(false);
        txtCorreoClientes.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoClientes.setEditable(true);
        txtNitClientes.setEditable(true);
        txtNombreClientes.setEditable(true);
        txtApellidoClientes.setEditable(true);
        txtTelefonoClientes.setEditable(true);
        txtDireccionClientes.setEditable(true);
        txtCorreoClientes.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoClientes.clear();
        txtNitClientes.clear();
        txtNombreClientes.clear();
        txtApellidoClientes.clear();
        txtDireccionClientes.clear();
        txtTelefonoClientes.clear();
        txtCorreoClientes.clear();
    }
    
    
    public void guardar(){
        Clientes registro = new Clientes();
        registro.setIdCliente(Integer.parseInt(txtCodigoClientes.getText()));
        registro.setNitCliente(txtNitClientes.getText());
        registro.setNombreCliente(txtNombreClientes.getText());
        registro.setApellidoCliente(txtApellidoClientes.getText());
        registro.setDireccionCliente(txtDireccionClientes.getText());
        registro.setTelefonoCliente(txtTelefonoClientes.getText());
        registro.setCorreoCliente(txtCorreoClientes.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarClientes(?, ?, ?, ?, ?, ?, ?)");
            procedimiento.setInt(1, registro.getIdCliente());
            procedimiento.setString(2, registro.getNitCliente());
            procedimiento.setString(3, registro.getNombreCliente());
            procedimiento.setString(4, registro.getApellidoCliente());
            procedimiento.setString(5, registro.getDireccionCliente());
            procedimiento.setString(6, registro.getTelefonoCliente());
            procedimiento.setString(7, registro.getCorreoCliente());
            procedimiento.execute();
            listaClientes.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    
    
    @FXML
    public void regresar (ActionEvent event){
        if(event.getSource() == btnRegresar){
            escenarioPrincipal.menuPrincipalView();
        }
    }
    
}
