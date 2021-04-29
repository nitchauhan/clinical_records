package com.example.clinicalrecord;
//getActionBar().setDisplayHomeAsUpEnabled(true); // to add back button on action bar

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Patient_Master extends Activity {
	Button btn_save, btn_goto_pdtlview;
	EditText et_patientCODE, et_patientname, et_mobile_NO, et_Addresss, et_DOB, et_Blood_Group, et_DoctorID, et_gender;
	String forupdate, patientID;

	ArrayList<String> pdtlArray = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_master);
		getActionBar().setDisplayHomeAsUpEnabled(true); // to add back button on action bar

		btn_save = (Button) findViewById(R.id.btn_ca_savepatient);
		et_patientCODE = (EditText) findViewById(R.id.patientCode);
		et_patientname = (EditText) findViewById(R.id.patientname);
		et_mobile_NO = (EditText) findViewById(R.id.patientmobile);
		et_Addresss = (EditText) findViewById(R.id.patientAddress);
		et_DOB = (EditText) findViewById(R.id.dateOfBirth);
		et_Blood_Group = (EditText) findViewById(R.id.bloodgroup);
		et_gender = (EditText) findViewById(R.id.Gender);

		btn_goto_pdtlview = (Button) findViewById(R.id.btn_goto_pdtlview);
//		et_DoctorID = (EditText) findViewById(R.id.et_ca_specilization);

		// by id logic
		forupdate = getIntent().getStringExtra("forupdate").toString();
		System.out.println(forupdate);
		if (forupdate.equals("true")) {
			String PatientID = getIntent().getStringExtra("patientID");
			btn_save.setText("Update");
			new ByIdPatientTask().execute(PatientID);

		}

		btn_goto_pdtlview.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent custAddIntent = new Intent(Patient_Master.this, ViewPatientDtl.class);
				custAddIntent.putExtra("pdtlArray", pdtlArray);
				custAddIntent.putExtra("patientID", patientID);

				Patient_Master.this.startActivity(custAddIntent);
				Patient_Master.this.finish();
			}
		});

		btn_save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String PatientCode = et_patientCODE.getText().toString();
				String PatientName = et_patientname.getText().toString();
				String mobile = et_mobile_NO.getText().toString();
				String Addresss = et_Addresss.getText().toString();
				String DOB = et_DOB.getText().toString();
				String BloodGroup = et_Blood_Group.getText().toString();
				String Gender = et_gender.getText().toString();

				if (Utils.Validate_Form_PatientsAdd(PatientCode, PatientName, mobile, Addresss, DOB, BloodGroup,
						Gender)) {
					if (forupdate.equals("true")) {
						new UpdatePatientTask().execute(PatientCode, PatientName, mobile, Addresss, DOB, BloodGroup,
								Gender, patientID);
					} else {
						new AddPatientTask().execute(PatientCode, PatientName, mobile, Addresss, DOB, BloodGroup,
								Gender, "0");
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
			Intent intent = new Intent(this, ViewPatients.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

//////////////////////Our Custom Class for API Call ////////////
	private class AddPatientTask extends AsyncTask<String, String, String> {
		String result;

		@Override
		protected String doInBackground(String... vb) {
			JSONObject data = new JSONObject();
			try {
				URL url = new URL(Utils.get_BaseUrl() + "/insert_patient_master.php");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json; utf-8");
				con.setRequestProperty("Accept", "application/json");
				con.setDoOutput(true);
				// (DoctorName, EmailID, DoctorContactNo,ProfilePic,specilization);
				// (PatientCode, PatientName, mobile, Addresss, DOB, BloodGroup,
				// Gender,DoctorID);

				data.put("patientCODE", vb[0]);
				data.put("patientname", vb[1]);
				data.put("mobile_NO", vb[2]);
				data.put("Addresss", vb[3]);
				data.put("DOB", vb[4]);
				data.put("Blood_Group", vb[5]);
				data.put("gender", vb[6]);
				data.put("DoctorID", vb[7]);

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

					Intent loginIntent = new Intent(Patient_Master.this, ViewPatients.class);
					Patient_Master.this.startActivity(loginIntent);
					Patient_Master.this.finish();
				} else {
					Toast.makeText(getApplicationContext(), "Unable to save Customer now", Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {

//TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

//////////////////////Our Custom Class Update  for API Call ////////////
	private class UpdatePatientTask extends AsyncTask<String, String, String> {
		String result;

		@Override
		protected String doInBackground(String... vb) {
			JSONObject data = new JSONObject();
			try {
				URL url = new URL(Utils.get_BaseUrl() + "/update_patient_master.php");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json; utf-8");
				con.setRequestProperty("Accept", "application/json");
				con.setDoOutput(true);

//(PatientCode, PatientName, mobile, Addresss, DOB, BloodGroup, Gender,DoctorID);

				data.put("patientCODE", vb[0]);
				data.put("patientname", vb[1]);
				data.put("mobile_NO", vb[2]);
				data.put("Addresss", vb[3]);
				data.put("DOB", vb[4]);
				data.put("Blood_Group", vb[5]);
				data.put("gender", vb[6]);
				data.put("patientID", vb[7]);

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

					Intent loginIntent = new Intent(Patient_Master.this, ViewPatients.class);
					Patient_Master.this.startActivity(loginIntent);
					Patient_Master.this.finish();
				} else {
					Toast.makeText(getApplicationContext(), "Unable to save Customer now", Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {

//TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

//////////////////////Our Custom Class for BYID API Call ////////////
	private class ByIdPatientTask extends AsyncTask<String, String, String> {
		String result;

		@Override
		protected String doInBackground(String... vb) {
			JSONObject data = new JSONObject();
			try {
				URL url = new URL(Utils.get_BaseUrl() + "/patient_BYid_list.php");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json; utf-8");
				con.setRequestProperty("Accept", "application/json");
				con.setDoOutput(true);
// (DoctorName, EmailID, DoctorContactNo,ProfilePic,specilization);
//(PatientCode, PatientName, mobile, Addresss, DOB, BloodGroup, Gender,DoctorID);

				data.put("patientID", vb[0]);

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
				JSONObject res1 = res;
				System.out.println(res);
				if (res.get("status").toString().equals("Success")) {
					res = res.getJSONArray("data").getJSONObject(0);
					et_Addresss.setText(res.get("Addresss").toString());
					et_Blood_Group.setText(res.get("Blood_Group").toString());
					et_DOB.setText(res.get("DOB").toString());
					et_gender.setText(res.get("gender").toString());
					et_mobile_NO.setText(res.get("mobile_NO").toString());
					et_patientCODE.setText(res.get("patientCODE").toString());
					et_patientname.setText(res.get("patientname").toString());
					patientID = res.get("patientID").toString();

					///////////////////////////////

					JSONArray ja = res1.getJSONArray("dtldata");
					System.out.println(ja);
					for (int i = 0; i < ja.length(); i++) {
						JSONObject item = ja.getJSONObject(i);

						pdtlArray.add("Dieses : " + item.get("Dieses_Name").toString() + ", situations : "
								+ item.get("situations").toString() + ", symptoms : "
								+ item.get("symptoms").toString());
					}

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
