package net.digitallogic.UserLogin.web.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "Error")
public class ErrorDto<T> implements Serializable {
    // == Constants == //
    private static final long serialVersionUID = 8803006459428643809L;

    // == Fields == //
    private Date timestamp;
    private String path;
    private T message;

    // == Constructors == //
    public ErrorDto() {
        timestamp = new Date();
    }

    public ErrorDto(T message) {
        this();
        this.message = message;
    }

    public ErrorDto(T message, String path) {
        this();
        this.message = message;
        this.path = path.replace("uri=", "");
    }
}
