package com.jverson.quartz.controller;

import java.util.List;

import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jverson.quartz.common.Message;
import com.jverson.quartz.job.ScheduleJob;
import com.jverson.quartz.service.ScheduleJobService;

@RestController
@RequestMapping("/api")
public class RestfulController {

	private static Logger logger = LoggerFactory.getLogger(RestfulController.class);

	@Autowired 
	private ScheduleJobService scheduleJobService;
	
	@RequestMapping("/metaData")
	public Object metaData() throws SchedulerException{
		SchedulerMetaData metaData = scheduleJobService.getMetaData();
		return metaData;
	}
	
	@RequestMapping("/getAllJobs")
	public Object getAllJobs() throws SchedulerException{
		List<ScheduleJob> jobList = scheduleJobService.getAllJobList(); 
		return jobList;
	}
	
	@RequestMapping("/getRunningJobs")
	public Object getRunningJobs() throws SchedulerException{
		List<ScheduleJob> jobList = scheduleJobService.getRunningJobList();
		return jobList;
	}
	
	@RequestMapping(value = "/pauseJob", method = {RequestMethod.GET, RequestMethod.POST})
	public Object pauseJob(ScheduleJob job){
		logger.info("params, job = {}", job);
		Message message = Message.failure();
		try {
			scheduleJobService.pauseJob(job);
			message = Message.success();
		} catch (Exception e) {
			message.setMsg(e.getMessage());
			logger.error("pauseJob ex:", e);
		}
		return message;
	}
	
	@RequestMapping(value = "/resumeJob", method = {RequestMethod.GET, RequestMethod.POST})
	public Object resumeJob(ScheduleJob job){
		logger.info("params, job = {}", job);
		Message message = Message.failure();
		try {
			scheduleJobService.resumeJob(job);
			message = Message.success();
		} catch (Exception e) {
			message.setMsg(e.getMessage());
			logger.error("resumeJob ex:", e);
		}
		return message;
	}
	
	
	@RequestMapping(value = "/deleteJob", method = {RequestMethod.GET, RequestMethod.POST})
	public Object deleteJob(ScheduleJob job){
		logger.info("params, job = {}", job);
		Message message = Message.failure();
		try {
			scheduleJobService.deleteJob(job);
			message = Message.success();
		} catch (Exception e) {
			message.setMsg(e.getMessage());
			logger.error("deleteJob ex:", e);
		}
		return message;
	}
	
	@RequestMapping(value = "/runJob", method = {RequestMethod.GET, RequestMethod.POST})
	public Object runJob(ScheduleJob job){
		logger.info("params, job = {}", job);
		Message message = Message.failure();
		try {
			scheduleJobService.runJobOnce(job);
			message = Message.success();
		} catch (Exception e) {
			message.setMsg(e.getMessage());
			logger.error("runJob ex:", e);
		}
		return message;
	}
	
	
	@RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.GET, RequestMethod.POST})
	public Object saveOrupdate(ScheduleJob job){
		logger.info("params, job = {}", job);
		Message message = Message.failure();
		try {
			scheduleJobService.saveOrupdate(job);
			message = Message.success();
		} catch (Exception e) {
			message.setMsg(e.getMessage());
			logger.error("updateCron ex:", e);
		}
		return message;
	}
	

}
