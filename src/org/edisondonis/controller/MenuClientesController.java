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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    @FXML private TextField txtDireccionClientes;
    @FXML private TextField txtCorreoClientes;
    @FXML private TextField txtCodigoClientes;
    @FXML private TextField txtNit;
    @FXML private TextField txtNombreClientes;
    @FXML private TextField txtApellidoClientes;
    @FXML private TextField txtTelefonoClientes;
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
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblClientes.setItems(getClientes());
        colCodigoClientes.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("idCliente"));
        colNombreClientes.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombreCliente"));
        colApellidoClientes.setCellValueFactory(new PropertyValueFactory<Clientes, String>("apellidoCliente"));
        colNitClientes.setCellValueFactory(new PropertyValueFactory<Clientes, String>("NitCliente"));
        colTelefonoClientes.setCellValueFactory(new PropertyValueFactory<Clientes, String>("telefonoCliente"));
        colDireccionClientes.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccionCliente"));
        colCorreoClientes.setCellValueFactory(new PropertyValueFactory<Clientes, String>("correoCliente"));
        
    }
    
    public void seleccionarElemento(){
        txtCodigoClientes.setText(String.valueOf(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getClass()));
        txtNombreClientes.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getNombreCliente());
        txtApellidoClientes.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getApellidoCliente());
        txtNit.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getNitCliente());
        txtTelefonoClientes.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getTelefonoCliente());
        txtDireccionClientes.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getDireccionCliente());
        txtCorreoClientes.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCorreoCliente());
        
    }
    
    public ObservableList<Clientes> getClientes (){
        ArrayList<Clientes> lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarCliente()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Clientes(
                    resultado.getInt("idCliente"),
                    resultado.getString("nombreCliente"),
                    resultado.getString("apellidoCliente"),
                    resultado.getString("NitCliente"),
                    resultado.getString("telefonoCliente"),
                    resultado.getString("direccionCliente"),
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
    
    public void guardar(){
        Clientes registro = new Clientes();
        registro.setIdCliente(Integer.parseInt(txtCodigoClientes.getText()));
        registro.setNombreCliente(txtNombreClientes.getText());
        registro.setApellidoCliente(txtApellidoClientes.getText());
        registro.setNitCliente(txtNit.getText());
        registro.setTelefonoCliente(txtTelefonoClientes.getText());
        registro.setDireccionCliente(txtDireccionClientes.getText());
        registro.setCorreoCliente(txtCorreoClientes.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarCliente(?, ?, ?, ?, ?, ?, ?)");
            procedimiento.setInt(1, registro.getIdCliente());
            procedimiento.setString(2, registro.getNombreCliente());
            procedimiento.setString(3, registro.getApellidoCliente());
            procedimiento.setString(4, registro.getNitCliente());
            procedimiento.setString(5, registro.getTelefonoCliente());
            procedimiento.setString(6, registro.getDireccionCliente());
            procedimiento.setString(7, registro.getCorreoCliente());
            procedimiento.execute();
            listaClientes.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void agregar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                activarControles();
                btnAgregar.setText("GUARDAR");
                btnEliminar.setText("ELIMINAR");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("AGREGAR");
                btnEliminar.setText("ELIMINAR");
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
                imgAgregar.setImage(new Image("/org/edisondonis/image/AgregarClientes.png"));
                imgEliminar.setImage(new Image("/org/edisondonis/image/Cancelar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if(tblClientes.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "CONFIRMAR SI ELIMINAR EL REGISTRO"
                            , "ELIMINAR CLIENTES", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarCliente(?)");
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
                    imgEditar.setImage(new Image(""));
                    imgReporte.setImage(new Image(""));
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
                    imgEditar.setImage(new Image(""));
                    imgReporte.setImage(new Image(""));
                    desactivarControles();
                    limpiarControles();
                    tipoDeOperaciones = operaciones.NINGUNO;
                    cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarCliente(?, ?, ?, ?, ?, ?, ?)");
            Clientes registro = (Clientes)tblClientes.getSelectionModel().getSelectedItem();
            registro.setNombreCliente(txtNombreClientes.getText());
            registro.setApellidoCliente(txtApellidoClientes.getText());
            registro.setNitCliente(txtNit.getText());
            registro.setTelefonoCliente(txtTelefonoClientes.getText());
            registro.setDireccionCliente(txtDireccionClientes.getText());
            registro.setCorreoCliente(txtCorreoClientes.getText());
            procedimiento.setInt(1, registro.getIdCliente());
            procedimiento.setString(2, registro.getNombreCliente());
            procedimiento.setString(3, registro.getApellidoCliente());
            procedimiento.setString(4, registro.getNitCliente());
            procedimiento.setString(5, registro.getTelefonoCliente());
            procedimiento.setString(6, registro.getDireccionCliente());
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
        txtNombreClientes.setEditable(false);
        txtApellidoClientes.setEditable(false);
        txtDireccionClientes.setEditable(false);
        txtCorreoClientes.setEditable(false);
        txtNit.setEditable(false);
        txtTelefonoClientes.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoClientes.setEditable(true);
        txtNombreClientes.setEditable(true);
        txtApellidoClientes.setEditable(true);
        txtDireccionClientes.setEditable(true);
        txtCorreoClientes.setEditable(true);
        txtNit.setEditable(true);
        txtTelefonoClientes.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoClientes.clear();
        txtNombreClientes.clear();
        txtApellidoClientes.clear();
        txtDireccionClientes.clear();
        txtCorreoClientes.clear();
        txtNit.clear();
        txtTelefonoClientes.clear();
    }  
    
    @FXML
    public void regresar (ActionEvent event){
        if(event.getSource() == btnRegresar){
            escenarioPrincipal.menuPrincipalView();
        }
    }
    
}
