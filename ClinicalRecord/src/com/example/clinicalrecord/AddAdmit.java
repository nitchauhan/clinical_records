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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddAdmit extends Activity {

	AutoCompleteTextView pat;
	ArrayList<String> patientArray = new ArrayList<String>();
	JSONObject ForViewResponse;
	Button btn_save;
	EditText et_patientName1, et_patientAdmitDate, et_patientDischargeDate, et_Ward;
	String forupdate, AdmitID,patid;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_admit);

		getActionBar().setDisplayHomeAsUpEnabled(true); // to add back button on action bar

		btn_save = (Button) findViewById(R.id.btn_saveadmit);
		pat = (AutoCompleteTextView) findViewById(R.id.patname);
		et_Ward = (EditText) findViewById(R.id.ad_Ward);
		patid="0";

		// by id logic
//		forupdate = getIntent().getStringExtra("forupdate").toString();
//		System.out.println(forupdate);
//		if (forupdate.equals("true")) {
//			String admitid = getIntent().getStringExtra("AdmitID");
//			btn_save.setText("Update");
//			new AddAdmitTask().execute(AdmitID);
//
//		}
		
		pat.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Object item = parent.getItemAtPosition(position);
				try {
					JSONArray ja = ForViewResponse.getJSONArray("data");
					for(int i=0;i<ja.length();i++)
					{
						if(ja.getJSONObject(i).get("patientname").toString().equals(item.toString()))
						{
							
							JSONObject prodData = ja.getJSONObject(i);
							patid = prodData.get("patientID").toString();
							break;
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
				}

			}
		});
		new GetPatientsForViewTask().execute(); 
		btn_save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				String patientName1 = et_patientName1.getText().toString();
//				String patientAdmitDate = et_patientAdmitDate.getText().toString();
//				String patientDischargeDate = et_patientDischargeDate.getText().toString();
				String Ward = et_Ward.getText().toString();
				System.out.println(patid);
				new AddAdmitTask().execute(patid, Ward);
//				if (Utils.Validate_Form_AdmitAdd Ward)) {
//					if (forupdate.equals("true")) {
//						new updateDoctorTask().execute(patientName1, patientAdmitDate, patientDischargeDate, Ward);
//					} else {
//						new AddAdmitTask().execute(patid, Ward);
//					}
//				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_admit, menu);
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
	private class AddAdmitTask extends AsyncTask<String, String, String> {
		String result;

		@Override
		protected String doInBackground(String... vb) {
			JSONObject data = new JSONObject();
			try {
				URL url = new URL(Utils.get_BaseUrl() + "/insert_admit_master.php");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json; utf-8");
				con.setRequestProperty("Accept", "application/json");
				con.setDoOutput(true);
				// (DoctorName, EmailID, DoctorContactNo,ProfilePic,specilization);

				data.put("patientID", vb[0]);				
				data.put("Ward", vb[1]);
				
				System.out.println(vb[0]);
				System.out.println(vb[1]);
				
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

					Intent loginIntent = new Intent(AddAdmit.this, ViewAdmit.class);
					AddAdmit.this.startActivity(loginIntent);
					AddAdmit.this.finish();
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
				URL url = new URL(Utils.get_BaseUrl() + "/update_admit_master.php");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json; utf-8");
				con.setRequestProperty("Accept", "application/json");
				con.setDoOutput(true);
//(DoctorName, EmailID, DoctorContactNo,ProfilePic,specilization);

//				data.put("admittID", vb[5]);
				data.put("admittID", vb[0]);
				data.put("patientID", vb[1]);
				data.put("Admit_Date", vb[2]);
				data.put("Discharge_Date", vb[4]);
				data.put("Ward", vb[3]);

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

					Intent loginIntent = new Intent(AddAdmit.this, AdmitPatientDetails.class);
					AddAdmit.this.startActivity(loginIntent);
					AddAdmit.this.finish();
				} else {
					Toast.makeText(getApplicationContext(), "Unable to save Customer now", Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {

//TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

//////////////////////Our Custom Class for API Call ////////////
	private class GetPatientsForViewTask extends AsyncTask<String, String, String> {
		String result;

		@Override
		protected String doInBackground(String... vb) {
			JSONObject data = new JSONObject();
			try {
				URL url = new URL(Utils.get_BaseUrl() + "/patient_view_list.php");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json; utf-8");
				con.setRequestProperty("Accept", "application/json");
				con.setDoOutput(true);

//				data.put("patientID", vb[0]);

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

					patientArray.add(item.get("patientname").toString());
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
						R.layout.activity_listview_patientview, patientArray);

				pat.setAdapter(adapter);
				pat.setThreshold(1);

			} catch (JSONException e) {

// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
