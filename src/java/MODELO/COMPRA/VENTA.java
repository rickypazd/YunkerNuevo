/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELO.COMPRA;

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
public class VENTA {

    private Conexion con = null;
    private String TBL = "venta";

    public VENTA(Conexion con) {
        this.con = con;
    }

    public int Insertar(String codigo, Date fecha, String nit, String nombreCliente, double total, int id_admin) throws SQLException {
        String consulta = "INSERT INTO public.venta(\n"
                + "	codigo, fecha, nit, nombre_cliente, total, id_administrador, fecha_on)\n"
                + "	VALUES (?, ?, ?, ?, ?, ?, now());";
        PreparedStatement ps = con.statamet(consulta);

        ps.setString(1, codigo);
        ps.setTimestamp(2, new Timestamp(fecha.getTime()));
        ps.setString(3, nit);
        ps.setString(4, nombreCliente);
        ps.setDouble(5, total);
        ps.setInt(6, id_admin);

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

    public int updateTotal(int id, double total) throws SQLException {
        String consulta = "UPDATE public." + TBL + " \n"
                + "	SET total=?\n"
                + "	WHERE id = ?";
        PreparedStatement ps = con.statamet(consulta);
        ps.setDouble(1, total);
        ps.setInt(2, id);
        int row = ps.executeUpdate();
        ps.close();
        return row;
    }

    public String getPaginationJSON(int pag, int limit, String busqueda, String fecha, String fechaFin) throws SQLException, JSONException {

        String consultaCount = "SELECT count(ve.id)"
                + "    FROM venta ve\n"
                + "    WHERE(\n"
                + "          UPPER(ve.codigo) LIKE UPPER('%" + busqueda + "%') OR \n"
                + "          UPPER(ve.nit) LIKE UPPER('%" + busqueda + "%') OR\n"
                + "          UPPER(ve.nombre_cliente) LIKE UPPER('%" + busqueda + "%')\n"
                + "         )\n"
                + "     AND ve.fecha >= '" + fecha + "' \n"
                + "     AND ve.fecha <= '" + fechaFin + "' \n";

        String consultaArray = "select *\n"
                + "		from (\n"
                + "SELECT ve.id, ve.codigo, DATE(ve.fecha) AS fecha ,  ve.nit, ve.nombre_cliente, ve.total, ve.id_administrador, ve.estado, to_json(ganancias.*) as ganancias\n"
                + "    FROM venta ve,(\n"
                + "\n"
                + "            select subq3.id, sum(subq3.ingreso) as ingreso_total,  sum(subq3.ingreso)-sum(subq3.egreso) as ganancias\n"
                + "            from (\n"
                + "\n"
                + "            select subq2.id,\n"
                + "                subq2.ingreso,\n"
                + "                case when \n"
                + "                    subq2.tipo = 5 \n"
                + "                then 0 \n"
                + "                else subq2.egreso \n"
                + "                end as egreso\n"
                + "            from\n"
                + "            (\n"
                + "                select subq.id , sum(subq.tipo) as tipo, subq.id_cardex, sum(subq.total) ingreso, sum(subq.precio_compra) egreso\n"
                + "                from (\n"
                + "                select \n"
                + "                    venta.id,\n"
                + "                    case when \n"
                + "                            ven_detalle.descuento isnull \n"
                + "                        then \n"
                + "                            ven_detalle.precio \n"
                + "                        else \n"
                + "                            (ven_detalle.precio-((ven_detalle.precio*ven_detalle.descuento)/100)) \n"
                + "                        end \n"
                + "                        as total,\n"
                + "                    cardex_movimiento.tipo,\n"
                + "                    cardex_movimiento.id_cardex,\n"
                + "                    compras.precio as precio_compra\n"
                + "                    \n"
                + "                from \n"
                + "                    venta,\n"
                + "                    ven_detalle,\n"
                + "                    cardex_movimiento,\n"
                + "                    (\n"
                + "                        select cd.* , cm.id_cardex\n"
                + "                        from \n"
                + "                        cardex_movimiento cm\n"
                + "                        ,com_detalle cd\n"
                + "                        where \n"
                + "                        cm.tipo = 1\n"
                + "                        and cm.id_ref = cd.id\n"
                + "                    ) compras\n"
                + "                where \n"
                + "                  \n"
                + "                 venta.id = ven_detalle.id_venta \n"
                + "                and cardex_movimiento.id_ref = ven_detalle.id\n"
                + "                and cardex_movimiento.tipo in (2,3)\n"
                + "                and compras.id_cardex = cardex_movimiento.id_cardex\n"
                + "                ) subq\n"
                + "                group by (subq.id, subq.id_cardex)\n"
                + "            ) subq2\n"
                + "            ) subq3\n"
                + "            group by (subq3.id)\n"
                + "    ) ganancias \n"
                + "    WHERE(\n"
                + "          UPPER(ve.codigo) LIKE UPPER('%" + busqueda + "%') OR \n"
                + "          UPPER(ve.nit) LIKE UPPER('%" + busqueda + "%') OR\n"
                + "          UPPER(ve.nombre_cliente) LIKE UPPER('%" + busqueda + "%')\n"
                + "         )\n"
                + "     AND ve.fecha >= '" + fecha + "' \n"
                + "     AND ve.fecha <= '" + fechaFin + "' \n"
                + "     AND ve.id = ganancias.id \n"
                + "    ORDER BY (ve.id, ve.fecha) DESC \n"
                + "limit " + limit + " offset " + (pag * limit) + " \n"
                + ") as list";
        String consulta = "select row_to_json(res) as json\n"
                + "from(\n"
                + "	select (\n"
                + consultaCount
                + "		), (\n"
                + "		select array_to_json(array_agg(list.*))\n"
                + "		from (\n"
                + consultaArray
                + "			) as list\n"
                + "		) as arrayjson\n"
                + "	) as res";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        String resp = "error";
        if (rs.next()) {
            resp = rs.getString("json");

        }
        ps.close();
        rs.close();
        return resp;

    }

    public String getPaginationCuotasJSON(int pag, int limit, String busqueda) throws SQLException, JSONException {

        String consultaCount = "SELECT count(ve.id)"
                + "    FROM venta ve\n"
                + "    WHERE(\n"
                + "          UPPER(ve.codigo) LIKE UPPER('%" + busqueda + "%') OR \n"
                + "          UPPER(ve.nit) LIKE UPPER('%" + busqueda + "%') OR\n"
                + "          UPPER(ve.nombre_cliente) LIKE UPPER('%" + busqueda + "%')\n"
                + "         )\n";

        String consultaArray = "select * from ( select *\n"
                + "		from (\n"
                + "SELECT ve.id, ve.codigo, DATE(ve.fecha) AS fecha ,  ve.nit, ve.nombre_cliente, ve.total, ve.id_administrador, ve.estado, count(vpc.fecha_on) as cuotas_pagadas, \n"
                + "	(select min(sq.fecha) from venta_plan_cuenta sq where sq.id_venta = ve.id and sq.fecha_on is null) as cuota_pendiente,\n"
                + "array_to_json(array_agg(vpc.*)) as cuotas\n"
                + "    FROM venta ve, venta_plan_cuenta vpc \n"
                + "    WHERE(\n"
                + "          UPPER(ve.codigo) LIKE UPPER('%" + busqueda + "%') OR \n"
                + "          UPPER(ve.nit) LIKE UPPER('%" + busqueda + "%') OR\n"
                + "          UPPER(ve.nombre_cliente) LIKE UPPER('%" + busqueda + "%')\n"
                + "         )\n"
                + "     and ve.id = vpc.id_venta\n"
                + "	group by(ve.id, ve.codigo, ve.fecha ,  ve.nit, ve.nombre_cliente, ve.total, ve.id_administrador, ve.estado) ) ve\n"
                + "    ORDER BY  ve.cuota_pendiente asc \n"
                + "limit " + limit + " offset " + (pag * limit) + " \n"
                + ") as list";
        String consulta = "select row_to_json(res) as json\n"
                + "from(\n"
                + "	select (\n"
                + consultaCount
                + "		), (\n"
                + "		select array_to_json(array_agg(list.*))\n"
                + "		from (\n"
                + consultaArray
                + "			) as list\n"
                + "		) as arrayjson\n"
                + "	) as res";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        String resp = "error";
        if (rs.next()) {
            resp = rs.getString("json");

        }
        ps.close();
        rs.close();
        return resp;

    }

    public String gerGananciaTotal(String fecha, String fechaFin) throws SQLException, JSONException {

        String consulta = "            select to_json(final.*) as json\n"
                + "from(\n"
                + "select  SUM(list.ganancia) as ganancia_total,\n"
                + "SUM(list.total_venta) as total_venta,\n"
                + "SUM(list.total_deuda) as total_deuda\n"
                + "		from (\n"
                + "                SELECT ve.id,\n"
                + "                 ve.codigo, \n"
                + "                 DATE(ve.fecha) AS fecha ,\n"
                + "                   ve.nit, ve.nombre_cliente, \n"
                + "                   ve.total,\n"
                + "                    ve.id_administrador, \n"
                + "                    ve.estado, \n"
                + "                    to_json(ganancias.*) as ganancias,\n"
                + "                    ganancias.ganancias as ganancia,\n"
                + "                    ganancias.total_venta as total_venta,\n"
                + "                    (\n" +
"						select TRUNC(SUM(vpc.monto)::numeric,1) \n" +
"						from venta_plan_cuenta vpc\n" +
"						where vpc.id_venta = ve.id\n" +
"						and vpc.fecha_on is null\n" +
"					) as total_deuda\n"
                + "                    FROM venta ve,(\n"
                + "\n"
                + "\n"
                + "                            select subq3.id as id_venta, sum(subq3.ingreso) as total_venta,  sum(subq3.ingreso)-sum(subq3.egreso) as ganancias\n"
                + "                                    from (\n"
                + "\n"
                + "                                    select subq2.id,\n"
                + "                                        subq2.ingreso,\n"
                + "                                        case when \n"
                + "                                            subq2.tipo = 5 \n"
                + "                                        then 0 \n"
                + "                                        else subq2.egreso \n"
                + "                                        end as egreso\n"
                + "                                    from\n"
                + "                                    (\n"
                + "                                        select subq.id , sum(subq.tipo) as tipo, subq.id_cardex, sum(subq.total) ingreso, sum(subq.precio_compra) egreso\n"
                + "                                        from (\n"
                + "                                        select \n"
                + "                                            venta.id,\n"
                + "                                            case when \n"
                + "                                                    ven_detalle.descuento isnull \n"
                + "                                                then \n"
                + "                                                    ven_detalle.precio \n"
                + "                                                else \n"
                + "                                                    (ven_detalle.precio-((ven_detalle.precio*ven_detalle.descuento)/100)) \n"
                + "                                                end \n"
                + "                                                as total,\n"
                + "                                            cardex_movimiento.tipo,\n"
                + "                                            cardex_movimiento.id_cardex,\n"
                + "                                            compras.precio as precio_compra\n"
                + "                                            \n"
                + "                                        from \n"
                + "                                            venta,\n"
                + "                                            ven_detalle,\n"
                + "                                            cardex_movimiento,\n"
                + "                                            (\n"
                + "                                                select cd.* , cm.id_cardex\n"
                + "                                                from \n"
                + "                                                cardex_movimiento cm\n"
                + "                                                ,com_detalle cd\n"
                + "                                                where \n"
                + "                                                cm.tipo = 1\n"
                + "                                                and cm.id_ref = cd.id\n"
                + "                                            ) compras\n"
                + "                                        where \n"
                + "                                        \n"
                + "                                        venta.id = ven_detalle.id_venta \n"
                + "                                        and cardex_movimiento.id_ref = ven_detalle.id\n"
                + "                                        and cardex_movimiento.tipo in (2,3)\n"
                + "                                        and compras.id_cardex = cardex_movimiento.id_cardex\n"
                + "                                        ) subq\n"
                + "                                        group by (subq.id, subq.id_cardex)\n"
                + "                                    ) subq2\n"
                + "                                    ) subq3\n"
                + "                                    group by (subq3.id)\n"
                + "\n"
                + "\n"
                + "\n"
                + "                     ) ganancias \n"
                + "                    WHERE(\n"
                + "                        UPPER(ve.codigo) LIKE UPPER('%%') OR \n"
                + "                        UPPER(ve.nit) LIKE UPPER('%%') OR\n"
                + "                        UPPER(ve.nombre_cliente) LIKE UPPER('%%')\n"
                + "                        )\n"
                + "     AND ve.fecha >= '" + fecha + "' \n"
                + "     AND ve.fecha <= '" + fechaFin + "' \n"
                + "                    AND ve.id = ganancias.id_venta \n"
                + "                    ORDER BY (ve.id , ve.fecha)  DESC \n"
                + "                \n"
                + "		    ) as list\n"
                + ") final";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        String resp = "error";
        if (rs.next()) {
            resp = rs.getString("json");

        }
        ps.close();
        rs.close();
        return resp;

    }

    public String getById(int id) throws SQLException, JSONException {

        String consulta = "select row_to_json(res) as json\n"
                + "from(\n"
                + "select ve.*, (\n"
                + "    select array_to_json(array_agg(ved.*))\n"
                + "    from (\n"
                + "                    select ved.*, subq1.devo as devoluciones\n"
                + "                from ven_detalle ved,(\n"
                + "\n"
                + "            select vendet.id, array_to_json(array_agg(devo.*)) as devo\n"
                + "            from (\n"
                + "                select ved.*\n"
                + "                from ven_detalle ved,\n"
                + "                    cardex_movimiento cm\n"
                + "                where ved.id_venta = " + id + "\n"
                + "                and ved.id = cm.id_ref\n"
                + "                and cm.tipo in (3)\n"
                + "                group by ved.id\n"
                + "            ) vendet left join (\n"
                + "\n"
                + "                select ved.*\n"
                + "                from ven_detalle ved,\n"
                + "                    cardex_movimiento cm\n"
                + "                where ved.id_venta = " + id + "\n"
                + "                and ved.id = cm.id_ref\n"
                + "                and cm.tipo in (2)\n"
                + "                group by ved.id\n"
                + "            ) devo\n"
                + "            on \n"
                + "                vendet.id_articulo = devo.id_articulo\n"
                + "                group by\n"
                + "                vendet.id\n"
                + "                ) subq1\n"
                + "\n"
                + "                where ved.id = subq1.id\n"
                + "    ) ved\n"
                + ") as detalle\n"
                + ", (\n"
                + "    select array_to_json(array_agg(usr.*))\n"
                + "    from usuario usr\n"
                + "    where usr.id = ve.id_administrador\n"
                + ") as administrador,\n"
                + "(\n"
                + "	    select array_to_json(array_agg(vpc.*))\n"
                + "    from (select * from venta_plan_cuenta vpc\n"
                + "    where vpc.id_venta = ve.id\n"
                + "   order by (vpc.numero) asc ) vpc\n"
                + ") as cuotas\n"
                + "from venta ve\n"
                + "where ve.id = " + id + "\n"
                + "	) as res";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        String resp = "error";
        if (rs.next()) {
            resp = rs.getString("json");

        }
        ps.close();
        rs.close();
        return resp;

    }

}
