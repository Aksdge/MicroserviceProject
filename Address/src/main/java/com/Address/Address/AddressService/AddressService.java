package com.Address.Address.AddressService;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.Address.Address.AddressEntity.AddressEntity;
import com.Address.Address.AddressRepo.AddressRepo;
import com.Address.AddressResponse;

@Service
public class AddressService {

    private final AddressRepo addressRepo;
    private final ModelMapper modelMapper;

    public AddressService(AddressRepo addressRepo, ModelMapper modelMapper) {
        this.addressRepo = addressRepo;
        this.modelMapper = modelMapper;
    }

    public AddressResponse getAddressById(int id) {
        AddressEntity addressEntity = addressRepo.findById(id).orElse(null);
        if (addressEntity == null) {
            return null;
        }
        return modelMapper.map(addressEntity, AddressResponse.class);
    }

}
