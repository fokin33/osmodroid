package com.OsMoDroid;

import org.json.JSONObject;

import android.os.AsyncTask;

public class CommandAsyncTask extends AsyncTask<Void, Void, JSONObject> {
	ResultsListener listener;
	
	public void setOnResultsListener(ResultsListener listener) {
        this.listener = listener;
    }
	
	
	@Override
	protected JSONObject doInBackground(Void... voids) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
    protected void onPostExecute(JSONObject result) {
       listener.onResultsSucceeded(result);

    }
	
}
