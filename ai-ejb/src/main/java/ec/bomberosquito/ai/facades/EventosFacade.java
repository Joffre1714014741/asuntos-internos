/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.facades;

import ec.bomberosquito.ai.entidades.Eventos;
import ec.bomberosquito.ai.excepciones.ConsultarException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jpverdezoto
 */
@Stateless
public class EventosFacade extends AbstractFacade<Eventos> {

    @PersistenceContext(unitName = "primary")
    private EntityManager em;
   

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EventosFacade() {
        super(Eventos.class);
    }
    
    public List<Eventos> obtenerNotificacionesPorEstado(String estado) throws ConsultarException {
        List<Eventos> listaEventos = new ArrayList<>();
        java.util.Map<String, Object> parametros = new HashMap<>();
        parametros.put(";where", "o.estado=:estado");
        parametros.put("estado", estado);
        listaEventos = encontrarParametros(parametros);
        return listaEventos;
}
}