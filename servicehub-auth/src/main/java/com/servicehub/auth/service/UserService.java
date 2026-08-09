package com.servicehub.auth.service;

import com.servicehub.auth.dto.UserDto;
import com.servicehub.auth.entity.User;

import java.util.List;

public interface UserService {

    List<User> fetchUsers();

    User fetchUsersById(Integer id);

    User saveOrUpdateUsers(UserDto userDto);

    void deleteUserById(Integer id);

}
