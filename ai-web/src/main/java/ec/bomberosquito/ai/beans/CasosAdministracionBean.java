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
import ec.bomberosquito.ai.facades.UsuariosFacade;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author jpverdezoto
 */

@Named(value = "casosAdministracionBean")
@ViewScoped
public class CasosAdministracionBean implements Serializable {

    private Usuarios usuarioSeleccionado;
    private String nombre;
    private Usuarios user;
    private String estadoCasos;
    private String observacion = "";

    public Usuarios getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuarios usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private Casos caso = new Casos();
    private Eventos evento = new Eventos();

    private List<Casos> listaCasos;
    private List<Casos> listaCasosSeleccionados;

    private Integer idSolicitud;

    @EJB
    private CasosFacade ejbCasos;

    @EJB
    private UsuariosFacade ejbUsuarios;
    
    @EJB
    private EventosFacade ejbEventos;

    @PostConstruct
    public void init() {
       cargarCasos();

    }
    
    public void cargarCasos(){
       HashMap paremetros = new HashMap<>();
        paremetros.put(";where", "o.estado=:estado");
        paremetros.put("estado", "CREADO");
        try {
            listaCasos = ejbCasos.encontrarParametros(paremetros);
        } catch (ConsultarException e) {
        }
    }

    public void casoNuevo() {
        caso = new Casos();
    }

    public SelectItem[] getCargarInvestigadores() throws ConsultarException {

        HashMap paremetros = new HashMap<>();
        paremetros.put(";where", "o.tipo=:tipo");
        paremetros.put("tipo", "INVESTIGADOR");
        List<Usuarios> listaInvestigadores = ejbUsuarios.encontrarParametros(paremetros);
        int size = listaInvestigadores.size() + 1;
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        items[0] = new SelectItem(null, "---");
        i++;
        for (Usuarios objus : listaInvestigadores) {
            items[i++] = new SelectItem(objus,
                    (objus.getPersona().getNombres() != null ? objus.getPersona().getNombres() : "")
                    + " " + (objus.getPersona().getApellidos() != null ? objus.getPersona().getApellidos() : ""));
        }

        return items;
    }
    
   

    public void asignarCaso() {
        System.out.println("obejto usuarios " + user);

        System.out.println("entro");
        System.out.println("caoso" + caso.getId());
//        if (usuarioSeleccionado == null) {
//            System.out.println("usuario null");
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("selecione investigador"));
//        } else {
//            System.out.println("usuasiro no null");
//            caso.setResponsable(usuarioSeleccionado);
//            ejbCasos.edit(caso);
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("caso asigando"));
//        }
    }

    public void guardarCaso() {
        if (caso.getId() == null) {
            ejbCasos.create(caso);
            listaCasos.add(caso);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Caso Creado"));
        } else {
            ejbCasos.edit(caso);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Caso Guardado"));
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
        for (Casos caso : listaCasosSeleccionados) {
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

    public void inicioCaso() {
        // Editar caso
        caso.setEstado(estadoCasos);
        System.out.println("observaciones " + caso.getObservaciones());
        ejbCasos.edit(caso);
        //fin editar caso
        // crear tracking
        evento.setFechahora(new Date());
        evento.setEstado(estadoCasos);
        evento.setAccionrealizada("Director Inicia el la Investigación");
        evento.setCaso(caso);
        ejbEventos.create(evento);
        // finizaiza tracking
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Caso editado"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-casos");
        PrimeFaces.current().executeScript("PF('dialogoGestionCaso').hide()");

    }
    
    
    public void modificar(Casos casoparametro) {
        caso = casoparametro;
        PrimeFaces.current().executeScript("PF('dialogoGestionCaso').show()");
        
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

    public Integer getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Usuarios getUser() {
        return user;
    }

    public void setUser(Usuarios user) {
        this.user = user;
    }

    public String getEstadoCasos() {
        return estadoCasos;
    }

    public void setEstadoCasos(String estadoCasos) {
        this.estadoCasos = estadoCasos;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Eventos getEvento() {
        return evento;
    }

    public void setEvento(Eventos evento) {
        this.evento = evento;
    }

}
