/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.entidades;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jpverdezoto
 */
@Entity
@Table(name = "eventos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Eventos.findAll", query = "SELECT e FROM Eventos e"),
    @NamedQuery(name = "Eventos.findById", query = "SELECT e FROM Eventos e WHERE e.id = :id"),
    @NamedQuery(name = "Eventos.findByFechahora", query = "SELECT e FROM Eventos e WHERE e.fechahora = :fechahora"),
    @NamedQuery(name = "Eventos.findByEstado", query = "SELECT e FROM Eventos e WHERE e.estado = :estado"),
    @NamedQuery(name = "Eventos.findByAccionrealizada", query = "SELECT e FROM Eventos e WHERE e.accionrealizada = :accionrealizada")})
public class Eventos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fechahora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechahora;
    @Size(max = 25)
    @Column(name = "estado")
    private String estado;
    @Size(max = 2147483647)
    @Column(name = "accionrealizada")
    private String accionrealizada;
    @JoinColumn(name = "caso", referencedColumnName = "id")
    @ManyToOne
    private Casos caso;

    public Eventos() {
    }

    public Eventos(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechahora() {
        return fechahora;
    }

    public void setFechahora(Date fechahora) {
        this.fechahora = fechahora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getAccionrealizada() {
        return accionrealizada;
    }

    public void setAccionrealizada(String accionrealizada) {
        this.accionrealizada = accionrealizada;
    }

    public Casos getCaso() {
        return caso;
    }

    public void setCaso(Casos caso) {
        this.caso = caso;
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
        if (!(object instanceof Eventos)) {
            return false;
        }
        Eventos other = (Eventos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.bomberosquito.ai.entidades.Eventos[ id=" + id + " ]";
    }
    
}
