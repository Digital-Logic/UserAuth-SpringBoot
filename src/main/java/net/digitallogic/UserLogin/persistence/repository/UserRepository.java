package net.digitallogic.UserLogin.persistence.repository;

import net.digitallogic.UserLogin.persistence.model.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUserId(String user);

    @Modifying
    @Query("UPDATE UserEntity set emailVerified = true where userId = :userId")
    void setUserVerificationStatusToVerified(@Param("userId") String userId);

    @Query("SELECT user FROM UserEntity user JOIN FETCH user.roles WHERE user.userId = :userId")
    UserEntity findByUserIdWithRoles(@Param("userId") String userId);

    @Query("SELECT user FROM UserEntity user " +
            "LEFT JOIN FETCH user.roles roles " +
            "LEFT JOIN FETCH roles.authorities authorities " +
            "where user.userId = :userId")
    UserEntity findByUserIdWithAuthorities(@Param("userId") String userId);

    @Query("SELECT user  FROM UserEntity user " +
            "LEFT JOIN FETCH user.roles roles " +
            "LEFT JOIN FETCH roles.authorities authorities " +
            "where user.email = :email")
    UserEntity findByEmailWithAuthorities(@Param("email") String email);
}
