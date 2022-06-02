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
    private List<CourseInstructor> instructorList;
    private List<AssessmentObj> assessmentList;
    private List<String> courseNotes;
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

    public List<CourseInstructor> getInstructorList() {
        return instructorList;
    }

    public void setInstructorList(List<CourseInstructor> instructorList) {
        this.instructorList = instructorList;
    }

    public List<AssessmentObj> getAssessmentList() {
        return assessmentList;
    }

    public void setAssessmentList(List<AssessmentObj> assessmentList) {
        this.assessmentList = assessmentList;
    }

    public List<String> getCourseNotes() {
        return courseNotes;
    }

    public void setCourseNotes(List<String> courseNotes) {
        this.courseNotes = courseNotes;
    }

    public void addInstructor(CourseInstructor instructor) {
        this.instructorList.add(instructor);
    }

    public boolean removeInstructor(CourseInstructor instructor) {
        return this.instructorList.remove(instructor);
    }

    public void addAssessment(AssessmentObj assess) {
        this.assessmentList.add(assess);
    }

    public boolean removeAssessment(AssessmentObj assess) {
        return this.assessmentList.remove(assess);
    }

    public void addCourseNote(String note) {
        this.courseNotes.add(note);
    }

    public boolean removeCourseNote(String note) {
        return this.courseNotes.remove(note);
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
        setId(-1);
        setTermId(-1);
        this.instructorList = new ArrayList<>();
        this.assessmentList = new ArrayList<>();
        this.courseNotes = new ArrayList<>();
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
}
