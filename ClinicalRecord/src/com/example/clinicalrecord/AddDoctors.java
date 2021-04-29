package com.example.clinicalrecord;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import android.widget.EditText;
import android.widget.Toast;

public class AddDoctors extends Activity {

	Button btn_save;
	EditText et_doctorname, et_doctoremail, et_doctormobile, et_ProfilePic, et_specilization;
	String forupdate, DoctorID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_doctors);

		getActionBar().setDisplayHomeAsUpEnabled(true); // to add back button on action bar

		btn_save = (Button) findViewById(R.id.btn_ca_savedoctor);
		et_doctoremail = (EditText) findViewById(R.id.et_ca_doctoremail);
		et_doctormobile = (EditText) findViewById(R.id.et_ca_doctorphone);
		et_doctorname = (EditText) findViewById(R.id.et_ca_doctorname);
		et_ProfilePic = (EditText) findViewById(R.id.et_ca_ProfilePic);
		et_specilization = (EditText) findViewById(R.id.et_ca_specilization);

		// by id logic
		forupdate = getIntent().getStringExtra("forupdate").toString();
		System.out.println(forupdate);
		if (forupdate.equals("true")) {
			String doctorid = getIntent().getStringExtra("DoctorID");
			btn_save.setText("Update");
			new ByIdDoctorTask().execute(doctorid);

		}

		btn_save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String DoctorName = et_doctorname.getText().toString();
				String EmailID = et_doctoremail.getText().toString();
				String DoctorContactNo = et_doctormobile.getText().toString();
				String ProfilePic = " ";
				String specilization = et_specilization.getText().toString();

				if (Utils.Validate_Form_DoctorsAdd(DoctorName, EmailID, DoctorContactNo, specilization, ProfilePic)) {
					if (forupdate.equals("true")) {
						new updateDoctorTask().execute(DoctorName, EmailID, DoctorContactNo, specilization, " ",
								DoctorID);
					} else {
						new AddDoctorTask().execute(DoctorName, EmailID, DoctorContactNo, specilization, " ");
					}
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_doctors, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, ViewDoctor.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

//////////////////////Our Custom Class for API Call ////////////
	private class AddDoctorTask extends AsyncTask<String, String, String> {
		String result;

		@Override
		protected String doInBackground(String... vb) {
			JSONObject data = new JSONObject();
			try {
				URL url = new URL(Utils.get_BaseUrl() + "/insert_doctor_master.php");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json; utf-8");
				con.setRequestProperty("Accept", "application/json");
				con.setDoOutput(true);
				// (DoctorName, EmailID, DoctorContactNo,ProfilePic,specilization);

				data.put("DoctorName", vb[0]);
				data.put("EmailID", vb[1]);
				data.put("DoctorContactNo", vb[2]);
				data.put("ProfilePic", vb[4]);
				data.put("specilization", vb[3]);

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
				if (res.get("status").toString().equals("Success")) {

					Intent loginIntent = new Intent(AddDoctors.this, ViewDoctor.class);
					AddDoctors.this.startActivity(loginIntent);
					AddDoctors.this.finish();
				} else {
					Toast.makeText(getApplicationContext(), "Unable to save Customer now", Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {

//TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

//////////////////////Our Custom Class UPDATE for API Call ////////////
	private class updateDoctorTask extends AsyncTask<String, String, String> {
		String result;

		@Override
		protected String doInBackground(String... vb) {
			JSONObject data = new JSONObject();
			try {
				URL url = new URL(Utils.get_BaseUrl() + "/update_doctor_master.php");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json; utf-8");
				con.setRequestProperty("Accept", "application/json");
				con.setDoOutput(true);
//(DoctorName, EmailID, DoctorContactNo,ProfilePic,specilization);

				data.put("DoctorID", vb[5]);
				data.put("DoctorName", vb[0]);
				data.put("EmailID", vb[1]);
				data.put("DoctorContactNo", vb[2]);
				data.put("ProfilePic", vb[4]);
				data.put("specilization", vb[3]);

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

					Intent loginIntent = new Intent(AddDoctors.this, ViewDoctor.class);
					AddDoctors.this.startActivity(loginIntent);
					AddDoctors.this.finish();
				} else {
					Toast.makeText(getApplicationContext(), "Unable to save Customer now", Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {

//TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

//////////////////////Our Custom Class BYID  for API Call ////////////
	private class ByIdDoctorTask extends AsyncTask<String, String, String> {
		String result;

		@Override
		protected String doInBackground(String... vb) {
			JSONObject data = new JSONObject();
			try {
				URL url = new URL(Utils.get_BaseUrl() + "/doctor_byid_list.php");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json; utf-8");
				con.setRequestProperty("Accept", "application/json");
				con.setDoOutput(true);
//(DoctorName, EmailID, DoctorContactNo,ProfilePic,specilization);

				data.put("DoctorID", vb[0]);

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
					et_specilization.setText(res.get("specilization").toString());
					et_doctorname.setText(res.get("DoctorName").toString());
					et_doctormobile.setText(res.get("DoctorContactNo").toString());
					et_doctoremail.setText(res.get("EmailID").toString());
					DoctorID = res.get("DoctorID").toString();
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
