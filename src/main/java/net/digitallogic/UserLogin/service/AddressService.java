package net.digitallogic.UserLogin.service;

import lombok.extern.slf4j.Slf4j;
import net.digitallogic.UserLogin.mapper.AddressMapper;
import net.digitallogic.UserLogin.persistence.model.AddressEntity;
import net.digitallogic.UserLogin.persistence.model.UserEntity;
import net.digitallogic.UserLogin.persistence.repository.AddressRepository;
import net.digitallogic.UserLogin.persistence.repository.UserRepository;
import net.digitallogic.UserLogin.shared.Utils;
import net.digitallogic.UserLogin.web.Dto.AddressDto;
import net.digitallogic.UserLogin.web.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;

@Slf4j
@Service
public class AddressService {
    // == Fields == //
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final Utils utils;
    private final Validator validator;
    private final AddressMapper addressMapper;

    // == Constructors == //
    @Autowired
    public AddressService(AddressRepository addressRepository, UserRepository userRepository, Utils utils,
                          Validator validator, AddressMapper addressMapper){
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.utils = utils;
        this.validator = validator;
        this.addressMapper = addressMapper;
    }

    // == Services == //
    public List<AddressDto> getAddresses(String userId) {

        return addressMapper.addressEntityListToAddressDtoList(
                addressRepository.findByUserUserId(userId)
        );
    }

    public AddressDto getAddress(String addressId) {
        AddressEntity address = addressRepository.findByAddressId(addressId);
        if (address == null)
            throw new EntityNotFoundException();

        return addressMapper.addressEntityToAddressDto(address);
    }

    public AddressDto createAddressEntity(String userId, AddressDto addressDto) {
        UserEntity user = userRepository.findByUserId(userId);
        if (user == null)
            throw new EntityNotFoundException();

        return addressMapper.addressEntityToAddressDto(
            createAddressEntity(user, addressDto)
        );
    }

    public AddressDto updateAddress(AddressDto addressDto) {
        AddressEntity addressEntity = addressRepository.findByAddressId(addressDto.getId());
        if (addressEntity == null)
            throw new EntityNotFoundException();

        return addressMapper.addressEntityToAddressDto(
            addressRepository.save(
               addressMapper.updateAddressDtoToEntity(addressEntity, addressDto)
            )
        );
    }

    public void deleteAddress(String addressId) {
        AddressEntity address = addressRepository.findByAddressId(addressId);
        if (address == null)
            throw new EntityNotFoundException();

        addressRepository.delete(address);
    }

    public AddressEntity createAddressEntity(UserEntity user, AddressDto addressDto) {
        AddressEntity address = addressMapper.addressDtoToAddressEntity(addressDto);

        address.setAddressId(generateUniqueId());
        user.addAddress(address);
        return address;
    }

    public String generateUniqueId() {
        String addressId = null;
        do {
            addressId = utils.generateId();
        } while (addressRepository.findByAddressId(addressId) != null);

        return addressId;
    }
}
