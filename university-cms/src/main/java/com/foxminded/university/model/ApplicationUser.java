package com.foxminded.university.model;

import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "users", schema = "university", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private long id;
    private String username;
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", schema = "university",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))

    private Set<Role> autorities;

    public ApplicationUser() {
        super();
    }

    public ApplicationUser(String username, String password, Set<Role> autorities) {
        super();
        this.username = username;
        this.password = password;
        this.autorities = autorities;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getAutorities() {
        return autorities;
    }

    public void setAutorities(Set<Role> autorities) {
        this.autorities = autorities;
    }

    @Override
    public int hashCode() {
        return Objects.hash(autorities, id, password, username);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || (getClass() != obj.getClass()))
            return false;
        ApplicationUser other = (ApplicationUser) obj;
        return Objects.equals(autorities, other.autorities) && id == other.id
                && Objects.equals(password, other.password) && Objects.equals(username, other.username);
    }

    @Override
    public String toString() {
        return "ApplicationUser [id=" + id + ", username=" + username + ", password=" + password + ", autorities="
                + autorities + "]";
    }

}
