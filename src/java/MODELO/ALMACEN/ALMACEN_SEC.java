/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELO.ALMACEN;

import Conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONException;

/**
 *
 * @author ricardopazdemiquel
 */
public class ALMACEN_SEC {

    private Conexion con = null;
    private String TBL = "almacen_sec";

    public ALMACEN_SEC(Conexion con) {
        this.con = con;
    }

    public int Insertar(int id_almacen, int id_padre, String nombre, int x1, int y1, int x2, int y2) throws SQLException {
        String consulta = "INSERT INTO public.almacen_sec(\n"
                + "	 id_almacen, id_padre, nombre, x1, y1, x2, y2)\n"
                + "	VALUES ( ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, id_almacen);
        ps.setInt(2, id_padre);
        ps.setString(3, nombre);
        ps.setInt(4, x1);
        ps.setInt(5, y1);
        ps.setInt(6, x2);
        ps.setInt(7, y2);

        ps.execute();
        consulta = "select last_value from " + TBL + "_id_seq ";
        ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt("last_value");
        }
        rs.close();
        ps.close();
        return id;
    }

    public int eliminar(int id) throws SQLException {
        String consulta = "UPDATE public." + TBL + " \n"
                + "	SET estado=?\n"
                + "	WHERE id = ?";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, 1);
        ps.setInt(2, id);
        int row = ps.executeUpdate();
        ps.close();
        return row;
    }
    public int editar_pos(int id, int x1, int y1, int x2, int y2) throws SQLException {
        String consulta = "UPDATE public." + TBL + " \n"
                + "	SET x1=?, y1=?, x2=?, y2=? \n"
                + "	WHERE id = ?";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, x1);
        ps.setInt(2,y1);
        ps.setInt(3, x2);
        ps.setInt(4, y2);
        ps.setInt(5, id);
        int row = ps.executeUpdate();
        ps.close();
        return row;
    }

    public String getAll(int id_almacen) throws SQLException, JSONException {

        String consulta = "SELECT array_to_json(ARRAY_AGG(con1.*)) AS json\n"
                + "FROM (\n"
                + "SELECT *\n"
                + "FROM almacen_sec als\n"
                + "WHERE als.id_almacen = ?\n"
                + "AND als.estado = 0\n"
                + ")AS con1";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, id_almacen);
        ResultSet rs = ps.executeQuery();
        String resp = "error";
        if (rs.next()) {
            resp = rs.getString("json");

        }
        ps.close();
        rs.close();
        return resp;

    }

}
