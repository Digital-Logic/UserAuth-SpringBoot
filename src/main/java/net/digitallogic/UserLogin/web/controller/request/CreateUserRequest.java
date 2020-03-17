package net.digitallogic.UserLogin.web.controller.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(of = "id")
public class CreateUserRequest {

    // == Fields == //
    @Size(max = 50, message = "{field.error.maxSize}")
    @NotNull(message = "{field.error.notNull}")
    private String firstName;

    @Size(max = 50)
    @NotNull(message = "{field.error.notNull}")
    private String lastName;

    @Size(max = 120)
    @NotEmpty(message = "{field.error.notNull}")
    @NotNull(message = "{field.error.email}")
    private String email;

    @Size(min=7, max=60, message = "{field.error.minMaxSize}")
    @NotNull(message = "{field.error.notNull}")
    private String password;

    @Valid
    private List<CreateAddressRequest> addresses;

    public CreateUserRequest() {
        addresses = new ArrayList<>();
    }
}
