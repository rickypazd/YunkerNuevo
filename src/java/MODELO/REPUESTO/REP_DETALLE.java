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

public class REP_DETALLE {

    private int ID;
    private int ID_REPUESTO;
    private String DETALLE;
    private String VALOR;
    private Conexion con = null;
    private String TBL = "rep_detalle";

    public REP_DETALLE(Conexion con) {
        this.con = con;
    }

    public int Insertar() throws SQLException {
        String consulta = "INSERT INTO public." + TBL + "(\n"
                + "	id_repuesto, detalle, valor)\n"
                + "	VALUES (?, ?, ?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setInt(1, getID_REPUESTO());
        ps.setString(2, getDETALLE());
        ps.setString(3, getVALOR());
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

    public JSONArray getAll_by_id_vehiculo(int id) throws SQLException, JSONException {
        String consulta = "SELECT rc.*, count(rsc.id) FROM public.rep_sub_categoria_activa rscac, rep_sub_categoria rsc, rep_categoria rc\n"
                + "where rscac.id_rep_auto_to_rep_version = "+id+" and rsc.id = rscac.id_rep_sub_categoria\n"
                + "and rc.id = rsc.id_rep_categoria\n"
                + "group by (rc.id)";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray arr = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = parseJson(rs);
              parseObj.put("count", rs.getInt("count"));
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
        parseObj.put("id_repuesto", rs.getInt("id_repuesto"));
        parseObj.put("detalle", rs.getString("detalle") != null ? rs.getString("detalle") : "");
        parseObj.put("valor", rs.getString("valor") != null ? rs.getString("valor") : "");
        return parseObj;
    }

    public JSONObject getJson() throws JSONException, SQLException {
        JSONObject obj = new JSONObject();
        obj.put("id", getID());
        obj.put("id_repuesto", getID_REPUESTO());
        obj.put("detalle", getDETALLE());
        obj.put("valor", getVALOR());
        return obj;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID_REPUESTO() {
        return ID_REPUESTO;
    }

    public void setID_REPUESTO(int ID_REPUESTO) {
        this.ID_REPUESTO = ID_REPUESTO;
    }

    public String getDETALLE() {
        return DETALLE;
    }

    public void setDETALLE(String DETALLE) {
        this.DETALLE = DETALLE;
    }

    public String getVALOR() {
        return VALOR;
    }

    public void setVALOR(String VALOR) {
        this.VALOR = VALOR;
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

}
