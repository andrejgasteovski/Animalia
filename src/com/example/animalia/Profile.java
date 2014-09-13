package com.example.animalia;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Profile extends Activity {
	private ArrayAdapter<AnimalShort> adapter;
	// URL to get user data JSON
	private static String url = "http://hcibiology.herokuapp.com/animals";

	// JSON Node names
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_USERNAME = "username";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_SURNAME = "surname";

	private String name;
	private String username;

	JSONObject user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_layout);

		initialize();
		parseJson();

	}

	private void initialize() {
		Intent intent = getIntent();
		username = intent.getStringExtra("username");
		name = intent.getStringExtra("name");
	}

	private void parseJson() {
		/*
		 * String jsonStr = null; try { jsonStr = new
		 * GetResponse().execute(url).get(); } catch (InterruptedException e) {
		 * Log.d("animalia", e.getMessage()); } catch (ExecutionException e) {
		 * Log.d("animalia", e.getMessage()); } if (jsonStr != null) { try {
		 * 
		 * user = new JSONObject(jsonStr);
		 * 
		 * } catch (JSONException e) { e.printStackTrace(); } } else {
		 * Log.e("animalia", "Couldn't get any data from the url"); }
		 */
		TextView txtName=(TextView)findViewById(R.id.name);
		TextView txtUsername=(TextView)findViewById(R.id.username);
		TextView txtSurname=(TextView)findViewById(R.id.surname);
		TextView txtEmail=(TextView)findViewById(R.id.email);
		
		txtName.setText(this.name);
		txtUsername.setText(this.username);
	}
}
