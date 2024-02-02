/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.entidades;

import java.io.Serializable;
import java.util.Objects;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author danielhwang
 */
@Entity
@Table(name = "documentos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Documentos.findAll", query = "SELECT d FROM Documentos d"),
    @NamedQuery(name = "Documentos.findById", query = "SELECT d FROM Documentos d WHERE d.id = :id"),
    @NamedQuery(name = "Documentos.findByNombredocumento", query = "SELECT d FROM Documentos d WHERE d.nombredocumento = :nombredocumento"),
    @NamedQuery(name = "Documentos.findByRuta", query = "SELECT d FROM Documentos d WHERE d.ruta = :ruta")})
public class Documentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "nombredocumento")
    private String nombredocumento;
    @Size(max = 2147483647)
    @Column(name = "ruta")
    private String ruta;
    @JoinColumn(name = "caso", referencedColumnName = "id")
    @ManyToOne
    private Casos caso;

    public Documentos() {
    }

    public Documentos(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombredocumento() {
        return nombredocumento;
    }

    public void setNombredocumento(String nombredocumento) {
        this.nombredocumento = nombredocumento;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Casos getCaso() {
        return caso;
    }

    public void setCaso(Casos caso) {
        this.caso = caso;
    }

//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (id != null ? id.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof Documentos)) {
//            return false;
//        }
//        Documentos other = (Documentos) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.nombredocumento);
        hash = 53 * hash + Objects.hashCode(this.ruta);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Documentos other = (Documentos) obj;
        return true;
    }
    
    

    @Override
    public String toString() {
        return "ec.bomberosquito.ai.entidades.Documentos[ id=" + id + " ]";
    }
    
}
