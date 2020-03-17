package net.digitallogic.UserLogin.persistence.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = "authority")
@Entity(name = "AuthorityEntity")
@Table(name = "authority")
public class AuthorityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 40, unique = true)
    private String authority;

    @ManyToMany(mappedBy="authorities")
    private Set<RoleEntity> roles;

    // == Constructors == //
    public AuthorityEntity() {
        roles = new HashSet<>();
    }

    public AuthorityEntity(String authority) {
        this();
        this.authority = authority;
    }

    // == Methods == //
}
