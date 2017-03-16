package com.yunfa.system.service;

import com.yunfa.system.entities.User;
import org.springframework.stereotype.Service;

/**
 * Created by yunfa on 2017-01-03.
 */
@Service
public interface UserService {

	User getUser(long id);

	int save(User user);

	int update(User user);

	int delete(User user);
}
