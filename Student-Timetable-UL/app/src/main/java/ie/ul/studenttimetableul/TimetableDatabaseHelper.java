package ie.ul.studenttimetableul;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TimetableDatabaseHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "timetable";
    private static final int DB_VERSION = 1;

    public TimetableDatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(TimetableDatabaseContract.SQL_CREATE_MODULE_ENTRIES);
        db.execSQL(TimetableDatabaseContract.SQL_CREATE_CLASSES_ENTRIES);
        db.execSQL(TimetableDatabaseContract.SQL_CREATE_ASSIGNMENTS_ENTRIES);
        db.execSQL(TimetableDatabaseContract.SQL_CREATE_LOGIN_ENTRIES);
        db.execSQL("INSERT INTO Login VALUES (0)");
    }

    public void insertModule(SQLiteDatabase db, String moduleID, String moduleName, String color)
    {
        ContentValues cvModule = new ContentValues();
        //cvModule.put(TimetableDatabaseContract.Module._ID, ID);
        cvModule.put(TimetableDatabaseContract.Module.COLUMN_NAME_MODULE_ID, moduleID);
        cvModule.put(TimetableDatabaseContract.Module.COLUMN_NAME_MODULE_NAME, moduleName);
        cvModule.put(TimetableDatabaseContract.Module.COLUMN_NAME_MODULE_COLOR, color);
        db.insert(TimetableDatabaseContract.Module.TABLE_NAME, null, cvModule);
    }

    public void insertClass(SQLiteDatabase db, String moduleID, String type, String day, String startTime, String endTime, String room, String info)
    {
        ContentValues cvClass = new ContentValues();
        //cvClass.put(TimetableDatabaseContract.Classes._ID, ID);
        cvClass.put(TimetableDatabaseContract.Classes.COLUMN_NAME_MODULE_ID, moduleID);
        cvClass.put(TimetableDatabaseContract.Classes.COLUMN_NAME_TYPE, type);
        cvClass.put(TimetableDatabaseContract.Classes.COLUMN_NAME_DAY, day);
        cvClass.put(TimetableDatabaseContract.Classes.COLUMN_NAME_STARTTIME, startTime);
        cvClass.put(TimetableDatabaseContract.Classes.COLUMN_NAME_ENDTIME, endTime);
        cvClass.put(TimetableDatabaseContract.Classes.COLUMN_NAME_ROOM, room);
        cvClass.put(TimetableDatabaseContract.Classes.COLUMN_NAME_INFO, info);
        db.insert(TimetableDatabaseContract.Classes.TABLE_NAME, null, cvClass);
    }



    public void insertAssignment(SQLiteDatabase db, String moduleID, String title, String todo, String info, String dueDate)
    {
        ContentValues cvAssignment = new ContentValues();
        cvAssignment.put(TimetableDatabaseContract.Assignments.COLUMN_NAME_MODULE_ID, moduleID);
        cvAssignment.put(TimetableDatabaseContract.Assignments.COLUMN_NAME_TITLE, title);
        cvAssignment.put(TimetableDatabaseContract.Assignments.COLUMN_NAME_TODO, todo);
        cvAssignment.put(TimetableDatabaseContract.Assignments.COLUMN_NAME_INFO, info);
        cvAssignment.put(TimetableDatabaseContract.Assignments.COLUMN_NAME_DUEDATE, dueDate);
        db.insert(TimetableDatabaseContract.Assignments.TABLE_NAME, null, cvAssignment);
    }

    public void updateLogin(SQLiteDatabase db, int login)
    {
        String currentStatus;
        if(login == 1)
            currentStatus = "0";
        else
            currentStatus = "1";
        String selection = TimetableDatabaseContract.Login.TABLE_NAME_LOGGED_IN + " = ?";
        ContentValues cvLogin = new ContentValues();
        cvLogin.put(TimetableDatabaseContract.Login.TABLE_NAME_LOGGED_IN, login);
        db.update(TimetableDatabaseContract.Login.TABLE_NAME, cvLogin, selection, new String[]{currentStatus});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //TODO: implement onUpgrade
    }
}
