package com.example.clinicalrecord;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Dashboard extends Activity {
	Button btn_goto_docterview, btn_goto_patientview, btn_nof_pat, btn_nof_doc,btn_goto_newadmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		btn_goto_docterview = (Button) findViewById(R.id.btn_goto_doctorview);
		btn_goto_patientview = (Button) findViewById(R.id.btn_goto_patientview);
		btn_goto_newadmit =(Button)findViewById(R.id.btn_goto_newadmit);
		btn_nof_pat = (Button) findViewById(R.id.btn_nof_pat);
		btn_nof_doc = (Button) findViewById(R.id.btn_nof_doc);
		new DBCountTask().execute();
		btn_goto_docterview.setOnClickListener(new View.OnClickListener() {

		
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent viewdoctorIntent = new Intent(Dashboard.this, ViewDoctor.class);
				Dashboard.this.startActivity(viewdoctorIntent);
				Dashboard.this.finish();
			}
		});

		btn_goto_newadmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent viewpatientIntent = new Intent(Dashboard.this, ViewAdmit.class);
				Dashboard.this.startActivity(viewpatientIntent);
				Dashboard.this.finish();
			}
		});
		
		
		btn_goto_patientview.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent viewpatientIntent = new Intent(Dashboard.this, ViewPatients.class);
				Dashboard.this.startActivity(viewpatientIntent);
				Dashboard.this.finish();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dashboard, menu);
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

//////////////////////Our Custom Class BYID  for API Call ////////////
	private class DBCountTask extends AsyncTask<String, String, String> {
		String result;

		@Override
		protected String doInBackground(String... vb) {
			JSONObject data = new JSONObject();
			try {
				URL url = new URL(Utils.get_BaseUrl() + "/dashboard_count.php");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json; utf-8");
				con.setRequestProperty("Accept", "application/json");
				con.setDoOutput(true);
//(DoctorName, EmailID, DoctorContactNo,ProfilePic,specilization);

//				data.put("DoctorID", vb[0]);

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
//TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
//TODO Auto-generated catch block
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
				if (res.get("status").toString().equals("Success")) {
					res = res.getJSONArray("data").getJSONObject(0);
					btn_nof_doc.setText(btn_nof_doc.getText().toString() + res.get("docc"));
					btn_nof_pat.setText(btn_nof_pat.getText().toString() + res.get("patc"));
				} else {
					Toast.makeText(getApplicationContext(), "Unable to save Customer now", Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {

//TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
