package com.servicehub.auth.service.Impl;

import com.servicehub.auth.dto.UserDto;
import com.servicehub.auth.entity.User;
import com.servicehub.auth.repository.UserRepository;
import com.servicehub.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> fetchUsers() {
        return userRepository.findAll();
    }

    @Override
    public User fetchUsersById(Integer id) {
        return null;
    }

    @Override
    public User saveOrUpdateUsers(UserDto userDto) {
        return null;
    }

    @Override
    public void deleteUserById(Integer id) {

    }
}
