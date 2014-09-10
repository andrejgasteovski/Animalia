package com.example.animalia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ListModules extends ListActivity {

	// URL to get contacts JSON
	private static String url = "http://hcibiology.herokuapp.com/modules";
	private static String basicURL = "http://hcibiology.herokuapp.com";

	// JSON Node names
	private static final String TAG_NUMBER = "number";
	private static final String TAG_MODULE = "module";
	private static final String TAG_TEXT = "text";
	private static final String TAG_LINK = "link";

	// contacts JSONArray
	JSONArray modules = null;

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> modulesList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_layout);

		modulesList = new ArrayList<HashMap<String, String>>();
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

				modules = new JSONArray(jsonStr);

				// looping through All modules
				for (int i = 0; i < modules.length(); i++) {
					JSONObject a = modules.getJSONObject(i);

					String number = a.getString(TAG_NUMBER);
					String module = a.getString(TAG_MODULE);
					String text = a.getString(TAG_TEXT);
					String link=a.getString(TAG_LINK);

					// tmp hashmap for single animal
					HashMap<String, String> animal = new HashMap<String, String>();

					// adding each child node to HashMap key => value
					animal.put(TAG_NUMBER, number);
					animal.put(TAG_MODULE, module);
					animal.put(TAG_TEXT, text);
					animal.put(TAG_LINK, link);
					// animal.put(TAG_PHONE_MOBILE, mobile);

					// adding animal to animal list
					modulesList.add(animal);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("animalia", "Couldn't get any data from the url");
		}

		ListAdapter adapter = new SimpleAdapter(ListModules.this,
				modulesList, R.layout.list_item, new String[] { TAG_NUMBER,
						TAG_MODULE, TAG_TEXT }, new int[] { R.id.name, R.id.link,
						R.id.id });

		setListAdapter(adapter);
	}

	

}
