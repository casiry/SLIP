package slip.main;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener {
	
	ToggleButton btnZoneA;
	ToggleButton btnZoneB;
	ToggleButton btnZoneC;
	ToggleButton btnPriceFull;
	ToggleButton btnPriceReduced;
	Button btnSend;

	TextView displayTextViewTop;
	TextView displayTextViewBottom;

	String strPricing = "H";
	String strZoneA = "";
	String strZoneB = "";
	String strZoneC = "";
	String stringToSend = "";
	
	//SL SMS phone number as of 2014-04-25
	//SL PHONE 0767201010
	String phoneNumber = "0767201010";
	
	//SL SMS ticket pricing as of 2014-04-25
	int costOneZone = 36;
	int costTwoZones = 54;
	int costThreeZones = 72;
	int costOneZoneReduced = 20;
	int costTwoZonesReduced = 30;
	int costThreeZonesReduced = 40;
	
	int totalCost = 0;

	SmsManager smsManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		displayTextViewTop = (TextView) findViewById(R.id.tv_display_text);
		displayTextViewTop.setText("Kostnad: " + totalCost + ":00 kr");
		
		displayTextViewBottom = (TextView) findViewById(R.id.tv_display_disclaimer);
		displayTextViewBottom.setText("(ev. operatörskostnader tillkommer)");
		
		btnZoneA = (ToggleButton) findViewById(R.id.btn_zone_a);
		btnZoneA.setOnClickListener(this);
		btnZoneB = (ToggleButton) findViewById(R.id.btn_zone_b);
		btnZoneB.setOnClickListener(this);
		btnZoneC = (ToggleButton) findViewById(R.id.btn_zone_c);
		btnZoneC.setOnClickListener(this);
		
		btnPriceReduced = (ToggleButton) findViewById(R.id.btn_price_red);
		btnPriceReduced.setOnClickListener(this);
		btnPriceFull = (ToggleButton) findViewById(R.id.btn_price_full);
		btnPriceFull.setOnClickListener(this);
		btnPriceFull.setChecked(true);
		
		btnSend = (Button) findViewById(R.id.btn_send);
		btnSend.setOnClickListener(this);
		btnSend.setEnabled(false);	
	}

	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btn_price_full: {
			btnPriceReduced.setChecked(false);
			btnPriceReduced.setEnabled(true);
			btnPriceFull.setEnabled(false);
			strPricing="H";			
			break;
		}
		
		case R.id.btn_price_red: {
			btnPriceFull.setChecked(false);
			btnPriceFull.setEnabled(true);
			btnPriceReduced.setEnabled(false);
			strPricing="R";			
			break;
		}

		case R.id.btn_zone_a: {
			if (btnZoneA.isChecked()) {
				strZoneA = "A";				
				break;
			} else {
				strZoneA = "";				
				break;
			}
		}

		case R.id.btn_zone_b: {
			if (btnZoneB.isChecked()) {
				strZoneB = "B";				
				break;
			} else {
				strZoneB = "";				
				break;
			}
		}

		case R.id.btn_zone_c: {
			if (btnZoneC.isChecked()) {
				strZoneC = "C";				
				break;
			} else {
				strZoneC = "";				
				break;
			}
		}
		
		case R.id.btn_send: {
			ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment();
			confirmDialogFragment.show(getFragmentManager(), "Confirmdialog");
//			sendSMS(phoneNumber, stringToSend);							
			break;			
			}
		}
		updateString();
	}	
	
	public void updateString() {
		
		stringToSend = strPricing + strZoneA + strZoneB + strZoneC;		

		if (stringToSend.length() < 2) {
			totalCost = 0;
			btnSend.setEnabled(false);
		} else if (stringToSend.length() >= 2) {
			totalCost = calculateCost();
			btnSend.setEnabled(true);
		}
		
		displayTextViewTop.setText("Kostnad: " + totalCost + ":00 kr");
	}
	
	public int calculateCost() {
		int cost = -1;
		
		if (strPricing.equals("H")) {
			if (stringToSend.length() == 2) {
				cost = costOneZone;
			} else if (stringToSend.length() == 3) {
				cost = costTwoZones;
			} else if (stringToSend.length() == 4) {
				cost = costThreeZones;
			}
		} else if (strPricing.equals("R")) {
			if (stringToSend.length() == 2) {
				cost = costOneZoneReduced;
			} else if (stringToSend.length() == 3) {
				cost = costTwoZonesReduced;
			} else if (stringToSend.length() == 4) {
				cost = costThreeZonesReduced;
			}
		}
		return cost;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	
	public String getStringToSend() {
		return this.stringToSend;
	}
}
