package com.example.animalia;

import java.io.IOException;

import com.facebook.*;
import com.facebook.model.*;

import android.widget.TextView;
import android.content.Intent;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

	// URL to get contacts JSON
	private static String url = "http://hcibiology.herokuapp.com/animals/aotd";

	// JSON Node names
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_LINK = "link";
	private static final String TAG_PHOTO = "photo";

	private JSONObject animalOfTheDay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		try {
			parseJson();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void parseJson() throws IOException {
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

				animalOfTheDay = new JSONObject(jsonStr);
				String id = animalOfTheDay.getString(TAG_ID);
				String name = animalOfTheDay.getString(TAG_NAME);
				String link = animalOfTheDay.getString(TAG_LINK);
				String photo = animalOfTheDay.getString(TAG_PHOTO);

				// tmp hashmap for single animal
				HashMap<String, String> animal = new HashMap<String, String>();

				// adding each child node to HashMap key => value
				animal.put(TAG_ID, id);
				animal.put(TAG_NAME, name);
				animal.put(TAG_LINK, link);
				animal.put(TAG_PHOTO, photo);

				ImageView imageView = (ImageView) findViewById(R.id.image);
				new ImageLoadTask(photo, imageView).execute(null, null);
					
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("animalia", "Couldn't get any data from the url");
		}

	}

	public void onClickLearn(View view) {
		//Intent intent=new Intent(this, ListAnimals.class);
		Intent intent=new Intent(this, ListModules.class);
		startActivity(intent);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
	
	public void loginFacebook(View view){
		Session.openActiveSession(this, true, new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if(session.isOpened()){
					Request.newMeRequest(session, new Request.GraphUserCallback() {	
						@Override
						public void onCompleted(GraphUser user, Response response) {
							TextView tv = (TextView) findViewById(R.id.welcome);
							tv.setText("Hello " + user.getName() +  "!");
						
						}
					}).executeAsync();
				}
			}
		});
	}

}
