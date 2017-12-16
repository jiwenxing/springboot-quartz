package com.jverson.quartz.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class Sender {

	private static Logger logger = LoggerFactory.getLogger(Sender.class.getName());
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	public void send(String interfaceName){
		logger.info("interface: {} send mq", interfaceName);
		amqpTemplate.convertAndSend("task", interfaceName);
	}
	
}
