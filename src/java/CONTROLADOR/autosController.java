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
import MODELO.AUTO.REP_AUTO_MODELO;
import MODELO.AUTO.REP_AUTO_VERSION;
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
@WebServlet(name = "autosController", urlPatterns = {"/autosController"})

public class autosController extends HttpServlet {

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
                //<editor-fold defaultstate="collapsed" desc="REP_AUTO">
                case "registrar_rep_auto":
                    html = registrar_rep_auto(request, con);
                    break;
                case "agg_auto_to_rep":
                    html = agg_auto_to_rep(request, con);
                    break;
                case "eliminar_auto_to_rep":
                    html = eliminar_auto_to_rep(request, con);
                    break;
                case "editar_rep_auto":
                    html = editar_rep_auto(request, con);
                    break;
                case "eliminar_rep_auto":
                    html = eliminar_rep_auto(request, con);
                    break;
                case "getAll_rep_auto_paginationJSON":
                    html = getAll_rep_auto_paginationJSON(request, con);
                    break;
                case "getAll_autos_disponibles":
                    html = getAll_autos_disponibles(request, con);
                    break;
                case "getAll_autos_activos":
                    html = getAll_autos_activos(request, con);
                    break;

                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="REP_AUTO_MARCA">
                case "registrar_rep_auto_marca":
                    html = registrar_rep_auto_marca(request, con);
                    break;
                case "editar_rep_auto_marca":
                    html = editar_rep_auto_marca(request, con);
                    break;
                case "eliminar_rep_auto_marca":
                    html = eliminar_rep_auto_marca(request, con);
                    break;
                case "subir_foto_rep_auto_marca":
                    html = subir_foto_rep_auto_marca(request, con);
                    break;
                case "getById_rep_auto_marca":
                    html = getById_rep_auto_marca(request, con);
                    break;
                case "getAll_rep_auto_marca":
                    html = getAll_rep_auto_marca(request, con);
                    break;
                case "getAll_rep_auto_marca_paginationJSON":
                    html = getAll_rep_auto_marca_paginationJSON(request, con);
                    break;

                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="REP_AUTO_MODELO">
                case "registrar_rep_auto_modelo":
                    html = registrar_rep_auto_modelo(request, con);
                    break;
                case "editar_rep_auto_modelo":
                    html = editar_rep_auto_modelo(request, con);
                    break;
                case "getById_rep_auto_modelo":
                    html = getById_rep_auto_modelo(request, con);
                    break;
                case "subir_foto_rep_auto_modelo":
                    html = subir_foto_rep_auto_modelo(request, con);
                    break;
                case "get_rep_auto_modelo_by_id_rep_auto_marca":
                    html = get_rep_auto_modelo_by_id_rep_auto_marca(request, con);
                    break;
                case "get_rep_auto_modelo_by_id_rep_auto_marca_registrados":
                    html = get_rep_auto_modelo_by_id_rep_auto_marca_registrados(request, con);
                    break;
                case "getAll_rep_auto_modelo_paginationJSON":
                    html = getAll_rep_auto_modelo_paginationJSON(request, con);
                    break;

                case "eliminar_rep_auto_modelo":
                    html = eliminar_rep_auto_modelo(request, con);
                    break;

                //</editor-fold>  
                //<editor-fold defaultstate="collapsed" desc="REP_AUTO_VERSION">
                case "registrar_rep_auto_version":
                    html = registrar_rep_auto_version(request, con);
                    break;
                case "editar_rep_auto_version":
                    html = editar_rep_auto_version(request, con);
                    break;
                case "get_rep_auto_version_by_id_rep_auto_modelo":
                    html = get_rep_auto_version_by_id_rep_auto_modelo(request, con);
                    break;
                case "eliminar_rep_auto_version":
                    html = eliminar_rep_auto_version(request, con);
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

