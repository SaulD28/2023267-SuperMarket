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
    private ObservableList <TipoProducto> listaTipoProducto;
    
    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtDescripcionProducto;
    @FXML private TextField txtPrecioUnitario;
    @FXML private TextField txtPrecioDocena;
    @FXML private TextField txtPrecioMayor;
    @FXML private TextField txtExistencia;
    @FXML private TextField txtCodigoProveedor;
    @FXML private TextField txtCodigoTipoProducto;
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
        cargarDatos();
        desactivarControles();
    }


    
    public void cargarDatos(){
    tblProductos.setItems(getProductos());
    colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Productos, String>("idProducto"));
    colDescripcionProducto.setCellValueFactory(new PropertyValueFactory<Productos, String>("descripcionProducto"));
    colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioUnitario"));
    colPrecioDocena.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioDocena"));
    colPrecioMayor.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioMayor"));
    colExistencia.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("existencia"));
    colCodigoProveedor.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("idProveedor"));
    colCodigoTipoProducto.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("idTipoProducto"));
    
    
    }
    public void seleccionarElementos(){
       txtCodigoProducto.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getIdProducto()));
       txtDescripcionProducto.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getDescripcionProducto());
       txtPrecioUnitario.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
       txtPrecioDocena.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioDocena()));
       txtPrecioMayor.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioMayor()));
       txtExistencia.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getExistencia()));
       txtCodigoTipoProducto.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getIdTipoProducto()));
       txtCodigoProveedor.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getIdProveedor()));
    }
    
        public void guardar (){
            Productos registro = new Productos();
            registro.setIdProducto(Integer.parseInt(txtCodigoProducto.getText()));
            registro.setIdProveedor(Integer.parseInt(txtCodigoProveedor.getText()));
            registro.setIdTipoProducto(Integer.parseInt(txtCodigoTipoProducto.getText()));
            registro.setDescripcionProducto(txtDescripcionProducto.getText());
            registro.setPrecioDocena(Double.parseDouble(txtPrecioDocena.getText()));
            registro.setPrecioUnitario(Double.parseDouble(txtPrecioUnitario.getText()));
            registro.setPrecioMayor(Double.parseDouble(txtPrecioMayor.getText()));
            registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall ("call sp_AgregarProducto(?, ?, ?, ?, ?, ?, ?, ?, ?)");
                procedimiento.setInt(1, registro.getIdProducto());
                procedimiento.setString(2, registro.getDescripcionProducto());
                procedimiento.setDouble(3, registro.getPrecioUnitario());
                procedimiento.setDouble(4, registro.getPrecioDocena());
                procedimiento.setDouble(5, registro.getPrecioMayor());
                procedimiento.setInt(6, registro.getExistencia());
                procedimiento.setInt(7, registro.getIdProveedor());
                procedimiento.setInt(8, registro.getIdTipoProducto());
                procedimiento.execute();
        
        listaProductos.add(registro);

         }catch (Exception e){
             e.printStackTrace();
         }
     
     }
    
    public ObservableList<Productos> getProductos(){
    ArrayList<Productos> lista = new ArrayList<Productos>();
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos()");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Productos (resultado.getInt("idProducto"),
                                    resultado.getString("descripcionProducto"),
                                    resultado.getDouble("precioUnitario"),
                                    resultado.getDouble("precioDocena"),
                                    resultado.getDouble("precioMayor"),
                                    resultado.getString("imagenProducto"),
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
 
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarProducto(?)");
            Productos registro = (Productos) tblProductos.getSelectionModel().getSelectedItem();
            registro.setDescripcionProducto(txtDescripcionProducto.getText());
            registro.setPrecioUnitario(Double.parseDouble(txtPrecioUnitario.getText()));
            registro.setPrecioDocena(Double.parseDouble(txtPrecioDocena.getText()));
            registro.setPrecioMayor(Double.parseDouble(txtPrecioMayor.getText()));
            registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
            registro.setIdProveedor(Integer.parseInt(txtCodigoProveedor.getText()));
            registro.setIdTipoProducto(Integer.parseInt(txtCodigoTipoProducto.getText()));
            procedimiento.setInt(1, registro.getIdProducto());
            procedimiento.setString(2, registro.getDescripcionProducto());
            procedimiento.setDouble(3, registro.getPrecioUnitario());
            procedimiento.setDouble(4, registro.getPrecioDocena());
            procedimiento.setDouble(5, registro.getPrecioMayor());
            procedimiento.setInt(6, registro.getExistencia());
            procedimiento.setInt(7, registro.getIdProveedor());
            procedimiento.setInt(9, registro.getIdTipoProducto());
            procedimiento.execute();
            procedimiento.execute();
            listaProductos.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
     
     public void desactivarControles(){
        txtCodigoProducto.setEditable(false);
        txtDescripcionProducto.setEditable(false);
        txtPrecioUnitario.setEditable(false);
        txtPrecioDocena.setEditable(false);
        txtPrecioMayor.setEditable(false);
        txtExistencia.setEditable(false);
        txtCodigoProveedor.setEditable(false);
        txtCodigoTipoProducto.setEditable(false);
    }    
    
    public void activarControles(){
        txtCodigoProducto.setEditable(true);
        txtDescripcionProducto.setEditable(true);
        txtPrecioUnitario.setEditable(true);
        txtPrecioDocena.setEditable(true);
        txtPrecioMayor.setEditable(true);
        txtExistencia.setEditable(true);
        txtCodigoProveedor.setEditable(true);
        txtCodigoTipoProducto.setEditable(true);
    }    
    
    public void limpiarControles(){
      txtCodigoProducto.clear();
        txtDescripcionProducto.clear();
        txtPrecioUnitario.clear();
        txtPrecioDocena.clear();
        txtPrecioMayor.clear();
        txtExistencia.clear();
        txtCodigoProveedor.clear();
        txtCodigoTipoProducto.clear();
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
             cargarDatos();
             break;
         }
    
    }
     
     public void eliminar(){
         switch (tipoDeOperacion){
             case ACTUALIZAR:
                 desactivarControles();
                 limpiarControles();
                 btnAgregar.setText("AGREGAR");
                 btnEliminar.setText("ELIMINAR");
                 btnEditar.setDisable(false);
                 btnReporte.setDisable(false);
                 tipoDeOperacion = operaciones.NINGUNO;
                 break;
             default:
                 if(tblProductos.getSelectionModel().getSelectedItem() != null){
                     int respuesta = JOptionPane.showConfirmDialog(null, "CONFIRMA SI SE ELIMINA EL REGISTRO", "ELIMINAR PRODUCTOS", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                     if(respuesta == JOptionPane.YES_OPTION){
                         try{
                             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_EliminarProductos(?)");
                             procedimiento.setInt(1, ((Productos) tblProductos.getSelectionModel().getSelectedItem()).getIdProducto());
                             procedimiento.execute();
                             listaProductos.remove(tblProductos.getSelectionModel().getSelectedItem());
                             limpiarControles();
                         }catch(Exception e){
                             e.printStackTrace();
                         }
                     }else {
                         JOptionPane.showMessageDialog(null, "DEBE DE ALMENOS SELECCIONAR UN PROUCTO");
                     }
                 }
                 
         }
    }
     
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblProductos.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("ACTUALIZAR");
                    btnReporte.setText("CANCELAR");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    txtCodigoProducto.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    
                }else {
                    JOptionPane.showMessageDialog(null, "DEBE SELECCIONAR UN PRODUCTO");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("EDITAR");
                btnReporte.setText("CANCELAR");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                desactivarControles();
                txtCodigoProducto.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                limpiarControles();
                cargarDatos();
                break;
                
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("EDITAR");
                btnReporte.setText("REPORTE");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                txtCodigoProducto.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
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
