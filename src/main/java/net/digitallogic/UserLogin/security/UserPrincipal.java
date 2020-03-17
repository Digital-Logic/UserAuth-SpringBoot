package net.digitallogic.UserLogin.security;

import net.digitallogic.UserLogin.persistence.model.UserEntity;
import net.digitallogic.UserLogin.web.exceptions.EntityNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class UserPrincipal implements UserDetails {
    // == Constants == //
    private static final long serialVersionUID = -3491428009646240021L;

    // == Fields == //
    private UserEntity userEntity;

    // == Constructors == //
    public UserPrincipal(UserEntity userEntity) {
        if (userEntity == null)
            throw new EntityNotFoundException();

        this.userEntity = userEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        return userEntity.getRoles().stream()
                .flatMap(role ->
                        role.getAuthorities().stream()
                            .map(authorityEntity -> new SimpleGrantedAuthority(authorityEntity.getAuthority()))
                )
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return userEntity.getEncryptedPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userEntity.isEmailVerified();
    }
}
