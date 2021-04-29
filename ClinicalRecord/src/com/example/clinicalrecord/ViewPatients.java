package com.example.clinicalrecord;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ViewPatients extends Activity {
	Button btn_goto_patientsadd;
	ListView lv_patientview;
	ArrayList<String> patientArray = new ArrayList<String>();
	JSONObject ForViewResponse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_patients);
		getActionBar().setDisplayHomeAsUpEnabled(true); // to add back button on action bar
		lv_patientview = (ListView) findViewById(R.id.lv_patientview);
		btn_goto_patientsadd = (Button) findViewById(R.id.btn_goto_patientsadd);// ahiya mane err lage 6
		String patientID = "0";
		StringBuffer sb = new StringBuffer();
		try {
			// Attaching BufferedReader to the FileInputStream by the help of
			// InputStreamReader
			BufferedReader inputReader = new BufferedReader(
					new InputStreamReader(openFileInput("clinicalRecords_udata.txt")));
			String inputString;
			while ((inputString = inputReader.readLine()) != null) {
				sb.append(inputString + "\n");
			}

			JSONObject data = new JSONObject(sb.toString());
			patientID = data.get("patientID").toString();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new GetPatientsForViewTask().execute(patientID);

		btn_goto_patientsadd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent loginIntent = new Intent(ViewPatients.this, Patient_Master.class);
				loginIntent.putExtra("forupdate", "false");
				ViewPatients.this.startActivity(loginIntent);
				ViewPatients.this.finish();
			}
		});

		// Logic to open in ByID

		lv_patientview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Object o = lv_patientview.getItemAtPosition(position);
				String str = (String) o;
				try {
					JSONArray ja = ForViewResponse.getJSONArray("data");
					JSONObject custData = ja.getJSONObject(position);
					if (custData.get("patientID") != "0") {
						Intent custAddIntent = new Intent(ViewPatients.this, Patient_Master.class);
						custAddIntent.putExtra("patientID", custData.get("patientID").toString());
						custAddIntent.putExtra("forupdate", "true");
						ViewPatients.this.startActivity(custAddIntent);
						ViewPatients.this.finish();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

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

				lv_patientview.setAdapter(adapter);

			} catch (JSONException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}