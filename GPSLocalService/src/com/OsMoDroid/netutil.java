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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.OsMoDroid.R;
public class netutil {
	
	  
	
	
	
	//public static void Close(){con.disconnect();};
	
	public static class MyAsyncTask extends AsyncTask<String, Void, APIComResult> {

	    ResultsListener listener;
	    HttpURLConnection con;
	    InputStream in;
	    public void Close(){con.disconnect();
	    try {
			in.close();
		} catch (Exception e) {
			 Log.d(this.getClass().getName(),"imtask.close exeption");
			//e.printStackTrace();
		}
	    Log.d(this.getClass().getName(),"imtask.close");};
	    String getPage(String adr, boolean dopost, String post)
				throws IOException, NullPointerException {
			// Log.d(getClass().getSimpleName(), "getpage() gpsclient");
			Log.d( GPSLocalServiceClient.class.getName(), adr);
			
		
			int portOfProxy = android.net.Proxy.getDefaultPort();
			if (portOfProxy > 0) {
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
						android.net.Proxy.getDefaultHost(), portOfProxy));
				con = (HttpURLConnection) new URL(adr).openConnection(proxy);
			} else {
				con = (HttpURLConnection) new URL(adr).openConnection();
			}
			con.setReadTimeout(300000);
			con.setConnectTimeout(30000);
			if (dopost) {
				con.setRequestMethod("POST");
				con.setDoOutput(true);
				con.setDoInput(true);
				OutputStream os = con.getOutputStream();
				os.write(post.getBytes());
//				Log.d(this.getClass().getName(), "Что POSTим" + post);
				os.flush();
				os.close();
			}

			con.connect();
			
			try {
				in = con.getInputStream();
				if (con.getResponseCode() == HttpURLConnection.HTTP_OK && !(in==null)) {
					 String str=inputStreamToString(in);
					Log.d( GPSLocalServiceClient.class.getName(), str);
					//con.disconnect();
					return str;
				} else {
//			Log.d(this.getClass().getName(),
				//			Integer.toString(con.getResponseCode()));
					// String str=inputStreamToString(con.getInputStream());
					// Log.d(this.getClass().getName(),str);
					// return str;
				//	return getString(R.string.ErrorRecieve);
				//	con.disconnect();
					Log.d( GPSLocalServiceClient.class.getName(), Integer.toString(con.getResponseCode()));
					return "Косяк";

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d( GPSLocalServiceClient.class.getName(), e.toString());
				e.printStackTrace();
				return "Косяк2";
			}

		}
	    
	    MyAsyncTask(ResultsListener listener)
	    { this.listener = listener;}
	    
	//    public void setOnResultsListener(ResultsListener listener) {
	  //      this.listener = listener;
	   // }

	    @Override
	    protected APIComResult doInBackground(String... params) {
	    	Log.d(this.getClass().getName(), "команда"+ params[3]);
	    	JSONObject resJSON = null;
	    	String Commandtext = null;
	    	APIComResult resAPI = new APIComResult();
	    	  	
	    	
	    	
	    	try {
				Commandtext = getPage(params[0],
						Boolean.parseBoolean(params[1]), params[2]);
			} catch (IOException e1) {
				Log.d(this.getClass().getName(),  "IO exp");
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
	    	try {
				
			
			Log.d(this.getClass().getName(),  Commandtext);
			resJSON = new JSONObject(Commandtext);
			resAPI.Jo= resJSON;
			Log.d(this.getClass().getName(),  resJSON.toString());
			Log.d(this.getClass().getName(),  params[3]);
			}  catch (JSONException e) {
			e.printStackTrace();}
	    	catch (NullPointerException e){
	    		e.printStackTrace();
	    		Log.d(this.getClass().getName(),  "Что-то нулл");
	    		
	    	}
	    	
			try {
				resAPI.ja = new JSONArray(Commandtext);
			} catch (JSONException e) {
				Log.d(this.getClass().getName(),  "JSON exeption in array");
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			catch (NullPointerException e){
	    		//e.printStackTrace();
	    		Log.d(this.getClass().getName(),  "Что-то нулл2");
	    		
	    	}
			
			resAPI.Command=params[3];
			
	        return resAPI;

	    }

	    @Override
	    protected void onPostExecute(APIComResult result) {
	    	Log.d(this.getClass().getName(), "void onPostExecute");
	    	Log.d(this.getClass().getName(), Boolean.toString(isCancelled()));
	    	
	    	listener.onResultsSucceeded(result);

	    }

@Override
protected void onCancelled() {
	// TODO Auto-generated method stub
	super.onCancelled();
	try {
		con.disconnect();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		Log.d(this.getClass().getName(), "onCanceled exeption in disconnect");
		
		e.printStackTrace();
	}
	Log.d(this.getClass().getName(), "void onCanceled");
	
	
}
	}
	
	
	public static String buildcommand (Context context, String action, String[] params, String[] values ){
		SharedPreferences settings  = PreferenceManager.getDefaultSharedPreferences(context);
		
		//		"http://api.esya.ru/?system=om&action=device"
//				+ "&key="
//				+ key
//				+ "&signature="
//				+ SHA1(
//						"system:om;action:device;key:"
//								+ key
//								+ ";"
//								+ "--"
//								+ "JGu473g9DFj3y_gsh463j48hdsgl34lqzkvnr420gdsg-32hafUehcDaw3516Ha-aghaerUhhvF42123na38Agqmznv_46bd-67ogpwuNaEv6")
//						.substring(1, 25), "false", "",
//		"device" };
String tempstr = ""; 
		 String apicommand = "http://api.esya.ru/?system=om&action="+action+"&key="
					+ settings.getString("key", "");
					//+ "&signature=";
		 for (int i = 0; i < params.length; i++) {
			 	 apicommand = apicommand + "&"+ params[i] + "=" + values[i];
			 	}
		 for (int i = 0; i < params.length; i++) {
		 	 tempstr = tempstr +  params[i] + ":" + values[i]+";";
		 	}
		 
		 apicommand = apicommand +  "&signature="+SHA1("system:om;action:"+action+";key:"+settings.getString("key", "")+";"+tempstr + "--"
			+ "JGu473g9DFj3y_gsh463j48hdsgl34lqzkvnr420gdsg-32hafUehcDaw3516Ha-aghaerUhhvF42123na38Agqmznv_46bd-67ogpwuNaEv6").substring(1, 25);
			//Log.v(getClass().getSimpleName(),apicommand);
		 return apicommand;}
	
	

	//public static 

	

	public static String inputStreamToString(InputStream in) throws IOException, NullPointerException {
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
