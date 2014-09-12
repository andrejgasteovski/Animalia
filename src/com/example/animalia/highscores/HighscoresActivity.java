package com.example.animalia.highscores;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.animalia.R;
import com.example.animalia.http_request.GetResponse;
import com.example.animalia.http_request.JsonTags;

public class HighscoresActivity extends FragmentActivity implements ActionBar.TabListener{
	ViewPager mViewPager;
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	
	private static final String URL_BASE = "http://hcibiology.herokuapp.com";
	private static final String URL_TOTAL = "/quizzes/highscores/Total";
	private static final String URL_EXPERT = "/quizzes/highscores/Expert";
	private static final String URL_MAMMALS = "/quizzes/highscores/Mammals";
	private static final String URL_BIRDS = "/quizzes/highscores/Birds";
	private static final String URL_REPTILES = "/quizzes/highscores/Reptiles";
	private static final String URL_AMPHIBIANS = "/quizzes/highscores/Amphibians";
	private static final String URL_ARTHROPODS = "/quizzes/highscores/Arthropods";
	
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.highscores_main);
	
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
		final ActionBar actionBar = getActionBar();
		
		actionBar.setTitle("Animalia Highscores");
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		mViewPager = (ViewPager)findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
		
		for(int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++){
			actionBar.addTab(actionBar.newTab()
					.setText(mAppSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		
	}
	
//	private void initializeListViews(){
//	
////	    LayoutInflater inflater = (LayoutInflater)   getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
////	    LinearLayout mContainer = (LinearLayout) inflater.inflate(R.layout.highscores_total, null); 
////	    
//	//	lvTotal = (ListView)mContainer.findViewById(R.id.listViewHigscoresTotal);
//		lvExpert = (ListView)findViewById(R.id.listViewHigscoresExpert);
//		lvMammals = (ListView)findViewById(R.id.listViewHigscoresMammals);
//		lvBirds = (ListView)findViewById(R.id.listViewHigscoresBirds);
//		lvReptiles = (ListView)findViewById(R.id.listViewHigscoresReptiles);
//		lvAmphibians = (ListView)findViewById(R.id.listViewHigscoresAmphibians);
//		lvArthropods = (ListView)findViewById(R.id.listViewHigscoresArthropods);
//		
//		
//		int layout=android.R.layout.simple_list_item_1;
//		
//		//ArrayList<HighscoresUser> users = parseJson(URL_BASE + URL_TOTAL);
//		//ArrayAdapter<HighscoresUser> adapterTotal = new ArrayAdapter<HighscoresUser>(HighscoresActivity.this, layout, users);
//		
////		ArrayList<String> users = new ArrayList<String>();
////		users.add("Andrej");
////		users.add("Sandra");
//		
////		ArrayAdapter<String> adapterTotal = new ArrayAdapter<String>(HighscoresActivity.this, layout, users);
////		Log.d("animalia", "Adapter initialized... users: " + users);
////		adapterTotal.notifyDataSetChanged();
////		
////		if(lvTotal == null){
////			Log.d("animalia", "list total is null");
////		} else {
////			//lvTotal.setAdapter(adapterTotal);
////			//lvTotal.setBackgroundColor(123);
////			Log.d("animalia", "list total is NOT null");
////			Log.d("animalia", "Count: " + lvTotal.getAdapter().getCount());
////		}
//	}
	

	
	@Override
	public void onTabReselected(ActionBar.Tab arg0, FragmentTransaction arg1) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab arg0, FragmentTransaction arg1) {
		mViewPager.setCurrentItem(arg0.getPosition());
	}
	
	@Override
	public void onTabUnselected(ActionBar.Tab arg0, FragmentTransaction arg1) {
	}
	
	
	public class AppSectionsPagerAdapter extends FragmentPagerAdapter{
		final int NUM_ITEMS = 7;
		
		public AppSectionsPagerAdapter(FragmentManager fm){
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
                Fragment fragment = new DummySectionFragment();
                Bundle args = new Bundle();
                args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position);
                fragment.setArguments(args);
                return fragment;
		}
		
		@Override
		public int getCount() {
			return NUM_ITEMS;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			String tabLabel = null;
			switch(position){
			case 0:
				tabLabel = getString(R.string.total);
				break;
			case 1:
				tabLabel = getString(R.string.expert);
				break;
			case 2:
				tabLabel = getString(R.string.mammals);
				break;
			case 3:
				tabLabel = getString(R.string.birds);
				break;
			case 4:
				tabLabel = getString(R.string.reptiles);
				break;
			case 5:
				tabLabel = getString(R.string.amphibians);
				break;
			case 6:
				tabLabel = getString(R.string.arthropods);
				break;
			}
			
			return tabLabel;
		}
	}
	
	public static class DummySectionFragment extends Fragment {
		public static final String ARG_SECTION_NUMBER = "section_number";
		
	       @Override
	        public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                Bundle savedInstanceState) {
	        
	            Bundle args = getArguments();
	            int position = args.getInt(ARG_SECTION_NUMBER);
	            int tabLayout = 0;
	            int listViewId = R.id.listViewHigscoresTotal;
	            String url = URL_BASE + URL_TOTAL;
	            
	            
	            
	            switch(position){
				case 0:
					tabLayout = R.layout.highscores_total;
					listViewId = R.id.listViewHigscoresTotal;
					url = URL_BASE + URL_TOTAL;
					break;
				case 1:
					tabLayout = R.layout.highscores_expert;
					listViewId = R.id.listViewHigscoresExpert;
					url = URL_BASE + URL_EXPERT;
					break;
				case 2:
					tabLayout = R.layout.highscores_mammals;
					listViewId = R.id.listViewHigscoresMammals;
					url = URL_BASE + URL_MAMMALS;
					break;
				case 3:
					tabLayout = R.layout.highscores_birds;
					listViewId = R.id.listViewHigscoresBirds;
					url = URL_BASE + URL_BIRDS;
					break;
				case 4:
					tabLayout = R.layout.highscores_reptiles;
					listViewId = R.id.listViewHigscoresReptiles;
					url = URL_BASE + URL_REPTILES;
					break;
				case 5:
					tabLayout = R.layout.highscores_amphibians;
					listViewId = R.id.listViewHigscoresAmphibians;
					url = URL_BASE + URL_AMPHIBIANS;
					break;
				case 6:
					tabLayout = R.layout.highscores_arthropods;
					listViewId = R.id.listViewHigscoresArthropods;
					url = URL_BASE + URL_ARTHROPODS;
					break;
				}
	            
	            View rootView = inflater.inflate(tabLayout, container, false);      
	            //View rootView = inflater.inflate(R.layout.highscores_total, container, false);
	            
	            ListView lv = (ListView) rootView.findViewById(listViewId);
	         
	            ArrayList<HighscoresUser> users = parseJson(url);
	    		int layout=android.R.layout.simple_list_item_1;
	    		ArrayAdapter<HighscoresUser> adapterTotal = new ArrayAdapter<HighscoresUser>(inflater.getContext(), layout, users);
	    		adapterTotal.notifyDataSetChanged();
	    		lv.setAdapter(adapterTotal);

	            return rootView;
	        }
	       
	   	private ArrayList<HighscoresUser> parseJson(String url){
			ArrayList<HighscoresUser> users = new ArrayList<HighscoresUser>();
			JSONArray usersArray = null;
			JSONObject user = null;
			
			String jsonStr = null;
			try {
				jsonStr = new GetResponse().execute(url).get();
			} catch (Exception e) {
				Log.d("animalia", e.getMessage());
			}
			
			if (jsonStr != null) {
				try {
					usersArray = new JSONArray(jsonStr);
					for(int i = 0; i < usersArray.length(); i++){
						user = usersArray.getJSONObject(i);
						int place = user.getInt(JsonTags.TAG_PLACE);
						String userName = user.getString(JsonTags.TAG_USER);
						String score = user.getString(JsonTags.TAG_SCORE);
						String type = user.getString(JsonTags.TAG_TYPE);
						
						Log.d("animalia", "User is parsed: " + place + userName + score + type);
						
						HighscoresUser hu = new HighscoresUser(place, userName, score, type);
						users.add(hu);
					}
				} catch (JSONException e) {
					Log.d("caci", e.getMessage());
				}
			} else {
				Log.e("animalia", "Couldn't get any data from the url");
			}
			return users;
		}
	       
	}
	
	
	
	
	
}
