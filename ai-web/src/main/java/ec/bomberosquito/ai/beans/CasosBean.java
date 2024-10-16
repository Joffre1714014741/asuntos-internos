/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.beans;

import ec.bomberosquito.ai.entidades.Personas;
import ec.bomberosquito.ai.entidades.Usuarios;
import ec.bomberosquito.ai.entidades.Casos;
import ec.bomberosquito.ai.entidades.Documentos;
import ec.bomberosquito.ai.entidades.Eventos;
import ec.bomberosquito.ai.excepciones.ConsultarException;
import ec.bomberosquito.ai.facades.PersonasFacade;
import ec.bomberosquito.ai.facades.CasosFacade;
import ec.bomberosquito.ai.facades.DocumentosFacade;
import ec.bomberosquito.ai.facades.EventosFacade;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import org.primefaces.PrimeFaces;

/**
 *
 * @author jpverdezoto
 */
@Named(value = "casosBean")
@ViewScoped
public class CasosBean implements Serializable {

    private Personas persona;
    private Casos caso;
    private List<Documentos> listaDocumentos;
    private List<Casos> listaCasos;
    private String estadoCasos;
    private UploadedFile file;

    @EJB
    private PersonasFacade ejbPersonas;
    @EJB
    private CasosFacade ejbCasos;
    @EJB
    private DocumentosFacade ejbDocumentos;
    @EJB
    private EventosFacade ejbEventos;

    @PostConstruct
    public void init() {
    }

    public void buscar() {
        HashMap paremetros = new HashMap<>();
        paremetros.put(";where", "o.estado=:estado");
        paremetros.put("estado", "CREADO");
        try {
            setListaCasos(ejbCasos.encontrarParametros(paremetros));
        } catch (ConsultarException e) {
        }
    }

    public void crearCaso() {
        listaDocumentos = new ArrayList<>();
        caso = new Casos();
        persona = new Personas();
        PrimeFaces.current().executeScript("PF('manageDialogcaso').show()");
    }

    public void editarCaso(Casos casoParametro) {
        caso = casoParametro;
        buscarDocumentos();
        PrimeFaces.current().executeScript("PF('manageDialogcasoeditar').show()");
    }

    public void upload() {
        try {
            if (file == null) {
                return;
            }
            System.out.println("paso archivo");
            String filename = file.getFileName();
            Path folder = Paths.get("C:\\Users\\jpverdezoto\\Documents\\documentos");
            Path filePath = Files.createTempFile(folder, filename + "-", ".tmp");
            Files.write(filePath, file.getContent());

            Documentos nuevoDocumento = new Documentos();
            nuevoDocumento.setNombredocumento(filename);
            nuevoDocumento.setRuta(filePath.toString());
            listaDocumentos.add(nuevoDocumento);
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al subir el archivo", null));
        }
    }

    public void eliminarDocumento(Documentos documento) {
        try {
            Path fileToDeletePath = Paths.get(documento.getRuta());
            Files.deleteIfExists(fileToDeletePath);
            listaDocumentos.remove(documento);
            if (documento.getId() != null) {
                ejbDocumentos.remove(documento);
            }
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar el archivo físico", null));
        }
    }

