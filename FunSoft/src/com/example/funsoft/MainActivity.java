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
import java.util.Collections;
import java.util.Comparator;

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
import android.telephony.TelephonyManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
//import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.EventLogTags.Description;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	private static class myData {
		String title, desription, size, category, link, rating, downloads;
		int downloadCount, sizeB;
		float ratingNum;

		myData(String t, String d, String s, String c, String l, String r,
				String dd) {

			System.out.println(l + " " + r);
			int tempSize = Integer.parseInt(s) / 1024;
			if (tempSize < 1024) {
				size = Integer.toString(tempSize) + " KB";
			} else {
				tempSize /= 1024;
				size = Integer.toString(tempSize) + " MB";
			}
			title = t;
			desription = d;
			// size = s;
			category = c;
			link = l;
			rating = "Ratings : " + r;
			downloads = "Downloads : " + dd;
			sizeB = Integer.parseInt(s);
			downloadCount = Integer.parseInt(dd);
			ratingNum = Float.parseFloat(r);
		}
	}

	private static class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public EfficientAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		public View getView(int position, View convertView, ViewGroup parent) {

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
				holder.rating = (TextView) convertView
						.findViewById(R.id.rating);
				holder.download = (TextView) convertView
						.findViewById(R.id.download);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.title.setText(resultList.get(position).title);

			int templen = 20;
			if (resultList.get(position).desription.length() < templen)
				templen = resultList.get(position).desription.length();

			String tempstr = resultList.get(position).desription.substring(0,
					templen);

			holder.description.setText(tempstr + " ...");
			holder.size.setText(resultList.get(position).size);
			holder.rating.setText(resultList.get(position).rating);
			holder.download.setText(resultList.get(position).downloads);

			String cat = resultList.get(position).category;
			if (cat.equals("text")) {
				holder.category.setImageResource(R.drawable.content);
			} else if (cat.equals("audio")) {
				holder.category.setImageResource(R.drawable.audio);
			} else if (cat.equals("video")) {
				holder.category.setImageResource(R.drawable.video);
			}

			return convertView;
		}

		static class ViewHolder {
			TextView title;
			TextView description;
			ImageView category;
			TextView size;
			TextView rating;
			TextView download;

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
		}

	}

	Button download;
	EditText search;
	ListView results;
	Spinner sortby;
	static ArrayList<myData> resultList = new ArrayList<myData>();
	public final Context c = this;

	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

	String server = "http://10.20.254.248/opensoft/";
	String query = server + "server.php";
	String learn = server + "download.php";
	String rating = server + "rating.php";

	List<NameValuePair> nameValuePairs;
	HttpResponse response;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		download = (Button) findViewById(R.id.download_btn);
		search = (EditText) findViewById(R.id.searchBox);
		results = (ListView) findViewById(R.id.listView_results);
		results.setAdapter(new EfficientAdapter(this));
		results.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				String data = resultList.get(position).link;
				String titledata = resultList.get(position).title;
				displayPopup(resultList.get(position));
				// startDownload(data, titledata);
			}

		});
		download.setOnClickListener(this);

		sortby = (Spinner) findViewById(R.id.sort_spinner);
		sortby.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 1:
					Collections.sort(resultList, new Comparator<myData>() {

						@Override
						public int compare(myData lhs, myData rhs) {
							// TODO Auto-generated method stub
							int dnleft = lhs.downloadCount;
							int dnright = rhs.downloadCount;
							if (dnleft > dnright) {
								return -1;
							} else {
								return 0;
							}

						}
					});
					break;
				case 2:
					Collections.sort(resultList, new Comparator<myData>() {

						@Override
						public int compare(myData lhs, myData rhs) {
							// TODO Auto-generated method stub
							float dnleft = lhs.ratingNum;
							float dnright = rhs.ratingNum;
							if (dnleft > dnright) {
								return -1;
							} else {
								return 0;
							}

						}
					});
					break;
				case 3:
					Collections.sort(resultList, new Comparator<myData>() {

						@Override
						public int compare(myData lhs, myData rhs) {
							// TODO Auto-generated method stub
							int dnleft = lhs.sizeB;
							int dnright = rhs.sizeB;
							if (dnleft < dnright) {
								return -1;
							} else {
								return 0;
							}

						}
					});
					break;
				}

				results.setAdapter(new EfficientAdapter(c));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
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

			break;
		}
	}

	private void searchData(View v, final String search) {

		// TODO Auto-generated method stub
		new ProgressDialog(v.getContext());
		final ProgressDialog p = ProgressDialog.show(v.getContext(),
				"Waiting for Server", "Accessing Server");

		Thread thread = new Thread() {

			@Override
			public void run() {
				try {

					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(query);

					nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("command",
							"search"));
					nameValuePairs.add(new BasicNameValuePair("query", search));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					response = httpclient.execute(httppost);
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					final String response = httpclient.execute(httppost,
							responseHandler);

					runOnUiThread(new Runnable() {
						public void run() {
							p.dismiss();
							System.out.println(response);
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

	private void learnData(final String link) {

		// TODO Auto-generated method stub
		Thread thread = new Thread() {

			@Override
			public void run() {
				try {

					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(learn);

					nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("command",
							"download"));
					nameValuePairs.add(new BasicNameValuePair("query", link));
					TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					String uuid = tManager.getDeviceId();
					System.out.println("UUID is :" + uuid);
					nameValuePairs.add(new BasicNameValuePair("uuid", uuid));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					response = httpclient.execute(httppost);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};
		thread.start();
	}

	private void startDownload(String url, String title) {
		// TODO Auto-generated method stub
		// your
		// download
		// url
		new DownloadFileAsync().execute(url, title);
	}

	class DownloadFileAsync extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... aurl) {
			System.out.println("Download started...");
			int count;
			Intent intent = new Intent();
			PendingIntent pendingIntent = PendingIntent.getActivity(
					getApplicationContext(), 0, intent,
					Intent.FLAG_ACTIVITY_NEW_TASK);

			final NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					getApplicationContext());
			mBuilder.setContentTitle("Downloading " + aurl[1])
					.setContentText("Download in progress")
					.setSmallIcon(R.drawable.ic_launcher)
					.setTicker("Downloading " + aurl[1])
					.setProgress(100, 0, false).setContentIntent(pendingIntent);
			try {

				URL url = new URL(aurl[0]);
				URLConnection conexion = url.openConnection();
				conexion.connect();
				System.out.println("Url connected...");
				int lenghtOfFile = conexion.getContentLength();
				System.out.println(lenghtOfFile);
				File SDCardRoot = Environment.getExternalStorageDirectory();

				System.out.println(lenghtOfFile);

				System.out.println(url.toString());
				String[] tokens = url.toString().split("/");
				String filename = tokens[tokens.length - 1];
				System.out.println(url.toString().split("/"));

				// tokens = filename.split(".");
				// String ext = tokens[tokens.length - 1];
				// create a new file, to save the downloaded file
				// String filename=url.toString().split("/")[-1];
				System.out.println(url.toString());
				System.out.println("asd" + filename);
				File file = new File(SDCardRoot, filename);

				InputStream input = new BufferedInputStream(url.openStream());

				mNotifyManager.notify(0, mBuilder.build());

				OutputStream output = new FileOutputStream(file); // save file
																	// in SD
																	// Card

				System.out.println("asdsadsdsa");
				int mProgressStatus;
				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					mProgressStatus = (int) ((total * 100) / lenghtOfFile);
					mBuilder.setProgress(100, mProgressStatus, false);
					mNotifyManager.notify(0, mBuilder.build());

					System.out
							.println("Downloading..." + mProgressStatus + "%");
					output.write(data, 0, count);
				}
				System.out.println("Downloaded");

				// intent = new Intent();
				// intent.setAction(android.content.Intent.ACTION_VIEW);
				// File file = new File("/sdcard/download/path/test.mp3");
				// MimeTypeMap mime = MimeTypeMap.getSingleton();
				// intent.setDataAndType(Uri.fromFile(file),mime.getMimeTypeFromExtension(ext));

				mBuilder.setContentTitle("Downloaded " + aurl[1])
						.setContentText("Download Complete")
						.setTicker("Download Complete")
						// .setContentIntent(intent);
						.setContentIntent(pendingIntent);
				mNotifyManager.notify(0, mBuilder.build());
				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
				e.printStackTrace();
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

			JSONArray j = new JSONArray(response);
			for (int i = 0; i < j.length(); ++i) {

				resultList.add(new myData(
						j.getJSONObject(i).getString("title"), j.getJSONObject(
								i).getString("description"), j.getJSONObject(i)
								.getString("size"), j.getJSONObject(i)
								.getString("category"), j.getJSONObject(i)
								.getString("link"), j.getJSONObject(i)
								.getString("rating"), j.getJSONObject(i)
								.getString("downloads")));
			}
			// results.;
			results.setAdapter(new EfficientAdapter(this));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void displayPopup(final myData data) {
		// TODO Auto-generated method stub
		AlertDialog.Builder filePopup = new AlertDialog.Builder(this);

		final View v = getLayoutInflater().inflate(R.layout.popup, null);

		TextView title, description, size;

		title = (TextView) v.findViewById(R.id.title);
		description = (TextView) v.findViewById(R.id.description);
		size = (TextView) v.findViewById(R.id.size);

		title.setText(data.title);
		description.setText(data.desription);
		size.setText(data.size);

		filePopup
				.setTitle("File Info")
				.setView(v)
				.setPositiveButton("Download",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

								System.out.println("link " + data.link);

								startDownload(data.link, data.title);
								learnData(data.link);
								getRating(data);
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						});

		AlertDialog alertDialog = filePopup.create();
		alertDialog.show();
	}

	public void getRating(final myData data) {
		AlertDialog.Builder ratingPopup = new AlertDialog.Builder(this);

		final View v = getLayoutInflater().inflate(R.layout.rating, null);

		final RatingBar rate;

		rate = (RatingBar) v.findViewById(R.id.rating_bar);

		ratingPopup
				.setTitle("Rate This File")
				.setView(v)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						setRating(data.link, rate.getRating());
						System.out.println("Rating set");
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
							}
						});
		AlertDialog alertDialog = ratingPopup.create();
		ratingPopup.show();
	}

	public void setRating(final String link, final Float ratingV) {

		// TODO Auto-generated method stub
		Thread thread = new Thread() {

			@Override
			public void run() {
				try {

					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(rating);

					nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("command",
							"rating"));
					nameValuePairs.add(new BasicNameValuePair("query", link));
					nameValuePairs.add(new BasicNameValuePair("rating", ratingV
							.toString()));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					response = httpclient.execute(httppost);
					System.out.println("Setting the rating..."
							+ ratingV.toString());

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};
		thread.start();
	}

}
