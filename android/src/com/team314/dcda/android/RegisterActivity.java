package com.team314.dcda.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.team314.dcda.android.http.RestServiceCalls;
import com.team314.dcda.android.json.User;

public class RegisterActivity extends Activity implements OnClickListener {
	
	public static final String TAG = "RegisterActivity";

	private SharedPreferences prefs;
	private ArrayList<EditText> allTextViews;
	private EditText text_first;
	private EditText text_last;
	private EditText text_phone;
	private EditText text_email;
	private EditText text_pass;
	private EditText text_conf;
	private Button button_done;
	

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
        prefs = this.getSharedPreferences(Utils.PREFS_NAME, 0);
        allTextViews = new ArrayList<EditText>();
        
        text_first = (EditText) this.findViewById(R.id.editText_firstname);
        text_last = (EditText) this.findViewById(R.id.editText_lastname);
        text_phone = (EditText) this.findViewById(R.id.editText_phone);
        text_email = (EditText) this.findViewById(R.id.editText_email);
        text_pass = (EditText) this.findViewById(R.id.editText_pass);
        text_conf = (EditText) this.findViewById(R.id.editText_passconf);
        
        allTextViews.add(text_first);
        allTextViews.add(text_last);
        allTextViews.add(text_phone);
        allTextViews.add(text_email);
        allTextViews.add(text_pass);
        allTextViews.add(text_conf);
        
        button_done = (Button) this.findViewById(R.id.button_done);
        button_done.setOnClickListener(this);
	}


	@Override
	public void onClick(View arg0) {
		
		switch(arg0.getId()){
		
		case R.id.button_done:
			
			SharedPreferences.Editor editor = prefs.edit();
			boolean allDone = true;
			for(EditText e: allTextViews)
			{
				if(!(e.getText().length()>0))
				{
					allDone = false;
				}
			}
			
			if(allDone)
			{
				if(text_pass.getText().toString().equals(text_conf.getText().toString()))
				{
					//create account
					User user = new User();
					user.setFirstName(text_first.getText().toString());
					user.setLastName(text_last.getText().toString());
					user.setPhone(text_phone.getText().toString());
					user.setEmail(text_email.getText().toString());
					user.setPass(text_pass.getText().toString());
					
//					//save information in shared preferences
//					editor.putString("first", text_first.getText().toString());
//					editor.putString("last", text_last.getText().toString());
//					editor.putString("phone", text_phone.getText().toString());
//					editor.putString("email", text_email.getText().toString());
//					editor.putString("pass", text_pass.getText().toString());
					
					String location = prefs.getString("location", null);
					if(location == null)
					{
						location = Utils.getAdminLocation(this);
						if(location == null)
							Utils.createAlertDialog(this, "Error", "Could not determine location! Check internet connection!");
						else
						{
							editor.putString("location", location);
							editor.commit();
						}
					}
					
					location = prefs.getString("location", null);
					if(location != null)
					{
						user.setCounty(location);
						RestServiceCalls.registerUser(user, this);											
					}
					else
					{
						Utils.createAlertDialog(this, "Error", "Could not determine location! Check internet connection!");
					}
				}
				else
				{
					Utils.createAlertDialog(this, "Error", "Password fields do not match!");
				}
			}
			else
			{
				Utils.createAlertDialog(this, "Error", "Please fill in all fields!");
			}
			break;
		
		}
		
	}
	

	
}
