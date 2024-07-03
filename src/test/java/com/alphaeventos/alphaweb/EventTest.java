package com.alphaeventos.alphaweb;

import com.alphaeventos.alphaweb.models.Event;
import com.alphaeventos.alphaweb.models.User;
import com.alphaeventos.alphaweb.repository.EventRepository;
import com.alphaeventos.alphaweb.repository.UserRepository;
import com.alphaeventos.alphaweb.services.EventService;
import com.alphaeventos.alphaweb.services.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveEvent() throws MalformedURLException {
        User user = new User();
        user.setEnterpriseName("Test Enterprise");
        user = userRepository.save(user);

        Event event = new Event();
        event.setName("Test Event");
        event.setInformation("Test Information");
        event.setPhotosVideos(new URL("http://example.com/event"));
        event.setEnterpriseCollabs("Test Collaborations");
        event.setDescriptionRequest("Test Description Request");
        event.setUser(user);

        Event savedEvent = eventRepository.save(event);
        assertNotNull(savedEvent);
        assertNotNull(savedEvent.getId());
        assertEquals("Test Event", savedEvent.getName());
    }
}
@SpringBootTest
@Transactional
class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Test
    public void testSaveEvent() throws MalformedURLException {
        User user = new User();
        user.setEnterpriseName("Test Enterprise");
        user = userService.save(user);

        Event event = new Event();
        event.setName("Test Event");
        event.setInformation("Test Information");
        event.setPhotosVideos(new URL("http://example.com/event"));
        event.setEnterpriseCollabs("Test Collaborations");
        event.setDescriptionRequest("Test Description Request");
        event.setUser(user);

        Event savedEvent = eventService.save(event);
        assertNotNull(savedEvent);
        assertNotNull(savedEvent.getId());
        assertEquals("Test Event", savedEvent.getName());
    }
}
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class EventControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setEnterpriseName("Test Enterprise");
        user = userService.save(user);
    }

    @Test
    public void testGetAllEvents() {
        ResponseEntity<List> response = restTemplate.getForEntity("/events", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Event> events = response.getBody();
        assertNotNull(events);
    }

    @Test
    public void testGetEventById() throws MalformedURLException {
        Event event = new Event();
        event.setName("Test Event");
        event.setInformation("Test Information");
        event.setPhotosVideos(new URL("http://example.com/event"));
        event.setEnterpriseCollabs("Test Collaborations");
        event.setDescriptionRequest("Test Description Request");
        event.setUser(user);

        Event savedEvent = eventService.save(event);
        ResponseEntity<Event> response = restTemplate.getForEntity("/events/" + savedEvent.getId(), Event.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Event fetchedEvent = response.getBody();
        assertNotNull(fetchedEvent);
        assertEquals("Test Event", fetchedEvent.getName());
    }

    @Test
    public void testCreateEvent() throws MalformedURLException {
        Event event = new Event();
        event.setName("Test Event");
        event.setInformation("Test Information");
        event.setPhotosVideos(new URL("http://example.com/event"));
        event.setEnterpriseCollabs("Test Collaborations");
        event.setDescriptionRequest("Test Description Request");
        event.setUser(user);

        ResponseEntity<Event> response = restTemplate.postForEntity("/events", event, Event.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Event createdEvent = response.getBody();
        assertNotNull(createdEvent.getId());
        assertEquals("Test Event", createdEvent.getName());
    }
}