/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.beans;

import ec.bomberosquito.ai.entidades.Casos;
import ec.bomberosquito.ai.entidades.Eventos;
import ec.bomberosquito.ai.excepciones.ConsultarException;
import ec.bomberosquito.ai.facades.CasosFacade;
import ec.bomberosquito.ai.facades.EventosFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author jpverdezoto
 */
@Named(value = "revisionCasosBean")
@ViewScoped
public class RevisionCasosBean implements Serializable {

    private List<Casos> listaCasos = new ArrayList<>();
    private Casos caso;
    private Eventos evento;

    @Inject
    private SeguridadBean seguridadBean;

    @EJB
    private CasosFacade ejbCasos;
    @EJB
    private EventosFacade ejbEventos;

    @PostConstruct

    public void init() {
        listaCasos.clear();
        cargarCasos();

    }

    public void cargarCasos() {
        HashMap paremetros = new HashMap<>();
        paremetros.put(";where", "o.estado=:estado");
        paremetros.put("estado", "PARA REVISION");
        try {
            listaCasos = ejbCasos.encontrarParametros(paremetros);
        } catch (ConsultarException e) {
        }
    }

    public void regresarCaso() {
        if (caso.getObservaciones() == null || caso.getObservaciones().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Debes llenar la observación"));
            return;
        }
        // implementacion del tracing del caso cuando se devuekve para revison 
        Eventos eventos = new Eventos();
        eventos.setEstado("PENDIENTE REVISION");
        eventos.setCaso(caso);
        eventos.setFechahora(new Date());
        eventos.setAccionrealizada("Director retorna el informe al analista para que realice los cambios indicados " + caso.getId());
        eventos.setComentario(caso.getObservaciones());
        eventos.setAccionante(seguridadBean.getUserLogueado().getTipo());
        ejbEventos.create(eventos);
        caso.setEstado("PENDIENTE REVISION");
        ejbCasos.edit(caso);
        listaCasos.clear();
        cargarCasos();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Informe enviado para realizar cambios por el Analista"));
        PrimeFaces.current().executeScript("PF('manageDialogregresar').hide()");
    }

    public void aprobarcaso() {
        // implementacion de tracking cuando se aprueba un caso
        if (caso.getObservaciones() == null || caso.getObservaciones().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Debes llenar la observación"));
            return;
        }
        Eventos eventos = new Eventos();
        eventos.setEstado("APROBADO");
        eventos.setCaso(caso);
        eventos.setFechahora(new Date());
        eventos.setAccionrealizada("Director aprueba el informe " + caso.getId());
        eventos.setComentario(caso.getObservaciones());
        eventos.setAccionante(seguridadBean.getUserLogueado().getTipo());
        ejbEventos.create(eventos);
        caso.setEstado("APROBADO");
        ejbCasos.edit(caso);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Informe Aprobado por el Director"));
        cargarCasos();
        PrimeFaces.current().executeScript("PF('manageDialogaprobar').hide()");
    }

    public List<Casos> getListaCasos() {
        return listaCasos;
    }

    public void setListaCasos(List<Casos> listaCasos) {
        this.listaCasos = listaCasos;
    }

    public CasosFacade getEjbCasos() {
        return ejbCasos;
    }

    public void setEjbCasos(CasosFacade ejbCasos) {
        this.ejbCasos = ejbCasos;
    }

    public Casos getCaso() {
        return caso;
    }

    public void setCaso(Casos caso) {
        this.caso = caso;
    }

    public Eventos getEvento() {
        return evento;
    }

    public void setEvento(Eventos evento) {
        this.evento = evento;
    }

}
