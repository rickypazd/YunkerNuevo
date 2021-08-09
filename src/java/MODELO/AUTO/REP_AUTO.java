package MODELO.AUTO;

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

public class REP_AUTO {

    private int ID;
    private int ESTADO;
    private String CLAVE;
    private String ANHO;
    private int ID_VERSION;
    private Conexion con = null;
    private String TBL = "rep_auto";

    public REP_AUTO(Conexion con) {
        this.con = con;
    }

    public int Insertar() throws SQLException {
        String consulta = "INSERT INTO public." + TBL + "(\n"
                + "	clave, anho, id_version)\n"
                + "	VALUES (?, ?, ?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setString(1, getCLAVE());
        ps.setString(2, getANHO());
        ps.setInt(3, getID_VERSION());
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

    public int agg_auto_to_rep(int id_auto, int id_rep) throws SQLException {
        String consulta = "INSERT INTO public.rep_to_rep_auto(\n"
                + "	id_repuesto, id_rep_auto)\n"
                + "	VALUES (?, ?);";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, id_rep);
        ps.setInt(2, id_auto);
        ps.execute();
        ps.close();
        return 1;
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

    public int eliminar_auto_to_rep(int id_auto, int id_rep) throws SQLException {
        String consulta = "DELETE from public.rep_to_rep_auto\n"
                + "	WHERE id_repuesto = ? and id_rep_auto = ?";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, id_rep);
        ps.setInt(2, id_auto);
        ps.execute();
        ps.close();
        return 1;
    }

