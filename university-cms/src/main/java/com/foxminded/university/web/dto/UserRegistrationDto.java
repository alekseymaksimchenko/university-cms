package com.foxminded.university.web.dto;

import java.util.Objects;

public class UserRegistrationDto {

    private String userName;
    private String password;
    private String role;

    public UserRegistrationDto() {
        super();
    }

    public UserRegistrationDto(String userName, String password, String role) {
        super();
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(password, role, userName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || (getClass() != obj.getClass()))
            return false;
        UserRegistrationDto other = (UserRegistrationDto) obj;
        return Objects.equals(password, other.password) && Objects.equals(role, other.role)
                && Objects.equals(userName, other.userName);
    }

    @Override
    public String toString() {
        return "UserRegistrationDto [userName=" + userName + ", password=" + password + ", role=" + role + "]";
    }

}
