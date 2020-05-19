package ie.ul.studenttimetableul;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Timetable extends Fragment {

    //TODO: update details after editing

    View view;
    private int h;
    private static String [] times = {"00:00", "01:00", "02:00", "03:00", "04:00", "05:00",
                                        "06:00", "07:00","08:00", "09:00", "10:00", "11:00",
                                        "12:00", "13:00", "14:00", "15:00", "16:00", "17:00",
                                        "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};


    public Timetable() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_timetable, container, false);
        h = convertDpToPixels(60);
        setRetainInstance(true);
        return view;
    }



    @Override
    public void onStart() {
            super.onStart();
            LinearLayout ll = view.findViewById(R.id.llTimes);
            ll.removeAllViews();
            ll = view.findViewById(R.id.llMon);
            ll.removeAllViews();
            ll = view.findViewById(R.id.llTue);
            ll.removeAllViews();
            ll = view.findViewById(R.id.llWed);
            ll.removeAllViews();
            ll = view.findViewById(R.id.llThu);
            ll.removeAllViews();
            ll = view.findViewById(R.id.llFri);
            ll.removeAllViews();
            ll = view.findViewById(R.id.llSat);
            ll.removeAllViews();
            ll = view.findViewById(R.id.llSun);
            ll.removeAllViews();
            createTimetable();
    }

    private void createTimetable()
    {
        setDates();
        setTimes();
        setMonday();
        setTuesday();
        setWednesday();
        setThursday();
        setFriday();
        setSaturday();
        setSunday();
        //setHorizontalLines();
    }

    private void setHorizontalLines()
    {

        /*int x = 60;
        int y = convertDpToPixels(1);
        LinearLayout llTimes = (LinearLayout) view.findViewById(R.id.llMon);
        for(int i = 0; i < 24; i++)
        {
            View line = new View(getActivity());
            line.setBackgroundColor(Color.BLACK);
            line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, y));
            //line.bringToFront();
            llTimes.addView(line);
            View space = new View(getActivity());
            space.setBackgroundColor(Color.TRANSPARENT);
            space.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, convertDpToPixels(x)));
            llTimes.addView(space);

        }*/

        /*Paint paint = new Paint();
        paint.setColor(Color.parseColor("#CD5C5C"));
        Bitmap bg = Bitmap.createBitmap(480, 800, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);
        canvas.drawRect(50, 50, 200, 200, paint);
        LinearLayout llL = (LinearLayout) view.findViewById(R.id.llL);
        llL.setBackground(new BitmapDrawable(bg));*/
    }

    /*
    Set the date at the top of the timetable
     */
    public void setDates()
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat formattedDate = new SimpleDateFormat("dd");
        int fDate = Integer.parseInt(formattedDate.format(c.getTime()));
        SimpleDateFormat formattedMonth = new SimpleDateFormat("MMM");
        String currentMonth = formattedMonth.format(c.getTime());

        TextView tv = view.findViewById(R.id.tvBlank);
        tv.setText(currentMonth);
        tv.setGravity(Gravity.CENTER);

        tv = view.findViewById(R.id.tvMon);
        tv.setText("MON\n" + String.format("%02d", fDate));
        tv.setGravity(Gravity.CENTER);
        fDate++;

        tv = view.findViewById(R.id.tvTue);
        tv.setText("TUE\n" + String.format("%02d", fDate));
        tv.setGravity(Gravity.CENTER);
        fDate++;

        tv = view.findViewById(R.id.tvWed);
        tv.setText("WED\n"+ String.format("%02d", fDate));
        tv.setGravity(Gravity.CENTER);
        fDate++;

        tv = view.findViewById(R.id.tvThu);
        tv.setText("THU\n" + String.format("%02d", fDate));
        tv.setGravity(Gravity.CENTER);
        fDate++;

        tv = view.findViewById(R.id.tvFri);
        tv.setText("FRI\n" + String.format("%02d", fDate));
        tv.setGravity(Gravity.CENTER);
        fDate++;

        tv = view.findViewById(R.id.tvSat);
        tv.setText("SAT\n" + String.format("%02d", fDate));
        tv.setGravity(Gravity.CENTER);
        fDate++;

        tv = view.findViewById(R.id.tvSun);
        tv.setText("SUN\n" + String.format("%02d", fDate));
        tv.setGravity(Gravity.CENTER);
        /*
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        System.out.println(c.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        String formattedDate = formatter.format(c.getTime());
        System.out.println("Date = " + formattedDate);

        SimpleDateFormat format1 = new SimpleDateFormat("MM");
        String formattedMonth = format1.format(c.getTime());
        System.out.println("Month = " + formattedMonth);
        int m = Integer.parseInt(formattedMonth);
        System.out.println("Month name = " + monthNames[m-1]);
        */
    }

    /*
    Set times on the left side of the timetable
     */
    public void setTimes()
    {
        LinearLayout llTimes = (LinearLayout) view.findViewById(R.id.llTimes);
        for(int i = 0; i < times.length; i++)
        {
            TextView t = new TextView(getActivity());
            t.setText(times[i]);
            t.setGravity(Gravity.CENTER);
            t.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h));
            llTimes.addView(t);
        }
    }


    public void setMonday()
    {
        LinearLayout llMon = view.findViewById(R.id.llMon);
        displayClassesForDayOnTimetable("Monday", llMon);
    }

    public void setTuesday()
    {
        LinearLayout llTue = view.findViewById(R.id.llTue);
        displayClassesForDayOnTimetable("Tuesday", llTue);
    }

    public void setWednesday()
    {
        LinearLayout llWed = view.findViewById(R.id.llWed);
        displayClassesForDayOnTimetable("Wednesday", llWed);
    }

    public void setThursday()
    {
        LinearLayout llThu = view.findViewById(R.id.llThu);
        displayClassesForDayOnTimetable("Thursday", llThu);
    }

    public void setFriday()
    {
        LinearLayout llFri = view.findViewById(R.id.llFri);
        displayClassesForDayOnTimetable("Friday", llFri);
    }

    public void setSaturday()
    {
        LinearLayout llSat = view.findViewById(R.id.llSat);
        displayClassesForDayOnTimetable("Saturday", llSat);
    }

    public void setSunday()
    {
        LinearLayout llSun = view.findViewById(R.id.llSun);
        displayClassesForDayOnTimetable("Sunday", llSun);
    }


    /*
    Fill the LinearLayout with the classes for the current day
     */
    private void displayClassesForDayOnTimetable(String currentDay, LinearLayout llDay)
    {
        ArrayList<String> classes = getClassesForDayFromDB(currentDay);

        int defaultHeight = convertDpToPixels(60);
        int currentHour = 0;
        int previousHour = 0;

        for(int index = 0; index < classes.size(); index++)
        {
            String elements [] = classes.get(index).split(" ");
            String id = elements[0];
            String moduleID = elements[1];
            String type = elements[2];
            String startTime = elements[3];
            String endTime = elements[4];
            String room = elements[5];
            String color = elements[6];

            elements = startTime.split(":");
            int startHour = Integer.parseInt(elements[0]);
            int startMinute = Integer.parseInt(elements[1]);

            elements = endTime.split(":");
            int endHour = Integer.parseInt(elements[0]);
            int endMinute = Integer.parseInt(elements[1]);

            while (currentHour < startHour)
            {
                System.out.println("HERE: " + currentHour + " " + startHour);
                if(currentHour < startHour)
                    currentHour++;
            }

            if(currentHour == startHour)
            {
                int height = convertDpToPixels(60*(currentHour - previousHour));
                TextView t = new TextView(getActivity());
                t.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
                //t.setBackgroundColor(Color.BLUE);
                llDay.addView(t);

                if(startMinute == 0)
                {
                    if(endMinute == 0)
                    {
                        int dif = endHour - startHour;
                        height = convertDpToPixels(60*dif);

                        final TextView tv = new TextView(getActivity());
                        tv.setId(Integer.parseInt(id));
                        tv.setText(moduleID+"\n"+type+"\n"+room);
                        tv.setBackgroundColor(Color.parseColor(color));
                        tv.setGravity(Gravity.CENTER);
                        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                System.out.println(tv.getId());

                                Intent intent = new Intent(view.getContext(), ClassDetailsActivity.class);
                                intent.putExtra(ClassDetailsActivity.EXTRA_CLASS_ID, (long) tv.getId());
                                startActivity(intent);
                            }
                        });
                        llDay.addView(tv);
                        previousHour = endHour;
                    }
                    else if(endMinute > 0)
                    {
                        int dif = endHour - startHour;
                        int var = 60 - endMinute;
                        height = convertDpToPixels((60*dif)+endMinute);
                        final TextView tv = new TextView(getActivity());
                        tv.setId(Integer.parseInt(id));
                        tv.setBackgroundColor(Color.parseColor(color));
                        tv.setText(moduleID+"\n"+type+"\n"+room);
                        tv.setGravity(Gravity.CENTER);
                        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                System.out.println(tv.getId());

                                Intent intent = new Intent(view.getContext(), ClassDetailsActivity.class);
                                intent.putExtra(ClassDetailsActivity.EXTRA_CLASS_ID, (long) tv.getId());
                                startActivity(intent);
                            }
                        });
                        llDay.addView(tv);
                        previousHour = endHour;

                        TextView tv1 = new TextView(getActivity());
                        //tv1.setBackgroundColor(Color.RED);
                        height = convertDpToPixels(var);
                        tv1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
                        llDay.addView(tv1);

                    }
                }
                else if(startMinute > 0)
                {
                    if(endMinute == 0)
                    {
                        int dif = endHour - startHour;
                        TextView tv = new TextView(getActivity());
                        height = convertDpToPixels(startMinute);
                        //tv.setBackgroundColor(Color.YELLOW);
                        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
                        llDay.addView(tv);

                        height = convertDpToPixels((60*dif)-startMinute);
                        final TextView tv1 = new TextView(getActivity());
                        tv1.setId(Integer.parseInt(id));
                        tv1.setText(moduleID+"\n"+type+"\n"+room);
                        tv1.setBackgroundColor(Color.parseColor(color));
                        tv1.setGravity(Gravity.CENTER);
                        tv1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
                        tv1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                System.out.println(tv1.getId());

                                Intent intent = new Intent(view.getContext(), ClassDetailsActivity.class);
                                intent.putExtra(ClassDetailsActivity.EXTRA_CLASS_ID, (long) tv1.getId());
                                startActivity(intent);
                            }
                        });
                        llDay.addView(tv1);
                        previousHour = endHour;
                    }
                    else if(endMinute > 0)
                    {
                        int dif1 = endHour - startHour;
                        TextView tv = new TextView(getActivity());
                        height = convertDpToPixels(startMinute);
                        //tv.setBackgroundColor(Color.YELLOW);
                        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
                        llDay.addView(tv);

                        int var = 60 - endMinute;
                        height = convertDpToPixels(((60*dif1)-startMinute)+endMinute);
                        final TextView tv1 = new TextView(getActivity());
                        tv1.setId(Integer.parseInt(id));
                        tv1.setBackgroundColor(Color.parseColor(color));
                        tv1.setText(moduleID+"\n"+type+"\n"+room);
                        tv1.setGravity(Gravity.CENTER);
                        tv1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
                        tv1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                System.out.println(tv1.getId());

                                Intent intent = new Intent(view.getContext(), ClassDetailsActivity.class);
                                intent.putExtra(ClassDetailsActivity.EXTRA_CLASS_ID, (long)tv1.getId());
                                startActivity(intent);
                            }
                        });
                        llDay.addView(tv1);
                        previousHour = endHour;

                        TextView tv3 = new TextView(getActivity());
                        //tv3.setBackgroundColor(Color.RED);
                        height = convertDpToPixels(var);
                        tv3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
                        llDay.addView(tv3);
                    }
                }
            }

        }
    }

    private int convertDpToPixels(int dp)
    {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, view.getResources().getDisplayMetrics());
        System.out.println("px = " + px);
        return  px;
    }

    /*
    Get the classes for the current day from DB
     */
    private ArrayList<String> getClassesForDayFromDB(String day)
    {
        ArrayList<String> classes = new ArrayList<>();

        TimetableDatabaseHelper mDbHelper = new TimetableDatabaseHelper(view.getContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String [] columns = {TimetableDatabaseContract.Classes._ID,
                TimetableDatabaseContract.Classes.COLUMN_NAME_MODULE_ID,
                TimetableDatabaseContract.Classes.COLUMN_NAME_TYPE,
                TimetableDatabaseContract.Classes.COLUMN_NAME_STARTTIME,
                TimetableDatabaseContract.Classes.COLUMN_NAME_ENDTIME,
                TimetableDatabaseContract.Classes.COLUMN_NAME_ROOM};
        String selection = TimetableDatabaseContract.Classes.COLUMN_NAME_DAY + " = ?";
        Cursor cursor = db.query(
                TimetableDatabaseContract.Classes.TABLE_NAME,
                columns,
                selection,
                new String[]{day},
                null,
                null,
                TimetableDatabaseContract.Classes.COLUMN_NAME_STARTTIME + " ASC"
        );

        List<String> modules = new ArrayList<>();
        while(cursor.moveToNext())
        {
            classes.add(cursor.getLong(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes._ID)) + " "
                    + cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_MODULE_ID)) + " "
                    + cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_TYPE)) + " "
                    + cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_STARTTIME)) + " "
                    + cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_ENDTIME)) + " "
                    + cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_ROOM)));

            modules.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_MODULE_ID)));
        }
        cursor.close();
        db.close();

        System.out.println(classes);

        ArrayList<String> colors = new ArrayList<>();

        for(int i = 0; i < modules.size(); i++)
        {
            selection = TimetableDatabaseContract.Module.COLUMN_NAME_MODULE_ID + " = ?";
            db = mDbHelper.getReadableDatabase();
            cursor = db.query(
                    TimetableDatabaseContract.Module.TABLE_NAME,
                    null,
                    selection,
                    new String[]{modules.get(i)},
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            colors.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Module.COLUMN_NAME_MODULE_COLOR)));
        }

        for(int i = 0; i < classes.size(); i++)
            classes.set(i, classes.get(i) + " " + colors.get(i));

        return classes;
    }

}
