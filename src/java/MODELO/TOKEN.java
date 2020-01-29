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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TOKEN {

    private int ID;
    private String TOKEN;
    private int ID_USR;
    private Conexion con = null;

    public TOKEN(Conexion con) {
        this.con = con;
    }

    public int Insertar() {
        try {
            String consulta = "INSERT INTO public.token(\n"
                    + "token, id_usr)\n"
                    + "VALUES (?, ?);";
            PreparedStatement ps = con.statamet(consulta);
            
            ps.setString(1, getTOKEN());
            ps.setInt(2, getID_USR());
            ps.execute();
            
            consulta = "select last_value from token_id_seq ";
            ps = con.statamet(consulta);
            ResultSet rs = ps.executeQuery();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt("last_value");
            }
            rs.close();
            ps.close();
            return id;
        } catch (SQLException ex) {
           
        }
        return 0;
    }

    public JSONArray get_Token_usr(int id) throws SQLException, JSONException {
        String consulta = "select * from token where id_usr="+id+" order by(id) desc";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray arr = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("ID", rs.getInt("id"));
            obj.put("TOKEN", rs.getString("token"));
            obj.put("ID_USR", rs.getInt("id_usr"));
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

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public int getID_USR() {
        return ID_USR;
    }

    public void setID_USR(int ID_USR) {
        this.ID_USR = ID_USR;
    }

    public Conexion getCon() {
        return con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

   

   

}
