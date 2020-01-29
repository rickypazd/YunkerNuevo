/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADOR;

import Conexion.Conexion;
import Conexion.FtpClient;
import Conexion.SngleCon;
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
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author RICKY
 */
@MultipartConfig
@WebServlet(name = "repuestosController", urlPatterns = {"/repuestosController"})

public class repuestosController extends HttpServlet {

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
                //<editor-fold defaultstate="collapsed" desc="REPUESTO">
                case "registrar_repuesto":
                    html = registrar_repuesto(request, con);
                    break;
                case "registrar_repuesto_parte_esquema":
                    html = registrar_repuesto_parte_esquema(request, con);
                    break;
                case "registrar_repuesto_existente_parte_esquema":
                    html = registrar_repuesto_existente_parte_esquema(request, con);
                    break;
                case "registrar_repuesto_parte":
                    html = registrar_repuesto_parte(request, con);
                    break;
                case "getRepuestoByIdJson":
                    html = getRepuestoByIdJson(request, con);
                    break;
                case "subirFotoEsquema":
                    html = subirFotoEsquema(request, con);
                    break;
                case "getAll_repuesto_paginationJSON":
                    html = getAll_repuesto_paginationJSON(request, con);
                    break;
                case "getAll_repuesto_partes_paginationJSON":
                    html = getAll_repuesto_partes_paginationJSON(request, con);
                    break;
               
                case "eliminar_repuesto":
                    html = eliminar_repuesto(request, con);
                    break;
//</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="REP_CATEGORIA">
                case "registrar_rep_categoria":
                    html = registrar_rep_categoria(request, con);
                    break;
                case "getAll_rep_categoria":
                    html = getAll_rep_categoria(request, con);
                    break;
                case "getAll_rep_categoriaJSON":
                    html = getAll_rep_categoriaJSON(request, con);
                    break;
                case "getAll_rep_categoria_de_vehiculo":
                    html = getAll_rep_categoria_de_vehiculo(request, con);
                    break;
                case "eliminar_rep_categoria":
                    html = eliminar_rep_categoria(request, con);
                    break;

                //</editor-fold>         
                //<editor-fold defaultstate="collapsed" desc="REP_SUB_CATEGORIA">
                case "registrar_rep_sub_categoria":
                    html = registrar_rep_sub_categoria(request, con);
                    break;
                case "getAll_rep_sub_categoria":
                    html = getAll_rep_sub_categoria(request, con);
                    break;
                case "get_rep_sub_categoria_by_id_rep_categoria":
                    html = get_rep_sub_categoria_by_id_rep_categoria(request, con);
                    break;
                case "get_rep_sub_categoria_by_id_rep_categoria_de_id_vehiculo":
                    html = get_rep_sub_categoria_by_id_rep_categoria_de_id_vehiculo(request, con);
                    break;
                case "eliminar_rep_sub_categoria":
                    html = eliminar_rep_sub_categoria(request, con);
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
    //<editor-fold defaultstate="collapsed" desc="REPUESTO">

