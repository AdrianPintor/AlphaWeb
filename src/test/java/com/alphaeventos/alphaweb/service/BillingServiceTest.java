package com.alphaeventos.alphaweb.service;

import com.alphaeventos.alphaweb.models.Bill;
import com.alphaeventos.alphaweb.models.Billing;
import com.alphaeventos.alphaweb.models.User;
import com.alphaeventos.alphaweb.repository.UserRepository;
import com.alphaeventos.alphaweb.services.BillingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BillingServiceTest {

    @Autowired
    private BillingService billingService;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Billing billing;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        userRepository.save(user);

        billing = new Billing();
        billing.setDniCif("12345678A");
        billing.setBillingAddress("123 Main St");
        billing.setBillstatus(Bill.PAID);
        billing.setUser(user);
    }

    @Test
    public void testSaveBilling() {
        Billing savedBilling = billingService.save(billing);
        assertNotNull(savedBilling);
        assertNotNull(savedBilling.getId());
        assertEquals("12345678A", savedBilling.getDniCif());
    }

    @Test
    public void testFindAllBillings() {
        billingService.save(billing);
        List<Billing> billings = billingService.findAll();
        assertNotNull(billings);
        assertFalse(billings.isEmpty());
    }

    @Test
    public void testFindBillingById() {
        Billing savedBilling = billingService.save(billing);
        Optional<Billing> foundBilling = billingService.findById(savedBilling.getId());
        assertTrue(foundBilling.isPresent());
        assertEquals("12345678A", foundBilling.get().getDniCif());
    }

    @Test
    public void testDeleteBillingById() {
        Billing savedBilling = billingService.save(billing);
        billingService.deleteById(savedBilling.getId());
        Optional<Billing> foundBilling = billingService.findById(savedBilling.getId());
        assertFalse(foundBilling.isPresent());
    }
}
