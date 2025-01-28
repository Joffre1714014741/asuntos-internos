/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.beans;

import ec.bomberosquito.ai.auxiliares.Recurso;
import ec.bomberosquito.ai.auxiliares.Validadores;
import ec.bomberosquito.ai.entidades.Personas;
import ec.bomberosquito.ai.entidades.Casos;
import ec.bomberosquito.ai.entidades.Documentos;
import ec.bomberosquito.ai.entidades.Eventos;
import ec.bomberosquito.ai.excepciones.ConsultarException;
import ec.bomberosquito.ai.facades.PersonasFacade;
import ec.bomberosquito.ai.facades.CasosFacade;
import ec.bomberosquito.ai.facades.DocumentosFacade;
import ec.bomberosquito.ai.facades.EventosFacade;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.primefaces.model.file.UploadedFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author jpverdezoto
 */
@Named(value = "casosBean")
@ViewScoped
public class CasosBean implements Serializable {

    private String cedula;
    private Personas persona;
    private Casos caso;
    private List<Documentos> listaDocumentos = new ArrayList<>();
    private List<Casos> listaCasos;
    private String estadoCasos;
    private UploadedFile file;
    private Recurso recurso;
    private boolean editable=false;
    
    @Inject
    private SeguridadBean seguridadBean;
    
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

    public CasosBean() {

    }

    public void llenarDatos() throws ConsultarException {
        System.out.println("entro");
        if (cedula != null) {
            HashMap paremetros = new HashMap<>();
            paremetros.put(";where", "o.cedula=:cedula");
            paremetros.put("cedula", cedula);
            if (!ejbPersonas.encontrarParametros(paremetros).isEmpty()) {
                this.persona = ejbPersonas.encontrarParametros(paremetros).get(0);
                setEditable(true);
                System.out.println("si encontro personas " + persona);
            }
        }
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
        System.out.println("paso archivo 1");
        try {
            if (file == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se seleccionó ningún archivo", null));
                return;
            }
            System.out.println("paso archivo");
            String filename = file.getFileName();
            Path folder = Paths.get("C:\\Users\\jpverdezoto\\Documents\\documentos");
            if (!Files.exists(folder)) {
                Files.createDirectories(folder);
            }
            Path filePath = Files.createTempFile(folder, filename + "-", ".pdf");
            Files.write(filePath, file.getContent());

            Documentos nuevoDocumento = new Documentos();
            nuevoDocumento.setNombredocumento(filename);
            nuevoDocumento.setRuta(filePath.toString());
            listaDocumentos.add(nuevoDocumento);
            // Mensaje de éxito
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Archivo cargado con éxito", filename));
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al subir el archivo", null));
        }

    }

