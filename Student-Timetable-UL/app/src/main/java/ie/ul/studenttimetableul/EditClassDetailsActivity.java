package ie.ul.studenttimetableul;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
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

public class EditClassDetailsActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    static final String EXTRA_CID = "ie.ul.studenttimetableul";

    private boolean timeChanged = false;

    private Long id;
    private String itemModuleID;
    private String itemType;
    private String itemDay;
    private String itemStartTime;
    private String itemEndTime;
    private String itemRoom;
    private String itemInfo;

    private Spinner spinnerTypeEdit;
    private Spinner spinnerDayEdit;
    private EditText etRoomEdit;
    private TextView tvStartTimeEdit;
    private TextView tvEndTimeEdit;
    private EditText etInfoEdit;

    private boolean selectStart = false;
    private boolean selectEnd = false;

    private Time originalST, originalET;

    private String [] startTime = new String[2];
    private String [] endTime = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class_details);

        Intent intent = getIntent();
        id = intent.getExtras().getLong(EXTRA_CID);
        setDetails();

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

    /*
    Retrieve details of the class from the DB
     */
    public void setDetails()
    {
        //System.out.println("id:" + id);
        TimetableDatabaseHelper mDbHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = TimetableDatabaseContract.Classes._ID + " = ?";
        String [] args = {String.valueOf(id)};
        Cursor cursor = db.query(
                TimetableDatabaseContract.Classes.TABLE_NAME,
                null,
                selection,
                args,
                null,
                null,
                null
        );

        while (cursor.moveToNext())
        {
            itemModuleID = cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_MODULE_ID));
            itemType = cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_TYPE));
            itemDay = cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_DAY));
            itemStartTime = cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_STARTTIME));
            itemEndTime = cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_ENDTIME));
            itemRoom = cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_ROOM));
            itemInfo = cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_INFO));
        }

        String els [] = itemStartTime.split(":");
        originalST = new Time(Integer.parseInt(els[0]), Integer.parseInt(els[1]), 0);
        els = itemEndTime.split(":");
        originalET = new Time(Integer.parseInt(els[0]), Integer.parseInt(els[1]), 0);

        cursor.close();
        db.close();
        displayDetails();
    }

    /*
    set the fields of the activity to the stored values
     */
    private void displayDetails()
    {
        setSpinners();
        setTimePickers();
        etInfoEdit = (EditText) findViewById(R.id.etInfo);
        etInfoEdit.setText(itemInfo);
        etRoomEdit = (EditText) findViewById(R.id.etRoom);
        etRoomEdit.setText(itemRoom);
        tvStartTimeEdit = (TextView) findViewById(R.id.tvStartTimeEdit);
        tvStartTimeEdit.setText(itemStartTime);
        tvEndTimeEdit = (TextView) findViewById(R.id.tvEndTimeEdit);
        tvEndTimeEdit.setText(itemEndTime);
    }

    private void setSpinners()
    {
        int index = 0;
        spinnerTypeEdit = (Spinner) findViewById(R.id.spinnerTypeEdit);
        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(this, R.array.list_of_class_types, android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeEdit.setAdapter(adapterType);
        switch (itemType)
        {
            case "LEC":
                index = 0;
                break;
            case "TUT":
                index = 2;
                break;
            case "LAB":
                index = 1;
                break;
        }
        spinnerTypeEdit.setSelection(index);


        spinnerDayEdit = (Spinner) findViewById(R.id.spinnerDayEdit);
        ArrayAdapter<CharSequence> adapterDay = ArrayAdapter.createFromResource(this, R.array.list_of_days, android.R.layout.simple_spinner_item);
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDayEdit.setAdapter(adapterDay);
        index = 0;
        System.out.println("itemDay = " + itemDay);
        switch (itemDay){
            case "Monday":
                index = 0;
                break;
            case "Tuesday":
                index = 1;
                break;
            case "Wednesday":
                index = 2;
                break;
            case "Thursday":
                index = 3;
                break;
            case "Friday":
                index = 4;
                break;
            case "Saturday":
                index = 5;
                break;
            case "Sunday":
                index = 6;
        }
        spinnerDayEdit.setSelection(index);
    }

    private void setTimePickers()
    {
        tvStartTimeEdit = (TextView) findViewById(R.id.tvStartTimeEdit);
        tvStartTimeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStart = true;
                selectEnd = false;
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        tvEndTimeEdit = (TextView) findViewById(R.id.tvEndTimeEdit);
        tvEndTimeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectEnd = true;
                selectStart = false;
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
    }

    /*
    Called when save is pressed
    Validates the fields of the activity, displays a Toast if any are invalid
    Updates DB if all fields are valid
     */
    public void onClickSaveClass(View view)
    {
        String mID = itemModuleID;
        String type = spinnerTypeEdit.getSelectedItem().toString();
        String day = spinnerDayEdit.getSelectedItem().toString();
        String sTime = tvStartTimeEdit.getText().toString();
        String eTime = tvEndTimeEdit.getText().toString();
        String room = etRoomEdit.getText().toString();
        String info = etInfoEdit.getText().toString();
        String [] t = sTime.split(":");
        int sh = Integer.parseInt(t[0]);
        int sm = Integer.parseInt(t[1]);
        t = eTime.split(":");
        int eh = Integer.parseInt(t[0]);
        int em = Integer.parseInt(t[1]);
        if(sh > eh)
            Toast.makeText(this, "Start Time must be before End Time", Toast.LENGTH_SHORT).show();
        else if(sh == eh && sm >= em)
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
                    case "Tutorial":
                        type = "TUT";
                        break;
                    case "Lab":
                        type = "LAB";
                        break;
                }

                // save changes in db
                TimetableDatabaseHelper mDbHelper = new TimetableDatabaseHelper(this);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(TimetableDatabaseContract.Classes.COLUMN_NAME_TYPE, type);
                cv.put(TimetableDatabaseContract.Classes.COLUMN_NAME_DAY, day);
                cv.put(TimetableDatabaseContract.Classes.COLUMN_NAME_STARTTIME, sTime);
                cv.put(TimetableDatabaseContract.Classes.COLUMN_NAME_ENDTIME, eTime);
                cv.put(TimetableDatabaseContract.Classes.COLUMN_NAME_ROOM, room);
                cv.put(TimetableDatabaseContract.Classes.COLUMN_NAME_INFO, info);
                db.update(TimetableDatabaseContract.Classes.TABLE_NAME, cv, "_id=" + id, null);
                db.close();

                if (!itemStartTime.equalsIgnoreCase(sTime))
                    timeChanged = true;
                if (!itemEndTime.equalsIgnoreCase(eTime))
                    timeChanged = true;
                //Set new alarm if times changed
                if (timeChanged) {
                    /*AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(this, AlertReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) (long) id, intent, 0);
                    alarmManager.cancel(pendingIntent);*/

                    String title = mID + " " + type;
                    String text = "Room: " + room + ", " + sTime + " - " + eTime;

                    System.out.println("TITLE = " + title);
                    System.out.println("TEXT = " + text);

                    String[] sTEls = sTime.split(":");
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

                    System.out.println(id + " " + hour + " " + minute + " " + day + " " + mID);
                    int d;
                    if (day.equalsIgnoreCase("Monday"))
                        d = 2;
                    else if (day.equalsIgnoreCase("Tuesday"))
                        d = 3;
                    else if (day.equalsIgnoreCase("Wednesday"))
                        d = 4;
                    else if (day.equalsIgnoreCase("Thursday"))
                        d = 5;
                    else if (day.equalsIgnoreCase("Friday"))
                        d = 6;
                    else if (day.equalsIgnoreCase("Saturday"))
                        d = 7;
                    else
                        d = 1;
                    //System.out.println("N HOUR = " + hour + " N MIN = " + minute);
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.DAY_OF_WEEK, d);
                    c.set(Calendar.HOUR_OF_DAY, hour);
                    c.set(Calendar.MINUTE, minute);
                    c.set(Calendar.SECOND, 0);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(this, AlertReceiver.class);
                    intent.putExtra("N_TITLE", title);
                    intent.putExtra("N_TEXT", text);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) (long) id, intent, 0);
                    alarmManager.cancel(pendingIntent);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
                }

                finish();
            }
        }
    }

    public void onClickDelete(View view)
    {
        //Remove alarm
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) (long)id, intent,0);
        alarmManager.cancel(pendingIntent);
        // Delete entry from timetable.
        TimetableDatabaseHelper mDbHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + TimetableDatabaseContract.Classes.TABLE_NAME + " WHERE " + TimetableDatabaseContract.Classes._ID + " = " + id);
        db.close();
        finish();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        System.out.println("h: " + i + " m: "+ i1);
        if(selectStart)
        {
            startTime[0] = String.format(Locale.getDefault(), "%02d", i);
            startTime[1] = String.format(Locale.getDefault(), "%02d", i1);
            tvStartTimeEdit.setText(startTime[0] + ":" + startTime[1]);
        }
        if(selectEnd)
        {
            endTime[0] = String.format(Locale.getDefault(),"%02d", i);
            endTime[1] = String.format(Locale.getDefault(),"%02d", i1);
            tvEndTimeEdit.setText(endTime[0] + ":" + endTime[1]);
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

            if(!(originalST.equals(classStartTime) && originalET.equals(classEndTime))) {

                if (sTime.equals(classStartTime))
                    classScheduled = true;
                else if (eTime.equals(classEndTime))
                    classScheduled = true;
                else if (sTime.after(classStartTime) && sTime.before(classEndTime) && eTime.after(classStartTime) && eTime.before(classEndTime))
                    classScheduled = true;
                else if (sTime.before(classStartTime) && eTime.after(classStartTime) && eTime.before(classEndTime))
                    classScheduled = true;
                else if (sTime.after(classStartTime) && sTime.before(classEndTime) && eTime.after(classEndTime))
                    classScheduled = true;
            }
        }
        return classScheduled;
    }
}
