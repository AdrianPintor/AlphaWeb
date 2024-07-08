package com.alphaeventos.alphaweb.controller;

import com.alphaeventos.alphaweb.models.Performance;
import com.alphaeventos.alphaweb.services.PerformanceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/performances")
public class PerformanceController {

    private final PerformanceService performanceService;
    private final ObjectMapper objectMapper;

    @Autowired
    public PerformanceController(PerformanceService performanceService, ObjectMapper objectMapper) {
        this.performanceService = performanceService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<List<Performance>> getAllPerformances() {
        List<Performance> performances = performanceService.findAll();
        return new ResponseEntity<>(performances, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Performance> getPerformanceById(@PathVariable Long id) {
        Optional<Performance> performance = performanceService.findById(id);
        return performance.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Performance> createPerformance(@RequestBody Performance performance) {
        Performance createdPerformance = performanceService.save(performance);
        return new ResponseEntity<>(createdPerformance, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Performance> updatePerformance(@PathVariable Long id, @RequestBody Performance performanceDetails) {
        Optional<Performance> optionalPerformance = performanceService.findById(id);

        if (optionalPerformance.isPresent()) {
            Performance existingPerformance = optionalPerformance.get();
            existingPerformance.setDate(performanceDetails.getDate());
            existingPerformance.setLocation(performanceDetails.getLocation());
            existingPerformance.setArtistCalendar(performanceDetails.getArtistCalendar());

            Performance updatedPerformance = performanceService.save(existingPerformance);
            return ResponseEntity.ok(updatedPerformance);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerformance(@PathVariable Long id) {
        Optional<Performance> performance = performanceService.findById(id);

        if (performance.isPresent()) {
            performanceService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}