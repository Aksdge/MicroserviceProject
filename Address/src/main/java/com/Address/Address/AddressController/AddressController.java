package com.Address.Address.AddressController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.Address.Address.AddressService.AddressService;
import com.Address.AddressResponse;



@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

@GetMapping("/address/{empid}")
public ResponseEntity<AddressResponse> getAddressById(@PathVariable("empid") int empid) {
    AddressResponse address = addressService.getAddressById(empid);
    if (address == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(address);
}

}
