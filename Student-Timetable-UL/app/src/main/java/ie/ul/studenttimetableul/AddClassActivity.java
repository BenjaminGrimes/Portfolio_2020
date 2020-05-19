package ie.ul.studenttimetableul;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddClassActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{


    TextView selectedStartTime, selectedEndTime;
    boolean selectStart = false;
    boolean selectEnd = false;
    String [] endTime = new String[2];
    String [] startTime = new String[2];
    EditText etroom;
    Spinner spinnerType, spinnerDay;
    String moduleID;
    EditText etInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        moduleID = getIntent().getExtras().getString("EXTRA_MODULE_ID");
        setTitle(moduleID + ": Add Class");

        etroom = findViewById(R.id.etRoom);
        etInfo = findViewById(R.id.etInfo);

        selectedStartTime = findViewById(R.id.tvEnteredStartTime);
        selectedEndTime = findViewById(R.id.tvEnteredEndTime);

        setTimePickers();
        setSpinners();

        ActionBar ab = getSupportActionBar();
        if(ab != null)
        {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setSpinners()
    {
        spinnerType = (Spinner) findViewById(R.id.spinnerTypeEdit);
        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(this, R.array.list_of_class_types, android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapterType);

        spinnerDay = (Spinner) findViewById(R.id.spinnerDay);
        ArrayAdapter<CharSequence> adapterDay = ArrayAdapter.createFromResource(this, R.array.list_of_days, android.R.layout.simple_spinner_item);
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(adapterDay);
    }

    public void setTimePickers()
    {
        selectedStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStart = true;
                selectEnd = false;
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        selectedEndTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                selectStart = false;
                selectEnd = true;
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        System.out.println(i + ":" + i1);
        if(selectStart)
        {
            startTime[0] = String.format(Locale.getDefault(),"%02d", i);
            startTime[1] = String.format(Locale.getDefault(),"%02d", i1);
            //System.out.println("START: " + startTime[0] + ":" + startTime[1]);
            String t = startTime[0] + ":" + startTime[1];
            selectedStartTime.setText(t);
        }
        if(selectEnd)
        {
            endTime[0] = String.format(Locale.getDefault(), "%02d", i);
            endTime[1] = String.format(Locale.getDefault(), "%02d", i1);
            //System.out.println("END: " + endTime[0] + ":" + endTime[1]);
            String t = endTime[0] + ":" + endTime[1];
            selectedEndTime.setText(t);
        }
    }


    /*
    Called when save button is pressed.
    If a field is not filled out a Toast is shown
     */
    public void onClickSaveClass(View view) {
        String room = etroom.getText().toString();
        String sTime = startTime[0] + ":" + startTime[1];
        String eTime = endTime[0] + ":" + endTime[1];
        String day = spinnerDay.getSelectedItem().toString();
        String type = spinnerType.getSelectedItem().toString();
        String info = etInfo.getText().toString();
        System.out.println(moduleID + " " + type + " " + day + " " + sTime + " " + eTime + " " + room + " " + info);

        if (startTime[0] == null)
            Toast.makeText(this, "Please select a Start Time", Toast.LENGTH_SHORT).show();
        else if(endTime[0] == null)
            Toast.makeText(this, "Please select an End Time", Toast.LENGTH_SHORT).show();
        else {
            int sh = Integer.parseInt(startTime[0]);
            int sm = Integer.parseInt(startTime[1]);
            int eh = Integer.parseInt(endTime[0]);
            int em = Integer.parseInt(endTime[1]);
            if(sh > eh)
                Toast.makeText(this, "Start Time must be before End Time", Toast.LENGTH_SHORT).show();
            else if(sh == eh && sm > em)
                Toast.makeText(this, "Start Time must be before End Time", Toast.LENGTH_SHORT).show();
            else if(checkForScheduledClass(sh, sm, eh, em, day))
                Toast.makeText(this, "Class already scheduled for this time", Toast.LENGTH_SHORT).show();
            else {

                if(room.isEmpty())
                    Toast.makeText(this, "Please enter a Room", Toast.LENGTH_SHORT).show();
                else {
                    switch (type) {
                        case "Lecture":
                            type = "LEC";
                            break;
                        case "Lab":
                            type = "LAB";
                            break;
                        case "Tutorial":
                            type = "TUT";
                            break;
                    }
                    //System.out.println(moduleID + " " + type + " " + day + " " + sTime + " " + eTime + " " + room + " " + info);

                    addClassToDatabase(moduleID, type, day, sTime, eTime, room, info);
                }
            }
        }
    }

    /*
    Check if class entered after pressing save overlaps with any class
     */
    private boolean checkForScheduledClass(int sh, int sm, int eh, int em, String day) {
        boolean classScheduled = false;
        Time sTime = new Time(sh, sm, 0);
        System.out.println(sTime);
        Time eTime = new Time(eh, em, 0);
        System.out.println(eTime);


        List<String> startTimes = new ArrayList<>();
        List<String> endTimes = new ArrayList<>();

        TimetableDatabaseHelper mDbHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                TimetableDatabaseContract.Classes.TABLE_NAME,
                new String[]{TimetableDatabaseContract.Classes.COLUMN_NAME_STARTTIME, TimetableDatabaseContract.Classes.COLUMN_NAME_ENDTIME},
                TimetableDatabaseContract.Classes.COLUMN_NAME_DAY + " = ? ",
                new String[]{day},
                null,
                null,
                null
        );

        while (cursor.moveToNext())
        {
            startTimes.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_STARTTIME)));
            endTimes.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_ENDTIME)));
        }
        cursor.close();
        db.close();

        for(int i = 0; i < startTimes.size() && !classScheduled; i++)
        {
            String els [] = startTimes.get(i).split(":");
            int sHour = Integer.parseInt(els[0]);
            int sMin = Integer.parseInt(els[1]);
            Time classStartTime = new Time(sHour, sMin, 0);
            els = endTimes.get(i).split(":");
            int eHour = Integer.parseInt(els[0]);
            int eMin = Integer.parseInt(els[1]);
            Time classEndTime = new Time(eHour, eMin, 0);

            System.out.println(classStartTime + "   " + classEndTime);

            if(sTime.equals(classStartTime))
                classScheduled = true;
            else if(eTime.equals(classEndTime))
                classScheduled = true;
            else if(sTime.after(classStartTime) && sTime.before(classEndTime) && eTime.after(classStartTime) && eTime.before(classEndTime))
                classScheduled = true;
            else if(sTime.before(classStartTime) && eTime.after(classStartTime) && eTime.before(classEndTime))
                classScheduled = true;
            else if(sTime.after(classStartTime) && sTime.before(classEndTime) && eTime.after(classEndTime))
                classScheduled = true;
        }
        return classScheduled;
    }

    /*
    Add the class to the database and set the alarm
     */
    private void addClassToDatabase(String moduleID, String type, String day, String sTime, String eTime, String room, String info) {
        TimetableDatabaseHelper mDbHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        mDbHelper.insertClass(db, moduleID, type, day, sTime, eTime, room, info);
        long id;
        Cursor cursor = db.query(
                TimetableDatabaseContract.Classes.TABLE_NAME,
                new String[]{TimetableDatabaseContract.Classes._ID},
                null,
                null,
                null,
                null,
                TimetableDatabaseContract.Classes._ID + " DESC ",
                "1"
        );
        cursor.moveToFirst();
        id = cursor.getLong(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes._ID));
        //System.out.println("ID == " + id);
        cursor.close();
        db.close();

        String title = moduleID + " " + type;
        String text = "Room: " + room + ", " + sTime + " - " + eTime;
        String [] sTEls = sTime.split(":");
        int hour = Integer.parseInt(sTEls[0]);
        int minute = Integer.parseInt(sTEls[1]);

        if(minute < 15) {
            minute = 60 - (15 - minute);
            if(hour == 0)
                hour = 23;
            else
                hour -= 1;
        }
        else
            minute -= 15;

        int d;
        if(day.equalsIgnoreCase("Monday"))
            d = 2;
        else if(day.equalsIgnoreCase("Tuesday"))
            d = 3;
        else if(day.equalsIgnoreCase("Wednesday"))
            d = 4;
        else if(day.equalsIgnoreCase("Thursday"))
            d = 5;
        else if(day.equalsIgnoreCase("Friday"))
            d = 6;
        else if(day.equalsIgnoreCase("Saturday"))
            d = 7;
        else
            d = 1;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, d);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        String [] extras = new String[2];
        extras[0] = title;
        extras[1] = text;

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("N_TITLE", title);
        intent.putExtra("N_TEXT", text);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int)id, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent);

        finish();
    }

    public void onClickCancelAddingClass(View view) {
        finish();
    }
}
