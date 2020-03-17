package net.digitallogic.UserLogin.mapper;

import net.digitallogic.UserLogin.persistence.model.UserEntity;
import net.digitallogic.UserLogin.web.Dto.UserDto;
import net.digitallogic.UserLogin.web.controller.request.CreateUserRequest;
import net.digitallogic.UserLogin.web.controller.request.UpdateUserRequest;
import net.digitallogic.UserLogin.web.controller.response.UserListResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    disableSubMappingMethodsGeneration = true,
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    uses = { AddressMapper.class, RoleMapper.class },
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", source = "userId")
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "roles", ignore = true)
    UserDto userEntityToUserDto(UserEntity user);

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)// userDto does not have this property yet.
    UserEntity userDtoToUserEntity(UserDto user);

    UserDto createUserToUserDto(CreateUserRequest createUser);
    UserDto updateUserToUserDto(UpdateUserRequest updateUser);

    List<UserEntity> userDtoListToUserEntityList(List<UserDto> userDtoList);
    List<UserDto> userEntityListToUserDtoList(List<UserEntity> userList);

    // update userDto object
    @Mappings({
            @Mapping(target = "firstName", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
            @Mapping(target = "lastName", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
            @Mapping(target = "password", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
            @Mapping(target = "email", ignore = true),
            @Mapping(target = "addresses", ignore = true),
            @Mapping(target = "roles", ignore = true)
    })
    UserDto updateUserDto(@MappingTarget UserDto target, UserDto source);

    // update userEntity object
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "userId", ignore = true),
            @Mapping(target = "firstName", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
            @Mapping(target = "lastName", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
            @Mapping(target = "email", ignore = true),
            @Mapping(target = "addresses", ignore = true),
            @Mapping(target = "roles", ignore = true)
    })
    UserEntity updateUserDtoToUserEntity(@MappingTarget UserEntity target, UserDto source);

    // UsersResponse mapper
    UserListResponse userListToUserListResponse(List<UserDto> users);
}
