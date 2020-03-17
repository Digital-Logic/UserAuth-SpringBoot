package net.digitallogic.UserLogin.web.controller.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import net.digitallogic.UserLogin.web.Dto.UserDto;

import java.util.*;
import java.util.function.Consumer;

@Data
@JacksonXmlRootElement(localName = "users")
public class UserListResponse implements Iterable<UserDto> {

    // == Fields == //
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "user")
    private List<UserDto> users;

    // == Constructors == //
    public UserListResponse() {
        users = new ArrayList<>();
    }

    public UserListResponse(List<UserDto> users) {
        // initialize list
        this();
        Collections.copy(this.users, users);
    }

    @Override
    public Iterator<UserDto> iterator() {
        return users.iterator();
    }

    @Override
    public void forEach(Consumer action) {
        users.forEach(action);
    }

    @Override
    public Spliterator spliterator() {
        return users.spliterator();
    }

    public boolean add(UserDto user) {
        return users.add(user);
    }

    //    @Override
//    public int size() {
//        return users.size();
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return users.isEmpty();
//    }
//
//    @Override
//    public boolean contains(Object o) {
//        return users.contains(o);
//    }
//
//    @Override
//    public Iterator<UserDto> iterator() {
//        return users.iterator();
//    }
//
//    @Override
//    public Object[] toArray() {
//        return users.toArray();
//    }
//
//    @Override
//    public <T> T[] toArray(T[] a) {
//        return users.toArray(a);
//    }
//
//    @Override
//    public boolean add(UserDto userDto) {
//        return users.add(userDto);
//    }
//
//    @Override
//    public boolean remove(Object o) {
//        return users.remove(o);
//    }
//
//    @Override
//    public boolean containsAll(Collection<?> c) {
//        return users.containsAll(c);
//    }
//
//    @Override
//    public boolean addAll(Collection<? extends UserDto> c) {
//        return users.addAll(c);
//    }
//
//    @Override
//    public boolean addAll(int index, Collection<? extends UserDto> c) {
//        return users.addAll(index, c);
//    }
//
//    @Override
//    public boolean removeAll(Collection<?> c) {
//        return users.removeAll(c);
//    }
//
//    @Override
//    public boolean retainAll(Collection<?> c) {
//        return users.retainAll(c);
//    }
//
//    @Override
//    public void clear() {
//        users.clear();
//    }
//
//    @Override
//    public UserDto get(int index) {
//        return users.get(index);
//    }
//
//    @Override
//    public UserDto set(int index, UserDto element) {
//        return users.set(index, element);
//    }
//
//    @Override
//    public void add(int index, UserDto element) {
//        users.add(index, element);
//    }
//
//    @Override
//    public UserDto remove(int index) {
//        return users.remove(index);
//    }
//
//    @Override
//    public int indexOf(Object o) {
//        return users.indexOf(o);
//    }
//
//    @Override
//    public int lastIndexOf(Object o) {
//        return users.lastIndexOf(o);
//    }
//
//    @Override
//    public ListIterator<UserDto> listIterator() {
//        return users.listIterator();
//    }
//
//    @Override
//    public ListIterator<UserDto> listIterator(int index) {
//        return users.listIterator(index);
//    }
//
//    @Override
//    public List<UserDto> subList(int fromIndex, int toIndex) {
//        return users.subList(fromIndex, toIndex);
//    }
}
