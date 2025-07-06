package com.example.barclays.mappers.impl;

import com.example.barclays.domain.dto.AddressDTO;
import com.example.barclays.domain.entities.Address;
import com.example.barclays.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AddressMapperImpl implements Mapper<Address, AddressDTO> {
    private ModelMapper modelMapper;

    public AddressMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Address mapTo(AddressDTO addressDTO) {
        return modelMapper.map(addressDTO, Address.class);
    }

    @Override
    public AddressDTO mapFrom(Address address) {
        return modelMapper.map(address, AddressDTO.class);
    }
}
