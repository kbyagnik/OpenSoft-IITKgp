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

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v7.app.ActionBarActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
//import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	private static class myData {
		String title, desription, size, category, link;

		myData(String t, String d, String s, String c, String l) {
			title = t;
			desription = d;
			size = s;
			category = c;
			link = l;
		}
	}

	private static class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public EfficientAdapter(Context context) {
			System.out.println("rsexchj,bghfgx");
			mInflater = LayoutInflater.from(context);
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			System.out.println("hf23456789hgfv");
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.list_item, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.description = (TextView) convertView
						.findViewById(R.id.description);
				holder.size = (TextView) convertView.findViewById(R.id.size);
				holder.category = (ImageView) convertView
						.findViewById(R.id.category);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			System.out.println("hfhgfv");
			holder.title.setText(resultList.get(position).title);
			holder.description.setText(resultList.get(position).desription);
			// holder.category.setText(resultList.get(position).category);
			holder.size.setText(resultList.get(position).size);
			// holder.title.setText(resultList.title);

			// holder.text2.setText(country[position]);

			return convertView;
		}

		static class ViewHolder {
			TextView title;
			TextView description;
			ImageView category;
			TextView size;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return resultList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public void notifyDataSetChanged() {
			System.out.println("Called");
			super.notifyDataSetChanged();
			// _NotifyOnChange = true;
		}

	}

	Button download;
	// TextView status;
	EditText search;
	ListView results;
	static ArrayList<myData> resultList = new ArrayList<myData>();
	// ArrayAdapter<String> resultAdapter;

	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

	String query = "http://10.20.87.91/check.php";
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
		// resultAdapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, resultList);
		results.setAdapter(new EfficientAdapter(this));
		results.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				// System.out.println(position);
				// System.out.println(resultList.get(position).link);
				// String data = (String) adapter.getItemAtPosition(position);
				// data = data.split("Link")[1];
				String data = resultList.get(position).link;
				String titledata=resultList.get(position).title;
				System.out.println(data);
				startDownload(data,titledata);
			}
		});
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
			searchData(v, search.getText().toString());
			// status.setText("Button Clicked!");
			// mProgress.setProgress(50);
			// startDownload();
			break;
		}
	}

	private void searchData(View v, final String search) {

		// TODO Auto-generated method stub
		System.out.println("asdsa78d");
		new ProgressDialog(v.getContext());
		final ProgressDialog p = ProgressDialog.show(v.getContext(),
				"Waiting for Server", "Accessing Server");

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
							p.dismiss();
							// Toast.makeText(getApplicationContext(),"Success",
							// Toast.LENGTH_SHORT).show();
							displayResults(response);
						}
					});

				} catch (Exception e) {
					// TODO Auto-generated catch block
					p.dismiss();
					e.printStackTrace();
				}
			}

		};
		thread.start();
	}

	private void startDownload(String url,String title) {
		// TODO Auto-generated method stub
		// String url = "http://cse.iitrpr.ac.in/ckn/courses/s2015/q1.pdf"; //
		// your
		// download
		// url
		new DownloadFileAsync().execute(url,title);
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
			System.out.println(aurl);
			System.out.println("LOL");
			Intent intent = new Intent();
			PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);

			final NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		//	 NotificationCompat.Builder builder = new
		//	 NotificationCompat.Builder(getApplicationContext());
			final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				getApplicationContext());
			mBuilder.setContentTitle("Downloading "+aurl[1])
					.setContentText("Download in progress")
					.setSmallIcon(R.drawable.video)
					.setTicker("Downloading "+aurl[1])
					.setProgress(100, 10, false)
				//	.setWhen(System.currentTimeMillis())
					.setContentIntent(pendingIntent);
	//		.setProgressBar(100, 100, false);
			try {

				URL url = new URL(aurl[0]);
				URLConnection conexion = url.openConnection();
				conexion.connect();

				int lenghtOfFile = conexion.getContentLength();
				Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
				System.out.println("LOL");
				File SDCardRoot = Environment.getExternalStorageDirectory();
				System.out.println("LOL");
				// create a new file, to save the downloaded file
				File file = new File(SDCardRoot, "downloaded_file123.pdf");
				System.out.println("LOL"+file);
				mNotifyManager.notify(0, mBuilder.build());
				InputStream input = new BufferedInputStream(url.openStream());
				System.out.println("LOLb"+file.canWrite()+file.isFile());
				mBuilder.setProgress(100, 17, false);
				mNotifyManager.notify(0, mBuilder.build());
				System.out.println("LOLb"+file.canWrite()+file.isFile());
				mNotifyManager.notify(0, mBuilder.build());
				OutputStream output = new FileOutputStream(file); // save file
																	// in SD
																	// Card
				System.out.println("LOLa");
		//		mBuilder.setProgress(100, 17, false);
				System.out.println("LOL");
				int mProgressStatus;
				byte data[] = new byte[1024];

				long total = 0;
				
				mNotifyManager.notify(0, mBuilder.build());
//				mNotifyManager.notify();
				while ((count = input.read(data)) != -1) {
					total += count;
					mProgressStatus = (int) ((total * 100) / lenghtOfFile);
					mBuilder.setProgress(100, mProgressStatus, false);
					// mProgress.setProgress(mProgressStatus);
					System.out
							.println("Downloading..." + mProgressStatus + "%");
					output.write(data, 0, count);
				}
				System.out.println("Downloaded");
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

	void displayResults(String response) {
		resultList.clear();
		try {
			System.out.println("lol");
			JSONArray j = new JSONArray(response);
			for (int i = 0; i < j.length(); ++i) {

				// title.add(j.getJSONObject(i).getString("title"));
				// String t=
				resultList.add(new myData(
						j.getJSONObject(i).getString("title"), j.getJSONObject(
								i).getString("description"), j.getJSONObject(i)
								.getString("size"), j.getJSONObject(i)
								.getString("category"), j.getJSONObject(i)
								.getString("link")));
				// resultList.add("Title: "
				// + j.getJSONObject(i).getString("title")
				// + "\nDescription: "
				// + j.getJSONObject(i).getString("description")
				// + "\nLink" + j.getJSONObject(i).getString("link"));
			}
			// results.;
			results.setAdapter(new EfficientAdapter(this));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
