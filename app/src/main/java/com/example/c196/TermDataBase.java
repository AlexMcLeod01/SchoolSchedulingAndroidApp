package com.example.c196;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A database for keeping data
 */
public class TermDataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "terms.db";
    private static final int VERSION = 1;

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

    private static final class TermTable {
        private static final String TABLE = "terms";
        private static final String TERM_ID = "term_id";
        private static final String TERM_TITLE = "title";
        private static final String TERM_START = "start_date";
        private static final String TERM_END = "end_date";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TermTable.TABLE + " (" +
                TermTable.TERM_ID + " integer primary key autoincrement, " +
                TermTable.TERM_TITLE + " text, " +
                TermTable.TERM_START + " text, " +
                TermTable.TERM_END + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TermTable.TABLE);
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
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TermTable.TERM_TITLE, term.getTitle());
        values.put(TermTable.TERM_START, term.getStartDate().toString());
        values.put(TermTable.TERM_END, term.getEndDate().toString());
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
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TermTable.TERM_TITLE, term.getTitle());
        values.put(TermTable.TERM_START, term.getStartDate().toString());
        values.put(TermTable.TERM_END, term.getEndDate().toString());
        db.update(TermTable.TABLE, values,
                TermTable.TERM_ID + " = ?", new String[] { Integer.toString(term.getId())});
    }
}
