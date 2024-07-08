package com.alphaeventos.alphaweb.services;

import com.alphaeventos.alphaweb.models.Performance;
import com.alphaeventos.alphaweb.repository.PerformanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PerformanceService {

    private final PerformanceRepository performanceRepository;

    @Autowired
    public PerformanceService(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
    }

    public List<Performance> findAll() {
        return performanceRepository.findAll();
    }

    public Optional<Performance> findById(Long id) {
        return performanceRepository.findById(id);
    }

    public Performance save(Performance performance) {
        return performanceRepository.save(performance);
    }

    public void deleteById(Long id) {
        performanceRepository.deleteById(id);
    }
}