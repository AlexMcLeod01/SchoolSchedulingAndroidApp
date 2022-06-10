package com.example.c196;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A database for keeping data
 */
public class TermDataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "terms.db";
    private static final int VERSION = 6;

    private static TermDataBase tdb;

    public static TermDataBase getInstance(Context context) {
        if (tdb == null) {
            tdb = new TermDataBase(context);
        }
        return tdb;
    }

    public TermDataBase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    //The primary table "terms"
    private static final class TermTable {
        private static final String TABLE = "terms";
        private static final String TERM_ID = "term_id";
        private static final String TERM_TITLE = "title";
        private static final String TERM_START = "start_date";
        private static final String TERM_END = "end_date";
    }

    //Secondary table "course" and tertiary tables that Classes activity requires
    private static final class CourseTable {
        private static final String TABLE = "course";
        private static final String COURSE_ID = "course_id";
        private static final String COURSE_TITLE = "title";
        private static final String COURSE_START = "start_date";
        private static final String COURSE_END = "end_date";
        private static final String COURSE_STATUS = "status";
        private static final String TERM_ID = "term_id";
    }

    private static final class CourseNotesTable {
        private static final String TABLE = "course_notes";
        private static final String NOTES_ID = "notes_id";
        private static final String NOTE = "note";
        private static final String COURSE_ID = "course_id";
    }

    private static final class InstructorTable {
        private static final String TABLE = "instructor";
        private static final String INSTRUCTOR_ID = "instructor_id";
        private static final String INSTRUCTOR_NAME = "name";
        private static final String INSTRUCTOR_PHONE = "phone_number";
        private static final String INSTRUCTOR_EMAIL = "email_address";
        private static final String COURSE_ID = "course_id";
    }

    //Tertiary table "assessments"
    private static final class AssessmentsTable {
        private static final String TABLE = "assessments";
        private static final String ASSESSMENT_ID = "assessment_id";
        private static final String ASSESSMENT_TITLE = "title";
        private static final String ASSESSMENT_START = "start_date";
        private static final String ASSESSMENT_END = "end_date";
        private static final String ASSESSMENT_TYPE = "assessment_type";
        private static final String CLASS_ID = "class_id";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //First create table "terms"
        db.execSQL("create table " + TermTable.TABLE + " (" +
                TermTable.TERM_ID + " integer primary key autoincrement, " +
                TermTable.TERM_TITLE + " text, " +
                TermTable.TERM_START + " text, " +
                TermTable.TERM_END + " text)");

        //Insert a term placeholder for unassigned classes
        TermObj noTerm = new TermObj("Not Yet Assigned", new Date(), new Date());
        noTerm.setId(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues values = new ContentValues();
        values.put(TermTable.TERM_ID, noTerm.getId());
        values.put(TermTable.TERM_TITLE, noTerm.getTitle());
        values.put(TermTable.TERM_START, dateFormat.format(noTerm.getStartDate()));
        values.put(TermTable.TERM_END, dateFormat.format(noTerm.getEndDate()));
        long id = db.insert(TermTable.TABLE, null, values);

        //Next create table "course"
        db.execSQL("create table " + CourseTable.TABLE + " (" +
                CourseTable.COURSE_ID + " integer primary key autoincrement, " +
                CourseTable.COURSE_TITLE + " text, " +
                CourseTable.COURSE_START + " text, " +
                CourseTable.COURSE_END + " text, " +
                CourseTable.COURSE_STATUS + " text, " +
                CourseTable.TERM_ID + " integer references " + TermTable.TABLE + ")");

        //Placeholder Course for unassigned assessments
        CourseObj noCourse = new CourseObj("Not Yet Assigned", new Date(), new Date(), "Planned");
        noCourse.setId(0);
        noCourse.setTermId(0);
        ContentValues courseValues = new ContentValues();
        courseValues.put(CourseTable.COURSE_ID, noCourse.getId());
        courseValues.put(CourseTable.COURSE_TITLE, noCourse.getTitle());
        courseValues.put(CourseTable.COURSE_START, dateFormat.format(noCourse.getStartDate()));
        courseValues.put(CourseTable.COURSE_END, dateFormat.format(noCourse.getEndDate()));
        courseValues.put(CourseTable.COURSE_STATUS, noCourse.getStatus());
        courseValues.put(CourseTable.TERM_ID, noCourse.getTermId());
        long cId = db.insert(CourseTable.TABLE, null, courseValues);

        //And the supplementary course tables
        db.execSQL("create table " + CourseNotesTable.TABLE + " (" +
                CourseNotesTable.NOTES_ID + " integer primary key autoincrement, " +
                CourseNotesTable.NOTE + " text, " +
                CourseNotesTable.COURSE_ID + " integer references " + CourseTable.TABLE + ")");

        db.execSQL("create table " + InstructorTable.TABLE + " (" +
                InstructorTable.INSTRUCTOR_ID + " integer primary key autoincrement, " +
                InstructorTable.INSTRUCTOR_NAME + " text, " +
                InstructorTable.INSTRUCTOR_PHONE + " text, " +
                InstructorTable.INSTRUCTOR_EMAIL + " text, " +
                InstructorTable.COURSE_ID + " integer references " + CourseTable.TABLE +")");

        //Finally create table "assessments"
        db.execSQL("create table " + AssessmentsTable.TABLE + " (" +
                AssessmentsTable.ASSESSMENT_ID + " integer primary key autoincrement, " +
                AssessmentsTable.ASSESSMENT_TITLE + " text, " +
                AssessmentsTable.ASSESSMENT_START + " text, " +
                AssessmentsTable.ASSESSMENT_END + " text," +
                AssessmentsTable.ASSESSMENT_TYPE + " int check (" + AssessmentsTable.ASSESSMENT_TYPE + " in (0, 1))," +
                AssessmentsTable.CLASS_ID + " integer references " + CourseTable.TABLE + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop old tables
        db.execSQL("drop table if exists " + AssessmentsTable.TABLE);
        db.execSQL("drop table if exists " + CourseNotesTable.TABLE);
        db.execSQL("drop table if exists " + InstructorTable.TABLE);
        db.execSQL("drop table if exists " + CourseTable.TABLE);
        db.execSQL("drop table if exists " + TermTable.TABLE);

        //Rebuild
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                db.execSQL("pragma foreign_keys = on;");
            } else {
                db.setForeignKeyConstraintsEnabled(true);
            }
        }
    }

    /*************************************************************************************
     *********************************TERM TABLE******************************************
     *************************************************************************************/

    /**
     * Getting the table Terms from Terms database
     * @return List of TermObj
     */
    public List<TermObj> getTerms() {
        List<TermObj> terms = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

        String orderBy = TermTable.TERM_START + " asc";

        String sql = "select * from " + TermTable.TABLE + " order by " + orderBy;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                Date start;
                Date end;
                try {
                    start = date.parse(cursor.getString(2));
                    end = date.parse(cursor.getString(3));
                    TermObj t = new TermObj(id, title, start, end);
                    terms.add(t);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return terms;
    }

    /**
     * Adds a TermObj to the Terms database
     * @param term TermObj
     * @return true if success
     */
    public boolean addTerm(TermObj term) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TermTable.TERM_TITLE, term.getTitle());
        values.put(TermTable.TERM_START, dateFormat.format(term.getStartDate()));
        values.put(TermTable.TERM_END, dateFormat.format(term.getEndDate()));
        long id = db.insert(TermTable.TABLE, null, values);
        return id != -1;
    }

    /**
     * Deletes the given TermObj from database
     * @param term TermObj
     */
    public void deleteTerm(TermObj term) {
        SQLiteDatabase db = getWritableDatabase();
        if (getCourseByTermId(term.getId()).size() == 0) {
            db.delete(TermTable.TABLE,
                    TermTable.TERM_ID + " = ?", new String[]{Integer.toString(term.getId())});
        }
    }

    /**
     * Updates the given TermObj
     * @param term
     */
    public void updateTerm(TermObj term) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TermTable.TERM_TITLE, term.getTitle());
        values.put(TermTable.TERM_START, dateFormat.format(term.getStartDate()));
        values.put(TermTable.TERM_END, dateFormat.format(term.getEndDate()));
        db.update(TermTable.TABLE, values,
                TermTable.TERM_ID + " = ?", new String[] { Integer.toString(term.getId())});
    }

    public TermObj getTermById(int id) {
        TermObj term = null;
        SQLiteDatabase db = this.getReadableDatabase();

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

        String sql = "select * from " + TermTable.TABLE + " where " + TermTable.TERM_ID +" = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                cursor.getString(0);
                String title = cursor.getString(1);
                Date start;
                Date end;
                try {
                    start = date.parse(cursor.getString(2));
                    end = date.parse(cursor.getString(3));
                    term = new TermObj(id, title, start, end);
                } catch (Exception e) {
                    term = new TermObj(-1, "", new Date (), new Date());
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return term;
    }

    /******************************************************************************************
     **********************************COURSE TABLE*********************************************
     ******************************************************************************************/
    /**
     * Getting all the courses
     * @return List of CourseObj
     */
    public List<CourseObj> getAllCourses() {
        List<CourseObj> courses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

        String orderBy = CourseTable.COURSE_START + " asc";

        String sql = "select * from " + CourseTable.TABLE + " order by " + orderBy;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                Date start;
                Date end;
                String status = cursor.getString(4);
                int termId = cursor.getInt(5);
                try {
                    start = date.parse(cursor.getString(2));
                    end = date.parse(cursor.getString(3));
                    CourseObj c = new CourseObj(title, start, end, status);
                    c.setId(id);
                    c.setTermId(termId);
                    courses.add(c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return courses;
    }

    /**
     * Getting the courses set for the term with term_id id
     * @return List of CourseObj
     */
    public List<CourseObj> getCourseByTermId(int termId) {
        List<CourseObj> courses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

        String orderBy = CourseTable.COURSE_START + " asc";

        String sql = "select * from " + CourseTable.TABLE + " where " +  CourseTable.TERM_ID + " = " + termId +
                " order by " + orderBy;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                Date start;
                Date end;
                String status = cursor.getString(4);
                try {
                    start = date.parse(cursor.getString(2));
                    end = date.parse(cursor.getString(3));
                    CourseObj c = new CourseObj(title, start, end, status);
                    c.setId(id);
                    c.setTermId(termId);
                    courses.add(c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return courses;
    }

    /**
     * Getting course with course id
     * @return CourseObj
     */
    public CourseObj getCourseById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        CourseObj c = null;

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

        String sql = "select * from " + CourseTable.TABLE + " where " +  CourseTable.COURSE_ID + " = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                cursor.getInt(0);
                String title = cursor.getString(1);
                Date start;
                Date end;
                String status = cursor.getString(4);
                int termId = cursor.getInt(5);
                try {
                    start = date.parse(cursor.getString(2));
                    end = date.parse(cursor.getString(3));
                    c = new CourseObj(title, start, end, status);
                    c.setId(id);
                    c.setTermId(termId);
                } catch (Exception e) {
                    e.printStackTrace();
                    c = new CourseObj();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return c;
    }

    /**
     * Adds a CourseObj to the Terms database
     * @param course CourseObj
     * @return true if success
     */
    public int addCourse (CourseObj course) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CourseTable.COURSE_TITLE, course.getTitle());
        values.put(CourseTable.COURSE_START, dateFormat.format(course.getStartDate()));
        values.put(CourseTable.COURSE_END, dateFormat.format(course.getEndDate()));
        values.put(CourseTable.COURSE_STATUS, course.getStatus());
        values.put(CourseTable.TERM_ID, course.getTermId());
        long id = db.insert(CourseTable.TABLE, null, values);
        return (int) id;
    }

    /**
     * Deletes the given CourseObj from database
     * @param course CourseObj
     */
    public void deleteCourse(CourseObj course) {
        SQLiteDatabase db = getWritableDatabase();
        if (1 == 1) {
            db.delete(CourseTable.TABLE,
                    CourseTable.COURSE_ID + " = ?", new String[]{Integer.toString(course.getId())});
        }
    }

    /**
     * Updates the given CourseObj
     * @param course CourseObj
     */
    public void updateCourse(CourseObj course) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CourseTable.COURSE_TITLE, course.getTitle());
        values.put(CourseTable.COURSE_START, dateFormat.format(course.getStartDate()));
        values.put(CourseTable.COURSE_END, dateFormat.format(course.getEndDate()));
        values.put(CourseTable.COURSE_STATUS, course.getStatus());
        values.put(CourseTable.TERM_ID, course.getTermId());
        db.update(CourseTable.TABLE, values,
                CourseTable.COURSE_ID + " = ?", new String[] { Integer.toString(course.getId())});
    }

    /******************************************************************************************
     *******************************OTHER COURSES SUB TABLES***********************************
     ******************************************************************************************/

    //Course Notes Table Functions
    /**
     * Getting the notes set for the course with course_id id
     * @return List of CourseNotesObj
     */
    public List<CourseNoteObj> getNotesByCourseId(int courseId) {
        List<CourseNoteObj> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String orderBy = CourseNotesTable.NOTES_ID + " asc";

        String sql = "select * from " + CourseNotesTable.TABLE + " where " +  CourseNotesTable.COURSE_ID + " = " + courseId +
                " order by " + orderBy;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String note = cursor.getString(1);
                CourseNoteObj n = new CourseNoteObj(note, courseId);
                n.setId(id);
                notes.add(n);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }

    /**
     * Adds a CourseNoteObj to the Terms database
     * @param notes CourseNoteObj
     * @return true if success
     */
    public boolean addNote (CourseNoteObj notes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CourseNotesTable.NOTE, notes.getNote());
        values.put(CourseNotesTable.COURSE_ID, notes.getCourse_id());
        long id = db.insert(CourseNotesTable.TABLE, null, values);
        return id != -1;
    }

    /**
     * Deletes the given CourseNoteObj from database
     * @param notes CourseNoteObj
     */
    public void deleteNote(CourseNoteObj notes) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(CourseNotesTable.TABLE, CourseNotesTable.NOTES_ID + " = ?",
                new String[]{Integer.toString(notes.getId())});
    }

    /**
     * Updates the given CourseNoteObj
     * @param notes CourseNoteObj
     */
    public void updateNote(CourseNoteObj notes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CourseNotesTable.NOTE, notes.getNote());
        values.put(CourseNotesTable.COURSE_ID, notes.getCourse_id());
        db.update(CourseNotesTable.TABLE, values,
                CourseNotesTable.NOTES_ID + " = ?", new String[] { Integer.toString(notes.getId())});
    }

    //Instructor Table functions
    /**
     * Getting the instructors set for the course with course_id id
     * @return List of CourseInstructor
     */
    public List<CourseInstructor> getInstructorByCourseId(int courseId) {
        List<CourseInstructor> instructor = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String orderBy = InstructorTable.INSTRUCTOR_ID + " asc";

        String sql = "select * from " + InstructorTable.TABLE + " where " +  InstructorTable.COURSE_ID + " = " + courseId +
                " order by " + orderBy;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String phone = cursor.getString(2);
                String email = cursor.getString(3);
                CourseInstructor i = new CourseInstructor(name, phone, email);
                i.setCourseId(courseId);
                i.setId(id);
                instructor.add(i);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return instructor;
    }

    /**
     * Adds a CourseInstructor to the Terms database
     * @param instructor CourseInstructor
     * @return true if success
     */
    public boolean addInstructor (CourseInstructor instructor) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InstructorTable.INSTRUCTOR_NAME, instructor.getName());
        values.put(InstructorTable.INSTRUCTOR_PHONE, instructor.getPhoneNumber());
        values.put(InstructorTable.INSTRUCTOR_EMAIL, instructor.getEmailAddress());
        values.put(InstructorTable.COURSE_ID, instructor.getCourseId());
        long id = db.insert(InstructorTable.TABLE, null, values);
        return id != -1;
    }

    /**
     * Deletes the given CourseInstructor from database
     * @param instructor CourseInstructor
     */
    public void deleteInstructor(CourseInstructor instructor) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(InstructorTable.TABLE, InstructorTable.INSTRUCTOR_ID + " = ?",
                new String[]{Integer.toString(instructor.getId())});
    }

    /**
     * Updates the given CourseInstructor
     * @param instructor CourseInstructor
     */
    public void updateInstructor(CourseInstructor instructor) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InstructorTable.INSTRUCTOR_NAME, instructor.getName());
        values.put(InstructorTable.INSTRUCTOR_PHONE, instructor.getPhoneNumber());
        values.put(InstructorTable.INSTRUCTOR_EMAIL, instructor.getEmailAddress());
        db.update(InstructorTable.TABLE, values,
                InstructorTable.INSTRUCTOR_ID + " = ?", new String[] { Integer.toString(instructor.getId())});
    }


    /******************************************************************************************
     ********************************ASSESSMENT TABLE******************************************
     ******************************************************************************************/

    /**
     * Add an Assessment to the database
     * @param assess AssessmentObj
     * @return true if success
     */
    public boolean addAssessment(AssessmentObj assess) {
        if (assess.getCourseId() < 0) {
            assess.setCourseId(0);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AssessmentsTable.ASSESSMENT_TITLE, assess.getTitle());
        values.put(AssessmentsTable.ASSESSMENT_START, dateFormat.format(assess.getStartDate()));
        values.put(AssessmentsTable.ASSESSMENT_END, dateFormat.format(assess.getEndDate()));
        values.put(AssessmentsTable.ASSESSMENT_TYPE, assess.isPerformance()? 1: 0);
        values.put(AssessmentsTable.CLASS_ID, assess.getCourseId());
        long id = db.insert(AssessmentsTable.TABLE, null, values);
        return id != -1;
    }

    /**
     * Get all the AssessmentObjs in database
     * @return List of AssessmentObj
     */
    public List<AssessmentObj> getAssessments() {
        List<AssessmentObj> assessments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateTime = new SimpleDateFormat("MMM dd yyyy hh:mm aa");

        String orderBy = AssessmentsTable.ASSESSMENT_START + " asc";

        String sql = "select * from " + AssessmentsTable.TABLE + " order by " + orderBy;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                AssessmentObj a;
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                Date start;
                Date end;
                boolean perform = (cursor.getInt(4) == 1);
                try {
                    start = dateTime.parse(cursor.getString(2));
                    end = dateTime.parse(cursor.getString(3));
                    a = new AssessmentObj(title, start, end, perform);
                    a.setId(id);
                    assessments.add(a);
                } catch (Exception e) {
                    try {
                        e.printStackTrace();
                        start = date.parse(cursor.getString(2));
                        end = date.parse(cursor.getString(3));
                        a = new AssessmentObj(title, start, end, perform);
                        a.setId(id);
                        assessments.add(a);
                    } catch (Exception e0) {
                        e0.printStackTrace();
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return assessments;
    }

    /**
     * Delete an assessment based upon id
     * @param assess AssessmentObj
     */
    public void deleteAssessment(AssessmentObj assess) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(AssessmentsTable.TABLE,
                AssessmentsTable.ASSESSMENT_ID + " = ?", new String[] {Integer.toString(assess.getId()) });
    }

    /**
     * Update an assessment based upon the given assessment's id
     * @param assess AssessmentObj
     */
    public void updateAssessment(AssessmentObj assess) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy hh:mm aa");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AssessmentsTable.ASSESSMENT_TITLE, assess.getTitle());
        values.put(AssessmentsTable.ASSESSMENT_START, dateFormat.format(assess.getStartDate()));
        values.put(AssessmentsTable.ASSESSMENT_END, dateFormat.format(assess.getEndDate()));
        values.put(AssessmentsTable.ASSESSMENT_TYPE, assess.isPerformance()? 1 : 0);
        values.put(AssessmentsTable.CLASS_ID, assess.getCourseId());
        db.update(AssessmentsTable.TABLE, values,
                AssessmentsTable.ASSESSMENT_ID + " = ?", new String[] { Integer.toString(assess.getId())});
    }

    /**
     * Gets an Assessment by id
     * @param id assessment id
     * @return AssessmentObj
     */
    public AssessmentObj getAssessmentById(int id) {
        AssessmentObj assess = null;
        SQLiteDatabase db = this.getReadableDatabase();

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateTime = new SimpleDateFormat("MMM dd yyyy hh:mm aa");

        String sql = "select * from " + AssessmentsTable.TABLE + " where " + AssessmentsTable.ASSESSMENT_ID + "= " + id;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                cursor.getString(0);
                String title = cursor.getString(1);
                Date start;
                Date end;
                boolean perform = cursor.getInt(4) == 1;
                int courseId = cursor.getInt(5);
                try {
                    start = dateTime.parse(cursor.getString(2));
                    end = dateTime.parse(cursor.getString(3));
                    assess = new AssessmentObj(title, start, end, perform);
                    assess.setId(id);
                    assess.setCourseId(courseId);
                } catch (Exception e) {
                    try {
                        start = date.parse(cursor.getString(2));
                        end = date.parse(cursor.getString(3));
                        assess = new AssessmentObj(title, start, end, perform);
                        assess.setId(id);
                        assess.setCourseId(courseId);
                    } catch (Exception e0) {
                        assess = new AssessmentObj("", new Date (), new Date(), false);
                        assess.setId(-1);
                        assess.setCourseId(-1);
                        e0.printStackTrace();
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return assess;
    }

    /**
     * Gets an Assessment by course_id
     * @param courseId course id
     * @return AssessmentObj
     */
    public List<AssessmentObj> getAssessmentByCourseId(int courseId) {
        List<AssessmentObj> assessments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateTime = new SimpleDateFormat("MMM dd yyyy hh:mm aa");

        String sql = "select * from " + AssessmentsTable.TABLE + " where " + AssessmentsTable.CLASS_ID + "= " + courseId;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                AssessmentObj assess;
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                Date start;
                Date end;
                boolean perform = cursor.getInt(4) == 1;
                try {
                    start = dateTime.parse(cursor.getString(2));
                    end = dateTime.parse(cursor.getString(3));
                    assess = new AssessmentObj(title, start, end, perform);
                    assess.setId(id);
                    assess.setCourseId(courseId);
                    assessments.add(assess);
                } catch (Exception e) {
                    try {
                        start = date.parse(cursor.getString(2));
                        end = date.parse(cursor.getString(3));
                        assess = new AssessmentObj(title, start, end, perform);
                        assess.setId(id);
                        assess.setCourseId(courseId);
                    } catch (Exception e0) {
                        assess = new AssessmentObj("", new Date (), new Date(), false);
                        assess.setId(-1);
                        e0.printStackTrace();
                    }
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return assessments;
    }
}
