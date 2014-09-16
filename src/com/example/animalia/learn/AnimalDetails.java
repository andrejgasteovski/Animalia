package com.example.animalia.learn;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.animalia.AnimalShort;
import com.example.animalia.ImageLoadTask;
import com.example.animalia.R;
import com.example.animalia.http_request.GetResponse;
import com.example.animalia.http_request.JsonTags;

public class AnimalDetails extends Activity {

	TextView tvName;
	TextView tvNameFront;
	TextView tvArea;
	TextView tvHabitat;
	TextView tvFood;
	TextView tvSize;
	TextView tvBabies;
	TextView tvFact;
	TextView tvText;
	ImageView ivPhoto;

	Button btnNextAnimal;
	Button btnPreviousAnimal;
	Button btnBackToModule;

	private String url;
	ArrayList<AnimalShort> animalsList;
	String animalId;

	AnimalShort nextAnimal = null;
	AnimalShort previousAnimal = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animal_details_layout);
		Log.d("animalia", "URL = " + getIntent().getStringExtra("url"));

		url = getIntent().getStringExtra("url");
		initializeAllViews();
		parseJson();
		Log.d("animalia", "JSON is parsed.. Text is set...");

		animalsList = (ArrayList<AnimalShort>) getIntent()
				.getSerializableExtra("animals");
		Log.d("animalia",
				"Animals list is taken from previous activity .. Size: "
						+ animalsList.size());

		initializePreviousAndNextAnimals();
		Log.d("animalia",
				"Previous and next animals initialized.. Previous: "
						+ previousAnimal.getName() + " --- Next: "
						+ nextAnimal.getName());

		setFont();
	}

	public void buttonBeep()
	{
		MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.button);
		mp.start();	
	}	
	
	private void initializePreviousAndNextAnimals() {
		for (AnimalShort animal : animalsList) {
			if (animal.getId().equals(this.animalId)) {
				int indexInList = animalsList.indexOf(animal);
				int indexNextAnimal = (indexInList + 1) % animalsList.size();
				int indexPreviousAnimal = indexInList - 1;

				if (indexInList == 0) {
					indexPreviousAnimal = animalsList.size() - 1;
				}

				nextAnimal = animalsList.get(indexNextAnimal);
				previousAnimal = animalsList.get(indexPreviousAnimal);
			}
		}
	}

	public void buttonOnClick(View view) {
		buttonBeep();
		String urlToOpen = ModuleDetails.basicURL;

		if (view.getId() == R.id.buttonNextAnimal) {
			urlToOpen += nextAnimal.getLink();
		} else if (view.getId() == R.id.buttonPreviousAnimal) {
			urlToOpen += previousAnimal.getLink();
		}

		Intent i = new Intent(AnimalDetails.this, AnimalDetails.class);
		i.putExtra("url", urlToOpen);
		i.putExtra("animals", animalsList);
		startActivity(i);
		this.finish();
	}

	public void backButtonOnClick(View view) {
		this.onBackPressed();
	}

	private void parseJson() {
		String jsonStr = null;
		try {
			jsonStr = new GetResponse().execute(url).get();
		} catch (Exception e) {
			Log.d("animalia", e.getMessage());
		}

		if (jsonStr != null) {
			try {
				JSONObject animal = new JSONObject(jsonStr);
				animalId = animal.getString(JsonTags.TAG_ID);
				tvName.setText(animal.getString(JsonTags.TAG_NAME));
				tvNameFront.setText(animal.getString(JsonTags.TAG_NAME));
				tvArea.setText(animal.getString(JsonTags.TAG_AREA));
				tvHabitat.setText(animal.getString(JsonTags.TAG_HABITAT));
				tvFood.setText(animal.getString(JsonTags.TAG_FOOD));
				tvSize.setText(animal.getString(JsonTags.TAG_SIZE));
				tvBabies.setText(animal.getString(JsonTags.TAG_BABIES));
				tvFact.setText(animal.getString(JsonTags.TAG_FACT));
				tvText.setText(animal.getString(JsonTags.TAG_TEXT));

				String photo = animal.getString(JsonTags.TAG_PHOTO);
				new ImageLoadTask(photo, ivPhoto).execute(null, null);

			} catch (JSONException e) {
				Log.d("caci", e.getMessage());
			}
		} else {
			Log.e("animalia", "Couldn't get any data from the url");
		}
	}

	private void initializeAllViews() {
		tvName = (TextView) findViewById(R.id.textViewName);
		tvNameFront = (TextView) findViewById(R.id.textViewNameFront);
		tvArea = (TextView) findViewById(R.id.textViewArea);
		tvHabitat = (TextView) findViewById(R.id.textViewHabitat);
		tvFood = (TextView) findViewById(R.id.textViewFood);
		tvSize = (TextView) findViewById(R.id.textViewSize);
		tvBabies = (TextView) findViewById(R.id.textViewBabies);
		tvFact = (TextView) findViewById(R.id.textViewFact);
		tvText = (TextView) findViewById(R.id.textViewText);
		ivPhoto = (ImageView) findViewById(R.id.imageViewAnimalPhoto);

		btnNextAnimal = (Button) findViewById(R.id.buttonNextAnimal);
		btnPreviousAnimal = (Button) findViewById(R.id.buttonPreviousAnimal);
		btnBackToModule = (Button) findViewById(R.id.buttonBackToModule);

		Log.d("animalia", "All views initialized");
	}

	private void setFont() {
		Typeface tf = Typeface
				.createFromAsset(getAssets(), "fonts/cookies.ttf");
		tvName.setTypeface(tf);
		tvNameFront.setTypeface(tf);
		
		//set titles
		TextView tvArea = (TextView) findViewById(R.id.title_textViewArea);
		TextView tvHabitat = (TextView) findViewById(R.id.title_textViewHabitat);
		TextView tvFood = (TextView) findViewById(R.id.title_textViewFood);
		TextView tvSize = (TextView) findViewById(R.id.title_textViewSize);
		TextView tvBabies = (TextView) findViewById(R.id.title_textViewBabies);
		TextView tvFact = (TextView) findViewById(R.id.title_textViewFact);
		TextView tvText = (TextView) findViewById(R.id.title_textViewMore);
		
		tvArea.setTypeface(tf);
		tvHabitat.setTypeface(tf);
		tvFood.setTypeface(tf);
		tvSize.setTypeface(tf);
		tvBabies.setTypeface(tf);
		tvFact.setTypeface(tf);
		tvText.setTypeface(tf);
	}
	

}
