package edu.rice.cs;

import edu.rice.cs.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static edu.rice.cs.WebSocketConfiguration.*;

/**
 * Created by songxiongfeng on 5/22/20
 */
@Component
@RepositoryEventHandler(Employee.class) // <1>
public class EventHandler {

    private final SimpMessagingTemplate websocket; // <2>

    private final EntityLinks entityLinks;

    @Autowired
    public EventHandler(SimpMessagingTemplate websocket, EntityLinks entityLinks) {
        this.websocket = websocket;
        this.entityLinks = entityLinks;
    }

    @HandleAfterCreate // <3>
    public void newEmployee(Employee employee) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/newEmployee", getPath(employee));
    }

    @HandleAfterDelete // <3>
    public void deleteEmployee(Employee employee) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/deleteEmployee", getPath(employee));
    }

    @HandleAfterSave // <3>
    public void updateEmployee(Employee employee) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/updateEmployee", getPath(employee));
    }

    /**
     * Take an {@link Employee} and get the URI using Spring Data REST's {@link EntityLinks}.
     *
     * @param employee
     */
    private String getPath(Employee employee) {
        return this.entityLinks.linkForItemResource(employee.getClass(),
                employee.getId()).toUri().getPath();
    }

}
