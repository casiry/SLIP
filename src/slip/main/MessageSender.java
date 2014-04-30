package slip.main;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;

public class MessageSender {

	Activity activity;
	Context baseContext;
	
	public MessageSender(Activity activity) {
		this.activity = activity;
		baseContext = activity.getBaseContext();
	}
	
	public void sendSMS(String phoneNumber, String completeStringToSend) {
		String SENT = "SMS SENT";

		PendingIntent sentPint = PendingIntent.getBroadcast(activity, 0,
				new Intent(SENT), 0);

		activity.registerReceiver(new BroadcastReceiver() {
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK: {
					Toast.makeText(baseContext, "SMS SENT", Toast.LENGTH_SHORT).show();
					break;					
				}
				
				case SmsManager.RESULT_ERROR_NO_SERVICE: {
					Toast.makeText(baseContext, "Failed: No signal.", Toast.LENGTH_SHORT).show();
					break; 
				}
				
				case SmsManager.RESULT_ERROR_NULL_PDU: {
					Toast.makeText(baseContext, "Failed: No PDU was provided.", Toast.LENGTH_SHORT).show();
					break;
				}
				case SmsManager.RESULT_ERROR_RADIO_OFF: {
					Toast.makeText(baseContext, "Failed: Device radio is off.", Toast.LENGTH_SHORT).show();					
					break;
				}
			}
		}
		}, new IntentFilter(SENT));

		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(phoneNumber, null, completeStringToSend, sentPint, null);
	}
}
