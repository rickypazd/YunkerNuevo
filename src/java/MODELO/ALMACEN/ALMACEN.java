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
public class ALMACEN {

    private Conexion con = null;
    private String TBL = "almacen";

    public ALMACEN(Conexion con) {
        this.con = con;
    }

    public int Insertar(String nombre, double lat, double lng, String direccion) throws SQLException {
        String consulta = "INSERT INTO public." + TBL + "(\n"
                + "	nombre, lat, lng, direccion)\n"
                + "	VALUES (?, ?, ?, ?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setString(1, nombre);
        ps.setDouble(2, lat);
        ps.setDouble(3, lng);
        ps.setString(4, direccion);

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
        String consultaCount = "select count(ra.id)"
                + "from almacen ra\n"
                + "where ra.estado = 0 \n"
                + "and ( upper(ra.nombre) like upper('%" + busqueda + "%') or\n"
                + "	 upper(ra.direccion) like upper('%" + busqueda + "%') )";

        String consultaArray = "select ra.*\n"
                + "from almacen ra\n"
                + "where ra.estado = 0 \n"
                + "and ( upper(ra.nombre) like upper('%" + busqueda + "%') or\n"
                + "	 upper(ra.direccion) like upper('%" + busqueda + "%') )\n"
                + "	 order by(ra.nombre) asc\n"
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

    public String getCardexPaginationJSON(int pag, int limit, String busqueda) throws SQLException, JSONException {
        String exprecion = "";

        busqueda = busqueda.toLowerCase();
        String[] palabras = busqueda.split(" ");
        int index = 0;

        for (int i = 0; i < palabras.length; i++) {
            if (exprecion.length() > 0) {
                exprecion += " AND ";
            } else {
                exprecion += " WHERE ";
            }
            exprecion += "lower(jsn::varchar) ~ E'" + palabras[i] + "'";

        }
        String consultaCount = "select count(ass.*) from ("
                + "select to_json(jsn.*) as jsn FROM ("
                + "select *,\n"
                + "    (\n"
                + "        select array_to_json(array_agg(mov.*))\n"
                + "        from cardex_movimiento mov\n"
                + "        where mov.id_cardex = ca.id\n"
                + "        \n"
                + "    )\n"
                + "from \n"
                + "    cardex ca,\n"
                + "    repuesto rep\n"
                + "where ca.id_repuesto = rep.id) jsn " + exprecion + ")ass";

        String consultaArray = "select to_json(jsn.*) from\n"
                + "(select ca.*, to_json(rep.*) as repuesto,\n"
                + "    (\n"
                + "        select array_to_json(array_agg(mov.*))\n"
                + "        from cardex_movimiento mov\n"
                + "        where mov.id_cardex = ca.id\n"
                + "        \n"
                + "    ) as movimientos\n"
                + "from \n"
                + "    cardex ca,\n"
                + "    repuesto rep\n"
                + "where ca.id_repuesto = rep.id\n"
                + "order by ca.id desc \n"
                + ") jsn " + exprecion + " LIMIT " + limit + " offset " + (pag * limit) + " ";

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
