/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.edisondonis.report;

import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.edisondonis.dao.Conexion;

/**
 *
 * @author informatica
 */
public class GenerarReportes {
    public static void mostrarReportes(String nombreReporte, String titulo, Map parametro){
        InputStream reporte = GenerarReportes.class.getResourceAsStream(nombreReporte);
        try{
        JasperReport reporteMaestro = (JasperReport)JRLoader.loadObject(reporte);    
        JasperPrint reporteImpreso = JasperFillManager.fillReport(reporteMaestro, parametro, Conexion.getInstance().getConexion());
        JasperViewer visor = new JasperViewer(reporteImpreso, false);
        visor.setTitle(titulo);
        visor.setVisible(true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
/*
Interface Map
 HashMap es uno de los objetos que implementa un conjunto de key-valu.
 Tiene un constructor sin parametros new HashMap() y su finalidad susle 
 referirse para agrupar  informacion en un unico objeto.
*/