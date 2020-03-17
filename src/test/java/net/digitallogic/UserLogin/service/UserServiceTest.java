package net.digitallogic.UserLogin.service;

import net.digitallogic.UserLogin.mapper.UserMapper;
import net.digitallogic.UserLogin.persistence.model.RoleEntity;
import net.digitallogic.UserLogin.persistence.model.UserEntity;
import net.digitallogic.UserLogin.persistence.repository.UserRepository;
import net.digitallogic.UserLogin.shared.Utils;
import net.digitallogic.UserLogin.web.Dto.UserDto;
import net.digitallogic.UserLogin.web.exceptions.EntityNotFoundException;
import net.digitallogic.UserLogin.web.exceptions.UserAlreadyExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserServiceTest {

    // get userMapper
    @Mock
    UserMapper userMapper;

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    Utils utils;

    @Mock
    MessageSource messageSource;

    @Mock
    AddressService addressService;

    @Mock
    RoleService roleService;

    @InjectMocks
    UserService userService;

    // == Setup == //
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        when(userMapper.userEntityToUserDto(any(UserEntity.class))).thenAnswer((InvocationOnMock invocation) -> {
            UserEntity entity = (UserEntity) invocation.getArgument(0);
            UserDto user = new UserDto();
            user.setFirstName(entity.getFirstName());
            user.setLastName(entity.getLastName());
            user.setId(entity.getUserId());
            user.setEmail(entity.getEmail());

            return user;
        });

        when(roleService.getRole(anyString())).thenReturn(new RoleEntity("USER_ROLE"));

        when(userMapper.userEntityListToUserDtoList(any(List.class))).thenAnswer((InvocationOnMock invocation) ->
            ((List<UserEntity>) invocation.getArgument(0)).stream()
                .map(userEntity -> userMapper.userEntityToUserDto(userEntity))
                .collect(Collectors.toList())
        );

        when(userMapper.userDtoToUserEntity(any(UserDto.class))).thenAnswer((InvocationOnMock invocation) -> {
            UserDto userDto = (UserDto) invocation.getArgument(0);
            UserEntity user = new UserEntity(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail());
            return user;
        });

        when(userRepository.save(any(UserEntity.class))).thenAnswer((InvocationOnMock var) -> var.getArgument(0));

        when(utils.generateId()).thenReturn("randomUserId");
    }

    // == Testing Functions == //

    @Test
    void getUserTest() {
        UserEntity userEntity = new UserEntity("Joe", "Thomas", "jthomas@gtesting.com");
        userEntity.setUserId("asdf1234");
        when(userRepository.findByUserId(anyString())).thenReturn(userEntity);

        UserDto user = userService.getUser("asjdfk");

        assertNotNull(user);
        assertEquals(user.getFirstName(), userEntity.getFirstName());
        assertEquals(user.getLastName(), userEntity.getLastName());
        assertEquals(user.getId(), userEntity.getUserId());
    }

    @Test
    void getUsersTest() {
        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(new UserEntity("Joe", "Thomas", "jtomas@testing.com"));
        userEntities.add(new UserEntity("Bob", "Barker", "bob_barker@testing.com"));

        when(userRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<UserEntity>(userEntities));

        List<UserDto> response = userService.getUsers(0,25);
        assertNotNull(response);
        assertEquals(response.size(), 2);
    }

    @Test
    void getUserNotFound() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> {
            userService.getUser("jalskdf");
        });
    }

    @Test
    void createUser() {
        String userId = "asldkjfl342";

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");
        UserDto newUser = new UserDto("Sarah", "Connor", "sconner@test.com", "password");

        UserDto response = userService.createUser(newUser);
        assertNotNull(response);
        assertEquals(response.getFirstName(), newUser.getFirstName());
        assertEquals(response.getLastName(), newUser.getLastName());
        assertNotNull(response.getId());
    }

    @Test
    void createDuplicateUser() {
        UserDto newUser = new UserDto("Bob", "Barker", "bk@test.com", "password");
        when(userRepository.findByEmail(anyString())).thenReturn(new UserEntity("Bob", "Barker", "bk@test.com"));

        assertThrows(UserAlreadyExistException.class, () -> userService.createUser(newUser));
    }

//
//    @Test
//    void updateUser() {
////        UserDto userDto = new UserDto("Larry", "Brown", "test@testing.com", "password123");
////        UserDto updateInfo = new UserDto();
////        updateInfo.setFirstName("Joe");
////        updateInfo.setLastName("Dirt");
////        updateInfo.setUserId("someRandomString");
////        userDto.setUserId("someRandomString");
////
////        when(userRepository.findByUserId(anyString())).thenReturn(new UserEntity(userDto));
////        when(userRepository.save(any(UserEntity.class))).thenAnswer((InvocationOnMock invocation) ->
////                invocation.getArgument(0));
////
////        UserDto response = userService.updateUser(updateInfo);
////
////        verify(userRepository, times(1)).findByUserId(anyString());
////        verify(userRepository, times(1)).save(any(UserEntity.class));
////        assertEquals(response.getUserId(), userDto.getUserId());
////        assertEquals(response.getFirstName(), updateInfo.getFirstName());
////        assertEquals(response.getLastName(), updateInfo.getLastName());
//    }
}