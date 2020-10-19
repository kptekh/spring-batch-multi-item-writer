package com.poc.springbatch.multiitemwriter.config;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import com.poc.springbatch.multiitemwriter.model.Employee;
import com.poc.springbatch.multiitemwriter.step.ConsoleWriter;
import com.poc.springbatch.multiitemwriter.step.DBWriter;


@Component
public class BatchConfig {
  private static final Logger LOGGER = LogManager.getLogger(BatchConfig.class);

  @Autowired
  private JobBuilderFactory jobBuilderFactory;
  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Value("classpath*:input/input*.csv")
  private Resource[] inputResources;

  @Bean
  public Job readCSVData(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
      ItemReader<Employee> itemReader, ItemWriter<Employee> itemWriter) {
    Step step = stepBuilderFactory.get("step").<Employee, Employee>chunk(2)
        .reader(multiResourceItemReader()).writer(getCompositeWriter()).build();

    return jobBuilderFactory.get("CSV-Reader").incrementer(new RunIdIncrementer()).start(step)
        .build();
  }


  @Bean
  public CompositeItemWriter<? super Employee> getCompositeWriter() {
    CompositeItemWriter<Employee> compositeItemWriter = new CompositeItemWriter<>();
    List<ItemWriter<? super Employee>> writerList = new ArrayList<ItemWriter<? super Employee>>();
    writerList.add(getConsoleWriter());
    writerList.add(writer());
    compositeItemWriter.setDelegates(writerList);
    return compositeItemWriter;
  }

  @Bean
  @Qualifier(value = "consoleWriter")
  public ItemWriter<Employee> getConsoleWriter() {
    ItemWriter<Employee> writer = new ConsoleWriter();
    return writer;
  }

  @Bean
  @Primary
  @Qualifier(value = "dbWriter")
  public ItemWriter<Employee> getDbWriter() {
    ItemWriter<Employee> writer = new DBWriter();
    return writer;
  }



  @Bean
  @Qualifier("outputfile")
  public FlatFileItemWriter<Employee> writer() {
    FlatFileItemWriter<Employee> fileItemWriter = new FlatFileItemWriter<>();
    fileItemWriter.setResource(new FileSystemResource("output/output.csv"));
    int i = 0;
    while (i < inputResources.length) {
      fileItemWriter.setAppendAllowed(true);
      i++;
    }
    fileItemWriter.setAppendAllowed(false);
    fileItemWriter.setLineAggregator(getLineAggregator());
    return fileItemWriter;
  }

  @Bean
  public LineAggregator<Employee> getLineAggregator() {
    DelimitedLineAggregator<Employee> lineAggregator = new DelimitedLineAggregator<Employee>();
    lineAggregator.setDelimiter(",");
    lineAggregator.setFieldExtractor(getFiledExtractor());
    return lineAggregator;
  }

  @Bean
  public FieldExtractor<Employee> getFiledExtractor() {
    BeanWrapperFieldExtractor beanWrapperFieldExtractor = new BeanWrapperFieldExtractor();
    beanWrapperFieldExtractor.setNames(new String[] {"id", "firstName", "lastName", "city"});
    beanWrapperFieldExtractor.afterPropertiesSet();
    return beanWrapperFieldExtractor;
  }

  @Bean
  public ResourceAwareItemReaderItemStream reader() {
    FlatFileItemReader<Employee> flatFileItemReader = new FlatFileItemReader<Employee>();
    flatFileItemReader.setLinesToSkip(1);
    flatFileItemReader.setLineMapper(getLineMapper());
    return flatFileItemReader;
  }

  @Bean
  public LineMapper<Employee> getLineMapper() {
    DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<Employee>();
    BeanWrapperFieldSetMapper<Employee> fieldSetMapper = new BeanWrapperFieldSetMapper<Employee>();

    fieldSetMapper.setTargetType(Employee.class);
    lineMapper.setFieldSetMapper(fieldSetMapper);

    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
    tokenizer.setNames(new String[] {"id", "firstName", "lastName", "city"});
    tokenizer.setIncludedFields(new int[] {0, 1, 2, 3});
    tokenizer.setDelimiter(",");
    tokenizer.setStrict(true);
    lineMapper.setLineTokenizer(tokenizer);

    return lineMapper;
  }

  @Bean
  public MultiResourceItemReader<Employee> multiResourceItemReader() {
    MultiResourceItemReader multiResourceItemReader = new MultiResourceItemReader();
    for (Resource res : inputResources) {
      LOGGER.info("multiResourceItemReader multiResourceItemReader " + res);
    }
    multiResourceItemReader.setResources(inputResources);
    multiResourceItemReader.setStrict(true);
    multiResourceItemReader.setDelegate(reader());
    return multiResourceItemReader;
  }
}
