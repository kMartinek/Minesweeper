package com.example.kmartin.minesweeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by kmartin on 2/6/19.
 */

public class DBAdapter {


    public static final String TABLE_NAME = "HighScores2";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_REZULTAT = "rezultat";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_TEZINA = "tezina";





    static final String KEY_ROWID = "id";
    static final String KEY_REZULTAT = "rezultat";
    static final String KEY_TIMESTAMP="timestamp";
    static final String KEY_TEZINA="tezina";
    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "MyDB2";
    static final String DATABASE_TABLE = "HighScores2";
    static final int DATABASE_VERSION = 4;

    static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_REZULTAT + " INTEGER ,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP ,"
                    + COLUMN_TEZINA + " INTEGER"
                    + ")";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading db from" + oldVersion + "to"
                    + newVersion );
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    //---insert a contact into the database---
    public long insertHighScore(int rezultat, int tezina)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_REZULTAT, rezultat);
        initialValues.put(KEY_TEZINA, tezina);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteRezultat(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---retrieves all the contacts---
    public Cursor getAllResults()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_REZULTAT, KEY_TIMESTAMP, KEY_TEZINA}, null, null, null, null, COLUMN_REZULTAT + " ASC");
    }

    //---retrieves a particular contact---
    public Cursor getResult(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_REZULTAT}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }




}
