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
public class COMPRA {

    private Conexion con = null;
    private String TBL = "compra";

    public COMPRA(Conexion con) {
        this.con = con;
    }

    public int Insertar(String codigo, Date fecha, String nombreProveedor, double total, int id_admin) throws SQLException {
        String consulta = "INSERT INTO public.compra(\n"
                + "	codigo, fecha, nombre_proveedor, total, id_administrador,fecha_on)\n"
                + "	VALUES (?, ?, ?, ?, ?, now());";
        PreparedStatement ps = con.statamet(consulta);

        ps.setString(1, codigo);
        ps.setTimestamp(2, new Timestamp(fecha.getTime()));
        ps.setString(3, nombreProveedor);
        ps.setDouble(4, total);
        ps.setInt(5, id_admin);

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

    public String getPaginationJSON(int pag, int limit, String busqueda, String fecha, String fechaFin) throws SQLException, JSONException {
        String consultaCount = "SELECT count(ve.id)"
                + "    FROM compra ve\n"
                + "    WHERE(\n"
                + "          UPPER(ve.codigo) LIKE UPPER('%" + busqueda + "%') OR \n"
                + "          UPPER(ve.nombre_proveedor) LIKE UPPER('%" + busqueda + "%')\n"
                + "         )\n"
                + "     AND ve.fecha >= '" + fecha + "' \n"
                + "     AND ve.fecha <= '" + fechaFin + "' \n";

        String consultaArray = "SELECT ve.id, ve.codigo, DATE(ve.fecha) AS fecha , ve.nombre_proveedor, ve.total, ve.id_administrador, ve.estado\n"
                + "    FROM compra ve\n"
                + "    WHERE(\n"
                + "          UPPER(ve.codigo) LIKE UPPER('%" + busqueda + "%') OR \n"
                + "          UPPER(ve.nombre_proveedor) LIKE UPPER('%" + busqueda + "%')\n"
                + "         )\n"
                + "     AND ve.fecha >= '" + fecha + "' \n"
                + "     AND ve.fecha <= '" + fechaFin + "' \n"
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

    public String getById(int id) throws SQLException, JSONException {

        String consulta = "select row_to_json(res) as json\n"
                + "from(\n"
                + "select ve.*, (\n"
                + "    select array_to_json(array_agg(ved.*))\n"
                + "    from com_detalle ved\n"
                + "    where ved.id_compra = ve.id\n"
                + ") as detalle\n"
                + ", (\n"
                + "    select array_to_json(array_agg(usr.*))\n"
                + "    from usuario usr\n"
                + "    where usr.id = ve.id_administrador\n"
                + ") as administrador\n"
                + "from compra ve\n"
                + "where ve.id = " + id + "\n"
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
