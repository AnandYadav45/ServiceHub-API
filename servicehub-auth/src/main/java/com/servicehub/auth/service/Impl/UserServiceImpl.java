package com.servicehub.auth.service.Impl;

import com.servicehub.auth.dto.UserDto;
import com.servicehub.auth.entity.User;
import com.servicehub.auth.repository.UserRepository;
import com.servicehub.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public List<User> fetchUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User fetchUsersById(Integer id) {
        return null;
    }

    @Override
    @Transactional
    public User saveOrUpdateUsers(UserDto userDto) {
        return null;
    }

    @Override
    @Transactional
    public void deleteUserById(Integer id) {

    }
}
