/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jpverdezoto
 */
@Entity
@Table(name = "casos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Casos.findAll", query = "SELECT c FROM Casos c"),
    @NamedQuery(name = "Casos.findById", query = "SELECT c FROM Casos c WHERE c.id = :id"),
    @NamedQuery(name = "Casos.findByEstado", query = "SELECT c FROM Casos c WHERE c.estado = :estado"),
    @NamedQuery(name = "Casos.findByFechaderealizacion", query = "SELECT c FROM Casos c WHERE c.fechaderealizacion = :fechaderealizacion"),
    @NamedQuery(name = "Casos.findByFechadeincidente", query = "SELECT c FROM Casos c WHERE c.fechadeincidente = :fechadeincidente"),
    @NamedQuery(name = "Casos.findByFechadeasignacion", query = "SELECT c FROM Casos c WHERE c.fechadeasignacion = :fechadeasignacion"),
    @NamedQuery(name = "Casos.findByDetallecausa", query = "SELECT c FROM Casos c WHERE c.detallecausa = :detallecausa"),
    @NamedQuery(name = "Casos.findByLugardetrabajo", query = "SELECT c FROM Casos c WHERE c.lugardetrabajo = :lugardetrabajo"),
    @NamedQuery(name = "Casos.findByActividadesrealizadas", query = "SELECT c FROM Casos c WHERE c.actividadesrealizadas = :actividadesrealizadas"),
    @NamedQuery(name = "Casos.findByLugardeincidente", query = "SELECT c FROM Casos c WHERE c.lugardeincidente = :lugardeincidente"),
    @NamedQuery(name = "Casos.findByAntecedentes", query = "SELECT c FROM Casos c WHERE c.antecedentes = :antecedentes"),
    @NamedQuery(name = "Casos.findByCalificacion", query = "SELECT c FROM Casos c WHERE c.calificacion = :calificacion"),
    @NamedQuery(name = "Casos.findByRelaciondehechos", query = "SELECT c FROM Casos c WHERE c.relaciondehechos = :relaciondehechos"),
    @NamedQuery(name = "Casos.findByConclusiones", query = "SELECT c FROM Casos c WHERE c.conclusiones = :conclusiones"),
    @NamedQuery(name = "Casos.findByRecomendaciones", query = "SELECT c FROM Casos c WHERE c.recomendaciones = :recomendaciones"),
    @NamedQuery(name = "Casos.findByNumeroinforme", query = "SELECT c FROM Casos c WHERE c.numeroinforme = :numeroinforme")})
public class Casos implements Serializable {

