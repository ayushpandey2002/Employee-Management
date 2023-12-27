package com.global_real;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends CouchbaseRepository<EmployeeModel, String> {
}
   
