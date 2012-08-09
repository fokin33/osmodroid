package com.OsMoDroid;

import android.os.AsyncTask;

public class CommandAsyncTask extends AsyncTask<Void, Void, String> {
	ResultsListener listener;
	
	public void setOnResultsListener(ResultsListener listener) {
        this.listener = listener;
    }
	
	
	@Override
	protected String doInBackground(Void... voids) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
    protected void onPostExecute(String result) {
       listener.onResultsSucceeded(result);

    }
	
}
