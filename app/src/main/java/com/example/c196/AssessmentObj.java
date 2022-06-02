package com.example.c196;

import java.io.Serializable;
import java.util.Date;

/**
 * Assessment object to help with CourseObj model
 */
public class AssessmentObj implements Serializable {
    private int id;
    private String title;
    private Date startDate;
    private Date endDate;
    private boolean isPerformance; //true if performance, false if objective
    private int courseId;

    /**
     *A bunch of getters and setters
     */
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

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

    public boolean isPerformance() {
        return isPerformance;
    }

    public void setPerformance(boolean performance) {
        isPerformance = performance;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public AssessmentObj(String title, Date start, Date end, boolean perform) {
        setTitle(title);
        setStartDate(start);
        setEndDate(end);
        setPerformance(perform);
        setCourseId(-1);
    }
}
