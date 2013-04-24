package com.team314.dcda.android;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.team314.dcda.android.http.RestServiceCalls;
import com.team314.dcda.android.json.Address;

public class AddressActivity extends Activity implements OnClickListener{

	private SharedPreferences prefs;
	private EditText text_country;
	private EditText text_county;
	private EditText text_city;
	private EditText text_street;
	private EditText text_street_number;
	private EditText text_postal_code;
	private Button button_done;
	private Address adr;
	private boolean create = false;
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adr_entry);
        
        prefs = this.getSharedPreferences(Utils.PREFS_NAME, 0);
        
        text_country = (EditText) this.findViewById(R.id.editText_country);
        text_county = (EditText) this.findViewById(R.id.editText_county);
        text_city = (EditText) this.findViewById(R.id.editText_city);
        text_street = (EditText) this.findViewById(R.id.editText_street);
        text_street_number = (EditText) this.findViewById(R.id.editText_street_number);
        text_postal_code = (EditText) this.findViewById(R.id.editText_postal_code);
        
        button_done = (Button) this.findViewById(R.id.button_done);
        button_done.setOnClickListener(this);
        
        Bundle b = getIntent().getExtras();
        if( b!= null)
        {
        	adr = (Address) b.getSerializable("adr");
        	if(adr != null)
        	{
        		populateFields();
        	}
        }
        
        create = getIntent().getBooleanExtra("new", false);
        if(create)
        {
        	adr = new Address();
        }
    }

	private void populateFields() {
		
		text_country.setText(adr.getCountry());
		text_county.setText(adr.getCounty());
		text_city.setText(adr.getCity());
		text_street.setText(adr.getStreet());
		text_street_number.setText(adr.getStreet_number());
		text_postal_code.setText(adr.getPostal_code());
		
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		
		case R.id.button_done:

				adr.setCountry(text_country.getText().toString());
				adr.setCounty(text_county.getText().toString());
				adr.setCity(text_city.getText().toString());
				adr.setStreet(text_street.getText().toString());
				adr.setStreet_number(text_street_number.getText().toString());
				adr.setPostal_code(text_postal_code.getText().toString());
				
				if(create)
				{
					RestServiceCalls.createAddress(this, adr);
				}
				else
				{					
					RestServiceCalls.updateAddress(this, adr);
				}
				
			break;
		}
		
	}
}
