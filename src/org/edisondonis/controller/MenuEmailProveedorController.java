package org.edisondonis.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import org.edisondonis.models.EmailProveedor;
import org.edisondonis.system.Main;

public class MenuEmailProveedorController implements Initializable {
    private Main escenarioPrincipal;
    private enum Operaciones { AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO }
    private Operaciones tipoDeOperaciones = Operaciones.NINGUNO;
    private ObservableList<EmailProveedor> listaEmailsProveedor;
    
    @FXML private Button btnRegresar;
    @FXML private TextField txtIdEmailProveedor;
    @FXML private TextField txtEmailProveedor;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtIdProveedor;
    @FXML private TableView tblEmailProveedor;
    @FXML private TableColumn colIdEmailProveedor;
    @FXML private TableColumn colEmailProveedor;
    @FXML private TableColumn colDescripcion;
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
        tblEmailProveedor.setItems(getEmailProveedor());
        colIdEmailProveedor.setCellValueFactory(new PropertyValueFactory<>("idEmailProveedor"));
        colEmailProveedor.setCellValueFactory(new PropertyValueFactory<>("emailProveedor"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colIdProveedor.setCellValueFactory(new PropertyValueFactory<>("idProveedor"));
    }
    
    public void seleccionarElemento(){
        EmailProveedor emailSeleccionado = (EmailProveedor) tblEmailProveedor.getSelectionModel().getSelectedItem();
        if (emailSeleccionado != null) {
            txtIdEmailProveedor.setText(String.valueOf(emailSeleccionado.getIdEmailProveedor()));
            txtEmailProveedor.setText(emailSeleccionado.getEmailProveedor());
            txtDescripcion.setText(emailSeleccionado.getDescripcion());
            txtIdProveedor.setText(String.valueOf(emailSeleccionado.getIdProveedor()));
        }
    }
    
    public ObservableList<EmailProveedor> getEmailProveedor(){
        ArrayList<EmailProveedor> lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmailsProveedor()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new EmailProveedor(
                        resultado.getInt("idEmailProveedor"),
                        resultado.getString("emailProveedor"),
                        resultado.getString("descripcion"),
                        resultado.getInt("idProveedor")
                ));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return listaEmailsProveedor = FXCollections.observableArrayList(lista);
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
                if(tblEmailProveedor.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "CONFIRMAR SI ELIMINAR EL REGISTRO"
                            , "ELIMINAR EMAIL DE PROVEEDOR", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmailProveedor(?)");
                            procedimiento.setInt(1, ((EmailProveedor) tblEmailProveedor.getSelectionModel().getSelectedItem()).getIdEmailProveedor());
                            procedimiento.execute();
                            listaEmailsProveedor.remove(tblEmailProveedor.getSelectionModel().getSelectedIndex());
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
                if(tblEmailProveedor.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("ACTUALIZAR");
                    btnReporte.setText("CANCELAR");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    txtIdEmailProveedor.setEditable(false);
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarEmailProveedor(?, ?, ?, ?)");
            EmailProveedor email = (EmailProveedor) tblEmailProveedor.getSelectionModel().getSelectedItem();
            email.setEmailProveedor(txtEmailProveedor.getText());
            email.setDescripcion(txtDescripcion.getText());
            email.setIdProveedor(Integer.parseInt(txtIdProveedor.getText()));
            procedimiento.setInt(1, email.getIdEmailProveedor());
            procedimiento.setString(2, email.getEmailProveedor());
            procedimiento.setString(3, email.getDescripcion());
            procedimiento.setInt(4, email.getIdProveedor());
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
        txtIdEmailProveedor.setEditable(false);
        txtEmailProveedor.setEditable(false);
        txtDescripcion.setEditable(false);
        txtIdProveedor.setEditable(false);
    }    
    
    public void activarControles(){
        txtIdEmailProveedor.setEditable(true);
        txtEmailProveedor.setEditable(true);
        txtDescripcion.setEditable(true);
        txtIdProveedor.setEditable(true);
    }    
    
    public void limpiarControles(){
        txtIdEmailProveedor.clear();
        txtEmailProveedor.clear();
        txtDescripcion.clear();
        txtIdProveedor.clear();
    }    
    
    public void guardar(){
        EmailProveedor email = new EmailProveedor();
        email.setIdEmailProveedor(Integer.parseInt(txtIdEmailProveedor.getText()));
        email.setEmailProveedor(txtEmailProveedor.getText());
        email.setDescripcion(txtDescripcion.getText());
        email.setIdProveedor(Integer.parseInt(txtIdProveedor.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarEmailProveedor(?, ?, ?, ?)");
            procedimiento.setInt(1, email.getIdEmailProveedor());
            procedimiento.setString(2, email.getEmailProveedor());
            procedimiento.setString(3, email.getDescripcion());
            procedimiento.setInt(4, email.getIdProveedor());
            procedimiento.execute();
            listaEmailsProveedor.add(email);
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
