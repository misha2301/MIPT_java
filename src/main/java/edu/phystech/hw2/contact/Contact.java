package edu.phystech.hw2.contact;

public record Contact(String username, String email, String phoneNumber) implements Comparable<Contact> {

    public Contact {
        if (username == null || username.isBlank()) {
            throw new InvalidContactFieldException("username");
        }
        if (email == null || !email.endsWith("@gmail.com")) {
            throw new InvalidContactFieldException("email");
        }
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new InvalidContactFieldException("phoneNumber");
        }
    }

    @Override
    public int compareTo(Contact other) {
        return Integer.compare(this.username.length(), other.username.length());
    }
}
