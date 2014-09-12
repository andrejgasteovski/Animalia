package com.example.animalia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.animalia.http_request.GetResponse;
import com.example.animalia.learn.ModuleDetails;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ListAnimals extends ListActivity {

	private ProgressDialog pDialog;
	private ArrayAdapter<AnimalShort> adapter;
	// URL to get animals JSON
	private static String url = "http://hcibiology.herokuapp.com/animals";

	// JSON Node names
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_LINK = "link";

	// animals JSONArray
	JSONArray animals = null;

	// Hashmap for ListView
	ArrayList<AnimalShort> animalsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animals_layout);

		animalsList = new ArrayList<AnimalShort>();
		ListView lv = getListView();

		parseJson();
		setSearch();
	}
	private void setSearch(){
		EditText txt=(EditText)findViewById(R.id.inputSearch);
		txt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				ListAnimals.this.adapter.getFilter().filter(s);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
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

					AnimalShort animal=new AnimalShort(id, name, link);

					// adding animal to animal list
					animalsList.add(animal);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("animalia", "Couldn't get any data from the url");
		}

		//adapter = new SimpleAdapter(ListAnimals.this,
		//		animalsList, R.layout.list_item, new String[] { TAG_NAME }, new int[] { R.id.name});
		int layout=android.R.layout.simple_list_item_1;
		adapter = new ArrayAdapter<AnimalShort>(ListAnimals.this,layout,animalsList);
		adapter.notifyDataSetChanged();
		setListAdapter(adapter);
	}

	

}
