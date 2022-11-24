package org.aadi.graphqlDemo.repository;

import org.aadi.graphqlDemo.entity.Employee;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;

public interface EmployeeRepository extends ReactiveCrudRepository<Employee, Integer> {
	Flux<Employee> getEmployeeByName(String name);
	Flux<Employee> getAllEmployeeByDepartmentId(Integer departmentId);
}