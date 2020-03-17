package net.digitallogic.UserLogin.web.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = "id")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "user")
@JsonPropertyOrder({"id", "firstName", "lastName", "email", "emailVerified","addresses"})
public class UserDto {
    // == Constants == //

    // == Fields == //
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Boolean emailVerified;

    @JacksonXmlProperty(localName = "address")
    @JacksonXmlElementWrapper(localName = "addresses")
    private Set<AddressDto> addresses;

    @JacksonXmlProperty(localName = "role")
    @JacksonXmlElementWrapper(localName = "roles")
    private Set<RoleDto> roles;

    // == Constructors == //

    public UserDto() {
        emailVerified = false;
        addresses = new HashSet<>();
        roles = new HashSet<>();
    }

    public UserDto(String firstName, String lastName, String email, String password) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
