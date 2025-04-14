package edu.phystech.hw2.contact;

public class InvalidContactFieldException extends RuntimeException {
    private final String fieldName;

    public InvalidContactFieldException(String fieldName) {
        super("Invalid value in field: " + fieldName);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
