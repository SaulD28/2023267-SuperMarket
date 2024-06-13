package org.edisondonis.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.edisondonis.dao.Conexion;
import org.edisondonis.models.Empleados;
import org.edisondonis.system.Main;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;

public class MenuEmpleadosController implements Initializable {
    private Main escenarioPrincipal;
    private enum Operaciones { AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO }
    private Operaciones tipoDeOperaciones = Operaciones.NINGUNO;
    private ObservableList<Empleados> listaEmpleados;

    @FXML private Button btnRegresar;
    @FXML private TextField txtCodigoEmpleado;
    @FXML private TextField txtNombreEmpleado;
    @FXML private TextField txtApellidoEmpleado;
    @FXML private TextField txtSueldo;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTurno;
    @FXML private TextField txtIdCargoEmpleado;
    @FXML private TableView tblEmpleado;
    @FXML private TableColumn colIdEmpleado;
    @FXML private TableColumn colNombreEmpleado;
    @FXML private TableColumn colApellidoEmpleado;
    @FXML private TableColumn colSueldo;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colTurno;
    @FXML private TableColumn colIdCargoEmpleado;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        cargarDatos();
    }

    public void cargarDatos(){
        tblEmpleado.setItems(getEmpleados());
        colIdEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("idEmpleado"));
        colNombreEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombreEmpleado"));
        colApellidoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, String>("apellidoEmpleado"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<Empleados, Double>("sueldo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Empleados, String>("direccion"));
        colTurno.setCellValueFactory(new PropertyValueFactory<Empleados, String>("turno"));
        colIdCargoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("idCargoEmpleado"));
    }
    
     public void seleccionarDatos(){
        txtCodigoEmpleado.setText(String.valueOf(((Empleados)tblEmpleado.getSelectionModel().getSelectedItem()).getIdEmpleado()));
        txtNombreEmpleado.setText(((Empleados)tblEmpleado.getSelectionModel().getSelectedItem()).getNombreEmpleado());
        txtApellidoEmpleado.setText(((Empleados)tblEmpleado.getSelectionModel().getSelectedItem()).getApellidoEmpleado());
        txtSueldo.setText(String.valueOf(((Empleados)tblEmpleado.getSelectionModel().getSelectedItem()).getSueldo()));
        txtDireccion.setText(((Empleados)tblEmpleado.getSelectionModel().getSelectedItem()).getDireccion());
        txtTurno.setText(((Empleados)tblEmpleado.getSelectionModel().getSelectedItem()).getTurno());
        txtIdCargoEmpleado.setText(String.valueOf(((Empleados)tblEmpleado.getSelectionModel().getSelectedItem()).getIdCargoEmpleado()));
    }

    public ObservableList<Empleados> getEmpleados(){
        ArrayList<Empleados> lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpleados()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empleados(
                        resultado.getInt("idEmpleado"),
                        resultado.getString("nombreEmpleado"),
                        resultado.getString("apellidoEmpleado"),
                        resultado.getDouble("sueldo"),
                        resultado.getString("direccion"),
                        resultado.getString("turno"),
                        resultado.getInt("idCargoEmpleado")
                ));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpleados = FXCollections.observableArrayList(lista);
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
                if(tblEmpleado.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "CONFIRMAR SI ELIMINAR EL REGISTRO"
                            , "ELIMINAR EMPLEADO", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpleado(?)");
                            procedimiento.setInt(1, ((Empleados) tblEmpleado.getSelectionModel().getSelectedItem()).getIdEmpleado());
                            procedimiento.execute();
                            listaEmpleados.remove(tblEmpleado.getSelectionModel().getSelectedIndex());
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
                if(tblEmpleado.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("ACTUALIZAR");
                    btnReporte.setText("CANCELAR");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    txtCodigoEmpleado.setEditable(false);
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarEmpleado(?, ?, ?, ?, ?, ?, ?)");
            Empleados empleado = (Empleados) tblEmpleado.getSelectionModel().getSelectedItem();
            empleado.setNombreEmpleado(txtNombreEmpleado.getText());
            empleado.setApellidoEmpleado(txtApellidoEmpleado.getText());
            empleado.setSueldo(Double.parseDouble(txtSueldo.getText()));
            empleado.setDireccion(txtDireccion.getText());
            empleado.setTurno(txtTurno.getText());
            empleado.setIdCargoEmpleado(Integer.parseInt(txtIdCargoEmpleado.getText()));
            procedimiento.setInt(1, empleado.getIdEmpleado());
            procedimiento.setString(2, empleado.getNombreEmpleado());
            procedimiento.setString(3, empleado.getApellidoEmpleado());
            procedimiento.setDouble(4, empleado.getSueldo());
            procedimiento.setString(5, empleado.getDireccion());
            procedimiento.setString(6, empleado.getTurno());
            procedimiento.setInt(7, empleado.getIdCargoEmpleado());
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
        txtCodigoEmpleado.setEditable(false);
        txtNombreEmpleado.setEditable(false);
        txtApellidoEmpleado.setEditable(false);
        txtSueldo.setEditable(false);
        txtDireccion.setEditable(false);
        txtTurno.setEditable(false);
        txtIdCargoEmpleado.setEditable(false);
    }

    public void activarControles(){
        txtCodigoEmpleado.setEditable(true);
        txtNombreEmpleado.setEditable(true);
        txtApellidoEmpleado.setEditable(true);
        txtSueldo.setEditable(true);
        txtDireccion.setEditable(true);
        txtTurno.setEditable(true);
        txtIdCargoEmpleado.setEditable(true);
    }

    public void limpiarControles(){
        txtCodigoEmpleado.clear();
        txtNombreEmpleado.clear();
        txtApellidoEmpleado.clear();
        txtSueldo.clear();
        txtDireccion.clear();
        txtTurno.clear();
        txtIdCargoEmpleado.clear();
    }

    public void guardar(){
        Empleados empleado = new Empleados();
        empleado.setIdEmpleado(Integer.parseInt(txtCodigoEmpleado.getText()));
        empleado.setNombreEmpleado(txtNombreEmpleado.getText());
        empleado.setApellidoEmpleado(txtApellidoEmpleado.getText());
        empleado.setSueldo(Double.parseDouble(txtSueldo.getText()));
        empleado.setDireccion(txtDireccion.getText());
        empleado.setTurno(txtTurno.getText());
        empleado.setIdCargoEmpleado(Integer.parseInt(txtIdCargoEmpleado.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarEmpleado(?, ?, ?, ?, ?, ?, ?)");
            procedimiento.setInt(1, empleado.getIdEmpleado());
            procedimiento.setString(2, empleado.getNombreEmpleado());
            procedimiento.setString(3, empleado.getApellidoEmpleado());
            procedimiento.setDouble(4, empleado.getSueldo());
            procedimiento.setString(5, empleado.getDireccion());
            procedimiento.setString(6, empleado.getTurno());
            procedimiento.setInt(7, empleado.getIdCargoEmpleado());
            procedimiento.execute();
            listaEmpleados.add(empleado);
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

