/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.converters;

import ec.bomberosquito.ai.entidades.Personas;
import javax.ejb.EJB;
import javax.inject.Named;
import java.io.Serializable;
import ec.bomberosquito.ai.facades.PersonasFacade;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author danielhwang
 */

@Named(value = "personasConverterBean")
@RequestScoped
public class PersonasConverterBean implements Serializable {
    
    @EJB
    private PersonasFacade ejbPersonas;
    
    public Personas find(Object id) {
        return ejbPersonas.find(id);
    }
    
}
