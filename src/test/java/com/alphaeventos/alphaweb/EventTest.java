package com.alphaeventos.alphaweb;

import com.alphaeventos.alphaweb.models.Artist;
import com.alphaeventos.alphaweb.models.Event;
import com.alphaeventos.alphaweb.models.User;
import com.alphaeventos.alphaweb.repository.ArtistRepository;
import com.alphaeventos.alphaweb.repository.EventRepository;
import com.alphaeventos.alphaweb.repository.UserRepository;
import com.alphaeventos.alphaweb.services.EventService;
import com.alphaeventos.alphaweb.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.WebApplicationContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    public void testDeleteAllEvents() {
        eventService.save(event);
        eventService.deleteAll();
        List<Event> events = eventService.findAll();
        assertTrue(events.isEmpty());
    }
}
@SpringBootTest
@ActiveProfiles("test")
class EventControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private EventRepository eventRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Event event;

    @BeforeEach
    void setUp() throws MalformedURLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        event = new Event();
        event.setName("Test Event");
        event.setInformation("Test Information");
        event.setPhotosVideos(new URL("http://example.com/event"));
        event.setEnterpriseCollabs("Test Collaborations");
        event.setDescriptionRequest("Test Description Request");
        eventRepository.save(event);
    }

    @AfterEach
    void tearDown() {
        eventRepository.deleteAll();
    }

    @Test
    void testGetAllEvents() throws Exception {
        MvcResult result = mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Test Event"));
    }

    @Test
    void testGetEventById() throws Exception {
        MvcResult result = mockMvc.perform(get("/events/{id}", event.getId()))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Test Event"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateEvent() throws Exception {
        Event newEvent = new Event();
        newEvent.setName("New Event");
        newEvent.setInformation("New Information");
        newEvent.setPhotosVideos(new URL("http://example.com/new-event"));
        newEvent.setEnterpriseCollabs("New Collaborations");
        newEvent.setDescriptionRequest("New Description Request");
        String body = objectMapper.writeValueAsString(newEvent);

        MvcResult result = mockMvc.perform(post("/events")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("New Event"));
    }

    @Test
    void testUpdateEvent() throws Exception {
        event.setName("Updated Event Name");
        event.setInformation("Updated Information");
        String body = objectMapper.writeValueAsString(event);

        mockMvc.perform(put("/events/{id}", event.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Event updated = eventRepository.findById(event.getId()).get();
        assertEquals("Updated Event Name", updated.getName());
        assertEquals("Updated Information", updated.getInformation());
    }

    @Test
    void testDeleteEvent() throws Exception {
        mockMvc.perform(delete("/events/{id}", event.getId()))
                .andExpect(status().isNoContent());

        assertFalse(eventRepository.findById(event.getId()).isPresent());
    }
}