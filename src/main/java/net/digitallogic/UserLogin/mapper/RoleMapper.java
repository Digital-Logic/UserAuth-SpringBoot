package net.digitallogic.UserLogin.mapper;

import net.digitallogic.UserLogin.persistence.model.RoleEntity;
import net.digitallogic.UserLogin.web.Dto.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Collection;
import java.util.Set;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    RoleDto roleEntityToRoleDto(RoleEntity entity);
    Set<RoleDto> roleEntityListToRoleDtoList(Collection<RoleEntity> roleEntities);
}
