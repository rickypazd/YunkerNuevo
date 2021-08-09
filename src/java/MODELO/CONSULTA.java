/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELO;



import Conexion.Conexion2;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ricardopazdemiquel
 */
public class CONSULTA {

    public static String insertarArray(JSONObject obj) {

        try {
            if (obj == null) { //validar obj JSON
                return "El objeto es nulo.";
            }

            if (!obj.has("nombre_tabla")) {
                return "No se encontro nombre_tabla";
            }
            String nombre_tabla = obj.getString("nombre_tabla");
            if (!obj.has("data")) {
                return "No se encontro data";
            }
            JSONArray arr = obj.getJSONArray("data");

            String consulta = "SELECT insert_json_arr(?,'" + arr.toString() + "');";
            PreparedStatement ps = Conexion2.getConeccion().statamet(consulta);
            ps.setString(1, nombre_tabla);
            ResultSet rs = ps.executeQuery();
            String respuesta = "";
            if (rs.next()) {
                respuesta = rs.getString("insert_json_arr");
            }
            rs.close();
            ps.close();
            return respuesta;
        } catch (JSONException ex) {
            Logger.getLogger(CONSULTA.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        } catch (SQLException ex) {
            Logger.getLogger(CONSULTA.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }

    }

    public static String insertOrSelect(String NombreTabla, JSONArray data, String[] keys) {

        Conexion2.getConeccion().transacction();
        try {

            if (NombreTabla.length() <= 0) {
                return "No se encontro nombre_tabla";
            }

            if (data == null) {
                return "No se encontro data";
            }
            if (data == null) {
                return "No se encontro keys";
            }

            
            // ?1= nombre_tabla, ?2= arrData([{".*":".*"}]), ?3= keys compare({"id","nombre"})
            String consulta = "SELECT insert_or_select(?,?,?);";
            
            PreparedStatement ps = Conexion2.getConeccion().statamet(consulta);
            ps.setString(1, NombreTabla);
            ps.setString(2, data.toString());
            ps.setArray(3, Conexion2.getConeccion().createArray(keys));
           
           
            ResultSet rs = ps.executeQuery();
            String respuesta = "";
            if (rs.next()) {
                respuesta = rs.getString("insert_or_select");
            }
            rs.close();
            ps.close();
            
            Conexion2.getConeccion().commit();
            Conexion2.getConeccion().transacctionEnd();
            return respuesta;
        } catch (SQLException ex) {
            Conexion2.getConeccion().rollback();
            Conexion2.getConeccion().transacctionEnd();
            Logger.getLogger(CONSULTA.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }

    }

    public static String insertarRep_to_auto(int id_repuesto, int id_auto) {

        try {
            String consulta = "INSERT INTO rep_to_rep_auto(\n"
                    + "	id_repuesto, id_rep_auto)\n"
                    + "	VALUES (?, ?);";
            PreparedStatement ps = Conexion2.getConeccion().statamet(consulta);

            ps.setInt(1, id_repuesto);
            ps.setInt(2, id_auto);

            ps.execute();
            ps.close();
            return "Rep to Auto";
        } catch (SQLException ex) {
//            Logger.getLogger(CONSULTA.class.getName()).log(Level.SEVERE, null, ex);
            return "Error insert rep to auto";
        }

    }

    public static String updateArray(JSONObject obj) {

        try {
            if (obj == null) { //validar obj JSON
                return "El objeto es nulo.";
            }

            if (!obj.has("nombre_tabla")) {
                return "No se encontro nombre_tabla";
            }
            String nombre_tabla = obj.getString("nombre_tabla");
            if (!obj.has("data")) {
                return "No se encontro data";
            }
            JSONArray arr = obj.getJSONArray("data");

            String consulta = "SELECT updatep1(?,'" + arr.toString() + "');";
            PreparedStatement ps = Conexion2.getConeccion().statamet(consulta);
            ps.setString(1, nombre_tabla);
            ResultSet rs = ps.executeQuery();
            String respuesta = "Null";
            if (rs.next()) {
                respuesta = rs.getString("updatep1");
            }
            rs.close();
            ps.close();
            return respuesta;
        } catch (JSONException ex) {
            Logger.getLogger(CONSULTA.class.getName()).log(Level.SEVERE, null, ex);
            return "Error";
        } catch (SQLException ex) {
            Logger.getLogger(CONSULTA.class.getName()).log(Level.SEVERE, null, ex);
            return "Error";
        }

    }

    public static String selectAll(JSONObject obj) {

        try {
            if (obj == null) { //validar obj JSON
                return "El objeto es nulo.";
            }

            if (!obj.has("nombre_tabla")) {
                return "No se encontro nombre_tabla";
            }
            String nombre_tabla = obj.getString("nombre_tabla");

            String consulta = "SELECT * \n"
                    + "FROM " + nombre_tabla + " \n"
                    + "order by (id) asc";

            String consultaJson = "SELECT array_to_json(array_agg(qry.*)) AS json\n"
                    + "FROM (\n"
                    + consulta + "\n"
                    + ") qry";

            PreparedStatement ps = Conexion2.getConeccion().statamet(consultaJson);

            ResultSet rs = ps.executeQuery();
            String respuesta = "Null";
            if (rs.next()) {
                respuesta = rs.getString("json");
            }
            rs.close();
            ps.close();
            return respuesta;
        } catch (JSONException ex) {
            Logger.getLogger(CONSULTA.class.getName()).log(Level.SEVERE, null, ex);
            return "Error";
        } catch (SQLException ex) {
            Logger.getLogger(CONSULTA.class.getName()).log(Level.SEVERE, null, ex);
            return "Error";
        }

    }

    public static String getByWheres(JSONObject obj) {

        try {
            if (obj == null) { //validar obj JSON
                return "El objeto es nulo.";
            }

            if (!obj.has("nombre_tabla")) {
                return "No se encontro nombre_tabla";
            }
            String nombre_tabla = obj.getString("nombre_tabla");
            if (!obj.has("wheres")) {
                return "No se encontro wheres";
            }
            JSONArray arr = obj.getJSONArray("wheres");

            String consulta = "SELECT array_to_json(array_agg(qry.*)) AS json\n"
                    + "FROM (\n";
            consulta += "SELECT * ";
            consulta += "FROM " + nombre_tabla;
            consulta += " WHERE ";
            JSONObject temp;
            for (int i = 0; i < arr.length(); i++) {
                temp = arr.getJSONObject(i);
                if (i != 0) {
                    consulta += " AND ";
                }
                consulta += temp.getString("key") + " " + temp.getString("ope") + " " + temp.getString("val");

            }

            consulta += ") qry";
            PreparedStatement ps = Conexion2.getConeccion().statamet(consulta);

            ResultSet rs = ps.executeQuery();
            String respuesta = "";
            if (rs.next()) {
                respuesta = rs.getString("json");
            }
            rs.close();
            ps.close();
            return respuesta;
        } catch (JSONException ex) {
            Logger.getLogger(CONSULTA.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        } catch (SQLException ex) {
            Logger.getLogger(CONSULTA.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }

    }

    public static String getBySelectWheres(JSONObject obj) {

        try {
            if (obj == null) { //validar obj JSON
                return "El objeto es nulo.";
            }

            if (!obj.has("nombre_tabla")) {
                return "No se encontro nombre_tabla";
            }
            String nombre_tabla = obj.getString("nombre_tabla");

            if (!obj.has("wheres")) {
                return "No se encontro wheres";
            }

            if (!obj.has("select")) {
                return "No se encontro wheres";
            }

            JSONArray arr = obj.getJSONArray("wheres");

            String consulta = "SELECT array_to_json(array_agg(tp.*)) AS json\n"
                    + "FROM (\n";
            consulta += "SELECT * ";

            JSONArray arrSelect = obj.getJSONArray("select");
            for (int i = 0; i < arrSelect.length(); i++) {
//                if (!obj.has("wheres")) {
//                    return "No se encontro wheres";
//                }
                JSONObject objSelect = arrSelect.getJSONObject(i);
                String myLlave = objSelect.getString("myKey");
                String llave = objSelect.getString("key");
                String nombreTablaHijo = objSelect.getString("nombre_tabla");
                String consultaSelect = "( select array_to_json(array_agg(tph.*)) "
                        + "FROM (SELECT * ";
                consultaSelect += "FROM " + nombreTablaHijo + " t1 ";
                consultaSelect += " WHERE tpf." + myLlave + " = t1." + llave + " ";
                if (objSelect.has("wheres")) {
                    JSONArray selcWheres = objSelect.getJSONArray("wheres");

                    JSONObject temp;
                    for (int j = 0; j < selcWheres.length(); j++) {
                        temp = selcWheres.getJSONObject(j);

                        consultaSelect += " AND ";

                        consultaSelect += temp.getString("key") + " " + temp.getString("ope") + " " + temp.getString("val");

                    }
                }
                consultaSelect += "order by 2 desc) tph ) as " + nombreTablaHijo + " ";
                consulta += ", " + consultaSelect;
            }

            consulta += "FROM " + nombre_tabla + " tpf ";
            consulta += " WHERE ";
            JSONObject temp;
            for (int i = 0; i < arr.length(); i++) {
                temp = arr.getJSONObject(i);
                if (i != 0) {
                    consulta += " AND ";
                }
                consulta += temp.getString("key") + " " + temp.getString("ope") + " " + temp.getString("val");

            }

            consulta += ") tp";
            PreparedStatement ps = Conexion2.getConeccion().statamet(consulta);

            ResultSet rs = ps.executeQuery();
            String respuesta = "Null";
            if (rs.next()) {
                respuesta = rs.getString("json");
            }
            rs.close();
            ps.close();
            return respuesta;
        } catch (JSONException ex) {
            Logger.getLogger(CONSULTA.class.getName()).log(Level.SEVERE, null, ex);
            return "Error";
        } catch (SQLException ex) {
            Logger.getLogger(CONSULTA.class.getName()).log(Level.SEVERE, null, ex);
            return "Error";
        }

    }

    public static String getDispositivo(String API_KEY) {

        try {

            String consulta = "select array_to_json(array_agg(qf.*)) as json\n"
                    + "from (\n"
                    + "select tbd.*, tbs.id as id_session, tbs.id_usuario\n"
                    + "from tbl_session tbs, tbl_dispositivo tbd\n"
                    + "where tbs.key = '" + API_KEY + "'\n"
                    + "and tbs.id_dispositivo = tbd.id \n"
                    + ") qf";
            PreparedStatement ps = Conexion2.getConeccion().statamet(consulta);

            ResultSet rs = ps.executeQuery();
            String respuesta = "";
            if (rs.next()) {
                respuesta = rs.getString("json");
            }
            rs.close();
            ps.close();
            return respuesta;
        } catch (SQLException ex) {
            Logger.getLogger(CONSULTA.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }

    }

    public static String getMensajes(int id) {

        try {

            String consulta = "select array_to_json(array_agg(qf.*)) as json\n"
                    + "from (\n"
                    + "select *\n"
                    + "from rep_auto tbm\n"
                    + "where (tbm.id_emisor = " + id + " ) \n"
                    + "or (tbm.id_receptor = " + id + " )  order by (tbm.id) desc \n"
                    + ") qf";
            PreparedStatement ps = Conexion2.getConeccion().statamet(consulta);

            ResultSet rs = ps.executeQuery();
            String respuesta = "";
            if (rs.next()) {
                respuesta = rs.getString("json");
            }
            rs.close();
            ps.close();
            return respuesta;
        } catch (SQLException ex) {
            Logger.getLogger(CONSULTA.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }

    }

    public static JSONArray get_rep_auto(String clave) {

        try {

            String consulta = "SELECT \n"
                    + "    array_to_json(array_agg(obfin.*)) AS json\n"
                    + "FROM \n"
                    + "    (\n"
                    + "        SELECT \n"
                    + "            to_json(ra.*) AS rep_auto,\n"
                    + "            to_json(rav.*) AS rep_auto_version, \n"
                    + "            to_json(ramo.*) AS rep_auto_modelo, \n"
                    + "            to_json(rama.*) AS rep_auto_marca\n"
                    + "        FROM\n"
                    + "            rep_auto ra,\n"
                    + "            rep_auto_version rav,\n"
                    + "            rep_auto_modelo ramo,\n"
                    + "            rep_auto_marca rama\n"
                    + "        WHERE\n"
                    + "            ra.clave = '" + clave + "'\n"
                    + "        AND ra.estado = 0\n"
                    + "        AND ra.id_version = rav.id\n"
                    + "        AND rav.id_rep_auto_modelo = ramo.id\n"
                    + "        AND ramo.id_rep_auto_marca = rama.id\n"
                    + "    ) obfin";
            PreparedStatement ps = Conexion2.getConeccion().statamet(consulta);

            ResultSet rs = ps.executeQuery();
            JSONArray arrResp = new JSONArray();

            String respuesta = "";
            if (rs.next()) {
                respuesta = rs.getString("json");
                if (respuesta != null) {
                    arrResp = new JSONArray(respuesta);
                }
            }
            rs.close();
            ps.close();
            return arrResp;
        } catch (JSONException ex) {
            Logger.getLogger(CONSULTA.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(CONSULTA.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }
}
