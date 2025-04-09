package edu.phystech.hw2.contact;

import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class ContactTest {

    @Test
    public void contactCreationTest() {
        Assertions.assertDoesNotThrow(() -> {
            Person alpha = new Person("username", "username@gmail.com");
            Person beta = new Person("123", "123@gmail.com");

            Person noEmail = new Person("noEmailProvided");
            Assertions.assertEquals(Person.DEFAULT_EMAIL, noEmail.getEmail());
        });
    }

    @Test
    public void invalidInputTest() {
        var ex = Assertions.assertThrows(FieldValidationException.class, () -> new Person("   ", "123@gmail.com"));
        Assertions.assertEquals("username", ex.getInvalidField());

        ex = Assertions.assertThrows(FieldValidationException.class, () -> new Person("   1", "123@mail.ru"));
        Assertions.assertEquals("email", ex.getInvalidField());

        ex = Assertions.assertThrows(FieldValidationException.class, () -> new Person("   ", ""));
        Assertions.assertEquals("username", ex.getInvalidField());
    }

    @Test
    public void sortingTest() {
        var sortedNames = Stream.of(new Person("AFD"), new Person("a"), new Person("zZ"))
                .sorted(Person::compareTo)
                .map(Person::getUsername)
                .toList();

        Assertions.assertEquals(List.of("a", "zZ", "AFD"), sortedNames);
        Assertions.assertInstanceOf(Comparable.class, new Person("AFD"));
    }
}

class Person implements Comparable<Person> {
    public static final String DEFAULT_EMAIL = "unknown@gmail.com";

    private final String name;
    private final String emailAddress;

    Person(String name) {
        this(name, DEFAULT_EMAIL);
    }

    Person(String name, String emailAddress) {
        checkName(name);
        checkEmail(emailAddress);

        this.name = name.trim();
        this.emailAddress = emailAddress.trim();
    }

    private void checkName(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new FieldValidationException("username");
        }
    }

    private void checkEmail(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new FieldValidationException("email");
        }

        String pattern = "^[A-Za-z0-9+_.-]+@gmail\\.com$";
        if (!input.trim().matches(pattern)) {
            throw new FieldValidationException("email");
        }
    }

    String getUsername() {
        return name;
    }

    String getEmail() {
        return emailAddress;
    }

    @Override
    public int compareTo(Person other) {
        return Integer.compare(this.name.length(), other.name.length());
    }
}

class FieldValidationException extends RuntimeException {
    private final String invalidField;

    FieldValidationException(String field) {
        this.invalidField = field;
    }

    String getInvalidField() {
        return invalidField;
    }
}
