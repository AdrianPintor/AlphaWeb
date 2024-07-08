package com.alphaeventos.alphaweb.controller;

import com.alphaeventos.alphaweb.models.Contract;
import com.alphaeventos.alphaweb.services.ContractService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contracts")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping
    public ResponseEntity<List<Contract>> getAllContracts() {
        List<Contract> contracts = contractService.findAll();
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContractById(@PathVariable Long id) {
        Optional<Contract> contract = contractService.findById(id);
        return contract.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Contract> createContract(@RequestBody Contract contract) {
        Contract savedContract = contractService.save(contract);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedContract);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contract> updateContract(@PathVariable Long id, @RequestBody Contract contract) {
        Contract existingContract = contractService.findById(id).orElse(null);
        if (existingContract == null) {
            return ResponseEntity.notFound().build();
        }
        existingContract.setDate(contract.getDate());
        existingContract.setTerms(contract.getTerms());
        existingContract.setUser(contract.getUser());
        existingContract.setArtist(contract.getArtist());
        existingContract.setEvent(contract.getEvent());

        Contract updatedContract = contractService.save(existingContract);
        return ResponseEntity.ok(updatedContract);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        Contract contract = contractService.findById(id).orElse(null);
        if (contract == null) {
            return ResponseEntity.notFound().build();
        }
        contractService.delete(contract);
        return ResponseEntity.noContent().build();
    }
}
