package com.github.michalszynkiewicz.entrystore.endpoint;

import com.github.michalszynkiewicz.entrystore.service.EntryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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

@Produces("application/json")
@Consumes("application/json")
@Path("")
@Api(value = "/entries", description = "Entry endpoint", tags = "entry", produces = "application/json", consumes = "application/json")
public class EntryEndpoint {

    public static final String PATH = "/entries";
    @Inject
    private EntryService entryService;

    @GET
    @ApiOperation("get all entries")
    public Response getAll() {
        List<EntryDto> all = entryService.getAll();

        return Response.ok(all).build();
    }

    @POST
    @ApiOperation("add entry")
    public Response add(EntryDto dto) {
        Integer id = entryService.save(dto);
        return Response.created(URI.create(String.format("/entries/%d", id))).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(Integer id) {
        entryService.delete(id);
        return Response.ok().build();
    }
}
