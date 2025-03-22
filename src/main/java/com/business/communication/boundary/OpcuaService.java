package com.business.communication.boundary;

import com.business.communication.entity.OpcuaDevice;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

/**
 *
 * @author adminavvimpa
 */
@Stateless
public class OpcuaService {
    @PersistenceContext(unitName = "newstl_PU")
    EntityManager em;

    public List<OpcuaDevice> list() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OpcuaDevice> query = cb.createQuery(OpcuaDevice.class);
        Root<OpcuaDevice> root = query.from(OpcuaDevice.class);
        CriteriaQuery<OpcuaDevice> select = query.select(root).distinct(true);

        return em.createQuery(select).getResultList();
    }
}
