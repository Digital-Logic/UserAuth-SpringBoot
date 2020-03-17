package net.digitallogic.UserLogin.persistence.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = "name")
@Entity(name = "RoleEntity")
@Table(name = "roles")
public class RoleEntity {

    // == Fields == //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 40, unique = true)
    private String name;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(
            name="roles_authorities",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id")
    )
    private Set<AuthorityEntity> authorities;

    @ManyToMany(mappedBy="roles")
    private Set<UserEntity> users;

    // == Constructors == //
    public RoleEntity() {
        users = new HashSet<>();
        authorities = new HashSet<>();
    }

    public RoleEntity(String name) {
        this();
        this.name = name;
    }

    // == Methods == //

    public RoleEntity addAuthority(AuthorityEntity authorityEntity) {
        this.authorities.add(authorityEntity);
        authorityEntity.getRoles().add(this);

        return this;
    }

    public RoleEntity removeAuthority(AuthorityEntity authorityEntity) {
        this.authorities.remove(authorityEntity);
        authorityEntity.getRoles().remove(this);

        return this;
    }
}
