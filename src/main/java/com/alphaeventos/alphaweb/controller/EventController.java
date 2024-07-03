package com.alphaeventos.alphaweb.controller;

import com.alphaeventos.alphaweb.models.Event;
import com.alphaeventos.alphaweb.services.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.findAll();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Event event = eventService.findById(id).orElse(null);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(event);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event savedEvent = eventService.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        Event existingEvent = eventService.findById(id).orElse(null);
        if (existingEvent == null) {
            return ResponseEntity.notFound().build();
        }
        existingEvent.setName(event.getName());
        existingEvent.setInformation(event.getInformation());
        existingEvent.setPhotosVideos(event.getPhotosVideos());
        existingEvent.setEnterpriseCollabs(event.getEnterpriseCollabs());
        existingEvent.setDescriptionRequest(event.getDescriptionRequest());
        existingEvent.setUser(event.getUser());

        Event updatedEvent = eventService.save(existingEvent);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        Event event = eventService.findById(id).orElse(null);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        eventService.delete(event);
        return ResponseEntity.noContent().build();
    }
}
