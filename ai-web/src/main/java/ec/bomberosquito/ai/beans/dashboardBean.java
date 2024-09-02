/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.beans;

import ec.bomberosquito.ai.entidades.Casos;
import ec.bomberosquito.ai.entidades.Usuarios;
import ec.bomberosquito.ai.excepciones.ConsultarException;
import ec.bomberosquito.ai.facades.CasosFacade;
import ec.bomberosquito.ai.facades.EventosFacade;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author jpverdezoto
 */
@ManagedBean(name = "dashboardBean")
@ViewScoped
public class dashboardBean implements Serializable {

    private String bandeja;
    private Usuarios usuarios = new Usuarios();

    @EJB
    private CasosFacade ejbCasos;

    @ManagedProperty(value = "#{seguridad}")
    private SeguridadBean seguridadBean;
    
    public String cantidadCasos() throws ConsultarException {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put(";where", "o.estado=:estado");
        parametros.put("estado", "CREADO");
        List<Casos> listaCasos = ejbCasos.encontrarParametros(parametros);
        Integer i = listaCasos.size();
        return i.toString();
    }
    
    public String cantidadElaborados() throws ConsultarException {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put(";where", "o.estado=:estado");
        parametros.put("estado", "ASIGNADO");
        List<Casos> listaCasos = ejbCasos.encontrarParametros(parametros);
        Integer i = listaCasos.size();
        return i.toString();
    }
    
    public String cantidadFinalizados() throws ConsultarException {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put(";where", "o.estado=:estado");
        parametros.put("estado", "APROBADO");
        List<Casos> listaCasos = ejbCasos.encontrarParametros(parametros);
        Integer i = listaCasos.size();
        return i.toString();
    }
    
    
    
        
    
    
  

    

    

    

    

    public String getBandeja() {
        return bandeja;
    }

    public void setBandeja(String bandeja) {
        this.bandeja = bandeja;
    }

    public CasosFacade getEjbCasos() {
        return ejbCasos;
    }

    public void setEjbCasos(CasosFacade ejbCasos) {
        this.ejbCasos = ejbCasos;
    }

    public SeguridadBean getSeguridadBean() {
        return seguridadBean;
    }

    public void setSeguridadBean(SeguridadBean seguridadBean) {
        this.seguridadBean = seguridadBean;
    }


}
