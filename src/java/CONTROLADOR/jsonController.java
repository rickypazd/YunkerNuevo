/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADOR;

import Conexion.Conexion;
import MODELO.CONSULTA;

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
@WebServlet(name = "jsonController", urlPatterns = {"/admin/jsonController"})
public class jsonController extends HttpServlet {

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
            //conexion linux
            
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain");
            String evento = request.getParameter("evento");
            boolean retornar = true;
            String html = "";
            switch (evento) {
                case "login":
                    html = login(request);
                    break;
                case "insertar":
                    html = insertar(request);
                    break;
                case "actualizar":
                    html = actualizar(request);
                    break;
                case "selectAll":
                    html = selectAll(request);
                    break;
                case "getByWheres":
                    html = getByWheres(request);
                    break;
                case "getBySelectWheres":
                    html = getBySelectWheres(request);
                    break;
               
            }

            //con.Close();
            if (retornar) {
                response.getWriter().write(html);
            }
        } //        catch (SQLException ex) {
        //            con.rollback();
        //            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
        //            response.getWriter().write("falso");
        //        } catch (ParseException ex) {
        //            con.rollback();
        //            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
        //            response.getWriter().write("falso");
        //        } catch (JSONException ex) {
        //            con.rollback();
        //            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
        //            response.getWriter().write("falso");
        //        } 
        catch (Exception ex) {

            Logger.getLogger(jsonController.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write("error");
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

    private String insertar(HttpServletRequest request) throws JSONException {
        JSONObject obj = new JSONObject(request.getParameter("json"));
        return CONSULTA.insertarArray(obj);
    }
    private String actualizar(HttpServletRequest request) throws JSONException {
        JSONObject obj = new JSONObject(request.getParameter("json"));
        return CONSULTA.updateArray(obj);
    }

    private String selectAll(HttpServletRequest request) throws JSONException {
        JSONObject obj = new JSONObject(request.getParameter("json"));
        return CONSULTA.selectAll(obj);
    }
    private String getByWheres(HttpServletRequest request) throws JSONException {
        JSONObject obj = new JSONObject(request.getParameter("json"));
        return CONSULTA.getByWheres(obj);
    }
    private String getBySelectWheres(HttpServletRequest request) throws JSONException {
        JSONObject obj = new JSONObject(request.getParameter("json"));
        return CONSULTA.getBySelectWheres(obj);
    }

    

    private String login(HttpServletRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
