package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.ToolRecord;
import org.springframework.data.repository.CrudRepository;

public interface ToolRepository extends CrudRepository<ToolRecord, String> {

}
