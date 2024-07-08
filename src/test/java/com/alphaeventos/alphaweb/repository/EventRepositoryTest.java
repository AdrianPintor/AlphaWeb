package com.alphaeventos.alphaweb.repository;

import com.alphaeventos.alphaweb.models.Event;
import com.alphaeventos.alphaweb.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
