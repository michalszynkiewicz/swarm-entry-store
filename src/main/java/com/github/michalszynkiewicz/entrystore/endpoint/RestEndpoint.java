package com.github.michalszynkiewicz.entrystore.endpoint;

import javax.inject.Inject;
import javax.ws.rs.Path;

/**
 * Author: Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com
 * Date: 2/2/17
 * Time: 11:52 PM
 * oh-oh, look what one ca do :O
 */
@Path("/")
public class RestEndpoint {

    @Inject
    private EntryEndpoint entryEndpoint;

    @Path(EntryEndpoint.PATH)
    public EntryEndpoint get() {
        return entryEndpoint;
    }
}
