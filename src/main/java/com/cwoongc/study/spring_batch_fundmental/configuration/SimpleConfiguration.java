package com.cwoongc.study.spring_batch_fundmental.configuration;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

@EnableBatchProcessing
@Import(BatchConfiguration.class)
@Configuration
public class SimpleConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;



    @Autowired
    private SimpleJobLauncher simpleJobLauncher;

//    @Scheduled(fixedRate = 3000)
//    public void doWork() {
//        System.out.println("최웅철");
//    }

    @Scheduled(cron = "0/5 * * * * *")
    public void launchSimpleJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("JobID",String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        simpleJobLauncher.run(job(),jobParameters);



    }





    @Bean
    public Job job() {
        return jobBuilderFactory.get("simple-job")
                .start(step())
                .build();
    }


    @Bean
    public Step step() {
        return stepBuilderFactory.get("simple-step")
                .<String, StringWrapper>chunk(10)
                .reader(itemReader())
                .processor(itemProcess())
                .writer(itemWriter())
                .build();
    }


    private ItemReader<String> itemReader() {
        List<String> list = new ArrayList<>();

        for(int i=0;i<100;i++) {
            list.add("test"+i);
        }

        return new ListItemReader<String>(list);
    }

    private ItemProcessor<String, StringWrapper> itemProcess() {
        return StringWrapper::new;
    }


    private ItemWriter<StringWrapper> itemWriter() {
        return System.out::println;
    }

    private class StringWrapper {
        private String value;

        public StringWrapper(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "StringWrapper{" +
                    "value='" + value + '\'' +
                    '}';
        }
    }


}