    private String registrar_repuesto(HttpServletRequest request, Conexion con) {
        String nameAlert = "repuesto";
        try {
            REPUESTO repuesto = new REPUESTO(con);

            repuesto.setID_REP_SUB_CATEGORIA(pInt(request, "id_sub_categoria"));
            repuesto.setNOMBRE(pString(request, "nombre"));
            repuesto.setPRECIO(pDouble(request, "precio"));
            repuesto.setCODIGO(pString(request, "codigo"));
            repuesto.setTIPO(1);
            JSONArray arr = new JSONArray(request.getParameter("list_detalle"));
            int id = repuesto.Insertar();
            repuesto.setID(id);
            REP_DETALLE rep_detalle = new REP_DETALLE(con);
            rep_detalle.setID_REPUESTO(id);
            JSONObject temp;
            for (int i = 0; i < arr.length(); i++) {
                temp = arr.getJSONObject(i);
                rep_detalle.setDETALLE(temp.getString("detalle"));
                rep_detalle.setVALOR(temp.getString("valor"));
                rep_detalle.Insertar();
            }
            Part file = request.getPart("foto");
            String name = "";
            String names = "";
            if (file != null) {
                names = file.getSubmittedFileName();
                String ruta = request.getSession().getServletContext().getRealPath("/");
                name = EVENTOS.guardar_file(file, ruta , URL.ruta_foto_repuesto + "/" + id + "/", names);
            }
            repuesto.setURL_FOTO(URL.ruta_foto_repuesto + "/" + id + "/" + name);
            repuesto.subir_foto_perfil();
            RESPUESTA resp = new RESPUESTA(1, "", nameAlert + " registrado con exito.", repuesto.getJson().toString());
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
        } catch (IOException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al subir foto.", "{}");
            return resp.toString();
        } catch (ServletException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al subir foto.", "{}");
            return resp.toString();
        }
    }
    private String registrar_repuesto_parte_esquema(HttpServletRequest request, Conexion con) {
        String nameAlert = "repuesto";
        try {
            REPUESTO repuesto = new REPUESTO(con);

            repuesto.setID_PADRE(pInt(request, "id_padre"));
            repuesto.setNOMBRE(pString(request, "nombre"));
            repuesto.setPRECIO(pDouble(request, "precio"));
            repuesto.setCODIGO(pString(request, "codigo"));
            repuesto.setTIPO(3);
            
            double x = pDouble(request, "x");
            double y = pDouble(request, "y");
            double width = pDouble(request, "width");
            double height = pDouble(request, "height");
            JSONArray arr = new JSONArray(request.getParameter("list_detalle"));
            int id = repuesto.InsertarHijo();
            repuesto.setID(id);
            repuesto.InsertarPosicionEsquema(id, x, y, width, height);
            REP_DETALLE rep_detalle = new REP_DETALLE(con);
            rep_detalle.setID_REPUESTO(id);
            JSONObject temp;
            for (int i = 0; i < arr.length(); i++) {
                temp = arr.getJSONObject(i);
                rep_detalle.setDETALLE(temp.getString("detalle"));
                rep_detalle.setVALOR(temp.getString("valor"));
                rep_detalle.Insertar();
            }
            
            Part file = request.getPart("foto");
            String name = "";
            String names = "";
            if (file != null) {
                names = file.getName()+".png"; 
                String ruta = request.getSession().getServletContext().getRealPath("/");
                name = EVENTOS.guardar_file(file, ruta , URL.ruta_foto_repuesto + "/" + id + "/", names);
            }
            repuesto.setURL_FOTO(URL.ruta_foto_repuesto + "/" + id + "/" + name);
            repuesto.subir_foto_perfil();
            RESPUESTA resp = new RESPUESTA(1, "", nameAlert + " registrado con exito.", repuesto.getJson().toString());
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
        } catch (IOException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al subir foto.", "{}");
            return resp.toString();
        } catch (ServletException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al subir foto.", "{}");
            return resp.toString();
        }
    }
    private String registrar_repuesto_existente_parte_esquema(HttpServletRequest request, Conexion con) {
        String nameAlert = "repuesto";
        try {
            REPUESTO repuesto = new REPUESTO(con);

            repuesto.setID_PADRE(pInt(request, "id_padre"));
            repuesto.setID(pInt(request, "id_rep"));
            repuesto.setTIPO(3);
            repuesto.editarTipo();
            double x = pDouble(request, "x");
            double y = pDouble(request, "y");
            double width = pDouble(request, "width");
            double height = pDouble(request, "height");
            int id = repuesto.getID();
            repuesto.InsertarPosicionEsquema(id, x, y, width, height);   
            Part file = request.getPart("foto");
            String name = "";
            String names = "";
            if (file != null) {
                names = file.getName()+".png"; 
                String ruta = request.getSession().getServletContext().getRealPath("/");
                name = EVENTOS.guardar_file(file, ruta , URL.ruta_foto_repuesto + "/" + id + "/", names);
            }
            repuesto.setURL_FOTO(URL.ruta_foto_repuesto + "/" + id + "/" + name);
            repuesto.subir_foto_perfil();
            RESPUESTA resp = new RESPUESTA(1, "", nameAlert + " registrado con exito.", repuesto.getJson().toString());
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
        } catch (IOException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al subir foto.", "{}");
            return resp.toString();
        } catch (ServletException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al subir foto.", "{}");
            return resp.toString();
        }
    }
    private String registrar_repuesto_parte(HttpServletRequest request, Conexion con) {
        String nameAlert = "repuesto";
        try {
            REPUESTO repuesto = new REPUESTO(con);

            repuesto.setID_PADRE(pInt(request, "id_padre"));
            repuesto.setNOMBRE(pString(request, "nombre"));
            repuesto.setPRECIO(pDouble(request, "precio"));
            repuesto.setCODIGO(pString(request, "codigo"));
            repuesto.setTIPO(2);
            JSONArray arr = new JSONArray(request.getParameter("list_detalle"));
            int id = repuesto.InsertarHijo();
            repuesto.setID(id);
            REP_DETALLE rep_detalle = new REP_DETALLE(con);
            rep_detalle.setID_REPUESTO(id);
            JSONObject temp;
            for (int i = 0; i < arr.length(); i++) {
                temp = arr.getJSONObject(i);
                rep_detalle.setDETALLE(temp.getString("detalle"));
                rep_detalle.setVALOR(temp.getString("valor"));
                rep_detalle.Insertar();
            }
            Part file = request.getPart("foto");
            String name = "";
            String names = "";
            if (file != null) {
                names = file.getSubmittedFileName();
                String ruta = request.getSession().getServletContext().getRealPath("/");
                name = EVENTOS.guardar_file(file, ruta , URL.ruta_foto_repuesto + "/" + id + "/", names);
            }
            repuesto.setURL_FOTO(URL.ruta_foto_repuesto + "/" + id + "/" + name);
            repuesto.subir_foto_perfil();
            RESPUESTA resp = new RESPUESTA(1, "", nameAlert + " registrado con exito.", repuesto.getJson().toString());
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
        } catch (IOException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al subir foto.", "{}");
            return resp.toString();
        } catch (ServletException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al subir foto.", "{}");
            return resp.toString();
        }
    }

