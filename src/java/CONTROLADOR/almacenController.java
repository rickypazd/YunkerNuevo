/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADOR;

import Conexion.Conexion;
import Conexion.SngleCon;
import MODELO.ALMACEN.ALMACEN;
import MODELO.ALMACEN.ALMACEN_SEC;
import MODELO.AUTO.REP_AUTO;
import MODELO.AUTO.REP_AUTO_MARCA;
import MODELO.REPUESTO.REPUESTO;
import MODELO.REPUESTO.REP_CATEGORIA;
import MODELO.REPUESTO.REP_DETALLE;
import MODELO.REPUESTO.REP_SUB_CATEGORIA;
import UTILES.URL;
import MODELO.USUARIO;
import UTILES.EVENTOS;
import UTILES.RESPUESTA;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

import java.nio.file.*;
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
@WebServlet(name = "almacenController", urlPatterns = {"/almacenController"})

public class almacenController extends HttpServlet {

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
        String tokenAcceso = request.getParameter("TokenAcceso") != null ? request.getParameter("TokenAcceso") : "";
        boolean retornar = true;
        String html = "";
//</editor-fold>

        if (tokenAcceso.equals(URL.TokenAcceso)) {
            switch (evento) {
                //<editor-fold defaultstate="collapsed" desc="ALMACEN">
                case "registrar_almacen":
                    html = registrar_almacen(request, con);
                    break;
                case "getAll_almacen_pagination":
                    html = getAll_almacen_pagination(request, con);
                    break;

                case "eliminar_almacen":
                    html = eliminar_almacen(request, con);
                    break;
//</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="SUBALMACEN">
                case "registrar_sub_almacen":
                    html = registrar_sub_almacen(request, con);
                    break;
                case "edit_almacen_sec":
                    html = edit_almacen_sec(request, con);
                    break;
                case "getAll_sub_almacen":
                    html = getAll_sub_almacen(request, con);
                    break;
                case "eliminar_almacen_sec":
                    html = eliminar_almacen_sec(request, con);
                    break;

//</editor-fold>
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
    //<editor-fold defaultstate="collapsed" desc="ALMACEN">

    private String registrar_almacen(HttpServletRequest request, Conexion con) {
        String nameAlert = "almacen";
        try {
            String nombre = pString(request, "nombre");
            String direccion = pString(request, "direccion");
            double lat = pDouble(request, "lat");
            double lng = pDouble(request, "lng");
            ALMACEN almacen = new ALMACEN(con);
            int id = almacen.Insertar(nombre, lat, lng, direccion);
            JSONObject obj = new JSONObject();
            obj.put("nombre", nombre);
            obj.put("direccion", direccion);
            obj.put("lng", lng);
            obj.put("lat", lat);
            obj.put("id", id);
            RESPUESTA resp = new RESPUESTA(1, "", nameAlert + " registrado con exito.", obj.toString());
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al registrar " + nameAlert + ".", "{}");
            return resp.toString();
        } catch (JSONException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al convertir " + nameAlert + " a JSON.", "{}");
            return resp.toString();
        }
    }

    private String eliminar_almacen(HttpServletRequest request, Conexion con) {
        String nameAlert = "almacen";
        try {
            int id = pInt(request, "id");
            ALMACEN almacen = new ALMACEN(con);

            almacen.eliminar(id);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", "{}");
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al obtener " + nameAlert + ".", "{}");
            return resp.toString();
        }
    }
    

    private String getAll_almacen_pagination(HttpServletRequest request, Conexion con) {
        String nameAlert = "almacen";
        try {
            int pagina = pInt(request, "pagina");
            int cantidad = pInt(request, "cantidad");
            String busqueda = pString(request, "busqueda");
            ALMACEN almacen = new ALMACEN(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", almacen.getPaginationJSON(pagina, cantidad, busqueda));
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

    private String registrar_sub_almacen(HttpServletRequest request, Conexion con) {
        String nameAlert = "almacen_sec";
        try {
            String nombre = pString(request, "nombre");

            int id_almacen = pInt(request, "id_almacen");
            int id_padre = pInt(request, "id_padre");
            int x1 = pInt(request, "x1");
            int y1 = pInt(request, "y1");
            int x2 = pInt(request, "x2");
            int y2 = pInt(request, "y2");
            ALMACEN_SEC almacen = new ALMACEN_SEC(con);
            int id = almacen.Insertar(id_almacen, id_padre, nombre, x1, y1, x2, y2);
            RESPUESTA resp = new RESPUESTA(1, "", nameAlert + " registrado con exito.", "exito");
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al registrar " + nameAlert + ".", "{}");
            return resp.toString();
        }
    }
    private String edit_almacen_sec(HttpServletRequest request, Conexion con) {
        String nameAlert = "almacen_sec";
        try {
            

            int id = pInt(request, "id");
            
            int x1 = pInt(request, "x1");
            int y1 = pInt(request, "y1");
            int x2 = pInt(request, "x2");
            int y2 = pInt(request, "y2");
            ALMACEN_SEC almacen = new ALMACEN_SEC(con);
            almacen.editar_pos(id, x1, y1, x2, y2);
            RESPUESTA resp = new RESPUESTA(1, "", nameAlert + " registrado con exito.", "exito");
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al registrar " + nameAlert + ".", "{}");
            return resp.toString();
        }
    }

    private String getAll_sub_almacen(HttpServletRequest request, Conexion con) {
        String nameAlert = "almacen";
        try {
            int id_almacen = pInt(request, "id_almacen");
            ALMACEN_SEC almacen = new ALMACEN_SEC(con);

            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", almacen.getAll(id_almacen));
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
    private String eliminar_almacen_sec(HttpServletRequest request, Conexion con) {
        String nameAlert = "almacen";
        try {
            int id = pInt(request, "id");
             ALMACEN_SEC almacen = new ALMACEN_SEC(con);

            almacen.eliminar(id);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", "{}");
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al obtener " + nameAlert + ".", "{}");
            return resp.toString();
        }
    }

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

}
