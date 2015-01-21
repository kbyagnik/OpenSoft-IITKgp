package com.example.funsoft;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	Button download;
	// TextView status;
	EditText search;
	ListView results;
	ArrayList<String> resultList = new ArrayList<String>();
	ArrayAdapter<String> resultAdapter;

	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

	String query = "http://10.20.254.55/opensoft/server.php";
	List<NameValuePair> nameValuePairs;
	HttpResponse response;

	// private ProgressBar mProgress;
	// private int mProgressStatus = 0;

	// private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		download = (Button) findViewById(R.id.download_btn);
		search = (EditText) findViewById(R.id.searchBox);
		results = (ListView) findViewById(R.id.listView_results);
		resultAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, resultList);
		results.setAdapter(resultAdapter);
		// status = (TextView) findViewById(R.id.status);
		// mProgress = (ProgressBar) findViewById(R.id.downloadBar);
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
			searchData(search.getText().toString());
			// status.setText("Button Clicked!");
			// mProgress.setProgress(50);
			// startDownload();
			break;
		}
	}

	private void searchData(final String search) {

		// TODO Auto-generated method stub
		System.out.println("asdsa78d");
		Thread thread = new Thread() {

			@Override
			public void run() {
				try {

					System.out.println("asdsad");
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(query);

					nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("command",
							"search"));
					nameValuePairs.add(new BasicNameValuePair("query", search));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					System.out.println("asdsad");
					System.out.println(nameValuePairs);
					response = httpclient.execute(httppost);
					System.out.println(response);
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					final String response = httpclient.execute(httppost,
							responseHandler);

					System.out.println(response);
					runOnUiThread(new Runnable() {
						public void run() {
							displayResults(response);
						}
					});

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};
		thread.start();
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
			// showDialog(DIALOG_DOWNLOAD_PROGRESS);
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
					// mProgressStatus=(int) ((total * 100) / lenghtOfFile);
					// mProgress.setProgress(mProgressStatus);
					// publishProgress("" + (int) ((total * 100) /
					// lenghtOfFile));
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
			// dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
		}
	}

	void displayResults(String response){
		resultList.clear();
		try {
			JSONArray j = new JSONArray(response);
			for (int i = 0; i < j.length(); ++i) {
				resultList.add("Title: "+j.getJSONObject(i).getString("title")+"\nDescription: "+j.getJSONObject(i).getString("description"));
			}
			resultAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
