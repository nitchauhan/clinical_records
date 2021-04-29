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
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {
	TextView tv_goto_fp;
	EditText et_username, et_password;
	Button btn_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Assign of value to Activity objects
		/* tv_goto_reg = (TextView)findViewById(R.id.tv_goto_reg); */

		// Assign of value to Activity objects
		tv_goto_fp = (TextView) findViewById(R.id.tv_goto_fp);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		btn_login = (Button) findViewById(R.id.btn_login);

		// Operations on Activity

		////////////////////// tv_gotofp //////////////////////////////
		tv_goto_fp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent fpIntent = new Intent(Login.this, ForgotPassword.class);
				Login.this.startActivity(fpIntent);
				Login.this.finish();
			}

		});
		///////////////////////////////////////////// btn_login
		///////////////////////////////////////////// /////////////////////////////////////////////
		///////////////////////////////////////////// ////////////////////////////
		btn_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String username = et_username.getText().toString();
				String password = et_password.getText().toString();
				if (Utils.Validate_Form_Login(username, password)) {
					new LoginTask().execute(username, password);
				}

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
	public class LoginTask extends AsyncTask<String, String, String> {
		String result;

		@Override
		protected String doInBackground(String... vb) {
			JSONObject data = new JSONObject();
			try {
				URL url = new URL(Utils.get_BaseUrl() + "/login.php");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json; utf-8");
				con.setRequestProperty("Accept", "application/json");
				con.setDoOutput(true);

				data.put("EmailID", vb[0]);
				data.put("PassWord", vb[1]);

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
				if (res.get("status").toString().equals("Success")) {
					Intent loginIntent = new Intent(Login.this, Dashboard.class);
					Login.this.startActivity(loginIntent);
					Login.this.finish();
				} else {
					Toast.makeText(getApplicationContext(), "Wrong Username and Password", Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}