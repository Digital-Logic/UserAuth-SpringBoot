package net.digitallogic.UserLogin.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.digitallogic.UserLogin.mapper.AddressMapper;
import net.digitallogic.UserLogin.service.AddressService;
import net.digitallogic.UserLogin.web.Dto.AddressDto;
import net.digitallogic.UserLogin.web.Routes;
import net.digitallogic.UserLogin.web.controller.request.CreateAddressRequest;
import net.digitallogic.UserLogin.web.controller.request.UpdateAddressRequest;
import net.digitallogic.UserLogin.web.controller.response.AddressListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = Routes.ADRESSES, // path: "/{userId}/addresses"
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
        consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class AddressController {
    // == Fields == //
    private final AddressService addressService;
    private final MessageSource messageSource;
    private final AddressMapper addressMapper;

    // == Constructors == //
    @Autowired
    public AddressController(AddressService addressService, MessageSource messageSource, AddressMapper addressMapper) {
        this.addressService = addressService;
        this.messageSource = messageSource;
        this.addressMapper = addressMapper;
    }

    // == Rest Mappings == //
    @GetMapping()
    public AddressListResponse getAddresses(@PathVariable String userId) {
        return addressMapper.addressListToAddressResponse(
                addressService.getAddresses(userId)
        );
    }

    @GetMapping(path = "/{addressId}")
    public AddressDto getAddress(@PathVariable String userId, @PathVariable String addressId) {
        AddressDto response = addressService.getAddress(addressId);

        return response;
    }

    @PostMapping(path = "/")
    public AddressDto createAddress(@PathVariable String userId, @Valid @RequestBody CreateAddressRequest createAddress) {
        return addressService.createAddressEntity(userId,
               addressMapper.createAddressToAddressDto(createAddress)
        );
    }

    @PutMapping(path = "/{addressId}")
    public AddressDto updateAddress(@PathVariable String userId, @PathVariable String addressId,
                                    @Valid @RequestBody UpdateAddressRequest updateAddress) {
        AddressDto addressDetails =addressMapper.updateAddressToAddressDto(updateAddress);
        addressDetails.setId(addressId);

        return addressService.updateAddress(
               addressMapper.updateAddressToAddressDto(updateAddress)
        );
    }

    @DeleteMapping(path = "/{addressId}")
    public void deleteAddress(@PathVariable String userId, @PathVariable String addressId) {

    }

    // == Error Handlers == //

    // == Init Binder == //
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }
}
