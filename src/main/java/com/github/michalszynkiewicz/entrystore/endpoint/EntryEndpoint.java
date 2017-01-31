package com.github.michalszynkiewicz.entrystore.endpoint;

import com.github.michalszynkiewicz.entrystore.dao.EntryDao;
import com.github.michalszynkiewicz.entrystore.model.Entry;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Path("/entries")
@Produces("application/json")
@Consumes("application/json")
public class EntryEndpoint {

    @Inject
    private EntryDao entryDao;

    @GET
    public Response getAll() {
        List<Entry> all = entryDao.getAll();
        List<EntryDto> entries = all.stream().map(EntryDto::new).collect(Collectors.toList());

        return Response.ok(entries).build();
    }

    @POST
    public Response add(EntryDto dto) {
        Entry entry = dto.toEntry();
        Integer id = entryDao.save(entry);
        return Response.created(URI.create(String.format("/entries/%d", id))).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(Integer id) {
        entryDao.delete(id);
        return Response.ok().build();
    }
}
