package com.example.animalia.learn;
import com.example.animalia.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.animalia.ImageLoadTask;
import com.example.animalia.R;
import com.example.animalia.R.id;
import com.example.animalia.R.layout;
import com.example.animalia.http_request.GetResponse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ListModules extends Activity {

	// URL to get contacts JSON
	private static String url = "http://hcibiology.herokuapp.com/modules";
	private static String basicURL = "http://hcibiology.herokuapp.com";
	// JSON Node names
	private static final String TAG_NUMBER = "number";
	private static final String TAG_MODULE = "module";
	private static final String TAG_TEXT = "text";
	private static final String TAG_LINK = "link";
	private static final String TAG_ICON = "icon";

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
					String link = a.getString(TAG_LINK);
					String icon = a.getString(TAG_ICON);

					// tmp hashmap for single animal
					HashMap<String, String> animal = new HashMap<String, String>();
					text = text.substring(0, 60) + "...";
					// adding each child node to HashMap key => value
					animal.put(TAG_NUMBER, number);
					animal.put(TAG_MODULE, module);
					animal.put(TAG_TEXT, text);
					animal.put(TAG_LINK, link);
					animal.put(TAG_ICON, icon);

					// adding animal to animal list
					modulesList.add(animal);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("animalia", "Couldn't get any data from the url");
		}

		/*
		 * ListAdapter adapter = new SimpleAdapter(ListModules.this,
		 * modulesList, R.layout.modules_list_item, new String[] { TAG_MODULE,
		 * TAG_TEXT }, new int[] { R.id.module, R.id.text });
		 */
		// treba da se dodade za R.id.icon da ja pretstavi slikata za sekoj
		// modul
		putIntoLayout();
	}

	private void putIntoLayout() {

		HashMap<String, String> module0 = modulesList.get(0);
		ImageView imgView0 = (ImageView) findViewById(R.id.img0);
		new ImageLoadTask(module0.get(TAG_ICON), imgView0).execute(null, null);
		TextView tv0 = (TextView) findViewById(R.id.module0);
		tv0.setText(module0.get(TAG_MODULE));

		HashMap<String, String> module1 = modulesList.get(1);
		ImageView imgView1 = (ImageView) findViewById(R.id.img1);
		new ImageLoadTask(module1.get(TAG_ICON), imgView1).execute(null, null);
		TextView tv1 = (TextView) findViewById(R.id.module1);
		tv1.setText(module1.get(TAG_MODULE));

		HashMap<String, String> module2 = modulesList.get(2);
		ImageView imgView2 = (ImageView) findViewById(R.id.img2);
		new ImageLoadTask(module2.get(TAG_ICON), imgView2).execute(null, null);
		TextView tv2 = (TextView) findViewById(R.id.module2);
		tv2.setText(module2.get(TAG_MODULE));

		HashMap<String, String> module3 = modulesList.get(3);
		ImageView imgView3 = (ImageView) findViewById(R.id.img3);
		new ImageLoadTask(module3.get(TAG_ICON), imgView3).execute(null, null);
		TextView tv3 = (TextView) findViewById(R.id.module3);
		tv3.setText(module3.get(TAG_MODULE));

		HashMap<String, String> module4 = modulesList.get(4);
		ImageView imgView4 = (ImageView) findViewById(R.id.img4);
		new ImageLoadTask(module4.get(TAG_ICON), imgView4).execute(null, null);
		TextView tv4 = (TextView) findViewById(R.id.module4);
		tv4.setText(module4.get(TAG_MODULE));
	}

	public void imageClicked(View view) {
		Intent i;
		switch (view.getId()) {
		case R.id.img0:
			i = new Intent(this, ModuleDetails.class);
			i.putExtra("url", basicURL + modulesList.get(0).get(TAG_LINK));
			startActivity(i);
			break;
		case R.id.img1:
			i = new Intent(this, ModuleDetails.class);
			i.putExtra("url", basicURL + modulesList.get(1).get(TAG_LINK));
			startActivity(i);
			break;
		case R.id.img2:
			i = new Intent(this, ModuleDetails.class);
			i.putExtra("url", basicURL + modulesList.get(2).get(TAG_LINK));
			startActivity(i);
			break;
		case R.id.img3:
			i = new Intent(this, ModuleDetails.class);
			i.putExtra("url", basicURL + modulesList.get(3).get(TAG_LINK));
			startActivity(i);
			break;
		case R.id.img4:
			i = new Intent(this, ModuleDetails.class);
			i.putExtra("url", basicURL + modulesList.get(4).get(TAG_LINK));
			startActivity(i);
			break;
		}
	}
}
