package ec.bomberosquito.ai.beans;

import ec.bomberosquito.ai.entidades.Personas;
import ec.bomberosquito.ai.entidades.Usuarios;
import ec.bomberosquito.ai.facades.PersonasFacade;
import ec.bomberosquito.ai.facades.UsuariosFacade;
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

@Named
@ViewScoped
public class UsuariosBean implements Serializable {

    private Personas persona;
    private Usuarios usuario;
    private List<Personas> listaPersonas;
    private List<Personas> listaPersonasSeleccionadas;
    private List<Usuarios> listaUsuarios;
    private List<Usuarios> listaUsuariosSeleccionados;
    @EJB
    private PersonasFacade ejbPersonas;
    @EJB
    private UsuariosFacade ejbUsuarios;
    
    @PostConstruct
    public void init() {
       listaPersonas = ejbPersonas.findAll();
       listaUsuarios = ejbUsuarios.findAll();
    }
    
    public void personaNueva() {
        persona = new Personas();
    }
    
    public void guardarPersona() {
        if (persona.getId() == null) {
            ejbPersonas.create(persona);
            listaPersonas.add(persona);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Persona creada"));
        }
        else {
            ejbPersonas.edit(persona);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Persona actualizada"));
        }
        
        PrimeFaces.current().executeScript("PF('dialogoGestionPersona').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-personas");
    }
    
    public boolean hayPersonasSeleccionadas() {
        return this.listaPersonasSeleccionadas != null && !this.listaPersonasSeleccionadas.isEmpty();
    }
    
    public String getMensajeBotonEliminar() {
        if (hayPersonasSeleccionadas()) {
            int size = this.listaPersonasSeleccionadas.size();
            return size > 1 ? size + " personas seleccionadas" : "1 persona seleccionada";
        }
        return "Eliminar";
    }
    
    public void eliminarPersonasSeleccionadas() {
        for(Personas persona : listaPersonasSeleccionadas){
            ejbPersonas.remove(persona);
        }
        this.listaPersonas.removeAll(this.listaPersonasSeleccionadas);
        this.listaPersonasSeleccionadas = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Perfiles de persona eliminados"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-personas");
    }
    
    public void eliminarPersona() {
        ejbPersonas.remove(persona);
        this.listaPersonas.remove(this.persona);
        this.listaPersonasSeleccionadas = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Perfil de persona eliminado"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-personas");
    }
    
    public void onRowToggle(ToggleEvent event) {
        if (event.getVisibility() == Visibility.VISIBLE) {
            Personas persona = (Personas) event.getData();
        }
    }
    
    public void usuarioNuevo() {
        usuario = new Usuarios();
    }

//    public void guardarUsuario() {
//        if (usuario.getId() == null) {
//            ejbUsuarios.create(usuario);
//            listaUsuarios.add(usuario);
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario creado"));
//        } else {
//            ejbUsuarios.edit(usuario);
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario actualizado"));
//        }
//
//        PrimeFaces.current().executeScript("PF('dialogoGestionUsuario').hide()");
//        PrimeFaces.current().ajax().update("form:messages", "form:dt-usuarios");
//    }
   
    public void guardarUsuario() {
        if (usuario.getId() == null) {
            if (usuario.getPersona() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debes seleccionar una persona.", null));
                return; // Detiene el proceso de guardado si no se ha seleccionado una persona.
            }
            ejbUsuarios.create(usuario);
            listaUsuarios.add(usuario);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario creado"));
        } else {
            ejbUsuarios.edit(usuario);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario actualizado"));
        }

        PrimeFaces.current().executeScript("PF('dialogoGestionUsuario').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-usuarios");
    }

    public boolean hayUsuariosSeleccionados() {
        return this.listaUsuariosSeleccionados != null && !this.listaUsuariosSeleccionados.isEmpty();
    }

    public String getMensajeBotonEliminarUsuario() {
        if (hayUsuariosSeleccionados()) {
            int size = this.listaUsuariosSeleccionados.size();
            return size > 1 ? size + " usuarios seleccionados" : "1 usuario seleccionado";
        }
        return "Eliminar";
    }

    public void eliminarUsuariosSeleccionados() {
        for (Usuarios usuario : listaUsuariosSeleccionados) {
            ejbUsuarios.remove(usuario);
        }
        this.listaUsuarios.removeAll(this.listaUsuariosSeleccionados);
        this.listaUsuariosSeleccionados = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuarios eliminados"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-usuarios");
    }

    public void eliminarUsuario() {
        ejbUsuarios.remove(usuario);
        this.listaUsuarios.remove(this.usuario);
        this.listaUsuariosSeleccionados = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario eliminado"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-usuarios");
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

    public List<Personas> getListaPersonas() {
        return listaPersonas;
    }

    public void setListaPersonas(List<Personas> listaPersonas) {
        this.listaPersonas = listaPersonas;
    }

    public List<Usuarios> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public List<Personas> getListaPersonasSeleccionadas() {
        return listaPersonasSeleccionadas;
    }

    public void setListaPersonasSeleccionadas(List<Personas> listaPersonasSeleccionadas) {
        this.listaPersonasSeleccionadas = listaPersonasSeleccionadas;
    }

    public List<Usuarios> getListaUsuariosSeleccionados() {
        return listaUsuariosSeleccionados;
    }

    public void setListaUsuariosSeleccionados(List<Usuarios> listaUsuariosSeleccionados) {
        this.listaUsuariosSeleccionados = listaUsuariosSeleccionados;
    }

    public PersonasFacade getEjbPersonas() {
        return ejbPersonas;
    }
    
}
