package com.github.michalszynkiewicz.entrystore.service;

import com.github.michalszynkiewicz.entrystore.dao.EntryDao;
import com.github.michalszynkiewicz.entrystore.endpoint.EntryDto;
import com.github.michalszynkiewicz.entrystore.model.Entry;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com
 * Date: 2/3/17
 * Time: 11:08 PM
 */
@Singleton
public class EntryService {
    @Inject
    private EntryDao entryDao;
    @Inject
    private Event<EntryDto> entryObserver;

    public List<EntryDto> getAll() {
        List<Entry> all = entryDao.getAll();
        return all.stream().map(EntryDto::new).collect(Collectors.toList());
    }

    public Integer save(EntryDto dto) {
        Entry entry = dto.toEntry();
        Integer id = entryDao.save(entry);
        dto.setId(id);
        entryObserver.fire(dto);
        return id;
    }

    public void delete(Integer id) {
        entryDao.delete(id);
    }
}
