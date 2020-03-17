package net.digitallogic.UserLogin.persistence.repository;

import net.digitallogic.UserLogin.persistence.model.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {
    RoleEntity findByName(String name);
}
