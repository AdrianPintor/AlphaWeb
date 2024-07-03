package com.alphaeventos.alphaweb;

import com.alphaeventos.alphaweb.models.Artist;
import com.alphaeventos.alphaweb.models.Contract;
import com.alphaeventos.alphaweb.models.Event;
import com.alphaeventos.alphaweb.models.User;
import com.alphaeventos.alphaweb.repository.ArtistRepository;
import com.alphaeventos.alphaweb.repository.ContractRepository;
import com.alphaeventos.alphaweb.repository.EventRepository;
import com.alphaeventos.alphaweb.repository.UserRepository;
import com.alphaeventos.alphaweb.services.ArtistService;
import com.alphaeventos.alphaweb.services.ContractService;
import com.alphaeventos.alphaweb.services.EventService;
import com.alphaeventos.alphaweb.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ContractControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ContractService contractService;

    private User user;
    private Artist artist;
    private Event event;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setEnterpriseName("Test Enterprise");
        user = userService.save(user);

        artist = new Artist();
        artist.setArtisticName("Test Artist");
        artist = artistService.save(artist);

        event = new Event();
        event.setName("Test Event");
        event = eventService.save(event);
    }

    @Test
    public void testGetAllContracts() {
        ResponseEntity<List> response = restTemplate.getForEntity("/contracts", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Contract> contracts = response.getBody();
        assertNotNull(contracts);
    }

    @Test
    public void testGetContractById() {
        Contract contract = new Contract();
        contract.setDate(LocalDate.now());
        contract.setTerms("Test Terms");
        contract.setUser(user);
        contract.setArtist(artist);
        contract.setEvent(event);

        Contract savedContract = contractService.save(contract);
        ResponseEntity<Contract> response = restTemplate.getForEntity("/contracts/" + savedContract.getId(), Contract.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Contract fetchedContract = response.getBody();
        assertNotNull(fetchedContract);
        assertEquals("Test Terms", fetchedContract.getTerms());
    }

    @Test
    public void testCreateContract() {
        Contract contract = new Contract();
        contract.setDate(LocalDate.now());
        contract.setTerms("Test Terms");
    }
}