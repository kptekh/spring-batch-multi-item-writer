package com.poc.springbatch.multiitemwriter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.poc.springbatch.multiitemwriter.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

}
