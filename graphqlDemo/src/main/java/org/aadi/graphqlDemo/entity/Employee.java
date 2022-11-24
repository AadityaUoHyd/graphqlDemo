package org.aadi.graphqlDemo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    private Integer empId;
    private String name, salary;
	@Column("department_id")
    private Integer departmentId;
}