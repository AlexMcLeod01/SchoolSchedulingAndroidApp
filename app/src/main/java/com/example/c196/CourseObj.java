package com.example.c196;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A model object for Courses to help TermObj and TermList
 */
public class CourseObj implements Serializable {
    private int id;
    private String title;
    private Date startDate;
    private Date endDate;
    private String status;
    private List<AssessmentObj> assessmentList;
    private int termId;


    /**
     * Getters and Setters for CourseObj
     */

    public int getTermId() { return termId; }

    public void setTermId(int term) { this.termId = term; }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AssessmentObj> getAssessmentList() {
        return assessmentList;
    }

    public void setAssessmentList(List<AssessmentObj> assessmentList) {
        this.assessmentList = assessmentList;
    }

    /**
     * Constructor with no instructors, assessments or notes
     * @param title String
     * @param start Date
     * @param end Date
     * @param status String - in progress, completed, dropped, plan to take
     */
    public CourseObj(String title, Date start, Date end, String status) {
        setTitle(title);
        setStartDate(start);
        setEndDate(end);
        setStatus(status);
        setTermId(0);
        this.assessmentList = new ArrayList<>();
    }

    /**
     * Default constructor
     */
    public CourseObj() {
        setId(-1);
        setTitle("");
        setStartDate(new Date());
        setEndDate(new Date());
        setStatus("");
        setTermId(-1);
    }

    public String toString() {
        return getId() + ": " + getTitle();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CourseObj) {
            CourseObj c = (CourseObj) obj;
            if (this.getId() == c.getId()) {
                return true;
            }
        }
        return false;
    }
}
