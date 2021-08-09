/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADOR;

import Conexion.Conexion;
import Conexion.SngleCon;
import MODELO.CONSULTA;

import UTILES.URL;
import MODELO.USUARIO;
import MODELO.PERMISO;
import UTILES.EVENTOS;
import UTILES.RESPUESTA;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

import java.nio.file.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author RICKY
 */
@MultipartConfig
@WebServlet(name = "adminController", urlPatterns = {"/admin/adminController"})
public class adminController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //<editor-fold defaultstate="collapsed" desc="CONFIG">
        Conexion con = SngleCon.getCon();
        con.Transacction();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        String evento = request.getParameter("evento");
        String tokenAcceso = request.getParameter("TokenAcceso");
        boolean retornar = true;
        String html = "";
//</editor-fold>

        if (tokenAcceso.equals(URL.TokenAcceso)) {
            switch (evento) {

                //<editor-fold defaultstate="collapsed" desc="USUARIO">
                case "registrar_usuario":
                    html = registrar_usuario(request, con);
                    break;
                case "editar_usuario":
                    html = editar_usuario(request, con);
                    break;
                case "eliminar_usuario":
                    html = eliminar_usuario(request, con);
                    break;
                case "subirFoto_usuario":
                    html = subirFoto_usuario(request, con);
                    break;
                case "getAll_pagination_usuario":
                    html = getAll_pagination_usuario(request, con);
                    break;
                case "getAll_pagination_usuarioDescuento":
                    html = getAll_pagination_usuarioDescuento(request, con);
                    break;
                case "getDescuentoActual":
                    html = getDescuentoActual(request, con);
                    break;
                case "getCant_usuarios":
                    html = getCant_usuarios(request, con);
                    break;
          
                case "getById_usuario":
                    html = getById_usuario(request, con);
                    break;
                case "registrarDescuento":
                    html = registrarDescuento(request, con);
                    break;
                case "getDescuento":
                    html = getDescuento(request, con);
                    break;
//</editor-fold>
                case "getTableTraductor":
                    html = getTableTraductor(request, con);
                    break;

            }
        } else {
            RESPUESTA resp = new RESPUESTA(0, "Servisis: Token de acceso erroneo.", "Token denegado", "{}");
            html = resp.toString();
        }
        //<editor-fold defaultstate="collapsed" desc="RESPUESTA">
        con.commit();
//        con.Close();
        if (retornar) {
            response.getWriter().write(html);
        }
