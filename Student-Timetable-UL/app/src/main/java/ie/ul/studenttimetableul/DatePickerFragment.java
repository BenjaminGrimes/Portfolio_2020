package ie.ul.studenttimetableul;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.Calendar;

/**
 * Created by John Long on 22/04/2018.
 */

public class DatePickerFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(),
                year, month, day);
    }
}
