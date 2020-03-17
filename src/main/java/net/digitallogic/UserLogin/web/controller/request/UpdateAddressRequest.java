package net.digitallogic.UserLogin.web.controller.request;

import lombok.Data;
import net.digitallogic.UserLogin.persistence.model.AddressEntity;

import javax.validation.constraints.Size;

@Data
public class UpdateAddressRequest {

    @Size(max = AddressEntity.STREET_MAX_LENGTH, message = "{field.error.maxSize}")
    private String street;

    @Size(max = AddressEntity.CITY_MAX_LENGTH, message = "{field.error.maxSize}")
    private String city;

    @Size(max = AddressEntity.STATE_MAX_LENGTH,
            message = "{field.error.maxSize}")
    private String state;

    @Size(min = AddressEntity.POSTAL_CODE_MIN_LENGTH,
            max = AddressEntity.POSTAL_CODE_MAX_LENGTH,
            message = "{invalid Field}")
    private String postalCode;
}
