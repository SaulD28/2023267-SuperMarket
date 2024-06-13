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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.edisondonis.dao.Conexion;
import org.edisondonis.models.TelefonoProveedor;
import org.edisondonis.system.Main;

/**
 *
 * @author picor
 */
public class MenuTelefonoProveedorController implements Initializable {
    private Main escenarioPrincipal;
    private enum Operaciones { AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO }
    private Operaciones tipoDeOperaciones = Operaciones.NINGUNO;
    private ObservableList<TelefonoProveedor> listaTelefonosProveedor;
    
    @FXML private Button btnRegresar;
    @FXML private TextField txtIdTelefonoProveedor;
    @FXML private TextField txtNumeroPrincipal;
    @FXML private TextField txtNumeroSecundario;
    @FXML private TextField txtObservaciones;
    @FXML private TextField txtIdProveedor;
    @FXML private TableView tblTelefonoProveedor;
    @FXML private TableColumn colIdTelefonoProveedor;
    @FXML private TableColumn colNumeroPrincipal;
    @FXML private TableColumn colNumeroSecundario;
    @FXML private TableColumn colObservaciones;
    @FXML private TableColumn colIdProveedor;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;  

    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblTelefonoProveedor.setItems(getTelefonosProveedor());
        colIdTelefonoProveedor.setCellValueFactory(new PropertyValueFactory<>("idTelefonoProveedor"));
        colNumeroPrincipal.setCellValueFactory(new PropertyValueFactory<>("numeroPrincipal"));
        colNumeroSecundario.setCellValueFactory(new PropertyValueFactory<>("numeroSecundario"));
        colObservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        colIdProveedor.setCellValueFactory(new PropertyValueFactory<>("idProveedor"));
    }
    
    public void seleccionarElemento(){
        TelefonoProveedor telefonoSeleccionado = (TelefonoProveedor) tblTelefonoProveedor.getSelectionModel().getSelectedItem();
        if (telefonoSeleccionado != null) {
            txtIdTelefonoProveedor.setText(String.valueOf(telefonoSeleccionado.getIdTelefonoProveedor()));
            txtNumeroPrincipal.setText(telefonoSeleccionado.getNumeroPrincipal());
            txtNumeroSecundario.setText(telefonoSeleccionado.getNumeroSecundario());
            txtObservaciones.setText(telefonoSeleccionado.getObservaciones());
            txtIdProveedor.setText(String.valueOf(telefonoSeleccionado.getIdProveedor()));
        }
    }
    
    public ObservableList<TelefonoProveedor> getTelefonosProveedor(){
        ArrayList<TelefonoProveedor> lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTelefonosProveedor()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TelefonoProveedor(
                        resultado.getInt("idTelefonoProveedor"),
                        resultado.getString("numeroPrincipal"),
                        resultado.getString("numeroSecundario"),
                        resultado.getString("observaciones"),
                        resultado.getInt("idProveedor")
                ));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return listaTelefonosProveedor = FXCollections.observableArrayList(lista);
    }
    
    public Main getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Main escenarioPrincipal){
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
                tipoDeOperaciones = Operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperaciones = Operaciones.NINGUNO;
                break;
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("AGREGAR");
                btnEliminar.setText("ELIMINAR");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperaciones = Operaciones.NINGUNO;
                break;
            default:
                if(tblTelefonoProveedor.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "CONFIRMAR SI ELIMINAR EL REGISTRO"
                            , "ELIMINAR TELÉFONO DE PROVEEDOR", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTelefonoProveedor(?)");
                            procedimiento.setInt(1, ((TelefonoProveedor) tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getIdTelefonoProveedor());
                            procedimiento.execute();
                            listaTelefonosProveedor.remove(tblTelefonoProveedor.getSelectionModel().getSelectedIndex());
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                } else
                    JOptionPane.showMessageDialog(null, "DEBE DE SELECCIONAR UN ELEMENTO");
        }
    }
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblTelefonoProveedor.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("ACTUALIZAR");
                    btnReporte.setText("CANCELAR");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    txtIdTelefonoProveedor.setEditable(false);
                    tipoDeOperaciones = Operaciones.ACTUALIZAR;
                } else
                    JOptionPane.showMessageDialog(null, "DEBE DE SELECCIONAR ALGUN ELEMENTO");
                break;
            case ACTUALIZAR:
                    actualizar();
                    btnEditar.setText("EDITAR");
                    btnReporte.setText("REPORTE");
                    btnAgregar.setDisable(false);
                    btnEliminar.setDisable(false);
                    desactivarControles();
                    tipoDeOperaciones = Operaciones.NINGUNO;
                    cargarDatos();
                    limpiarControles();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTelefonoProveedor(?, ?, ?, ?, ?)");
            TelefonoProveedor telefono = (TelefonoProveedor) tblTelefonoProveedor.getSelectionModel().getSelectedItem();
            telefono.setNumeroPrincipal(txtNumeroPrincipal.getText());
            telefono.setNumeroSecundario(txtNumeroSecundario.getText());
            telefono.setObservaciones(txtObservaciones.getText());
            telefono.setIdProveedor(Integer.parseInt(txtIdProveedor.getText()));
            procedimiento.setInt(1, telefono.getIdTelefonoProveedor());
            procedimiento.setString(2, telefono.getNumeroPrincipal());
            procedimiento.setString(3, telefono.getNumeroSecundario());
            procedimiento.setString(4, telefono.getObservaciones());
            procedimiento.setInt(5, telefono.getIdProveedor());
            procedimiento.execute();
        } catch(Exception e){
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
                tipoDeOperaciones = Operaciones.NINGUNO;
                break;
        }
    }
     
    public void desactivarControles(){
        txtIdTelefonoProveedor.setEditable(false);
        txtNumeroPrincipal.setEditable(false);
        txtNumeroSecundario.setEditable(false);
        txtObservaciones.setEditable(false);
        txtIdProveedor.setEditable(false);
    }    
    
    public void activarControles(){
        txtIdTelefonoProveedor.setEditable(true);
        txtNumeroPrincipal.setEditable(true);
        txtNumeroSecundario.setEditable(true);
        txtObservaciones.setEditable(true);
        txtIdProveedor.setEditable(true);
    }    
    
    public void limpiarControles(){
        txtIdTelefonoProveedor.clear();
        txtNumeroPrincipal.clear();
        txtNumeroSecundario.clear();
        txtObservaciones.clear();
        txtIdProveedor.clear();
    }    
    
    public void guardar(){
        TelefonoProveedor telefono = new TelefonoProveedor();
        telefono.setIdTelefonoProveedor(Integer.parseInt(txtIdTelefonoProveedor.getText()));
        telefono.setNumeroPrincipal(txtNumeroPrincipal.getText());
        telefono.setNumeroSecundario(txtNumeroSecundario.getText());
        telefono.setObservaciones(txtObservaciones.getText());
        telefono.setIdProveedor(Integer.parseInt(txtIdProveedor.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTelefonoProveedor(?, ?, ?, ?, ?)");
            procedimiento.setInt(1, telefono.getIdTelefonoProveedor());
            procedimiento.setString(2, telefono.getNumeroPrincipal());
            procedimiento.setString(3, telefono.getNumeroSecundario());
            procedimiento.setString(4, telefono.getObservaciones());
            procedimiento.setInt(5, telefono.getIdProveedor());
            procedimiento.execute();
            
        listaTelefonosProveedor.add(telefono);
        
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
}





