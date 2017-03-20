package com.yunfa.system.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.BaseEntityEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

public class BaseEventListener extends BaseEntityEventListener {

	private Logger logger = LoggerFactory.getLogger(BaseEventListener.class);

	public void onCreate(ActivitiEvent event) {
		super.onCreate(event);
		logger.debug("on process create...");
	}

	public void onEntityEvent(ActivitiEvent event) {
		super.onEntityEvent(event);
		logger.debug("on entity event...");
	}
}
