/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.beans;

import ec.bomberosquito.ai.entidades.Casos;
import ec.bomberosquito.ai.entidades.Eventos;
import ec.bomberosquito.ai.entidades.Usuarios;
import ec.bomberosquito.ai.excepciones.ConsultarException;
import ec.bomberosquito.ai.facades.CasosFacade;
import ec.bomberosquito.ai.facades.EventosFacade;
import ec.bomberosquito.ai.facades.PersonasFacade;
import ec.bomberosquito.ai.facades.UsuariosFacade;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

@Named(value = "casosAsignacionBean")
@ViewScoped
public class CasosAsignacionBean implements Serializable {
    
    private Casos caso;
    private Usuarios responsable;
    private List<Usuarios> listaResponsables;
    private List<Casos> listaCasos;
    
    @EJB
    private CasosFacade ejbCasos;

    @EJB
    private PersonasFacade ejbPersonas;

    @EJB
    private UsuariosFacade ejbUsuarios;
    
    @EJB
    private EventosFacade ejbEventos;
    
    @PostConstruct
    public void init() {
        try {
            listaCasos = ejbCasos.obtenerCasosPorEstado("INICIO");
        } catch (ConsultarException ex) {
            Logger.getLogger(CasosAsignacionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            listaResponsables = ejbUsuarios.obtenerUsuariosPorTipo("ANALISTA");
        } catch (ConsultarException ex) {
            Logger.getLogger(CasosAsignacionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void guardarCaso() {
        if (responsable != null) {
            caso.setResponsable(responsable);
            caso.setEstado("ASIGNADO");
            ejbCasos.edit(caso);

            // Crear evento
            Eventos evento = new Eventos();
            evento.setFechahora(new Date());
            evento.setCaso(caso);
            evento.setAccionrealizada("Director asigna el caso");
            evento.setEstado("ASIGNADO");
            ejbEventos.create(evento);
            
            listaCasos.remove(caso);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Caso asignado correctamente."));
            caso = new Casos();
            PrimeFaces.current().executeScript("PF('dialogoGestionCaso').hide()");
            PrimeFaces.current().ajax().update("form:messages", "form:dt-casos");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debes seleccionar un responsable.", null));
        }
    }

    public void archivarCaso() {
        caso.setEstado("ARCHIVADO");
        ejbCasos.edit(caso);

        // Crear evento
        Eventos evento = new Eventos();
        evento.setFechahora(new Date());
        evento.setCaso(caso);
        evento.setAccionrealizada("Director archiva el caso");
        evento.setEstado("ARCHIVADO");
        ejbEventos.create(evento);
        listaCasos.remove(caso);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Caso archivado correctamente."));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-casos");
        caso = new Casos();
    }

    // Getters y Setters

    public Casos getCaso() {
        return caso;
    }

    public void setCaso(Casos caso) {
        this.caso = caso;
    }

    public Usuarios getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuarios responsable) {
        this.responsable = responsable;
    }

        public List<Usuarios> getListaResponsables() {
        return listaResponsables;
    }

    public void setListaResponsables(List<Usuarios> listaResponsables) {
        this.listaResponsables = listaResponsables;
    }

    public List<Casos> getListaCasos() {
        return listaCasos;
    }

    public void setListaCasos(List<Casos> listaCasos) {
        this.listaCasos = listaCasos;
    }
}