    //<editor-fold defaultstate="collapsed" desc="REP_AUTO">
    private String registrar_rep_auto(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto";
        try {
            REP_AUTO rep_auto = new REP_AUTO(con);
            rep_auto.setANHO(pString(request, "anho"));
            rep_auto.setCLAVE(pString(request, "clave"));
            String versiones = pString(request, "versiones");
            JSONArray arr_versiones = new JSONArray(versiones);

            JSONObject obj_version;
            for (int i = 0; i < arr_versiones.length(); i++) {
                obj_version = arr_versiones.getJSONObject(i);
                rep_auto.setID_VERSION(obj_version.getInt("id"));
                rep_auto.Insertar();
            }
            RESPUESTA resp = new RESPUESTA(1, "", nameAlert + " registrado con exito.", rep_auto.getJson().toString());
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

    private String agg_auto_to_rep(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto";
        try {
            int id_auto = pInt(request, "id_auto");
            int id_rep = pInt(request, "id_rep");
            REP_AUTO rep_auto = new REP_AUTO(con);
            rep_auto.agg_auto_to_rep(id_auto, id_rep);
            RESPUESTA resp = new RESPUESTA(1, "", nameAlert + " registrado con exito.", rep_auto.getVehiculosActivos(id_rep, ""));
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

    private String eliminar_auto_to_rep(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto";
        try {
            int id_auto = pInt(request, "id_auto");
            int id_rep = pInt(request, "id_rep");
            REP_AUTO rep_auto = new REP_AUTO(con);
            rep_auto.eliminar_auto_to_rep(id_auto, id_rep);
            RESPUESTA resp = new RESPUESTA(1, "", nameAlert + " registrado con exito.", "{}");
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al registrar " + nameAlert + ".", "{}");
            return resp.toString();
        }
    }

    private String eliminar_rep_auto(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto";
        try {
            int id = pInt(request, "id");
            REP_AUTO rep_auto = new REP_AUTO(con);
            rep_auto.setID(id);
            rep_auto.eliminar();
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_auto.getJson().toString());
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

    private String editar_rep_auto(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto";
        try {
            int id = pInt(request, "id");
            String nombre = pString(request, "nombre");
            REP_AUTO_MARCA rep_auto_marca = new REP_AUTO_MARCA(con);
            rep_auto_marca.setID(id);
            rep_auto_marca.setNOMBRE(nombre);
            rep_auto_marca.editar();
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_auto_marca.getJson().toString());
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

    private String getAll_rep_auto_paginationJSON(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto";
        try {
            int pagina = pInt(request, "pagina");
            int cantidad = pInt(request, "cantidad");
            String busqueda = pString(request, "busqueda");
            REP_AUTO rep_auto = new REP_AUTO(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_auto.getPaginationJSON(pagina, cantidad, busqueda));
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

    private String getAll_autos_disponibles(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto";
        try {
            int id = pInt(request, "id");
            String busqueda = pString(request, "busqueda");
            REP_AUTO rep_auto = new REP_AUTO(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_auto.getVehiculosDisponibles(id, busqueda));
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

    private String getAll_autos_activos(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto";
        try {
            int id = pInt(request, "id");
            String busqueda = pString(request, "busqueda");
            REP_AUTO rep_auto = new REP_AUTO(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_auto.getVehiculosActivos(id, busqueda));
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
    //<editor-fold defaultstate="collapsed" desc="REP_AUTO_MARCA">
    private String registrar_rep_auto_marca(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto_marca";
        try {
            REP_AUTO_MARCA rep_auto_marca = new REP_AUTO_MARCA(con);
            rep_auto_marca.setNOMBRE(pString(request, "nombre"));
            int id = rep_auto_marca.Insertar();
            rep_auto_marca.setID(id);
            Part file = request.getPart("foto");
            String name = "";
            String names = "";
            if (file != null) {
                names = file.getSubmittedFileName();
                String ruta = request.getSession().getServletContext().getRealPath("/");

                name = EVENTOS.guardar_file(file, ruta, URL.ruta_foto_rep_auto_marca + "/" + id + "/", names);
            }
            rep_auto_marca.setURL_FOTO(URL.ruta_foto_rep_auto_marca + "/" + id + "/" + name);

            rep_auto_marca.subir_foto_perfil();
            RESPUESTA resp = new RESPUESTA(1, "", nameAlert + " registrado con exito.", rep_auto_marca.getJson().toString());
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

    private String eliminar_rep_auto_marca(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto_marca";
        try {
            int id = pInt(request, "id");
            REP_AUTO_MARCA rep_auto_marca = new REP_AUTO_MARCA(con);
            rep_auto_marca.setID(id);
            rep_auto_marca.eliminar();
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_auto_marca.getJson().toString());
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

    private String editar_rep_auto_marca(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto_marca";
        try {
            int id = pInt(request, "id");
            String nombre = pString(request, "nombre");
            REP_AUTO_MARCA rep_auto_marca = new REP_AUTO_MARCA(con);
            rep_auto_marca.setID(id);
            rep_auto_marca.setNOMBRE(nombre);
            rep_auto_marca.editar();
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_auto_marca.getJson().toString());
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

    private String subir_foto_rep_auto_marca(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto_marca";
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Part file = request.getPart("archibo");
            String name = "";
            String names = "";
            if (file != null) {
                names = file.getSubmittedFileName();
                String ruta = request.getSession().getServletContext().getRealPath("/");

                name = EVENTOS.guardar_file(file, ruta, URL.ruta_foto_rep_auto_marca + "/" + id + "/", names);
            }
            REP_AUTO_MARCA rep_auto_marca = new REP_AUTO_MARCA(con);
            rep_auto_marca.setURL_FOTO(URL.ruta_foto_rep_auto_marca + "/" + id + "/" + name);
            rep_auto_marca.setID(id);
            rep_auto_marca.subir_foto_perfil();
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", "{}");
            return resp.toString();
        } catch (IOException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al obtener " + nameAlert + ".", "{}");
            return resp.toString();
        } catch (ServletException ex) {
            con.rollback();
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al obtener " + nameAlert + ".", "{}");
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al obtener " + nameAlert + ".", "{}");
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            return resp.toString();
        }
    }

    private String getById_rep_auto_marca(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto_marca";
        try {
            int id = pInt(request, "id");
            REP_AUTO_MARCA rep_auto_marca = new REP_AUTO_MARCA(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_auto_marca.getById(id).toString());
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

    private String getAll_rep_auto_marca(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto_marca";
        try {

            REP_AUTO_MARCA rep_auto_marca = new REP_AUTO_MARCA(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_auto_marca.getAll().toString());
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

    private String getAll_rep_auto_marca_paginationJSON(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto_marca";
        try {
            int pagina = pInt(request, "pagina");
            int cantidad = pInt(request, "cantidad");
            String busqueda = pString(request, "busqueda");
            REP_AUTO_MARCA rep_auto_marca = new REP_AUTO_MARCA(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_auto_marca.getPaginationJson(pagina, cantidad, busqueda));
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
    //<editor-fold defaultstate="collapsed" desc="REP_AUTO_MODELO">
    private String registrar_rep_auto_modelo(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto_modelo";
        try {
            REP_AUTO_MODELO rep_auto_modelo = new REP_AUTO_MODELO(con);
            rep_auto_modelo.setNOMBRE(pString(request, "nombre"));
            rep_auto_modelo.setID_REP_AUTO_MARCA(pInt(request, "id_rep_auto_marca"));
            int id = rep_auto_modelo.Insertar();
            rep_auto_modelo.setID(id);
            Part file = request.getPart("foto");
            String name = "";
            String names = "";
            if (file != null) {
                names = file.getSubmittedFileName();
                String ruta = request.getSession().getServletContext().getRealPath("/");
                name = EVENTOS.guardar_file(file, ruta, URL.ruta_foto_rep_auto_modelo + "/" + id + "/", names);
            }
            rep_auto_modelo.setURL_FOTO(URL.ruta_foto_rep_auto_modelo + "/" + id + "/" + name);
            rep_auto_modelo.subir_foto_perfil();
            RESPUESTA resp = new RESPUESTA(1, "", nameAlert + " registrado con exito.", rep_auto_modelo.getJson().toString());
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

    private String editar_rep_auto_modelo(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto_modelo";
        try {
            int id = pInt(request, "id");
            String nombre = pString(request, "nombre");
            REP_AUTO_MODELO rep_auto_modelo = new REP_AUTO_MODELO(con);
            rep_auto_modelo.setID(id);
            rep_auto_modelo.setNOMBRE(nombre);
            rep_auto_modelo.editar();
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_auto_modelo.getJson().toString());
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

    private String eliminar_rep_auto_modelo(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto_modelo";
        try {
            int id = pInt(request, "id");
            REP_AUTO_MODELO rep_auto_modelo = new REP_AUTO_MODELO(con);
            rep_auto_modelo.setID(id);
            rep_auto_modelo.eliminar();
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_auto_modelo.getJson().toString());
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

    private String subir_foto_rep_auto_modelo(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto_modelo";
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Part file = request.getPart("archibo");
            String name = "";
            String names = "";
            if (file != null) {
                names = file.getSubmittedFileName();
                String ruta = request.getSession().getServletContext().getRealPath("/");
                name = EVENTOS.guardar_file(file, ruta, URL.ruta_foto_rep_auto_modelo + "/" + id + "/", names);
            }
            REP_AUTO_MODELO rep_auto_modelo = new REP_AUTO_MODELO(con);
            rep_auto_modelo.setID(id);
            rep_auto_modelo.setURL_FOTO(URL.ruta_foto_rep_auto_modelo + "/" + id + "/" + name);
            rep_auto_modelo.subir_foto_perfil();
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", "{}");
            return resp.toString();
        } catch (IOException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al obtener " + nameAlert + ".", "{}");
            return resp.toString();
        } catch (ServletException ex) {
            con.rollback();
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al obtener " + nameAlert + ".", "{}");
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al obtener " + nameAlert + ".", "{}");
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            return resp.toString();
        }
    }

    private String getById_rep_auto_modelo(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto_modelo";
        try {
            int id = pInt(request, "id");
            REP_AUTO_MODELO rep_auto_modelo = new REP_AUTO_MODELO(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_auto_modelo.getById(id).toString());
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

    private String getAll_rep_auto_modelo(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto_modelo";
        try {
            REP_AUTO_MODELO rep_auto_modelo = new REP_AUTO_MODELO(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_auto_modelo.getAll().toString());
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

    private String get_rep_auto_modelo_by_id_rep_auto_marca(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto_modelo";
        try {
            REP_AUTO_MODELO rep_auto_modelo = new REP_AUTO_MODELO(con);
            int id_rep_auto_marca = pInt(request, "id");
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_auto_modelo.getBy_id_rep_auto_marca(id_rep_auto_marca).toString());
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

    private String get_rep_auto_modelo_by_id_rep_auto_marca_registrados(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto_modelo";
        try {
            REP_AUTO_MODELO rep_auto_modelo = new REP_AUTO_MODELO(con);
            int id_rep_auto_marca = pInt(request, "id");
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_auto_modelo.getBy_id_rep_auto_marca_registrados(id_rep_auto_marca).toString());
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

    private String getAll_rep_auto_modelo_paginationJSON(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto_modelo";
        try {
            int pagina = pInt(request, "pagina");
            int cantidad = pInt(request, "cantidad");
            String busqueda = pString(request, "busqueda");
            REP_AUTO_MODELO rep_auto_modelo = new REP_AUTO_MODELO(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_auto_modelo.getPaginationJson(pagina, cantidad, busqueda));
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
    //<editor-fold defaultstate="collapsed" desc="REP_AUTO_VERSION">
    private String registrar_rep_auto_version(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto_version";
        try {
            REP_AUTO_VERSION rep_auto_version = new REP_AUTO_VERSION(con);
            rep_auto_version.setNOMBRE(pString(request, "nombre"));
            rep_auto_version.setID_REP_AUTO_MODELO(pInt(request, "id"));
            int id = rep_auto_version.Insertar();
            rep_auto_version.setID(id);
            RESPUESTA resp = new RESPUESTA(1, "", nameAlert + " registrado con exito.", rep_auto_version.getJson().toString());
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

    private String get_rep_auto_version_by_id_rep_auto_modelo(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto_version";
        try {
            REP_AUTO_VERSION rep_auto_version = new REP_AUTO_VERSION(con);
            int id_rep_auto_modelo = pInt(request, "id");
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_auto_version.getBy_id_rep_auto_modelo(id_rep_auto_modelo).toString());
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

    private String editar_rep_auto_version(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto_version";
        try {
            int id = pInt(request, "id");
            String nombre = pString(request, "nombre");
            REP_AUTO_VERSION rep_auto_version = new REP_AUTO_VERSION(con);
            rep_auto_version.setID(id);
            rep_auto_version.setNOMBRE(nombre);
            rep_auto_version.editar();
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_auto_version.getJson().toString());
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

    private String eliminar_rep_auto_version(HttpServletRequest request, Conexion con) {
        String nameAlert = "rep_auto_version";
        try {
            int id = pInt(request, "id");
            REP_AUTO_VERSION rep_auto_version = new REP_AUTO_VERSION(con);
            rep_auto_version.setID(id);
            rep_auto_version.eliminar();
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", rep_auto_version.getJson().toString());
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
//    </editor-fold>
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
