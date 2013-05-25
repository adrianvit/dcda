package com.team314.dcda.android.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.StringEntity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.team314.dcda.android.LoginActivity;
import com.team314.dcda.android.MenuActivity;
import com.team314.dcda.android.Utils;
import com.team314.dcda.android.json.Address;
import com.team314.dcda.android.json.LoggedUser;
import com.team314.dcda.android.json.Product;
import com.team314.dcda.android.json.User;

public class RestServiceCalls {

	public static final String TAG = "RestServiceCalls";
	
	public static void login(final Context context, String userName, String password)
	{
		final SharedPreferences prefs = context.getSharedPreferences(Utils.PREFS_NAME, 0);
		final SharedPreferences.Editor editor = prefs.edit();

		//get admin county
		String temp = Utils.getAdminLocation(context);
		
		
		URI uri = null;

		if(temp != null)
		{
			prefs.edit().putString("location", temp);
			prefs.edit().commit();
			ProgressDialog dialog = new ProgressDialog(context);
			dialog.setMessage("Please wait");

			//get local server ip and store in preferences
			try {
				uri = URIUtils.createURI(HttpUtils.scheme, HttpUtils.central_ip, HttpUtils.central_port, HttpUtils.central_path + "/" + temp, null, null);
				Log.d(TAG, "Uri is " + uri.toString());	
				
				HttpGet get_method = new HttpGet(uri);
		        
				MyAsyncTask locateAsyncTask = new MyAsyncTask(dialog, new AsyncTaskCallback(){

					@Override
					public void onTaskComplete(HttpResponse response) {
						
						int status = response.getStatusLine().getStatusCode();
						Log.d(TAG, response.getStatusLine().toString());
						if(status == 200)
						{
							String local_server_ip = null ;
							try {
								local_server_ip = HttpUtils.readInputStream(response.getEntity().getContent());
								
								editor.putString("local_server_ip", local_server_ip);
								editor.commit();
							} catch (IllegalStateException e1) {
								Log.e(TAG, "Error reading response [local ip]", e1);	
							} catch (IOException e1) {
								Log.e(TAG, "Error reading response [local ip]", e1);	
							}
							
						}
						else 
						{
							Log.d(TAG, "Didn't receive ok response [local ip]");
							Utils.createAlertDialog(context, "Error", "Could not connect to server!");
						}
						
					}});
				locateAsyncTask.execute(get_method);
				
			} catch (URISyntaxException e1) {
				Log.e(TAG, "Error creating uri", e1);	
			}
			
			
			try
			{
				String query_params = "email=" + userName +"&password=" + password;
				uri = URIUtils.createURI(HttpUtils.scheme, HttpUtils.central_ip, HttpUtils.central_port, HttpUtils.local_login_path, query_params, null);
				HttpPost post_method = new HttpPost(uri);
				Log.d(TAG, "Uri is " + uri.toString());	
				MyAsyncTask loginAsyncTask = new MyAsyncTask(dialog, new AsyncTaskCallback(){

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
								LoggedUser loggingResponse = gson.fromJson(reader, LoggedUser.class);
								//store token 
								editor.putString("token", loggingResponse.getToken());								
								editor.putInt("userid", loggingResponse.getUserid());
								editor.commit();
								Log.d(TAG, "Got token "+loggingResponse.getToken());	
								
								//start main menu intent
								Intent intent = new Intent(context, MenuActivity.class);
								context.startActivity(intent);
								
							} catch (IllegalStateException e1) {
								Log.e(TAG, "Error reading response [login]", e1);	
							} catch (IOException e1) {
								Log.e(TAG, "Error reading response [login]", e1);	
							}
						}
						else
						{
							editor.putString("token", null);
							editor.commit();
							Log.d(TAG, "Didn't receive ok response [login]");
							Utils.createAlertDialog(context, "Error", "Could not login");
						}
						
					}});
				
