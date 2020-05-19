package ie.ul.studenttimetableul;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String STUDENT_ID = "ie.ul.studenttimetableul.STUDENT_ID";

    boolean valid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // remove data from all tables for now because data will be reentered everytime app is run
        TimetableDatabaseHelper mDBHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        //db.execSQL(TimetableDatabaseContract.SQL_DELETE_CLASSES_ENTRIES);
        db.execSQL(TimetableDatabaseContract.SQL_CREATE_CLASSES_ENTRIES);

        //Delete the assignments table to and create to update it!!
        //db.execSQL(TimetableDatabaseContract.SQL_DELETE_ASSIGNMENTS_ENTRIES);
        db.execSQL(TimetableDatabaseContract.SQL_CREATE_ASSIGNMENTS_ENTRIES);

        //db.execSQL(TimetableDatabaseContract.SQL_DELETE_MODULE_ENTRIES);
        db.execSQL(TimetableDatabaseContract.SQL_CREATE_MODULE_ENTRIES);

       // db.execSQL(TimetableDatabaseContract.SQL_DELETE_LOGIN_ENTRIES);
        db.execSQL(TimetableDatabaseContract.SQL_CREATE_LOGIN_ENTRIES);
        //db.execSQL("INSERT INTO Login VALUES (0)");

        //db.execSQL("delete from " + TimetableDatabaseContract.Module.TABLE_NAME);
        //db.execSQL("delete from " + TimetableDatabaseContract.Classes.TABLE_NAME);
        //db.execSQL("delete from " + TimetableDatabaseContract.Assignments.TABLE_NAME);

        db.close();

        int isLoggedInAlready = checkIfLoggedIn();
        if(isLoggedInAlready == 1)
        {
            Intent intent = new Intent(this, Nav.class);
            intent.putExtra("FRAGMENT_TO_SHOW", 0);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed()
    {
        finishAffinity();
    }

    /*
    Check is user has logged in.
     */
    private int checkIfLoggedIn()
    {
        int loggedIn;

        TimetableDatabaseHelper mDbHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                TimetableDatabaseContract.Login.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        loggedIn = cursor.getInt(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Login.TABLE_NAME_LOGGED_IN));
        cursor.close();
        db.close();
        return loggedIn;
    }

    /*
    Called when Go button is pressed
     */
    public void onClickBtn(View view)
    {
        TextView t = findViewById(R.id.tvID);
        final String studentID = t.getText().toString();

        if(!valid) {
            if (validateStudentID(studentID)) {
                if (connectedToTheInternet()) {
                    valid = true;
                    Intent intent = new Intent(this, LoadActivity.class);
                    intent.putExtra(STUDENT_ID, studentID);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Please connect to the Internet", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter a valid UL student ID", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateStudentID(String studentID)
    {
        boolean valid = true;

        if(studentID.length() < 7 || studentID.length() > 8)
            valid = false;
        if(studentID.contains("[^0-9]+"))
            valid = false;

        return valid;
    }

    /*
    Check if connected to the internet
     */
    public boolean connectedToTheInternet()
    {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        if(networkInfo != null && networkInfo.isConnectedOrConnecting())
            connected = true;
        return connected;
    }

}
