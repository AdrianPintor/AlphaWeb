package com.alphaeventos.alphaweb.repository;

import com.alphaeventos.alphaweb.models.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
}
