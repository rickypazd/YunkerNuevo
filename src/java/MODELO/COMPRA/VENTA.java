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
public class VENTA {

    private Conexion con = null;
    private String TBL = "venta";

    public VENTA(Conexion con) {
        this.con = con;
    }

    public int Insertar(String codigo, Date fecha, String nit, String nombreCliente, double total, int id_admin) throws SQLException {
        String consulta = "INSERT INTO public.venta(\n"
                + "	codigo, fecha, nit, nombre_cliente, total, id_administrador)\n"
                + "	VALUES (?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setString(1, codigo);
        ps.setTimestamp(2, new Timestamp(fecha.getTime()));
        ps.setString(3, nit);
        ps.setString(4, nombreCliente);
        ps.setDouble(5, total);
        ps.setInt(6, id_admin);

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

    public String getPaginationJSON(int pag, int limit, String busqueda) throws SQLException, JSONException {
        String consultaCount = "SELECT count(ve.id)"
                + "    FROM venta ve\n"
                + "    WHERE(\n"
                + "          UPPER(ve.codigo) LIKE UPPER('%"+busqueda+"%') OR \n"
                + "          UPPER(ve.nit) LIKE UPPER('%"+busqueda+"%') OR\n"
                + "          UPPER(ve.nombre_cliente) LIKE UPPER('%"+busqueda+"%')\n"
                + "         )\n";

        String consultaArray = "SELECT ve.id, ve.codigo, DATE(ve.fecha) AS fecha ,  ve.nit, ve.nombre_cliente, ve.total, ve.id_administrador, ve.estado\n"
                + "    FROM venta ve\n"
                + "    WHERE(\n"
                + "          UPPER(ve.codigo) LIKE UPPER('%"+busqueda+"%') OR \n"
                + "          UPPER(ve.nit) LIKE UPPER('%"+busqueda+"%') OR\n"
                + "          UPPER(ve.nombre_cliente) LIKE UPPER('%"+busqueda+"%')\n"
                + "         )\n"
                + "    ORDER BY ve.fecha DESC \n"
                + "limit " + limit + " offset " + (pag * limit);
        String consulta = "select row_to_json(res) as json\n"
                + "from(\n"
                + "	select (\n"
                + consultaCount
                + "		), (\n"
                + "		select array_to_json(array_agg(list.*))\n"
                + "		from (\n"
                + consultaArray
                + "			) as list\n"
                + "		) as arrayjson\n"
                + "	) as res";
        PreparedStatement ps = con.statamet(consulta);
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