    public int editar() throws SQLException {
        String consulta = "UPDATE public." + TBL + "\n"
                + "	SET nombre=?\n"
                + "	WHERE id=" + getID();
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, getCLAVE());
        int row = ps.executeUpdate();
        ps.close();
        return row;
    }

    public JSONObject getById(int id) throws SQLException, JSONException {
        String consulta = "select ar.*, mar.nombre as nombre_marca, mar.url_foto as url_foto_marca "
                + "from " + TBL + " ar, rep_auto_marca mar\n"
                + "where ar.estado = 0 and ar.id=? and ar.id_rep_auto_marca = mar.id";
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

    public JSONArray getAll() throws SQLException, JSONException {
        String consulta = "select ra.*, rav.nombre as nombre_version, ram.id as id_modelo, ram.nombre as nombre_modelo, ram.url_foto as url_foto_modelo, rama.id as id_marca, rama.nombre as nombre_marca, rama.url_foto as url_foto_marca\n"
                + "from rep_auto ra, rep_auto_version rav, rep_auto_modelo ram, rep_auto_marca rama\n"
                + "where ra.estado = 0 \n"
                + "and ra.id_version = rav.id \n"
                + "and rav.id_rep_auto_modelo = ram.id\n"
                + "and ram.id_rep_auto_marca = rama.id";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray arr = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = parseJson(rs);
            obj.put("nombre_version", rs.getString("nombre_version") != null ? rs.getString("nombre_version") : "");
            obj.put("id_marca", rs.getInt("id_marca"));
            obj.put("nombre_marca", rs.getString("nombre_marca") != null ? rs.getString("nombre_marca") : "");
            obj.put("url_foto_marca", rs.getString("url_foto_marca") != null ? rs.getString("url_foto_marca") : "img/Sin_imagen.png");
            obj.put("id_modelo", rs.getInt("id_marca"));
            obj.put("nombre_modelo", rs.getString("nombre_modelo") != null ? rs.getString("nombre_modelo") : "");
            obj.put("url_foto_modelo", rs.getString("url_foto_modelo") != null ? rs.getString("url_foto_modelo") : "img/Sin_imagen.png");
            arr.put(obj);
        }
        ps.close();
        rs.close();
        return arr;
    }

    public JSONArray getBy_id_rep_auto_marca(int id) throws SQLException, JSONException {
        String consulta = "select ar.* "
                + "from " + TBL + " ar where ar.estado = 0 and ar.id_rep_auto_marca = " + id;
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
                + "select * from rep_auto ra, rep_auto_version rav, rep_auto_modelo ram, rep_auto_marca rama\n"
                + "where ra.estado = 0 \n"
                + "and ra.id_version = rav.id \n"
                + "and rav.id_rep_auto_modelo = ram.id\n"
                + "and ram.id_rep_auto_marca = rama.id) jsn " + exprecion + ")ass";

        String consultaArray = "select to_json(jsn.*) from\n"
                + "(select ra.*, rav.nombre as nombre_version,ram.id as id_modelo, ram.nombre as nombre_modelo, ram.url_foto as url_foto_modelo, rama.id as id_marca, rama.nombre as nombre_marca, rama.url_foto as url_foto_marca\n"
                + "from rep_auto ra, rep_auto_version rav, rep_auto_modelo ram, rep_auto_marca rama\n"
                + "where ra.estado = 0 \n"
                + "and ra.id_version = rav.id \n"
                + "and rav.id_rep_auto_modelo = ram.id\n"
                + "and ram.id_rep_auto_marca = rama.id\n"
                + "order by(rama.nombre,ram.nombre,rav.nombre,ra.anho) asc\n"
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

    public String getVehiculosDisponibles(int id_rep, String busqueda) throws SQLException, JSONException {

        String consulta = "WITH RECURSIVE path AS (\n"
                + "\n"
                + "    SELECT id, id_padre\n"
                + "    FROM repuesto \n"
                + "    WHERE id = ?\n"
                + "\n"
                + "UNION ALL\n"
                + "\n"
                + "    SELECT rep.id, rep.id_padre\n"
                + "    FROM repuesto rep\n"
                + "    INNER JOIN path AS pa\n"
                + "    ON (rep.id = pa.id_padre)    \n"
                + ")\n"
                + "SELECT array_to_json(array_agg(ra.*)) as json \n"
                + "FROM (\n"
                + "    SELECT * , pa.id as idp\n"
                + "    FROM path pa, rep_to_rep_auto rta \n"
                + "    WHERE pa.id = rta.id_repuesto) AS rb \n"
                + "RIGHT JOIN (\n"
                + "    SELECT ra.*, rav.nombre AS nombre_version,ram.id AS id_modelo, ram.nombre AS nombre_modelo, ram.url_foto AS url_foto_modelo, rama.id AS id_marca, rama.nombre AS nombre_marca,rama.url_foto AS url_foto_marca\n"
                + "    FROM rep_auto ra, rep_auto_version rav, rep_auto_modelo ram, rep_auto_marca rama\n"
                + "    WHERE ra.estado = 0 \n"
                + "    AND ra.id_version = rav.id \n"
                + "    AND rav.id_rep_auto_modelo = ram.id\n"
                + "    AND ram.id_rep_auto_marca = rama.id\n"
                + "    AND ( upper(ra.clave) LIKE upper('%" + busqueda + "%') OR\n"
                + "	 upper(ra.anho) LIKE upper('%" + busqueda + "%') OR\n"
                + "	 upper(ram.nombre) LIKE upper('%" + busqueda + "%') OR\n"
                + "	 upper(rama.nombre) LIKE upper('%" + busqueda + "%') OR\n"
                + "	  upper(rav.nombre) LIKE upper('%" + busqueda + "%'))\n"
                + "	  order by(rama.nombre,ram.nombre,rav.nombre,ra.anho) ASC\n"
                + " limit 30            ) AS ra\n"
                + "ON ra.id = rb.id_rep_auto\n"
                + "WHERE rb.idp IS null";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, id_rep);
        ResultSet rs = ps.executeQuery();
        String resp = "error";
        if (rs.next()) {
            resp = rs.getString("json");

        }
        ps.close();
        rs.close();
        return resp;

    }

    public String getVehiculosActivos(int id_rep, String busqueda) throws SQLException, JSONException {

        String consulta = "WITH RECURSIVE path AS (\n"
                + "\n"
                + "    SELECT id, id_padre\n"
                + "    FROM repuesto \n"
                + "    WHERE id = ?\n"
                + "\n"
                + "UNION ALL\n"
                + "\n"
                + "    SELECT rep.id, rep.id_padre\n"
                + "    FROM repuesto rep\n"
                + "    INNER JOIN path AS pa\n"
                + "    ON (rep.id = pa.id_padre)    \n"
                + ")\n"
                + "SELECT array_to_json(array_agg(final.*)) as json\n"
                + "FROM (\n"
                + "    SELECT *\n"
                + "    FROM (\n"
                + "        SELECT rta.id_rep_auto ,max(rta.id_repuesto) as id_repuesto\n"
                + "        FROM path pa, rep_to_rep_auto rta \n"
                + "        WHERE pa.id = rta.id_repuesto\n"
                + "        group by (rta.id_rep_auto) ) AS rb \n"
                + "        JOIN (\n"
                + "            SELECT ra.*, rav.nombre AS nombre_version,ram.id AS id_modelo, ram.nombre AS nombre_modelo, ram.url_foto AS url_foto_modelo, rama.id AS id_marca, rama.nombre AS     nombre_marca, rama.url_foto AS url_foto_marca\n"
                + "            FROM rep_auto ra, rep_auto_version rav, rep_auto_modelo ram, rep_auto_marca rama\n"
                + "            WHERE ra.estado = 0 \n"
                + "            AND ra.id_version = rav.id \n"
                + "            AND rav.id_rep_auto_modelo = ram.id\n"
                + "            AND ram.id_rep_auto_marca = rama.id\n"
                + "            AND ( upper(ra.clave) LIKE upper('%" + busqueda + "%') OR\n"
                + "	         upper(ra.anho) LIKE upper('%" + busqueda + "%') OR\n"
                + "	        upper(ram.nombre) LIKE upper('%" + busqueda + "%') OR\n"
                + "	         upper(rama.nombre) LIKE upper('%" + busqueda + "%') OR\n"
                + "	          upper(rav.nombre) LIKE upper('%" + busqueda + "%'))\n"
                + "	          order by(rama.nombre,ram.nombre,rav.nombre,ra.anho) ASC\n"
                + "                ) AS ra\n"
                + "ON ra.id = rb.id_rep_auto\n"
                + "\n"
                + ") AS final";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, id_rep);
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
        parseObj.put("estado", rs.getInt("estado"));
        parseObj.put("id_version", rs.getInt("id_version"));
        parseObj.put("clave", rs.getString("clave") != null ? rs.getString("clave") : "");
        parseObj.put("anho", rs.getString("anho") != null ? rs.getString("anho") : "");
        return parseObj;
    }

    public JSONObject getJson() throws JSONException, SQLException {
        JSONObject obj = new JSONObject();
        obj.put("id", getID());
        obj.put("id_version", getID_VERSION());
        obj.put("anho", getANHO());
        obj.put("clave", getCLAVE());
        obj.put("estado", getESTADO());
        return obj;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getESTADO() {
        return ESTADO;
    }

    public void setESTADO(int ESTADO) {
        this.ESTADO = ESTADO;
    }

    public String getCLAVE() {
        return CLAVE;
    }

    public void setCLAVE(String CLAVE) {
        this.CLAVE = CLAVE;
    }

    public int getID_VERSION() {
        return ID_VERSION;
    }

    public void setID_VERSION(int ID_VERSION) {
        this.ID_VERSION = ID_VERSION;
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

    public JSONObject getParseObj() {
        return parseObj;
    }

    public void setParseObj(JSONObject parseObj) {
        this.parseObj = parseObj;
    }

    public String getANHO() {
        return ANHO;
    }

    public void setANHO(String ANHO) {
        this.ANHO = ANHO;
    }

}
