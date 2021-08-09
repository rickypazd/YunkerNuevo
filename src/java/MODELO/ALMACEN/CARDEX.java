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

    public int InsertarRepuesto(int id_repuesto) throws SQLException {
        String consulta = "INSERT INTO public.cardex(\n"
                + "     id_repuesto )\n"
                + "	VALUES (?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setInt(1, id_repuesto);
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

    public int InsertarMovimiento(int id_cardex, int tipo, int id_almacen, int id_ref) throws SQLException {

        //TIPO
        // 1 = INGRESO COMPRA
        // 2 = INGRESO DEVOLUCION
        // 3 = SALIDA VENTA
        String consulta = "INSERT INTO public.cardex_movimiento(\n"
                + "     id_cardex, tipo, fecha, id_almacen, id_ref)\n"
                + "	VALUES (?, ?, ?, ?, ?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setInt(1, id_cardex);
        ps.setInt(2, tipo);
        ps.setTimestamp(3, new Timestamp(new Date().getTime()));
        ps.setInt(4, id_almacen);
        ps.setInt(5, id_ref);

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

    public int VenderRepuesto(int id_com_detalle, int cantidad, int id_repuesto, int id_usuario) throws SQLException {
        String consulta = "INSERT INTO cardex_movimiento (id_cardex, tipo, fecha, id_ref, id_usuario) (\n"
                + "select \n"
                + "    tabla.id as id_cardex,\n"
                + "    '3' as tipo, \n"
                + "    now() as fecha,\n"
                + "    '" + id_com_detalle + "' as id_ref,\n"
                + "    '" + id_usuario + "' as id_usuario\n"
                + "from \n"
                + "    repuesto repcount,(\n"
                + "                    \n"
                + "        SELECT\n"
                + "            ca.*,\n"
                + "            (\n"
                + "                SELECT\n"
                + "                    cam.tipo\n"
                + "                FROM\n"
                + "                    cardex_movimiento cam\n"
                + "                WHERE\n"
                + "                    cam.id_cardex = ca.id\n"
                + "                ORDER BY\n"
                + "                    cam.fecha DESC\n"
                + "                LIMIT 1) AS tipo\n"
                + "        FROM\n"
                + "            cardex ca\n"
                + "        WHERE\n"
                + "            ca.id_repuesto = ?\n"
                + "                ) tabla\n"
                + "where \n"
                + "    repcount.id = tabla.id_repuesto\n"
                + "and tabla.tipo IN (1,2)\n"
                + "Limit " + cantidad + "\n"
                + ")";
        PreparedStatement ps = con.statamet(consulta);

        ps.setInt(1, id_repuesto);

        int row = ps.executeUpdate();
        ps.close();
        return row;
    }

    public int devolverRepuesto(int id_vent_detalle, int id_usuario, int cantidad, int id_ref) throws SQLException {
        String consulta = "INSERT INTO cardex_movimiento (id_usuario,id_cardex, id_almacen, tipo, fecha, id_ref , id_dev_ref) (\n"
                + "    select \n"
                + "                    " + id_usuario + " as id_usuario,\n"
                + "                    car.id as id_cardex,\n"
                + "                    cam_compra.id_almacen as id_almacen,\n"
                + "                    2 as tipo, \n"
                + "                    now() as fecha,\n"
                + "                    " + id_ref + " as id_ref,\n"
                + "                    " + id_vent_detalle + " as id_dev_ref\n"
                + "                from cardex car,\n"
                + "                (\n"
                + "                    \n"
                + "                    select *\n"
                + "                    from (\n"
                + "                        SELECT\n"
                + "                            cam.*\n"
                + "                        FROM\n"
                + "                            cardex_movimiento cam left join\n"
                + "                            (select * from cardex_movimiento cam2 where cam2.tipo in (2) and cam2.id_dev_ref = " + id_vent_detalle + ") cam2\n"
                + "                            on cam.id_cardex = cam2.id_cardex\n"
                + "                          where  cam.id_ref = " + id_vent_detalle + "\n"
                + "                        and cam.tipo in (3)\n"
                + "                        and cam2.id is null\n"
                + "                    ) mi \n"
                + "                ) cam,\n"
                + "                (\n"
                + "                        SELECT\n"
                + "                            cam.*\n"
                + "                        FROM\n"
                + "                            cardex_movimiento cam\n"
                + "                        WHERE cam.tipo in (1)\n"
                + "                        and   cam.id_ref <> " + id_vent_detalle + "\n"
                + "                ) cam_compra\n"
                + "                where cam.id_cardex =car.id\n"
                + "                and cam_compra.id_cardex = car.id\n"
                + "                limit " + cantidad + "\n"
                + ")";
        PreparedStatement ps = con.statamet(consulta);
        int row = ps.executeUpdate();
        ps.close();
        return row;
    }

 
    public int VenderRepuesto2(int id_venta, int cantidad, int id_repuesto) throws SQLException {
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
