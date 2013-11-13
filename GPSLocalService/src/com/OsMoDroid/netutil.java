package com.OsMoDroid;

import java.io.BufferedReader;import java.io.DataOutputStream;import java.io.File;import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.MessageDigest;

import org.apache.http.util.ByteArrayBuffer;import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat.Builder;import android.text.util.Linkify;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.OsMoDroid.R;

public class netutil {
	
	  
	
	
	
	//public static void Close(){con.disconnect();};
	
	public static class MyAsyncTask extends AsyncTask<APIcomParams, Void, APIComResult> {
		
		private Context mContext;
		 ResultsListener listener;
		    HttpURLConnection con;
		    InputStream in;
		 ProgressDialog dialog;   
	    public MyAsyncTask(ResultsListener listener, Context context) {
	    	this.listener = listener;
	    	mContext = context;
	    } 
	 
	    MyAsyncTask(ResultsListener listener)
	    { this.listener = listener;}
		
	   
	    
	     

		// Dialog dial = Dialog.

		protected void onPreExecute() {
			// dialog.dismiss();
			if (!(mContext==null)){				 Log.d(this.getClass().getName(),"Dialog context="+mContext.toString());
				dialog= ProgressDialog.show(mContext,"", mContext.getString(R.string.commandpleasewait), true);
				dialog.show();}
		}
	    
	    
	    public void Close(){
	    try {
	    	
			in.close();
			con.disconnect();
		} catch (Exception e) {
			 Log.d(this.getClass().getName(),"MyAsyncTask close exeption");
			e.printStackTrace();
		}
	    Log.d(this.getClass().getName(),"MysyncClass.close");
	    };
	    
