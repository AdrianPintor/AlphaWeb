package com.alphaeventos.alphaweb.repository;

import com.alphaeventos.alphaweb.models.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
