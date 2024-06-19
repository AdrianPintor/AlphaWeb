package com.alphaeventos.alphaweb.repository;

import com.alphaeventos.alphaweb.models.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
}
