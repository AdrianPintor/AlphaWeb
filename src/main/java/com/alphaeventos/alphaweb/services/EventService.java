package com.alphaeventos.alphaweb.services;

import com.alphaeventos.alphaweb.models.Event;
import com.alphaeventos.alphaweb.repository.ContractRepository;
import com.alphaeventos.alphaweb.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ContractRepository contractRepository;

    public EventService(EventRepository eventRepository, ContractRepository contractRepository) {
        this.eventRepository = eventRepository;
        this.contractRepository = contractRepository;
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public void delete(Event event) {
        eventRepository.delete(event);
    }

    public void deleteAll() {
        eventRepository.deleteAll();
        contractRepository.deleteAll();
    }
}
