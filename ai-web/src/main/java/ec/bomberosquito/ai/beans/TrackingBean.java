/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.beans;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import ec.bomberosquito.ai.auxiliares.AuxiliarReporte;
import ec.bomberosquito.ai.auxiliares.DocumentoPDF;
import ec.bomberosquito.ai.entidades.Casos;
import ec.bomberosquito.ai.entidades.Eventos;
import ec.bomberosquito.ai.excepciones.ConsultarException;
import ec.bomberosquito.ai.facades.CasosFacade;
import ec.bomberosquito.ai.facades.EventosFacade;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

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
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
    private Casos caso;
    private File solicitudArchivo;
    private String pathpdfSolicitud;

    public void cargarEvento(Integer id) {
        try {

            System.out.println("parametro de busqueda" + id);
            HashMap paremetros = new HashMap<>();
            paremetros.put(";where", "o.caso.id=:jofre");
            paremetros.put("jofre", id);
            paremetros.put(";orden", "o.id");
            listaEventosTraking = ejbEventos.encontrarParametros(paremetros);
            System.out.println("tamanio de la lista" + listaEventosTraking.size());
        } catch (ConsultarException ex) {
            Logger.getLogger(TrackingBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void cargarCaso() {
        try {
            String where = "o.id is not null";
            HashMap paremetros = new HashMap<>();
            if (casoParametro != null) {
                where += " and o.id=:caso ";
                paremetros.put("caso", casoParametro);
            }
            paremetros.put(";where", where);

            listaCasos = ejbCasos.encontrarParametros(paremetros);
        } catch (ConsultarException ex) {
            Logger.getLogger(TrackingBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void generarPdf(Casos casoParametro) {
        caso = casoParametro;
        cargarEvento(caso.getId());
        if (listaEventosTraking == null) {
            return;
        }
        try {
            DocumentoPDF pdf = new DocumentoPDF("", "", 1, true);
            List<AuxiliarReporte> columnas = new LinkedList<>();
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_CENTER, 12, true, "DIRECCION DE ASUNTOS INTERNOS", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_CENTER, 12, true, "TRACKING DE INVESTIGACIÓN", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_CENTER, 12, true, "DAJ-UI-CBDMQ-2024-0" + caso.getId().toString() + "\n\n", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            pdf.agregarTabla(null, columnas, 1, 100, new float[]{Float.valueOf(100)}, false, Font.HELVETICA);

            columnas = new LinkedList<>();
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, true, "ESTADO: ", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, true, "ACCION REALIZADA", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
            columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, true, "FECHA ", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));

            if (listaEventosTraking.isEmpty()) {
                columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, true, "No existen registros", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));

            } else {
                for (Eventos ev : listaEventosTraking) {
                    columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, ev.getEstado() != null ?  ev.getEstado() : "", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
                    columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, ev.getAccionrealizada() != null ? ev.getAccionrealizada() : "", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
                    columnas.add(new AuxiliarReporte("String", 1, AuxiliarReporte.ALIGN_JUSTIFIED, 11, false, ev.getFechahora() != null ? sdf.format(ev.getFechahora()) : "", 1, 0, Color.BLACK, Color.WHITE, Color.WHITE));
                }

            }
            pdf.agregarTabla(null, columnas, 1, 100, new float[]{Float.valueOf(33), Float.valueOf(33), Float.valueOf(33)}, false, Font.HELVETICA);

            pdf.traerRecurso();
            setSolicitudArchivo(pdf.traerArchivo());

            mostrarPDF(getSolicitudArchivo().getAbsolutePath(), "test1.pdf");
            PrimeFaces.current().executeScript("PF('manageDialogpdf').show()");

        } catch (IOException | DocumentException | ConsultarException ex) {
            Logger.getLogger(atencionCasosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarPDF(String pathparametro, String nombreArchivo) throws IOException, ConsultarException {
        File file = new File(pathparametro);
        String realPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("visor");
        String pathpdfEliminar = crearFicheroTemp(nombreArchivo, Files.readAllBytes(file.toPath()), realPath);
        System.out.println("nombreArchivo " + nombreArchivo);
        setPathpdfSolicitud("./visor/ordentrabajo/" + nombreArchivo);

    }

    public String crearFicheroTemp(String id, byte[] archivo, String pathTemp) throws IOException, ConsultarException {

        File folder = new File(pathTemp + "/ordentrabajo");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File fichero = new File(folder.getAbsolutePath() + "/" + id);
        fichero.createNewFile();

        try ( OutputStream out = new FileOutputStream(fichero.getCanonicalPath())) {
            out.write(archivo);
        }
        return fichero.getCanonicalPath();
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

    /**
     * @return the solicitudArchivo
     */
    public File getSolicitudArchivo() {
        return solicitudArchivo;
    }

    /**
     * @param solicitudArchivo the solicitudArchivo to set
     */
    public void setSolicitudArchivo(File solicitudArchivo) {
        this.solicitudArchivo = solicitudArchivo;
    }

    /**
     * @return the pathpdfSolicitud
     */
    public String getPathpdfSolicitud() {
        return pathpdfSolicitud;
    }

    /**
     * @param pathpdfSolicitud the pathpdfSolicitud to set
     */
    public void setPathpdfSolicitud(String pathpdfSolicitud) {
        this.pathpdfSolicitud = pathpdfSolicitud;
    }

}
