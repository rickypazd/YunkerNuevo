/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELO.INDICADORES;

import MODELO.COMPRA.*;
import MODELO.ALMACEN.*;
import Conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ricardopazdemiquel
 */
public class IndicadorINEG {

    private Conexion con = null;

    public IndicadorINEG(Conexion con) {
        this.con = con;
    }

    public String getJson() throws SQLException, JSONException {
        String consulta = "SELECT ARRAY_to_json(ARRAY_AGG(res.*)) as json\n"
                + "FROM(\n"
                + "SELECT * FROM (\n"
                + "SELECT TO_date(fechas.fecha,'dd/mm/yyyy') AS fecha\n"
                + ",(\n"
                + "    SELECT SUM(com.total)\n"
                + "    FROM compra com\n"
                + "    WHERE TO_CHAR(com.fecha,'dd/mm/yyyy') = fechas.fecha\n"
                + ") AS compra,(\n"
                + "    SELECT SUM(com.total)\n"
                + "    FROM venta com\n"
                + "    WHERE TO_CHAR(com.fecha,'dd/mm/yyyy') = fechas.fecha\n"
                + ") AS venta\n"
                + "FROM\n"
                + "(\n"
                + "\n"
                + " SELECT TO_CHAR(com.fecha,'dd/mm/yyyy') AS fecha\n"
                + "        FROM compra com\n"
                + "        WHERE com.estado = 0\n"
                + "    UNION\n"
                + " SELECT TO_CHAR(com.fecha,'dd/mm/yyyy') AS fecha\n"
                + "        FROM venta com\n"
                + "        WHERE com.estado = 0\n"
                + ") AS fechas\n"
                + ") AS tbla ORDER BY fecha\n"
                + ") AS res";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        String resp = "[]";
        if (rs.next()) {
            resp = rs.getString("json");

        }
        ps.close();
        rs.close();
        return resp;
    }

}
