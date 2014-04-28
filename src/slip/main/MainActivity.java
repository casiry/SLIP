package slip.main;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener {
	
	ToggleButton btnZoneA;
	ToggleButton btnZoneB;
	ToggleButton btnZoneC;
	ToggleButton btnPrice;
	Button btnSend;

	TextView displayTextViewTop;
	TextView displayTextViewBottom;

	String strPricing = "H";
	String strZoneA = "";
	String strZoneB = "";
	String strZoneC = "";
	String stringToSend = "";
	
	//Phone number as of 2014-04-25
	String phoneNumber = "0767201010";
	
	//Pricing as of 2014-04-25
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

		btnPrice = (ToggleButton) findViewById(R.id.btn_price);
		btnPrice.setOnClickListener(this);

		btnSend = (Button) findViewById(R.id.btn_send);
		btnSend.setOnClickListener(this);
		btnSend.setEnabled(false);	
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_price: {
			if (btnPrice.isChecked()) {
				strPricing = "R";
				updateString();
				break;
			} else {
				strPricing = "H";
				updateString();
				break;
			}
		}

		case R.id.btn_zone_a: {
			if (btnZoneA.isChecked()) {
				strZoneA = "A";
				updateString();
				break;
			} else {
				strZoneA = "";
				updateString();
				break;
			}
		}

		case R.id.btn_zone_b: {
			if (btnZoneB.isChecked()) {
				strZoneB = "B";
				updateString();
				break;
			} else {
				strZoneB = "";
				updateString();
				break;
			}
		}

		case R.id.btn_zone_c: {
			if (btnZoneC.isChecked()) {
				strZoneC = "C";
				updateString();
				break;
			} else {
				strZoneC = "";
				updateString();
				break;
			}
		}
		
		case R.id.btn_send: {
			sendSMS(phoneNumber, stringToSend);							
			break;
			}
		}
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
	
	public void sendSMS(String phoneNumber, String completeStringToSend) {
		String SENT = "SMS SENT";

		PendingIntent sentPint = PendingIntent.getBroadcast(this, 0,
				new Intent(SENT), 0);

		registerReceiver(new BroadcastReceiver() {
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK: {
					Toast.makeText(getBaseContext(), "SMS SENT", Toast.LENGTH_SHORT).show();		
					finish();
					break;
				}
				
				case SmsManager.RESULT_ERROR_NO_SERVICE: {
					Toast.makeText(getBaseContext(), "Failed: No signal.", Toast.LENGTH_SHORT).show();
					break; 
				}
				
				case SmsManager.RESULT_ERROR_NULL_PDU: {
					Toast.makeText(getBaseContext(), "Failed: No PDU was provided.", Toast.LENGTH_SHORT).show();
					break;
				}
				case SmsManager.RESULT_ERROR_RADIO_OFF: {
					Toast.makeText(getBaseContext(), "Failed: Device radio is off.", Toast.LENGTH_SHORT).show();					
					break;
				}
			}
		}
		}, new IntentFilter(SENT));

		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(phoneNumber, null, completeStringToSend, sentPint, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
