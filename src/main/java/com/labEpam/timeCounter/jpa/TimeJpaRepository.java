package com.labEpam.timeCounter.jpa;

import com.labEpam.timeCounter.jpa.model.ValuesDB;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeJpaRepository extends JpaRepository<ValuesDB, Integer> {
}
