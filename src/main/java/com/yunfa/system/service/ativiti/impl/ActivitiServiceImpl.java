package com.yunfa.system.service.ativiti.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.VariableInstanceEntity;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunfa.system.service.ativiti.ActivitiService;

/**
 * @author yunfa
 * @version 1.0
 * @date 2017-01-11.
 */
@Service
public class ActivitiServiceImpl implements ActivitiService {

	private Logger logger = LoggerFactory.getLogger(ActivitiServiceImpl.class);

	@Autowired
	private ProcessEngine processEngine;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private FormService formService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private ManagementService managementService;

	@Autowired
	private IdentityService identityService;

	public void startProcess() {
		// deployProcess();
		// defineParam();
		// queryGroupTask();
		completeTask();
	}

	public void deployProcess() {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.createDeployment().addClasspathResource("VacationRequest.bpmn20.xml").deploy();
		logger.debug("Number of process definitions:{} ", repositoryService.createProcessDefinitionQuery().count());
	}

	public void defineParam() {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("employeeName", "Kermit");
		variables.put("numberOfDays", new Integer(4));
		variables.put("vacationMotivation", "I'm really tired!我要休息了，请假!");
		runtimeService.startProcessInstanceByKey("vacationRequest", variables);
		logger.debug("Number of process instances: {}", runtimeService.createProcessInstanceQuery().count());
	}

	public void queryGroupTask() {
		// Fetch all tasks for the management group
		TaskService taskService = processEngine.getTaskService();
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
		for(Task task : tasks) {
			logger.debug("management组中执行中的任务: {}", task.getName());
		}
	}

	public void queryUserTask() {
		List<Task> tasks = taskService.createTaskQuery().taskAssignee("kermit")
				.processVariableValueEquals("orderId", "0815").orderByDueDate().asc().list();
		for(Task task : tasks) {
			logger.debug("查询用户的任务: {}", task.getName());
		}
	}

	public void queryComplexTask() {
		List<Task> tasks = taskService
				.createNativeTaskQuery()
				.sql("SELECT count(*) FROM " + managementService.getTableName(Task.class)
						+ " T WHERE T.NAME_ = #{taskName}").parameter("taskName", "gonzoTask").list();
		for(Task task : tasks) {
			logger.debug("复合查询用户的任务: {}", task.getName());
		}
		long count = taskService
				.createNativeTaskQuery()
				.sql("SELECT count(*) FROM " + managementService.getTableName(Task.class) + " T1, "
						+ managementService.getTableName(VariableInstanceEntity.class)
						+ " V1 WHERE V1.TASK_ID_ = T1.ID_").count();
		logger.debug("复合查询数量:{}", count);
	}

	public void completeTask() {
		TaskService taskService = processEngine.getTaskService();
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
		for(Task task : tasks) {
			Map<String, Object> taskVariables = new HashMap<String, Object>();
			taskVariables.put("vacationApproved", "false");
			taskVariables.put("managerMotivation", "We have a tight deadline!就不给你假呀!");
			taskService.complete(task.getId(), taskVariables);
			logger.debug("请假操作完成，请假流程结束....");
			// 只完结第一条Task
			break;
		}
	}

	public void suspendProcess() {
		repositoryService.suspendProcessDefinitionByKey("vacationRequest");
		try {
			runtimeService.startProcessInstanceByKey("vacationRequest");
		} catch (ActivitiException e) {
			logger.error("流程挂起失败...", e);
		}
	}

	public void activateProcess() {
		runtimeService.activateProcessInstanceById("vacationRequest");
		logger.debug("流程挂起后被再次激活...");
	}

	/**
	 * 一个简单完整处理流程
	 */
	public void allProcess() {
		// Create Activiti process engine
		ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
				.buildProcessEngine();
		// Get Activiti services
		RepositoryService repositoryService = processEngine.getRepositoryService();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		// Deploy the process definition
		repositoryService.createDeployment().addClasspathResource("FinancialReportProcess.bpmn20.xml").deploy();
		// Start a process instance
		String procId = runtimeService.startProcessInstanceByKey("financialReport").getId();
		// Get the first task
		TaskService taskService = processEngine.getTaskService();
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("accountancy").list();
		for(Task task : tasks) {
			logger.debug("Following task is available for accountancy group: " + task.getName());
			// claim it
			taskService.claim(task.getId(), "fozzie");
		}
		// Verify Fozzie can now retrieve the task
		tasks = taskService.createTaskQuery().taskAssignee("fozzie").list();
		for(Task task : tasks) {
			logger.debug("Task for fozzie: " + task.getName());
			// Complete the task
			taskService.complete(task.getId());
		}
		logger.debug("Number of tasks for fozzie: " + taskService.createTaskQuery().taskAssignee("fozzie").count());
		// Retrieve and claim the second task
		tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
		for(Task task : tasks) {
			logger.debug("Following task is available for accountancy group: " + task.getName());
			taskService.claim(task.getId(), "kermit");
		}
		// Completing the second task ends the process
		for(Task task : tasks) {
			taskService.complete(task.getId());
		}
		// verify that the process is actually finished
		HistoryService historyService = processEngine.getHistoryService();
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(procId).singleResult();
		logger.debug("Process instance end time: " + historicProcessInstance.getEndTime());
	}
}
