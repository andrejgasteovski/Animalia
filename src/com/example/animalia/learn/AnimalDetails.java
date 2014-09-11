package com.example.animalia.learn;

import com.example.animalia.R;

import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
public class AnimalDetails extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animal_details_layout);
		Log.d("caci", getIntent().getStringExtra("url"));
	}
}
