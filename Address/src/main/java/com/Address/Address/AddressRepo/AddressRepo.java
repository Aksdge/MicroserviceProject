package com.Address.Address.AddressRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Address.Address.AddressEntity.AddressEntity;



@Repository
public interface AddressRepo extends JpaRepository<AddressEntity, Integer> {}
