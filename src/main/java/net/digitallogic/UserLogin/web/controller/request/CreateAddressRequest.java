package net.digitallogic.UserLogin.web.controller.request;

import lombok.Data;
import net.digitallogic.UserLogin.persistence.model.AddressEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateAddressRequest {

    @Size(max = AddressEntity.STREET_MAX_LENGTH, message = "{field.error.maxSize}")
    @NotNull(message = "{field.error.notNull}")
    private String street;

    @NotNull(message = "{field.error.notNull}")
    @Size(max = AddressEntity.CITY_MAX_LENGTH, message = "{field.error.maxSize}")
    private String city;

    @NotNull(message = "{field.error.notNull}")
    @Size(max = AddressEntity.STATE_MAX_LENGTH,
            message = "{field.error.maxSize}")
    private String state;

    @NotNull(message = "{field.error.notNull}")
    @Size(min = AddressEntity.POSTAL_CODE_MIN_LENGTH,
            max = AddressEntity.POSTAL_CODE_MAX_LENGTH,
            message = "{invalid Field}")
    private String postalCode;
}
