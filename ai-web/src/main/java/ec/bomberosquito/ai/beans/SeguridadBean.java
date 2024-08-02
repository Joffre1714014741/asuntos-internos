/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.beans;

import ec.bomberosquito.ai.entidades.Usuarios;
import ec.bomberosquito.ai.excepciones.ConsultarException;
import ec.bomberosquito.ai.facades.SeguridadLogeoFacade;
import ec.bomberosquito.auxiliares.AuxiliarUsuario;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author jpverdezoto
 */
@ManagedBean(name = "seguridad")
@SessionScoped
public class SeguridadBean implements Serializable {

    
    @EJB
    SeguridadLogeoFacade ejbSeguridad;

    private String txtrecomendacion;
     
    public SeguridadBean() {
    }

    @PostConstruct
    public void init() {
        txtrecomendacion = "<p>Se recomienda el uso de navegadores : <b>Google Chrome, </b> <b>Safari ,</b> <b>Opera</b></p>";
    }

    private String usuario;
    private String password;
    private Usuarios usuarios = new Usuarios();
    private AuxiliarUsuario entidad;
    private String UsuarioLogeado;

    // Creación de método para logear al usuario
    public String probar() throws ConsultarException, IOException {
        
        if ((usuario == null) || (usuario.trim().isEmpty())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ingresa usuario"));
            return null;
        }
        if ((password == null) || (password.trim().isEmpty())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ingresa password"));
            return null;

        }
        usuarios = ejbSeguridad.logear(usuario, password);
        if (usuarios == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No hay registros para ese usuario"));
            return null;
        } else {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
           // externalContext.redirect(externalContext.getRequestContextPath() + "/dashboard.xhtml");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("El usuario se encuentra registrado"));
        }
        return "/dashboard.xhtml?faces-redirect=true";
       
    }
    
    public String analista() throws ConsultarException, IOException {
        if ((usuario == null) || (usuario.trim().isEmpty())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ingresa usuario"));
            return null;
        }
        if ((password == null) || (password.trim().isEmpty())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ingresa password"));
            return null;

        }
        usuarios = ejbSeguridad.logear(usuario, password);
        if (usuarios == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No hay registros para ese usuario"));
            return null;
        } else {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
           // externalContext.redirect(externalContext.getRequestContextPath() + "/dashboard.xhtml");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("El usuario se encuentra registrado"));
        }
        return "/dashboard_Analista.xhtml?faces-redirect=true";
    }
    
    public String logout(){
        return "/login.xhtml?faces-redirect=true";
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

   

    public void setTxtrecomendacion(String txtrecomendacion) {
        this.txtrecomendacion = txtrecomendacion;
    }

    public AuxiliarUsuario getEntidad() {
        return entidad;
    }

    public void setEntidad(AuxiliarUsuario entidad) {
        this.entidad = entidad;
    }

    public String getUsuarioLogeado() {
        return UsuarioLogeado;
    }

    public void setUsuarioLogeado(String UsuarioLogeado) {
        this.UsuarioLogeado = UsuarioLogeado;
    }

}
