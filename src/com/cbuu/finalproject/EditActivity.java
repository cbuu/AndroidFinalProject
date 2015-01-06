package com.cbuu.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends Activity implements OnClickListener{

	private EditText contentText = null;
	private Button yes = null;
	private Button cancel = null;
	
	private boolean isEdit = false;
	private int id = -1;
	
	private LocationManager lm = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		
		contentText = (EditText)findViewById(R.id.content);
		yes = (Button)findViewById(R.id.yes);
		cancel = (Button)findViewById(R.id.cancel);
	
		yes.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		Intent intent = getIntent();
		isEdit = intent.getBooleanExtra("isEdit", isEdit);
		if (isEdit) {
			String content = intent.getStringExtra("content");
			id =  intent.getIntExtra("id", 0);
			contentText.setText(content);
		}
	
		
		
	}


	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.yes:
			yes();
			break;
		case R.id.cancel:
			cancel();
			break;
		default:
			break;
		}
	}
	
	private void yes(){
		Intent intent = new Intent(EditActivity.this,MainActivity.class);
		Bundle bundle = new Bundle();

		bundle.putString("content", contentText.getText().toString());
		
		bundle.putLong("time", System.currentTimeMillis());
		
		Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		bundle.putDouble("longitude", location.getLongitude());
		bundle.putDouble("latitude", location.getLatitude());
		
		bundle.putBoolean("isEdit", isEdit);
		bundle.putInt("id", id);
		intent.putExtras(bundle);
		
		setResult(RESULT_OK, intent);
		
		finish();
	}
	
	private void cancel(){
		setResult(RESULT_CANCELED,new Intent());
		finish();
	}
}
