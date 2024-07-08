package com.alphaeventos.alphaweb.service;

import com.alphaeventos.alphaweb.models.Artist;
import com.alphaeventos.alphaweb.models.Contract;
import com.alphaeventos.alphaweb.models.Event;
import com.alphaeventos.alphaweb.models.User;
import com.alphaeventos.alphaweb.services.ArtistService;
import com.alphaeventos.alphaweb.services.ContractService;
import com.alphaeventos.alphaweb.services.EventService;
import com.alphaeventos.alphaweb.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ContractServiceTest {

    @Autowired
    private ContractService contractService;

    @Autowired
    private UserService userService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private EventService eventService;

    @Test
    public void testSaveContract() {
        User user = new User();
        user.setEnterpriseName("Test Enterprise");
        user = userService.save(user);

        Artist artist = new Artist();
        artist.setArtisticName("Test Artist");
        artist = artistService.save(artist);

        Event event = new Event();
        event.setName("Test Event");
        event = eventService.save(event);

        Contract contract = new Contract();
        contract.setDate(LocalDate.now());
        contract.setTerms("Test Terms");
        contract.setUser(user);
        contract.setArtist(artist);
        contract.setEvent(event);

        Contract savedContract = contractService.save(contract);
        assertNotNull(savedContract);
        assertNotNull(savedContract.getId());
        assertEquals("Test Terms", savedContract.getTerms());
    }
}