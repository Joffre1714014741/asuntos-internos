/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.facades;

import ec.bomberosquito.ai.entidades.Casos;
import ec.bomberosquito.ai.excepciones.ConsultarException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author danielhwang
 */
@Stateless
public class CasosFacade extends AbstractFacade<Casos> {

    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CasosFacade() {
        super(Casos.class);
    }

    public List<Casos> obtenerCasosPorEstado(String estado) throws ConsultarException {
        List<Casos> listaCasos = new ArrayList<>();
        Map<String, Object> parametros = new HashMap<>();
        parametros.put(";where", "o.estado=:estado");
        parametros.put("estado", estado);
        listaCasos = encontrarParametros(parametros);
        return listaCasos;
    }
    
}
