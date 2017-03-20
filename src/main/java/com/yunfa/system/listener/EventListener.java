package com.yunfa.system.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

public class EventListener implements ActivitiEventListener {

	private Logger logger = LoggerFactory.getLogger(EventListener.class);

	public void onEvent(ActivitiEvent event) {
		switch(event.getType()) {
			case JOB_EXECUTION_SUCCESS:
				logger.debug("A job well done!");
				break;
			case JOB_EXECUTION_FAILURE:
				logger.debug("A job has failed...");
				break;
			default:
				logger.debug("Event received: " + event.getType());
		}
	}

	/**
	 * onEvent发生异常时会执行
	 */
	public boolean isFailOnException() {
		// The logic in the onEvent method of this listener is not critical,exceptions can be ignored if logging
		// fails...
		return false;
	}
}
