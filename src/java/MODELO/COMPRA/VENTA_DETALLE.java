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
import org.json.JSONException;

/**
 *
 * @author ricardopazdemiquel
 */
public class VENTA_DETALLE {

    private Conexion con = null;
    private String TBL = "ven_detalle";

    public VENTA_DETALLE(Conexion con) {
        this.con = con;
    }

    public int Insertar(int id_venta, String nombre, String descripcion, int cantidad,double precio,double sub_total,int tipo_articulo,int id_articulo,int descuento) throws SQLException {
        String consulta = "INSERT INTO public.ven_detalle(\n"
                + "      id_venta, nombre, descripcion, cantidad, precio, sub_total, tipo_articulo, id_articulo, descuento)\n"
                + "	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setInt(1, id_venta);
        ps.setString(2, nombre);
        ps.setString(3, descripcion);
        ps.setInt(4, cantidad);
        ps.setDouble(5, precio);
        ps.setDouble(6, sub_total);
        ps.setInt(7, tipo_articulo);
        ps.setInt(8, id_articulo);
        ps.setInt(9, descuento);
        

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
   public int ajustarPlanDePagos(int id_venta, double subTotal) throws SQLException {
        String consulta = "update venta_plan_cuenta vp\n"
                + "set monto = vpc.monto\n"
                + "from (\n"
                + "select vpc.id, vpc.monto-(\n"
                + "select "+subTotal+"/(\n"
                + "select count(*)\n"
                + "from venta_plan_cuenta vpc\n"
                + "where vpc.id_venta = "+id_venta+"\n"
                + "	) \n"
                + ") as monto\n"
                + "from venta_plan_cuenta vpc\n"
                + "where vpc.id_venta = "+id_venta+"\n"
                + ") vpc\n"
                + "where vp.id = vpc.id";
        PreparedStatement ps = con.statamet(consulta);
        int row = ps.executeUpdate();
        ps.close();
        return row;
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