    public void downloadFile(String filePath) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response
                = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        File file = new File(filePath);
        try ( FileInputStream fileInputStream = new FileInputStream(file);  OutputStream output = response.getOutputStream()) {

            // Configurar la respuesta HTTP
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
            response.setContentLength((int) file.length());

            // Copiar los datos del archivo al flujo de salida
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }

            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            facesContext.responseComplete();
        }
    }

    public String descargarImagen(String path, String nombre) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        try {
            recurso = new Recurso(Files.readAllBytes(Paths.get(path)));
        } catch (IOException ex) {
            Logger.getLogger(CasosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
        if (!validador()) {
            return;
        }
        
        if (persona.getId() == null){
            persona.setCedula(cedula);
            persona.setTipo("DENUNCIANTE");
                        System.out.println("TIPO " + persona.getTipo());

             ejbPersonas.create(persona);
        } else {
            System.out.println("mail " + persona.getMail());
             persona.setTipo("DENUNCIANTE");
            ejbPersonas.edit(persona);
        }
       
        
        // Crear persona
        caso.setDenunciante(persona);
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
        eventoCreacion.setAccionrealizada("Denuncia creada");
        eventoCreacion.setEstado("CREADO");
        eventoCreacion.setFechahora(new Date());
        eventoCreacion.setComentario("Usuario crea una nueva denuncia");
        eventoCreacion.setAccionante("DENUNCIANTE");
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
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Caso y documentos editados exitosamente"));

        Eventos eventoCreacion = new Eventos();
        eventoCreacion.setCaso(caso);
        eventoCreacion.setAccionrealizada("Revisado por el Director ");
        eventoCreacion.setEstado("REVISADO");
        eventoCreacion.setFechahora(new Date());
        eventoCreacion.setComentario("Director Verifica que la denuncia cumpla con los parametros requeridos para dar inicio a la investigación");
        eventoCreacion.setAccionante(seguridadBean.getUserLogueado().getTipo());
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
        HashMap paremetros = new HashMap<>();
        paremetros.put(";where", "o.caso=:caso");
        paremetros.put("caso", caso);
        try {
            listaDocumentos = ejbDocumentos.encontrarParametros(paremetros);
        } catch (ConsultarException e) {
        }
    }

//    private Personas verificarPersona(Personas personaParametro) {
//        HashMap paremetros = new HashMap<>();
//        paremetros.put(";where", "o.cedula=:cedula");
//        paremetros.put("cedula", personaParametro.getCedula());
//        try {
//            List<Personas> lista = ejbPersonas.encontrarParametros(paremetros);
//            if (lista.isEmpty()) {
//                Personas personaDevolver = new Personas();
//                personaDevolver.setApellidos(personaParametro.getApellidos());
//                personaDevolver.setNombres(personaParametro.getNombres());
//                personaDevolver.setCedula(cedula);
//                personaDevolver.setMail(personaParametro.getMail());
//                personaDevolver.setContacto(personaParametro.getContacto());
//                personaDevolver.setTipo("DENUNCIANTE");
//                try {
//                    ejbPersonas.create(personaDevolver);
//                } catch (javax.validation.ConstraintViolationException e) {
//                    for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
//                        System.out.println("Propiedad: " + violation.getPropertyPath());
//                        System.out.println("Mensaje: " + violation.getMessage());
//                        System.out.println("Valor inválido: " + violation.getInvalidValue());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace(); // Esto imprimirá cualquier otra excepción que no sea ConstraintViolationException.
//                }
//
//                return personaDevolver;
//            } else {
//                return lista.get(0);
//            }
//        } catch (ConsultarException e) {
//        }
//        return null;
//    }

    public void inicioCaso() {
        System.out.println("estadoCasos " + estadoCasos);
        if (estadoCasos == null) {
            return;
        }
        if (caso.getObservaciones() == null || caso.getObservaciones().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ingrese la observación"));
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
        eventoCreacion.setAccionrealizada("Director inicia la investigación");
        eventoCreacion.setCaso(caso);
        eventoCreacion.setComentario(caso.getObservaciones());
        eventoCreacion.setAccionante(seguridadBean.getUserLogueado().getTipo());
        ejbEventos.create(eventoCreacion);
        // finizaiza tracking
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Caso revisado de manera correcta"));
        PrimeFaces.current().executeScript("PF('manageDialogcontinuar').hide()");

        buscar();

    }

    public boolean validador() {
        if (!validarCorreo(persona.getMail())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Correo electrónico inválido"));
            return false;
        }
        if (!validarTelefono(persona.getContacto())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Numero de teléfono incorrecto"));
            return false;
        }
        return true;
    }

    public boolean validarCorreo(String correo) {
        // Expresión regular para validar el formato de un correo electrónico
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

        // Validar que el correo no sea nulo y coincida con la expresión regular
        if (correo == null) {
            return false;
        }
        return Pattern.matches(emailRegex, correo);
    }

    public boolean validarTelefono(String telefono) {
        // Expresión regular para validar un número de celular de 10 dígitos que empieza con 09
        String phoneRegex = "^09\\d{8}$";

        // Validar que el número no sea nulo y coincida con la expresión regular
        if (telefono == null) {
            return false;
        }

        return Pattern.matches(phoneRegex, telefono);
    }
    
    public void actualizarCorreo() {
    if (persona != null && persona.getMail() != null) {
        System.out.println("El correo está vacío o no es válido.");
        return; // Salir del método si el correo es inválido
    }

    // Validar que el correo tenga un formato válido (opcional)
    if (!persona.getMail().matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
        System.out.println("El formato del correo no es válido.");
        return;
    }

    // Lógica para actualizar el correo
    System.out.println("Correo actualizado: " + persona.getMail());
    // Aquí puedes llamar al servicio que persiste la información en la base de datos
}

    public void actualizarContacto() {
    // Lógica para actualizar el contacto en la base de datos
    if (persona != null && persona.getContacto() != null) {
        System.out.println("Elnúmero de contacto es válido.");
        return;
    }
    
    System.out.println("Correo actualizado: " + persona.getMail());
    // Aquí puedes llamar al servicio que persiste la información en la base de datos
        
        // Código para actualizar el contacto en la base de datos
    }
    
    
    public void limpiarCedula(){
        setCedula(null);
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

    public Recurso getRecurso() {
        return recurso;
    }

    public void setRecurso(Recurso recurso) {
        this.recurso = recurso;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

}
