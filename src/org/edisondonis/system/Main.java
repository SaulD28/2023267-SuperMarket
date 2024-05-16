/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.edisondonis.system;


import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.edisondonis.controller.MenuCargoEmpleadoController;
import org.edisondonis.controller.MenuClientesController;
import org.edisondonis.controller.MenuComprasController;
import org.edisondonis.controller.MenuPrincipalController;
import org.edisondonis.controller.MenuProgramadorController;
import org.edisondonis.controller.MenuProveedorController;
import org.edisondonis.controller.MenuTipoProductoController;

/**
*NOMBRE: EDISON SAUL DONIS GONZALEZ
*FECHA DE CREACION: 11/4
*FECHA DE MODIFICACIONES:
 */
public class Main extends Application {
    private Stage escenarioPrincipal;
    private Scene escena;
    private final String URLVIEW = "/org/edisondonis/view/";
    
    @Override
    public void start(Stage escenarioPrincipal) throws IOException {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("SUPER MARKET");
        /*  escenarioPrincipal.getIcons().add(new Image(Main.class.getResourceAsStream("/org/edisondonis/images/LogoSuperMarket.jpeg"))); */
        menuPrincipalView();
        escenarioPrincipal.show();
    }
    
    public Initializable cambiarEscena(String fxmlName, int width, int heigth) throws Exception{
        Initializable resultado;
        FXMLLoader loader = new FXMLLoader();
        InputStream file = Main.class.getResourceAsStream(URLVIEW + fxmlName);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(URLVIEW + fxmlName));
        
        escena = new Scene ((AnchorPane)loader.load(file),width, heigth);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        
        resultado = (Initializable)loader.getController();
        
        return resultado;
    }
    
    public void menuPrincipalView (){
        try{
            MenuPrincipalController menuPrincipalView = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 641, 539);
            menuPrincipalView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
            
        }
    }
    
    public void menuClientesView(){
        try{
            MenuClientesController menuClientesView = (MenuClientesController)cambiarEscena("MenuClientesView.fxml", 975, 754);
            menuClientesView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void menuProgramadorView(){
        try{
            MenuProgramadorController menuProgramadorView =(MenuProgramadorController)cambiarEscena("MenuProgramadorView.fxml", 820, 463);
            menuProgramadorView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void menuProveedoresView(){
        try{
            MenuProveedorController menuProveedorView = (MenuProveedorController)cambiarEscena("MenuProveedorView.fxml", 1056,596 );
            menuProveedorView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void menuComprasView(){
        try{
            MenuComprasController menuComprasView = (MenuComprasController)cambiarEscena("MenuComprasView.fxml",1056 ,596);
            menuComprasView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void menuCargoEmpleadoView(){
        try{
            MenuCargoEmpleadoController menuCargoEmpleadoView = (MenuCargoEmpleadoController)cambiarEscena("MenuCargoEmpleado.fxml",1056 ,596);
            menuCargoEmpleadoView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void menuTipoProductoView(){
        try{
        MenuTipoProductoController menuTipoProductoView = (MenuTipoProductoController)cambiarEscena("MenuTipoProdcuto.fxmol",1056 ,596);
        menuTipoProductoView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
