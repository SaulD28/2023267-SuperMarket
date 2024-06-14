package org.edisondonis.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author usuario
 */
public class Conexion {
    private Connection conexion;
    private static Conexion instancia;
    
    
    public Conexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection
        ("jdbc:mysql://localhost:3306/DBSuperMarket?useSSL=false","root","20232671");
        } catch (ClassNotFoundException | SQLException
                | InstantiationException | IllegalAccessException e) {
            e.printStackTrace(); 
        }
    }
    
    public static Conexion getInstance(){
        if(instancia == null)
            instancia = new Conexion();
        return instancia;
    }
    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }   
}
