package com.example.barclays.mappers.impl;

import com.example.barclays.domain.dto.UserDTO;
import com.example.barclays.domain.entities.User;
import com.example.barclays.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements Mapper<User, UserDTO> {

    private ModelMapper modelMapper;

    public UserMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public User mapTo(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    @Override
    public UserDTO mapFrom(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
