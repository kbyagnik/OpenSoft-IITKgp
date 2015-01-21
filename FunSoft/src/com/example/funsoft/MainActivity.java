package com.example.funsoft;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.support.v7.app.ActionBarActivity;
//import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	Button download;
//	TextView status;
	EditText search;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	
//	private ProgressBar mProgress;
//  private int mProgressStatus = 0;

//    private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		download = (Button) findViewById(R.id.download_btn);
		search = (EditText) findViewById(R.id.searchBox);
//		status = (TextView) findViewById(R.id.status);
//		mProgress = (ProgressBar) findViewById(R.id.downloadBar);
		download.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.download_btn:
			
//			status.setText("Button Clicked!");
	//		mProgress.setProgress(50);
		//	startDownload();
			break;
		}
	}

	private void startDownload() {
		// TODO Auto-generated method stub
		String url = "http://cse.iitrpr.ac.in/ckn/courses/s2015/q1.pdf"; // your
																			// download
																			// url
		new DownloadFileAsync().execute(url);
	}

	class DownloadFileAsync extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		//	showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		@Override
		protected String doInBackground(String... aurl) {
			int count;

			try {

				URL url = new URL(aurl[0]);
				URLConnection conexion = url.openConnection();
				conexion.connect();

				int lenghtOfFile = conexion.getContentLength();
				Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

				File SDCardRoot = Environment.getExternalStorageDirectory();
				// create a new file, to save the downloaded file
				File file = new File(SDCardRoot, "downloaded_file.pdf");

				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(file); // save file
																	// in SD
																	// Card

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
//					mProgressStatus=(int) ((total * 100) / lenghtOfFile);
	//				mProgress.setProgress(mProgressStatus);
					//publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
			}
			return null;

		}


		@Override
		protected void onPostExecute(String unused) {
	//		dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
		}
	}

}
