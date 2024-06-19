package com.alphaeventos.alphaweb.repository;

import com.alphaeventos.alphaweb.models.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
}
