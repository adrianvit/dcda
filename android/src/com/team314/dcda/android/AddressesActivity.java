package com.team314.dcda.android;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.team314.dcda.android.http.RestServiceCalls;
import com.team314.dcda.android.json.Address;

public class AddressesActivity extends ListActivity implements OnItemClickListener{

	private MyAddressAdapter adapter;
	private static final String TAG = "AddressesActivity";
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.list);
        
        ArrayList<Address> commentList = new ArrayList<Address>();
        adapter = new MyAddressAdapter(this, commentList);
        setListAdapter(adapter); 
        getListView().setOnItemClickListener(this);
        RestServiceCalls.getAddresses(this, 0, adapter);
    }
    
    
    class MyAddressAdapter extends ArrayAdapter<Address>
    {

		private final Context context;
    	private final ArrayList<Address> objects;

    	public MyAddressAdapter(Context context, ArrayList<Address> addressList) {
    		super(context, R.layout.row, addressList);
    		this.context = context;
    		this.objects = addressList;
    	}
    	
		@Override
		public int getCount() {
			return objects.size();
		}

		@Override
		public Address getItem(int position) {
			return objects.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.address, parent, false);
			TextView textView_body = (TextView) rowView.findViewById(R.id.textView_adr);
			textView_body.setText(objects.get(position).getCity()+" "+objects.get(position).getStreet());
			
			return rowView;
		}
    	
    }
    
    @Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
    	Intent i = new Intent(this, AddressActivity.class);
    	Bundle b = new Bundle();
    	b.putSerializable("adr", adapter.getItem(arg2));
    	i.putExtras(b);
    	startActivity(i);

	}



}
