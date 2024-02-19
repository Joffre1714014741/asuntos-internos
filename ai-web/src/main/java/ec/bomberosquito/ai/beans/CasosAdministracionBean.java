/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.beans;

import ec.bomberosquito.ai.entidades.Casos;
import ec.bomberosquito.ai.facades.CasosFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

/**
 *
 * @author danielhwang
 */

@Named
@ViewScoped
public class CasosAdministracionBean implements Serializable {
    private Casos caso;
    private List<Casos> listaCasos;
    private List<Casos> listaCasosSeleccionados;
    
    @EJB
    private CasosFacade ejbCasos;
    
    @PostConstruct
    public void init() {
       listaCasos = ejbCasos.findAll();
    }
    
    public void casoNuevo() {
        caso = new Casos();
    }
    
    public void guardarCaso() {
        if (caso.getId() == null) {
            ejbCasos.create(caso);
            listaCasos.add(caso);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Caso creado"));
        }
        else {
            ejbCasos.edit(caso);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Caso actualizado"));
        }
        
        PrimeFaces.current().executeScript("PF('dialogoGestionCaso').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-casos");
    }
    
    public boolean hayCasosSeleccionados() {
        return this.listaCasosSeleccionados != null && !this.listaCasosSeleccionados.isEmpty();
    }
    
    public String getMensajeBotonEliminar() {
        if (hayCasosSeleccionados()) {
            int size = this.listaCasosSeleccionados.size();
            return size > 1 ? size + " casos seleccionados" : "1 caso seleccionado";
        }
        return "Eliminar";
    }
    
    public void eliminarCasosSeleccionados() {
        for(Casos caso : listaCasosSeleccionados){
            ejbCasos.remove(caso);
        }
        this.listaCasos.removeAll(this.listaCasosSeleccionados);
        this.listaCasosSeleccionados = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Casos eliminados"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-casos");
    }
    
    public void eliminarCaso() {
        ejbCasos.remove(caso);
        this.listaCasos.remove(this.caso);
        this.listaCasosSeleccionados = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Caso eliminado"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-casos");
    }

    public Casos getCaso() {
        return caso;
    }

    public void setCaso(Casos caso) {
        this.caso = caso;
    }

    public List<Casos> getListaCasos() {
        return listaCasos;
    }

    public void setListaCasos(List<Casos> listaCasos) {
        this.listaCasos = listaCasos;
    }

    public List<Casos> getListaCasosSeleccionados() {
        return listaCasosSeleccionados;
    }

    public void setListaCasosSeleccionados(List<Casos> listaCasosSeleccionados) {
        this.listaCasosSeleccionados = listaCasosSeleccionados;
    }
}
    