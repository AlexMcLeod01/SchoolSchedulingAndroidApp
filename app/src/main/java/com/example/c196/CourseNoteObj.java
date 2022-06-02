package com.example.c196;


/**
 * Just a model object for passing course notes data around
 */
public class CourseNoteObj {
    private int id;
    private String note;
    private int course_id;

    /**
     * Various getters and setters
     */


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public CourseNoteObj (String note, int course_id) {
        setNote(note);
        setCourse_id(course_id);
    }
}
