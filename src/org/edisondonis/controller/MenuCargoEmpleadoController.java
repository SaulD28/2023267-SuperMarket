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
import org.edisondonis.models.CargoEmpleado;
import org.edisondonis.system.Main;

/**
 *
 * @author picor
 */
public class MenuCargoEmpleadoController implements Initializable {
    private Main escenarioPrincipal;
    private enum operaciones{AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<CargoEmpleado> listaCargoEmpleados;
    
    @FXML private Button btnRegresar;
    @FXML private TextField txtCodigoEmpleados;
    @FXML private TextField txtNombreCargo;
    @FXML private TextField txtDescripcionCargo;
    @FXML private TableView tblCargoEmpleados;
    @FXML private TableColumn colCodigoEmpleados;
    @FXML private TableColumn colNombreCargo;
    @FXML private TableColumn colDescripcionCargo;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;  
    @FXML private ImageView imgAgregar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblCargoEmpleados.setItems(getCargoEmpleados());
        colCodigoEmpleados.setCellValueFactory(new PropertyValueFactory<CargoEmpleado, Integer>("idCargoEmpleado"));
        colNombreCargo.setCellValueFactory(new PropertyValueFactory<CargoEmpleado, String>("nombreCargo"));
        colDescripcionCargo.setCellValueFactory(new PropertyValueFactory<CargoEmpleado, String>("descripcionCargo"));

    }
    
    public void seleccionarElemento(){
        txtCodigoEmpleados.setText(String.valueOf(((CargoEmpleado)tblCargoEmpleados.getSelectionModel().getSelectedItem()).getIdCargoEmpleado()));
        txtNombreCargo.setText(((CargoEmpleado)tblCargoEmpleados.getSelectionModel().getSelectedItem()).getNombreCargo());
        txtDescripcionCargo.setText(((CargoEmpleado)tblCargoEmpleados.getSelectionModel().getSelectedItem()).getDescripcionCargo());

    }
    
    public ObservableList<CargoEmpleado> getCargoEmpleados(){
        ArrayList<CargoEmpleado> lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarCargoEmpleado()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new CargoEmpleado(resultado.getInt("idCargoEmpleado"),
                                       resultado.getString("nombreCargo"),
                                       resultado.getString("descripcionCargo")
                        ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaCargoEmpleados = FXCollections.observableArrayList(lista);
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
                if(tblCargoEmpleados.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "CONFIRMAR SI ELIMINAR EL REGISTRO"
                            , "ELIMINAR TIPO PRODUCTO", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarCargoEmpleado(?)");
                            procedimiento.setInt(1, ((CargoEmpleado)tblCargoEmpleados.getSelectionModel().getSelectedItem()).getIdCargoEmpleado());
                            procedimiento.execute();
                            listaCargoEmpleados.remove(tblCargoEmpleados.getSelectionModel().getSelectedItem());
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
                if(tblCargoEmpleados.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("ACTUALIZAR");
                    btnReporte.setText("CANCELAR");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    txtCodigoEmpleados.setEditable(false);
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarCargoEmpleado(?, ?, ?)");
            CargoEmpleado registro = (CargoEmpleado)tblCargoEmpleados.getSelectionModel().getSelectedItem();
            registro.setNombreCargo(txtNombreCargo.getText());
            registro.setDescripcionCargo(txtDescripcionCargo.getText());
            procedimiento.setInt(1, registro.getIdCargoEmpleado());
            procedimiento.setString(2, registro.getNombreCargo());
            procedimiento.setString(3, registro.getDescripcionCargo());
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
        txtCodigoEmpleados  .setEditable(false);
        txtNombreCargo.setEditable(false);
        txtDescripcionCargo.setEditable(false);
    }    
    
    public void activarControles(){
        txtCodigoEmpleados.setEditable(true);
        txtNombreCargo.setEditable(true);
        txtDescripcionCargo.setEditable(true);

    }    
    
    public void limpiarControles(){
        txtCodigoEmpleados.clear();
        txtNombreCargo.clear();
        txtDescripcionCargo.clear();
    }    
    
     public void guardar(){
        CargoEmpleado registro = new CargoEmpleado();
        registro.setIdCargoEmpleado(Integer.parseInt(txtCodigoEmpleados.getText()));
        registro.setNombreCargo(txtNombreCargo.getText());
        registro.setDescripcionCargo(txtDescripcionCargo.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarCargoEmpleado(?, ?, ?)");
            procedimiento.setInt(1, registro.getIdCargoEmpleado());
            procedimiento.setString(2, registro.getNombreCargo());
            procedimiento.setString(3, registro.getDescripcionCargo());
            procedimiento.execute();
            listaCargoEmpleados.add(registro);
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
