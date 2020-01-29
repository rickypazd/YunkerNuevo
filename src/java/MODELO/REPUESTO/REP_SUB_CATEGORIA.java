package MODELO.REPUESTO;


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

public class REP_SUB_CATEGORIA {

    private int ID;
    private int ESTADO;
    
    private String NOMBRE;
    private int ID_REP_CATEGORIA;
    private Conexion con = null;
    private String TBL = "rep_sub_categoria";

    public REP_SUB_CATEGORIA(Conexion con) {
        this.con = con;
    }

    public int Insertar() throws SQLException {
        String consulta = "INSERT INTO public." + TBL + "(\n"
                + "	nombre, id_rep_categoria)\n"
                + "	VALUES (?, ?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setString(1, getNOMBRE());
        ps.setInt(2, getID_REP_CATEGORIA());
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

    public JSONObject getById(int id) throws SQLException, JSONException {
        String consulta = "select ar.* "
                + "from " + TBL + " ar\n"
                + "where ar.id=?";
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

    public JSONArray getBy_id_rep_categoria(int id) throws SQLException, JSONException {
        String consulta = "select ar.* "
                + "from " + TBL + " ar where ar.estado = 0 and ar.id_rep_categoria = "+id;
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
    public JSONArray getBy_id_rep_categoria_de_id_vehiculo(int id, int id_vehiculo) throws SQLException, JSONException {
        String consulta = "SELECT rsc.*, rscac.id as id_rep_sub_categoria_activa FROM public.rep_sub_categoria_activa rscac, rep_sub_categoria rsc\n" +
"                where rscac.id_rep_auto_to_rep_version = "+id_vehiculo+" and rsc.id = rscac.id_rep_sub_categoria\n" +
"                and rsc.id_rep_categoria ="+id;
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray arr = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = parseJson(rs);
            obj.put("id_rep_sub_categoria_activa",rs.getInt("id_rep_sub_categoria_activa"));
            arr.put(obj);
        }
        ps.close();
        rs.close();
        return arr;
    }

    private JSONObject parseObj;

    private JSONObject parseJson(ResultSet rs) throws JSONException, SQLException {
        parseObj = new JSONObject();
        parseObj.put("id", rs.getInt("id"));
        parseObj.put("estado", rs.getInt("estado"));
        parseObj.put("nombre", rs.getString("nombre") != null ? rs.getString("nombre") : "");
        parseObj.put("id_rep_categoria", rs.getInt("id_rep_categoria"));
        return parseObj;
    }

    public JSONObject getJson() throws JSONException, SQLException {
        JSONObject obj = new JSONObject();
        obj.put("id", getID());
        obj.put("id", getESTADO());
        obj.put("nombre", getNOMBRE());
        obj.put("id_rep_categoria", getID_REP_CATEGORIA());
        
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

    public int getID_REP_CATEGORIA() {
        return ID_REP_CATEGORIA;
    }

    public void setID_REP_CATEGORIA(int ID_REP_CATEGORIA) {
        this.ID_REP_CATEGORIA = ID_REP_CATEGORIA;
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
