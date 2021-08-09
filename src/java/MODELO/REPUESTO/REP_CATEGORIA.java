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

public class REP_CATEGORIA {

    private int ID;
    private String NOMBRE;
    private Conexion con = null;
    private String TBL = "rep_categoria";

    public REP_CATEGORIA(Conexion con) {
        this.con = con;
    }

    public int Insertar() throws SQLException {
        String consulta = "INSERT INTO public.rep_categoria_rec(\n"
                + "	nombre,estado)\n"
                + "	VALUES (?,0);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setString(1, getNOMBRE());
        ps.execute();
        consulta = "select last_value from rep_categoria_rec_id_seq ";
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

    public JSONArray getAllJSON() throws SQLException, JSONException {
        String consulta = "select array_to_json(array_agg(jsn.*))\n"
                + "from (\n"
                + "select rcr.* ,(\n"
                + "    \n"
                + "    select array_to_json(array_agg(rcr_h.*)) as sub_categorias\n"
                + "    from rep_categoria_rec rcr_h\n"
                + "    where rcr.id = rcr_h.id_padre\n"
                + "    and rcr_h.estado = 0\n"
                + ") sub_categorias\n"
                + "from rep_categoria_rec rcr\n"
                + "where rcr.id_padre is null\n"
                + "and rcr.estado = 0\n"
                + ") jsn";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray arr = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            arr = new JSONArray(rs.getString("array_to_json"));

        }
        ps.close();
        rs.close();
        return arr;
    }

    public String getAllJSONbyIdAuto(int id) throws SQLException, JSONException {
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
                + "select array_to_json(array_agg(resp.*))\n"
                + "from (\n"
                + "select *\n"
                + "from recur rcrf, (\n"
                + "select \n"
                + "    rcr.id,count(rcr.id)\n"
                + "from \n"
                + "    repuesto rep,\n"
                + "    rep_to_rep_auto rtra,\n"
                + "    rep_categoria_rec rcr\n"
                + "where rtra.id_rep_auto = "+id+"\n"
                + "and rep.id = rtra.id_repuesto\n"
                + "and rcr.id = rep.id_rep_categoria\n"
                + "GROUP by (rcr.id)\n"
                + ") rcr\n"
                + "where rcrf.id = rcr.id\n"
                + ") resp";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        
        String resp = "";
        while (rs.next()) {
            resp =rs.getString("array_to_json");
        }
        ps.close();
        rs.close();
        return resp;
    }

    public int eliminar() throws SQLException {
        String consulta = "UPDATE public.rep_categoria_rec \n"
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
                + "where rscac.id_rep_auto_to_rep_version = " + id + " and rsc.id = rscac.id_rep_sub_categoria\n"
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
        parseObj.put("nombre", rs.getString("nombre") != null ? rs.getString("nombre") : "");
        return parseObj;
    }

    public JSONObject getJson() throws JSONException, SQLException {
        JSONObject obj = new JSONObject();
        obj.put("id", getID());
        obj.put("nombre", getNOMBRE());
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
