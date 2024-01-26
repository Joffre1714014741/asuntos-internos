/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.beans;

import ec.bomberosquito.ai.entidades.Personas;
import ec.bomberosquito.ai.entidades.Usuarios;
import ec.bomberosquito.ai.entidades.Casos;
import ec.bomberosquito.ai.facades.PersonasFacade;
import ec.bomberosquito.ai.facades.UsuariosFacade;
import ec.bomberosquito.ai.facades.CasosFacade;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author danielhwang
 */
@Named
@ViewScoped
public class CasosBean implements Serializable {
    
    private Personas persona;
    private Usuarios usuario;
    private Casos caso;

    @EJB
    private PersonasFacade ejbPersonas;

    @EJB
    private UsuariosFacade ejbUsuarios;

    @EJB
    private CasosFacade ejbCasos;

    @PostConstruct
    public void init() {
        persona = new Personas();
        usuario = new Usuarios();
        caso = new Casos();
    }

    public void generarCaso() {
    }

    public void confirmarCaso() {
        
        // Crear persona
        ejbPersonas.create(persona);
        
        // Crear usuario
        usuario.setNombreusuario(persona.getApellidos().toLowerCase() + "." + persona.getNombres());
        usuario.setContrasena(persona.getCedula());
        usuario.setPersona(persona);
        usuario.setTipo("INVOLUCRADO");
        ejbUsuarios.create(usuario);
        
        // Asignar usuario como involucrado en el caso
        caso.setInvolucrado(usuario);
        caso.setEstado("CREADO");
        
        // Persistir el caso
        caso.setFechaderealizacion(new Date());
        ejbCasos.create(caso);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Caso creado exitosamente"));

        // Reiniciar los objetos para nuevos registros
        init();
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Casos getCaso() {
        return caso;
    }

    public void setCaso(Casos caso) {
        this.caso = caso;
    }
    
}
