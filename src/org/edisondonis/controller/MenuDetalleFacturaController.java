package org.edisondonis.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.edisondonis.dao.Conexion;
import org.edisondonis.models.DetalleFactura;
import org.edisondonis.system.Main;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;

public class MenuDetalleFacturaController implements Initializable {
    private Main escenarioPrincipal;
    private enum Operaciones { AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO }
    private Operaciones tipoDeOperaciones = Operaciones.NINGUNO;
    private ObservableList<DetalleFactura> listaDetalleFactura;

    @FXML private Button btnRegresar;
    @FXML private TextField txtIdDetalleFactura;
    @FXML private TextField txtPrecioUnitario;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtIdFactura;
    @FXML private TextField txtIdProducto;
    @FXML private TableView tblDetalleFactura;
    @FXML private TableColumn colIdDetalleFactura;
    @FXML private TableColumn colPrecioUnitario;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colIdFactura;
    @FXML private TableColumn colIdProducto;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        cargarDatos();
    }

    public void cargarDatos(){
        tblDetalleFactura.setItems(getDetalleFactura());
        colIdDetalleFactura.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("idDetalleFactura"));
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Double>("precioUnitario"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("cantidad"));
        colIdFactura.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("idFactura"));
        colIdProducto.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("idProducto"));
    }

    public ObservableList<DetalleFactura> getDetalleFactura(){
        ArrayList<DetalleFactura> lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarDetalleFactura()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new DetalleFactura(
                        resultado.getInt("idDetalleFactura"),
                        resultado.getDouble("precioUnitario"),
                        resultado.getInt("cantidad"),
                        resultado.getInt("idFactura"),
                        resultado.getInt("idProducto")
                ));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return listaDetalleFactura = FXCollections.observableArrayList(lista);
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
                if(tblDetalleFactura.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "CONFIRMAR SI ELIMINAR EL REGISTRO"
                            , "ELIMINAR DETALLE DE FACTURA", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarDetalleFactura(?)");
                            procedimiento.setInt(1, ((DetalleFactura) tblDetalleFactura.getSelectionModel().getSelectedItem()).getIdDetalleFactura());
                            procedimiento.execute();
                            listaDetalleFactura.remove(tblDetalleFactura.getSelectionModel().getSelectedIndex());
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
                if(tblDetalleFactura.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("ACTUALIZAR");
                    btnReporte.setText("CANCELAR");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    txtIdDetalleFactura.setEditable(false);
                    tipoDeOperaciones = Operaciones.ACTUALIZAR;
                } else{
                    JOptionPane.showMessageDialog(null, "DEBE DE SELECCIONAR ALGUN ELEMENTO");
                }
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarDetalleFactura(?, ?, ?, ?, ?)");
            DetalleFactura detalle = (DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem();
            detalle.setPrecioUnitario(Double.parseDouble(txtPrecioUnitario.getText()));
            detalle.setCantidad(Integer.parseInt(txtCantidad.getText()));
            detalle.setIdFactura(Integer.parseInt(txtIdFactura.getText()));
            detalle.setIdProducto(Integer.parseInt(txtIdProducto.getText()));
            procedimiento.setInt(1, detalle.getIdDetalleFactura());
            procedimiento.setDouble(2, detalle.getPrecioUnitario());
            procedimiento.setInt(3, detalle.getCantidad());
            procedimiento.setInt(4, detalle.getIdFactura());
            procedimiento.setInt(5, detalle.getIdProducto());
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
        txtIdDetalleFactura.setEditable(false);
        txtPrecioUnitario.setEditable(false);
        txtCantidad.setEditable(false);
        txtIdFactura.setEditable(false);
        txtIdProducto.setEditable(false);
    }

    public void activarControles(){
        txtIdDetalleFactura.setEditable(true);
        txtPrecioUnitario.setEditable(true);
        txtCantidad.setEditable(true);
        txtIdFactura.setEditable(true);
        txtIdProducto.setEditable(true);
    }

    public void limpiarControles(){
        txtIdDetalleFactura.clear();
        txtPrecioUnitario.clear();
        txtCantidad.clear();
        txtIdFactura.clear();
        txtIdProducto.clear();
    }

    public void guardar(){
        DetalleFactura detalle = new DetalleFactura();
        detalle.setIdDetalleFactura(Integer.parseInt(txtIdDetalleFactura.getText()));
        detalle.setPrecioUnitario(Double.parseDouble(txtPrecioUnitario.getText()));
        detalle.setCantidad(Integer.parseInt(txtCantidad.getText()));
        detalle.setIdFactura(Integer.parseInt(txtIdFactura.getText()));
        detalle.setIdProducto(Integer.parseInt(txtIdProducto.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarDetalleFactura(?, ?, ?, ?)");
            procedimiento.setInt(1, detalle.getIdDetalleFactura());
            procedimiento.setDouble(2, detalle.getPrecioUnitario());
            procedimiento.setInt(3, detalle.getCantidad());
            procedimiento.setInt(4, detalle.getIdFactura());
            procedimiento.setInt(5, detalle.getIdProducto());
            procedimiento.execute();
            listaDetalleFactura.add(detalle);
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

