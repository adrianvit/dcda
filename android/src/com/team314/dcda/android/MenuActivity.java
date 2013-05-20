package com.team314.dcda.android;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gcm.GCMRegistrar;

public class MenuActivity extends Activity implements OnClickListener {

	public static final String TAG = "MenuActivity";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        Button browseButton = (Button) this.findViewById(R.id.browse_button);
        browseButton.setOnClickListener(this);
        
        Button accountButton = (Button) this.findViewById(R.id.myAccount_button);
        accountButton.setOnClickListener(this);
        
        Button aboutButton = (Button) this.findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
        
        Button exitButton = (Button) this.findViewById(R.id.help_button);
        exitButton.setOnClickListener(this);   
        GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		Log.d("registrationID", "::" + regId);
		if (regId.equals("")) {
			// Automatically registers application on startup.
			GCMRegistrar.register(this, "472492214191");
			  
		}
		GCMRegistrar.setRegisteredOnServer(MenuActivity.this, true);
		
	}

	@Override
	public void onClick(View v) {

		Intent intent = null;
		
		switch(v.getId()){
			
		case R.id.browse_button:
			intent = new Intent(this, ProductListActivity.class);
			startActivity(intent);
			break;
			
		case R.id.myAccount_button:
			intent = new Intent(this, AccountActivity.class);
			startActivity(intent);
			break;
			
			
		case R.id.about_button:
			intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			break;
			
		case R.id.help_button:
			intent = new Intent(this, HelpActivity.class);
			startActivity(intent);
			break;
		}
	}

	
}
