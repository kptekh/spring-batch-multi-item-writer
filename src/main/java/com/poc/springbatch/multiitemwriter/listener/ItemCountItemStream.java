package com.poc.springbatch.multiitemwriter.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;

public class ItemCountItemStream implements ItemStream {

  private static final Logger LOGGER = LogManager.getLogger(ItemCountItemStream.class);

  @Override
  public void open(ExecutionContext executionContext) throws ItemStreamException {

  }

  @Override
  public void update(ExecutionContext executionContext) throws ItemStreamException {
    LOGGER.info("Count " + executionContext.get("FlatFileItemReader.read.count"));
  }

  @Override
  public void close() throws ItemStreamException {

  }
}
