package net.digitallogic.UserLogin.persistence.repository;

import net.digitallogic.UserLogin.persistence.model.AuthorityEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Integer> {
    AuthorityEntity findByAuthority(String authority);
}
