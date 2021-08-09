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
import MODELO.COMPRA.DEVOLUCION;
import MODELO.COMPRA.VENTA;
import MODELO.COMPRA.VENTA_DETALLE;
import MODELO.COMPRA.VENTA_PLAN_CUENTA;
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
@WebServlet(name = "comprasController", urlPatterns = {"/admin/comprasController"})

public class comprasController extends HttpServlet {

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
                //<editor-fold defaultstate="collapsed" desc="COMPRA">
                case "realizar_compra":
                    html = realizar_compra(request, con);
                    break;
                case "realizar_venta":
                    html = realizar_venta(request, con);
                    break;
                case "devolverArticulo":
                    html = devolverArticulo(request, con);
                    break;
                case "get_venta_id":
                    html = get_venta_id(request, con);
                    break;
                case "get_compra_id":
                    html = get_compra_id(request, con);
                    break;
                case "getVentasPagination":
                    html = getVentasPagination(request, con);
                    break;
                case "getVentasCuotasPagination":
                    html = getVentasCuotasPagination(request, con);
                    break;
                case "getComprasPagination":
                    html = getComprasPagination(request, con);
                    break;
                case "getGananciaTotal":
                    html = getGananciaTotal(request, con);
                    break;
                case "pagar_cuota":
                    html = pagar_cuota(request, con);
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

