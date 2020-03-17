package net.digitallogic.UserLogin.mapper;

import net.digitallogic.UserLogin.persistence.model.AddressEntity;
import net.digitallogic.UserLogin.web.Dto.AddressDto;
import net.digitallogic.UserLogin.web.controller.request.CreateAddressRequest;
import net.digitallogic.UserLogin.web.controller.request.UpdateAddressRequest;
import net.digitallogic.UserLogin.web.controller.response.AddressListResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    disableSubMappingMethodsGeneration = true)
public interface AddressMapper {
    AddressMapper MAPPER = Mappers.getMapper(AddressMapper.class);

    @Mapping( target = "id", source = "addressId")
    AddressDto addressEntityToAddressDto(AddressEntity addressEntity);

    @Mapping( target = "id", ignore = true)
    @Mapping( target = "addressId", source = "id")
    AddressEntity addressDtoToAddressEntity(AddressDto addressDto);

    AddressDto createAddressToAddressDto(CreateAddressRequest createAddress);
    AddressDto updateAddressToAddressDto(UpdateAddressRequest updateAddressRequest);

    List<AddressDto> createAddressListToAddressDtoList(List<CreateAddressRequest> createAddressList);

    Set<AddressDto> addressEntityListToAddressDtoList(Set<AddressEntity> addressEntities);
    Set<AddressEntity> addressDtoListToAddressEntity(Set<AddressDto> addressDtos);

    List<AddressDto> addressEntityListToAddressDtoList(List<AddressEntity> addressEntities);

    AddressListResponse addressListToAddressResponse(List<AddressDto> addressDtos);

//    @Mappings({
//            @Mapping(target = "id", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
//            @Mapping(target = "street", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
//            @Mapping(target = "city", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
//            @Mapping(target = "state", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
//            @Mapping(target = "postalCode", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
//    })
//    AddressDto updateAddressDto(@MappingTarget AddressDto target, AddressDto source);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "addressId", source = "id"),
            @Mapping(target = "street", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
            @Mapping(target = "city", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
            @Mapping(target = "state", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
            @Mapping(target = "postalCode", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
    })
    AddressEntity updateAddressDtoToEntity(@MappingTarget AddressEntity target, AddressDto source);


//    Set<AddressDto> addressDtoUpdate(@MappingTarget Set<AddressDto> target, Set<AddressDto> source);
}
