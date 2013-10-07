package com.OsMoDroid;

import java.io.File;
import java.io.FilenameFilter;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class TracFileListActivity extends Activity implements ResultsListener{

	private TrackFileAdapter trackFileAdapter;
	private ArrayList<TrackFile> trackFileList = new ArrayList<TrackFile>();
	private File path;
	int count=0;
	int progress=0;
	private ProgressDialog progressDialog;
	private ReadTrackList readTask;
	
	private class ReadTrackList extends AsyncTask<Void, TrackFile, Void> {
		@Override
		protected void onProgressUpdate(TrackFile... values) {
			
				progressDialog.setProgress(progress);
				trackFileList.add(values[0]);
				Collections.sort(trackFileList);
				trackFileAdapter.notifyDataSetChanged();
			
			super.onProgressUpdate(values);
		}

		private ArrayList<TrackFile> tempTrackFileList = new ArrayList<TrackFile>();
		@Override
		protected Void doInBackground(Void... params) {
			 count=0;
			 progress=0;
			
			String sdState = android.os.Environment.getExternalStorageState();
			if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {

				 File sdDir = android.os.Environment.getExternalStorageDirectory();

				

				 path = new File (sdDir, "OsMoDroid/");

				 File[] fileArray = path.listFiles(new FilenameFilter() {
					
					@Override
					public boolean accept(File dir, String filename) {
						
						return filename.toLowerCase().endsWith(".gpx");
					}
				});
				 for (File file :fileArray){
					 //tempTrackFileList.add(new TrackFile(file.getName(),file.lastModified(),file.length()));
					 count++;
		             progress += ( (float)count / (float)fileArray.length ) * 100;
		            publishProgress(new TrackFile(file.getName(),file.lastModified(),file.length()));
		            
				 }
				 
			
			}
			
			return null;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
			Collections.sort(trackFileList);
			trackFileAdapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			trackFileList.clear();
			progressDialog.show();
			Collections.sort(trackFileList);
			trackFileAdapter.notifyDataSetChanged();
			super.onPreExecute();
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		
		
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 progressDialog = new ProgressDialog(this);
		 progressDialog.setMessage("Loading...");
		 progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		setContentView(R.layout.trackfile); 
	    final ListView lv1 = (ListView) findViewById(R.id.trackfilelistView);
	    final Button b = (Button)findViewById(R.id.refreshtrackfiles);
	    trackFileAdapter = new TrackFileAdapter(this,R.layout.trackfileitem, trackFileList);
	    lv1.setAdapter(trackFileAdapter);
	    registerForContextMenu(lv1);
	    lv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				arg0.showContextMenuForChild(arg1);
				
			}
		});
	    b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getFileList();
				
			}
		});

	}
	
	 @Override

	  public void onCreateContextMenu(ContextMenu menu, View v,

	      ContextMenuInfo menuInfo) {

	    super.onCreateContextMenu(menu, v, menuInfo);
	    menu.add(0, 1, 1, "Загрузить в ТреРа").setIcon(android.R.drawable.arrow_up_float);
	    menu.add(0, 2, 2, "Удалить").setIcon(android.R.drawable.ic_menu_delete);
	  }
	 
	 @Override

	  public boolean onContextItemSelected(MenuItem item) {

		  final AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();

		  if (item.getItemId() == 1) {
			  File sdDir = android.os.Environment.getExternalStorageDirectory();
			  File file = new File (sdDir,"OsMoDroid/"+trackFileList.get((int) acmi.id).fileName);
			  PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);
				
				NotificationCompat.Builder notificationBuilder =new NotificationCompat.Builder(

						LocalService.serContext.getApplicationContext())

				    	.setWhen(System.currentTimeMillis())

				    	.setContentText(file.getName())

				    	.setContentTitle("OsMoDroid Загрузка файла")

				    	.setSmallIcon(android.R.drawable.arrow_up_float)

				    	.setAutoCancel(true)
				    	.setContentIntent(contentIntent)
				    	.setProgress(100, 0, false);
				    	;


					Notification notification = notificationBuilder.build();
					int uploadid = OsMoDroid.uploadnotifyid();

					LocalService.mNotificationManager.notify(uploadid, notification);
				
				
				netutil.newapicommand((ResultsListener)TracFileListActivity.this,  "tr_track_upload:1", file,notificationBuilder,uploadid);
			  
		  }
		  
		  if (item.getItemId() == 2){
			  File sdDir = android.os.Environment.getExternalStorageDirectory();
			  File file = new File (sdDir,"OsMoDroid/"+trackFileList.get((int) acmi.id).fileName);
			  file.delete();
			  getFileList();
		  }
		return true;
	 }

	
	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		 getFileList();
		super.onResume();
	}

	void getFileList(){
		if (readTask!=null){
			readTask.cancel(true);
		}
		readTask=new ReadTrackList();
		readTask.execute();
	}

	@Override
	public void onResultsSucceeded(APIComResult result) {
		LocalService.mNotificationManager.cancel(result.notificationid);
	}

	

}