	    String getPage(String adr, String post, File uploadfile, Builder notificationBuilder, int notification )
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
			con.setReadTimeout(15000);
			con.setConnectTimeout(15000);
			if (post!=null) {
				con.setRequestMethod("POST");
				con.setDoOutput(true);
				con.setDoInput(true);
				OutputStream os = con.getOutputStream();
				os.write(post.getBytes());
				Log.d(this.getClass().getName(), "Что POSTим:" + post);
				os.flush();
				os.close();
			}			if (uploadfile!=null){				String lineEnd = "\r\n";		        String twoHyphens = "--";		        String boundary = "*****";				con.setRequestMethod("POST");				con.setDoOutput(true);				con.setDoInput(true);                con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);				OutputStream os = con.getOutputStream();				DataOutputStream dos = new DataOutputStream(os);				InputStream in = new FileInputStream(uploadfile);				dos.writeBytes(twoHyphens + boundary + lineEnd);                dos.writeBytes("Content-Disposition: form-data; name=\"track\";filename=\"" + uploadfile.getName() + "\""+ lineEnd+" Content-Type: application//gpx+xml"+ lineEnd );                dos.writeBytes(lineEnd);				byte[] buffer=new byte[512];				int bytesRead=-1;				int count = 0;				while ((bytesRead = in.read(buffer)) != -1) {										dos.write(buffer, 0, bytesRead);					count=count+buffer.length;					if (notificationBuilder!=null){						notificationBuilder.setProgress(100, (int)(count * 100 / uploadfile.length()), false);						LocalService.mNotificationManager.notify(notification, notificationBuilder.build());					}					Log.d(this.getClass().getName(), "poststreambuffer:" +new String(buffer, "UTF-8") );				    }				 dos.writeBytes(lineEnd);                 dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);                 Log.d(this.getClass().getName(), "Что upload:" + uploadfile.getName());				dos.flush();				os.flush();				os.close();				dos.close();							}						

			con.connect();
			
			try {
				in = con.getInputStream();
				if (con.getResponseCode() == HttpURLConnection.HTTP_OK && !(in==null)) {
					 String str=inputStreamToString(in);
					Log.d( GPSLocalServiceClient.class.getName(), str);
					//con.disconnect();
					return str;
				} else {
					Log.d( GPSLocalServiceClient.class.getName(), Integer.toString(con.getResponseCode()));
					return "HTTP_CODE:"+Integer.toString(con.getResponseCode());

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d( GPSLocalServiceClient.class.getName(), e.toString());
				e.printStackTrace();
				return e.toString();
			}

		}
	    
	   
	    
	//    public void setOnResultsListener(ResultsListener listener) {
	  //      this.listener = listener;
	   // }

	    @Override		protected void onProgressUpdate(Void... values) {			// TODO Auto-generated method stub			super.onProgressUpdate(values);		}		@Override
	    protected APIComResult doInBackground(APIcomParams... params) {
	    	Log.d(this.getClass().getName(), "команда:"+ params[0].command);
	    	JSONObject resJSON = null;
	    	String Commandtext = null;
	    	APIComResult resAPI = new APIComResult();
	    	  	
	    	
	    	
	    	try {
				Commandtext = getPage(params[0].action, params[0].post, params[0].uploadfile, params[0].notificationBuilder, params[0].notification);
			} catch (IOException e1) {
				Log.d(this.getClass().getName(),  "IO exp"+e1.toString());
				Commandtext=mContext.getString(R.string.noanswerfromserver);
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
	    	try {
				
			
			Log.d(this.getClass().getName(),  Commandtext);
			resJSON = new JSONObject(Commandtext);
			resAPI.Jo= resJSON;
			Log.d(this.getClass().getName(),  resJSON.toString());
			Log.d(this.getClass().getName(),  params[0].command);
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
			
			resAPI.Command=params[0].command;
			resAPI.rawresponse=Commandtext;			resAPI.url=params[0].action;			resAPI.post=params[0].post;			resAPI.notificationid=params[0].notification;
	        return resAPI;

	    }

	    @Override
	    protected void onPostExecute(APIComResult result) {
	    	if (!(mContext==null)&&!(dialog==null)){
				if (dialog.isShowing()){
					dialog.dismiss();	
				}
				}
	    	
	    	Log.d(this.getClass().getName(), "void onPostExecute:"+result.Command + " "+ result.rawresponse);
	    	Log.d(this.getClass().getName(), Boolean.toString(isCancelled()));
	    	
	    	listener.onResultsSucceeded(result);

	    }

@Override
protected void onCancelled() {
	// TODO Auto-generated method stub
	super.onCancelled();
	
	Log.d(this.getClass().getName(), "void onCanceled");
	
	
}
	}
	public static void newapicommand(ResultsListener listener, String action) {
//		SharedPreferences settings  =  PreferenceManager.getDefaultSharedPreferences( LocalService.serContext);
		
		APIcomParams params = new APIcomParams("http://apim.esya.ru/?query="+action +";&key="+OsMoDroid.settings.getString("key", ""),null,"APIM"); 			//{"http://apim.esya.ru/?query="+action +";&key="+settings.getString("key", ""),"false","","APIM"};
		new MyAsyncTask(listener).execute(params);
			
	}
	
	
	public static void newapicommand(Context context, String action) {
		//SharedPreferences settings  =  PreferenceManager.getDefaultSharedPreferences( LocalService.serContext);
				APIcomParams params = new APIcomParams("http://apim.esya.ru/?query="+action +";&key="+OsMoDroid.settings.getString("key", ""),null,"APIM"); 
		//String[] params = {"http://apim.esya.ru/?query="+action +";&key="+settings.getString("key", ""),"false","","APIM"};
		new MyAsyncTask((ResultsListener) context, context).execute(params);
			
	}
	
	
	public static void newapicommand (Context context, String action, String post )
	{
	//	SharedPreferences settings  =  PreferenceManager.getDefaultSharedPreferences( LocalService.serContext);		APIcomParams params = new APIcomParams("http://apim.esya.ru/?query="+action +";&key="+OsMoDroid.settings.getString("key", ""),post,"APIM"); 
			//String[] params = {"http://apim.esya.ru/?query="+action +"&key="+settings.getString("key", ""),"true",post,"APIM"};
			new MyAsyncTask((ResultsListener) context, context).execute(params);	
	}	public static void newapicommand (ResultsListener listener,Context context, String action, String post )	{	//	SharedPreferences settings  =  PreferenceManager.getDefaultSharedPreferences( LocalService.serContext);		APIcomParams params = new APIcomParams("http://apim.esya.ru/?query="+action +";&key="+OsMoDroid.settings.getString("key", ""),post,"APIM"); 			//String[] params = {"http://apim.esya.ru/?query="+action +"&key="+settings.getString("key", ""),"true",post,"APIM"};			new MyAsyncTask(listener, context).execute(params);		}
	
	public static void newapicommand (ResultsListener listener, String action, String post )
	{
	//	SharedPreferences settings  =  PreferenceManager.getDefaultSharedPreferences( LocalService.serContext);
			//String[] params = {"http://apim.esya.ru/?query="+action +"&key="+settings.getString("key", ""),"true",post,"APIM"};			APIcomParams params = new APIcomParams("http://apim.esya.ru/?query="+action +";&key="+OsMoDroid.settings.getString("key", ""),post,"APIM"); 
			new MyAsyncTask(listener).execute(params);	
	}
		public static void newapicommand(ResultsListener listener, Context context, String action) {		//SharedPreferences settings  =  PreferenceManager.getDefaultSharedPreferences( LocalService.serContext);		//String[] params = {"http://apim.esya.ru/?query="+action +";&key="+settings.getString("key", ""),"false","","APIM"};APIcomParams params = new APIcomParams("http://apim.esya.ru/?query="+action +";&key="+OsMoDroid.settings.getString("key", ""),null,"APIM"); 		new MyAsyncTask(listener, context).execute(params);			}		public static void newapicommand(ResultsListener listener, String action, File file, Builder notificationBuilder, int notificationid) {		//SharedPreferences settings  =  PreferenceManager.getDefaultSharedPreferences( LocalService.serContext);				//String[] params = {"http://apim.esya.ru/?query="+action +";&key="+settings.getString("key", ""),"false","","APIM"};		APIcomParams params = new APIcomParams("http://apim.esya.ru/?query="+action +";&key="+OsMoDroid.settings.getString("key", ""),null,"APIM",file, notificationBuilder, notificationid); 				new MyAsyncTask(listener).execute(params);							}
	
	
	

	//public static 

	

	public static String inputStreamToString(InputStream in) throws IOException, NullPointerException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(in));
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;

		while ((line = bufferedReader.readLine()) != null) {
			Log.d("OsMo","line="+line);
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
