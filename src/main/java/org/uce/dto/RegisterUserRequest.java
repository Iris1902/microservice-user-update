package org.uce.dto;

import java.io.Serializable;

public class RegisterUserRequest implements Serializable {
    public String username;
    public String email;
    public String password;
    public String fullName;
    public String phoneNumber;
    public String address;
    public String role; // "CUSTOMER", "ADMIN", etc.

    public RegisterUserRequest() {}
}