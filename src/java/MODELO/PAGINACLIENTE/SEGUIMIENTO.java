/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELO.PAGINACLIENTE;

import Conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author ricardopazdemiquel
 */
public class SEGUIMIENTO {

    private Conexion con = null;

    public SEGUIMIENTO(Conexion con) {
        this.con = con;
    }

    public int Insertar(String arr) throws SQLException {
        
        //tipos
        // 0= rep_categoria
        // 1= rep_sub_categoria
        // 2= rep_auto_marca
        // 3= rep_auto_modelo
        // 4= precio
        // 5= repuesto
        String consulta = "INSERT INTO public.seguimiento_busqueda(id_relacion, tipo, busqueda, fecha, id_usuario)\n"
                + "SELECT  (value::JSON->'id_relacion')::text::INT AS id_relacion, (value::JSON->'tipo')::text::INT AS tipo, (value::JSON->'busqueda')::VARCHAR AS busqueda, now() AS fecha,(value::JSON->'id_usuario')::text::INT AS id_usuario  FROM (SELECT VALUE FROM json_array_elements('"+arr+"')) d";
        PreparedStatement ps = con.statamet(consulta);

        ps.execute();
        ps.close();
        return 0;
    }
}
