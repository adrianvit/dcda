package com.team314.dcda.android.http;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class MyAsyncTask extends AsyncTask<HttpRequestBase , Void, HttpResponse>{

	public static final String TAG = "MyAsyncTask";
	
	private ProgressDialog dialog;
	private AsyncTaskCallback callback;
	
	public MyAsyncTask(ProgressDialog dialog, AsyncTaskCallback callback)
	{
		this.dialog = dialog;
		this.callback = callback;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		if(this.dialog != null)
		{			
			this.dialog.show();
		}
	}
	
	@Override
	protected HttpResponse doInBackground(HttpRequestBase... params) {
		
		DefaultHttpClient httpClient = HttpUtils.getThreadSafeClient();
		HttpRequestBase method = params[0];
		try {
			HttpResponse response = httpClient.execute(method);
			return response;
		} catch (ClientProtocolException e) {
			Log.e(TAG, "Error in asyncTask", e);	
		} catch (IOException e) {
			Log.e(TAG, "Error in asyncTask", e);
		}
		return null;
	}

	
	@Override
	protected void onPostExecute(HttpResponse response)
	{
		if(this.dialog != null)
		{			
			this.dialog.dismiss();
		}
		if(response != null)
		{
			Log.d(TAG, "Got entity "+response.toString());			
		}
		else
		{
			Log.d(TAG, "Got null!!");		
		}
		callback.onTaskComplete(response);
	}
}
