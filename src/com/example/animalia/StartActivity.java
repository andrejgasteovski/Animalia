package com.example.animalia;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.animalia.database.Account;
import com.example.animalia.database.AccountDataSource;
import com.example.animalia.http_request.GetResponse;

public class StartActivity extends Activity{
	
	// URL to get contacts JSON
	private static String url = "http://hcibiology.herokuapp.com/accounts/start";
	
	// JSON Node names
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_USERNAME = "username";
	
	//Intent extra tags
	public static final String NAME = "name";
	public static final String USERNAME = "username";
	public static final String POINTS = "points";

	//User state tags
	private static final int USER_FIRST_TIME = 1;
	private static final int USER_NOT_LOGGED = 2;
	private static final int USER_LOGGED = 3;
	
	private String username = "";
	private String message = "";
	private String name = "";
	private int points = 0;
	
	AccountDataSource dao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		dao = new AccountDataSource(getApplicationContext());
		dao.open();
		Log.d("animalia", "DAO Database is open");
		
		int state = getUserState();
		Log.d("animalia", "User stete is returned -- State: " + state);
		
		if(state == USER_FIRST_TIME){
			setContentView(R.layout.start_activity);
			parseJson();
			Log.d("animalia", "Json is parsd --- username: " + username + " -- message: " + message);
		} else{
			startMainActivity();
		}
	}
	
	private int getUserState(){
		Log.d("animalia", "Get user state is called");
		
		List<Account> accounts = dao.getAllAccounts();
		Log.d("animalia", "Accounts initialized");
		
		if(accounts.size() <= 0){
			Log.d("animalia", "The application is started for the first time");
			return USER_FIRST_TIME;
		}
		
		Account account = accounts.get(0);
		username = account.getUsername();
		name = account.getName();
		points = account.getPoints();
		
		Log.d("animalia", "Account already exist -- Username: " + username + " -- Name: + " + name + " -- Points: " + points);
		
		if(name.equals("")){
			Log.d("animalia", "User is not logged");
			return USER_NOT_LOGGED;
		}
		
		Log.d("animalia", "User is logged");
		return USER_LOGGED;
	}
	
	public void startMainActivity(View view){
		boolean isSuccess = dao.createAccount(username);
		Log.d("animalia", "Is account created? -> " + isSuccess);
		
		startMainActivity();
	}
	
	private void startMainActivity(){
		Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
		intent.putExtra(USERNAME, username);
		intent.putExtra(NAME, name);
		intent.putExtra(POINTS, points);
		startActivity(intent);
		Log.d("animalia", "Main activity is started with following extras: Username: " + username + " -- Name: " + name + " -- Points: " + points);
	}
	
	private void parseJson(){
		String jsonStr = null;
		try {
			jsonStr = new GetResponse().execute(url).get();
		} catch (InterruptedException e) {
			Log.d("animalia", e.getMessage());
		} catch (ExecutionException e) {
			Log.d("animalia", e.getMessage());
		}

		if (jsonStr != null) {
			try {
				JSONObject user = new JSONObject(jsonStr);
				username = user.getString(TAG_USERNAME);
				message = user.getString(TAG_MESSAGE);
				} catch (JSONException e) {
					Log.d("animalia", e.getMessage());
				}
		} else {
			Log.e("animalia", "Couldn't get any data from the url");
		}
	}
}
