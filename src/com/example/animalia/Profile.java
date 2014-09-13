package com.example.animalia;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.animalia.http_request.GetResponse;

public class Profile extends Activity {
	private ArrayAdapter<AnimalShort> adapter;
	// URL to get user data JSON
	private static String url = "http://hcibiology.herokuapp.com/accounts/account/";

	// JSON Node names
	private static final String TAG_USER = "user";
	private static final String TAG_HIGHSCORE = "highscore";
	private static final String TAG_RANKING = "ranking";
	private static final String TAG_EMAIL = "email";

	private String module1;
	private String module2;
	private String module3;
	private String module4;
	private String module5;
	private String module6;
	private String module7;

	private String username;
	private String name;
	private String email;
	private String highscore;
	private JSONObject ranking;

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
	}

	private void parseJson() {

		String jsonStr = null;
		try {
			jsonStr = new GetResponse().execute(url + username).get();
		} catch (InterruptedException e) {
			Log.d("animalia", e.getMessage());
		} catch (ExecutionException e) {
			Log.d("animalia", e.getMessage());
		}
		if (jsonStr != null) {
			try {

				user = new JSONObject(jsonStr);
				name = user.getString(TAG_USER);
				email = user.getString(TAG_EMAIL);
				highscore = user.getString(TAG_HIGHSCORE);
				ranking = user.getJSONObject(TAG_RANKING);
				setModulesRanking();

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("animalia", "Couldn't get any data from the url");
		}
		setContent();
		
	}

	private void setModulesRanking() throws JSONException {

		module1 = ranking.getString("Arthropods");
		module2 = ranking.getString("Amphibians");
		module3 = ranking.getString("Reptiles");
		module4 = ranking.getString("Birds");
		module5 = ranking.getString("Mammals");
		module6 = ranking.getString("Expert");
		module7 = ranking.getString("Total");

	}
	private void setContent(){
		TextView txtName = (TextView) findViewById(R.id.name);
		TextView txtEmail = (TextView) findViewById(R.id.email);
		TextView txtHighscore = (TextView) findViewById(R.id.highscore);
		
		TextView txtRanking1 = (TextView) findViewById(R.id.ranking1);
		TextView txtRanking2 = (TextView) findViewById(R.id.ranking2);
		TextView txtRanking3 = (TextView) findViewById(R.id.ranking3);
		TextView txtRanking4 = (TextView) findViewById(R.id.ranking4);
		TextView txtRanking5 = (TextView) findViewById(R.id.ranking5);
		TextView txtRanking6 = (TextView) findViewById(R.id.ranking6);
		TextView txtRanking7 = (TextView) findViewById(R.id.ranking7);
		
		txtName.setText(name);
		txtEmail.setText(email);
		txtHighscore.setText(highscore);
		
		txtRanking1.setText(module1);
		txtRanking2.setText(module2);
		txtRanking3.setText(module3);
		txtRanking4.setText(module4);
		txtRanking5.setText(module5);
		txtRanking6.setText(module6);
		txtRanking7.setText(module7);
	}
}
