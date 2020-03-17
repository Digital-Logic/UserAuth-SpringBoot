package net.digitallogic.UserLogin.persistence.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.digitallogic.UserLogin.shared.Utils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = "userId")
@Entity(name = "UserEntity")
@Table(name = "user")
public class UserEntity implements Serializable {
    // == Constants == //
    private static final long serialVersionUID = 5164605472167291624L;

    // == Fields == //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50, nullable = false)
    private String lastName;

    @Column(length = 120, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String encryptedPassword;

    @Column(nullable = false)
    private boolean emailVerified;

    @Column(nullable = false, unique = true, length = Utils.KEY_LENGTH)
    private String userId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<AddressEntity> addresses;

    @ManyToMany( cascade = { CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name="user_roles",
            joinColumns = @JoinColumn(name= "users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id"))
    private Set<RoleEntity> roles;

    // == Constructors == //
    public UserEntity() {
        addresses = new HashSet<>();
        roles = new HashSet<>();
        emailVerified = false;
    }

    public UserEntity(String firstName, String lastName, String email) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // == Methods == //
    public void addAddress(AddressEntity address) {
        addresses.add(address);
        address.setUser(this);
    }

    public void removeAddress(AddressEntity address) {
        addresses.remove(address);
        address.setUser(null);
    }

    public UserEntity addRole(RoleEntity role) {
            roles.add(role);
            role.getUsers().add(this);
        return this;
    }
    public UserEntity removeRole(RoleEntity role) {
        roles.remove(role);
        role.getUsers().remove(this);
        return this;
    }
}
