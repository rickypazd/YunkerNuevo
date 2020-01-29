/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UTILES;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ricardopazdemiquel
 */
public class RESPUESTA {

    private int estado;
    private String error;
    private String mensaje;
    private String resp;

    public RESPUESTA(int estado, String error, String mensaje, String resp) {
        this.estado = estado;
        this.error = error;
        this.mensaje = mensaje;
        this.resp = resp;
    }

    public JSONObject getJson() {
        JSONObject obj =new JSONObject();
        try {
            obj.put("estado", getEstado());
            obj.put("error", getError());
            obj.put("mensaje", getMensaje());
            obj.put("resp", getResp());
        } catch (JSONException ex) {
            Logger.getLogger(RESPUESTA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }

    @Override
    public String toString() {
          JSONObject obj =new JSONObject();
        try {
            obj.put("estado", getEstado());
            obj.put("error", getError());
            obj.put("mensaje", getMensaje());
            obj.put("resp", getResp());
        } catch (JSONException ex) {
            Logger.getLogger(RESPUESTA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj.toString();
    }

}
