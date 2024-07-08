package com.alphaeventos.alphaweb.repository;

import com.alphaeventos.alphaweb.models.Event;
import com.alphaeventos.alphaweb.models.Offer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class OfferRepositoryTest {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private EventRepository eventRepository;

    @Test
    public void testSaveOffer() {
        Event event = new Event();
        event.setName("Test Event");
        event = eventRepository.save(event);

        Offer offer = new Offer();
        offer.setDate(LocalDate.now());
        offer.setTerms("Test Terms");
        offer.setEvent(event);

        Offer savedOffer = offerRepository.save(offer);
        assertNotNull(savedOffer);
        assertNotNull(savedOffer.getId());
        assertEquals("Test Terms", savedOffer.getTerms());
    }
}