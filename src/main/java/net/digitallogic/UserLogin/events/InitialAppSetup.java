package net.digitallogic.UserLogin.events;

import lombok.extern.slf4j.Slf4j;
import net.digitallogic.UserLogin.persistence.model.AuthorityEntity;
import net.digitallogic.UserLogin.persistence.model.RoleEntity;
import net.digitallogic.UserLogin.persistence.model.UserEntity;
import net.digitallogic.UserLogin.persistence.repository.AuthorityRepository;
import net.digitallogic.UserLogin.persistence.repository.RoleRepository;
import net.digitallogic.UserLogin.persistence.repository.UserRepository;
import net.digitallogic.UserLogin.security.Authority;
import net.digitallogic.UserLogin.shared.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InitialAppSetup {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final Utils utils;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public InitialAppSetup(UserRepository userRepository, RoleRepository roleRepository,
                           AuthorityRepository authorityRepository, Utils utils, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.utils = utils;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @EventListener
    @Transactional
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {
        log.info("Application Starting!!!");

        // Create authorities in the database
        Map<String, AuthorityEntity> authorities = Arrays.stream(Authority.values())
                .map(authority -> {
                    AuthorityEntity authorityEntity = authorityRepository.findByAuthority(authority.auth);
                    if (authorityEntity == null) {
                        authorityEntity = authorityRepository.save(new AuthorityEntity(authority.auth));
                    }
                    return authorityEntity;
                })
                .collect(Collectors.toMap(authorityEntity -> authorityEntity.getAuthority(), authorityEntity -> authorityEntity));

        // Create base Roles
        String[] basicRoles = {
            "ROLE_USER",
            "ROLE_ADMIN"
        };

        Map<String, RoleEntity> roles = Arrays.stream(basicRoles)
                .map(roleName -> {
                    RoleEntity roleEntity = roleRepository.findByName(roleName);
                    if (roleEntity == null) {
                        roleEntity = roleRepository.save(new RoleEntity(roleName));
                    }
                    return roleEntity;
                })
                .collect(Collectors.toMap(role -> role.getName(), role -> role));

        roles.get("ROLE_ADMIN")
                .addAuthority(authorities.get(Authority.ADMIN_ROLES.auth))
                .addAuthority(authorities.get(Authority.ADMIN_USER.auth));

        // if no users are in the database, add one.
        if (userRepository.count() == 0) {
            UserEntity adminUser = new UserEntity("Admin", "Administrator", "admin@testing.com");
            adminUser.setEmailVerified(true);
            adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("administrator"));
            adminUser.setUserId(utils.generateId());
            adminUser.addRole(roles.get("ROLE_ADMIN"))
                    .addRole(roles.get("ROLE_USER"));

            userRepository.save(adminUser);
        }
    }
}
