/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.excepciones;

/**
 *
 * @author jpverdezoto
 */
public class ConsultarException extends Exception {

    public ConsultarException(String message, Throwable cause) {
        super("Error al ejecutar la consulta :\n" + message, cause);
    }

    public ConsultarException(String message) {
        super("Error al ejecutar la consulta :\n" + message);
    }

}
