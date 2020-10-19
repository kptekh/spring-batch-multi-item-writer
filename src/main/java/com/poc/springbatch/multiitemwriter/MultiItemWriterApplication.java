package com.poc.springbatch.multiitemwriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
@EnableAutoConfiguration
@EnableJpaRepositories
public class MultiItemWriterApplication {

  private static final Logger LOGGER = LogManager.getLogger(MultiItemWriterApplication.class);

  @Autowired
  private JobLauncher jobLauncher;
  @Autowired
  private Job job;

  public static void main(String[] args) {
    SpringApplication.run(MultiItemWriterApplication.class, args);
  }

  @Scheduled(cron = "* */1 * * * ?")
  public void perform() throws JobExecutionAlreadyRunningException, JobRestartException,
      JobInstanceAlreadyCompleteException, JobParametersInvalidException {
    JobParameters jobParameters =
        new JobParametersBuilder().addLong("JobId", System.currentTimeMillis()).toJobParameters();
    JobExecution execution = jobLauncher.run(job, jobParameters);
    LOGGER.info("execution status " + execution.getStatus().name());
  }

}
