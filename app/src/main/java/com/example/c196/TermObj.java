package com.example.c196;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Simple objects to help TermList object work
 */
public class TermObj implements Serializable {
    private int id;
    private String title;
    private Date startDate;
    private Date endDate;
    private List<CourseObj> courses;

    /**
     * Generic getter and setter functions
     */

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<CourseObj> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseObj> courses) {
        this.courses = courses;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Constructor with no courses added to course
     * @param title
     * @param start
     * @param end
     */
    public TermObj(int id, String title, Date start, Date end) {
        setId(id);
        setTitle(title);
        setStartDate(start);
        setEndDate(end);
        this.courses = new ArrayList<>();
    }

    public TermObj(String title, Date start, Date end) {
        setId(id);
        setTitle(title);
        setStartDate(start);
        setEndDate(end);
        this.courses = new ArrayList<>();
    }

    public String toString() {
        return getId() + ": " + getTitle();
    }
}
