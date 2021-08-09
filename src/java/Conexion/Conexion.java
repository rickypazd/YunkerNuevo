    package Conexion;

import UTILES.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion
{
//    public static Connection con;  //POR QUE LA CONEXION INCIA OTRA INSTANCIA EN LA BASE DE DATOS
//    public static boolean loggedIn = false;
    public Connection con;
    public boolean loggedIn = false;
    private static Conexion ConexionConectada = null;    
    protected ResultSet rs;
    private String usr,pass,baseDatos,puerto,ip;
    

    public Conexion(String usr, String pass)
    {
        
        loggedIn=false;
        this.usr = usr;
        this.pass = pass;
        this.baseDatos=URL.db_name;
        //this.baseDatos="postgres";
        this.puerto="5432";
//        this.ip="204.93.165.196";
        this.ip=URL.ipdb;
        ifConnected();       
        
    }
    
      
    public PreparedStatement statamet(String sql)
    {
        try {
            return con.prepareStatement(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    public CallableStatement callable_statamet(String sql)
    {
        try {
            return con.prepareCall(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public boolean ifConnected()
    {        
        try
        {
            if ( con != null || !con.isClosed())
            {
             // con = Conexion.getDatabaseConnection().getConnection();
                return true;
            }
            else
            {
                Conectar();
                return true;
            }
        }
        catch(Exception e)
        {
            
            return false;
        }
        
    }
    
    public Savepoint SavePoint() 
    {
        try {
            return con.setSavepoint();
        } catch (SQLException ex) {            
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static Conexion getDatabaseConnection()
    {
        return Conexion.ConexionConectada;
    }

    private static void setDatabaseConnection(Conexion conexion)
    {
        Conexion.ConexionConectada = conexion;
    }
    
    public Connection getConnection()
    {
        return con;
    }
    
    public void Transacction() 
    {                
        
        try {
            if(con.getAutoCommit())
            {
                con.setAutoCommit(false);                    
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Transacction_end()
    {
        try 
        {
            if(!con.getAutoCommit())            
                con.setAutoCommit(true);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }    
    
    public void commit()
    {        
        try {
            con.commit();
        } catch (SQLException ex) {
           // Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
    public void rollback() 
    {
        try {        
            con.rollback();
        } catch (SQLException ex) {
           // Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void rollback(Savepoint savepoint) 
    {
        try {
            con.rollback(savepoint);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
            
    public void EjecutarSentencia(String sentencia) throws SQLException
    {
        //ifConnected();         
        PreparedStatement ps = con.prepareStatement(sentencia);
        ps.execute();
        ps.close();
    }
    
    public void EjecutarDDL(String sentencia) throws SQLException
    {
        Statement ps;        
        ps = con.createStatement();
        ps.execute(sentencia);
        ps.close();
    }
        
    public void EjecutarUpdate(String sentencia) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException 
    {
        ifConnected();
        PreparedStatement ps = con.prepareStatement(sentencia);
        ps.executeUpdate();     
        ps.close();
    }
    
    public void Close()
    {
        if(loggedIn)
        {
            try {                
                con.close();
                loggedIn=false;
            } catch (SQLException ex) {
                loggedIn=false;
            }
        }
    }    

    public void Conectar() throws SQLException 
    {           
        try
        {
            Class.forName("org.postgresql.Driver");       
            Login();        
        }
        catch(Exception e)
        {
            
        }        
    }

    private void Login() 
    {
        try {
            con = DriverManager.getConnection("jdbc:postgresql://"+ip+":"+puerto+"/"+baseDatos, usr, pass);
            loggedIn = true;        
            setDatabaseConnection(this);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public ResultSet getRs() {
        return rs;
    }

    public void setRs(ResultSet rs) {
        this.rs = rs;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public String getBaseDatos() {
        return baseDatos;
    }

    public void setBaseDatos(String baseDatos) {
        this.baseDatos = baseDatos;
    }

    public String getPuerto() {
        return puerto;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    

}
