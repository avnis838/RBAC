package com.example.RBACUserManagement.mapper;


import com.example.RBACUserManagement.dto.UserProfileDto;
import com.example.RBACUserManagement.model.User;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(user.getRoles().stream().map(r -> r.getName()).collect(java.util.stream.Collectors.toSet()))")
    UserProfileDto toProfileDto(User user);
}

