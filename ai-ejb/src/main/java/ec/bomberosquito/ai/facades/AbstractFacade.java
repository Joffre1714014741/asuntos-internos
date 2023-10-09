/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.facades;

import ec.bomberosquito.ai.excepciones.ConsultarException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;

/**
 *
 * @author danielhwang
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<T> encontrarParametros(Map parametros) throws ConsultarException {
        try {
            Iterator it = parametros.entrySet().iterator();
            String where = "";
            String orden = "";
            boolean all = false;
            int maxResults = -1;
            int firstResult = -1;
            String tabla = entityClass.getSimpleName();
            Map par = new HashMap();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry) it.next();
                String clave = (String) e.getKey();
                if (clave.contains(";where")) {
                    where = (String) e.getValue();
                } else if (clave.contains(";inicial")) {
                    firstResult = (Integer) e.getValue();
                    all = true;
                } else if (clave.contains(";orden")) {
                    orden = " order by " + (String) e.getValue();

                } else if (clave.contains(";final")) {
                    all = true;
                    maxResults = (Integer) e.getValue();
                } else {
                    par.put(clave, e.getValue());
                }

            }
            if (!where.isEmpty()) {
                where = " where " + where;
            }

            String sql = "Select object(o) from " + tabla + " as o " + where + orden;
            javax.persistence.Query q = getEntityManager().createQuery(sql);

            it = par.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry) it.next();
                String clave = (String) e.getKey();

                q.setParameter(clave, e.getValue());
            }
            if (all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } catch (Exception e) {
            throw new ConsultarException(entityClass.toString(), e);
        }
    }

    public int contar(Map parametros) throws ConsultarException {
        try {
            Iterator it = parametros.entrySet().iterator();
            String where = "";
            String orden = "";
            boolean all = false;
            int maxResults = -1;
            int firstResult = -1;
            String tabla = entityClass.getSimpleName();
            Map par = new HashMap();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry) it.next();
                String clave = (String) e.getKey();
                if (clave.contains("where")) {
                    where = (String) e.getValue();
                } else if (clave.contains(";inicial")) {
                    firstResult = (Integer) e.getValue();
                    all = true;
                } else if (clave.contains(";orden")) {
                    orden = " order by " + (String) e.getValue();

                } else if (clave.contains(";final")) {
                    all = true;
                    maxResults = (Integer) e.getValue();
                } else {
                    par.put(clave, e.getValue());
                }

            }
            if (!where.isEmpty()) {
                where = " where " + where;
            }

            String sql = "Select count(o) from " + tabla + " as o " + where;
            javax.persistence.Query q = getEntityManager().createQuery(sql);

            it = par.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry) it.next();
                String clave = (String) e.getKey();

                q.setParameter(clave, e.getValue());
            }

            return ((Long) q.getSingleResult()).intValue();
        } catch (Exception e) {
            throw new ConsultarException(entityClass.toString(), e);
        }
    }
    
}
