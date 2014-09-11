package com.example.animalia.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper{

	public static final String TABLE_ACCOUNTS = "accounts";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME= "name";
	public static final String COLUMN_USERNAME= "username";
	public static final String COLUMN_POINTS= "points";
	
	public static final String DATABASE_NAME = "accounts.db";
	public static final int DATABASE_VERSION = 1;
	
	public static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_ACCOUNTS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT NOT NULL, " + COLUMN_USERNAME + " TEXT NOT NULL, " + COLUMN_POINTS + " INTEGER)";
	
	public MySQLiteHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
		onCreate(db);
	}
	
}
