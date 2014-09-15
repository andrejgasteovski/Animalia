package com.example.animalia.quiz;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animalia.R;
import com.example.animalia.http_request.GetResponse;

public class QuestionActivity extends Activity {

	private CountDownTimer countDownTimer;
	private final long startTime = 30 * 1000;
	private final long interval = 1 * 1000;

	// URL to get contacts JSON
	private static String url;
	public static String basicURL = "http://hcibiology.herokuapp.com/quizzes/quiz/";
	// JSON Node names
	private static final String TAG_NUMBER = "number";
	private static final String TAG_QUESTION = "question";
	private static final String TAG_OPTIONS = "options";
	private static final String TAG_ANSWER = "answer";
	JSONObject question = null;
	ArrayList<Question> questionsArray;
	String module;

	int questionNumber;
	int guesses;
	TextView textViewQuestionNumber;
	TextView textViewQuestion;
	TextView textViewTime;
	Button option1;
	Button option2;
	Button option3;
	Button option4;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_layout);
		questionsArray = new ArrayList<Question>();
		Intent intent = getIntent();
		module = intent.getStringExtra("module");
		url = basicURL + module;
		parseJson();

		guesses = 0;
		questionNumber = 0;
		textViewQuestionNumber = (TextView) findViewById(R.id.textViewQuestionNumber);
		textViewQuestion = (TextView) findViewById(R.id.textViewQuestion);
		textViewTime = (TextView) findViewById(R.id.textViewTime);
		option1 = (Button) findViewById(R.id.option1);
		option2 = (Button) findViewById(R.id.option2);
		option3 = (Button) findViewById(R.id.option3);
		option4 = (Button) findViewById(R.id.option4);

		setTitle(module);
		fillData();
		countDownTimer = new MyCountDownTimer(startTime, interval);
		countDownTimer.start();
		intent = new Intent(this, DisplayScoreActivity.class);
	}

	private void setTitle(String name) {
		Log.d("caci", "Title: " + name);
		ImageView imgName = (ImageView) findViewById(R.id.imageViewModuleName);
		if (name.equals("Amphibians"))
			imgName.setImageDrawable(getResources().getDrawable(
					R.drawable.amphibiansquiz));
		else if (name.equals("Arthropods"))
			imgName.setImageDrawable(getResources().getDrawable(
					R.drawable.arthropodsquiz));
		else if (name.equals("Birds"))
			imgName.setImageDrawable(getResources().getDrawable(
					R.drawable.birdsquiz));
		else if (name.equals("Mammals"))
			imgName.setImageDrawable(getResources().getDrawable(
					R.drawable.mammalsquiz));
		else if (name.equals("Reptiles"))
			imgName.setImageDrawable(getResources().getDrawable(
					R.drawable.reptilesquiz));
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
			JSONArray response;
			try {
				response = new JSONArray(jsonStr);
				for (int i = 0; i < 5; i++) {
					question = response.getJSONObject(i);
					int number = Integer.parseInt(question
							.getString(TAG_NUMBER));
					String questionText = question.getString(TAG_QUESTION);
					int answer = Integer.parseInt(question
							.getString(TAG_ANSWER));
					JSONObject options = question.getJSONObject(TAG_OPTIONS);
					ArrayList<String> optionsArray = new ArrayList<String>();
					for (int j = 0; j < 4; j++) {
						String option = options.getString(Integer
								.toString(j + 1));
						optionsArray.add(option);
					}
					Question q = new Question(number, questionText,
							optionsArray, answer);
					questionsArray.add(q);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			Log.e("animalia", questionsArray.size() + "");
		} else {
			Log.e("animalia", "Couldn't get any data from the url");
		}
		//
	}

	// TODO Check if it's necessary to pass questionNumber
	private void fillData() {
		textViewQuestionNumber.setText("Question " + (questionNumber + 1)
				+ "/5: ");
		textViewQuestion.setText(questionsArray.get(questionNumber)
				.getQuestion());
		ArrayList<String> opts = questionsArray.get(questionNumber)
				.getAnswers();
		option1.setText(opts.get(0));
		option2.setText(opts.get(1));
		option3.setText(opts.get(2));
		option4.setText(opts.get(3));
	}

	public void chooseAnswer(View view) {
		int selectedOptionNumber = Integer.parseInt(view.getTag().toString());
		Question current = questionsArray.get(questionNumber);
		final Toast toast;
		LayoutInflater inflater = getLayoutInflater();
		View toast_image;

		if (current.getAnswer() == selectedOptionNumber) {
			toast = Toast.makeText(QuestionActivity.this, "RIGHT",
					Toast.LENGTH_SHORT);

			toast_image = inflater.inflate(R.layout.toast_right,
					(ViewGroup) findViewById(R.id.toast_right));
			guesses++;
		} else {
			toast = Toast.makeText(QuestionActivity.this, "WRONG",
					Toast.LENGTH_SHORT);
			toast_image = inflater.inflate(R.layout.toast_wrong,
					(ViewGroup) findViewById(R.id.toast_wrong));

		}
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(toast_image);
		toast.show();

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				toast.cancel();
				if (questionNumber < 4) {
					questionNumber++;
					fillData();
				} else {
					countDownTimer.cancel();

					intent.putExtra("guesses", guesses);
					intent.putExtra("type", module);
					intent.putExtra("timeleft", textViewTime.getText());
					finish();
					startActivity(intent);
				}
			}
		}, 500);

		// TODO make the time stop

	}

	public class MyCountDownTimer extends CountDownTimer {
		public MyCountDownTimer(long startTime, long interval) {
			super(startTime, interval);
		}

		@Override
		public void onFinish() {
			Intent intent = new Intent(QuestionActivity.this,
					TimeoutActivity.class);
			intent.putExtra("module", module);
			finish();
			startActivity(intent);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			textViewTime.setText("" + millisUntilFinished / 1000);
		}

	}

}
