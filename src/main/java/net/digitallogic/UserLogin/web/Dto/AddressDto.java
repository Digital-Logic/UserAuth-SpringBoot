package net.digitallogic.UserLogin.web.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "Address")
@JsonPropertyOrder({"id", "street","city","state","postalCode"})
public class AddressDto {

    // == Fields == //
    private String id;
    private String street;
    private String city;
    private String state;
    private String postalCode;
}
