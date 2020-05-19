package ie.ul.studenttimetableul;

import android.provider.BaseColumns;

public final class TimetableDatabaseContract{

    public TimetableDatabaseContract(){}

    public static class Module implements BaseColumns
    {
        static final String TABLE_NAME = "Modules";
        //public static final String COLUMN_NAME_MID = "MID";
        static final String COLUMN_NAME_MODULE_ID = "ID";
        static final String COLUMN_NAME_MODULE_NAME = "Name";
        static final String COLUMN_NAME_MODULE_COLOR = "Color";
    }

    public static final String SQL_CREATE_MODULE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + Module.TABLE_NAME + " (" +
                    Module._ID + " INTEGER PRIMARY KEY," +
                    Module.COLUMN_NAME_MODULE_ID + " TEXT," +
                    Module.COLUMN_NAME_MODULE_NAME + " TEXT,"+
                    Module.COLUMN_NAME_MODULE_COLOR + " TEXT)";

    public static final String SQL_DELETE_MODULE_ENTRIES =
            "DROP TABLE IF EXISTS " + Module.TABLE_NAME;

    public static class Classes implements BaseColumns
    {
        static final String TABLE_NAME = "Classes";
        //static final String COLUMN_NAME_ID = "ID";
        static final String COLUMN_NAME_MODULE_ID = "ModuleID";
        static final String COLUMN_NAME_TYPE = "Type";
        static final String COLUMN_NAME_DAY = "Day";
        static final String COLUMN_NAME_STARTTIME = "StartTime";
        static final String COLUMN_NAME_ENDTIME = "EndTime";
        static final String COLUMN_NAME_ROOM = "Room";
        static final String COLUMN_NAME_INFO = "Info";
    }

    public static final String SQL_CREATE_CLASSES_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + Classes.TABLE_NAME + " (" +
                    Classes._ID + " INTEGER PRIMARY KEY," +
                    Classes.COLUMN_NAME_MODULE_ID + " TEXT," +
                    Classes.COLUMN_NAME_TYPE + " TEXT," +
                    Classes.COLUMN_NAME_DAY + " TEXT," +
                    Classes.COLUMN_NAME_STARTTIME + " TEXT," +
                    Classes.COLUMN_NAME_ENDTIME + " TEXT," +
                    Classes.COLUMN_NAME_ROOM + " TEXT," +
                    Classes.COLUMN_NAME_INFO + " TEXT)";

    public static final String SQL_DELETE_CLASSES_ENTRIES =
            "DROP TABLE IF EXISTS " + Classes.TABLE_NAME;

    public static class Assignments implements BaseColumns
    {
        static final String TABLE_NAME = "Assignments";
        //public static final String COLUMN_NAME_ID = "ID";
        static final String COLUMN_NAME_MODULE_ID = "ModuleID";
        static final String COLUMN_NAME_TITLE = "Title";
        static final String COLUMN_NAME_TODO = "ToDo";
        static final String COLUMN_NAME_INFO = "Info";
        static final String COLUMN_NAME_DUEDATE = "DueDate";
    }

    public static final String SQL_CREATE_ASSIGNMENTS_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + Assignments.TABLE_NAME + " (" +
                    Assignments._ID + " INTEGER PRIMARY KEY," +
                    Assignments.COLUMN_NAME_MODULE_ID + " TEXT," +
                    Assignments.COLUMN_NAME_TITLE + " TEXT," +
                    Assignments.COLUMN_NAME_TODO + " TEXT," +
                    Assignments.COLUMN_NAME_INFO + " TEXT," +
                    Assignments.COLUMN_NAME_DUEDATE + " TEXT)";

    public static final String SQL_DELETE_ASSIGNMENTS_ENTRIES =
            "DROP TABLE IF EXISTS " + Assignments.TABLE_NAME;



    public static class Login implements  BaseColumns
    {
        static final String TABLE_NAME = "Login";
        static final String TABLE_NAME_LOGGED_IN = "LoggedIn";
    }

    public static final String SQL_CREATE_LOGIN_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + Login.TABLE_NAME + " (" + Login.TABLE_NAME_LOGGED_IN + " INTEGER DEFAULT 0)";

    public static final String SQL_DELETE_LOGIN_ENTRIES = "DROP TABLE IF EXISTS " + Login.TABLE_NAME;
}
