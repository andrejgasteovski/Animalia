package com.example.animalia;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.animalia.database.AccountDataSource;
import com.example.animalia.highscores.HighscoresActivity;
import com.example.animalia.http_request.GetResponse;
import com.example.animalia.learn.AnimalDetails;
import com.example.animalia.learn.ListModules;
import com.example.animalia.learn.ModuleDetails;
import com.example.animalia.quiz.ListModulesQuiz;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class MainActivity extends Activity {

	// URL to get contacts JSON
	private static String url = "http://hcibiology.herokuapp.com/animals/aotd";
	public static String URL_BASE = "http://hcibiology.herokuapp.com";

	// JSON Node names
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_LINK = "link";
	private static final String TAG_PHOTO = "photo";

	private JSONObject animalOfTheDay;
	private String animalOfTheDayLink;

	//user data
	private String username;
	private String name;
	private int points;
	
	Button btnLogin;
	TextView tvLoginStatus;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		
		try {
			parseJson();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		initializeAllShit();
		checkLoginStatus();
	}
	
	private void initializeAllShit(){
		Log.d("animalia", "Initialize all shit is called");
		
		Intent intent = getIntent();
		username = intent.getStringExtra(StartActivity.USERNAME);
		name = intent.getStringExtra(StartActivity.NAME);
		points = intent.getIntExtra(StartActivity.POINTS, 0);
		
		btnLogin = (Button) findViewById(R.id.buttonLogin);
		tvLoginStatus = (TextView) findViewById(R.id.textViewLoginStatus);
		
		Log.d("animalia", "All the shit is initialized.. Values --- Username: " + username + " -- Name: + " + name + " -- Points: " + points);
	}

	private void checkLoginStatus(){
		Log.d("animalia", "checkLoginStatus is called");
		
		if(name.equals("")){
			Log.d("animalia", "Name is empty -> user is not logged");
			btnLogin.setEnabled(true);
			tvLoginStatus.setText("User not logged");
		}else {
			Log.d("animalia", "Name is not empty -> user is logged");
			btnLogin.setEnabled(false);
			tvLoginStatus.setText("Logged as " + name);
		}
	}
	


	public void onClickLearn(View view) {
		Intent intent=new Intent(this, ListModules.class);
		startActivity(intent);
	}
	public void onClickProfile(View view) {
		Intent intent=new Intent(this, Profile.class);
		intent.putExtra("name", name);
		intent.putExtra("username", username);
		startActivity(intent);
	}
	
	public void onClickHighscores(View view){
		Intent intent = new Intent(this, HighscoresActivity.class);
		startActivity(intent);
	}
	
	public void onClickQuiz(View view) {
		// Intent intent=new Intent(this, ListAnimals.class);
		Intent intent = new Intent(this, ListModulesQuiz.class);
		startActivity(intent);
	}
	
	//Go staiv tuka privremeno dur ne se odlucime kaj kje bide searchot na zivotni
	public void onClickSearch(View view) {
		Intent intent=new Intent(this, ListAnimals.class);
		startActivity(intent);
	}
	
	public void onClickAbout(View view) {
		Intent intent=new Intent(this, About.class);
		startActivity(intent);
	}
	
	
	
	//event za klik na Learn more.. kaj Animal of the day
	public void onClickLearnMore(View view){
		Intent i=new Intent(MainActivity.this, AnimalDetails.class);
		i.putExtra("url", URL_BASE + animalOfTheDayLink);
		i.putExtra("animals", ListAnimals.getAnimalsList(ListAnimals.URL_ANIMALS));
		startActivity(i);
	}
	
	//ova se koristi za logiranjeto preku FB
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
	
	//logiranje preku FB
	public void loginFacebook(View view){
		Session.openActiveSession(this, true, new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if(session.isOpened()){
					Request.newMeRequest(session, new Request.GraphUserCallback() {	
						@Override
						public void onCompleted(GraphUser user, Response response) {
							//tvLoginStatus.setText("Hello " + user.getName() +  "!");
							name = user.getName();
							AccountDataSource dao = new AccountDataSource(getApplicationContext());
							dao.open();
							dao.updateName(name);
							checkLoginStatus();
							Log.d("animalia", "Button and TextView changed");
							
							String firstName = user.getFirstName();
							String lastName = user.getLastName();
							String email = user.getUsername();
							updateUserInServerDatabase(firstName, lastName, email);
						}
					}).executeAsync();
				}
			}
		});	
	}
	
	private void updateUserInServerDatabase(String firstName, String lastName, String emial){
		String url = "http://hcibiology.herokuapp.com/accounts/update?username=" + username + "&name=" + firstName + "&surname=" + lastName + "&email=" + emial;
		
		String jsonStr = null;
		try {
			jsonStr = new GetResponse().execute(url).get();
		} catch (Exception e) {
			Log.d("animalia", e.getMessage());
		}
		
		Log.d("animalia", "user updated");
	}
	
	//koga ke se klikne na kopceto Back na tel. da se zatvori aplikacijata
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
		super.onBackPressed();
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
					
				//used in onClickLearnMore method
				animalOfTheDayLink = link;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("animalia", "Couldn't get any data from the url");
		}

	}
}
