package com.github.michalszynkiewicz.entrystore.websockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.michalszynkiewicz.entrystore.endpoint.EntryDto;
import org.jboss.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Singleton;
import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.Session;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Author: Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com
 * Date: 2/4/17
 * Time: 4:08 PM
 */
@Singleton
public class Broadcaster {

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final Logger logger = Logger.getLogger(EntryWsEndpoint.class);

    private final ConcurrentLinkedQueue<Async> clients = new ConcurrentLinkedQueue<>();

    public void onNewEntry(@Observes EntryDto newEntry) throws JsonProcessingException {
        if (!clients.isEmpty()) {
            logger.info("sending to clients: " + clients);
            String dtoAsJson = jsonMapper.writeValueAsString(newEntry);
            clients.forEach(c -> c.sendText(dtoAsJson));
        }
    }

    public void add(Async client) {
        clients.add(client);
    }

    public void remove(Session session) {
        clients.remove(session.getAsyncRemote());
    }
}
