package com.poc.springbatch.multiitemwriter.step;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import com.poc.springbatch.multiitemwriter.model.Employee;

public class ConsoleWriter implements ItemWriter<Employee> {
  private static final Logger LOGGER = LogManager.getLogger(ConsoleWriter.class);

  @Override
  public void write(List<? extends Employee> items) throws Exception {
    items.stream().forEach(LOGGER::info);
  }

}
