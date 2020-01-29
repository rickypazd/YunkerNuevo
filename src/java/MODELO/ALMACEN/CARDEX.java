/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELO.ALMACEN;

import MODELO.COMPRA.*;
import MODELO.ALMACEN.*;
import Conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import org.json.JSONException;

/**
 *
 * @author ricardopazdemiquel
 */
public class CARDEX {

    private Conexion con = null;
    private String TBL = "cardex";

    public CARDEX(Conexion con) {
        this.con = con;
    }

    public int InsertarRepuesto(int id_repuesto, Date fecha, int tipo, int id_almacen, int id_com_detalle) throws SQLException {
        String consulta = "INSERT INTO public.cardex(\n"
                + "     id_repuesto, tipo, fecha_ingreso, id_almacen, id_com_detalle)\n"
                + "	VALUES (?, ?, ?, ?, ?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setInt(1, id_repuesto);
        ps.setInt(2, tipo);
        ps.setTimestamp(3, new Timestamp(fecha.getTime()));

        ps.setInt(4, id_almacen);
        ps.setInt(5, id_com_detalle);

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

    public int VenderRepuesto(int id_venta, int cantidad, int id_repuesto) throws SQLException {
        String consulta = "UPDATE cardex \n"
                + "SET estado = 1, fecha_salida = ?, id_venta = ?\n"
                + "FROM (\n"
                + "    SELECT * FROM cardex car\n"
                + "    WHERE car.id_repuesto = ?\n"
                + "    AND car.estado = 0\n"
                + "    ORDER BY(car.fecha_ingreso) ASC\n"
                + "    LIMIT ?\n"
                + "    ) AS list\n"
                + "WHERE cardex.id = list.id";
        PreparedStatement ps = con.statamet(consulta);
        ps.setTimestamp(1, new Timestamp(new Date().getTime()));
        ps.setInt(2, id_venta);
        ps.setInt(3, id_repuesto);
        ps.setInt(4, cantidad);
        int row = ps.executeUpdate();
        ps.close();
        return row;
    }

    public int eliminar(int id) throws SQLException {
        String consulta = "UPDATE public." + TBL + " \n"
                + "	SET estado=?\n"
                + "	WHERE id = ?";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, 2);
        ps.setInt(2, id);
        int row = ps.executeUpdate();
        ps.close();
        return row;
    }

}
