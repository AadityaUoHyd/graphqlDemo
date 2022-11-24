package org.aadi.graphqlDemo.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    private Integer deptId;
    private String name;
    private List<Employee> employees = new ArrayList<>();
}