package com.jverson.quartz.conf;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.jverson.quartz.job.QuartzJobFactory;
import com.jverson.quartz.job.ScheduleJob;
import com.jverson.quartz.spring.AutowiringSpringBeanJobFactory;


/**
 * @author Jverson
 * @date Dec 12, 2017 9:39:31 AM
 */
@Configuration
public class SchedulerConfig {
	
    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext)
    {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public Scheduler schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory) throws Exception {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // this allows to update triggers in DB when updating settings in config file
        factory.setOverwriteExistingJobs(true);
        factory.setDataSource(dataSource);
        // use specify jobFactory to create jobDetail
        factory.setJobFactory(jobFactory);

        factory.setQuartzProperties(quartzProperties());
        factory.afterPropertiesSet();

        Scheduler scheduler = factory.getScheduler();
        scheduler.setJobFactory(jobFactory);
        
        // register all jobs
        List<ScheduleJob> jobs = QuartzJobFactory.getInitAllJobs();
        for (ScheduleJob job : jobs) {
        	TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
        	CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        	if (null == trigger) {
        		JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class)
        				.withIdentity(job.getJobName(), job.getJobGroup())
        				.withDescription(job.getDesc()).build();
        		jobDetail.getJobDataMap().put("scheduleJob", job);
        		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
        			.getCronExpression());
        		trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();
        		scheduler.scheduleJob(jobDetail, trigger);
			}else {
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
					.getCronExpression());
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
					.withSchedule(scheduleBuilder).build();
				scheduler.rescheduleJob(triggerKey, trigger);
			}
		}

        scheduler.start();
        return scheduler;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

}
