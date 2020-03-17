package net.digitallogic.UserLogin.web.controller.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@EqualsAndHashCode(of = "email")
@Data
public class UserLoginRequest implements Serializable {
    // == Constants == //
    private static final long serialVersionUID = 5087787519188543026L;

    // == Fields == //
    @NotEmpty(message = "{field.error.notNull}")
    private String email;

    @NotEmpty(message = "{field.error.notNull}")
    private String password;
}
