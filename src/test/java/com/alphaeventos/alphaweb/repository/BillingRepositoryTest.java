package com.alphaeventos.alphaweb.repository;

import com.alphaeventos.alphaweb.models.Billing;
import com.alphaeventos.alphaweb.models.User;
import com.alphaeventos.alphaweb.models.Bill;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BillingRepositoryTest {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveBilling() {
        User user = new User();
        user.setEnterpriseName("Test Enterprise");
        user = userRepository.save(user);

        Billing billing = new Billing();
        billing.setDniCif("12345678A");
        billing.setBillingAddress("123 Test Street");
        billing.setBillstatus(Bill.PAID);
        billing.setUser(user);

        Billing savedBilling = billingRepository.save(billing);
        assertNotNull(savedBilling);
        assertNotNull(savedBilling.getId());
        assertEquals("12345678A", savedBilling.getDniCif());
        assertEquals("123 Test Street", savedBilling.getBillingAddress());
        assertEquals(Bill.PAID, savedBilling.getBillstatus());
        assertEquals(user.getId(), savedBilling.getUser().getId());
    }

    @Test
    void testFindBillingById() {
        User user = new User();
        user.setEnterpriseName("Test Enterprise");
        user = userRepository.save(user);

        Billing billing = new Billing();
        billing.setDniCif("12345678A");
        billing.setBillingAddress("123 Test Street");
        billing.setBillstatus(Bill.PAID);
        billing.setUser(user);

        Billing savedBilling = billingRepository.save(billing);
        Billing foundBilling = billingRepository.findById(savedBilling.getId()).orElse(null);
        assertNotNull(foundBilling);
        assertEquals("12345678A", foundBilling.getDniCif());
        assertEquals("123 Test Street", foundBilling.getBillingAddress());
        assertEquals(Bill.PAID, foundBilling.getBillstatus());
        assertEquals(user.getId(), foundBilling.getUser().getId());
    }

    @Test
    void testDeleteBilling() {
        User user = new User();
        user.setEnterpriseName("Test Enterprise");
        user = userRepository.save(user);

        Billing billing = new Billing();
        billing.setDniCif("12345678A");
        billing.setBillingAddress("123 Test Street");
        billing.setBillstatus(Bill.PAID);
        billing.setUser(user);

        Billing savedBilling = billingRepository.save(billing);
        billingRepository.deleteById(savedBilling.getId());
        assertFalse(billingRepository.findById(savedBilling.getId()).isPresent());
    }
}
