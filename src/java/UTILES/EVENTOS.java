package UTILES;


import Conexion.FtpClient;
import java.io.*;
import javax.servlet.http.Part;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author RICKY
 */
public class EVENTOS {
                    
    public static String guardar_file(Part file, String rootPath ,String url, String nombre) throws IOException {
        String nombreFTP = FtpClient.subirArchibo(file, url, nombre);
        int dbs = 2048;
        File d = new File(rootPath+url);
        if (!d.exists()) {
            d.mkdirs();
        }
       

        File f = new File(rootPath+"/"+url + "/" + nombreFTP);
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new BufferedInputStream(file.getInputStream(), dbs);
            out = new BufferedOutputStream(new FileOutputStream(f), dbs);
            byte[] bufer = new byte[dbs];
            for (int i = 0; ((i = in.read(bufer)) > 0);) {
                out.write(bufer, 0, i);
            }
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
                try {
                    in.close();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        }
        return nombreFTP;
    }
    public static String guardar_fileb64(String file, String url, String nombre) throws IOException {
        int dbs = 2048;
        File d = new File(url);
        if (!d.exists()) {
            d.mkdirs();
        }
        String nombre_code = java.util.UUID.randomUUID().toString();
        nombre = nombre.toLowerCase();
        nombre = nombre.trim();

        String extension = "";

        int inx = nombre.lastIndexOf('.');
        if (inx >= 0) {
            extension = nombre.substring(inx + 1);
        }

        nombre_code += "." + extension;

        File f = new File(url + "/" + nombre_code);
        int contador = 0;
        while (f.exists()) {
            contador++;
            nombre = contador + nombre_code;
            f = new File(url + "/" + nombre_code);
        }
        InputStream in = null;
        OutputStream out = null;

        try {
            
            out = new BufferedOutputStream(new FileOutputStream(f), dbs);
            byte[] bufer = Base64.decodeBase64(file);
            
                out.write(bufer);
            
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            
            }
        }
        return nombre_code;
    }
    
    public static String obtener_file(String url) throws IOException {
        File file = new File(url);
        String encodedBase64 = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);

            encodedBase64 = new String(Base64.encodeBase64(bytes));
        } catch (Exception e) {
            System.out.println(e);
        }

        return encodedBase64;
    }

    public static String obtener_file_cuadrado(String url, String nombre) throws IOException {
        File file = new File(url + URL.barra + nombre);
        String encodedBase64 = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);

            encodedBase64 = new String(Base64.encodeBase64(bytes));
        } catch (Exception e) {
            System.out.println(e);
        }

        return encodedBase64;
    }

}
