package com.example.c196;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A database for keeping data
 */
public class TermDataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "terms.db";
    private static final int VERSION = 3;

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

    //Secondary table "classes"

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

        //Next create table "classes"

        //Then create table "assessments"
        db.execSQL("create table " + AssessmentsTable.TABLE + " (" +
                AssessmentsTable.ASSESSMENT_ID + " integer primary key autoincrement, " +
                AssessmentsTable.ASSESSMENT_TITLE + " text, " +
                AssessmentsTable.ASSESSMENT_START + " text, " +
                AssessmentsTable.ASSESSMENT_END + " text," +
                AssessmentsTable.ASSESSMENT_TYPE + " int check (" + AssessmentsTable.ASSESSMENT_TYPE + " in (0, 1))," +
                AssessmentsTable.CLASS_ID + " integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop old tables
        db.execSQL("drop table if exists " + TermTable.TABLE);
        db.execSQL("drop table if exists " + AssessmentsTable.TABLE);

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
        db.delete(TermTable.TABLE,
                TermTable.TERM_ID + " = ?", new String[] {Integer.toString(term.getId()) });
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
     **********************************CLASS TABLE*********************************************
     ******************************************************************************************/

    /******************************************************************************************
     ********************************ASSESSMENT TABLE******************************************
     ******************************************************************************************/

    /**
     * Add an Assessment to the database
     * @param assess AssessmentObj
     * @return true if success
     */
    public boolean addAssessment(AssessmentObj assess) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AssessmentsTable.ASSESSMENT_TITLE, assess.getTitle());
        values.put(AssessmentsTable.ASSESSMENT_START, dateFormat.format(assess.getStartDate()));
        values.put(AssessmentsTable.ASSESSMENT_END, dateFormat.format(assess.getEndDate()));
        values.put(AssessmentsTable.ASSESSMENT_TYPE, assess.isPerformance()? 1: 0);
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

        String orderBy = AssessmentsTable.ASSESSMENT_START + " asc";

        String sql = "select * from " + AssessmentsTable.TABLE + " order by " + orderBy;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                Date start;
                Date end;
                boolean perform = (cursor.getInt(4) == 1);
                try {
                    start = date.parse(cursor.getString(2));
                    end = date.parse(cursor.getString(3));
                    AssessmentObj a = new AssessmentObj(title, start, end, perform);
                    a.setId(id);
                    assessments.add(a);
                } catch (Exception e) {
                    e.printStackTrace();
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AssessmentsTable.ASSESSMENT_TITLE, assess.getTitle());
        values.put(AssessmentsTable.ASSESSMENT_START, dateFormat.format(assess.getStartDate()));
        values.put(AssessmentsTable.ASSESSMENT_END, dateFormat.format(assess.getEndDate()));
        values.put(AssessmentsTable.ASSESSMENT_TYPE, assess.isPerformance()? 1 : 0);
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

        String sql = "select * from " + AssessmentsTable.TABLE + " where " + AssessmentsTable.ASSESSMENT_ID + "= " + id;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                cursor.getString(0);
                String title = cursor.getString(1);
                Date start;
                Date end;
                boolean perform = cursor.getInt(4) == 1;
                try {
                    start = date.parse(cursor.getString(2));
                    end = date.parse(cursor.getString(3));
                    assess = new AssessmentObj(title, start, end, perform);
                    assess.setId(id);
                } catch (Exception e) {
                    assess = new AssessmentObj("", new Date (), new Date(), false);
                    assess.setId(-1);
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return assess;
    }
}
