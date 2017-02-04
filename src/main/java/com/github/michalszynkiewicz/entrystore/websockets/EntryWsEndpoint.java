package com.github.michalszynkiewicz.entrystore.websockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Author: Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com
 * Date: 2/3/17
 * Time: 10:33 PM
 */
@ServerEndpoint(value = "/ws/entries", subprotocols = "sockjs")
public class EntryWsEndpoint implements MessageHandler {
    private final Logger logger = Logger.getLogger(EntryWsEndpoint.class);

    @Inject
    private Broadcaster broadcaster;

    @OnOpen
    public void register(Session session, EndpointConfig config) throws JsonProcessingException {
        Async client = session.getAsyncRemote();
        logger.info("registering client:" + client);
        broadcaster.add(client);
    }

    @OnClose
    public void unregister(Session session, CloseReason reason) {
        logger.info(String.format("unregistering % because of %", session, reason));
        broadcaster.remove(session);
    }
    @OnError
    public void unregister(Session session, Throwable reason) {
        logger.error(String.format("unregistering % because of error %", session, reason));
        broadcaster.remove(session);
    }
}
