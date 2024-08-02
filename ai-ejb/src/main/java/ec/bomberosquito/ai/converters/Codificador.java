/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.converters;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Codificador {

    public Codificador() {
    }

    public String getEncoded(String texto, String algoritmo) {
        String output = "";
        try {

            byte[] textBytes = texto.getBytes();
            MessageDigest md = MessageDigest.getInstance(algoritmo);
            md.update(textBytes);
            byte[] codigo = md.digest();
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < codigo.length; i++) {
                String hex = Integer.toHexString(0xFF & codigo[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            output = hexString.toString();

        } catch (NoSuchAlgorithmException ex) {
            //Logger.getLogger(Usuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    

    }
}
