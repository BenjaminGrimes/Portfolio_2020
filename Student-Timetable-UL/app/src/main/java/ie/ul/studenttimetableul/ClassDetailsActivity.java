package ie.ul.studenttimetableul;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ClassDetailsActivity extends AppCompatActivity {

    static final String EXTRA_CLASS_ID = "ie.ul.studenttimetableul";

    ListView listView;
    ArrayAdapter<String> listViewAdapter;
    long cID;
    String itemModuleID = "";
    String itemType = "";
    String itemDay = "";
    String itemStartTime = "";
    String itemEndTime = "";
    String itemRoom = "";
    String itemInfo = "";
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);
        id = getIntent().getExtras().getLong(EXTRA_CLASS_ID);
        System.out.println(id);
        listView = findViewById(R.id.class_details_listview);
        cID = id;
        getClassData((int)id);

        // Enable the up button
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
        setContentView(R.layout.activity_class_details);

        if(!checkForDeletion()) {
            listView = findViewById(R.id.class_details_listview);
            cID = id;
            getClassData((int) id);
        }
        else
            finish();
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    /*
    Checks if the class has been deleted in the editClassDetailsActivity
    If it has been delete, returns to the moduleClassesActivity
     */
    private boolean checkForDeletion()
    {
        boolean deleted = false;
        //System.out.println("cID = " + cID);
        String selection = TimetableDatabaseContract.Classes._ID + " = ?";
        String [] selectionArgs = {cID+""};
        TimetableDatabaseHelper mDbHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                TimetableDatabaseContract.Classes.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if(cursor.getCount() <= 0)
            deleted = true;
        cursor.close();
        db.close();
        return deleted;
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

    private void getClassData(int id)
    {
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

        while(cursor.moveToNext())
        {
            itemModuleID = (cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_MODULE_ID)));
            itemType = (cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_TYPE)));
            itemDay = (cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_DAY)));
            itemStartTime = (cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_STARTTIME)));
            itemEndTime = (cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_ENDTIME)));
            itemRoom = (cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_ROOM)));
            itemInfo = (cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Classes.COLUMN_NAME_INFO)));
        }
        cursor.close();
        db.close();
        //System.out.println(itemModuleID + " " + itemType + " " + itemDay + " " + itemStartTime + " " + itemEndTime + " " + itemRoom + " " + itemInfo);
        displayDetails();
    }

    /*
    fill the list view with the class details.
     */
    private void displayDetails()
    {
        switch(itemType){
            case "LEC":
                itemType = "Lecture";
                break;
            case "TUT":
                itemType = "Tutorial";
                break;
            case "LAB":
                itemType = "Lab";
                break;
        }
        setTitle(itemModuleID + ": Class Details");
        String [] details = new String[5];
        details[0] = "Type: " + itemType;
        details[1] = "Day: " + itemDay;
        details[2] = "Time: " + itemStartTime + " - " + itemEndTime;
        details[3] = "Room: "+ itemRoom;
        details[4] = "Info: " + itemInfo;
        listViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, details);
        listView.setAdapter(listViewAdapter);
    }

    /*
    Send intent to the EditClassDetailsActivity
    Adds _ID as an extra
     */
    public void onClickEditClass(View view) {
        Intent intent = new Intent(this, EditClassDetailsActivity.class);
        intent.putExtra(EditClassDetailsActivity.EXTRA_CID, cID);
        startActivity(intent);
    }
}
