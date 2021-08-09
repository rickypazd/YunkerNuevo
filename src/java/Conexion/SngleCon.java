/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import UTILES.URL;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricardopazdemiquel
 */
public class SngleCon {
    
    private static Conexion con;
    
    public static Conexion getCon(){
        if(con == null){
           con = new Conexion(URL.db_usr, URL.db_pass);
        }
        if(!con.ifConnected()){
            try {
                con.Conectar();
            } catch (SQLException ex) {
                Logger.getLogger(SngleCon.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return con;
    }
}
