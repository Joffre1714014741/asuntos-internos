/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.auxiliares;

import java.util.regex.Pattern;

/**
 *
 * @author jpverdezoto
 */
public class Validadores {
    
    
    public boolean validarCorreo(String correo) {
        // Expresión regular para validar el formato de un correo electrónico
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

        // Validar que el correo no sea nulo y coincida con la expresión regular
        if (correo == null) {
            return false;
        }
        return Pattern.matches(emailRegex, correo);
    } 
}
