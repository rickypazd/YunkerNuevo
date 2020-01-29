package MODELO.REPUESTO;

import MODELO.AUTO.*;
import Conexion.Conexion;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class REPUESTO {

    private int ID;
    private int ID_PADRE;
    private int ESTADO;
    private int TIPO;
    private int ID_REP_SUB_CATEGORIA;
    private String NOMBRE;
    private String CODIGO;
    private String URL_FOTO;
    private String URL_FOTO_ESQUEMA;
    private double PRECIO;
    private Conexion con = null;
    private String TBL = "repuesto";

    public REPUESTO(Conexion con) {
        this.con = con;
    }

    public int Insertar() throws SQLException {
        String consulta = "INSERT INTO public." + TBL + "(\n"
                + "	codigo, nombre, precio, tipo, id_rep_sub_categoria)\n"
                + "	VALUES (?, ?, ?, ?, ?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setString(1, getCODIGO());
        ps.setString(2, getNOMBRE());
        ps.setDouble(3, getPRECIO());
        ps.setInt(4, getTIPO());
        ps.setInt(5, getID_REP_SUB_CATEGORIA());
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

    public int InsertarHijo() throws SQLException {
        String consulta = "INSERT INTO public." + TBL + "(\n"
                + "	codigo, nombre, precio, tipo, id_padre, id_rep_sub_categoria)\n"
                + "	VALUES (?, ?, ?, ?, ?, (SELECT id_rep_sub_categoria from repuesto where id = ?));";
        PreparedStatement ps = con.statamet(consulta);

        ps.setString(1, getCODIGO());
        ps.setString(2, getNOMBRE());
        ps.setDouble(3, getPRECIO());
        ps.setInt(4, getTIPO());
        ps.setInt(5, getID_PADRE());
        ps.setInt(6, getID_PADRE());
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

    public int InsertarPosicionEsquema(int id, double x, double y, double width, double height) throws SQLException {
        String consulta = "INSERT INTO public.rep_posicion_esquema(\n"
                + "	id_repuesto, x, y, width, height)\n"
                + "	VALUES (?, ?, ?, ?, ?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setInt(1, id);
        ps.setDouble(2, x);
        ps.setDouble(3, y);
        ps.setDouble(4, width);
        ps.setDouble(5, height);
        ps.execute();
        ps.close();
        return id;
    }

    public int editarTipo() throws SQLException {
        String consulta = "UPDATE public." + TBL + "\n"
                + "	SET tipo=?\n"
                + "	WHERE id=" + getID();
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, getTIPO());
        int row = ps.executeUpdate();
        ps.close();
        return row;
    }

    public int editar() throws SQLException {
        String consulta = "UPDATE public." + TBL + "\n"
                + "	SET nombre=?\n"
                + "	WHERE id=" + getID();
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, getNOMBRE());
        int row = ps.executeUpdate();
        ps.close();
        return row;
    }

    public int eliminar() throws SQLException {
        String consulta = "UPDATE public." + TBL + " \n"
                + "	SET estado=?\n"
                + "	WHERE id=" + getID();
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, 1);
        int row = ps.executeUpdate();
        ps.close();
        return row;
    }

    public int subir_foto_perfil() throws SQLException {
        String consulta = "UPDATE public." + TBL + " \n"
                + "	SET url_foto=?\n"
                + "	WHERE id=" + getID();
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, getURL_FOTO());
        int row = ps.executeUpdate();
        ps.close();
        return row;
    }

    public int subir_foto_esquema() throws SQLException {
        String consulta = "UPDATE public." + TBL + " \n"
                + "	SET url_foto_esquema=?\n"
                + "	WHERE id=" + getID();
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, getURL_FOTO_ESQUEMA());
        int row = ps.executeUpdate();
        ps.close();
        return row;
    }

    public JSONObject getById(int id) throws SQLException, JSONException {
        String consulta = "select\n"
                + "	re.*, rsc.nombre as subcategoria, rc.nombre as categoria \n"
                + "from \n"
                + "	repuesto re, rep_sub_categoria rsc, rep_categoria rc\n"
                + "where\n"
                + "	re.id=? and re.id_rep_sub_categoria = rsc.id and rsc.id_rep_categoria =  rc.id";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj = parseJson(rs);
        } else {
            obj.put("exito", "no");
        }
        ps.close();
        rs.close();
        return obj;
    }

    public String getByIdJson(int id) throws SQLException, JSONException {
        String consulta = "WITH RECURSIVE recur AS (\n"
                + "    SELECT\n"
                + "        rcr.id,\n"
                + "        rcr.id_padre,\n"
                + "        1 AS lvl,\n"
                + "        (\n"
                + "            SELECT\n"
                + "                ('[ ' || to_json(rcr.*) || ' ]') AS json)::varchar\n"
                + "        FROM\n"
                + "            rep_categoria_rec rcr,\n"
                + "            repuesto rep\n"
                + "        WHERE\n"
                + "            rep.id = ?\n"
                + "            AND rep.id_rep_categoria = rcr.id\n"
                + "        UNION\n"
                + "        SELECT\n"
                + "            e.id,\n"
                + "            e.id_padre,\n"
                + "            s.lvl + 1, (\n"
                + "                SELECT\n"
                + "                    ('[ ' || string_agg(value::TEXT, ', ') || ' ]')::JSON FROM (\n"
                + "                        SELECT\n"
                + "                            * FROM json_array_elements(s.json::json)\n"
                + "                        UNION ALL\n"
                + "                        SELECT\n"
                + "                            to_json(e.*)) t)::VARCHAR\n"
                + "        FROM\n"
                + "            rep_categoria_rec e\n"
                + "            INNER JOIN recur s ON s.id_padre = e.id\n"
                + ")\n"
                + "select to_json(jsf.*) as json\n"
                + "from (\n"
                + "SELECT\n"
                + "    (\n"
                + "    SELECT\n"
                + "        recur.json\n"
                + "    FROM\n"
                + "        recur\n"
                + "    WHERE\n"
                + "        id_padre IS NULL) as categoria,\n"
                + "    (\n"
                + "        SELECT\n"
                + "            to_json(r.*) \n"
                + "        FROM\n"
                + "            repuesto r\n"
                + "        WHERE\n"
                + "            r.id = ?) as repuesto\n"
                + ")jsf";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, id);
        ps.setInt(2, id);

        String resp = "";
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            resp = rs.getString("json");
        }
        ps.close();
        rs.close();
        return resp;
    }

    public String getByIdJson1(int id) throws SQLException, JSONException {
        String consulta = "select row_to_json(final) as json FROM ("
                + "SELECT rep.* ,rsc.nombre AS subcategoria, rc.nombre AS categoria, (SELECT COUNT(car.id_repuesto) FROM cardex car\n"
                + "                WHERE car.id_repuesto = rep.id\n"
                + "                AND car.estado = 0\n"
                + "                GROUP BY (car.id_repuesto)) AS cantidad ,   (SELECT array_to_json(array_agg(rd.*)) AS json\n"
                + "                   	FROM rep_detalle rd\n"
                + "                    WHERE rd.id_repuesto = rep.id\n"
                + "                 ) AS arr_detalle,(\n"
                + "                      WITH RECURSIVE path AS (\n"
                + "\n"
                + "      SELECT id, id_padre,nombre, id_rep_sub_categoria\n"
                + "      FROM repuesto \n"
                + "      WHERE id = rep.id\n"
                + "\n"
                + "  UNION ALL\n"
                + "\n"
                + "      SELECT rep.id, rep.id_padre, rep.nombre, rep.id_rep_sub_categoria\n"
                + "      FROM repuesto rep\n"
                + "      , path AS pa\n"
                + "      WHERE (rep.id = pa.id_padre)    \n"
                + "  )  \n"
                + "      SELECT array_to_json(array_agg(pa.*)) AS json \n"
                + "       FROM path pa\n"
                + "                ) AS  json\n"
                + "            \n"
                + "                FROM repuesto rep, rep_sub_categoria rsc, rep_categoria rc\n"
                + "                WHERE rep.id = ? AND rep.id_rep_sub_categoria = rsc.id AND rep.estado = 0 AND  rsc.id_rep_categoria =  rc.id"
                + " ) as final";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, id);

        String resp = "";
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            resp = rs.getString("json");
        }
        ps.close();
        rs.close();
        return resp;
    }

    public String getByIdPaginaCli(int id) throws SQLException, JSONException {
        String consulta = "select to_json(obj.*) as json\n"
                + "from (\n"
                + "SELECT \n"
                + "	r.*,\n"
                + "	r1.nombre AS nombreSubCat,\n"
                + "    r2.nombre AS nombreCat,\n"
                + "    (\n"
                + "    	select array_to_json(array_agg(repde.*)) as detalle\n"
                + "		from (\n"
                + "				select * FROM rep_detalle rd\n"
                + "				where rd.id_repuesto = r.id\n"
                + "			) repde\n"
                + "    ),(\n"
                + "    	WITH RECURSIVE path AS (\n"
                + "		    SELECT id, id_padre\n"
                + "		    FROM repuesto \n"
                + "		    WHERE id = r.id\n"
                + "\n"
                + "		UNION ALL\n"
                + "\n"
                + "	    SELECT rep.id, rep.id_padre\n"
                + "    	FROM repuesto rep\n"
                + "	    INNER JOIN path AS pa\n"
                + "	    ON (rep.id = pa.id_padre)    \n"
                + "		)\n"
                + "		SELECT array_to_json(array_agg(final.*)) as json\n"
                + "		FROM (\n"
                + "	    SELECT ra.*,rb.id_repuesto \n"
                + "    	FROM (\n"
                + "        SELECT * \n"
                + "        FROM path pa, rep_to_rep_auto rta \n"
                + "        WHERE pa.id = rta.id_repuesto) AS rb \n"
                + "        JOIN (\n"
                + "            SELECT ra.*, rav.nombre AS nombre_version,ram.id AS id_modelo, ram.nombre AS nombre_modelo, ram.url_foto AS url_foto_modelo, rama.id AS id_marca, rama.nombre AS     nombre_marca, rama.url_foto AS url_foto_marca\n"
                + "            FROM rep_auto ra, rep_auto_version rav, rep_auto_modelo ram, rep_auto_marca rama\n"
                + "            WHERE ra.estado = 0 \n"
                + "            AND ra.id_version = rav.id \n"
                + "            AND rav.id_rep_auto_modelo = ram.id\n"
                + "            AND ram.id_rep_auto_marca = rama.id\n"
                + "            AND ( upper(ra.clave) LIKE upper('%%') OR\n"
                + "	         upper(ra.anho) LIKE upper('%%') OR\n"
                + "	        upper(ram.nombre) LIKE upper('%%') OR\n"
                + "	         upper(rama.nombre) LIKE upper('%%') OR\n"
                + "	          upper(rav.nombre) LIKE upper('%%'))\n"
                + "	          order by(rama.nombre,ram.nombre,rav.nombre,ra.anho) ASC\n"
                + "                ) AS ra\n"
                + "		ON ra.id = rb.id_rep_auto\n"
                + "		) AS final\n"
                + "    ) as vehiculos,\n"
                + "("
                + "select \n"
                + "	array_to_json(array_agg(part.*)) as partes\n"
                + "from (\n"
                + "	select * \n"
                + "	from \n"
                + "		repuesto r1 LEFT JOIN \n"
                + "		rep_posicion_esquema rpq\n"
                + "	ON r1.id = rpq.id_repuesto WHERE \n"
                + "		r1.id_padre = r.id\n"
                + ") part"
                + ") as partes \n"
                + "FROM \n"
                + "	repuesto r, \n"
                + "	rep_sub_categoria r1,\n"
                + "	rep_categoria r2\n"
                + "WHERE\n"
                + "	r.id = ?\n"
                + "	AND r1.id = r.id_rep_sub_categoria \n"
                + "	AND r2.id = r1.id_rep_categoria\n"
                + "	\n"
                + ") obj";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, id);

        String resp = "";
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            resp = rs.getString("json");
        }
        ps.close();
        rs.close();
        return resp;
    }

    public JSONArray getAll() throws SQLException, JSONException {
        String consulta = "select ar.* "
                + "from " + TBL + " ar where ar.estado = 0";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray arr = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = parseJson(rs);
            arr.put(obj);
        }
        ps.close();
        rs.close();
        return arr;
    }

    public String getPaginationJSON(int pag, int limit, String busqueda) throws SQLException, JSONException {
        String exprecion = "";

        busqueda = busqueda.toLowerCase();

        String[] palabras = busqueda.split(" ");
        int index = 0;
        if (busqueda.length() > 0) {
            for (int i = 0; i < palabras.length; i++) {
                if (exprecion.length() > 0) {
                    exprecion += " AND ";
                } else {
                    exprecion += " WHERE ";
                }
                exprecion += "lower(ass::varchar) ~ E'" + palabras[i] + "'";

            }
        }

        String consulta = "WITH RECURSIVE recur AS (\n"
                + "    SELECT\n"
                + "        rcr.id,\n"
                + "        rcr.id_padre,\n"
                + "        1 AS lvl,\n"
                + "        (\n"
                + "            SELECT\n"
                + "                ('[ ' || to_json(rcr.*) || ' ]') AS json)::varchar\n"
                + "        FROM\n"
                + "            rep_categoria_rec rcr\n"
                + "        WHERE\n"
                + "            rcr.id_padre IS NULL\n"
                + "        UNION\n"
                + "        SELECT\n"
                + "            e.id,\n"
                + "            e.id_padre,\n"
                + "            s.lvl + 1, (\n"
                + "                SELECT\n"
                + "                    ('[ ' || string_agg(value::TEXT, ', ') || ' ]')::JSON FROM (\n"
                + "                        SELECT\n"
                + "                            * FROM json_array_elements(s.json::json)\n"
                + "                        UNION ALL\n"
                + "                        SELECT\n"
                + "                            to_json(e.*)) t)::VARCHAR\n"
                + "        FROM\n"
                + "            rep_categoria_rec e\n"
                + "            INNER JOIN recur s ON s.id = e.id_padre\n"
                + ")\n"
                + "select(\n"
                + "SELECT\n"
                + "    array_to_json(array_agg(asfinal.*))\n"
                + "FROM (\n"
                + "    SELECT\n"
                + "        to_json(ass.*) AS obj\n"
                + "    FROM (\n"
                + "        SELECT\n"
                + "            to_json(rp.*) AS repuesto,\n"
                + "            rc.json::json AS categorias\n"
                + "            ,(select array_to_json(array_agg(det.*)) from rep_detalle det where det.id_repuesto = rp.id ) as detalle"
                + "            ,(SELECT COUNT(car.id_repuesto) FROM cardex car\n"
                + "                WHERE car.id_repuesto = rp.id\n"
                + "                AND car.estado = 0\n"
                + "                GROUP BY (car.id_repuesto)) AS cantidad"
                + "        FROM\n"
                + "            repuesto rp,\n"
                + "            recur rc\n"
                + "        WHERE\n"
                + "            rp.id_rep_categoria = rc.id\n"
                + "            AND rp.estado = 0) ass\n"
                + exprecion
                + "    LIMIT " + limit + " offset " + (pag * limit) + " ) asfinal\n"
                + ") as data,\n"
                + "(\n"
                + "SELECT\n"
                + "    count(asfinal2.*) \n"
                + "FROM (\n"
                + "    SELECT\n"
                + "        to_json(ass.*) AS obj\n"
                + "    FROM (\n"
                + "        SELECT\n"
                + "            to_json(rp.*) AS repuesto,\n"
                + "            rc.json::json AS categorias\n"
                + "            ,(select array_to_json(array_agg(det.*)) from rep_detalle det where det.id_repuesto = rp.id ) as detalle"
                + "        FROM\n"
                + "            repuesto rp,\n"
                + "            recur rc\n"
                + "        WHERE\n"
                + "            rp.id_rep_categoria = rc.id\n"
                + "            AND rp.estado = 0) ass\n"
                + exprecion
                + "    ) asfinal2\n"
                + ") as cantidad";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        String resp = "error";
        JSONObject obj = new JSONObject();
        if (rs.next()) {

            obj.put("data", rs.getString("data"));
            obj.put("cantidad", rs.getInt("cantidad"));

        }
        ps.close();
        rs.close();
        return obj.toString();

    }

    public String getPaginationJSON2(int pag, int limit, String busqueda) throws SQLException, JSONException {
        String consultaCount = "select count(re.id) \n"
                + "			FROM repuesto re\n"
                + "           LEFT JOIN (\n"
                + "                SELECT rsc.id, rsc.nombre AS subcategoria, rc.nombre AS categoria \n"
                + "                FROM rep_sub_categoria rsc, rep_categoria rc\n"
                + "                WHERE rsc.id_rep_categoria =  rc.id \n"
                + "\n"
                + "                        ) ca\n"
                + "           ON re.id_rep_sub_categoria = ca.id\n"
                + "			WHERE\n"
                + "           re.estado = 0 "
                + "			and (upper(re.codigo) like upper('%" + busqueda + "%') or\n"
                + "  				upper(re.nombre) like upper('%" + busqueda + "%') or\n"
                + "				upper(ca.subcategoria) like upper('%" + busqueda + "%') or\n"
                + "				upper(ca.categoria) like upper('%" + busqueda + "%'))";

        String consultaArray = "SELECT rep.* , rsc.nombre AS subcategoria, rc.nombre AS categoria, (SELECT COUNT(car.id_repuesto) FROM cardex car\n"
                + "                WHERE car.id_repuesto = rep.id\n"
                + "                AND car.estado = 0\n"
                + "                GROUP BY (car.id_repuesto)) AS cantidad \n"
                + "                FROM repuesto rep, rep_sub_categoria rsc, rep_categoria rc\n"
                + "                WHERE rep.id_rep_sub_categoria = rsc.id AND rep.estado = 0 AND  rsc.id_rep_categoria =  rc.id"
                + "			and (upper(rep.codigo) like upper('%" + busqueda + "%') or\n"
                + "  				upper(rep.nombre) like upper('%" + busqueda + "%') or\n"
                + "				upper(rsc.nombre) like upper('%" + busqueda + "%') or\n"
                + "				upper(rc.nombre) like upper('%" + busqueda + "%'))\n"
                + "			order by (rep.nombre) asc\n"
                + "            limit " + limit + " offset " + (pag * limit);
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

    public String getPaginationJSONPart(int pag, int limit, String busqueda, int id_padre) throws SQLException, JSONException {
        String consultaCount = "select count(re.id) \n"
                + "			from repuesto re\n"
                + "			where\n"
                + "			re.estado = 0 and re.id_padre = " + id_padre + " \n"
                + "			and (upper(re.codigo) like upper('%" + busqueda + "%') or\n"
                + "  				upper(re.nombre) like upper('%" + busqueda + "%'))	";

        String consultaArray = "select rp.*,to_json(rpe.*) jsonpos\n"
                + "from repuesto rp, rep_posicion_esquema rpe \n"
                + "where rp.id = rpe.id_repuesto\n"
                + "and rpe.id_esquema = " + id_padre + "\n"
                + "and rp.estado = 0\n"
                + "and rpe.estado = 0";
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

    public String getTop8() throws SQLException, JSONException {

        String consulta = "SELECT array_to_json(array_agg(arr.*)) AS json FROM (\n"
                + "SELECT rep2.*, tblrsc.nombre AS nombresubcat, tblrc.nombre AS nombrecat,\n"
                + "(SELECT COUNT(car.id_repuesto) FROM cardex car\n"
                + "                                WHERE car.id_repuesto = rep2.id\n"
                + "                                AND car.estado = 0\n"
                + "                                GROUP BY (car.id_repuesto)) AS cantidad\n"
                + "FROM (\n"
                + "        SELECT r.* , COALESCE(cuenta.cuen,0) AS cant FROM repuesto r\n"
                + "        LEFT JOIN (\n"
                + "        SELECT id_relacion , count(s.id) AS cuen \n"
                + "        FROM seguimiento_busqueda s\n"
                + "        WHERE tipo = 5\n"
                + "        group by(id_relacion)\n"
                + "        ) cuenta \n"
                + "        ON cuenta.id_relacion = r.id\n"
                + "        WHERE r.estado = 0\n"
                + "        ORDER BY cant DESC\n"
                + "        LIMIT 8 \n"
                + ") AS rep2,\n"
                + "     rep_sub_categoria tblrsc,\n"
                + "     rep_categoria tblrc\n"
                + "WHERE rep2.id_rep_sub_categoria = tblrsc.id\n"
                + "AND tblrc.id = tblrsc.id_rep_categoria\n"
                + "ORDER BY cant DESC"
                + ") arr";
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

    private JSONObject parseObj;

    private JSONObject parseJson(ResultSet rs) throws JSONException, SQLException {
        parseObj = new JSONObject();
        parseObj.put("id", rs.getInt("id"));
        parseObj.put("id_rep_sub_categoria", rs.getInt("id_rep_sub_categoria"));
        parseObj.put("tipo", rs.getInt("tipo"));
        parseObj.put("estado", rs.getInt("estado"));
        parseObj.put("precio", rs.getDouble("precio"));
        parseObj.put("nombre", rs.getString("nombre") != null ? rs.getString("nombre") : "");
        parseObj.put("codigo", rs.getString("codigo") != null ? rs.getString("codigo") : "");
        parseObj.put("url_foto", rs.getString("url_foto") != null ? rs.getString("url_foto") : "img/Sin_imagen.png");
        return parseObj;
    }

    public JSONObject getJson() throws JSONException, SQLException {
        JSONObject obj = new JSONObject();
        obj.put("id", getID());
        obj.put("id_rep_sub_categoria", getID_REP_SUB_CATEGORIA());
        obj.put("tipo", getTIPO());
        obj.put("estado", getESTADO());
        obj.put("precio", getPRECIO());
        obj.put("nombre", getNOMBRE());
        obj.put("codigo", getCODIGO());
        obj.put("url_foto", getURL_FOTO());

        return obj;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getURL_FOTO() {
        return URL_FOTO;
    }

    public void setURL_FOTO(String URL_FOTO) {
        this.URL_FOTO = URL_FOTO;
    }

    public int getESTADO() {
        return ESTADO;
    }

    public void setESTADO(int ESTADO) {
        this.ESTADO = ESTADO;
    }

    public int getTIPO() {
        return TIPO;
    }

    public String getURL_FOTO_ESQUEMA() {
        return URL_FOTO_ESQUEMA;
    }

    public void setURL_FOTO_ESQUEMA(String URL_FOTO_ESQUEMA) {
        this.URL_FOTO_ESQUEMA = URL_FOTO_ESQUEMA;
    }

    public void setTIPO(int TIPO) {
        this.TIPO = TIPO;
    }

    public int getID_REP_SUB_CATEGORIA() {
        return ID_REP_SUB_CATEGORIA;
    }

    public void setID_REP_SUB_CATEGORIA(int ID_REP_SUB_CATEGORIA) {
        this.ID_REP_SUB_CATEGORIA = ID_REP_SUB_CATEGORIA;
    }

    public String getCODIGO() {
        return CODIGO;
    }

    public void setCODIGO(String CODIGO) {
        this.CODIGO = CODIGO;
    }

    public double getPRECIO() {
        return PRECIO;
    }

    public void setPRECIO(double PRECIO) {
        this.PRECIO = PRECIO;
    }

    public String getTBL() {
        return TBL;
    }

    public void setTBL(String TBL) {
        this.TBL = TBL;
    }

    public Conexion getCon() {
        return con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    public int getID_PADRE() {
        return ID_PADRE;
    }

    public void setID_PADRE(int ID_PADRE) {
        this.ID_PADRE = ID_PADRE;
    }

    public JSONObject getParseObj() {
        return parseObj;
    }

    public void setParseObj(JSONObject parseObj) {
        this.parseObj = parseObj;
    }

}
