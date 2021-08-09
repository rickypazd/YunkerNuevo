/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 * @author ricardopazdemiquel
 */
public class FtpClient {

    private static String server = "localhost";
    private static int port = 21;
    private static String user = "ftpuser";
    private static String password = "ftpuser";
    private static FTPClient ftp;
    private static String nombre_code;

    // constructor
    public static FTPClient open() {

        if (ftp == null || !ftp.isConnected()) {
            try {
                ftp = new FTPClient();
//                ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

                ftp.connect(server, port);

//                int reply = ftp.getReplyCode();
                ftp.login(user, password);
                ftp.enterLocalPassiveMode();
                ftp.setFileType(FTP.BINARY_FILE_TYPE);

            } catch (IOException ex) {
                ftp = null;
                Logger.getLogger(FtpClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ftp;

    }

    public static void downloadFolder(String remotePath, String localPath) throws IOException {
        open();
        System.out.println("Descargando folder " + remotePath + " a " + localPath);
        FTPFile[] remoteFiles = ftp.listFiles(remotePath);

        for (FTPFile remoteFile : remoteFiles) {
            if (!remoteFile.getName().equals(".") && !remoteFile.getName().equals("..")) {
                String remoteFilePath = remotePath + "/" + remoteFile.getName();
                String localFilePath = localPath + "/" + remoteFile.getName();

                if (remoteFile.isDirectory()) {
                    new File(localFilePath).mkdirs();
                    downloadFolder(remoteFilePath, localFilePath);
                } else {
                    File fileExist = new File(localFilePath);
                    if (!fileExist.isFile()) {
                        System.out.println("Descargando file " + remoteFilePath + " a " + localFilePath);
                        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localFilePath));
                        if (!ftp.retrieveFile(remoteFilePath, outputStream)) {
                            System.out.println("fallo al descargar file " + remoteFilePath);
                        }
                        outputStream.close();
                    } else {
                        System.out.println("Encontrado el file " + localFilePath);
                    }
                }
            }
        }
       
    }

    public static String subirArchibo(Part file, String url, String nombre) throws IOException {
        FtpClient.open();
 
        int dbs = 2048;
        boolean isDir;
        
        isDir = ftp.changeWorkingDirectory("/");
        System.out.println(ftp.printWorkingDirectory());
        String arr[] = url.split("/");
//        String[] names = ftp.listNames();
//        for (String name : names) {
//            System.out.println("Name = " + name);
//        }

        // ftp.enterLocalPassiveMode();
        for (int i = 0; i < arr.length; i++) {
            isDir = ftp.changeWorkingDirectory(arr[i]);
            if (!isDir) {

                isDir = ftp.makeDirectory(arr[i]);
                isDir = ftp.changeWorkingDirectory(arr[i]);
            }
        }

        nombre_code = java.util.UUID.randomUUID().toString();
        nombre = nombre.toLowerCase();
        nombre = nombre.trim();

        String extension = "";

        int inx = nombre.lastIndexOf('.');
        if (inx >= 0) {
            extension = nombre.substring(inx + 1);
        }

        nombre_code += "." + extension;

        String[] nombres = ftp.listNames();
        String nombreFinal = isExist(nombre_code, nombres);
//        File f = new File(url + "/" + nombre_code);

//        int contador = 0;
//        while (f.exists()) {
//            contador++;
//            nombre = contador + nombre_code;
//            f = new File(url + "/" + nombre_code);
//        }
        ftp.storeFile(nombreFinal, file.getInputStream());
         close();
        return nombreFinal;
    }

    private static String isExist(String nombre, String[] nombres) {
        for (int i = 0; i < nombres.length; i++) {
            if (nombre.equals(nombres[i])) {
                isExist(i + nombre, nombres);

            }
        }
        return nombre;
    }

    public static void close() throws IOException {
        ftp.logout();
        ftp.disconnect();
        ftp = null;
    }
}
