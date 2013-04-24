package com.team314.dcda.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.team314.dcda.android.http.RestServiceCalls;

public class LoginActivity extends Activity implements OnClickListener{

	
	public static final String TAG = "LoginActivity";
	private  EditText userName;
	private  EditText password;
	private SharedPreferences prefs;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        prefs = this.getSharedPreferences(Utils.PREFS_NAME, 0);

        
        
        Button loginButton = (Button) this.findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(this);
        //loginButton.setEnabled(false);
        
        Button signupButton = (Button) this.findViewById(R.id.buttonSignUp);
        signupButton.setOnClickListener(this);
        
        userName = (EditText) this.findViewById(R.id.editText_username);
        password = (EditText) this.findViewById(R.id.editText_password);
        
        if(prefs.getString("username", null) != null)
        {
        	userName.setText(prefs.getString("username", null)); 
        }
        
        //for development only
        if(prefs.getString("password", null) != null)
        {
        	password.setText(prefs.getString("password", null)); 
        	
        }
	}

	@Override
	public void onClick(View v) {
		
		Intent intent = null;
		
		switch(v.getId()){
			
		case R.id.buttonLogin:
			//call login and store token if successful or show error if not
			String username = userName.getText().toString();
			String pass = password.getText().toString();
			
			if(username.equals("") || pass.equals(""))
			{
				Utils.createAlertDialog(this, "Error", "Please enter credentials");
			}
			else
			{
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("username", username);
				editor.putString("password", pass);
				editor.remove("rented_carid");
				editor.commit();
				if(editor.commit())
				{
					Log.d(TAG, "Commit true" + prefs.getString("username", "email"));
				}
				else
				{
					Log.d(TAG, "Commit false");
				}
				RestServiceCalls.login(this, userName.getText().toString(), password.getText().toString());					
			}
			break;
			
		case R.id.buttonSignUp:
			intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			break;

		}
	}
}