				loginAsyncTask.execute(post_method);
		        
			}catch (URISyntaxException e1) {
				Log.e(TAG, "Error creating uri", e1);	
			}
			
		}
		else
		{
			Utils.createAlertDialog(context, "Error", "Could not determine location");
		}

	}
	
	public static ArrayList<Product> getProducts(final Context context, int start, String filter, final  ArrayAdapter<Product> adapter)
	{
		SharedPreferences prefs = context.getSharedPreferences(Utils.PREFS_NAME, 0);
		URI uri = null;
		try {
			String query_params = "start="+start;
			if(filter != null)
			{
				query_params += filter;
			}
			
			
			uri = URIUtils.createURI(HttpUtils.scheme, HttpUtils.central_ip, HttpUtils.local_port, HttpUtils.local_products, query_params, null);
			Log.d(TAG, "Uri is " + uri.toString());	
			HttpGet get_method = new HttpGet(uri);
			
			MyAsyncTask getProductsAsyncTask = new MyAsyncTask(null, new AsyncTaskCallback(){
				
				@Override
				public void onTaskComplete(HttpResponse response) {
					
					int status = response.getStatusLine().getStatusCode();
					Log.d(TAG, response.getStatusLine().toString());
					if(status == 200)
					{
						
						try {
				
							InputStream input = response.getEntity().getContent();
							Reader reader = new InputStreamReader(input);	
							Gson gson = new Gson();
							ArrayList<Product> products = gson.fromJson(reader, new TypeToken<ArrayList<Product>>(){}.getType());
						    for(Product c: products)
						    {
						    	adapter.add(c);
						    }
						    Log.d(TAG, "Added products");
						    adapter.notifyDataSetChanged();
							
						} catch (IllegalStateException e1) {
							Log.e(TAG, "Error reading response [get products]", e1);	
						} catch (IOException e1) {
							Log.e(TAG, "Error reading response [get products]", e1);	
						}
						
					}
					else 
					{
						Log.d(TAG, "Didn't receive ok response [get products]");
					}
					
				}});
			getProductsAsyncTask.execute(get_method);
			Log.d(TAG, "Executed method");
		} catch (URISyntaxException e1) {
			Log.e(TAG, "Error creating uri", e1);	
		}
		return null;
	}
	
	public static void registerUser(User user, final Context context)
	{
		SharedPreferences prefs = context.getSharedPreferences(Utils.PREFS_NAME, 0);
		URI uri = null;
		try {
			
			uri = URIUtils.createURI(HttpUtils.scheme, HttpUtils.central_ip, HttpUtils.local_port, HttpUtils.local_users, null, null);
			Log.d(TAG, "Uri is " + uri.toString());	
			HttpPost post_method = new HttpPost(uri);
			post_method.setHeader("Content-Type","application/json");
			Gson gson = new Gson();
			String json = gson.toJson(user);
			post_method.setEntity(new StringEntity(json));
			
			ProgressDialog dialog = new ProgressDialog(context);
			dialog.setMessage("Please wait");
			
			MyAsyncTask asyncTask = new MyAsyncTask(dialog, new AsyncTaskCallback(){
				
				@Override
				public void onTaskComplete(HttpResponse response) {
					
					int status = response.getStatusLine().getStatusCode();
					Log.d(TAG, response.getStatusLine().toString());
					if(status == 200)
					{
						Intent intent = new Intent(context, LoginActivity.class);
						context.startActivity(intent);
					}
					else if (status == 409)
					{
						Utils.createAlertDialog(context, "Error", "This email already exists in the system");
					}
					else 
					{
						Log.d(TAG, "Didn't receive ok response [register User]");
					}
					
				}});
			asyncTask.execute(post_method);
			Log.d(TAG, "Executed method");
		} catch (URISyntaxException e1) {
			Log.e(TAG, "Error creating uri", e1);	
		} catch (UnsupportedEncodingException e1) {
			Log.e(TAG, "Error parsing json", e1);	
		}
	}
	
	public static void updateUser(User user, final Context context)
	{
		SharedPreferences prefs = context.getSharedPreferences(Utils.PREFS_NAME, 0);
		URI uri = null;
		try {
			
			int userid = prefs.getInt("userid", -1);
			String token = prefs.getString("token", null);
			if(userid == -1 || token == null)
			{
				Utils.createAlertDialog(context, "Error", "An error ocurred!");
				return;
			}
			
			uri = URIUtils.createURI(HttpUtils.scheme, HttpUtils.central_ip, HttpUtils.local_port, HttpUtils.local_users + "/" + userid, null, null);
			Log.d(TAG, "Uri is " + uri.toString());	
			HttpPut put_method = new HttpPut(uri);
			put_method.setHeader("Authorization",token);
			put_method.setHeader("Content-Type","application/json");
			Gson gson = new Gson();
			String json = gson.toJson(user);
			put_method.setEntity(new StringEntity(json));
			
			ProgressDialog dialog = new ProgressDialog(context);
			dialog.setMessage("Please wait");
			
			MyAsyncTask asyncTask = new MyAsyncTask(dialog, new AsyncTaskCallback(){
				
				@Override
				public void onTaskComplete(HttpResponse response) {
					
					int status = response.getStatusLine().getStatusCode();
					Log.d(TAG, response.getStatusLine().toString());
					if(status == 200)
					{
						Utils.createAlertDialog(context, "OK", "Information was updated");
					}
					else 
					{
						Utils.createAlertDialog(context, "Error", "Could not update information");
					}
					
				}});
			asyncTask.execute(put_method);
			Log.d(TAG, "Executed method");
		} catch (URISyntaxException e1) {
			Log.e(TAG, "Error creating uri", e1);	
		} catch (UnsupportedEncodingException e1) {
			Log.e(TAG, "Error parsing json", e1);	
		}
	}
	
	public static void getAddresses(final Context context, int i, final ArrayAdapter<Address> adapter) {
		
		final SharedPreferences prefs = context.getSharedPreferences(Utils.PREFS_NAME, 0);
		
		URI uri = null;
		try {
					
			int userid = prefs.getInt("userid", -1);
			String token = prefs.getString("token", null);
			if(userid == -1 || token == null)
			{
				Utils.createAlertDialog(context, "Error", "An error ocurred!");
				return;
			}
			
			uri = URIUtils.createURI(HttpUtils.scheme,HttpUtils.central_ip, HttpUtils.local_port, HttpUtils.local_users + "/" + userid + "/addresses", null, null);
			Log.d(TAG, "Uri is " + uri.toString());	
			HttpGet get_method = new HttpGet(uri);
			get_method.setHeader("Authorization",token);
			
			MyAsyncTask asyncTask = new MyAsyncTask(null, new AsyncTaskCallback(){
				
				@Override
				public void onTaskComplete(HttpResponse response) {
					
					int status = response.getStatusLine().getStatusCode();
					Log.d(TAG, response.getStatusLine().toString());
					if(status == 200)
					{
						
						try {
				
							InputStream input = response.getEntity().getContent();
							Reader reader = new InputStreamReader(input);	
							Gson gson = new Gson();
							ArrayList<Address> addresses = gson.fromJson(reader, new TypeToken<ArrayList<Address>>(){}.getType());
						    for(Address a: addresses)
						    {
						    	adapter.add(a);
						    }
						    Log.d(TAG, "Added Comments");
						    adapter.notifyDataSetChanged();
							
						} catch (IllegalStateException e1) {
							Log.e(TAG, "Error reading response [get Comments]", e1);	
						} catch (IOException e1) {
							Log.e(TAG, "Error reading response [get Comments]", e1);	
						}
						
					}
					else 
					{
						Log.d(TAG, "Didn't receive ok response [get Comments]");
					}
					
				}});
			asyncTask.execute(get_method);
			Log.d(TAG, "Executed method");
		} catch (URISyntaxException e1) {
			Log.e(TAG, "Error creating uri", e1);	
		}
		
	}
	
	public static void updateAddress(final Context context, Address adr)
	{
		SharedPreferences prefs = context.getSharedPreferences(Utils.PREFS_NAME, 0);
		URI uri = null;
		try {
			
			int userid = prefs.getInt("userid", -1);
			String token = prefs.getString("token", null);
			if(userid == -1 || token == null)
			{
				Utils.createAlertDialog(context, "Error", "An error ocurred!");
				return;
			}
			
			uri = URIUtils.createURI(HttpUtils.scheme, HttpUtils.central_ip, HttpUtils.local_port, HttpUtils.local_users + "/" + userid + "/addresses/" + adr.getAddressid(), null, null);
			Log.d(TAG, "Uri is " + uri.toString());	
			HttpPut put_method = new HttpPut(uri);
			put_method.setHeader("Authorization",token);
			put_method.setHeader("Content-Type","application/json");
			Gson gson = new Gson();
			String json = gson.toJson(adr);
			Log.d(TAG, json);
			put_method.setEntity(new StringEntity(json));
			
			ProgressDialog dialog = new ProgressDialog(context);
			dialog.setMessage("Please wait");
			
			MyAsyncTask asyncTask = new MyAsyncTask(dialog, new AsyncTaskCallback(){
				
				@Override
				public void onTaskComplete(HttpResponse response) {
					
					int status = response.getStatusLine().getStatusCode();
					Log.d(TAG, response.getStatusLine().toString());
					if(status == 200)
					{
						Utils.createAlertDialog(context, "OK", "Information was updated");
					}
					else 
					{
						Utils.createAlertDialog(context, "Error", "Could not update information");
					}
					
				}});
			asyncTask.execute(put_method);
			Log.d(TAG, "Executed method");
		} catch (URISyntaxException e1) {
			Log.e(TAG, "Error creating uri", e1);	
		} catch (UnsupportedEncodingException e1) {
			Log.e(TAG, "Error parsing json", e1);	
		}
	}
	
	public static void sendRegistrationIdToServer(final Context context, String regId)
	{
		SharedPreferences prefs = context.getSharedPreferences(Utils.PREFS_NAME, 0);
		URI uri = null;
		try {
			
			int userid = prefs.getInt("userid", -1);
			String token = prefs.getString("token", null);
			if(userid == -1 || token == null)
			{
				Utils.createAlertDialog(context, "Error", "An error ocurred!");
				return;
			}
			
			uri = URIUtils.createURI(HttpUtils.scheme, HttpUtils.central_ip, HttpUtils.local_port, HttpUtils.local_notification + "?id=" + userid + "&regId="+regId, null, null);
			Log.d(TAG, "Uri is " + uri.toString());	
			HttpPost method = new HttpPost(uri);
			method.setHeader("Authorization",token);
			method.setHeader("Content-Type","application/json");
			Gson gson = new Gson();
			String json = gson.toJson(regId);
			Log.d(TAG, json);
			method.setEntity(new StringEntity(json));
			
			ProgressDialog dialog = new ProgressDialog(context);
			dialog.setMessage("GCM Registration");
			
			MyAsyncTask asyncTask = new MyAsyncTask(dialog, new AsyncTaskCallback(){
				
				@Override
				public void onTaskComplete(HttpResponse response) {
					
					int status = response.getStatusLine().getStatusCode();
					Log.d(TAG, response.getStatusLine().toString());
					if(status == 200)
					{
						Utils.createAlertDialog(context, "OK", "GCM registered");
					}
					else 
					{
						Utils.createAlertDialog(context, "Error", "Could not register to GCM");
					}
					
				}});
			//asyncTask.execute(method);
			Log.d(TAG, "Executed method");
		} catch (URISyntaxException e1) {
			Log.e(TAG, "Error creating uri", e1);	
		} catch (UnsupportedEncodingException e1) {
			Log.e(TAG, "Error parsing json", e1);	
		}
		
	}
	
	public static void createAddress(final Context context, Address adr)
	{
		SharedPreferences prefs = context.getSharedPreferences(Utils.PREFS_NAME, 0);
		URI uri = null;
		try {
			
			int userid = prefs.getInt("userid", -1);
			String token = prefs.getString("token", null);
			if(userid == -1 || token == null)
			{
				Utils.createAlertDialog(context, "Error", "An error ocurred!");
				return;
			}
			
			uri = URIUtils.createURI(HttpUtils.scheme, HttpUtils.central_ip, HttpUtils.local_port, HttpUtils.local_users + "/" + userid + "/addresses", null, null);
			Log.d(TAG, "Uri is " + uri.toString());	
			HttpPost method = new HttpPost(uri);
			method.setHeader("Authorization",token);
			method.setHeader("Content-Type","application/json");
			Gson gson = new Gson();
			String json = gson.toJson(adr);
			Log.d(TAG, json);
			method.setEntity(new StringEntity(json));
			
			ProgressDialog dialog = new ProgressDialog(context);
			dialog.setMessage("Please wait");
			
			MyAsyncTask asyncTask = new MyAsyncTask(dialog, new AsyncTaskCallback(){
				
				@Override
				public void onTaskComplete(HttpResponse response) {
					
					int status = response.getStatusLine().getStatusCode();
					Log.d(TAG, response.getStatusLine().toString());
					if(status == 200)
					{
						Utils.createAlertDialog(context, "OK", "Created address");
					}
					else 
					{
						Utils.createAlertDialog(context, "Error", "Could not create address!");
					}
					
				}});
			asyncTask.execute(method);
			Log.d(TAG, "Executed method");
		} catch (URISyntaxException e1) {
			Log.e(TAG, "Error creating uri", e1);	
		} catch (UnsupportedEncodingException e1) {
			Log.e(TAG, "Error parsing json", e1);	
		}
	}
}
