/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.facades;

import ec.bomberosquito.ai.entidades.Usuarios;
import ec.bomberosquito.ai.excepciones.ConsultarException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author danielhwang
 */
@Stateless
public class UsuariosFacade extends AbstractFacade<Usuarios> {

    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuariosFacade() {
        super(Usuarios.class);
    }

    public List<Usuarios> obtenerUsuariosPorTipo(String tipo) throws ConsultarException {
        List<Usuarios> listaUsuarios = new ArrayList<>();
        Map<String, Object> parametros = new HashMap<>();
        parametros.put(";where", "o.tipo=:tipo");
        parametros.put("tipo", tipo);
        listaUsuarios = encontrarParametros(parametros);
        return listaUsuarios;
    }
    
}
