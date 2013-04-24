package com.team314.dcda.android.http;

import org.apache.http.HttpResponse;


public interface AsyncTaskCallback {

	public void onTaskComplete(HttpResponse response);
}
