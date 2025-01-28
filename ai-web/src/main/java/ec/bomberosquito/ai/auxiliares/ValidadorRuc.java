/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.bomberosquito.ai.auxiliares;

import javax.faces.validator.Validator;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author jpverdezoto
 */
@FacesValidator("ec.bomberosquito.ai.auxiliares.ValidadorRuc")
public class ValidadorRuc implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String cedula = value.toString();

        // Validar que la cédula tenga exactamente 10 dígitos numéricos
        if (!cedula.matches("\\d{10}")) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error de Validación", "La cédula debe contener exactamente 10 dígitos."));
        }

        // Validar la lógica de la cédula (según la normativa local)
        if (!esCedulaValida(cedula)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error de Validación", "El número de cédula ingresado no es válido."));
        }
    }

    private boolean esCedulaValida(String cedula) {
        // Lógica de validación (ejemplo para Ecuador)
        int suma = 0;
        for (int i = 0; i < cedula.length() - 1; i++) {
            int digito = Character.getNumericValue(cedula.charAt(i));
            if (i % 2 == 0) { // Posición par (índice impar)
                digito *= 2;
                if (digito > 9) {
                    digito -= 9;
                }
            }
            suma += digito;
        }
        int digitoVerificador = Character.getNumericValue(cedula.charAt(9));
        int decenaSuperior = ((suma / 10) + 1) * 10;
        int resultado = decenaSuperior - suma;
        return resultado == 10 ? digitoVerificador == 0 : digitoVerificador == resultado;
    }
}
