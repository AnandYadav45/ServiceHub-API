package com.servicehub.auth.mapper;

import com.servicehub.auth.dto.UserDto;
import com.servicehub.auth.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUser(User user);
}
