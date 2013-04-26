package com.team314.dcda.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.team314.dcda.android.http.AsyncTaskCallback;
import com.team314.dcda.android.http.HttpUtils;
import com.team314.dcda.android.http.MyAsyncTask;
import com.team314.dcda.android.http.RestServiceCalls;
import com.team314.dcda.android.json.User;

public class AccountActivity extends Activity implements OnClickListener {

	private static final String TAG = "AccountActivity"; 
	private EditText text_first;
	private EditText text_last;
	private EditText text_phone;
	private EditText text_oldpass;
	private EditText text_newpass;
	private Button button_save;
	private Button button_adr;
	private Button button_pass;
	private User user;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);
        
        text_first = (EditText) this.findViewById(R.id.editText_firstname_account);
        text_last = (EditText) this.findViewById(R.id.editText_lastname_account);
        text_phone = (EditText) this.findViewById(R.id.editText_phone_account);
        text_oldpass = (EditText) this.findViewById(R.id.editText_oldpass_account);
        text_newpass = (EditText) this.findViewById(R.id.editText_newpass_account);
        
        

        button_save = (Button) this.findViewById(R.id.button_save);
        button_save.setOnClickListener(this);

        button_adr = (Button) this.findViewById(R.id.button_adr);
        button_adr.setOnClickListener(this);
        
        button_pass = (Button) this.findViewById(R.id.button_pass);
        button_pass.setOnClickListener(this);

        user = new User();
        this.getUser();      
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		
		case R.id.button_save:
			
				user.setFirstName(text_first.getText().toString());
				user.setLastName(text_last.getText().toString());
				user.setPhone(text_phone.getText().toString());
				RestServiceCalls.updateUser(user, this);
				
			break;
			
		case R.id.button_adr:
			Intent i = new Intent(this, AddressesActivity.class);
			startActivity(i);
			break;
			
		case R.id.button_pass:

			if(user.getPass().equals(text_oldpass.getText().toString()) && !text_newpass.getText().toString().equals(""))
			{
				user.setPass(text_newpass.getText().toString());
				Log.d(TAG, "User id " + user.getUserid());	
				RestServiceCalls.updateUser(user, this);
			}
			else
			{
				Utils.createAlertDialog(this, "Error", "Wrong password");
			}
			break;
			
		}
	}
	
	private void getUser()
	{
		SharedPreferences prefs = this.getSharedPreferences(Utils.PREFS_NAME, 0);
		URI uri = null;
		try {
			
			int userid = prefs.getInt("userid", -1);
			String token = prefs.getString("token", null);
			if(userid == -1 || token == null)
			{
				Utils.createAlertDialog(this, "Error", "An error ocurred!");
				return;
			}	
			
			uri = URIUtils.createURI(HttpUtils.scheme, HttpUtils.central_ip, HttpUtils.local_port, HttpUtils.local_users + "/" + userid, null, null);
			Log.d(TAG, "Uri is " + uri.toString());	
			HttpGet method = new HttpGet(uri);
			method.setHeader("Authorization",token);

			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("Please wait");
			
			MyAsyncTask getCarsAsyncTask = new MyAsyncTask(dialog, new AsyncTaskCallback(){
				
				@Override
				public void onTaskComplete(HttpResponse response) {
					
					int status = response.getStatusLine().getStatusCode();
					Log.d(TAG, response.getStatusLine().toString());
					if(status == 200)
					{
						InputStream input;
						try {
							input = response.getEntity().getContent();
							Reader reader = new InputStreamReader(input);	
							Gson gson = new Gson();
							User temp = gson.fromJson(reader, User.class);
							if(user != null)
							{
								text_first.setText(temp.getFirstName());
								text_last.setText(temp.getLastName());
								text_phone.setText(temp.getPhone());
								user = temp;
							}
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					else 
					{
						Utils.createAlertDialog(AccountActivity.this, "Error", "Could not get information");
					}
					
				}});
			getCarsAsyncTask.execute(method);
			Log.d(TAG, "Executed method");
		} catch (URISyntaxException e1) {
			Log.e(TAG, "Error creating uri", e1);	
		}
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = this.getMenuInflater();
    	inflater.inflate(R.menu.menu3, menu);
    	return true;
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	

    	case R.id.add_adr :
    		Log.d(TAG, "Clicked Add");
    		Intent i = new Intent(AccountActivity.this, AddressActivity.class);
    		i.putExtra("new", true);
        	startActivity(i);
    		break;

    	}
		return false;
    }
}
