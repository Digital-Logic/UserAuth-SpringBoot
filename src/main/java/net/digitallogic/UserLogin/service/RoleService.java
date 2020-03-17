package net.digitallogic.UserLogin.service;

import net.digitallogic.UserLogin.persistence.model.RoleEntity;
import net.digitallogic.UserLogin.persistence.repository.RoleRepository;
import net.digitallogic.UserLogin.web.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    // == Constructors == //
    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // == Methods == //
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public RoleEntity getRole(String name) {
        RoleEntity role = roleRepository.findByName(name);
        if (role == null)
            throw new EntityNotFoundException();

        return role;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<RoleEntity> getRoles() {
        List<RoleEntity> roles = new ArrayList<>();
        roleRepository.findAll().forEach(roles::add);

        return roles;
    }
}
