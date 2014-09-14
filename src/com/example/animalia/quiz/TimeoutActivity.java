package com.example.animalia.quiz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.example.animalia.MainActivity;
import com.example.animalia.R;

public class TimeoutActivity extends Activity {

	public static final String NAME = "name";
	public static final String USERNAME = "username";
	public static final String POINTS = "points";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeout_layout);
	}
	
	public void openNewQuiz(View view) {
		finish();
		Intent current = getIntent();
		String module = current.getStringExtra("module");
		Intent intent = new Intent(TimeoutActivity.this, QuestionActivity.class);	
		intent.putExtra("module", module);
		startActivity(intent);
	}

	public void goToMainMenu(View view) {		
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		String username = sharedPreferences.getString("USER_NAME", "none");
		String name = sharedPreferences.getString("NAME", "none");
		int points = sharedPreferences.getInt("POINTS", 0);
		Log.e("sharedprefs", "***********************************************************************"+username);
		Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
		intent.putExtra(USERNAME, username);
		intent.putExtra(NAME, name);
		intent.putExtra(POINTS, points);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	
	}

}