    private String subirFotoEsquema(HttpServletRequest request, Conexion con) {
        String nameAlert = "esquema";
        try {
            int id_usr = Integer.parseInt(request.getParameter("id"));
            Part file = request.getPart("archibo");
            String name = "";
            String names = "";

            if (file != null) {
                names = file.getSubmittedFileName();
                String ruta = request.getSession().getServletContext().getRealPath("/");
                name = EVENTOS.guardar_file(file, ruta , URL.ruta_foto_rep_esquema + "/" + id_usr + "/", names);
            }
            REPUESTO repuesto = new REPUESTO(con);
            repuesto.setID(id_usr);
            repuesto.setURL_FOTO_ESQUEMA(URL.ruta_foto_rep_esquema + "/" + id_usr + "/" + name);
            repuesto.subir_foto_esquema();

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
    private String getById_repuesto(HttpServletRequest request, Conexion con) {
        String nameAlert = "repuesto";
        try {
            int id = pInt(request, "id");
            REPUESTO repuesto = new REPUESTO(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", repuesto.getById(id).toString());
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

    private String getAll_repuesto_paginationJSON(HttpServletRequest request, Conexion con) {
        String nameAlert = "Ventas";
        try {
            int pagina = pInt(request, "pagina");
            int cantidad = pInt(request, "cantidad");
            String busqueda = pString(request, "busqueda");
            REPUESTO repuesto = new REPUESTO(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", repuesto.getPaginationJSON(pagina, cantidad, busqueda));
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
    private String getAll_repuesto_partes_paginationJSON(HttpServletRequest request, Conexion con) {
        String nameAlert = "repuesto";
        try {
            int pagina = pInt(request, "pagina");
            int cantidad = pInt(request, "cantidad");
            int id_padre = pInt(request, "id_padre");
            String busqueda = pString(request, "busqueda");
            REPUESTO repuesto = new REPUESTO(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", repuesto.getPaginationJSONPart(pagina, cantidad, busqueda, id_padre));
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

   
    private String getRepuestoByIdJson(HttpServletRequest request, Conexion con) {
        String nameAlert = "repuesto";
        try {
            REPUESTO repuesto = new REPUESTO(con);
            int id = pInt(request, "id");
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", repuesto.getByIdJson(id) + "");
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

    private String eliminar_repuesto(HttpServletRequest request, Conexion con) {
        String nameAlert = "repuesto";
        try {
            int id = pInt(request, "id");
            REPUESTO repuesto = new REPUESTO(con);
            repuesto.setID(id);
            repuesto.eliminar();
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.","{}");
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al obtener " + nameAlert + ".", "{}");
            return resp.toString();
        } 
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="REP_CATEGORIA">

    private String registrar_rep_categoria(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_categoria";
        try {
            REP_CATEGORIA rep_categoria = new REP_CATEGORIA(con);
            rep_categoria.setNOMBRE(pString(request, "nombre"));
            int id = rep_categoria.Insertar();
            rep_categoria.setID(id);
            RESPUESTA resp = new RESPUESTA(1, "", nameAlert + " registrado con exito.", rep_categoria.getJson().toString());
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

    private String getAll_rep_categoria(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_categoria";
        try {
            REP_CATEGORIA rep_categoria = new REP_CATEGORIA(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_categoria.getAll().toString());
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
    private String getAll_rep_categoriaJSON(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_categoria";
        try {
            REP_CATEGORIA rep_categoria = new REP_CATEGORIA(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_categoria.getAllJSON().toString());
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

    private String getAll_rep_categoria_de_vehiculo(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_categoria";
        try {
            REP_CATEGORIA rep_categoria = new REP_CATEGORIA(con);
            int id_vehiculo = pInt(request, "id_vehiculo");
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_categoria.getAll_by_id_vehiculo(id_vehiculo).toString());
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

    private String eliminar_rep_categoria(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_categoria";
        try {
            int id = pInt(request, "id");
            REP_CATEGORIA rep_categoria = new REP_CATEGORIA(con);
            rep_categoria.setID(id);
            rep_categoria.eliminar();
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_categoria.getJson().toString());
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
    //<editor-fold defaultstate="collapsed" desc="REP_SUB_CATEGORIA">
    private String registrar_rep_sub_categoria(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_sub_categoria";
        try {
            REP_SUB_CATEGORIA rep_sub_categoria = new REP_SUB_CATEGORIA(con);
            rep_sub_categoria.setNOMBRE(pString(request, "nombre"));
            rep_sub_categoria.setID_REP_CATEGORIA(pInt(request, "id_rep_categoria"));
            int id = rep_sub_categoria.Insertar();
            rep_sub_categoria.setID(id);
            RESPUESTA resp = new RESPUESTA(1, "", nameAlert + " registrado con exito.", rep_sub_categoria.getJson().toString());
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

    private String eliminar_rep_sub_categoria(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_sub_categoria";
        try {
            int id = pInt(request, "id");
            REP_SUB_CATEGORIA rep_sub_categoria = new REP_SUB_CATEGORIA(con);
            rep_sub_categoria.setID(id);
            rep_sub_categoria.eliminar();
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_sub_categoria.getJson().toString());
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

    private String getAll_rep_sub_categoria(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_sub_categoria";
        try {
            REP_SUB_CATEGORIA rep_sub_categoria = new REP_SUB_CATEGORIA(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_sub_categoria.getAll().toString());
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

    private String get_rep_sub_categoria_by_id_rep_categoria(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_sub_categoria";
        try {
            REP_SUB_CATEGORIA rep_sub_categoria = new REP_SUB_CATEGORIA(con);
            int id_rep_categoria = pInt(request, "id_rep_categoria");
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_sub_categoria.getBy_id_rep_categoria(id_rep_categoria).toString());
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

    private String get_rep_sub_categoria_by_id_rep_categoria_de_id_vehiculo(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_sub_categoria";
        try {
            REP_SUB_CATEGORIA rep_sub_categoria = new REP_SUB_CATEGORIA(con);
            int id_rep_categoria = pInt(request, "id_rep_categoria");
            int id_vehiculo = pInt(request, "id_vehiculo");
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_sub_categoria.getBy_id_rep_categoria_de_id_vehiculo(id_rep_categoria, id_vehiculo).toString());
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
