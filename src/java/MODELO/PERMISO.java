package MODELO;

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

public class PERMISO {

    private int ID;
    private String NOMBRE;

    private Conexion con = null;

    public PERMISO(Conexion con) {
        this.con = con;
    }

    public int Insertar() throws SQLException {
        String consulta = "INSERT INTO public.permiso(\n"
                + "permiso_nombre)\n"
                + "VALUES (?);";
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, getNOMBRE());
        ps.execute();
        consulta = "select last_value from permiso_id_seq ";
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
    public boolean eliminar() throws SQLException {
        String consulta = "DELETE FROM public.permiso\n" +
                          "WHERE id=?;";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, getID());
        boolean efectuado=ps.execute();
        ps.close();
        return efectuado;
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "select * from permiso order by(permiso_nombre) asc";

        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray arr = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("ID", rs.getInt("id"));
            obj.put("NOMBRE", rs.getString("permiso_nombre"));
            arr.put(obj);
        }
        ps.close();
        rs.close();
        return arr;
    }

    public JSONArray todos_de_rol(int id) throws SQLException, JSONException {
        String consulta = "select id_rol,id_permiso,permiso_nombre from rol_to_permiso rtp, permiso per\n"
                + "where per.id=rtp.id_permiso and rtp.id_rol=" + id+" order by(permiso_nombre) asc";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray arr = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("ID_ROL", rs.getInt("id_rol"));
            obj.put("ID_PERMISO", rs.getInt("id_permiso"));
            obj.put("NOMBRE", rs.getString("permiso_nombre"));
            arr.put(obj);
        }
        ps.close();
        rs.close();
        return arr;
    }

    public JSONArray todos_dis_de_rol(int id) throws SQLException, JSONException {
        String consulta = "select pe.* from permiso pe left join (\n"
                + "select per.id\n"
                + "from permiso per, rol_to_permiso rtp\n"
                + "where per.id=rtp.id_permiso and rtp.id_rol="+id+"\n"
                + ") ani on ani.id=pe.id\n"
                + "where ani.id is null order by(pe.permiso_nombre) asc";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray arr = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("ID", rs.getInt("id"));
            obj.put("NOMBRE", rs.getString("permiso_nombre"));
            arr.put(obj);
        }
        ps.close();
        rs.close();
        return arr;
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

}
