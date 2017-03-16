package com.yunfa.system.service.impl;

import com.yunfa.system.dao.UserDao;
import com.yunfa.system.entities.User;
import com.yunfa.system.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author yunfa
 * @version 1.0
 * @date 2017-01-11.
 */
@Service
public class UserServiceImpl implements UserService {

	@Resource
	private UserDao userDao;

	public User getUser(long id) {
		User user = userDao.getUser(id);
		if(user == null) {
			return null;
		} else {
			return user;
		}
	}

	public int save(User user) {
		return userDao.saveUser(user);
	}

	public int update(User user) {
		return userDao.updateUser(user);
	}

	public int delete(User user) {
		return userDao.deleteUser(user);
	}
}
