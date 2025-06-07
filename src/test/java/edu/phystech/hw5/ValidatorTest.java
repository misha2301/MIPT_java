package edu.phystech.hw5;

import java.lang.reflect.Field;
import edu.phystech.hw5.annotation.validation.NotBlank;
import edu.phystech.hw5.annotation.validation.Size;
import edu.phystech.hw5.exception.ValidationException;
import edu.phystech.hw5.service.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author kzlv4natoly
 */
public class ValidatorTest {

    private Validator validator = object -> {
        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.getType().equals(String.class)) continue;

            field.setAccessible(true);
            String value;
            try {
                value = (String) field.get(object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            NotBlank notBlank = field.getAnnotation(NotBlank.class);
            if (notBlank != null) {
                if (value == null || value.trim().isEmpty()) {
                    String message = notBlank.message().isEmpty()
                            ? "Field must not be blank"
                            : notBlank.message();
                    throw new ValidationException(message);
                }
            }

            Size size = field.getAnnotation(Size.class);
            if (size != null) {
                int length = value != null ? value.length() : 0;
                if (length < size.min() || length > size.max()) {
                    String message = size.message().isEmpty()
                            ? "Field length must be between " + size.min() + " and " + size.max()
                            : size.message();
                    throw new ValidationException(message);
                }
            }
        }
    };


    @Test
    void notBlankWorks() {
        class Example {

            @NotBlank
            private final String x;

            @NotBlank(message = "This is a very important field and it can't be empty!")
            private final String y;

            Example(String x, String y) {
                this.x = x;
                this.y = y;
            }
        }

        Assertions.assertDoesNotThrow(() -> validator.validate(new Example("123", "567")));
        ValidationException exception =
                Assertions.assertThrows(ValidationException.class, () -> validator.validate(new Example("11", "")));
        Assertions.assertEquals("This is a very important field and it can't be empty!", exception.getMessage());
    }

    @Test
    void sizeWorks() {
        class Example {
            @Size(max = 52, message = "Long live Saint Petersburg!")
            private final String x;

            @Size(min = 5, max = 11)
            private final String y;

            Example(String x, String y) {
                this.x = x;
                this.y = y;
            }
        }

        Assertions.assertDoesNotThrow(() -> validator.validate(new Example("123", "567765")));
        ValidationException exception =
                Assertions.assertThrows(ValidationException.class, () -> validator.validate(new Example("", "")));
        Assertions.assertEquals("Long live Saint Petersburg!", exception.getMessage());
        Assertions.assertThrows(ValidationException.class, () -> validator.validate(new Example("", "0000000000000")));
    }

}
