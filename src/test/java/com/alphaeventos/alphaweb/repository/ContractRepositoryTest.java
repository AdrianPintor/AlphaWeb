package com.alphaeventos.alphaweb.repository;

import com.alphaeventos.alphaweb.models.Artist;
import com.alphaeventos.alphaweb.models.Contract;
import com.alphaeventos.alphaweb.models.Event;
import com.alphaeventos.alphaweb.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ContractRepositoryTest {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private EventRepository eventRepository;

    @Test
    public void testSaveContract() {
        User user = new User();
        user.setEnterpriseName("Test Enterprise");
        user = userRepository.save(user);

        Artist artist = new Artist();
        artist.setArtisticName("Test Artist");
        artist = artistRepository.save(artist);

        Event event = new Event();
        event.setName("Test Event");
        event = eventRepository.save(event);

        Contract contract = new Contract();
        contract.setDate(LocalDate.now());
        contract.setTerms("Test Terms");
        contract.setUser(user);
        contract.setArtist(artist);
        contract.setEvent(event);

        Contract savedContract = contractRepository.save(contract);
        assertNotNull(savedContract);
        assertNotNull(savedContract.getId());
        assertEquals("Test Terms", savedContract.getTerms());
    }
}
