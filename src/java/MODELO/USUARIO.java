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

public class USUARIO {

    private int ID;
    private String NOMBRE;
    private String APELLIDO_PA;
    private String APELLIDO_MA;
    private String CI;
    private String USUARIO;
    private String CONTRASENHA;
    private String SEXO;
    private String CORREO;
    private String TELEFONO;
    private String DIRECCION;
    private String ID_FACE;
    private Date FECHA_NAC;
    private int ID_ROL;
    private String FOTO_PERFIL;

    private Conexion con = null;

    public USUARIO(Conexion con) {
        this.con = con;
    }

    public int Insertar_sin_fecha() throws SQLException {
        String consulta = "INSERT INTO public.usuario(\n"
                + "nombre, apellido_pa, apellido_ma, usuario, contrasenha, id_rol, correo, sexo, telefono)\n"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, getNOMBRE());
        ps.setString(2, getAPELLIDO_PA());
        ps.setString(3, getAPELLIDO_MA());
        ps.setString(4, getUSUARIO());
        ps.setString(5, getCONTRASENHA());
        ps.setInt(6, getID_ROL());
        ps.setString(7, getCORREO());
        ps.setString(8, getSEXO());
        ps.setString(9, getTELEFONO());
        ps.execute();

        consulta = "select last_value from usuario_id_seq ";
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

    public int Insertar() throws SQLException {
        String consulta = "INSERT INTO public.usuario(\n"
                + "nombre, apellido_pa, apellido_ma, usuario, contrasenha, id_rol, correo, sexo, telefono, fecha_nac)\n"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, getNOMBRE());
        ps.setString(2, getAPELLIDO_PA());
        ps.setString(3, getAPELLIDO_MA());
        ps.setString(4, getUSUARIO());
        ps.setString(5, getCONTRASENHA());
        ps.setInt(6, getID_ROL());
        ps.setString(7, getCORREO());
        ps.setString(8, getSEXO());
        ps.setString(9, getTELEFONO());
        ps.setDate(10, new java.sql.Date(getFECHA_NAC().getTime()));
        ps.execute();

        consulta = "select last_value from usuario_id_seq ";
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

