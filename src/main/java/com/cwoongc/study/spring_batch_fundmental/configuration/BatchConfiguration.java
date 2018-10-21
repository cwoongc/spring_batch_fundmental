package com.cwoongc.study.spring_batch_fundmental.configuration;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@EnableScheduling
@Configuration
public class BatchConfiguration {

    /**
     * spring-batch의 JobRepositoryFactoryBean 을 Bean으로 등록
     *
     * @param dataSource
     * @param transactionManager
     * @return
     * @throws Exception
     */
    @Bean
    public JobRepositoryFactoryBean jobRepositoryFactoryBean(DataSource dataSource
            , PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();

        factoryBean.setDataSource(dataSource);
        factoryBean.setTransactionManager(transactionManager);
        factoryBean.afterPropertiesSet();

        return factoryBean;
    }

    /**
     * spring-batch의 JobRepositryFactoryBean을 context에서 꺼내
     * JobRepository를 Bean으로 등록
     * @param factoryBean
     * @return
     * @throws Exception
     */
    @Bean
    public JobRepository jobRepository(JobRepositoryFactoryBean factoryBean) throws Exception {
        return factoryBean.getObject();
    }


    /**
     * spring-batch의 JobRepository를 context에서 꺼내
     * JobBuilderFactory 인스턴스에 주입하여 Bean으로 등록
     *  JobBuilderFactory -> JobRepository -> DataSource
     *                                     -> TransactionManager
     * @param jobRepository
     * @return
     */
    @Bean
    public JobBuilderFactory jobBuilderFactory(JobRepository jobRepository) {
        return new JobBuilderFactory(jobRepository);
    }

    /**
     * StepBuilderFactory -> JobRepository -> DataSource, TransactionManager
     *                    -> TransactionManager
     * @param jobRepository
     * @param transactionManager
     * @return
     */
    @Bean
    public StepBuilderFactory stepBuilderFactory(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilderFactory(jobRepository, transactionManager);
    }

    /**
     * JobLauncher : Job과 JobParameter를 run()메소드의 인수로 받아 Job을 실행하는 런쳐 객체 @Bean 등록
     * SimpleJobLauncher -> JobRepository -> DataSource, TransactionManager
     * @param jobRepository
     * @return
     */
    @Bean
    public SimpleJobLauncher simpleJobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }

    /**
     *
     * JobExplorerFactoryBean -> DataSource
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean
    public JobExplorerFactoryBean jobExplorerFactoryBean(DataSource dataSource) throws Exception {
        JobExplorerFactoryBean factoryBean = new JobExplorerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.afterPropertiesSet();
        return factoryBean;
    }

    /**
     * JobExplorer : 수행된 JobInstance-[JobExecution, StepInstance-[StepExecution 등을 검색해오는데 사용되는 인터페이스
     * @param factoryBean
     * @return
     * @throws Exception
     */
    @Bean
    public JobExplorer jobExplorer(JobExplorerFactoryBean factoryBean) throws Exception {
        return factoryBean.getObject();
    }




}
