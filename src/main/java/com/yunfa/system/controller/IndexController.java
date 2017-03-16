package com.yunfa.system.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.yunfa.system.service.UserService;

/**
 * 
 * @author yunfa.li
 * @date 2017年3月16日
 */
@Controller
public class IndexController {

	@Resource
	private UserService userService;

	@RequestMapping("/")
	public ModelAndView index() {
		System.out.println("index....");
		ModelAndView view = new ModelAndView("index");
		return view;
	}
}