    public JSONObject getById(int id) throws SQLException, JSONException {
        String consulta = "select us.* "
                + "from usuario us\n"
                + "where us.id=? and us.estado = 0";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj = parseJson(rs);
        } else {
            obj.put("exito", "no");
        }
        ps.close();
        rs.close();
        return obj;
    }

    public int Insertar_face() throws SQLException {
        String consulta = "INSERT INTO public.usuario(\n"
                + "nombre, apellido_pa,id_rol, correo, sexo, telefono,id_face)\n"
                + "VALUES (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, getNOMBRE());
        ps.setString(2, getAPELLIDO_PA());
        ps.setInt(3, getID_ROL());
        ps.setString(4, getCORREO());
        ps.setString(5, getSEXO());
        ps.setString(6, getTELEFONO());
        ps.setString(7, getID_FACE());
        ps.execute();
        consulta = "select last_value from usuario_id_seq ";
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

    public int subir_foto_perfil() throws SQLException {
        String consulta = "UPDATE public.usuario\n"
                + "	SET foto_perfil=?\n"
                + "	WHERE id=" + getID();
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, getFOTO_PERFIL());
        int row = ps.executeUpdate();
        ps.close();
        return row;
    }

    public int editar() throws SQLException {
        String consulta = "UPDATE public.usuario\n"
                + "	SET nombre=?, apellido_pa=?, apellido_ma=?\n"
                + "	WHERE id=" + getID();
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, getNOMBRE());
        ps.setString(2, getAPELLIDO_PA());
        ps.setString(3, getAPELLIDO_MA());
        int row = ps.executeUpdate();
        ps.close();
        return row;
    }

    public int eliminar() throws SQLException {
        String consulta = "UPDATE public.usuario\n"
                + "	SET estado=?\n"
                + "	WHERE id=" + getID();
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, 1);
        int row = ps.executeUpdate();
        ps.close();
        return row;
    }

    public int cambiar_pass() throws SQLException {
        String consulta = "UPDATE public.usuario\n"
                + "	SET contrasenha=?\n"
                + "	WHERE id=" + getID();
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, getCONTRASENHA());
        int row = ps.executeUpdate();
        ps.close();
        return row;
    }

    public boolean validar_usr(String usr) throws SQLException, JSONException {
        String consulta = "select usuario.id from usuario where usuario.usuario=?";
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, usr);
        ResultSet rs = ps.executeQuery();
        boolean exist = false;
        if (rs.next()) {
            exist = true;
        }
        ps.close();
        rs.close();
        return exist;
    }

    public JSONObject get_por_usr_y_pass(String usr, String pass) throws SQLException, JSONException {
        String consulta = "select us.*, ro.nombre as nombrerol \n"
                + "from usuario us, rol ro\n"
                + "where us.usuario=? and us.contrasenha=? \n"
                + "and us.id_rol=ro.id";
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, usr);
        ps.setString(2, pass);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj = parseJson(rs);
            obj.put("nombrerol", rs.getString("nombrerol") != null ? rs.getString("nombrerol") : "");
            obj.put("exito", "si");
        } else {
            obj.put("exito", "no");
        }
        ps.close();
        rs.close();
        return obj;
    }

    public JSONObject getCliente_por_id(int id) throws SQLException, JSONException {
        String consulta = "select us.* "
                + "from usuario us\n"
                + "where us.id=?";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj = parseJson(rs);
        } else {
            obj.put("exito", "no");
        }
        ps.close();
        rs.close();
        return obj;
    }

    public JSONArray getPagination(int pag, int limit, String busqueda) throws SQLException, JSONException {
        String consulta = "select * from usuario ram\n"
                + "where ram.estado = 0 and upper(ram.nombre) like upper('%" + busqueda + "%')\n"
                + "order by (ram.nombre) asc\n"
                + "limit " + limit + " offset " + (pag * limit);
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray arr = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = parseJson(rs);
            arr.put(obj);
        }
        ps.close();
        rs.close();
        return arr;
    }

    public JSONArray getPaginationDescueto(int pag, int limit, String busqueda) throws SQLException, JSONException {
        String consulta = "select array_to_json(array_agg(json.*)) as arr\n"
                + "from( \n"
                + "select usuario.*, to_json(descuento.*) as descuentos\n"
                + "from\n"
                + "usuario,\n"
                + "descuento \n"
                + "where usuario.id = descuento.id_admin\n"
                + "order by descuento.fecha desc\n"
                + "limit " + limit + " offset " + (pag * limit) + "\n"
                + ") json";

        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray arr = new JSONArray();
        JSONObject obj;
        if (rs.next()) {
            arr = new JSONArray(rs.getString("arr"));
        }
        ps.close();
        rs.close();
        return arr;
    }

    public int getDescuentoActual() throws SQLException, JSONException {
        String consulta = "select descuento.cantidad\n"
                + "from\n"
                + "descuento \n"
                + "order by descuento.fecha desc   \n"
                + "limit 1";

        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        int cantidad = 0;
        if (rs.next()) {
            cantidad = rs.getInt("cantidad");
            }
        ps.close();
        rs.close();
        return cantidad;
    }

    public int getCantidad(int estado, String busqueda) throws SQLException, JSONException {
        String consulta = "select count(ram.id) from usuario ram\n"
                + "where ram.estado = " + estado + " and upper(ram.nombre) like upper('%" + busqueda + "%')";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        int cantidad = 0;
        if (rs.next()) {
            cantidad = rs.getInt("count");
        }
        ps.close();
        rs.close();
        return cantidad;
    }

    private JSONObject parseObj;

    private JSONObject parseJson(ResultSet rs) throws JSONException, SQLException {
        parseObj = new JSONObject();
        parseObj.put("apellido_pa", rs.getString("apellido_pa") != null ? rs.getString("apellido_pa") : "");
        parseObj.put("apellido_ma", rs.getString("apellido_ma") != null ? rs.getString("apellido_ma") : "");
        parseObj.put("direccion", rs.getString("direccion") != null ? rs.getString("direccion") : "");
        parseObj.put("id", rs.getInt("id"));
        parseObj.put("nombre", rs.getString("nombre") != null ? rs.getString("nombre") : "");
        parseObj.put("usuario", rs.getString("usuario"));
        parseObj.put("id_rol", rs.getInt("id_rol"));
        parseObj.put("correo", rs.getString("correo") != null ? rs.getString("correo") : "");
        parseObj.put("sexo", rs.getString("sexo") != null ? rs.getString("sexo") : "");
        parseObj.put("fecha_nac", rs.getString("fecha_nac") != null ? rs.getString("fecha_nac") : "");
        parseObj.put("ci", rs.getString("ci"));
        parseObj.put("telefono", rs.getString("telefono") != null ? rs.getString("telefono") : "");
        parseObj.put("url_foto", rs.getString("foto_perfil") != null ? rs.getString("foto_perfil") : "img/Sin_imagen.png");
        parseObj.put("exito", "si");
        return parseObj;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNOMBRE() {
        return NOMBRE != null ? NOMBRE : "";
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getAPELLIDO_PA() {
        return APELLIDO_PA != null ? APELLIDO_PA : "";
    }

    public void setAPELLIDO_PA(String APELLIDO_PA) {
        this.APELLIDO_PA = APELLIDO_PA;
    }

    public String getAPELLIDO_MA() {
        return APELLIDO_MA != null ? APELLIDO_MA : "";
    }

    public void setAPELLIDO_MA(String APELLIDO_MA) {
        this.APELLIDO_MA = APELLIDO_MA;
    }

    public String getUSUARIO() {
        return USUARIO != null ? USUARIO : "";
    }

    public void setUSUARIO(String USUARIO) {
        this.USUARIO = USUARIO;
    }

    public String getCONTRASENHA() {
        return CONTRASENHA != null ? CONTRASENHA : "";
    }

    public void setCONTRASENHA(String CONTRASENHA) {
        this.CONTRASENHA = CONTRASENHA;
    }

    public int getID_ROL() {
        return ID_ROL;
    }

    public void setID_ROL(int ID_ROL) {
        this.ID_ROL = ID_ROL;
    }

    public Conexion getCon() {
        return con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    public String getSEXO() {
        return SEXO != null ? SEXO : "";
    }

    public void setSEXO(String SEXO) {
        this.SEXO = SEXO;
    }

    public String getCORREO() {
        return CORREO != null ? CORREO : "";
    }

    public void setCORREO(String CORREO) {
        this.CORREO = CORREO;
    }

    public Date getFECHA_NAC() {
        return FECHA_NAC;
    }

    public void setFECHA_NAC(Date FECHA_NAC) {
        this.FECHA_NAC = FECHA_NAC;
    }

    public String getFOTO_PERFIL() {
        return FOTO_PERFIL;
    }

    public void setFOTO_PERFIL(String FOTO_PERFIL) {
        this.FOTO_PERFIL = FOTO_PERFIL;
    }

    public String getTELEFONO() {
        return TELEFONO;
    }

    public void setTELEFONO(String TELEFONO) {
        this.TELEFONO = TELEFONO;
    }

    public String getID_FACE() {
        return ID_FACE;
    }

    public void setID_FACE(String ID_FACE) {
        this.ID_FACE = ID_FACE;
    }

    public String getCI() {
        return CI;
    }

    public void setCI(String CI) {
        this.CI = CI;
    }

    public String getDIRECCION() {
        return DIRECCION;
    }

    public void setDIRECCION(String DIRECCION) {
        this.DIRECCION = DIRECCION;
    }

}
