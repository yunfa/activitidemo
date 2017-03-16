package com.yunfa.system.dao;

import com.yunfa.system.entities.User;

/**
 * 用户Dao
 * @author yunfa.li
 * @date 2017年3月16日
 */
public interface UserDao {

	User getUser(long id);

	int saveUser(User user);

	int updateUser(User user);

	int deleteUser(User user);
}
