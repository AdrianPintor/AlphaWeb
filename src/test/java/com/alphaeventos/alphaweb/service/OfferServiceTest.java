package com.alphaeventos.alphaweb.services;

import com.alphaeventos.alphaweb.models.Offer;
import com.alphaeventos.alphaweb.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private OfferService offerService;

    private Offer offer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        offer = new Offer();
        offer.setId(1L);
        offer.setDate(LocalDate.now());
        offer.setTerms("Test Terms");
    }

    @Test
    void testFindAll() {
        when(offerRepository.findAll()).thenReturn(Arrays.asList(offer));

        List<Offer> offers = offerService.findAll();
        assertNotNull(offers);
        assertEquals(1, offers.size());
        assertEquals("Test Terms", offers.get(0).getTerms());
    }

    @Test
    void testFindById() {
        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));

        Optional<Offer> foundOffer = offerService.findById(1L);
        assertTrue(foundOffer.isPresent());
        assertEquals("Test Terms", foundOffer.get().getTerms());
    }

    @Test
    void testSave() {
        when(offerRepository.save(offer)).thenReturn(offer);

        Offer savedOffer = offerService.save(offer);
        assertNotNull(savedOffer);
        assertEquals("Test Terms", savedOffer.getTerms());
    }

    @Test
    void testDeleteById() {
        doNothing().when(offerRepository).deleteById(1L);

        offerService.deleteById(1L);
        verify(offerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteAll() {
        doNothing().when(offerRepository).deleteAll();

        offerService.deleteAll();
        verify(offerRepository, times(1)).deleteAll();
    }
}
