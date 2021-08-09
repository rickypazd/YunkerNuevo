package MODELO.COMPRA;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import MODELO.COMPRA.*;
import MODELO.ALMACEN.*;
import Conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONException;

/**
 *
 * @author ricardopazdemiquel
 */
public class VENTA_PLAN_CUENTA {

    private Conexion con = null;
    private String TBL = "venta_plan_cuenta";

    public VENTA_PLAN_CUENTA(Conexion con) {
        this.con = con;
    }

    public int Insertar(int numero, String fecha, double monto, int id_venta) throws SQLException, ParseException {
        String consulta = "INSERT INTO public.venta_plan_cuenta(\n"
                + "	 numero, fecha, monto, id_venta)\n"
                + "	VALUES (?, ?, ?, ?);";
        PreparedStatement ps = con.statamet(consulta);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date parsed = format.parse(fecha);
        ps.setInt(1, numero);
        ps.setDate(2, new java.sql.Date(parsed.getTime()));
        ps.setDouble(3, monto);
        ps.setInt(4, id_venta);

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

    public int eliminar(int id) throws SQLException {
        String consulta = "UPDATE public." + TBL + " \n"
                + "	SET estado=?\n"
                + "	WHERE id = ?";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, 1);
        ps.setInt(2, id);
        int row = ps.executeUpdate();
        ps.close();
        return row;
    }

}
