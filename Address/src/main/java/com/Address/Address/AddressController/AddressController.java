package com.Address.Address.AddressController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.Address.Address.AddressService.AddressService;
import com.Address.AddressRequest;
import com.Address.AddressResponse;

@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    // Create - POST
    @PostMapping("/address")
    public ResponseEntity<?> createAddress(@RequestBody AddressRequest addressRequest) {
        try {
            AddressResponse address = addressService.createAddress(addressRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(address);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown error occurred";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"" + errorMessage.replace("\"", "\\\"") + "\"}");
        }
    }

    // Read - Get by ID
@GetMapping("/address/{empid}")
public ResponseEntity<AddressResponse> getAddressById(@PathVariable("empid") int empid) {
    AddressResponse address = addressService.getAddressById(empid);
    if (address == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(address);
}

    // Read - Get All
    @GetMapping("/address")
    public ResponseEntity<List<AddressResponse>> getAllAddresses() {
        List<AddressResponse> addresses = addressService.getAllAddresses();
        return ResponseEntity.status(HttpStatus.OK).body(addresses);
    }

    // Update - PUT
    @PutMapping("/address/{id}")
    public ResponseEntity<AddressResponse> updateAddress(
            @PathVariable("id") int id,
            @RequestBody AddressRequest addressRequest) {
        AddressResponse address = addressService.updateAddress(id, addressRequest);
        if (address == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(address);
    }

    // Delete - DELETE
    @DeleteMapping("/address/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable("id") int id) {
        boolean deleted = addressService.deleteAddress(id);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
