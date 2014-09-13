package com.example.animalia.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AccountDataSource {
	private SQLiteDatabase database;
	private SQLiteOpenHelper dbHelper;
	private String [] allColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_USERNAME, MySQLiteHelper.COLUMN_POINTS};
	
	public AccountDataSource(Context context){
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public Account cursorToAccount(Cursor cursor){
		Account account = new Account();
		account.setId(cursor.getLong(0));
		account.setName(cursor.getString(1));
		account.setUsername(cursor.getString(2));
		account.setPoints(cursor.getInt(3));
		return account;
	}
	
	//Shuold be called when the application is started for the first time
	//username is the generic username form the http request
	public boolean createAccount(String username){
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_USERNAME, username);
		values.put(MySQLiteHelper.COLUMN_POINTS, 0);
		values.put(MySQLiteHelper.COLUMN_NAME, "");
		
		long insertId = database.insert(MySQLiteHelper.TABLE_ACCOUNTS, null, values);
		
		if(insertId != -1){
			return true;
		}
		
		return false;
	}
	
	//should be called when the quiz is finished.. 
	//this will update the points of all the accounts in the database (should have only one account)
	
	public boolean updatePoints(int points){
		//database.execSQL("UPDATE " + MySQLiteHelper.TABLE_ACCOUNTS + " SET " + MySQLiteHelper.COLUMN_POINTS + " = " + points);
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_POINTS, points);
		int rows = database.update(MySQLiteHelper.TABLE_ACCOUNTS, values, null, null);
		
		if(rows > 0){
			return true;
		}
		return false;
	}
	
	
	//should be called when the user log with facebook
	public boolean updateName(String name){
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_NAME, name);
		int rows = database.update(MySQLiteHelper.TABLE_ACCOUNTS, values, null, null);
		
		if(rows > 0){
			return true;
		}
		return false;
	}
	
	public List<Account> getAllAccounts(){
		List<Account> accounts = new ArrayList<Account>();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_ACCOUNTS, allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()){
			Account account = cursorToAccount(cursor);
			accounts.add(account);
			cursor.moveToNext();
		}
		
		cursor.close();
		return accounts;
	}
	
}
