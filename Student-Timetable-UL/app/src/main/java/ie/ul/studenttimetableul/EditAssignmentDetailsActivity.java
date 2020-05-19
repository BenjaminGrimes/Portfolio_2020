package ie.ul.studenttimetableul;

import android.app.DialogFragment;
import android.content.ContentValues;
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
import android.widget.Spinner;

public class EditAssignmentDetailsActivity extends AppCompatActivity {

    static final String EXTRA_AID = "ie.ul.studenttimetableul";
    private Long id;
    private String itemAssignmentTitle;
    private String itemModuleID;
    private String itemToDo;
    private String itemInfo;
    private String itemDueDate;

    private EditText etAssignmentTitleEdit;
    private EditText etModuleIdEdit;
    private EditText etToDoEdit;
    private EditText etInfoEdit;
    private DatePicker etDueDateEdit;

    private boolean selectDueDate = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assignment_details);
        Intent intent = getIntent();
        id = intent.getExtras().getLong(EXTRA_AID);
        setAssignmentDetails();

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

    private void setAssignmentDetails() {
        TimetableDatabaseHelper aDbhelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = aDbhelper.getReadableDatabase();
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
            itemAssignmentTitle = cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Assignments.COLUMN_NAME_TITLE));
            itemModuleID = cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Assignments.COLUMN_NAME_MODULE_ID));
            itemToDo = cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Assignments.COLUMN_NAME_TODO));
            itemInfo = cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Assignments.COLUMN_NAME_INFO));
            itemDueDate = cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Assignments.COLUMN_NAME_DUEDATE));
        }
        cursor.close();
        db.close();
        //System.out.println("Title = " + itemAssignmentTitle);
        displayDetails();
    }

    private void displayDetails() {
        etAssignmentTitleEdit = (EditText) findViewById(R.id.editText_editAssignment_assignmentTitle);
        etAssignmentTitleEdit.setText(itemAssignmentTitle);
        etModuleIdEdit = (EditText) findViewById(R.id.editText_editAssignment_moduleID);
        etModuleIdEdit.setText(itemModuleID);
        etToDoEdit = (EditText) findViewById(R.id.editText_editAssignment_assignmentToDo);
        etToDoEdit.setText(itemToDo);
        etInfoEdit = (EditText) findViewById(R.id.editText_editAssignment_Info);
        etInfoEdit.setText(itemInfo);
        etDueDateEdit = findViewById(R.id.editdueDatePicker);
    }


    public void onClickSave(View view) {
       String aTitle = itemAssignmentTitle;
        String mID = itemModuleID;
        String toDo = itemToDo;
        String info = itemInfo;
        String dueDate = itemDueDate;

        TimetableDatabaseHelper aDbHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = aDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TimetableDatabaseContract.Assignments.COLUMN_NAME_TITLE, aTitle);
        cv.put(TimetableDatabaseContract.Assignments.COLUMN_NAME_MODULE_ID, mID);
        cv.put(TimetableDatabaseContract.Assignments.COLUMN_NAME_TODO, toDo);
        cv.put(TimetableDatabaseContract.Assignments.COLUMN_NAME_INFO, info);
        cv.put(TimetableDatabaseContract.Assignments.COLUMN_NAME_DUEDATE, dueDate);
        db.update(TimetableDatabaseContract.Assignments.TABLE_NAME, cv, "_id="+id, null);
        db.close();
        finish();
    }

    public void onClickCancel(View view) {
        finish();
    }

    public void onClickDeleteAssignment(View view) {
        TimetableDatabaseHelper mDbHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + TimetableDatabaseContract.Assignments.TABLE_NAME + " WHERE " + TimetableDatabaseContract.Assignments._ID + " = " + id);
        db.close();
        finish();
    }
}
