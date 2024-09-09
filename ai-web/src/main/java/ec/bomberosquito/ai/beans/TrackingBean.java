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
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author jpverdezoto
 */
@Named(value = "trackingBean")
@ViewScoped
public class TrackingBean implements Serializable {

    @EJB
    private EventosFacade ejbEventos;
    
    @EJB
    private CasosFacade ejbCasos;

    private List<Eventos> listaEventosTraking = new ArrayList();
    private List<Casos> listaCasos = new ArrayList();
    private Integer casoParametro;

    private Casos caso;

    public void cargarEvento(Integer id) {
        try {

            System.out.println("parametro de busqueda" + id);
            HashMap paremetros = new HashMap<>();
            paremetros.put(";where", "o.caso.id=:jofre");
            paremetros.put("jofre", id);
            paremetros.put(";orden","o.id desc");
            listaEventosTraking = ejbEventos.encontrarParametros(paremetros);
            System.out.println("tmanio de la lista" + listaEventosTraking.size());
        } catch (ConsultarException ex) {
            Logger.getLogger(TrackingBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void cargarCaso() {
        try {
            HashMap paremetros = new HashMap<>();
            paremetros.put(";where", "o.id=:caso");
            paremetros.put("caso", casoParametro);
            listaCasos = ejbCasos.encontrarParametros(paremetros);
                    } catch (ConsultarException ex) {
            Logger.getLogger(TrackingBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Integer getCasoParametro() {
        return casoParametro;
    }

    public void setCasoParametro(Integer casoParametro) {
        this.casoParametro = casoParametro;
    }

    public List<Eventos> getListaEventosTraking() {

        return listaEventosTraking;
    }

    public void setListaEventosTraking(List<Eventos> listaEventosTraking) {
        this.listaEventosTraking = listaEventosTraking;
    }

    public List<Casos> getListaCasos() {
        return listaCasos;
    }

    public void setListaCasos(List<Casos> listaCasos) {
        this.listaCasos = listaCasos;
    }

    public Casos getCaso() {
        return caso;
    }

    public void setCaso(Casos caso) {
        this.caso = caso;
    }

}
