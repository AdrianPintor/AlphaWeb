package com.alphaeventos.alphaweb.controller;

import com.alphaeventos.alphaweb.models.Performance;
import com.alphaeventos.alphaweb.services.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/performances")
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;

    @GetMapping
    public ResponseEntity<List<Performance>> getAllPerformances() {
        List<Performance> performances = performanceService.findAll();
        return new ResponseEntity<>(performances, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Performance> getPerformanceById(@PathVariable Long id) {
        Performance performance = performanceService.findById(id).orElse(null);
        if (performance == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(performance, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Performance> createPerformance(@RequestBody Performance performance) {
        try {
            Performance savedPerformance = performanceService.save(performance);
            return new ResponseEntity<>(savedPerformance, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerformance(@PathVariable Long id) {
        performanceService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
