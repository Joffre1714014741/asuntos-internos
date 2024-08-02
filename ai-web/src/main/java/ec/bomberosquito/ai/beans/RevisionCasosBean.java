/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.beans;

import ec.bomberosquito.ai.entidades.Casos;
import ec.bomberosquito.ai.entidades.Eventos;
import ec.bomberosquito.ai.excepciones.ConsultarException;
import ec.bomberosquito.ai.facades.CasosFacade;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author jpverdezoto
 */
@Named
@SessionScoped
public class RevisionCasosBean implements Serializable {

    private List<Casos> listaCasos = new ArrayList<>();
    private Casos caso = new Casos();
    private Eventos evento = new Eventos();
    private List<Casos> listaCasosSeleccionados;
    @EJB
    private CasosFacade ejbCasos;

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

    public String modificar(Casos casoparametro) {       
        this.caso = casoparametro;
        System.out.println("nombre  : " + caso.getInvolucrado().getNombres());
        return "/revision-casos.xhtml?faces-redirect=true";
    }
    
    
//     public void modificar() throws IOException {
//        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//try {
//            externalContext.redirect(externalContext.getRequestContextPath() + "/revision-casos.xhtml?faces-redirect=true");
//            //externalContext.getFlash().setKeepMessages(true); // Opcional: conserva los mensajes de FacesContext
//
//            // Actualizar o recargar la vista
//            externalContext.getApplicationMap().put("forceReload", true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void realizarCambiosInforme() {
        caso.setEstado("PENDIENTE REVISION");
        ejbCasos.edit(caso);
        listaCasos.clear();
                cargarCasos();

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Informe enviado para realizar cambios"));
    }

    public void aprobarInforme() {
        caso.setEstado("APROBADO");
        ejbCasos.edit(caso);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Informe Aprobado"));
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

    public List<Casos> getListaCasosSeleccionados() {
        return listaCasosSeleccionados;
    }

    public void setListaCasosSeleccionados(List<Casos> listaCasosSeleccionados) {
        this.listaCasosSeleccionados = listaCasosSeleccionados;
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
