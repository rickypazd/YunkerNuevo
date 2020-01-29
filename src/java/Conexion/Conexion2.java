package Conexion;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class Conexion2 {

    private String driver;
    private String url;
    private String host;
    private int puerto;
    private String baseDatos;
    private String usuarioBase;
    private String passwordBase;
    private Connection con;
    private boolean conectado;

    public Conexion2() {
        this.driver = "org.postgresql.Driver";
        this.host = "localhost";
        this.puerto = 5432;
        this.baseDatos = "YunkerNew";
        this.usuarioBase = "postgres";
        this.passwordBase = "rickypina123";
        this.url = "jdbc:postgresql://" + host + ":" + puerto + "/" + baseDatos;
        conectado = false;
        open();
    }
    
    public int insertar(String sql) throws SQLException{
        Statement stt = con.createStatement();
        stt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = stt.getGeneratedKeys();
        int id = 0;
        if(rs.next()) id = rs.getInt("ID");
        rs.close();
        stt.close();
        return id;
    }
     public Array createArray(String[] arr) throws SQLException{
        Array array = con.createArrayOf("varchar", arr);
        return array;
    }

    private void open() {
        try {
            if (!conectado) {
                try {
                    Class.forName(driver);
                } catch (ClassNotFoundException ex) {
                    System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
                }
                con = DriverManager.getConnection(url, usuarioBase, passwordBase);
                conectado = true;
            }
        } catch (java.sql.SQLException sqle) {
            System.out.println("Error al conectar con la base de datos de PostgreSQL (" + url + "): " + sqle);
        }
    }

    public void ejecutarSentencia(String sentencia) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sentencia);
        ps.execute();
        ps.close();
    }

    public void ejecutarSentencia(String sentencia, Object... campos) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sentencia);
        for (int i = 0; i < campos.length; i++) {
            ps.setObject(i + 1, campos[i]);
        }
        ps.execute();
        ps.close();
    }
    

    public void ejecutarSentencia(String sentencia, String... campos) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sentencia);
        for (int i = 0; i < campos.length; i++) {
            ps.setString(i + 1, campos[i]);
        }
        ps.execute();
        ps.close();
    }
    
    public int ejecutarSentencia(String sentencia, ArrayList campos) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sentencia);
        for (int i = 0; i < campos.size(); i++) {
            ps.setObject(i + 1, campos.get(i));
        }
        ResultSet rs = ps.executeQuery();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt("id");
        }
        rs.close();
        ps.close();
        return id;
    }

    public int ejecutarInsert(String consulta, String columnID, Object... campos) throws SQLException {
        consulta = consulta.replaceAll(";", "");
        consulta += "\nRETURNING \"" + columnID + "\";";
        PreparedStatement ps = con.prepareStatement(consulta);
        for (int i = 0; i < campos.length; i++) {
            ps.setObject(i + 1, campos[i]);
        }
        ResultSet rs = ps.executeQuery();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt(columnID);
        }
        rs.close();
        ps.close();
        return id;
    }
    
    public void insertar(String tabla, JSONObject datos) throws SQLException, JSONException{
        String consulta = "INSERT INTO public.\""+tabla+"\"";
        String values="";
        String dt="";
        Iterator<String> keys = datos.keys();
        while(keys.hasNext()){
            values+=keys.next()+",";
            dt+="?,";
        }
        if(values.length()>0) values=values.substring(0, values.length()-1);
        if(dt.length()>0) dt=dt.substring(0, dt.length()-1);
        consulta += "("+values+")";
        consulta += "("+dt+")";
        consulta += "\nRETURNING \"id\";";
        PreparedStatement ps = con.prepareStatement(consulta);
        int i=0;
        while(keys.hasNext()){
            ps.setString(i + 1, datos.getString(keys.next()));
            i++;
        }
        ResultSet rs = ps.executeQuery();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt("id");
        }
        ps.close();
        datos.put("id", id);
    }
    
    public int ejecutarInsert(String consulta, String... campos) throws SQLException {
        consulta = consulta.replaceAll(";", "");
        consulta += "\nRETURNING \"id\";";
        PreparedStatement ps = con.prepareStatement(consulta);
        for (int i = 0; i < campos.length; i++) {
            ps.setString(i + 1, campos[i]);
        }
        ResultSet rs = ps.executeQuery();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt("id");
        }
        ps.close();
        return id;
    }

    public void close() {
        if (conectado) {
            try {
                con.close();
                conectado = false;
            } catch (SQLException ex) {
                conectado = false;
            }
        }
    }

    public ResultSet ejecutarConsulta(String consulta) throws SQLException {
        PreparedStatement ps;
        try {
            ps = con.prepareStatement(consulta);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            rollback();
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet ejecutarConsulta(String consulta, Object... campos) throws SQLException {
        PreparedStatement ps;
        try {
            ps = statametObject(consulta);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            rollback();
            e.printStackTrace();
            return null;
        }
    }

    public PreparedStatement statamet(String sql) {
        try {
            return con.prepareStatement(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion2.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public PreparedStatement statametString(String sql, String[] campos) {
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            for (int i = 0; i < campos.length; i++) {
                ps.setString(i + 1, campos[i]);
            }
            return ps;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion2.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public PreparedStatement statametObject(String sql, Object... campos) {
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            for (int i = 0; i < campos.length; i++) {
                ps.setObject(i + 1, campos[i]);
            }
            return ps;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion2.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public boolean isConectado() {
        return conectado;
    }

    public void transacction() {
        try {
            if (con.getAutoCommit()) {
                con.setAutoCommit(false);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void transacctionEnd() {
        try {
            if (!con.getAutoCommit()) {
                con.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void commit() {
        try {
            con.commit();
        } catch (SQLException ex) {
        }
    }

    public void rollback() {
        try {
            con.rollback();
        } catch (SQLException ex) {
        }
    }

    public void error(Object obj, Exception ex) {
        rollback();
        ex.printStackTrace();
    }

    private static Conexion2 conexion;

    public static Conexion2 getConeccion() {
        try {
            if (conexion == null || !conexion.isConectado()) {
                conexion = new Conexion2();
            }
            if(conexion.con.isClosed()){
//                conexion.close();

                conexion =new Conexion2();
            }
            return conexion;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
   
}
