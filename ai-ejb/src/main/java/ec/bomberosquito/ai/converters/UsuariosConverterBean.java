/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.converters;

import ec.bomberosquito.ai.entidades.Usuarios;
import javax.ejb.EJB;
import javax.inject.Named;
import java.io.Serializable;
import ec.bomberosquito.ai.facades.UsuariosFacade;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author danielhwang
 */

@Named(value = "usuariosConverterBean")
@RequestScoped
public class UsuariosConverterBean implements Serializable {
    
    @EJB
    private UsuariosFacade ejbUsuarios;
    
    public Usuarios find(Object id) {
        return ejbUsuarios.find(id);
    }
    
}
