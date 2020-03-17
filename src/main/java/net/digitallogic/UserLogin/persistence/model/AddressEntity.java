package net.digitallogic.UserLogin.persistence.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.digitallogic.UserLogin.shared.Utils;

import javax.persistence.*;
import java.io.Serializable;


@Data
@EqualsAndHashCode(of = "addressId")
@Entity(name = "Addresses")
@Table(name = "addresses")
public class AddressEntity implements Serializable {
    // == Constants == //
    private static final long serialVersionUID = -7712869305103122749L;
    public static final int STATE_MAX_LENGTH = 30;
    public static final int CITY_MAX_LENGTH = 75;
    public static final int STREET_MAX_LENGTH = 75;
    public static final int POSTAL_CODE_MIN_LENGTH = 5;
    public static final int POSTAL_CODE_MAX_LENGTH = 9;

    // == Fields == //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = Utils.KEY_LENGTH, nullable = false, unique = true)
    private String addressId;

    @Column(length = STATE_MAX_LENGTH, nullable = false)
    private String state;

    @Column(length = CITY_MAX_LENGTH, nullable = false)
    private String city;

    @Column(length = STREET_MAX_LENGTH, nullable = false)
    private String street;

    @Column(length = POSTAL_CODE_MAX_LENGTH, nullable = false)
    private String postalCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="user_id",
            nullable = false)
    private UserEntity user;

    // == Constructors == //

    public AddressEntity() {
    }

    public AddressEntity(String street, String city, String state, String postalCode) {
        this.city = city;
        this.state = state;
        this.street = street;
        this.postalCode = postalCode;
    }
}
