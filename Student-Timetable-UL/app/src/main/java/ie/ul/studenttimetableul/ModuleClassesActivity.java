package ie.ul.studenttimetableul;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ModuleClassesActivity extends AppCompatActivity {

    static final String EXTRA_ID = "ie.ul.studenttimetableul";
    ListView listView;
    String moduleID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_classes);

        String moduleDetails = getIntent().getExtras().get(EXTRA_ID).toString();
        String [] moduleElements = moduleDetails.split(" ");
        moduleID = moduleElements[0];
        StringBuilder sb = new StringBuilder();
        for(int i = 2; i < moduleElements.length; i++)
            sb.append(moduleElements[i]).append(" ");

        setTitle(moduleID + ": " + sb.toString());

        getModuleClassesData(moduleID);

        ActionBar ab = getSupportActionBar();
        if(ab != null)
        {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        setContentView(R.layout.activity_module_classes);

        getModuleClassesData(moduleID);

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
    Get all classes for module
     */
    private void getModuleClassesData(String moduleID)
    {
        TimetableDatabaseHelper mDbHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = TimetableDatabaseContract.Classes.COLUMN_NAME_MODULE_ID + " = ?";
        String [] selectionArgs = {moduleID};
        Cursor cursor = db.query(
                TimetableDatabaseContract.Classes.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        List<Long> itemIDs = new ArrayList<>();
        List<String> itemModuleIDs = new ArrayList<>();
        List<String> itemTypes = new ArrayList<>();
        List<String> itemDays = new ArrayList<>();
        List<String> itemStartTimes = new ArrayList<>();
        List<String> itemEndTimes = new ArrayList<>();
        List<String> itemRooms = new ArrayList<>();
        List<String> itemInfos = new ArrayList<>();
        while (cursor.moveToNext())
        {
            itemIDs.add(cursor.getLong(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes._ID)));
            itemModuleIDs.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_MODULE_ID)));
            itemTypes.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_TYPE)));
            itemDays.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_DAY)));
            itemStartTimes.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_STARTTIME)));
            itemEndTimes.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_ENDTIME)));
            itemRooms.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_ROOM)));
            itemInfos.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_INFO)));
        }
        cursor.close();
        db.close();

        /*
        System.out.println(itemIDs);
        System.out.println(itemModuleIDs);
        System.out.println(itemTypes);
        System.out.println(itemDays);
        System.out.println(itemStartTimes);
        System.out.println(itemEndTimes);
        System.out.println(itemRooms);
        System.out.println(itemInfos);
        */

        displayModuleClassesDetails(itemIDs, itemModuleIDs, itemTypes, itemDays, itemStartTimes, itemEndTimes, itemRooms, itemInfos);
    }

    /*
    Fill listview with class details
     */
    private void displayModuleClassesDetails(final List<Long> itemIDs, List<String> itemModuleIDs, final List<String> itemTypes, final List<String> itemDays, final List<String> itemStartTimes, final List<String> itemEndTimes, final List<String> itemRooms, List<String> itemInfos)
    {
        listView = (ListView) findViewById(R.id.module_classes_listview);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, itemModuleIDs){
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text1.setText(itemTypes.get(position) + " " + itemRooms.get(position));
                text2.setText(itemDays.get(position) + " " + itemStartTimes.get(position) + " - " + itemEndTimes.get(position));
                return view;
            }
        };
        listView.setAdapter(adapter);

        AdapterView.OnItemClickListener onClassClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                System.out.println("id " + id + " p " + position + " i " + itemIDs.get(position));
                Intent intent = new Intent(view.getContext(), ClassDetailsActivity.class);
                intent.putExtra(ClassDetailsActivity.EXTRA_CLASS_ID, itemIDs.get(position));
                startActivity(intent);
            }
        };
        listView.setOnItemClickListener(onClassClickListener);
    }


    public void onClickAddClass(View view) {
        Intent intent = new Intent(this, AddClassActivity.class);
        intent.putExtra("EXTRA_MODULE_ID", moduleID);
        startActivity(intent);
    }


}
