/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.convertidores;

import ec.bomberosquito.ai.beans.UsuariosBean;
import ec.bomberosquito.ai.entidades.Usuarios;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.faces.convert.Converter;

/**
 *
 * @author jpverdezoto
 */
//Creacion del convertidor//

@FacesConverter(forClass = Usuarios.class)
public class analistas implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Usuarios codigo = null;
        UsuariosBean bean = (UsuariosBean) context.getELContext().getELResolver().getValue(context.getELContext(), null, "usuariosBean");
        Integer id = Integer.parseInt(value);
        codigo=bean.traer(id);
        return codigo;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Usuarios codigo = (Usuarios)value;
        if(codigo.getId()==null){
            return "0";
        }
        return codigo.getId().toString();
    }

}

