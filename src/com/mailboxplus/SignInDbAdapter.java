/*
 * Copyright (C) 2011 
 * 
 */

package com.mailboxplus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple sessions database access helper class. Defines the basic CRUD operations
 * for the sessionpad example, and gives the ability to list all sessions as well as
 * retrieve or modify a specific session.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class SignInDbAdapter {

    public static final String KEY_EMAIL = "email";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "SessionsDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
        "create table sessions (_id integer primary key autoincrement, "
        + "email text not null, token text not null);";

    private static final String DATABASE_NAME = "mailboxplus_client";
    private static final String DATABASE_TABLE = "sessions";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS sessions");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public SignInDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the sessions database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public SignInDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new session using the email and token provided. If the session is
     * successfully created return the new rowId for that session, otherwise return
     * a -1 to indicate failure.
     * 
     * @param email the email of the session
     * @param token the token of the session
     * @return rowId or -1 if failed
     */
    public long createSession(String email, String token) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_EMAIL, email);
        initialValues.put(KEY_TOKEN, token);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }
    
    /**
     * Delete the session with the given rowId
     * 
     * @param rowId id of session to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteSession(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    
    /**
     * Delete the all sessions 
     * 
     * @return true if deleted, false otherwise
     */
    public boolean deleteAllSessions() {

        return mDb.delete(DATABASE_TABLE, null, null) > 0;
    }

    /**
     * Return a Cursor over the list of all sessions in the database
     * 
     * @return Cursor over all sessions
     */
    public Cursor fetchAllSessions() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_EMAIL,
                KEY_TOKEN}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the session that matches the given rowId
     * 
     * @param rowId id of session to retrieve
     * @return Cursor positioned to matching session, if found
     * @throws SQLException if session could not be found/retrieved
     */
    public Cursor fetchSession(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                    KEY_EMAIL, KEY_TOKEN}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the session using the details provided. The session to be updated is
     * specified using the rowId, and it is altered to use the email and token
     * values passed in
     * 
     * @param rowId id of session to update
     * @param email value to set session email to
     * @param token value to set session token to
     * @return true if the session was successfully updated, false otherwise
     */
    public boolean updateSession(long rowId, String email, String token) {
        ContentValues args = new ContentValues();
        args.put(KEY_EMAIL, email);
        args.put(KEY_TOKEN, token);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
