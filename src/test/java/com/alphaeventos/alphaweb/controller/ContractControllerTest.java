package com.alphaeventos.alphaweb.controller;

import com.alphaeventos.alphaweb.models.Artist;
import com.alphaeventos.alphaweb.models.Contract;
import com.alphaeventos.alphaweb.models.Event;
import com.alphaeventos.alphaweb.models.User;
import com.alphaeventos.alphaweb.repository.ArtistRepository;
import com.alphaeventos.alphaweb.repository.ContractRepository;
import com.alphaeventos.alphaweb.repository.EventRepository;
import com.alphaeventos.alphaweb.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class ContractControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private EventRepository eventRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Contract contract;
    private User user;
    private Artist artist;
    private Event event;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        user = new User();
        user.setEnterpriseName("Test Enterprise");
        userRepository.save(user);

        artist = new Artist();
        artist.setArtisticName("Test Artist");
        artistRepository.save(artist);

        event = new Event();
        event.setName("Test Event");
        eventRepository.save(event);

        contract = new Contract();
        contract.setDate(LocalDate.now());
        contract.setTerms("Test Terms");
        contract.setUser(user);
        contract.setArtist(artist);
        contract.setEvent(event);
        contractRepository.save(contract);
    }

    @AfterEach
    void tearDown() {
        contractRepository.deleteAll();
        eventRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testGetAllContracts() throws Exception {
        MvcResult result = mockMvc.perform(get("/contracts"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Test Terms"));
    }

    @Test
    void testGetContractById() throws Exception {
        MvcResult result = mockMvc.perform(get("/contracts/{id}", contract.getId()))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Test Terms"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateContract() throws Exception {
        Contract newContract = new Contract();
        newContract.setDate(LocalDate.now());
        newContract.setTerms("New Terms");
        newContract.setUser(user);
        newContract.setArtist(artist);
        newContract.setEvent(event);
        String body = objectMapper.writeValueAsString(newContract);

        MvcResult result = mockMvc.perform(post("/contracts")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("New Terms"));
    }

    @Test
    void testUpdateContract() throws Exception {
        contract.setTerms("Updated Terms");
        String body = objectMapper.writeValueAsString(contract);

        mockMvc.perform(put("/contracts/{id}", contract.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Contract updated = contractRepository.findById(contract.getId()).get();
        assertEquals("Updated Terms", updated.getTerms());
    }

    @Test
    void testDeleteContract() throws Exception {
        mockMvc.perform(delete("/contracts/{id}", contract.getId()))
                .andExpect(status().isNoContent());

        assertFalse(contractRepository.findById(contract.getId()).isPresent());
    }
}
