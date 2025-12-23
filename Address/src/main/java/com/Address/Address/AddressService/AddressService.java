package com.Address.Address.AddressService;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.Address.Address.AddressEntity.AddressEntity;
import com.Address.Address.AddressRepo.AddressRepo;
import com.Address.AddressRequest;
import com.Address.AddressResponse;

@Service
public class AddressService {

    private final AddressRepo addressRepo;
    private final ModelMapper modelMapper;

    public AddressService(AddressRepo addressRepo, ModelMapper modelMapper) {
        this.addressRepo = addressRepo;
        this.modelMapper = modelMapper;
    }

    // Create
    public AddressResponse createAddress(AddressRequest addressRequest) {
        try {
            AddressEntity addressEntity = modelMapper.map(addressRequest, AddressEntity.class);
            AddressEntity savedEntity = addressRepo.save(addressEntity);
            return modelMapper.map(savedEntity, AddressResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create address: " + e.getMessage(), e);
        }
    }

    // Read - Get by ID
    public AddressResponse getAddressById(int id) {
        AddressEntity addressEntity = addressRepo.findById(id).orElse(null);
        if (addressEntity == null) {
            return null;
        }
        return modelMapper.map(addressEntity, AddressResponse.class);
    }

    // Read - Get All
    public List<AddressResponse> getAllAddresses() {
        List<AddressEntity> addressEntities = addressRepo.findAll();
        return addressEntities.stream()
                .map(entity -> modelMapper.map(entity, AddressResponse.class))
                .collect(Collectors.toList());
    }

    // Update
    public AddressResponse updateAddress(int id, AddressRequest addressRequest) {
        AddressEntity addressEntity = addressRepo.findById(id).orElse(null);
        if (addressEntity == null) {
            return null;
        }
        
        addressEntity.setLane_1(addressRequest.getLane_1());
        addressEntity.setLane_2(addressRequest.getLane_2());
        addressEntity.setState(addressRequest.getState());
        addressEntity.setZip(addressRequest.getZip());
        
        AddressEntity updatedEntity = addressRepo.save(addressEntity);
        return modelMapper.map(updatedEntity, AddressResponse.class);
    }

    // Delete
    public boolean deleteAddress(int id) {
        if (addressRepo.existsById(id)) {
            addressRepo.deleteById(id);
            return true;
        }
        return false;
    }

}
