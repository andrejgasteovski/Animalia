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
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ListAnimals extends ListActivity {

	private ProgressDialog pDialog;

	// URL to get contacts JSON
	private static String url = "http://hcibiology.herokuapp.com/animals";

	// JSON Node names
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_LINK = "link";

	// contacts JSONArray
	JSONArray animals = null;

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> animalsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animals_layout);

		animalsList = new ArrayList<HashMap<String, String>>();
		ListView lv = getListView();

		// new GetContacts().execute();
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

				animals = new JSONArray(jsonStr);

				// looping through All Animals
				for (int i = 0; i < animals.length(); i++) {
					JSONObject a = animals.getJSONObject(i);

					String id = a.getString(TAG_ID);
					String name = a.getString(TAG_NAME);
					String link = a.getString(TAG_LINK);

					// tmp hashmap for single animal
					HashMap<String, String> animal = new HashMap<String, String>();

					// adding each child node to HashMap key => value
					animal.put(TAG_ID, id);
					animal.put(TAG_NAME, name);
					animal.put(TAG_LINK, link);
					// animal.put(TAG_PHONE_MOBILE, mobile);

					// adding animal to animal list
					animalsList.add(animal);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("animalia", "Couldn't get any data from the url");
		}

		ListAdapter adapter = new SimpleAdapter(ListAnimals.this,
				animalsList, R.layout.list_item, new String[] { TAG_NAME,
						TAG_LINK, TAG_ID }, new int[] { R.id.name, R.id.link,
						R.id.id });

		setListAdapter(adapter);
	}

	

}
