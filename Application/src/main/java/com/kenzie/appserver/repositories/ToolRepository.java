package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.ToolRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ToolRepository extends CrudRepository<ToolRecord, String> {

}
