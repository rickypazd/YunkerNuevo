/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADOR;

import Conexion.Conexion;
import Conexion.SngleCon;
import MODELO.ALMACEN.ALMACEN;
import MODELO.ALMACEN.CARDEX;
import MODELO.AUTO.REP_AUTO;
import MODELO.AUTO.REP_AUTO_MARCA;
import MODELO.COMPRA.COMPRA;
import MODELO.COMPRA.COMPRA_DETALLE;
import MODELO.COMPRA.VENTA;
import MODELO.COMPRA.VENTA_DETALLE;
import MODELO.INDICADORES.IndicadorINEG;
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
@WebServlet(name = "indicadoresController", urlPatterns = {"/indicadoresController"})

public class indicadoresController extends HttpServlet {

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
                //<editor-fold defaultstate="collapsed" desc="INDICADORES">
                case "getIndicadorINEG":
                    html = getIndicadorINEG(request, con);
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

    private String getIndicadorINEG(HttpServletRequest request, Conexion con) {
        String nameAlert = "indicador INEG";
        try {
            IndicadorINEG indicadorINEG = new IndicadorINEG(con);
            
            RESPUESTA resp = new RESPUESTA(1, "", nameAlert + " registrado con exito.", indicadorINEG.getJson());
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al registrar " + nameAlert + ".", "{}");
            return resp.toString();
        } catch (JSONException ex) {
            Logger.getLogger(indicadoresController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al registrar " + nameAlert + ".", "{}");
            return resp.toString();
        }
    }

    private String realizar_venta(HttpServletRequest request, Conexion con) {
        String nameAlert = "venta";
        try {
            String codigo = pString(request, "codigo");
            String nombre_cliente = pString(request, "nombre_cliente");
            String nit = pString(request, "nit");
            String fecha = pString(request, "fecha");
            double total = pDouble(request, "total");
            int id_admin = pInt(request, "id_admin");
            int id_almacen = pInt(request, "id_almacen");
            String arr_detalle = pString(request, "arr_detalle");
            VENTA venta = new VENTA(con);
            //TODO: parsear fecha
            SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
            int id = venta.Insertar(codigo, form.parse(fecha), nit, nombre_cliente, total, id_admin);
            JSONArray arr = new JSONArray(arr_detalle);
            VENTA_DETALLE venta_detalle = new VENTA_DETALLE(con);
            CARDEX cardex = new CARDEX(con);
            JSONObject temp;
            int id_com_detalle;
            for (int i = 0; i < arr.length(); i++) {
                temp = arr.getJSONObject(i);
                id_com_detalle = venta_detalle.Insertar(id, temp.getString("nombre"), temp.getString("descripcion"), temp.getInt("cantidad"), temp.getDouble("precio"), temp.getDouble("subTotal"), 1, temp.getInt("id_articulo"));

                cardex.VenderRepuesto(id_com_detalle, temp.getInt("cantidad"), temp.getInt("id_articulo"));

            }

            RESPUESTA resp = new RESPUESTA(1, "", nameAlert + " registrado con exito.", "");
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al registrar " + nameAlert + ".", "{}");
            return resp.toString();
        } catch (JSONException ex) {
            Logger.getLogger(indicadoresController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al registrar " + nameAlert + ".", "{}");
            return resp.toString();
        } catch (ParseException ex) {
            Logger.getLogger(indicadoresController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al parsear fecha", "{}");
            return resp.toString();
        }
    }

}