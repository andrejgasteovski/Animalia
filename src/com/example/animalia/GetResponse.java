package com.example.animalia;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class GetResponse extends AsyncTask<String, Void, String> {
	 @Override
       protected void onPreExecute() {
           super.onPreExecute();
       }
	 
	 @Override
       protected String doInBackground(String... params) {
		 	String url = params[0];
		 
           // Creating service handler class instance
           ServiceHandler sh = new ServiceHandler();

           // Making a request to url and getting response
           String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

           return jsonStr;
       }
	 
	 @Override
       protected void onPostExecute(String result) {
           super.onPostExecute(result);
       }

}
