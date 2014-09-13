package com.example.animalia.quiz;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.animalia.R;
import com.example.animalia.http_request.GetResponse;

public class DisplayScoreActivity extends Activity {

	private static String url;
	public static String basicURL = "http://hcibiology.herokuapp.com/quizzes/submit?";
	// JSON Node names
	private static final String TAG_SCORE = "score";
	private static final String TAG_STARS = "stars";
	
	String score;
	int stars;
	TextView textViewScore;
	TextView textViewStars;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_score_layout);
		Intent intent = getIntent();
		//TODO
		String userName = "usrb1edeb477840d02889ff24e756dc57e1";
		String timeLeft = intent.getStringExtra("timeleft");
		String guesses = intent.getStringExtra("guesses");
		String type = intent.getStringExtra("type");
		url = basicURL + "username="+userName+"&type="+type+"&guesses="+guesses+"&timeleft="+timeLeft;
		
		textViewScore = (TextView) findViewById(R.id.textViewScore);
		textViewStars = (TextView) findViewById(R.id.textViewStars);
		parseJson();
	}

	private void parseJson() {
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
				JSONObject response = new JSONObject(jsonStr);
				score =response.getString(TAG_SCORE);
				stars = Integer.parseInt(response.getString(TAG_STARS));
				
				textViewScore.setText(score);
				textViewStars.setText("You have " + stars + " stars!");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			
		} else {
			Log.e("animalia", "Couldn't get any data from the url");
		}		
		
	}	
	
}
