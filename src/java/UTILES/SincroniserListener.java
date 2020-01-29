/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UTILES;

import Conexion.FtpClient;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author ricardopazdemiquel
 */
public class SincroniserListener implements ServletContextListener{
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        try {
//            FtpClient.downloadFolder("/", sce.getServletContext().getRealPath("/"));
//            FtpClient.close();
//        } catch (IOException ex) {
//            Logger.getLogger(SincroniserListener.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
       
    }
    
}
