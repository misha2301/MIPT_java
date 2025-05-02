package edu.phystech.hw2.contact;


record Contact(String username, String email) implements Comparable<Contact> {

    public static final String UNKNOWN_EMAIL = "unknown";

    Contact {
        if (username.isBlank()) {  // только пробелы тоже нельзя
            throw new InvalidContactFieldException("username");
        }
        if (email == null) {
            email = UNKNOWN_EMAIL;
        } else if (!email.endsWith("@gmail.com")) {
            throw new InvalidContactFieldException("email");
        }
    }

    Contact(String username) {
        this(username, null);
    }


    public int compareTo(Contact o) {
        return Integer.compare(this.username.length(), o.username.length());
    }
}