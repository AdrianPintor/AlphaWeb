package com.alphaeventos.alphaweb.controller;

import com.alphaeventos.alphaweb.models.Billing;
import com.alphaeventos.alphaweb.models.Event;
import com.alphaeventos.alphaweb.services.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/billings")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @GetMapping
    public List<Billing> getAllBillings() {
        return billingService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Billing> getBillingById(@PathVariable Long id) {
        Optional<Billing> billing = billingService.findById(id);
        return billing.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Billing createBilling(@RequestBody Billing billing) {
        return billingService.save(billing);
    }

    @PutMapping("/{id}")
    public Billing updateBilling(@PathVariable Long id, @RequestBody Billing billing) {
        return billingService.save(billing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBilling(@PathVariable Long id) {
        billingService.deleteById(id);
    }
}