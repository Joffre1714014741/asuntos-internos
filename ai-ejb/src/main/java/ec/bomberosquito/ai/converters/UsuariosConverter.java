/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ec.bomberosquito.ai.entidades.Usuarios;
import ec.bomberosquito.ai.facades.UsuariosFacade;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.convert.ConverterException;
import javax.inject.Named;

/**
 *
 * @author danielhwang
 */
@Named
@FacesConverter(value = "usuariosConverter")
public class UsuariosConverter implements Converter {

    @EJB
    private UsuariosFacade ejbUsuarios;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null; // Manejar el caso en el que el valor es nulo o vacío.
        }

        try {
            UsuariosConverterBean usuariosConverterBean = context.getApplication().evaluateExpressionGet(context, "#{usuariosConverterBean}", UsuariosConverterBean.class);
            return usuariosConverterBean.find(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de conversión", "Valor no válido"));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof Usuarios) {
            return String.valueOf(((Usuarios) value).getId());
        }
        return null;
    }

}
