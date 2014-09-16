package com.example.animalia.quiz;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.animalia.MainActivity;
import com.example.animalia.R;
import com.example.animalia.highscores.HighscoresActivity;
import com.example.animalia.http_request.GetResponse;

public class DisplayScoreActivity extends Activity {

	private static String url;
	public static String basicURL = "http://hcibiology.herokuapp.com/quizzes/submit?";
	// JSON Node names
	private static final String TAG_SCORE = "score";
	private static final String TAG_STARS = "stars";
	public static final String NAME = "name";
	public static final String USERNAME = "username";
	public static final String POINTS = "points";

	String score;
	int stars;
	TextView textViewScore;
	TextView textViewStars;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.display_score_layout);
		Intent intent = getIntent();
		//
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		String userName = sharedPreferences.getString("USER_NAME",
				"usrb1edeb477840d02889ff24e756dc57e1");
		String timeLeft = intent.getStringExtra("timeleft");
		String guesses = intent.getStringExtra("guesses");
		String type = intent.getStringExtra("type");
		url = basicURL + "username=" + userName + "&type=" + type + "&guesses="
				+ guesses + "&timeleft=" + timeLeft;

		textViewScore = (TextView) findViewById(R.id.textViewScore);
		parseJson();
		MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.clapping);
		mp.start();
	}

	public void buttonBeep()
	{
		MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.button);
		mp.start();	
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
				score = response.getString(TAG_SCORE);
				stars = Integer.parseInt(response.getString(TAG_STARS));

				setStars();

				textViewScore.setText(score);
				Log.e("parsing", "----------------------------" + score + " "
						+ stars);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Log.e("animalia", "Couldn't get any data from the url");
		}

	}

	public void setStars() {
		if (stars == 1)
			return;
		if (stars >= 2) {
			ImageView star2 = (ImageView) findViewById(R.id.imageViewStar2);
			star2.setImageDrawable(getResources().getDrawable(
					R.drawable.star_yellow));
			if (stars == 3)
			{
				ImageView star3 = (ImageView) findViewById(R.id.imageViewStar3);
				star3.setImageDrawable(getResources().getDrawable(
						R.drawable.star_yellow));
			}
	    }
	}

	public void goToMainMenu(View view) {
		buttonBeep();
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		String username = sharedPreferences.getString("USER_NAME", "none");
		String name = sharedPreferences.getString("NAME", "none");
		int points = sharedPreferences.getInt("POINTS", 0);

		Intent intent = new Intent(this.getApplicationContext(),
				MainActivity.class);
		intent.putExtra(USERNAME, username);
		intent.putExtra(NAME, name);
		intent.putExtra(POINTS, points);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);

	}

	public void onClickHighscores(View view) {
		buttonBeep();
		Intent intent = new Intent(this, HighscoresActivity.class);
		startActivity(intent);
	}
	
	public void openNewQuiz(View view) {
		buttonBeep();
		finish();
		Intent current = getIntent();
		String module = current.getStringExtra("type");
		Intent intent = new Intent(DisplayScoreActivity.this, QuestionActivity.class);	
		intent.putExtra("module", module);
		startActivity(intent);
		
	}

}
