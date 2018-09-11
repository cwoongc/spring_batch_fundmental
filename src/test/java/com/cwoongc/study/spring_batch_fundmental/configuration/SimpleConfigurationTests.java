package com.cwoongc.study.spring_batch_fundmental.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {
//        SimpleConfiguration.class,
//        JobRunnerConfiguration.class
//})

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        SimpleConfiguration.class,
        JobRunnerConfiguration.class
})
public class SimpleConfigurationTests {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void testLaunchJob() throws Exception {
        jobLauncherTestUtils.launchJob();

    }




}
