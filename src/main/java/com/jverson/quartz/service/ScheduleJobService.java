package com.jverson.quartz.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.base.Preconditions;
import com.jverson.quartz.job.QuartzJobFactory;
import com.jverson.quartz.job.ScheduleJob;

/**
 * job service
 * @author Jverson
 * @date Dec 9, 2017 1:10:47 PM
 */
@Service
public class ScheduleJobService {

	@Autowired 
	private Scheduler scheduler;
	
    public List<ScheduleJob> getAllJobList(){  
        List<ScheduleJob> jobList = new ArrayList<>();  
        try {  
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();  
            Set<JobKey> jobKeySet = scheduler.getJobKeys(matcher);  
            for (JobKey jobKey : jobKeySet){  
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);  
                for (Trigger trigger : triggers){  
                    ScheduleJob scheduleJob = new ScheduleJob();  
                    this.wrapScheduleJob(scheduleJob,scheduler,jobKey,trigger);  
                    jobList.add(scheduleJob);  
                }  
            }  
        } catch (SchedulerException e) {  
        	e.printStackTrace();   
        }  
        return jobList;  
    } 
	
	
    public List<ScheduleJob> getRunningJobList() throws SchedulerException{  
        List<JobExecutionContext> executingJobList = scheduler.getCurrentlyExecutingJobs();  
        List<ScheduleJob> jobList = new ArrayList<>(executingJobList.size());  
        for(JobExecutionContext executingJob : executingJobList){  
            ScheduleJob scheduleJob = new ScheduleJob();  
            JobDetail jobDetail = executingJob.getJobDetail();  
            JobKey jobKey = jobDetail.getKey();  
            Trigger trigger = executingJob.getTrigger();  
            this.wrapScheduleJob(scheduleJob,scheduler,jobKey,trigger);  
            jobList.add(scheduleJob);  
        }  
        return jobList;  
    }
    
    
    public void saveOrupdate(ScheduleJob scheduleJob) throws Exception {
		Preconditions.checkNotNull(scheduleJob, "job is null");
		if (StringUtils.isEmpty(scheduleJob.getJobId())) {
			addJob(scheduleJob);
		}else {
			updateJobCronExpression(scheduleJob);
		}
	} 
    
    private void addJob(ScheduleJob scheduleJob) throws Exception{  
    	checkNotNull(scheduleJob);
		Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getCronExpression()), "CronExpression is null");
		
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());  
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);  
        if(trigger != null){  
            throw new Exception("job already exists!");  
        }
        
        // simulate job info db persist operation
        scheduleJob.setJobId(String.valueOf(QuartzJobFactory.jobList.size()+1));
        QuartzJobFactory.jobList.add(scheduleJob);
        
        JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class).withIdentity(scheduleJob.getJobName(),scheduleJob.getJobGroup()).build();  
        jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);  
  
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());  
        trigger = TriggerBuilder.newTrigger().withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup()).withSchedule(cronScheduleBuilder).build();  
  
        scheduler.scheduleJob(jobDetail, trigger);  
  
    } 
    
    
    public void pauseJob(ScheduleJob scheduleJob) throws SchedulerException{  
    	checkNotNull(scheduleJob);
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());  
        scheduler.pauseJob(jobKey);  
    }  
    
    public void resumeJob(ScheduleJob scheduleJob) throws SchedulerException{  
    	checkNotNull(scheduleJob);
    	JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());  
        scheduler.resumeJob(jobKey);  
    }
    
    public void deleteJob(ScheduleJob scheduleJob) throws SchedulerException{  
    	checkNotNull(scheduleJob);
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());  
        scheduler.deleteJob(jobKey);  
    }
    
    public void runJobOnce(ScheduleJob scheduleJob) throws SchedulerException{ 
    	checkNotNull(scheduleJob);
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());  
        scheduler.triggerJob(jobKey);  
    } 
    
    
    private void updateJobCronExpression(ScheduleJob scheduleJob) throws SchedulerException{  
		checkNotNull(scheduleJob);
		Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getCronExpression()), "CronExpression is null");
		
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());  
        CronTrigger cronTrigger = (CronTrigger)scheduler.getTrigger(triggerKey);  
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());  
        cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();  
        scheduler.rescheduleJob(triggerKey, cronTrigger);  
    }
    
    private void wrapScheduleJob(ScheduleJob scheduleJob,Scheduler scheduler,JobKey jobKey,Trigger trigger){  
        try {  
            scheduleJob.setJobName(jobKey.getName());  
            scheduleJob.setJobGroup(jobKey.getGroup()); 
  
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);  
            ScheduleJob job = (ScheduleJob)jobDetail.getJobDataMap().get("scheduleJob");  
            scheduleJob.setDesc(job.getDesc());  
            scheduleJob.setJobId(job.getJobId());
  
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());  
            scheduleJob.setJobStatus(triggerState.name());  
            if(trigger instanceof CronTrigger){  
                CronTrigger cronTrigger = (CronTrigger)trigger;  
                String cronExpression = cronTrigger.getCronExpression();  
                scheduleJob.setCronExpression(cronExpression);  
            }  
        } catch (SchedulerException e) {  
            e.printStackTrace(); 
        }  
    }

    private void checkNotNull(ScheduleJob scheduleJob) {
    	Preconditions.checkNotNull(scheduleJob, "job is null");
		Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getJobName()), "jobName is null");
		Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getJobGroup()), "jobGroup is null");
	}
    
    
	public SchedulerMetaData getMetaData() throws SchedulerException {
		SchedulerMetaData metaData = scheduler.getMetaData();
		return metaData;
	}

	
}
