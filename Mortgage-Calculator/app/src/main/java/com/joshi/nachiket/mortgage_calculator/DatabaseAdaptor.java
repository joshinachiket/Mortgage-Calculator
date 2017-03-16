package com.joshi.nachiket.mortgage_calculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by NACHIKET on 3/14/2017.
 */

public class DatabaseAdaptor {

    Database dbHelper;

    public DatabaseAdaptor(Context context) {
        dbHelper = new Database(context);
    }
    public long insertData (String city_name, String pin_code) {
        SQLiteDatabase db =  dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.CITY, city_name);
        contentValues.put(Database.PID_CODE, pin_code);
        //this will return row id if correct else negative if something goes wrong
        long id = db.insert(Database.TABLE_NAME, null, contentValues);
        return id;
    }

    static class Database extends SQLiteOpenHelper {
        public static int VERSION_COUTER = 30;
        public static final String DATABASE_NAME = "location_database";
        public static final String TABLE_NAME = "LOCATIONS";
        public static final int DATABASE_VERSION = VERSION_COUTER++;

        public static final String CITY = "CITY";
        public static final String _RID = "_RID";
        public static final String PID_CODE = "PID_CODE";

        //create table query
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + _RID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + CITY + " VARCHAR(255), " + PID_CODE + " VARCHAR(255));";
        //drop table query
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

        private Context context;

        public Database(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
            Message.message(context, "CONSTRUCTOR WAS CALLED");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
                Message.message(context, "onCreate WAS CALLED");
            } catch (SQLException e) {
                //e.printStackTrace();
                Message.message(context, " " + e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Message.message(context, "onUpgrade WAS CALLED");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (SQLException e) {
                //e.printStackTrace();
                Message.message(context, " " + e);
            }
        }
    }
}
