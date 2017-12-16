package com.jverson.quartz.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * analog rabbit MQ consuming
 * receiver should be in business system
 */
/*@RabbitListener(queues = "task")
public class Receiver {

	private static Logger logger = LoggerFactory.getLogger(Receiver.class.getName());
	
	@RabbitHandler
    public void process(String interfaceName) {
		logger.info("Receiver : " + interfaceName);
    }
	
}*/
