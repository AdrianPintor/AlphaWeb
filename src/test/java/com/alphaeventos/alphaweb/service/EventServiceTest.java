package com.alphaeventos.alphaweb.service;

import com.alphaeventos.alphaweb.models.Event;
import com.alphaeventos.alphaweb.services.EventService;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EventServiceTest {

    @Autowired
    private EventService eventService;

    private Event event;

    @BeforeEach
    public void setup() throws MalformedURLException {
        event = new Event();
        event.setName("Test Event");
        event.setInformation("Some information about the event");
        event.setPhotosVideos(new URL("http://example.com/videos"));
        event.setEnterpriseCollabs("Enterprise collaborations");
        event.setDescriptionRequest("Description request details");
    }

    @Test
    public void testSaveEvent() {
        Event savedEvent = eventService.save(event);

        assertNotNull(savedEvent);
        assertNotNull(savedEvent.getId());
        assertEquals("Test Event", savedEvent.getName());
        assertEquals("Some information about the event", savedEvent.getInformation());
        assertEquals("http://example.com/videos", savedEvent.getPhotosVideos().toString());
        assertEquals("Enterprise collaborations", savedEvent.getEnterpriseCollabs());
        assertEquals("Description request details", savedEvent.getDescriptionRequest());
    }

    @Test
    public void testFindAllEvents() {
        eventService.save(event);
        List<Event> events = eventService.findAll();
        assertNotNull(events);
        assertFalse(events.isEmpty());
    }

    @Test
    public void testFindEventById() {
        Event savedEvent = eventService.save(event);
        Optional<Event> foundEvent = eventService.findById(savedEvent.getId());
        assertTrue(foundEvent.isPresent());
        assertEquals(savedEvent.getName(), foundEvent.get().getName());
    }

    @Test
    public void testDeleteEvent() {
        Event savedEvent = eventService.save(event);
        eventService.delete(savedEvent);
        Optional<Event> deletedEvent = eventService.findById(savedEvent.getId());
        assertFalse(deletedEvent.isPresent());
    }
}