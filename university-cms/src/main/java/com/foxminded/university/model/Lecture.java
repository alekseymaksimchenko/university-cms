package com.foxminded.university.model;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "lectures", schema = "university")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "lecture_date")
    private String lectureDate;

    @Column(name = "lecture_time")
    private String lectureTime;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "group_id")
    private Group group;

    public Lecture() {

    }

    public Lecture(String lectureDate, String lectureTime) {
        super();
        this.lectureDate = lectureDate;
        this.lectureTime = lectureTime;
    }

    public Lecture(String lectureDate, String lectureTime, Course course, Group group) {
        super();
        this.lectureDate = lectureDate;
        this.lectureTime = lectureTime;
        this.course = course;
        this.group = group;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLectureDate() {
        return lectureDate;
    }

    public void setLectureDate(String lectureDate) {
        this.lectureDate = lectureDate;
    }

    public String getLectureTime() {
        return lectureTime;
    }

    public void setLectureTime(String lectureTime) {
        this.lectureTime = lectureTime;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, group, id, lectureDate, lectureTime);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || (getClass() != obj.getClass()))
            return false;
        Lecture other = (Lecture) obj;
        return Objects.equals(course, other.course) && Objects.equals(group, other.group) && id == other.id
                && Objects.equals(lectureDate, other.lectureDate) && Objects.equals(lectureTime, other.lectureTime);
    }

    @Override
    public String toString() {
        return "Lecture [id=" + id + ", lectureDate=" + lectureDate + ", lectureTime=" + lectureTime + ", course="
                + course + ", group=" + group + "]";
    }

}
