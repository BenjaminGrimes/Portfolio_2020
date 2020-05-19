package ie.ul.studenttimetableul;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {

    View view;

    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        displaySettingsOptions();

        return view;
    }

    private void displaySettingsOptions()
    {
        String [] settingOptions = getResources().getStringArray(R.array.settings_string_array);
        ListView llSettings = view.findViewById(R.id.llSettings);
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, settingOptions);
        llSettings.setAdapter(adapter);

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    System.out.println("RESET ACCOUNT");
                    resetAccount();
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        llSettings.setOnItemClickListener(onItemClickListener);
    }

    /*
    Remove all details from DB
    Set logged in to 0
    Remove all alarms
     */
    private void resetAccount()
    {
        TimetableDatabaseHelper mDbHelper = new TimetableDatabaseHelper(view.getContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        mDbHelper.updateLogin(db, 0);
        Cursor cursor = db.query(
                TimetableDatabaseContract.Classes.TABLE_NAME,
                new String[]{TimetableDatabaseContract.Classes._ID},
                null,
                null,
                null,
                null,
                null
        );
        while(cursor.moveToNext())
        {
            int id = (int) (long) cursor.getLong(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes._ID));
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getContext(), AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), id, intent, 0);
            alarmManager.cancel(pendingIntent);

        }
        db.execSQL(TimetableDatabaseContract.SQL_DELETE_MODULE_ENTRIES);
        db.execSQL(TimetableDatabaseContract.SQL_DELETE_ASSIGNMENTS_ENTRIES);
        db.execSQL(TimetableDatabaseContract.SQL_DELETE_CLASSES_ENTRIES);
        cursor.close();
        db.close();
    }

}
