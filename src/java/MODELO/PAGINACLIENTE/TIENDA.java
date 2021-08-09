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
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ricardopazdemiquel
 */
public class TIENDA {

    private Conexion con = null;

    public TIENDA(Conexion con) {
        this.con = con;
    }

    public String getCategoriasAndMarcas() throws SQLException, JSONException {
        String consulta = "SELECT to_json(objec.*) as json\n"
                + "FROM (\n"
                + "    SELECT (\n"
                + "            SELECT array_to_json(array_agg(res.*))\n"
                + "FROM (\n"
                + "select rcr.* ,(\n"
                + "    \n"
                + "    select array_to_json(array_agg(rcr_h.*)) as sub_categorias\n"
                + "    from rep_categoria_rec rcr_h\n"
                + "    where rcr.id = rcr_h.id_padre\n"
                + "    and rcr_h.estado = 0\n"
                + ") sub_categorias\n"
                + "from rep_categoria_rec rcr\n"
                + "where rcr.id_padre is null\n"
                + "and rcr.estado = 0\n"
                + ") res\n"
                + "        ) AS categorias,\n"
                + "           (\n"
                + "             \n"
                + "SELECT array_to_json(array_agg(res.*))\n"
                + "FROM (\n"
                + "SELECT * ,(\n"
                + "    SELECT array_to_json(array_agg(ramo.*)) as modelos\n"
                + "    FROM  rep_auto_modelo ramo\n"
                + "        WHERE ramo.id_rep_auto_marca = rama.id\n"
                + "        AND ramo.estado = 0 \n"
                + "        \n"
                + "   \n"
                + ") \n"
                + "FROM rep_auto_marca rama\n"
                + "WHERE rama.estado = 0\n"
                + ") res\n"
                + "   \n"
                + "        ) AS marcas\n"
                + ") objec";
        PreparedStatement ps = con.statamet(consulta);
        String resp = "";
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            resp = rs.getString("json");
        }
        ps.close();
        rs.close();
        return resp;
    }

    public String getRepuestos(JSONArray busquedas) throws SQLException, JSONException {
        String consulta = "WITH RECURSIVE path AS (\n"
                + "    SELECT rep1.id, rep1.id_padre, \n"
                + "    ( \n"
                + "                    \n"
                + "                   SELECT ARRAY_to_json(ARRAY_AGG(rtf1.*)) \n"
                + "                    FROM (\n"
                + "                        SELECT rta1.* , ra1.* ,\n"
                + "                                rav1.nombre AS nombre_version,\n"
                + "                                ram1.id AS id_modelo, \n"
                + "                                ram1.nombre AS nombre_modelo, \n"
                + "                                ram1.url_foto AS url_foto_modelo, \n"
                + "                                rama1.id AS id_marca, \n"
                + "                                rama1.nombre AS nombre_marca, \n"
                + "                                rama1.url_foto AS url_foto_marca\n"
                + "\n"
                + "                        FROM rep_to_rep_auto rta1, rep_auto ra1, rep_auto_version rav1, rep_auto_modelo ram1, rep_auto_marca rama1\n"
                + "                        WHERE rta1.id_repuesto = rep1.id\n"
                + "                        AND rta1.id_rep_auto = ra1.id\n"
                + "                        AND rav1.id = ra1.id_version\n"
                + "                        AND ram1.id = rav1.id_rep_auto_modelo\n"
                + "                        AND rama1.id = ram1.id_rep_auto_marca\n"
                + "                        \n"
                + "                        ) rtf1\n"
                + "    )AS json \n"
                + "    FROM repuesto rep1\n"
                + "    WHERE rep1.id_padre IS NULL\n"
                + "    AND rep1.estado = 0\n"
                + "UNION ALL\n"
                + "    SELECT rep2.id, rep2.id_padre,\n"
                + "    ( \n"
                + "        SELECT ('[' || string_agg(value::TEXT, ', ') || ']')::JSON\n"
                + "        FROM (\n"
                + "            SELECT TRANSLATE(value::jsonb::TEXT, '[]','')::JSON AS VALUE FROM json_array_elements((\n"
                + "           SELECT ARRAY_to_json(ARRAY_AGG(rtf2.*)) \n"
                + "            FROM (\n"
                + "                        SELECT rta2.* , ra2.* ,\n"
                + "                                rav2.nombre AS nombre_version,\n"
                + "                                ram2.id AS id_modelo, \n"
                + "                                ram2.nombre AS nombre_modelo, \n"
                + "                                ram2.url_foto AS url_foto_modelo, \n"
                + "                                rama2.id AS id_marca, \n"
                + "                                rama2.nombre AS nombre_marca, \n"
                + "                                rama2.url_foto AS url_foto_marca\n"
                + "\n"
                + "                        FROM rep_to_rep_auto rta2, rep_auto ra2, rep_auto_version rav2, rep_auto_modelo ram2, rep_auto_marca rama2\n"
                + "                        WHERE rta2.id_repuesto = rep2.id\n"
                + "                        AND rta2.id_rep_auto = ra2.id\n"
                + "                        AND rav2.id = ra2.id_version\n"
                + "                        AND ram2.id = rav2.id_rep_auto_modelo\n"
                + "                        AND rama2.id = ram2.id_rep_auto_marca\n"
                + "                        ) rtf2\n"
                + ") ) asd\n"
                + "        UNION ALL\n"
                + "              SELECT TRANSLATE(value::jsonb::TEXT, '[]','')::JSON AS VALUE FROM json_array_elements(pa.json) AS p\n"
                + "        ) AS t              \n"
                + "    )AS json  \n"
                + "    FROM repuesto rep2\n"
                + "    INNER JOIN path AS pa\n"
                + "    ON (pa.id = rep2.id_padre)\n"
                + ")\n"
                + "SELECT ARRAY_to_json(array_agg(rsq.*)) as json\n"
                + "FROM (\n"
                + "   SELECT pa.json, tblrep.*, (SELECT COUNT(car.id_repuesto) FROM cardex car\n"
                + "                WHERE car.id_repuesto = tblrep.id\n"
                + "                AND car.estado = 0\n"
                + "                GROUP BY (car.id_repuesto)) AS cantidad \n"
                + "       FROM (\n"
                + "            SELECT pat.id, cast(pat.json AS jsonb) FROM path pat) pa, repuesto tblrep\n"
                + "        WHERE pa.id = tblrep.id LIMIT 12 \n";
                
        consulta += construirBusqueda(busquedas);

        consulta += " ) AS rsq";

        PreparedStatement ps = con.statamet(consulta);
        String resp = "";
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            resp = rs.getString("json");
        }
        ps.close();
        rs.close();
        return resp;
    }

    private String construirBusqueda(JSONArray arr) throws JSONException {
        if (arr == null) {
            return "";
        }
        JSONObject obj;
        if (arr.length() <= 0) {
            return "";
        }
        String resp = "";
        ArrayList<String> busVeh = new ArrayList();
        ArrayList<String> buscat = new ArrayList();
        for (int i = 0; i < arr.length(); i++) {
            obj = arr.getJSONObject(i);

            switch (obj.getInt("tipo")) {
                case 0: //ID CATEGORIA
                    buscat.add(" tblrc.id = " + obj.getInt("id_relacion"));
                    break;
                case 1:// ID SUB-CATEGORIA
                    buscat.add(" tblrc.id = " + obj.getInt("id_relacion"));
                    break;
                case 2: // ID MARCA
                    busVeh.add(" pa.json::TEXT LIKE '%\"id_marca\": " + obj.getInt("id_relacion") + ",%' ");
                    break;

                case 3: // ID MODELO
                    busVeh.add(" pa.json::TEXT LIKE '%\"id_modelo\": " + obj.getInt("id_relacion") + ",%' ");
                    break;

            }

        }
        for (int i = 0; i < busVeh.size(); i++) {
            if (i == 0) {
                resp += " AND ( ";
            } else {
                resp += " OR ";
            }
            resp += busVeh.get(i);
            if (i == busVeh.size() - 1) {
                resp += " ) ";
            }
        }
        for (int i = 0; i < buscat.size(); i++) {
            if (i == 0) {
                resp += " AND ( ";
            } else {
                resp += " OR ";
            }
            resp += buscat.get(i);
            if (i == buscat.size() - 1) {
                resp += " ) ";
            }
        }
        return resp;
    }
}
