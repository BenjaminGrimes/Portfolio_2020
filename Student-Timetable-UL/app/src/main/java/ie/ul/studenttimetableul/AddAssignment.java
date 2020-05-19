package ie.ul.studenttimetableul;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

//TODO: Add DatePicker for assignment due date

public class AddAssignment extends AppCompatActivity {

    String itemModuleID = "";
    String itemToDo = "";
    String itemInfo = "";
    String itemDueDate ="";
    String itemTitle ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);

        Intent intent = getIntent();

        ActionBar ab = getSupportActionBar();
        if(ab != null)
        {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())        {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickCancel(View view)
    {
        finish();
    }

    private void addNewAssignmentToDatabase(String moduleId, String title, String toDo, String info, String dueDate)
    {
        TimetableDatabaseHelper asmntDBHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = asmntDBHelper.getWritableDatabase();
        asmntDBHelper.insertAssignment(db,moduleId,title,toDo,info,dueDate);
    }

    public void printDatbase(){
        TimetableDatabaseHelper asmntDbHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = asmntDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                TimetableDatabaseContract.Assignments.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext())
        {
            //itemID = cursor.getLong(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Assignments._ID));
            itemTitle =  cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Assignments.COLUMN_NAME_TITLE));
            itemModuleID = cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Assignments.COLUMN_NAME_MODULE_ID));
            itemToDo = cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Assignments.COLUMN_NAME_TODO));
            itemInfo = cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Assignments.COLUMN_NAME_INFO));
            itemDueDate = cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Assignments.COLUMN_NAME_DUEDATE));
        }

        cursor.close();
        db.close();
    }

    public void onClickAdd(View view) {
        //TODO: Add Data to database and then exit activity
        //TODO: Check if assignment title already exists
        EditText moduleId = findViewById(R.id.editText_addAssignment_moduleID);
        EditText assignmentTitle = findViewById(R.id.editText_addAssignment_assignmentTitle);
        EditText assignmentInfo = findViewById(R.id.editText_addAssignment_Info);
        EditText assignmentToDo = findViewById(R.id.editText_addAssignment_assignmentToDo);
        DatePicker assignmentDueDate = findViewById(R.id.dueDatePicker);

        addNewAssignmentToDatabase(moduleId.getText().toString(),
                assignmentTitle.getText().toString(),
                assignmentToDo.getText().toString(),
                assignmentInfo.getText().toString(),
                (Integer.toString(assignmentDueDate.getDayOfMonth())+ "/" + Integer.toString(assignmentDueDate.getMonth())
                + "/" + Integer.toString(assignmentDueDate.getYear())));

        finish();
    }
}