    public void insertarCaso() {
        // Crear persona
        caso.setInvolucrado(verificarPersona(persona));
        caso.setEstado("CREADO");
        // Persistir el caso
        caso.setFechaderealizacion(new Date());
        ejbCasos.create(caso);
        // Ahora, asigna el caso y persiste cada documento
        for (Documentos documento : listaDocumentos) {
            documento.setCaso(caso);
            ejbDocumentos.create(documento);
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Caso y documentos creados exitosamente"));

        Eventos eventoCreacion = new Eventos();
        eventoCreacion.setCaso(caso);
        eventoCreacion.setAccionrealizada("Usuario crea un caso");
        eventoCreacion.setEstado("CASO_CREADO");
        eventoCreacion.setFechahora(new Date());
        ejbEventos.create(eventoCreacion);
        buscar();
        PrimeFaces.current().executeScript("PF('manageDialogcaso').hide()");

    }

    public void grabarCaso() {

        ejbCasos.edit(caso);
        // Ahora, asigna el caso y persiste cada documento
        System.out.println("listaDocumentos" + listaDocumentos.size());
        for (Documentos documento : listaDocumentos) {
            if (documento.getId() == null) {
                System.out.println("creo documento");

                documento.setCaso(caso);
                ejbDocumentos.create(documento);
            }

        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Caso y documentos editadps exitosamente"));

        Eventos eventoCreacion = new Eventos();
        eventoCreacion.setCaso(caso);
        eventoCreacion.setAccionrealizada("Usuario edita un caso");
        eventoCreacion.setEstado("CASO_CREADO");
        eventoCreacion.setFechahora(new Date());
        ejbEventos.create(eventoCreacion);
        PrimeFaces.current().executeScript("PF('manageDialogcasoeditar').hide()");

    }

    public void eliminarCAso() {
        buscarDocumentos();
        for (Documentos doc : listaDocumentos) {
            ejbDocumentos.remove(doc);
        }
        ejbCasos.remove(caso);
        buscar();
        PrimeFaces.current().executeScript("PF('manageDialogeliminar').hide()");

    }

    private void buscarDocumentos() {
        listaDocumentos = new ArrayList<>();
        HashMap paremetros = new HashMap<>();
        paremetros.put(";where", "o.caso=:caso");
        paremetros.put("caso", caso);
        try {
            listaDocumentos = ejbDocumentos.encontrarParametros(paremetros);
        } catch (ConsultarException e) {
        }
    }

    private Personas verificarPersona(Personas personaParametro) {
        HashMap paremetros = new HashMap<>();
        paremetros.put(";where", "o.cedula=:cedula");
        paremetros.put("cedula", personaParametro.getCedula());
        try {
            List<Personas> lista = ejbPersonas.encontrarParametros(paremetros);
            if (lista.isEmpty()) {
                Personas personaDevolver = new Personas();
                personaDevolver.setApellidos(personaParametro.getApellidos());
                personaDevolver.setNombres(personaParametro.getNombres());
                personaDevolver.setCedula(personaParametro.getCedula());
                personaDevolver.setMail(personaParametro.getMail());
                personaDevolver.setContacto(personaParametro.getContacto());
                personaDevolver.setTipo("INVOLUCRADO");
                ejbPersonas.create(personaDevolver);
                return personaDevolver;
            } else {
                return lista.get(0);
            }
        } catch (ConsultarException e) {
        }
        return null;
    }

    public void inicioCaso() {
        System.out.println("estadoCasos " + estadoCasos);
        if (estadoCasos == null) {
            return;
        }
        // Editar caso
        caso.setEstado(estadoCasos);
        System.out.println("observaciones " + caso.getObservaciones());
        ejbCasos.edit(caso);
        //fin editar caso
        // crear tracking
        Eventos eventoCreacion = new Eventos();
        eventoCreacion.setFechahora(new Date());
        eventoCreacion.setEstado(estadoCasos);
        eventoCreacion.setAccionrealizada("Director Ingresa Caso");
        eventoCreacion.setCaso(caso);
        ejbEventos.create(eventoCreacion);
        // finizaiza tracking
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Caso editado"));
        PrimeFaces.current().executeScript("PF('manageDialogcontinuar').hide()");

        buscar();

    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public Casos getCaso() {
        return caso;
    }

    public void setCaso(Casos caso) {
        this.caso = caso;
    }

    public List<Documentos> getListaDocumentos() {
        return listaDocumentos;
    }

    public void setListaDocumentos(List<Documentos> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    /**
     * @return the listaCasos
     */
    public List<Casos> getListaCasos() {
        return listaCasos;
    }

    /**
     * @param listaCasos the listaCasos to set
     */
    public void setListaCasos(List<Casos> listaCasos) {
        this.listaCasos = listaCasos;
    }

    /**
     * @return the estadoCasos
     */
    public String getEstadoCasos() {
        return estadoCasos;
    }

    /**
     * @param estadoCasos the estadoCasos to set
     */
    public void setEstadoCasos(String estadoCasos) {
        this.estadoCasos = estadoCasos;
    }

    /**
     * @return the file
     */
    public UploadedFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(UploadedFile file) {
        this.file = file;
    }

}