    private String realizar_compra(HttpServletRequest request, Conexion con) {
        String nameAlert = "compra";
        try {
            String codigo = pString(request, "codigo");
            String nombre_proveedor = pString(request, "nombre_proveedor");
            String fecha = pString(request, "fecha");
            double total = pDouble(request, "total");
            int id_admin = pInt(request, "id_admin");

            String arr_detalle = pString(request, "arr_detalle");
            COMPRA compra = new COMPRA(con);
            //TODO: parsear fecha
            SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
            int id = compra.Insertar(codigo, form.parse(fecha), nombre_proveedor, total, id_admin);
            JSONArray arr = new JSONArray(arr_detalle);
            COMPRA_DETALLE compra_detalle = new COMPRA_DETALLE(con);
            CARDEX cardex = new CARDEX(con);
            JSONObject temp;
            int id_com_detalle;
            for (int i = 0; i < arr.length(); i++) {
                temp = arr.getJSONObject(i);
                id_com_detalle = compra_detalle.Insertar(id, temp.getString("nombre"), temp.getString("descripcion"), temp.getInt("cantidad"), temp.getDouble("precio"), temp.getDouble("subTotal"), 1, temp.getInt("id_articulo"));
                REPUESTO rep = new REPUESTO(con);
                rep.setID(temp.getInt("id_articulo"));
                rep.editarPrecio(temp.getDouble("precioVenta"));
                int id_almacen = temp.getInt("id_almacen");
                for (int j = 0; j < temp.getInt("cantidad"); j++) {
                    int id_cardex = cardex.InsertarRepuesto(temp.getInt("id_articulo"));
                    cardex.InsertarMovimiento(id_cardex, 1, id_almacen, id_com_detalle);
                }

            }

            RESPUESTA resp = new RESPUESTA(1, "", nameAlert + " registrado con exito.", id + "");
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al registrar " + nameAlert + ".", "{}");
            return resp.toString();
        } catch (JSONException ex) {
            Logger.getLogger(comprasController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al registrar " + nameAlert + ".", "{}");
            return resp.toString();
        } catch (ParseException ex) {
            Logger.getLogger(comprasController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al parsear fecha", "{}");
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

            int cuotas = pInt(request, "cuotas");

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
                id_com_detalle = venta_detalle.Insertar(id, temp.getString("nombre"), temp.getString("descripcion"), temp.getInt("cantidad"), temp.getDouble("precio"), temp.getDouble("subTotal"), 1, temp.getInt("id_articulo"), temp.getInt("descuento"));

                cardex.VenderRepuesto(id_com_detalle, temp.getInt("cantidad"), temp.getInt("id_articulo"), id_admin);

            }
            if (cuotas > 1) {

                String arr_cuotoas = pString(request, "plandetalle");
                JSONArray arrCuotas = new JSONArray(arr_cuotoas);
                VENTA_PLAN_CUENTA venta_plan_cuenta = new VENTA_PLAN_CUENTA(con);
                for (int i = 0; i < arrCuotas.length(); i++) {
                    temp = arrCuotas.getJSONObject(i);
                    venta_plan_cuenta.Insertar(temp.getInt("numero"), temp.getString("fecha"), temp.getDouble("cuota"), id);

                }

            }

            RESPUESTA resp = new RESPUESTA(1, "", nameAlert + " registrado con exito.", id + "");
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al registrar " + nameAlert + ".", "{}");
            return resp.toString();
        } catch (JSONException ex) {
            con.rollback();
            Logger.getLogger(comprasController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al registrar " + nameAlert + ".", "{}");
            return resp.toString();
        } catch (ParseException ex) {
            con.rollback();
            Logger.getLogger(comprasController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al parsear fecha", "{}");
            return resp.toString();
        }
    }

    private String getVentasPagination(HttpServletRequest request, Conexion con) {
        String nameAlert = "ventas";
        try {
            int pagina = pInt(request, "pagina");
            int cantidad = pInt(request, "cantidad");
            String busqueda = pString(request, "busqueda");
            String fecha = pString(request, "fecha");
            String fechaFin = pString(request, "fechaFin");
            VENTA venta = new VENTA(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", venta.getPaginationJSON(pagina, cantidad, busqueda, fecha, fechaFin));
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
    private String getVentasCuotasPagination(HttpServletRequest request, Conexion con) {
        String nameAlert = "ventas";
        try {
            int pagina = pInt(request, "pagina");
            int cantidad = pInt(request, "cantidad");
            String busqueda = pString(request, "busqueda");
            VENTA venta = new VENTA(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", venta.getPaginationCuotasJSON(pagina, cantidad, busqueda));
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

    private String getComprasPagination(HttpServletRequest request, Conexion con) {
        String nameAlert = "compras";
        try {
            int pagina = pInt(request, "pagina");
            int cantidad = pInt(request, "cantidad");
            String busqueda = pString(request, "busqueda");
            String fecha = pString(request, "fecha");
            String fechaFin = pString(request, "fechaFin");
            COMPRA compra = new COMPRA(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", compra.getPaginationJSON(pagina, cantidad, busqueda, fecha, fechaFin));
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

    private String get_venta_id(HttpServletRequest request, Conexion con) {
        String nameAlert = "ventas";
        try {
            int id = pInt(request, "id");

            VENTA venta = new VENTA(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", venta.getById(id));
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

    private String get_compra_id(HttpServletRequest request, Conexion con) {
        String nameAlert = "compras";
        try {
            int id = pInt(request, "id");

            COMPRA compra = new COMPRA(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", compra.getById(id));
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

    private String getGananciaTotal(HttpServletRequest request, Conexion con) {
        String nameAlert = "ventas";
        try {
            String fecha = pString(request, "fecha");
            String fechaFin = pString(request, "fechaFin");
            VENTA venta = new VENTA(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", venta.gerGananciaTotal(fecha, fechaFin));
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

    private String devolverArticulo(HttpServletRequest request, Conexion con) {
        String nameAlert = "devolucion";
        try {
            String obj = pString(request, "obj");
            JSONObject objJson = new JSONObject(obj);
            JSONObject temp = objJson;
            int id_admin = pInt(request, "id_admin");
            VENTA_DETALLE venta_detalle = new VENTA_DETALLE(con);
            int id_ref = venta_detalle.Insertar(
                    temp.getInt("id_venta"),
                    temp.getString("nombre"),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                    temp.getInt("cantidad"),
                    temp.getDouble("precio") * -1,
                    temp.getInt("cantidad")*temp.getDouble("precio")* -1,
                    1,
                    temp.getInt("id_articulo"),
                    0
            );
            venta_detalle.ajustarPlanDePagos(temp.getInt("id_venta"),  temp.getInt("cantidad")*temp.getDouble("precio"));
            CARDEX cardex = new CARDEX(con);

            cardex.devolverRepuesto(temp.getInt("id"), id_admin, temp.getInt("cantidad"), id_ref);

//HACER LA DEVOLUCION COMO UNA COMPRA PERO AGREGARLA A VENTA DETALLE EL CARDEX TIPO ES 2 DE DEVOLUCION
            DEVOLUCION devolucion = new DEVOLUCION(con);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", devolucion.toString());
            return resp.toString();
        } catch (JSONException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al convertir " + nameAlert + " a JSON.", "{}");
            return resp.toString();
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al convertir " + nameAlert + " a JSON.", "{}");
            return resp.toString();
        }
    }

    private String pagar_cuota(HttpServletRequest request, Conexion con) {
        String nameAlert = "pagar";
        try {
            int id = pInt(request, "id");
            String consulta = "UPDATE public.venta_plan_cuenta\n"
                    + "	SET fecha_on=now() \n"
                    + "	WHERE id = "+id;
            con.EjecutarUpdate(consulta);
            RESPUESTA resp = new RESPUESTA(1, "", "Exito.", "exito");
            return resp.toString();
        } catch (SQLException ex) {
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al convertir " + nameAlert + " a JSON.", "{}");
            return resp.toString();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al convertir " + nameAlert + " a JSON.", "{}");
            return resp.toString();
        } catch (IllegalAccessException ex) {
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al convertir " + nameAlert + " a JSON.", "{}");
            return resp.toString();
        } catch (InstantiationException ex) {
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            RESPUESTA resp = new RESPUESTA(0, ex.getMessage(), "Error al convertir " + nameAlert + " a JSON.", "{}");
            return resp.toString();
        }
    }

}
