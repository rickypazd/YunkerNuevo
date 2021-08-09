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

public class REP_AUTO_VERSION {

    private int ID;
    private int ESTADO;
    private String NOMBRE;
    private int ID_REP_AUTO_MODELO;
    private Conexion con = null;
    private String TBL = "rep_auto_version";

    public REP_AUTO_VERSION(Conexion con) {
        this.con = con;
    }

    public int Insertar() throws SQLException {
        String consulta = "INSERT INTO public." + TBL + "(\n"
                + "	nombre, id_rep_auto_modelo)\n"
                + "	VALUES (?, ?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setString(1, getNOMBRE());
        ps.setInt(2, getID_REP_AUTO_MODELO());
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

    
    public JSONObject getById(int id) throws SQLException, JSONException {
        String consulta = "select ar.*"
                + "from " + TBL + " ar\n"
                + "where ar.estado = 0 and ar.id=?";
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

    public JSONArray getBy_id_rep_auto_modelo(int id) throws SQLException, JSONException {
        String consulta = "select ar.* "
                + "from " + TBL + " ar where ar.estado = 0 and ar.id_rep_auto_modelo = " + id;
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

    public JSONArray getBy_id_rep_auto_marca_registrados(int id) throws SQLException, JSONException {
        String consulta = "select ram.* from rep_auto ra, rep_auto_modelo ram\n"
                + "where ra.id_modelo = ram.id and ram.id_rep_auto_marca = " + id + "\n"
                + "group by(ram.id)";
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

    public JSONArray getModelosPagination(int pag, int limit, String busqueda) throws SQLException, JSONException {
        String consulta = "select mod.*, ram.id as id_marca, ram.nombre as nombre_marca, ram.url_foto as url_foto_marca from rep_auto_modelo mod, rep_auto_marca ram\n"
                + "where mod.estado = 0 and ram.id=mod.id_rep_auto_marca \n"
                + "and ( upper(ram.nombre) like upper('%" + busqueda + "%') or upper(mod.nombre) like upper('%" + busqueda + "%') )\n"
                + "order by (ram.nombre,mod.nombre) asc\n"
                + "limit " + limit + " offset " + (pag * limit);
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray arr = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = parseJson(rs);
            obj.put("id_marca", rs.getInt("id_marca"));
            obj.put("nombre_marca", rs.getString("nombre_marca") != null ? rs.getString("nombre_marca") : "");
            obj.put("url_foto_marca", rs.getString("url_foto_marca") != null ? rs.getString("url_foto_marca") : "img/Sin_imagen.png");
            arr.put(obj);
        }
        ps.close();
        rs.close();
        return arr;
    }

    public int getCantidadModelos(int estado, String busqueda) throws SQLException, JSONException {
        String consulta = "select count(mod.id) from rep_auto_modelo mod, rep_auto_marca ram\n"
                + "where mod.estado = 0 and ram.id=mod.id_rep_auto_marca \n"
                + "and ( upper(ram.nombre) like upper('%" + busqueda + "%') or upper(mod.nombre) like upper('%" + busqueda + "%') )";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        int cantidad = 0;
        if (rs.next()) {
            cantidad = rs.getInt("count");
        }
        ps.close();
        rs.close();
        return cantidad;
    }
    private JSONObject parseObj;

    private JSONObject parseJson(ResultSet rs) throws JSONException, SQLException {
        parseObj = new JSONObject();
        parseObj.put("id", rs.getInt("id"));
        parseObj.put("estado", rs.getInt("estado"));
        parseObj.put("id_rep_auto_modelo", rs.getInt("id_rep_auto_modelo"));
        parseObj.put("nombre", rs.getString("nombre") != null ? rs.getString("nombre") : "");
        return parseObj;
    }

    public JSONObject getJson() throws JSONException, SQLException {
        JSONObject obj = new JSONObject();
        obj.put("id", getID());
        obj.put("estado", getESTADO());
        obj.put("id_rep_auto_modelo", getID_REP_AUTO_MODELO());
        obj.put("nombre", getNOMBRE());
        
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

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public int getID_REP_AUTO_MODELO() {
        return ID_REP_AUTO_MODELO;
    }

    public void setID_REP_AUTO_MODELO(int ID_REP_AUTO_MODELO) {
        this.ID_REP_AUTO_MODELO = ID_REP_AUTO_MODELO;
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

}
