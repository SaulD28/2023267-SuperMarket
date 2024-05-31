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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML private ComboBox cmbCodigoProveedor;
    @FXML private ComboBox cmbCodigoTipoProducto;
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
        cargaDatos();
        cmbCodigoProveedor.setItems(buscarProveedores());
        cmbCodigoTipoProducto.setItems(buscarTipoProducto());
    }


    
    public void cargaDatos(){
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
    public void selecionarElementos(){
       txtCodigoProducto.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getIdProducto()));
       txtDescripcionProducto.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getDescripcionProducto());
       txtPrecioUnitario.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
       txtPrecioDocena.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioDocena()));
       txtPrecioMayor.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioMayor()));
       txtExistencia.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getExistencia()));
       cmbCodigoTipoProducto.getSelectionModel().select(buscarTipoProducto(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getIdTipoProducto()));
       cmbCodigoProveedor.getSelectionModel().select(buscarProveedores(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getIdProveedor()));
    }
    
    public TipoProducto buscarTipoProducto (int codigoTipoProducto ){
        TipoProducto resultado = null;
        try{
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_buscarTipoProducto(?)");
         procedimiento.setInt(1, codigoTipoProducto);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new TipoProducto(registro.getInt("idTipoProducto"),
                                            registro.getString("descripcionProducto")
             );
         }
        }catch (Exception e){
            e.printStackTrace();
        }    
    
        return resultado;
    }
    
       public Proveedores buscarProveedores (int codigoProveedores ){
        Proveedores resultado = null;
        try{
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_buscarProveedores(?)");
         procedimiento.setInt(1, codigoProveedores);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new Proveedores(registro.getInt("idProveedor"),
                                            registro.getString("NitProveedor"),
                                            registro.getString("nombreProveedor"),
                                            registro.getString("apellidoProveedor"),
                                            registro.getString("direccionProveedor"),
                                            registro.getString("razonSocial"),
                                            registro.getString("contactoPrincipal"),
                                            registro.getString("paginaWeb")
             );
         }
        }catch (Exception e){
            e.printStackTrace();
        }    
    
        return resultado;
    }
        public void guardar (){
            Productos registro = new Productos();
            registro.setIdProducto((txtCodigoProducto.getText()));
            registro.setIdProveedor(((Proveedores)cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getIdProveedor());
            registro.setIdTipoProducto(((TipoProducto)cmbCodigoTipoProducto.getSelectionModel().getSelectedItem()).getIdTipoProducto());
            registro.setDescripcionProducto(txtDescripcionProducto.getText());
            registro.setPrecioDocena(Double.parseDouble(txtPrecioDocena.getText()));
            registro.setPrecioUnitario(Double.parseDouble(txtPrecioUnitario.getText()));
            registro.setPrecioMayor(Double.parseDouble(txtPrecioMayor.getText()));
            registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall ("call sp_agregarProducto(?, ?, ?, ?, ?, ?, ?, ?)");
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
     
     public void desactivarControles(){
        txtCodigoProducto.setEditable(false);
        txtDescripcionProducto.setEditable(false);
        txtPrecioUnitario.setEditable(false);
        txtPrecioDocena.setEditable(false);
        txtPrecioMayor.setEditable(false);
        txtExistencia.setEditable(false);
        cmbCodigoProveedor.setEditable(false);
        cmbCodigoTipoProducto.setEditable(false);
    }    
    
    public void activarControles(){
        txtCodigoProducto.setEditable(true);
        txtDescripcionProducto.setEditable(true);
        txtPrecioUnitario.setEditable(true);
        txtPrecioDocena.setEditable(true);
        txtPrecioMayor.setEditable(true);
        txtExistencia.setEditable(true);
        cmbCodigoProveedor.setEditable(true);
        cmbCodigoTipoProducto.setEditable(true);

    }    
    
    public void limpiarControles(){
      txtCodigoProducto.clear();
        txtDescripcionProducto.clear();
        txtPrecioUnitario.clear();
        txtPrecioDocena.clear();
        txtPrecioMayor.clear();
        txtExistencia.clear();
        cmbCodigoProveedor.getSelectionModel().getSelectedItem();
        cmbCodigoTipoProducto.getSelectionModel().getSelectedItem();
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
             cargaDatos();
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
