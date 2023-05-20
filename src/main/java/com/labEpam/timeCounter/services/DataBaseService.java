package com.labEpam.timeCounter.services;

import com.labEpam.timeCounter.entity.Values;
import com.labEpam.timeCounter.jpa.TimeJpaRepository;
import com.labEpam.timeCounter.jpa.model.ValuesDB;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DataBaseService {
    private TimeJpaRepository repository;
    private static final Logger logger = LogManager.getLogger(DataBaseService.class);

    public DataBaseService(TimeJpaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveValue(Values value) {
        logger.info("Saving value in database");
        ValuesDB entity = new ValuesDB(value.getSpeed(), value.getDistance(), value.getTime());
        repository.save(entity);
        logger.info("Value saved");
    }

    @Transactional
    public void saveValueDB(ValuesDB valuesDB) {
        logger.info("Saving valueDB in database");
        repository.save(valuesDB);
        logger.info("Value saved");
    }

    @Transactional
    public ValuesDB findOne(int id){
        Optional<ValuesDB> foundTime = repository.findById(id);
        return foundTime.orElse(null);
    }

    public List<ValuesDB> getAll() {
        return repository.findAll();
    }

    public void saveAll(List<Values> value) {
        value.forEach(this::saveValue);
    }
}
