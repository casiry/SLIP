package slip.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ConfirmDialogFragment extends DialogFragment {

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		MainActivity caller = (MainActivity) this.getActivity();
		final MessageSender messageSender = new MessageSender(caller);
		
		final String phoneNumber = caller.getPhoneNumber();
		final String stringToSend = caller.getStringToSend();
		
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.confirmString)
               .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {                	   
                       messageSender.sendSMS(phoneNumber, stringToSend);
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       
                   }
               });      
        
        return builder.create();
    }
}


