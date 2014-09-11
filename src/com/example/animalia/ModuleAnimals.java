package com.example.animalia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ModuleAnimals extends ListActivity {
	// URL to get contacts JSON
	private static String url;;

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
	ArrayList<HashMap<String, String>> animalsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.module_animals_layout);

		animalsList = new ArrayList<HashMap<String, String>>();

		Intent intent = getIntent();
		url = intent.getStringExtra("url");

		TextView tvName = (TextView) findViewById(R.id.spot_name);
		tvName.setText("Probaa");
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

		ListAdapter adapter = new SimpleAdapter(ModuleAnimals.this,
				animalsList, R.layout.list_item, new String[] { TAG_NAME },
				new int[] { R.id.name });

		setListAdapter(adapter);
	}

	private void setModuleInfo(JSONObject module) throws JSONException {
		String number = module.getString(TAG_NUMBER);
		String name = module.getString(TAG_MODULE);
		String text = module.getString(TAG_TEXT);
		String icon = module.getString(TAG_ICON);
		
		// get the elements from layout
		TextView tvName = (TextView) findViewById(R.id.module);
		TextView tvText = (TextView) findViewById(R.id.text);
		ImageView imageView = (ImageView) findViewById(R.id.icon);

		// set data
		tvName.setText(name);
		tvText.setText(text);
		new ImageLoadTask(icon, imageView).execute(null, null);
	}

	private void setSpotlight() throws JSONException {
		String id = spotlight.getString(TAG_ID);
		String name = spotlight.getString(TAG_NAME);
		String photo = spotlight.getString(TAG_PHOTO_SPOT);
		String fact = spotlight.getString(TAG_FACT_SPOT);
		String link = spotlight.getString(TAG_LINK);
		// get the elements from layout
		TextView tvName = (TextView) findViewById(R.id.spot_name);
		TextView tvFact = (TextView) findViewById(R.id.spot_fact);
		ImageView imageView = (ImageView) findViewById(R.id.spot_photo);

		// set data
		tvName.setText(name);
		tvFact.setText(fact);
		new ImageLoadTask(photo, imageView).execute(null, null);

	}

	private void setAnimals() throws JSONException {
		// looping through All Animals
		for (int i = 0; i < animals.length(); i++) {
			JSONObject a = animals.getJSONObject(i);
		
			String id = a.getString(TAG_ID);
			Log.d("caci", id+" vo setAnimals");
			String name = a.getString(TAG_NAME);
			String link = a.getString(TAG_LINK);

			// tmp hashmap for single animal
			HashMap<String, String> animal = new HashMap<String, String>();

			// adding each child node to HashMap key => value
			animal.put(TAG_ID, id);
			animal.put(TAG_NAME, name);
			animal.put(TAG_LINK, link);

			// adding animal to animal list
			animalsList.add(animal);
		}
	}

}
