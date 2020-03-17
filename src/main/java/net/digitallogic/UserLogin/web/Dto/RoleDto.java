package net.digitallogic.UserLogin.web.Dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "name")
@JacksonXmlRootElement(localName = "role")
public class RoleDto {

    // == Fields == //
    private String  name;

    // == Constructors == //
    public RoleDto() {
    }
    public RoleDto(String name) {
        this.name = name;
    }
}
