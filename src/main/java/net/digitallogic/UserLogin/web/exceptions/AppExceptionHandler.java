package net.digitallogic.UserLogin.web.exceptions;

import lombok.extern.slf4j.Slf4j;
import net.digitallogic.UserLogin.web.Dto.ErrorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public AppExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        return new ResponseEntity<>(
            new ErrorDto(ex.getMessage() == null ? getMessage("exception.record.notFound") : ex.getMessage(), request.getRequestURI()),
                HttpStatus.NOT_FOUND
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, String> errorMap = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new String[]{ error.getField(), error.getDefaultMessage()})
                .collect(Collectors.toMap( e -> e[0], e -> e[1]));

        return new ResponseEntity<Object>(
            new ErrorDto<Map<String,String>>(errorMap, request.getDescription(false)),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex) {

        return new ResponseEntity<Object>(
                new ErrorDto("it is working"),
                HttpStatus.BAD_REQUEST
        );
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
//        Map<String, String> result = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach( error -> {
//            String fieldName = ((FieldError)error).getField();
//            String message = error.getDefaultMessage();
//            result.put(fieldName, message);
//        });
//
//        return new ResponseEntity<>(
//                new ErrorDto<Map<String, String>>(result, request.getRequestURI()),
//                HttpStatus.BAD_REQUEST
//        );
//    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(ValidationException.class)
//    public ErrorDto handleValidationException(ValidationException ex, HttpServletResponse response) {
//        ex.getViolations().forEach(validation -> {
//            System.out.println(validation.getMessage());
//        });
//
//        return null;
//    }



//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorDto> handleGenericException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
//        return new ResponseEntity<ErrorDto>(
//                new ErrorDto(ex.getMessage() != null ? ex.getMessage() : getMessage("exception.serverError"), request.getRequestURI()),
//                HttpStatus.INTERNAL_SERVER_ERROR
//        );
//    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorDto> handleException(Exception ex, WebRequest request) {
//        log.error("Exception {}", ex);
//
//        return new ResponseEntity<ErrorDto>(
//                new ErrorDto(ex.getMessage()),
//                HttpStatus.INTERNAL_SERVER_ERROR
//        );
//    }

    private String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
