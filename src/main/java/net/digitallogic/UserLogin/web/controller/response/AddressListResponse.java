package net.digitallogic.UserLogin.web.controller.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import net.digitallogic.UserLogin.web.Dto.AddressDto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

@Data
@JacksonXmlRootElement(localName = "Addresses")
public class AddressListResponse implements Iterable<AddressDto> {
    // == Fields == //
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "address")
    private List<AddressDto> addresses;

    // == Constructors == //
    public AddressListResponse() {
        addresses = new ArrayList<>();
    }

    // == Methods == //
    @Override
    public Iterator<AddressDto> iterator() {
        return addresses.iterator();
    }

    @Override
    public void forEach(Consumer<? super AddressDto> action) {
        addresses.forEach(action);
    }

    @Override
    public Spliterator<AddressDto> spliterator() {
        return addresses.spliterator();
    }

    public boolean add(AddressDto address) {
        return addresses.add(address);
    }
}
