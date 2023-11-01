package com.foxminded.university.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "groups", schema = "university")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "group_name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "group")
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "group")
    private Set<Lecture> lectures = new HashSet<>();

    public Group() {
    }

    public Group(String name) {
        this.name = name;
    }

    public void addStudentToGroup(Student student) {
        students.add(student);
    }

    public void removeStudentFromGroup(Student student) {
        students.remove(student);
    }

    public void addLectureToGroup(Lecture lecture) {
        lectures.add(lecture);
    }

    public void removeLectureFromGroup(Lecture lecture) {
        lectures.remove(lecture);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || (getClass() != obj.getClass()))
            return false;
        Group other = (Group) obj;
        return id == other.id && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return String.format("Group %s (id - %s)", name, id);
    }

}
