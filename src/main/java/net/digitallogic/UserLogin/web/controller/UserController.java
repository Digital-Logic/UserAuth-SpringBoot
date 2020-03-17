package net.digitallogic.UserLogin.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.digitallogic.UserLogin.mapper.UserMapper;
import net.digitallogic.UserLogin.service.UserService;
import net.digitallogic.UserLogin.web.Dto.ErrorDto;
import net.digitallogic.UserLogin.web.Dto.UserDto;
import net.digitallogic.UserLogin.web.Routes;
import net.digitallogic.UserLogin.web.controller.request.CreateUserRequest;
import net.digitallogic.UserLogin.web.controller.request.UpdateUserRequest;
import net.digitallogic.UserLogin.web.controller.response.UserListResponse;
import net.digitallogic.UserLogin.web.exceptions.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;


@Slf4j
@RestController
@RequestMapping(path = Routes.USERS,
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, "application/hal+json" },
        consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class UserController {

    // == Fields == //
    private final UserService userService;
    private final MessageSource messageSource;
    private final UserMapper userMapper;

    // == Constructor == //
    @Autowired
    public UserController(UserService userService, MessageSource messageSource, UserMapper userMapper) {
        this.userService = userService;
        this.messageSource = messageSource;
        this.userMapper = userMapper;
    }

    // == Rest Mappings == //
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody CreateUserRequest createUser) {
        return userService.createUser(
                  userMapper.createUserToUserDto(createUser)
              );
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_USER_AUTHORITY', 'READ_USER_AUTHORITY')")
    @GetMapping()
    public UserListResponse getUsers(@RequestParam(value="page", defaultValue = "0") int page,
                                     @RequestParam(value="limit", defaultValue = "25") int limit) {

        return userMapper.userListToUserListResponse(
                userService.getUsers(page, limit)
        );
    }

    @PreAuthorize("#userId == principal.getUsername() or hasAnyAuthority('ADMIN_USER_AUTHORITY', 'READ_USER_AUTHORITY')")
    @GetMapping(path = "/{userId}")
    public UserDto getUser(@PathVariable String userId) {
        return userService.getUser(userId);
    }

    @PreAuthorize("#userId == principal.getUsername or hasAnyAuthority('ADMIN_USER_AUTHORITY', 'UPDATE_USER_AUTHORITY')")
    @PutMapping(path = "/{userId}")
    public UserDto updateUser(@PathVariable String userId , @Valid @RequestBody UpdateUserRequest updateUser) {

        UserDto userDetails = userMapper.updateUserToUserDto(updateUser);
        userDetails.setId(userId);

        return userService.updateUser(userDetails);
    }

    @PreAuthorize("#userId == principal.getUsername() or hasAnyAuthority('ADMIN_USER_AUTHORITY', 'DELETE_USER_AUTHORITY')")
    @DeleteMapping(path = "/{userId}")
    public void deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
    }

     // == Login == //
     @GetMapping(path = Routes.LOGIN)
     public UserDto getLoggedInUser(Principal principal) {
        return userService.getUser(principal.getName());
     }

    //== Life cycle hooks == //
//    @PostConstruct
//    public void configMappings() {
//
//    }

    // == Error Handlers == //

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyExistException.class)
    public ErrorDto handleUserExistError(UserAlreadyExistException ex, HttpServletRequest request) {
        return new ErrorDto<String>(ex.getMessage(), request.getRequestURI());
    }

    // == Init Binder == //
    /* This function will be called on every
     */
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }
}
