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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AssignmentDetailsActivity extends AppCompatActivity {
    static final String EXTRA_ID = "ie.ul.studenttimetableul";
    ListView listView;
    ArrayAdapter<String> listViewAdapter;

    long aID;
    String itemModuleID = "";
    String itemToDo = "";
    String itemInfo = "";
    String itemDueDate ="";
    String itemTitle ="";
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_details);
        id = getIntent().getExtras().getLong(EXTRA_ID);
        listView = findViewById(R.id.assignments_details_listview);
        aID = id;
        listView.setAdapter(null);
        getAssignmentDetailsData((int)id);
        ActionBar ab = getSupportActionBar();
        if(ab != null)
        {
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    protected void onRestart(){
        super.onRestart();
        setContentView(R.layout.activity_assignment_details);
        listView = findViewById(R.id.assignments_details_listview);

        aID = id;
        getAssignmentDetailsData((int)id);

        ActionBar ab = getSupportActionBar();
        if(ab != null)
        {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void getAssignmentDetailsData(int id) {
        TimetableDatabaseHelper assignmentDBHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db  = assignmentDBHelper.getWritableDatabase();
        String selection = TimetableDatabaseContract.Assignments._ID + " = ?";
        String [] args = {String.valueOf(id)};
        Cursor cursor = db.query(
                TimetableDatabaseContract.Assignments.TABLE_NAME,
                null,
                selection,
                args,
                null,
                null,
                null
        );

        while(cursor.moveToNext()){
            //itemID.add(cursor.getLong(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Assignments._ID)));
            itemTitle = cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Assignments.COLUMN_NAME_TITLE));
            itemModuleID = cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Assignments.COLUMN_NAME_MODULE_ID));
            itemToDo = cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Assignments.COLUMN_NAME_TODO));
            itemInfo = cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Assignments.COLUMN_NAME_INFO));
            itemDueDate = cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Assignments.COLUMN_NAME_DUEDATE));
        }

        cursor.close();
        db.close();

        displayAssignmentsDetailsData();


    }

    private void displayAssignmentsDetailsData() {
        String [] details  = new String[4];
        details[0] = "Assignment Title: " + itemModuleID;
        details[1] = "To Do: " + itemToDo;
        details[2] = "Info: " + itemInfo;
        details[3] = "Due Date: " + itemDueDate;
        setTitle(itemTitle);
        listViewAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,details);
        listView.setAdapter(listViewAdapter);
    }


    public void onClickEditAssignment(View view) {
        Intent intent = new Intent(this,EditAssignmentDetailsActivity.class);
        intent.putExtra(EditAssignmentDetailsActivity.EXTRA_AID, aID);
        startActivity(intent);
    }
}
