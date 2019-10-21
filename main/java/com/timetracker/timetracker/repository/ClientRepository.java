package com.timetracker.timetracker.repository;

import com.timetracker.timetracker.model.Client;
import com.timetracker.timetracker.model.Task;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {
}
