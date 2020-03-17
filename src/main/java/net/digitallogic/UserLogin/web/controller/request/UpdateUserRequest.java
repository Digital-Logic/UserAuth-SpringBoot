package net.digitallogic.UserLogin.web.controller.request;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UpdateUserRequest {

    // == Fields == //
    @Size(max = 50, message = "{field.error.maxSize}")
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Size(min=7, max=60, message = "{field.error.minMaxSize}")
    private String password;

    public UpdateUserRequest() {
    }

    public UpdateUserRequest(String firstName, String lastName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
}
