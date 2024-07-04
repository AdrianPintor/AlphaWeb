package com.alphaeventos.alphaweb.services;

import com.alphaeventos.alphaweb.models.Billing;
import com.alphaeventos.alphaweb.repository.BillingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillingService {

    @Autowired
    private BillingRepository billingRepository;

    public List<Billing> findAll() {
        return billingRepository.findAll();
    }

    public Optional<Billing> findById(Long id) {
        return billingRepository.findById(id);
    }

    public Billing save(Billing billing) {
        return billingRepository.save(billing);
    }

    public void delete(Long id) {
        billingRepository.deleteById(id);
    }

    public void deleteAll() {
        billingRepository.deleteAll();
    }
}

