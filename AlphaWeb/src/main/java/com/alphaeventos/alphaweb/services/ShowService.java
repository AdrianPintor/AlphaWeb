package com.alphaeventos.alphaweb.services;

import com.alphaeventos.alphaweb.models.Show;
import com.alphaeventos.alphaweb.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    public List<Show> findAll() {
        return showRepository.findAll();
    }

    public Optional<Show> findById(Long id) {
        return showRepository.findById(id);
    }

    public Show save(Show show) {
        return showRepository.save(show);
    }

    public void deleteById(Long id) {
        showRepository.deleteById(id);
    }
}
