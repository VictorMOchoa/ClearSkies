package mytestapps.victor.clearskies.UserInterface;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import mytestapps.victor.clearskies.R;

/**
 * Created by Victor on 5/20/2015.
 */
public class AlertDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.error_alert_title))
                .setMessage(context.getString(R.string.alert_error_message))
                .setPositiveButton("Okay", null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
