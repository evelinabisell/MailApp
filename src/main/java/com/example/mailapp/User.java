package com.example.mailapp;

/**
 * Class for user info that holds username and password.
 *
 * @author Evelina Bisell
 * @version 1.0
 */
// @TODO Increase safety of password storing
class User {
    private final String username, password;

    protected User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    protected String getUsername() {
        return username;
    }

    protected String getPassword() {
        return password;
    }
}
