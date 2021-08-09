/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELO.COMPRA;

import MODELO.ALMACEN.*;
import Conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import org.json.JSONException;

/**
 *
 * @author ricardopazdemiquel
 */
public class DEVOLUCION {

    private Conexion con = null;
    private String TBL = "devolucion";

    public DEVOLUCION(Conexion con) {
        this.con = con;
    }


}