//</editor-fold>
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PARSERS">
    private int parseInt(String val) {
        return Integer.parseInt(val);
    }

    private String pString(HttpServletRequest request, String key) {
        return request.getParameter(key);
    }

    private int pInt(HttpServletRequest request, String key) {
        return Integer.parseInt(request.getParameter(key));
    }

    private double pDouble(HttpServletRequest request, String key) {
        return Double.parseDouble(request.getParameter(key));
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="USUARIO">
    private String registrar_usuario(HttpServletRequest request, Conexion con) {
        try {
            String nombre = request.getParameter("nombre");
            String apellido_pa = request.getParameter("apellido_pa");
            String apellido_ma = request.getParameter("apellido_ma");
            String usuario = request.getParameter("usuario");
            usuario = usuario.trim();
            String pass = request.getParameter("pass");
            int id_rol = Integer.parseInt(request.getParameter("id_rol"));
            USUARIO usr = new USUARIO(con);
            usr.setNOMBRE(nombre);
            usr.setAPELLIDO_PA(apellido_pa);
            usr.setAPELLIDO_MA(apellido_ma);
            usr.setUSUARIO(usuario);
            usr.setCONTRASENHA(pass);
            usr.setID_ROL(id_rol);
            RESPUESTA resp = new RESPUESTA(1, "", "Usuario registrado con exito.", usr.Insertar_sin_fecha() + "");
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al registrar usuario.", "{}");
            return resp.toString();
        }
    }

    private String editar_usuario(HttpServletRequest request, Conexion con) {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String nombre = request.getParameter("nombre");
            String apellido_pa = request.getParameter("apellido_pa");
            String apellido_ma = request.getParameter("apellido_ma");
            USUARIO usr = new USUARIO(con);
            usr.setID(id);
            usr.setNOMBRE(nombre);
            usr.setAPELLIDO_PA(apellido_pa);
            usr.setAPELLIDO_MA(apellido_ma);
            RESPUESTA resp = new RESPUESTA(1, "", "Usuario registrado con exito.", usr.editar() + "");
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al registrar usuario.", "{}");
            return resp.toString();
        }
    }

    private String eliminar_usuario(HttpServletRequest request, Conexion con) {
        String nameAlert = "usuario";
        try {
            int id = pInt(request, "id");
            USUARIO usr = new USUARIO(con);
            usr.setID(id);
            usr.eliminar();
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", "");
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al obtener " + nameAlert + ".", "{}");
            return resp.toString();
        }
    }

    private String subirFoto_usuario(HttpServletRequest request, Conexion con) {
        String nameAlert = "usuario";
        try {
            int id_usr = Integer.parseInt(request.getParameter("id"));
            Part file = request.getPart("archibo");
            String name = "";
            String names = "";

            if (file != null) {
                names = file.getSubmittedFileName();
//                String ruta = request.getSession().getServletContext().getRealPath("/");
                name = EVENTOS.guardar_file(file, "", URL.ruta_foto_prefil + "/" + id_usr + "/", names);
            }
            USUARIO usr = new USUARIO(con);
            usr.setID(id_usr);
            usr.setFOTO_PERFIL(URL.ruta_foto_prefil + "/" + id_usr + "/" + name);
            usr.subir_foto_perfil();

            return "exito";
        } catch (IOException ex) {
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al obtener " + nameAlert + ".", "{}");
            return resp.toString();
        } catch (ServletException ex) {
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al obtener " + nameAlert + ".", "{}");
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            return resp.toString();
        } catch (SQLException ex) {
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al obtener " + nameAlert + ".", "{}");
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            return resp.toString();
        }
    }

    private String getAll_pagination_usuario(HttpServletRequest request, Conexion con) {
        String nameAlert = "usuario";
        try {
            int pagina = pInt(request, "pagina");
            int cantidad = pInt(request, "cantidad");
            String busqueda = pString(request, "busqueda");
            USUARIO usuario = new USUARIO(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", usuario.getPagination(pagina, cantidad, busqueda).toString());
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al obtener " + nameAlert + ".", "{}");
            return resp.toString();
        } catch (JSONException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al convertir " + nameAlert + " a JSON.", "{}");
            return resp.toString();
        }
    }
    private String getAll_pagination_usuarioDescuento(HttpServletRequest request, Conexion con) {
        String nameAlert = "usuario";
        try {
            int pagina = pInt(request, "pagina");
            int cantidad = pInt(request, "cantidad");
            String busqueda = pString(request, "busqueda");
            USUARIO usuario = new USUARIO(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", usuario.getPaginationDescueto(pagina, cantidad, busqueda).toString());
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al obtener " + nameAlert + ".", "{}");
            return resp.toString();
        } catch (JSONException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al convertir " + nameAlert + " a JSON.", "{}");
            return resp.toString();
        }
    }
    private String getDescuentoActual(HttpServletRequest request, Conexion con) {
        String nameAlert = "usuario";
        try {
            USUARIO usuario = new USUARIO(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", usuario.getDescuentoActual()+"");
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al obtener " + nameAlert + ".", "{}");
            return resp.toString();
        } catch (JSONException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al convertir " + nameAlert + " a JSON.", "{}");
            return resp.toString();
        }
    }

    private String getCant_usuarios(HttpServletRequest request, Conexion con) {
        String nameAlert = "usuario";
        try {
            USUARIO usuario = new USUARIO(con);
            String busqueda = pString(request, "busqueda");
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", usuario.getCantidad(0, busqueda) + "");
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al obtener " + nameAlert + ".", "{}");
            return resp.toString();
        } catch (JSONException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al convertir " + nameAlert + " a JSON.", "{}");
            return resp.toString();
        }
    }
 
    private String getById_usuario(HttpServletRequest request, Conexion con) {
        String nameAlert = "usuario";
        try {
            int id = pInt(request, "id");
            USUARIO usuario = new USUARIO(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", usuario.getById(id).toString());
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al obtener " + nameAlert + ".", "{}");
            return resp.toString();
        } catch (JSONException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al convertir " + nameAlert + " a JSON.", "{}");
            return resp.toString();
        }
    }

//</editor-fold>
    private String getTableTraductor(HttpServletRequest request, Conexion con) {
        String nombre_tabla = request.getParameter("nombre_tabla");
        String nameAlert = nombre_tabla;
        try {

            JSONObject obj = new JSONObject();
            obj.put("nombre_tabla", nombre_tabla);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", CONSULTA.selectAll(obj));
            return resp.toString();
        } catch (JSONException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al convertir " + nameAlert + " a JSON.", "{}");
            return resp.toString();
        }
    }

    private String registrarDescuento(HttpServletRequest request, Conexion con) {
        try {
            int descuento = Integer.parseInt(request.getParameter("descuento"));
            int usr_log = Integer.parseInt(request.getParameter("usr_log"));

            String consulta = "INSERT INTO public.descuento(\n"
                    + "	cantidad, fecha, id_admin)\n"
                    + "	VALUES (?,now(),?);";
            PreparedStatement ps = con.statamet(consulta);

            ps.setInt(1, descuento);
            ps.setInt(2, usr_log);
            ps.execute();
            consulta = "select last_value from descuento_id_seq ";
            ps = con.statamet(consulta);
            ResultSet rs = ps.executeQuery();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt("last_value");
            }
            rs.close();
            ps.close();

            RESPUESTA resp = new RESPUESTA(1, "", "Descuento registrado con exito.", "");
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al registrar descuento.", "{}");
            return resp.toString();
        }
    }

    private String getDescuento(HttpServletRequest request, Conexion con) {
        try {

            String consulta = "select cantidad, fecha\n"
                    + "from descuento\n"
                    + "order by fecha desc\n"
                    + "limit 1";
            PreparedStatement ps = con.statamet(consulta);
            ResultSet rs = ps.executeQuery();
            int descuento = 0;
            if (rs.next()) {
                descuento = rs.getInt("cantidad");
            }
            rs.close();
            ps.close();

            RESPUESTA resp = new RESPUESTA(1, "", "Descuento exito.", descuento + "");
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al obtener descuento.", "{}");
            return resp.toString();
        }
    }


}
