package com.example.clinicalrecord;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ViewAdmit extends Activity {

	Button btn_goto_doctorsadd;
	ListView lv_doctorview;
	ArrayList<String> doctorArray = new ArrayList<String>();
	JSONObject ForViewResponse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_admit);
		getActionBar().setDisplayHomeAsUpEnabled(true); // to add back button on action bar
		lv_doctorview = (ListView) findViewById(R.id.lv_admitview);
		btn_goto_doctorsadd = (Button) findViewById(R.id.btn_goto_add_admit);//

		new GetDoctorsForViewTask().execute();

		btn_goto_doctorsadd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent loginIntent = new Intent(ViewAdmit.this, AddAdmit.class);
				loginIntent.putExtra("forupdate", "false");
				ViewAdmit.this.startActivity(loginIntent);
				ViewAdmit.this.finish();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_admit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, Dashboard.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

//////////////////////Our Custom Class for API Call ////////////
	private class GetDoctorsForViewTask extends AsyncTask<String, String, String> {
		String result;

		@Override
		protected String doInBackground(String... vb) {
			JSONObject data = new JSONObject();
			try {
				URL url = new URL(Utils.get_BaseUrl() + "/admit_list_view.php");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json; utf-8");
				con.setRequestProperty("Accept", "application/json");
				con.setDoOutput(true);

				String jsonInputString = data.toString();
				System.out.println(jsonInputString);

				OutputStream os = con.getOutputStream();
				byte[] input = jsonInputString.getBytes("utf-8");
				os.write(input, 0, input.length);

				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));

				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				System.out.println(response.toString());
				result = response.toString();
				return response.toString();

			} catch (MalformedURLException e) {
// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String aVoid) {

			super.onPostExecute(aVoid);
			try {
				JSONObject res = new JSONObject(result);
				System.out.println(res);
				ForViewResponse = res;
				JSONArray ja = res.getJSONArray("data");
				System.out.println(ja);
				for (int i = 0; i < ja.length(); i++) {
					JSONObject item = ja.getJSONObject(i);

					doctorArray.add(item.get("data").toString());
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
						R.layout.activity_listview_doctorview, doctorArray);

				lv_doctorview.setAdapter(adapter);

			} catch (JSONException e) {

// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
