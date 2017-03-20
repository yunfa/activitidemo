package com.yunfa.system.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.yunfa.system.service.ativiti.ActivitiService;

/**
 * 
 * @author yunfa.li
 * @date 2017年3月16日
 */
@Controller
public class ActivitiController {

	@Resource
	private ActivitiService activitiService;

	@RequestMapping("/")
	public ModelAndView index() {
		ModelAndView view = new ModelAndView("index");
		activitiService.startProcess();
		return view;
	}
}
