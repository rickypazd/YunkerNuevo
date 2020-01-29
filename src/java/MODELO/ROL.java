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

public class ROL {

    private int ID;
    private String NOMBRE;

    private Conexion con = null;

    public ROL(Conexion con) {
        this.con = con;
    }

    public int Insertar() throws SQLException {
        String consulta = "INSERT INTO public.rol(\n"
                + "nombre)\n"
                + "VALUES (?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setString(1, getNOMBRE());
        ps.execute();

        consulta = "select last_value from rol_id_seq ";
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

    public JSONArray get_roles() throws SQLException, JSONException {
        String consulta = "select * from rol order by(nombre) asc";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray arr = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("ID", rs.getInt("id"));
            obj.put("NOMBRE", rs.getString("nombre"));
           
            arr.put(obj);
        }
        ps.close();
        rs.close();
        return arr;
    }
    public JSONObject get_rol_por_nombre(String nombre) throws SQLException, JSONException {
        String consulta = "select * from rol where nombre=? order by(nombre) asc";
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, nombre);
        ResultSet rs = ps.executeQuery();
        JSONObject obj= new JSONObject();
        if (rs.next()) {
            obj = new JSONObject();
            obj.put("ID", rs.getInt("id"));
            obj.put("NOMBRE", rs.getString("nombre"));
            obj.put("exito", "si");
        }else{
            obj.put("exito","no");
        }
        ps.close();
        rs.close();
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

   

}
