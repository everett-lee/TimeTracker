package com.timetracker.timetracker.repository;

import com.timetracker.timetracker.model.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client, Long> {
    List<Client> findAllByOwnerId(Long ownerId);
}
