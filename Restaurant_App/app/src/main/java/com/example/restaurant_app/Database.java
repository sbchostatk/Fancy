package com.example.restaurant_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    Database(Context context) {
        super(context, "login", null, 201);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE booking ("
                + "id integer,"
                + "name text,"
                + "email text,"
                + "phone text,"
                + "date text,"
                + "time text,"
                + "persons integer,"
                + "table_num integer" + ");");

        db.execSQL("create table sign_in ("
                + "id integer primary key,"
                + "first_name text,"
                + "last_name text,"
                + "gender text,"
                + "email text,"
                + "phone text,"
                + "password text" + ");");
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
