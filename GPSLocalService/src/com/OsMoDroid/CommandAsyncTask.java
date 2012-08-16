package com.OsMoDroid;

import org.json.JSONObject;

import android.os.AsyncTask;

public class CommandAsyncTask extends AsyncTask<Void, Void, APIComResult> {
	ResultsListener listener;
	
	public void setOnResultsListener(ResultsListener listener) {
        this.listener = listener;
    }
	
	
	@Override
	protected APIComResult doInBackground(Void... voids) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
    protected void onPostExecute(APIComResult result) {
       listener.onResultsSucceeded(result);

    }
	
}
