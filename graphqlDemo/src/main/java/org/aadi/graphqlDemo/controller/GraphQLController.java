package org.aadi.graphqlDemo.controller;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.aadi.graphqlDemo.entity.Department;
import org.aadi.graphqlDemo.entity.Employee;
import org.aadi.graphqlDemo.input.AddEmployeeInput;
import org.aadi.graphqlDemo.input.UpdateSalaryInput;
import org.aadi.graphqlDemo.repository.DepartmentRepository;
import org.aadi.graphqlDemo.repository.EmployeeRepository;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
public class GraphQLController {

	private final EmployeeRepository employeeRepository;
	private final DepartmentRepository departmentRepository;
	private final HttpGraphQlClient httpGraphQlClient;

	Function<AddEmployeeInput, Employee> mapping = aei -> {
		var employee = new Employee();
		employee.setName(aei.getName());
		employee.setSalary(aei.getSalary());
		employee.setDepartmentId(aei.getDepartmentId());
		return employee;
	};

	@GetMapping("/employeeByName")
	public Mono<List<Employee>> employeeByName() {
		var document = "query {\n" +
				"  employeeByName(employeeName: \"Aadi\") {\n" +
				"    empId, name, salary\n" +
				"  }\n" +
				"}";
		return this.httpGraphQlClient.document(document)
				.retrieve("employeeByName")
				.toEntityList(Employee.class);
	}

	@MutationMapping
	public Mono<Employee> addEmployee(@Argument AddEmployeeInput addEmployeeInput) {
		return this.employeeRepository.save(mapping.apply(addEmployeeInput));
	}

	@QueryMapping
	public Flux<Employee> employeeByName(@Argument String employeeName) {
		return this.employeeRepository.getEmployeeByName(employeeName);
	}

	@MutationMapping
	public Mono<Employee> updateSalary(@Argument UpdateSalaryInput updateSalaryInput) {
		return this.employeeRepository.findById(updateSalaryInput.getEmployeeId())
				.flatMap(employee -> {
					employee.setSalary(updateSalaryInput.getSalary());
					return this.employeeRepository.save(employee);
				});
	}

	@QueryMapping
	public Flux<Department> allDepartment() {
		return this.departmentRepository.findAll();
	}

	@BatchMapping
	public Mono<Map<Department, Collection<Employee>>> employees(List<Department> departments) {
		return Flux.fromIterable(departments)
				.flatMap(department -> this.employeeRepository.getAllEmployeeByDepartmentId(department.getDeptId()))
				.collectMultimap(employee -> departments.stream().filter(department -> department.getDeptId().equals(employee.getDepartmentId())).findFirst().get());
	}

	@SubscriptionMapping
	public Flux<Employee> allEmployee() {
		return this.employeeRepository.findAll().delayElements(Duration.ofSeconds(3));
	}

}