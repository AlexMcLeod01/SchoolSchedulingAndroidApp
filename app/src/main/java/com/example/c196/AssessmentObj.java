package com.example.c196;

import java.util.Date;

/**
 * Assessment object to help with CourseObj model
 */
public class AssessmentObj {
    private String title;
    private Date startDate;
    private Date endDate;
    private boolean isPerformance; //true if performance, false if objective

    /**
     *A bunch of getters and setters
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

    public boolean isPerformance() {
        return isPerformance;
    }

    public void setPerformance(boolean performance) {
        isPerformance = performance;
    }

    public AssessmentObj(String title, Date start, Date end, boolean perform) {
        setTitle(title);
        setStartDate(start);
        setEndDate(end);
        setPerformance(perform);
    }
}
