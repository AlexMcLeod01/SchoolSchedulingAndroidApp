package com.example.c196;

/**
 * CourseInstructor Object to help CourseObj
 */
public class CourseInstructor {
    private String name;
    private String phoneNumber;
    private String emailAddress;


    /**
     * Just a bunch of getters and setters
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * The constructor for this object
     * @param name String
     * @param phone String
     * @param email String
     */
    public CourseInstructor(String name, String phone, String email) {
        setName(name);
        setPhoneNumber(phone);
        setEmailAddress(email);
    }
}
