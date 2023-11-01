package com.foxminded.university.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles", schema = "university")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private long id;

    private String autority;

    public Role() {

    }

    public Role(String autority) {
        this.autority = autority;
    }

    public Role(long id, String autority) {
        this.id = id;
        this.autority = autority;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAutority() {
        return autority;
    }

    public void setAutority(String autority) {
        this.autority = autority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(autority, id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || (getClass() != obj.getClass()))
            return false;
        Role other = (Role) obj;
        return Objects.equals(autority, other.autority) && id == other.id;
    }

    @Override
    public String toString() {
        return "Role [id=" + id + ", autority=" + autority + "]";
    }

}
