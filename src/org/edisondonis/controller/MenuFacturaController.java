/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.edisondonis.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import javax.swing.JOptionPane;
import org.edisondonis.dao.Conexion;
import org.edisondonis.models.Factura;
import org.edisondonis.system.Main;

public class MenuFacturaController implements Initializable {
    private Main escenarioPrincipal;
    private enum Operaciones { AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO }
    private Operaciones tipoDeOperaciones = Operaciones.NINGUNO;
    private ObservableList<Factura> listaFacturas;
    
    @FXML private Button btnRegresar;
    @FXML private TextField txtIdFactura;
    @FXML private TextField txtEstado;
    @FXML private TextField txtTotalFactura;
    @FXML private TextField txtFechaFactura;
    @FXML private TextField txtIdCliente;
    @FXML private TextField txtIdEmpleado;
    @FXML private TableView tblFactura;
    @FXML private TableColumn colIdFactura;
    @FXML private TableColumn colEstado;
    @FXML private TableColumn colTotalFactura;
    @FXML private TableColumn colFechaFactura;
    @FXML private TableColumn colIdCliente;
    @FXML private TableColumn colIdEmpleado;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;  

    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblFactura.setItems(getFacturas());
        colIdFactura.setCellValueFactory(new PropertyValueFactory<>("idFactura"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colTotalFactura.setCellValueFactory(new PropertyValueFactory<>("totalFactura"));
        colFechaFactura.setCellValueFactory(new PropertyValueFactory<>("fechaFactura"));
        colIdCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        colIdEmpleado.setCellValueFactory(new PropertyValueFactory<>("idEmpleado"));
    }
    
    public void seleccionarElemento(){
        Factura facturaSeleccionada = (Factura) tblFactura.getSelectionModel().getSelectedItem();
        if (facturaSeleccionada != null) {
            txtIdFactura.setText(String.valueOf(facturaSeleccionada.getIdFactura()));
            txtEstado.setText(facturaSeleccionada.getEstado());
            txtTotalFactura.setText(String.valueOf(facturaSeleccionada.getTotalFactura()));
            txtFechaFactura.setText(facturaSeleccionada.getFechaFactura());
            txtIdCliente.setText(String.valueOf(facturaSeleccionada.getIdCliente()));
            txtIdEmpleado.setText(String.valueOf(facturaSeleccionada.getIdEmpleado()));
        }
    }
    
    public ObservableList<Factura> getFacturas(){
        ArrayList<Factura> lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarFacturas()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Factura(
                        resultado.getInt("idFactura"),
                        resultado.getString("estado"),
                        resultado.getDouble("totalFactura"),
                        resultado.getString("fechaFactura"),
                        resultado.getInt("idCliente"),
                        resultado.getInt("idEmpleado")
                ));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return listaFacturas = FXCollections.observableArrayList(lista);
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
                if(tblFactura.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "CONFIRMAR SI ELIMINAR EL REGISTRO"
                            , "ELIMINAR FACTURA", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarFactura(?)");
                            procedimiento.setInt(1, ((Factura) tblFactura.getSelectionModel().getSelectedItem()).getIdFactura());
                            procedimiento.execute();
                            listaFacturas.remove(tblFactura.getSelectionModel().getSelectedIndex());
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
                if(tblFactura.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("ACTUALIZAR");
                    btnReporte.setText("CANCELAR");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    txtIdFactura.setEditable(false);
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarFactura(?, ?, ?, ?, ?)");
            Factura factura = (Factura) tblFactura.getSelectionModel().getSelectedItem();
            factura.setEstado(txtEstado.getText());
            factura.setTotalFactura(Double.parseDouble(txtTotalFactura.getText()));
            factura.setFechaFactura(txtFechaFactura.getText());
            factura.setIdCliente(Integer.parseInt(txtIdCliente.getText()));
            factura.setIdEmpleado(Integer.parseInt(txtIdEmpleado.getText()));
            procedimiento.setInt(1, factura.getIdFactura());
            procedimiento.setString(2, factura.getEstado());
            procedimiento.setDouble(3, factura.getTotalFactura());
            procedimiento.setString(4, factura.getFechaFactura());
            procedimiento.setInt(5, factura.getIdCliente());
            procedimiento.setInt(6, factura.getIdEmpleado());
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
        txtIdFactura.setEditable(false);
        txtEstado.setEditable(false);
        txtTotalFactura.setEditable(false);
        txtFechaFactura.setEditable(false);
        txtIdCliente.setEditable(false);
        txtIdEmpleado.setEditable(false);
    }    
    
    public void activarControles(){
        txtIdFactura.setEditable(true);
        txtEstado.setEditable(true);
        txtTotalFactura.setEditable(true);
        txtFechaFactura.setEditable(true);
        txtIdCliente.setEditable(true);
        txtIdEmpleado.setEditable(true);
    }    
    
    public void limpiarControles(){
        txtIdFactura.clear();
        txtEstado.clear();
        txtTotalFactura.clear();
        txtFechaFactura.clear();
        txtIdCliente.clear();
        txtIdEmpleado.clear();
    }    
    
    public void guardar(){
        Factura factura = new Factura();
        factura.setIdFactura(Integer.parseInt(txtIdFactura.getText()));
        factura.setEstado(txtEstado.getText());
        factura.setTotalFactura(Double.parseDouble(txtTotalFactura.getText()));
        factura.setFechaFactura(txtFechaFactura.getText());
        factura.setIdCliente(Integer.parseInt(txtIdCliente.getText()));
        factura.setIdEmpleado(Integer.parseInt(txtIdEmpleado.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarFactura(?, ?, ?, ?, ?)");
            procedimiento.setInt(1, factura.getIdFactura());
            procedimiento.setString(2, factura.getEstado());
            procedimiento.setDouble(3, factura.getTotalFactura());
            procedimiento.setString(4, factura.getFechaFactura());
            procedimiento.setInt(5, factura.getIdCliente());
            procedimiento.setInt(6, factura.getIdEmpleado());
            procedimiento.execute();
            listaFacturas.add(factura);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @FXML
    public void regresar(ActionEvent event){
        if(event.getSource() == btnRegresar){
            escenarioPrincipal.menuPrincipalView();
        }
    }
}


