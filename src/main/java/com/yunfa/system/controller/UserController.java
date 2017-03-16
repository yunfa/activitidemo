package com.yunfa.system.controller;

import com.yunfa.system.entities.User;
import com.yunfa.system.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 
 * @author yunfa.li
 * @date 2017年3月16日
 */
@Controller
@RequestMapping("user")
public class UserController {

	@Resource
	private UserService userService;

	@ResponseBody
	@RequestMapping("/login")
	public User getUser(@RequestParam("id") long id) {
		System.out.print(id);
		User user = userService.getUser(id);
		return user;
	}
}
