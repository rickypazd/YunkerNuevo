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

public class ROL_TO_PERMISO {

    private int ID_ROL;
    private int ID_PERMISO;

    private Conexion con = null;

    public ROL_TO_PERMISO(Conexion con) {
        this.con = con;
    }

    public void Insertar() throws SQLException {
        String consulta = "INSERT INTO public.rol_to_permiso(\n"
                + "id_rol, id_permiso)\n"
                + "VALUES (?, ?);";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, getID_ROL());
        ps.setInt(2, getID_PERMISO());
        ps.execute();
        ps.close();
    }

    public void eliminar() throws SQLException {
        String consulta = "DELETE FROM public.rol_to_permiso\n"
                + "WHERE id_rol=? and id_permiso=?;";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, getID_ROL());
        ps.setInt(2, getID_PERMISO());
        ps.execute();
        ps.close();
    }

    public JSONArray todos_vehiculos_activos_ultima_posicion() throws SQLException, JSONException {
        String consulta = "select * from vehiculo v,(select pv.id as id_posicion, pv.lat, pv.lon, pv.fecha, pv.id_vehiculo from posicion_vehiculo pv where pv.id_vehiculo=1 order by(fecha) DESC LIMIT 1)vp\n"
                + "where v.id=vp.id_vehiculo";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray arr = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id_vehiculo", rs.getInt("id"));
            obj.put("placa", rs.getString("placa"));
            obj.put("modelo", rs.getString("modelo"));
            obj.put("ano", rs.getString("ano"));
            obj.put("color", rs.getString("color"));
            obj.put("marca", rs.getString("marca"));
            obj.put("lat", rs.getDouble("lat"));
            obj.put("lon", rs.getDouble("lon"));
            obj.put("fecha", rs.getTimestamp("fecha"));
            arr.put(obj);
        }
        ps.close();
        rs.close();
        return arr;
    }

    public int getID_ROL() {
        return ID_ROL;
    }

    public void setID_ROL(int ID_ROL) {
        this.ID_ROL = ID_ROL;
    }

    public int getID_PERMISO() {
        return ID_PERMISO;
    }

    public void setID_PERMISO(int ID_PERMISO) {
        this.ID_PERMISO = ID_PERMISO;
    }

    public Conexion getCon() {
        return con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

}
