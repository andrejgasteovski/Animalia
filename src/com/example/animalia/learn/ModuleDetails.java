package com.example.animalia.learn;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.animalia.AnimalShort;
import com.example.animalia.ImageLoadTask;
import com.example.animalia.ListAnimals;
import com.example.animalia.MainActivity;
import com.example.animalia.R;
import com.example.animalia.http_request.GetResponse;

public class ModuleDetails extends Activity {
	// URL to get contacts JSON
	private String urlModule;
	public static String basicURL = "http://hcibiology.herokuapp.com";
	// JSON Node names
	private static final String TAG_NUMBER = "number";
	private static final String TAG_MODULE = "module";
	private static final String TAG_TEXT = "text";
	private static final String TAG_ICON = "icon";
	private static final String TAG_SPOT = "spotlight";
	private static final String TAG_ANIMALS = "animals";

	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_LINK = "link";
	private static final String TAG_PHOTO_SPOT = "photo";
	private static final String TAG_FACT_SPOT = "fact";

	// animals JSONArray
	JSONArray animals = null;

	// spotlight animal JSONObject
	JSONObject spotlight = null;

	// Hashmap for ListView
	ArrayList<AnimalShort> animalsList;
	
	private String spotlightLink;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.module_animals_layout);

		animalsList = new ArrayList<AnimalShort>();

		Intent intent = getIntent();
		urlModule = intent.getStringExtra("url");

		parseJson();
		// initializeList();
	}

	public void buttonBeep() {
		MediaPlayer mp = MediaPlayer.create(getApplicationContext(),
				R.raw.button);
		mp.start();
	}

	/*
	 * private void initializeList() { getListView().setClickable(true);
	 * getListView().setOnItemClickListener( new
	 * AdapterView.OnItemClickListener() {
	 * 
	 * @Override public void onItemClick(AdapterView<?> parent, View view, int
	 * position, long id) { AnimalShort animal = (AnimalShort) getListView()
	 * .getItemAtPosition(position); Intent i = new Intent(ModuleDetails.this,
	 * AnimalDetails.class); i.putExtra("url", basicURL + animal.getLink());
	 * i.putExtra("animals", animalsList); startActivity(i); } }); }
	 */

	private void parseJson() {
		String jsonStr = null;
		try {
			jsonStr = new GetResponse().execute(urlModule).get();
		} catch (InterruptedException e) {
			Log.d("animalia", e.getMessage());
		} catch (ExecutionException e) {
			Log.d("animalia", e.getMessage());
		}
		if (jsonStr != null) {
			try {
				JSONObject module = new JSONObject(jsonStr);

				spotlight = module.getJSONObject(TAG_SPOT);
				animals = module.getJSONArray(TAG_ANIMALS);

				setModuleInfo(module);
				setSpotlight();
				setAnimals();

			} catch (JSONException e) {
				Log.d("caci", e.getMessage());
			}
		} else {
			Log.e("animalia", "Couldn't get any data from the url");
		}
		/*
		 * int layout = android.R.layout.simple_list_item_1;
		 * ArrayAdapter<AnimalShort> adapter = new ArrayAdapter<AnimalShort>(
		 * ModuleDetails.this, layout, animalsList);
		 * adapter.notifyDataSetChanged(); setListAdapter(adapter);
		 */
	}

	private void setModuleInfo(JSONObject module) throws JSONException {
		String number = module.getString(TAG_NUMBER);
		String name = module.getString(TAG_MODULE);
		String text = module.getString(TAG_TEXT);
		String icon = module.getString(TAG_ICON);

		// get the elements from layout

		TextView tvText = (TextView) findViewById(R.id.text);
		// ImageView imageView = (ImageView) findViewById(R.id.icon);

		// set data
		setTitle(name);
		tvText.setText(text);
		// new ImageLoadTask(icon, imageView).execute(null, null);
	}

	private void setTitle(String name) {
		Log.d("caci", "Title: " + name);
		ImageView imgName = (ImageView) findViewById(R.id.module);
		if (name.equals("Amphibians"))
			imgName.setImageDrawable(getResources().getDrawable(
					R.drawable.amphibians));
		else if (name.equals("Arthropods"))
			imgName.setImageDrawable(getResources().getDrawable(
					R.drawable.arthropods));
		else if (name.equals("Birds"))
			imgName.setImageDrawable(getResources().getDrawable(
					R.drawable.birds));
		else if (name.equals("Mammals"))
			imgName.setImageDrawable(getResources().getDrawable(
					R.drawable.mammals));
		else if (name.equals("Reptiles"))
			imgName.setImageDrawable(getResources().getDrawable(
					R.drawable.reptiles));
	}

	private void setSpotlight() throws JSONException {
		String id = spotlight.getString(TAG_ID);
		String name = spotlight.getString(TAG_NAME);
		String photo = spotlight.getString(TAG_PHOTO_SPOT);
		String fact = spotlight.getString(TAG_FACT_SPOT);
		spotlightLink = spotlight.getString(TAG_LINK);
		// get the elements from layout
		Button btnName = (Button) findViewById(R.id.spot_name);
		TextView tvFact = (TextView) findViewById(R.id.spot_fact);
		ImageView imageView = (ImageView) findViewById(R.id.spot_photo);

		// set data
		btnName.setText(name);
		tvFact.setText(fact);
		new ImageLoadTask(photo, imageView).execute(null, null);

	}

	private void setAnimals() throws JSONException {
		// looping through All Animals
		for (int i = 0; i < animals.length(); i++) {
			JSONObject a = animals.getJSONObject(i);

			String id = a.getString(TAG_ID);
			String name = a.getString(TAG_NAME);
			String link = a.getString(TAG_LINK);

			AnimalShort animal = new AnimalShort(id, name, link);

			// adding animal to animal list
			animalsList.add(animal);
		}
	}

	public void onClickViewAnimals(View view) {
		buttonBeep();
		Intent intent = new Intent(this, ListAnimals.class);
		intent.putExtra("url", urlModule + "/animals");
		Log.d("animalia", "URL passed to ListAnimals: " + "/animals");
		startActivity(intent);
	}
	
	public void onClickSpotlightAnimal(View view) 
	{
		buttonBeep();
	}
	
	public void onClickSpotlightAnimalName(View view){
		Intent i = new Intent(ModuleDetails.this, AnimalDetails.class);
		i.putExtra("url", MainActivity.URL_BASE + spotlightLink);
		i.putExtra("animals",
				ListAnimals.getAnimalsList(urlModule + "/animals"));
		startActivity(i);
	}

}
