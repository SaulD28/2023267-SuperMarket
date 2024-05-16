/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.edisondonis.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import org.edisondonis.system.Main;

/**
 *
 * @author usuario
 */
public class MenuProgramadorController implements Initializable {
     private Main escenarioPrincipal;
    
    @FXML private Button btnRegresar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        
    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
    @FXML
    public void regresar (ActionEvent event){
        if(event.getSource() == btnRegresar){
            escenarioPrincipal.menuPrincipalView();
        }
    }
    // SE COPIA EL METODO QUE AL FUN Y AL CABO ES LO MISMO
    
    // LUEGO CAMBIAMOS EL BTN QUE QUEREMOS QUE DETECTE, COMO ES COMO EVENTOS, EL EVENTO DE 
    // CUANDO SE ACCIONE EL MENUITEM LLAMADO PROGRAMADOR TIENE QUE LLAMAR A LA PESTAÑA PROGRAMADOR
    // ENTONCES CAMBIAMOS BTNREGRESAR POR btnProgramador, ACA EL EVENTO DE btnProgramador VA A LLAMAR AL METODO
    //  AHORA ESTE METODO ESTA EN LA MAIN, AL PONER EL . VAS A VER QUE DICE PROGRAMADOR VIEW QUE ES ESTE MIRA
    
    // AHORA EJECUTAMOS Y AL DETECTAR QUE PRECIONASTE EL BOTON VA A LALMAR A LA PESTAÑA A JAJAJJA
    
    // a ver donde esta el error?
   // vamos a ver si lo encontras XDDDD
    // segun yo es porque ya esta definido el metodo? si te das cuenta tiene el mismo nombre uwu's se llama regresar y se tiene que llamar
    // programador ya? u otro nombre ya?, ya viste que no da error?osea se busca hacer lo mismo solo que con un idetificador diferente de 
    // de boton y de metodo?  eso es polimofismo aleluya lo entendiste uwu's, va ahora tenemos que decirle al item menu que al 
    // accionase se inyecte el metodo programador,vamos al pgotochop
    
    
}
