package com.OsMoDroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.MessageDigest;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.OsMoDroid.R;
public class netutil {
	
	
	
	
	
	
	
	public class MyAsyncTask extends AsyncTask<Void, Void, String> {

	    ResultsListener listener;

	    public void setOnResultsListener(ResultsListener listener) {
	        this.listener = listener;
	    }

	    @Override
	    protected String doInBackground(Void... voids) {
	        String someString = "foo bar";
	        return someString.subSequence(3, 5).toString();

	    }

	    @Override
	    protected void onPostExecute(String result) {
	       listener.onResultsSucceeded(result);

	    }


	}
	
	
	
	
	

	public String getPage(String adr, boolean dopost, String post)
			throws IOException {
		// Log.d(getClass().getSimpleName(), "getpage() gpsclient");
		Log.d(getClass().getSimpleName(), adr);
		HttpURLConnection con;
		int portOfProxy = android.net.Proxy.getDefaultPort();
		if (portOfProxy > 0) {
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
					android.net.Proxy.getDefaultHost(), portOfProxy));
			con = (HttpURLConnection) new URL(adr).openConnection(proxy);
		} else {
			con = (HttpURLConnection) new URL(adr).openConnection();
		}
		con.setReadTimeout(10000);
		con.setConnectTimeout(30000);
		if (dopost) {
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			OutputStream os = con.getOutputStream();
			os.write(post.getBytes());
			Log.d(this.getClass().getName(), "Что POSTим" + post);
			os.flush();
			os.close();
		}

		con.connect();
		if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
			// String str=inputStreamToString(con.getInputStream());
			return inputStreamToString(con.getInputStream());
		} else {
			Log.d(this.getClass().getName(),
					Integer.toString(con.getResponseCode()));
			// String str=inputStreamToString(con.getInputStream());
			// Log.d(this.getClass().getName(),str);
			// return str;
		//	return getString(R.string.ErrorRecieve);
			return "Косяк";

		}

	}

	public String inputStreamToString(InputStream in) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(in));
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;

		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line + "\n");
		}

		bufferedReader.close();
		return stringBuilder.toString();
	}

	public static String bytesToHex(byte[] b) {
		char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < b.length; j++) {
			buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
			buf.append(hexDigit[b[j] & 0x0f]);
		}
		return buf.toString();
	}

	public static String SHA1(String text) {
		//Log.d(this.getClass().getName(), text);
		MessageDigest md;
		byte[] sha1hash = new byte[40];
		try {
			md = MessageDigest.getInstance("SHA-1");

			// md.update(text.getBytes());//, 0, text.length());
			sha1hash = md.digest(text.getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bytesToHex(sha1hash);
	}
	
	
}
