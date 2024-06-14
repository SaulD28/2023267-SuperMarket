package org.edisondonis.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javax.swing.JOptionPane;
import org.edisondonis.dao.Conexion;
import org.edisondonis.models.DetalleCompra;
import org.edisondonis.system.Main;

public class MenuDetalleCompraController implements Initializable {
    private Main escenarioPrincipal;
    private enum operaciones { AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO }
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<DetalleCompra> listaDetalleCompra;
    
    @FXML private Button btnRegresar;
    @FXML private TextField txtIdDetalleCompra;
    @FXML private TextField txtCostoUnitario;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtIdProducto;
    @FXML private TextField txtNumeroDocumento;
    @FXML private TableView tblDetalleCompra;
    @FXML private TableColumn colIdDetalleCompra;
    @FXML private TableColumn colCostoUnitario;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colIdProducto;
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
        tblDetalleCompra.setItems(getDetalleCompra());
        colIdDetalleCompra.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("idDetalleCompra"));
        colCostoUnitario.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Double>("costoUnitario"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("cantidad"));
        colIdProducto.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("idProducto"));
        colNumeroDocumento.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("numeroDocumento"));
    }
    
    public void seleccionarElemento(){
        DetalleCompra detalleCompra = (DetalleCompra) tblDetalleCompra.getSelectionModel().getSelectedItem();
        if (detalleCompra != null) {
            txtIdDetalleCompra.setText(String.valueOf(detalleCompra.getIdDetalleCompra()));
            txtCostoUnitario.setText(String.valueOf(detalleCompra.getCostoUnitario()));
            txtCantidad.setText(String.valueOf(detalleCompra.getCantidad()));
            txtIdProducto.setText(String.valueOf(detalleCompra.getIdProducto()));
            txtNumeroDocumento.setText(String.valueOf(detalleCompra.getNumeroDocumento()));
        }
    }
    
    public ObservableList<DetalleCompra> getDetalleCompra(){
        ArrayList<DetalleCompra> lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarDetalleCompra()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new DetalleCompra(
                        resultado.getInt("idDetalleCompra"),
                        resultado.getDouble("costoUnitario"),
                        resultado.getInt("cantidad"),
                        resultado.getInt("idProducto"),
                        resultado.getInt("numeroDocumento")
                ));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return listaDetalleCompra = FXCollections.observableArrayList(lista);
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
                btnAgregar.setText("AGREGAR");
                btnEliminar.setText("ELIMINAR");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if(tblDetalleCompra.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "CONFIRMAR SI ELIMINAR EL REGISTRO"
                            , "ELIMINAR DETALLE DE COMPRA", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarDetalleCompra(?)");
                            procedimiento.setInt(1, ((DetalleCompra) tblDetalleCompra.getSelectionModel().getSelectedItem()).getIdDetalleCompra());
                            procedimiento.execute();
                            listaDetalleCompra.remove(tblDetalleCompra.getSelectionModel().getSelectedIndex());
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
                if(tblDetalleCompra.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("ACTUALIZAR");
                    btnReporte.setText("CANCELAR");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    txtIdDetalleCompra.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
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
                    tipoDeOperaciones = operaciones.NINGUNO;
                    cargarDatos();
                    limpiarControles();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarDetalleCompra(?, ?, ?, ?, ?)");
            DetalleCompra registro = (DetalleCompra) tblDetalleCompra.getSelectionModel().getSelectedItem();
            registro.setIdDetalleCompra(Integer.parseInt(txtIdDetalleCompra.getText()));
            registro.setCostoUnitario(Double.parseDouble(txtCostoUnitario.getText()));
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            registro.setIdProducto(Integer.parseInt(txtIdProducto.getText()));
            registro.setNumeroDocumento(Integer.parseInt(txtNumeroDocumento.getText()));
            procedimiento.setInt(1, registro.getIdDetalleCompra());
            procedimiento.setDouble(2, registro.getCostoUnitario());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.setInt(4, registro.getIdProducto());
            procedimiento.setInt(5, registro.getNumeroDocumento());
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
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
     
    public void desactivarControles(){
        txtIdDetalleCompra.setEditable(false);
        txtCostoUnitario.setEditable(false);
        txtCantidad.setEditable(false);
        txtIdProducto.setEditable(false);
        txtNumeroDocumento.setEditable(false);
    }    
    
    public void activarControles(){
        txtIdDetalleCompra.setEditable(true);
        txtCostoUnitario.setEditable(true);
        txtCantidad.setEditable(true);
        txtIdProducto.setEditable(true);
        txtNumeroDocumento.setEditable(true);
    }    
    
    public void limpiarControles(){
        txtIdDetalleCompra.clear();
        txtCostoUnitario.clear();
        txtCantidad.clear();
        txtIdProducto.clear();
        txtNumeroDocumento.clear();
    }    
    
    public void guardar(){
        DetalleCompra detalleCompra = new DetalleCompra();
        detalleCompra.setIdDetalleCompra(Integer.parseInt(txtIdDetalleCompra.getText()));
        detalleCompra.setCostoUnitario(Double.parseDouble(txtCostoUnitario.getText()));
        detalleCompra.setCantidad(Integer.parseInt(txtCantidad.getText()));
        detalleCompra.setIdProducto(Integer.parseInt(txtIdProducto.getText()));
        detalleCompra.setNumeroDocumento(Integer.parseInt(txtNumeroDocumento.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarDetalleCompra(?, ?, ?, ?, ?)");
            procedimiento.setInt(1, detalleCompra.getIdDetalleCompra());
            procedimiento.setDouble(2, detalleCompra.getCostoUnitario());
            procedimiento.setInt(3, detalleCompra.getCantidad());
            procedimiento.setInt(4, detalleCompra.getIdProducto());
            procedimiento.setInt(5, detalleCompra.getNumeroDocumento());
            procedimiento.execute();
            listaDetalleCompra.add(detalleCompra);
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
