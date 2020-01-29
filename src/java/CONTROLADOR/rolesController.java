/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADOR;

import Conexion.Conexion;
import Conexion.SngleCon;

import MODELO.PERMISO;

import MODELO.ROL;
import MODELO.ROL_TO_PERMISO;
import UTILES.URL;

import java.io.*;

import java.nio.file.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@WebServlet(name = "rolesController", urlPatterns = {"/rolesController"})
public class rolesController extends HttpServlet {

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
        try {
            Conexion con = SngleCon.getCon();
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain");
            String evento = request.getParameter("evento");
            boolean retornar = true;
            String html = "";
            switch (evento) {
                case "get_roles":
                    html = get_roles(request, con);
                    break;
                case "get_permisos_id_rol":
                    html = get_permisos_id_rol(request, con);
                    break;
                case "get_permisos":
                    html = get_permisos(request, con);
                    break;
                case "nuevo_permiso":
                    html = nuevo_permiso(request, con);
                    break;
                case "nuevo_rol":
                    html = nuevo_rol(request, con);
                    break;
                case "agg_permiso_a_rol":
                    html = agg_permiso_a_rol(request, con);
                    break;
                case "get_permisos_dis_rol":
                    html = get_permisos_dis_rol(request, con);
                    break;
                case "remover_permiso":
                    html = remover_permiso(request, con);
                    break;
                case "eliminar_permiso":
                    html = eliminar_permiso(request, con);
                    break;
                case "get_rol_por_nombre":
                    html = get_rol_por_nombre(request, con);
                    break;

            }
//            con.Close();
            if (retornar) {
                response.getWriter().write(html);
            }
        } catch (SQLException ex) {
            Logger.getLogger(rolesController.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write("falso");
        } catch (JSONException ex) {
            Logger.getLogger(rolesController.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write("falso");
        } catch (Exception ex) {
            Logger.getLogger(rolesController.class.getName()).log(Level.SEVERE, null, ex);
        }

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

    private String get_roles(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        ROL rol = new ROL(con);
        return rol.get_roles().toString();
    }

    private String get_permisos_id_rol(HttpServletRequest request, Conexion con) throws JSONException, SQLException, JSONException {
        int id = Integer.parseInt(request.getParameter("id"));
        PERMISO permiso = new PERMISO(con);
        return permiso.todos_de_rol(id).toString();

    }

    private String get_permisos(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        PERMISO per = new PERMISO(con);
        return per.todos().toString();
    }

    private String nuevo_permiso(HttpServletRequest request, Conexion con) throws SQLException {
        String nombre = request.getParameter("nombre");
        PERMISO per = new PERMISO(con);
        per.setNOMBRE(nombre);
        return per.Insertar() + "";
    }

    private String nuevo_rol(HttpServletRequest request, Conexion con) throws SQLException {
        String nombre = request.getParameter("nombre");
        ROL rol = new ROL(con);
        rol.setNOMBRE(nombre);
        return rol.Insertar() + "";
    }

    private String agg_permiso_a_rol(HttpServletRequest request, Conexion con) throws JSONException, SQLException {
        int id_rol = Integer.parseInt(request.getParameter("id"));
        JSONArray arr = new JSONArray(request.getParameter("jsonarr"));
        ROL_TO_PERMISO rtp = new ROL_TO_PERMISO(con);
        for (int i = 0; i < arr.length(); i++) {
            rtp.setID_ROL(id_rol);
            rtp.setID_PERMISO(arr.getJSONObject(i).getInt("id"));
            rtp.Insertar();
        }
        return "exito";
    }

    private String get_permisos_dis_rol(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int id_rol = Integer.parseInt(request.getParameter("id"));
        PERMISO per = new PERMISO(con);
        return per.todos_dis_de_rol(id_rol).toString();
    }

    private String remover_permiso(HttpServletRequest request, Conexion con) throws SQLException {
        int id_rol = Integer.parseInt(request.getParameter("id_rol"));
        int id_permiso = Integer.parseInt(request.getParameter("id_permiso"));
        ROL_TO_PERMISO rtp = new ROL_TO_PERMISO(con);
        rtp.setID_ROL(id_rol);
        rtp.setID_PERMISO(id_permiso);
        rtp.eliminar();
        return "exito";
    }

    private String eliminar_permiso(HttpServletRequest request, Conexion con) throws SQLException {
       int id = Integer.parseInt(request.getParameter("id"));
       PERMISO per = new PERMISO(con);
       per.setID(id);
       return per.eliminar()+"";
    }

    private String get_rol_por_nombre(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        String nombre = request.getParameter("nombre");
        ROL rol = new ROL(con);
       return rol.get_rol_por_nombre(nombre).toString();
    }

}
