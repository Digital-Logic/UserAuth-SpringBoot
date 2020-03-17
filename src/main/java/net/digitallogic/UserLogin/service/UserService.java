package net.digitallogic.UserLogin.service;

import lombok.extern.slf4j.Slf4j;
import net.digitallogic.UserLogin.mapper.UserMapper;
import net.digitallogic.UserLogin.persistence.model.UserEntity;
import net.digitallogic.UserLogin.persistence.repository.UserRepository;
import net.digitallogic.UserLogin.security.UserPrincipal;
import net.digitallogic.UserLogin.shared.Utils;
import net.digitallogic.UserLogin.web.Dto.UserDto;
import net.digitallogic.UserLogin.web.exceptions.EntityNotFoundException;
import net.digitallogic.UserLogin.web.exceptions.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    // == Fields == //
    private final UserRepository userRepository;
    private final Utils utils;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MessageSource messageSource;
    private final UserMapper userMapper;
    private final AddressService addressService;
    private final RoleService roleService;

    // == Constructors == //
    @Autowired
    public UserService( UserRepository userRepository, AddressService addressService, Utils utils,
                       BCryptPasswordEncoder bCryptPasswordEncoder, MessageSource messageSource,
                       UserMapper userMapper, RoleService roleService) {
        this.userRepository = userRepository;
        this.addressService = addressService;
        this.utils = utils;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.messageSource = messageSource;
        this.userMapper = userMapper;
        this.roleService = roleService;
    }

    // == Services == //
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<UserDto> getUsers(int page, int limit) {
        Pageable pageableRequest = PageRequest.of(page, limit);

        Page<UserEntity> userPage = userRepository.findAll(pageableRequest);

        return userMapper.userEntityListToUserDtoList(userPage.getContent());
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public UserDto getUser(String userId) {
        UserEntity user = userRepository.findByUserId(userId);
        if (user == null)
            throw new EntityNotFoundException();

        return userMapper.userEntityToUserDto(user);
    }

    @Transactional
    public UserDto createUser(UserDto userDto) {

        if (userRepository.findByEmail(userDto.getEmail()) != null)
            throw new UserAlreadyExistException(getMessage("exception.userExist", userDto.getEmail()));

        UserEntity user = userMapper.userDtoToUserEntity(userDto);

        user.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setUserId(generateUserId());

        user.getAddresses().forEach(address -> address.setAddressId(addressService.generateUniqueId()));

        try {
            user.addRole(roleService.getRole("ROLE_USER"));
        } catch (EntityNotFoundException ex) {
            log.error("Create new user error: 'ROLE_USER' not found");
        }

        return userMapper.userEntityToUserDto(userRepository.save(user));
    }

    @Transactional
    public UserDto updateUser(UserDto updateDetails) {
        UserEntity user = userRepository.findByUserId(updateDetails.getId());
        if (user == null)
            throw new EntityNotFoundException();

        return userMapper.userEntityToUserDto(
                userMapper.updateUserDtoToUserEntity(user, updateDetails)
        );
    }


    @Transactional
    public void deleteUser(String userId) {
        UserEntity user = userRepository.findByUserId(userId);
        if (user == null)
            throw new EntityNotFoundException();

        userRepository.delete(user);
    }

    // == Methods == //
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmailWithAuthorities(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);

        return new UserPrincipal(userEntity);
    }

    private String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    private String generateUserId() {
        String userId = "";
        do {
            userId = utils.generateId();
        } while (userRepository.findByUserId(userId) != null);

        return userId;
    }
}
