package com.poc.springbatch.multiitemwriter.step;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import com.poc.springbatch.multiitemwriter.model.Employee;
import com.poc.springbatch.multiitemwriter.repository.EmployeeRepository;

public class DBWriter implements ItemWriter<Employee> {
  private static final Logger LOGGER = LogManager.getLogger(DBWriter.class);

  @Autowired
  private EmployeeRepository employeeRepository;

  @Override
  public void write(List<? extends Employee> items) throws Exception {
    LOGGER.info("Saving into DB");
    employeeRepository.saveAll(items);
  }

}
