package com.team314.dcda.android;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.team314.dcda.android.http.RestServiceCalls;
import com.team314.dcda.android.json.Product;

public class ProductListActivity extends ListActivity implements OnScrollListener, OnItemClickListener, OnClickListener {

	 private MyAdapter adapter;
	 private int prevTotalCount = 0;
	 private String filters;
	 private EditText searchText;
	 private Button buttonSearch;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ArrayList<Product> productList = new ArrayList<Product>();
        
        setContentView(R.layout.list);
        
        searchText = (EditText) findViewById(R.id.textSearch);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        
        buttonSearch.setOnClickListener(this);
        
        filters = getIntent().getStringExtra("filters");
        
        adapter = new MyAdapter(this, productList);
        setListAdapter(adapter); 
        getListView().setOnScrollListener(this);
        getListView().setOnItemClickListener(this);
        RestServiceCalls.getProducts(this, 0, filters, adapter);
    }

    int old_count = 0;

    
    @Override
    public void onScroll(AbsListView view, int firstVisible, int visibleCount, int totalCount) {

		/*
        boolean loadMore = (firstVisible + visibleCount >= totalCount) && totalCount!= prevTotalCount;
        Log.d("list",loadMore+"");
        if(loadMore) {
            totalCount = prevTotalCount;
            RestServiceCalls.getProducts(this, firstVisible + visibleCount, filters, adapter);
        }*/

    }

    @Override
    public void onScrollStateChanged(AbsListView v, int s) { }    

    class MyAdapter extends ArrayAdapter<Product>
    {

		private final Context context;
    	private final ArrayList<Product> objects;

    	public MyAdapter(Context context, ArrayList<Product> productList) {
    		super(context, R.layout.row, productList);
    		this.context = context;
    		this.objects = productList;
    	}
    	
		@Override
		public int getCount() {
			return objects.size();
		}

		@Override
		public Product getItem(int position) {
			return objects.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.row, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.row_text);
			//ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
			textView.setText(objects.get(position).getDescription() + " " + objects.get(position).getPrice());
			
			return rowView;
		}
    	
    }


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = this.getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        
        if(requestCode == 0)
        {
        	if(resultCode == RESULT_OK)
        	{        	
        		filters = intent.getStringExtra("filters");
        		adapter.clear();
        		RestServiceCalls.getProducts(this, 0, filters, adapter);
        	}
        	else
        	{
        		Log.d("List", "Result not ok");
        	}
        }
        else
        {
        	Log.d("List", "No response");
        }

    }

	@Override
	public void onClick(View arg0) {
		Editable text = searchText.getText();
		RestServiceCalls.searchProducts(this, 0, filters,text.toString(), adapter);
		
	}
}
