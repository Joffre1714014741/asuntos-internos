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
import ec.bomberosquito.ai.facades.PersonasFacade;
import ec.bomberosquito.ai.facades.UsuariosFacade;
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

/**
 *
 * @author jpverdezoto
 */
@Named
@ViewScoped
public class CasosBean implements Serializable {
    
    private Personas persona;
    private Usuarios usuario;
    private Casos caso;
    private List<Documentos> documentosTemporales;
    private List<Documentos> listaDocumentos;   

    @EJB
    private PersonasFacade ejbPersonas;

    @EJB
    private UsuariosFacade ejbUsuarios;

    @EJB
    private CasosFacade ejbCasos;
    
    @EJB
    private DocumentosFacade ejbDocumentos;
    
    @EJB
    private EventosFacade ejbEventos;

    @PostConstruct
    public void init() {
        persona = new Personas();
        usuario = new Usuarios();
        caso = new Casos();
        listaDocumentos = new ArrayList<>();
        documentosTemporales = new ArrayList<>();
    }

    public void generarCaso() {
    }
    
    public String casos(){
        return "/creacion-casos.xhtml?faces-redirect=true";
    }
    
    public void subirDocumento(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();
            String filename = file.getFileName();
            Path folder = Paths.get("/Users/danielhwang/Documents/ai");
            Path filePath = Files.createTempFile(folder, filename + "-", ".tmp");
            Files.write(filePath, file.getContent());

            Documentos nuevoDocumento = new Documentos();
            nuevoDocumento.setNombredocumento(filename);
            nuevoDocumento.setRuta(filePath.toString());
            documentosTemporales.add(nuevoDocumento);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al subir el archivo", null));
        }
    }

    public void eliminarDocumento(Documentos documento) {
        try {
            Path fileToDeletePath = Paths.get(documento.getRuta());
            Files.deleteIfExists(fileToDeletePath);
            documentosTemporales.remove(documento);
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar el archivo físico", null));
        }
    }

    public void confirmarCaso() {
        
        // Crear persona
        persona.setTipo("INVOLUCRADO");
        ejbPersonas.create(persona);
        
       
        
        // Asignar usuario como involucrado en el caso
        caso.setInvolucrado(persona);
        caso.setEstado("CREADO");
        
        // Persistir el caso
        caso.setFechaderealizacion(new Date());
        ejbCasos.create(caso);
        
        // Ahora, asigna el caso y persiste cada documento
        for (Documentos documento : documentosTemporales) {
            documento.setCaso(caso);
            ejbDocumentos.create(documento);
        }
        
        listaDocumentos = new ArrayList<>(documentosTemporales);
        documentosTemporales.clear();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Caso y documentos creados exitosamente"));
        
        Eventos eventoCreacion = new Eventos();
        eventoCreacion.setCaso(caso);
        eventoCreacion.setAccionrealizada("Usuario crea un caso");
        eventoCreacion.setEstado("CASO_CREADO");
        eventoCreacion.setFechahora(new Date());
        ejbEventos.create(eventoCreacion);

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

    public List<Documentos> getListaDocumentos() {
        return listaDocumentos;
    }

    public void setListaDocumentos(List<Documentos> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    public List<Documentos> getDocumentosTemporales() {
        return documentosTemporales;
    }

    public void setDocumentosTemporales(List<Documentos> documentosTemporales) {
        this.documentosTemporales = documentosTemporales;
    }
    
}
