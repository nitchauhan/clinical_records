package com.example.clinicalrecord;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ViewPatientDtl extends Activity {

	ListView lv_pdtlview;
	ArrayList<String> pdtlArray = new ArrayList<String>();
	Button btn_goto_addpatientdtl;
	String patientID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_patient_dtl);
		getActionBar().setDisplayHomeAsUpEnabled(true); // to add back button on action bar
		lv_pdtlview = (ListView) findViewById(R.id.lv_patient_dtl_view);
		pdtlArray = getIntent().getStringArrayListExtra("pdtlArray");
		patientID = getIntent().getStringExtra("patientID");
		btn_goto_addpatientdtl = (Button) findViewById(R.id.btn_goto_addpatientdtl);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.activity_listview_patient_dtl_view, pdtlArray);

		lv_pdtlview.setAdapter(adapter);

		btn_goto_addpatientdtl.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent custAddIntent = new Intent(ViewPatientDtl.this, PatientDetails.class);
				custAddIntent.putExtra("patientID", patientID);
				ViewPatientDtl.this.startActivity(custAddIntent);
				ViewPatientDtl.this.finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_patient_dtl, menu);
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
}
