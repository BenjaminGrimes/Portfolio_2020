package ie.ul.studenttimetableul;


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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    View view;
    ListView listView;


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        setTextViewForCurrentDay();
        getClassesForToday();
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        ListView lv = view.findViewById(R.id.listView_classes_for_day);
        lv.setAdapter(null);
        lv.setAdapter(null);
        setTextViewForCurrentDay();
        getClassesForToday();
    }

    private void setTextViewForCurrentDay() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String day = getCurrentDay();
        String date = dateFormat.format(currentDate);
        TextView dayDetails = (TextView) view.findViewById(R.id.tvCurrentDayDetails);
        dayDetails.setText(day +  " - " + date);
    }

    /*
    Get classes for the current day from the DB
     */
    private void getClassesForToday()
    {
        String day = getCurrentDay();
        TimetableDatabaseHelper mDbHelper = new TimetableDatabaseHelper(getActivity());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = TimetableDatabaseContract.Classes.COLUMN_NAME_DAY + " = ?";
        String [] selectionArgs = {day};
        Cursor cursor = db.query(
                TimetableDatabaseContract.Classes.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                TimetableDatabaseContract.Classes.COLUMN_NAME_STARTTIME + " ASC"
        );

        List<Long> itemIDs = new ArrayList<>();
        List<String> itemModuleIDs = new ArrayList<>();
        List<String> itemTypes = new ArrayList<>();
        List<String> itemDays = new ArrayList<>();
        List<String> itemStartTimes = new ArrayList<>();
        List<String> itemEndTimes = new ArrayList<>();
        List<String> itemRooms = new ArrayList<>();
        List<String> itemInfo = new ArrayList<>();
        while(cursor.moveToNext())
        {
            itemIDs.add(cursor.getLong(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes._ID)));
            itemModuleIDs.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_MODULE_ID)));
            itemTypes.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_TYPE)));
            itemDays.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_DAY)));
            itemStartTimes.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_STARTTIME)));
            itemEndTimes.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_ENDTIME)));
            itemRooms.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_ROOM)));
            itemInfo.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_INFO)));
        }

        cursor.close();
        db.close();

        List<String> itemModuleName = new ArrayList<>();
        selection = TimetableDatabaseContract.Module.COLUMN_NAME_MODULE_ID + " = ?";

        db = mDbHelper.getReadableDatabase();

        for(int i = 0; i < itemModuleIDs.size(); i++)
        {
            cursor = db.query(
                    TimetableDatabaseContract.Module.TABLE_NAME,
                    null,
                    selection,
                    new String[]{itemModuleIDs.get(i)},
                    null,
                    null,
                    null
            );
            while (cursor.moveToNext())
            {
                itemModuleName.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Module.COLUMN_NAME_MODULE_NAME)));
            }
        }
        cursor.close();
        db.close();

        displayClassDetails(itemIDs, itemModuleIDs, itemTypes, itemDays, itemStartTimes, itemEndTimes, itemRooms, itemInfo, itemModuleName);

    }

    /*
    Fill the list view with classes for current day
     */
    public void displayClassDetails(final List<Long> itemIDs, final List<String> itemModuleIDs, final List<String> itemTypes, List<String> itemDays, final List<String> itemStartTimes, final List<String> itemEndTimes, final List<String> itemRooms, List<String> itemInfo, final List<String> itemModuleName)
    {
        listView = (ListView) view.findViewById(R.id.listView_classes_for_day);
        if (itemIDs.isEmpty())
        {
            List<String> noClasses = new ArrayList<>();
            noClasses.add("No Classes Today");
            ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, android.R.id.text1, noClasses);
            listView.setAdapter(adapter);
        }
        else {
            ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, itemModuleIDs) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                    text1.setText(itemModuleIDs.get(position) + " " + itemModuleName.get(position));
                    text2.setText(itemTypes.get(position) + " " + itemStartTimes.get(position) + " - " + itemEndTimes.get(position) + " " + itemRooms.get(position));
                    return view;
                }
            };
            listView.setAdapter(adapter);

            AdapterView.OnItemClickListener onClassClickedListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(view.getContext(), ClassDetailsActivity.class);
                    intent.putExtra(ClassDetailsActivity.EXTRA_CLASS_ID, itemIDs.get(i));
                    startActivity(intent);
                }
            };
            listView.setOnItemClickListener(onClassClickedListener);


            ListAdapter listAdapter = listView.getAdapter();
            if(listAdapter == null)
                ;
            else
            {
                int height = 0;
                int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
                for(int i = 0; i < listAdapter.getCount(); i++)
                {
                    View listItem = listAdapter.getView(i, null, listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    height += listItem.getMeasuredHeight();
                }
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = height + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
                listView.setLayoutParams(params);
                listView.requestLayout();
            }

        }
    }

    private String getCurrentDay()
    {
        String day = "";
        Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DAY_OF_WEEK);
        //System.out.println("d = " + d);
        switch (d)
        {
            case 1:
                day = "Sunday";
                break;
            case 2:
                day = "Monday";
                break;
            case 3:
                day = "Tuesday";
                break;
            case 4:
                day = "Wednesday";
                break;
            case 5:
                day = "Thursday";
                break;
            case 6:
                day = "Friday";
                break;
            case 7:
                day = "Saturday";
                break;
        }
        //System.out.println("day = " + day);
        return day;
    }
}
