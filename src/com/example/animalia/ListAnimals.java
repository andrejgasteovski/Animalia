package com.example.animalia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.example.animalia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.animalia.http_request.GetResponse;
import com.example.animalia.learn.AnimalDetails;
import com.example.animalia.learn.ModuleDetails;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

	// Hashmap for ListView
	ArrayList<AnimalShort> animals;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animals_layout);

		animals = getAnimalsList();
		
		ListView lv = getListView();
		setListAdapter();

		setSearch();	
		initializeList();
	}
	
	private void initializeList(){
		getListView().setClickable(true);
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AnimalShort animal=(AnimalShort)getListView().getItemAtPosition(position);
				Intent i=new Intent(ListAnimals.this, AnimalDetails.class);
				i.putExtra("url", MainActivity.URL_BASE + animal.getLink());
				i.putExtra("animals", animals);
				startActivity(i);
			}
		});
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
	
	
	//isto so parseJson() ...
	//napraena e static zatoa sto se koristi vo MainActivity
	public static ArrayList<AnimalShort> getAnimalsList() {
		ArrayList<AnimalShort> animalsList = new ArrayList<AnimalShort>();
		JSONArray animals = null;
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
		
		return animalsList;
	}

	private void setListAdapter(){
		int layout=android.R.layout.simple_list_item_1;
		adapter = new ArrayAdapter<AnimalShort>(ListAnimals.this,layout,animals);
		adapter.notifyDataSetChanged();
		setListAdapter(adapter);
	}

}
