/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.bomberosquito.ai.facades;

import ec.bomberosquito.ai.entidades.Documentos;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jpverdezoto
 */
@Stateless
public class DocumentosFacade extends AbstractFacade<Documentos> {

    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocumentosFacade() {
        super(Documentos.class);
    }
    
}