    @Size(max = 2147483647)
    @Column(name = "nombreinvolucrado")
    private String nombreinvolucrado;
    @Size(max = 2147483647)
    @Column(name = "apellidosinvloucrado")
    private String apellidosinvloucrado;
    @Size(max = 2147483647)
    @Column(name = "incurrencia")
    private String incurrencia;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 25)
    @Column(name = "estado")
    private String estado;
    @Column(name = "fechaderealizacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaderealizacion;
    @Column(name = "fechadeincidente")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechadeincidente;
    @Column(name = "fechadeasignacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechadeasignacion;
    @Size(max = 2147483647)
    @Column(name = "detallecausa")
    private String detallecausa;
    @Size(max = 255)
    @Column(name = "lugardetrabajo")
    private String lugardetrabajo;
    @Size(max = 2147483647)
    @Column(name = "actividadesrealizadas")
    private String actividadesrealizadas;
    @Size(max = 255)
    @Column(name = "lugardeincidente")
    private String lugardeincidente;
    @Size(max = 2147483647)
    @Column(name = "antecedentes")
    public String antecedentes;
    @Size(max = 2147483647)
    @Column(name = "calificacion")
    private String calificacion;
    @Size(max = 2147483647)
    @Column(name = "relaciondehechos")
    private String relaciondehechos;
    @Size(max = 2147483647)
    @Column(name = "conclusiones")
    private String conclusiones;
    @Size(max = 2147483647)
    @Column(name = "recomendaciones")
    private String recomendaciones;
    @Size(max = 255)
    @Column(name = "numeroinforme")
    private String numeroinforme;
    @JoinColumn(name = "causa", referencedColumnName = "id")
    @ManyToOne
    private Causas causa;
    @JoinColumn(name = "director", referencedColumnName = "id")
    @ManyToOne
    private Usuarios director;
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    @ManyToOne
    private Usuarios responsable;
    @Size(max = 2147483647)
    @Column(name = "observaciones")
    private String observaciones;

    @JoinColumn(name = "denunciante", referencedColumnName = "id")
    @ManyToOne
    public Personas denunciante;

    public Casos() {
    }

    public Casos(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaderealizacion() {
        return fechaderealizacion;
    }

    public void setFechaderealizacion(Date fechaderealizacion) {
        this.fechaderealizacion = fechaderealizacion;
    }

    public Date getFechadeincidente() {
        return fechadeincidente;
    }

    public void setFechadeincidente(Date fechadeincidente) {
        this.fechadeincidente = fechadeincidente;
    }

    public Date getFechadeasignacion() {
        return fechadeasignacion;
    }

    public void setFechadeasignacion(Date fechadeasignacion) {
        this.fechadeasignacion = fechadeasignacion;
    }

    public String getDetallecausa() {
        return detallecausa;
    }

    public void setDetallecausa(String detallecausa) {
        this.detallecausa = detallecausa;
    }

    public String getLugardetrabajo() {
        return lugardetrabajo;
    }

    public void setLugardetrabajo(String lugardetrabajo) {
        this.lugardetrabajo = lugardetrabajo;
    }

    public String getActividadesrealizadas() {
        return actividadesrealizadas;
    }

    public void setActividadesrealizadas(String actividadesrealizadas) {
        this.actividadesrealizadas = actividadesrealizadas;
    }

    public String getLugardeincidente() {
        return lugardeincidente;
    }

    public void setLugardeincidente(String lugardeincidente) {
        this.lugardeincidente = lugardeincidente;
    }

    public String getAntecedentes() {
        return antecedentes;
    }

    public void setAntecedentes(String antecedentes) {
        this.antecedentes = antecedentes;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public String getRelaciondehechos() {
        return relaciondehechos;
    }

    public void setRelaciondehechos(String relaciondehechos) {
        this.relaciondehechos = relaciondehechos;
    }

    public String getConclusiones() {
        return conclusiones;
    }

    public void setConclusiones(String conclusiones) {
        this.conclusiones = conclusiones;
    }

    public String getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(String recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    public String getNumeroinforme() {
        return numeroinforme;
    }

    public void setNumeroinforme(String numeroinforme) {
        this.numeroinforme = numeroinforme;
    }

    public Causas getCausa() {
        return causa;
    }

    public void setCausa(Causas causa) {
        this.causa = causa;
    }

    public Usuarios getDirector() {
        return director;
    }

    public void setDirector(Usuarios director) {
        this.director = director;
    }

    public Usuarios getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuarios responsable) {
        this.responsable = responsable;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Casos)) {
            return false;
        }
        Casos other = (Casos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.bomberosquito.ai.entidades.Casos[ id=" + id + " ]";
    }

    /**
     * @return the observaciones
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * @param observaciones the observaciones to set
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Personas getDenunciante() {
        return denunciante;
    }

    public void setDenunciante(Personas denunciante) {
        this.denunciante = denunciante;
    }

    public String getNombreinvolucrado() {
        return nombreinvolucrado;
    }

    public void setNombreinvolucrado(String nombreinvolucrado) {
        this.nombreinvolucrado = nombreinvolucrado;
    }

    public String getApellidosinvloucrado() {
        return apellidosinvloucrado;
    }

    public void setApellidosinvloucrado(String apellidosinvloucrado) {
        this.apellidosinvloucrado = apellidosinvloucrado;
    }

    public String getIncurrencia() {
        return incurrencia;
    }

    public void setIncurrencia(String incurrencia) {
        this.incurrencia = incurrencia;
    }

}
