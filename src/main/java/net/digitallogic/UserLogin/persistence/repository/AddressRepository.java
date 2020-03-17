package net.digitallogic.UserLogin.persistence.repository;

import net.digitallogic.UserLogin.persistence.model.AddressEntity;
import net.digitallogic.UserLogin.persistence.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
    AddressEntity findByAddressId(String id);
    List<AddressEntity> findByUser(UserEntity user);

    List<AddressEntity> findByUserUserId(String userId);
}
