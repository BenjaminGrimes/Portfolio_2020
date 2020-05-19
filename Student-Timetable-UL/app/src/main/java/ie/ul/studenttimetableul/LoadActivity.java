package ie.ul.studenttimetableul;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ie.ul.studenttimetableul.TimetableDatabaseContract.Classes.COLUMN_NAME_MODULE_ID;

public class LoadActivity extends AppCompatActivity {

    ProgressBar progressBar;
    boolean [] recievedAllResponse;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        Intent intent = getIntent();
        String studentID = intent.getStringExtra(MainActivity.STUDENT_ID);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        getTimetable(studentID);
    }

    /*
    Send request to timetable site and stores classes in the database
     */
    public void getTimetable(final String studentID)
    {
        RequestQueue myRequestQueue = Volley.newRequestQueue(this);
        String url = "https://www.timetable.ul.ie/tt2.asp";
        StringRequest myStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseTimetableHTML(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<>();
                MyData.put("T1", studentID);
                MyData.put("B1", "Submit");
                return MyData;
            }
        };
        myRequestQueue.add(myStringRequest);
    }

    public void parseTimetableHTML(String html)
    {
        Document doc = Jsoup.parse(html);
        Elements els = doc.select("table tbody tr");
        els = els.eq(1);

        String days [] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Elements [] allDays = new Elements[6];
        for(int i = 0; i < allDays.length; i++)
            allDays[i] = els.select("td").eq(i);

        ArrayList<ArrayList<String>> timetable = new ArrayList<>(6);
        for(int i = 0; i < allDays.length; i++)
        {
            ArrayList<String> a = new ArrayList<>();
            for(Element e : allDays[i].select("p")) {
                if(!e.text().isEmpty()) {
                    String details = e.text();
                    details = parseClassDetails(details);
                    a.add(details);
                }
            }
            timetable.add(a);
        }

        //add timetable details to db
        for(int i = 0; i < timetable.size(); i++)
        {
            addClassesToDatabase(timetable.get(i), days[i]);
        }
        // printClassesTable method, just used to if insertion of data went correctly
        //printClassesTable();
        getModuleInformation();
    }

    public String parseClassDetails(String details)
    {
        details = details.trim();
        details = details.replaceAll("\\s+", " ");
        ArrayList<String> elements = new ArrayList<>(Arrays.asList(details.split(" ")));
        for(int i = 0; i < elements.size(); )
        {
            if(elements.get(i).equalsIgnoreCase("-"))
                elements.remove(i);
            else
                i++;
        }
        for(int i = 0; i < elements.size(); )
        {
            if(elements.get(i).contains("Wks"))
                elements.remove(i);
            else
                i++;
        }
        for(int i = 0; i < elements.size(); i++)
        {
            if(elements.get(i).equalsIgnoreCase("LAB") || elements.get(i).equalsIgnoreCase("TUT"))
                elements.remove(i+1);
        }
        for(int i = 0; i < elements.size(); )
        {
            if(elements.get(i).isEmpty())
                elements.remove(i);
            else
                i++;
        }
        StringBuilder parsedDetails = new StringBuilder();
        for(int i = 0; i < elements.size(); i++)
        {
            if(i < elements.size() - 1)
                parsedDetails.append(elements.get(i)).append(" ");
            else
                parsedDetails.append(elements.get(i));
        }
        return parsedDetails.toString();
    }

    public void addClassesToDatabase(ArrayList<String> details, String day)
    {
        TimetableDatabaseHelper dbHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for(int i = 0; i < details.size(); i++)
        {
            String [] elements = details.get(i).split(" ");
            String startTime = elements[0];
            String endTime = elements[1];
            String moduleID = elements[2];
            String type = elements[3];
            String room = elements[4];
            dbHelper.insertClass(db, moduleID, type, day, startTime, endTime, room, "");
        }
        db.close();
    }
    public void printClassesTable()
    {
        TimetableDatabaseHelper dbHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                TimetableDatabaseContract.Classes.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        List<Long> itemsIDs = new ArrayList<>();
        List<String> itemModuleIDS = new ArrayList<>();
        List<String> itemTypes = new ArrayList<>();
        List<String> itemDays = new ArrayList<>();
        List<String> itemStartTimes = new ArrayList<>();
        List<String> itemEndTimes = new ArrayList<>();
        List<String> itemRooms = new ArrayList<>();
        List<String> itemInfo = new ArrayList<>();
        while(cursor.moveToNext())
        {
            itemsIDs.add(cursor.getLong(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes._ID)));
            itemModuleIDS.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_MODULE_ID)));
            itemTypes.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_TYPE)));
            itemDays.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_DAY)));
            itemStartTimes.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_STARTTIME)));
            itemEndTimes.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_ENDTIME)));
            itemRooms.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_ROOM)));
            itemInfo.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_INFO)));
        }
        cursor.close();
        for(int i = 0; i < itemsIDs.size(); i++)
        {
            System.out.println("ID: " + itemsIDs.get(i) + " Module ID: " + itemModuleIDS.get(i) +
                    " Type: " + itemTypes.get(i) + " DAY: " + itemDays.get(i) + " Start Time: " + itemStartTimes.get(i) +
                    " End Time: " + itemEndTimes.get(i) + " Room: " + itemRooms.get(i) + " Info: " + itemInfo.get(i));
        }
        db.close();
    }

    /*
    Get all moduleIDs
     */
    public void getModuleInformation()
    {
        TimetableDatabaseHelper dbHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(true, TimetableDatabaseContract.Classes.TABLE_NAME, new String[] {TimetableDatabaseContract.Classes.COLUMN_NAME_MODULE_ID}, null, null, null, null, null, null);

        List<String> moduleIDs = new ArrayList<>();
        while(cursor.moveToNext())
        {
            moduleIDs.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_MODULE_ID)));
        }
        cursor.close();
        //System.out.println(moduleIDs);

        //TODO: input module data in to Module table
        recievedAllResponse = new boolean[moduleIDs.size()];
        requestModuleInformation(moduleIDs);
    }

    /*
    Send request to module details url and get name for each module
     */
    public void requestModuleInformation(final List<String> moduleIDs)
    {
        for(int i = 0; i < moduleIDs.size(); i++) {
            RequestQueue myRequestQueue = Volley.newRequestQueue(this);
            String url = "https://www.timetable.ul.ie/tt_moduledetails_res.asp";
            final int finalI = i;
            StringRequest myStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    parseModuleDetailsHTML(response, moduleIDs.get(finalI));
                    recievedAllResponse[finalI] = true;
                    boolean ready = true;
                    for(int i = 0; i < recievedAllResponse.length && ready; i++)
                    {
                        if(recievedAllResponse[i])
                            ready = true;
                        else
                            ready = false;
                    }
                    if(ready)
                    {
                        gotoHomeActivity();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<>();
                    MyData.put("T1", moduleIDs.get(finalI));
                    MyData.put("B1", "Submit");
                    return  MyData;
                }
            };
            myRequestQueue.add(myStringRequest);
        }
    }
    public void parseModuleDetailsHTML(String html, String moduleID)
    {
        Document doc = Jsoup.parse(html);
        Elements els = doc.select("td b");
        String details = els.text();
        details = details.trim();
        details = details.replaceAll("\\s+", " ");
        String [] detailElements = details.split(" ");
        StringBuilder moduleName = new StringBuilder();
        for(int i = 5; i < detailElements.length; i++)
        {
            detailElements[i] = detailElements[i].substring(0,1).toUpperCase() + detailElements[i].substring(1).toLowerCase();
            if(i < detailElements.length - 1)
                moduleName.append(detailElements[i]).append(" ");
            else
                moduleName.append(detailElements[i]);
        }
        insertModuleDetailsIntoDatabase(moduleName.toString(), moduleID);
    }
    public void insertModuleDetailsIntoDatabase(String moduleName, String moduleID)
    {
        TimetableDatabaseHelper myHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = myHelper.getWritableDatabase();
        String [] colors = getResources().getStringArray(R.array.colors_for_modules);
        String color = colors[index];
        index++;
        myHelper.insertModule(db, moduleID, moduleName, color);
        db.close();
        //printModuleTable();
    }
    public void printModuleTable()
    {
        TimetableDatabaseHelper myHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.query(
                TimetableDatabaseContract.Module.TABLE_NAME,
                null, null, null, null, null, null);
        List<Long> id = new ArrayList<>();
        List<String> moduleid = new ArrayList<>();
        List<String> moduleName = new ArrayList<>();
        while (cursor.moveToNext())
        {
            id.add(cursor.getLong(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Module._ID)));
            moduleid.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Module.COLUMN_NAME_MODULE_ID)));
            moduleName.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Module.COLUMN_NAME_MODULE_NAME)));
        }
        cursor.close();
        for(int i = 0; i < id.size(); i++)
        {
            System.out.println("ID: " + id.get(i) + " Module ID: " + moduleid.get(i) +
            " Module Name: " + moduleName.get(i));
        }
        db.close();
    }

    public void gotoHomeActivity()
    {
        setAlarms();
        setLoggedIn();
        Intent intent = new Intent(this, Nav.class);
        intent.putExtra("FRAGMENT_TO_SHOW", 0);
        startActivity(intent);
    }

    /*
    Set alarm for each class
     */
    private void setAlarms()
    {
        TimetableDatabaseHelper mDbHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                TimetableDatabaseContract.Classes.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<Long> ids = new ArrayList<>();
        List<String> modIDs = new ArrayList<>();
        List<String> types = new ArrayList<>();
        List<String> days = new ArrayList<>();
        List<String> sTimes = new ArrayList<>();
        List<String> eTimes = new ArrayList<>();
        List<String> rooms = new ArrayList<>();
        //List<String> info = new ArrayList<>();
        while (cursor.moveToNext())
        {
            ids.add(cursor.getLong(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes._ID)));
            modIDs.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_MODULE_ID)));
            types.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_TYPE)));
            days.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_DAY)));
            sTimes.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_STARTTIME)));
            eTimes.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_ENDTIME)));
            rooms.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_ROOM)));
        }
        cursor.close();
        db.close();

        for(int i = 0; i < ids.size(); i++)
        {
            int id = (int) (long) ids.get(i);
            String [] sTEls = sTimes.get(i).split(":");
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



            System.out.println(id + " " + hour + " " + minute + " " + days.get(i) + " " + modIDs.get(i));
            int day;
            if(days.get(i).equalsIgnoreCase("Monday"))
                day = 2;
            else if(days.get(i).equalsIgnoreCase("Tuesday"))
                day = 3;
            else if(days.get(i).equalsIgnoreCase("Wednesday"))
                day = 4;
            else if(days.get(i).equalsIgnoreCase("Thursday"))
                day = 5;
            else if(days.get(i).equalsIgnoreCase("Friday"))
                day = 6;
            else if(days.get(i).equalsIgnoreCase("Saturday"))
                day = 7;
            else
                day = 1;
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_WEEK, day);
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);

            String title = modIDs.get(i) + " " + types.get(i);
            String text = "Room: " + rooms.get(i) + ", " + sTimes.get(i) + " - " + eTimes.get(i);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
            intent.putExtra("N_TITLE", title);
            intent.putExtra("N_TEXT", text);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent);
        }
    }

    /*
    Set logged in in DB.
     */
    private void setLoggedIn()
    {
        TimetableDatabaseHelper mDbHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        mDbHelper.updateLogin(db, 1);
        db.close();
    }
}
