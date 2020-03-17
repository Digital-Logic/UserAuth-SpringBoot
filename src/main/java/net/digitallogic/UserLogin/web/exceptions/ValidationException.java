package net.digitallogic.UserLogin.web.exceptions;

import lombok.Getter;

import javax.validation.ConstraintViolation;
import java.util.Set;

@Getter
public class ValidationException extends RuntimeException {
    private final Set<ConstraintViolation<Object>> violations;

    public ValidationException(Set<ConstraintViolation<Object>> violations) {
        super();
        this.violations = violations;
    }
}
