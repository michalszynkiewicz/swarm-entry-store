package com.github.michalszynkiewicz.entrystore.dao;

import com.github.michalszynkiewicz.entrystore.model.Entry;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

/**
 * Author: Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com
 * Date: 1/26/17
 * Time: 10:46 AM
 */
@Singleton
public class EntryDao {
    private static final Logger log = Logger.getLogger(EntryDao.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    public List<Entry> getAll() {
        return entityManager.createQuery("SELECT e FROM Entry e", Entry.class).getResultList();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Integer save(Entry entry) {
        entityManager.persist(entry);
        entityManager.flush();
        return entry.getId();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(Integer id) {
        entityManager.createQuery("DELETE FROM Entry e where e.id = id");
    }
}
