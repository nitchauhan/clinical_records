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
import android.widget.EditText;
import android.widget.Toast;

public class PatientDetails extends Activity {

	Button btn_save_pdtl;
	EditText et_diesesName, et_symptoms, et_temparature, et_dateTime, et_situations, et_needToAdmit;
	String forupdate, patientID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_details);
		patientID = getIntent().getStringExtra("patientID");
		btn_save_pdtl = (Button) findViewById(R.id.btn_save_pdtl);
		et_diesesName = (EditText) findViewById(R.id.diesesName);
		et_symptoms = (EditText) findViewById(R.id.symptoms);
		et_temparature = (EditText) findViewById(R.id.temparature);
		et_dateTime = (EditText) findViewById(R.id.dateTime);
		et_needToAdmit = (EditText) findViewById(R.id.needToAdmit);
		et_situations = (EditText) findViewById(R.id.situations);

		btn_save_pdtl.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String diesesName = et_diesesName.getText().toString();
				String symptoms = et_symptoms.getText().toString();
				String temparature = et_temparature.getText().toString();
				String dateTime = et_dateTime.getText().toString();
				String needToAdmit = et_needToAdmit.getText().toString();
				String situations = et_situations.getText().toString();

				if (Utils.Validate_Form_PatientsDtl(diesesName, symptoms, temparature, dateTime, needToAdmit,
						situations)) {

					new AddPatientDtlTask().execute(diesesName, symptoms, temparature, dateTime, needToAdmit,
							situations, patientID, " ");

				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.patient_details, menu);
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

//////////////////////Our Custom Class for API Call ////////////
	private class AddPatientDtlTask extends AsyncTask<String, String, String> {
		String result;

		@Override
		protected String doInBackground(String... vb) {
			JSONObject data = new JSONObject();
			try {
				URL url = new URL(Utils.get_BaseUrl() + "/insert_patients_dieses_details.php");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json; utf-8");
				con.setRequestProperty("Accept", "application/json");
				con.setDoOutput(true);
// (DoctorName, EmailID, DoctorContactNo,ProfilePic,specilization);
// (PatientCode, PatientName, mobile, Addresss, DOB, BloodGroup,
// Gender,DoctorID);

				data.put("Dieses_Name", vb[0]);
				data.put("symptoms", vb[1]);
				data.put("temprature", vb[2]);
				data.put("dates", vb[3]);
				data.put("need_to_admit", vb[4]);
				data.put("situations", vb[5]);
				data.put("patientID", vb[6]);
				data.put("patient_Name", vb[7]);

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

					Intent loginIntent = new Intent(PatientDetails.this, ViewPatients.class);
					PatientDetails.this.startActivity(loginIntent);
					PatientDetails.this.finish();
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
