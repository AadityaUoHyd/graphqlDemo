package org.aadi.graphqlDemo.repository;

import org.aadi.graphqlDemo.entity.Department;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface DepartmentRepository extends ReactiveCrudRepository<Department, Integer> {
	
}