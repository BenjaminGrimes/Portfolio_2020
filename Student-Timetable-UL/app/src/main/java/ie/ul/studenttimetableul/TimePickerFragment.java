package ie.ul.studenttimetableul;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by my pc on 29/03/2018.
 */

public class TimePickerFragment extends DialogFragment {

    static final String TIME_PICKER_TIME = "ie.ul.studenttimetableul";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int hour, minute;
        if(getArguments() != null) {
            String details = getArguments().getString(TIME_PICKER_TIME);
            String[] els = details.split(":");
            hour = Integer.parseInt(els[0]);
            minute = Integer.parseInt(els[1]);
        }
        else
        {
            hour = 12;
            minute = 0;
        }
        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(),
                hour, minute, true);
    }
}
