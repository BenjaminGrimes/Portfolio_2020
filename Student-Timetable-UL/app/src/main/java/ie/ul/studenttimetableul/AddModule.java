package ie.ul.studenttimetableul;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;

import java.util.ArrayList;
import java.util.List;

public class AddModule extends AppCompatActivity {

    private boolean colorSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_module);

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

    /*
    Called when add button is pressed.
    Validates fields and shows Toast if any is incorrect
     */
    public void onClickAdd(View view) {
        EditText moduleID = findViewById(R.id.editText_addModule_moduleID);
        EditText moduleName = findViewById(R.id.editText_addModule_moduleName);
        System.out.println(moduleID.toString());
        if(moduleID.getText().toString().isEmpty())
            Toast.makeText(this, "Please enter a Module ID", Toast.LENGTH_SHORT).show();
        else if(moduleName.getText().toString().isEmpty())
            Toast.makeText(this, "Please enter a Module Name", Toast.LENGTH_SHORT).show();
        else if(!colorSelected)
            Toast.makeText(this, "Please select a Color", Toast.LENGTH_SHORT).show();
        else {
            if (!moduleAlreadyExists(moduleID.getText().toString())) {

                System.out.println("DOES NOT EXIST");
                //String [] colors = getResources().getStringArray(R.array.colors_for_modules);
                //String color = colors[0];
                Button bt = findViewById(R.id.btColor);
                ColorDrawable colorDrawable = (ColorDrawable) bt.getBackground();
                int co = colorDrawable.getColor();
                String color = String.format("#%06X", (0xFFFFFF & co));
                System.out.println("COLOR: " + color);
                addNewModuleToDatabase(moduleID.getText().toString(), moduleName.getText().toString(), color);

                finish();
            } else {
                moduleID.setText("");
                moduleName.setText("");
                Context context = getApplicationContext();
                String text = "Module Already Exists";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }

    public void onClickCancel(View view)
    {
        finish();
    }


    private void addNewModuleToDatabase(String moduleID, String moduleName, String color)
    {
        //System.out.println(moduleID + " = " + moduleName);
        TimetableDatabaseHelper mDbHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        mDbHelper.insertModule(db,moduleID, moduleName, color);
        //printDatabase();
    }

    /*
    Check if moduleID is already in DB
     */
    private boolean moduleAlreadyExists(String moduleID)
    {
        boolean exists = false;
        TimetableDatabaseHelper mDbHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = TimetableDatabaseContract.Module.COLUMN_NAME_MODULE_ID + " = ?";
        String [] selectionArgs = {moduleID};
        Cursor cursor = db.query(
                TimetableDatabaseContract.Module.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        List<String> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            result.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Module.COLUMN_NAME_MODULE_ID)));
        }
        cursor.close();
        db.close();
        System.out.println("RESULT = " + result);

        for(int i = 0; i < result.size() && !exists; i++)
        {
            if(moduleID.equalsIgnoreCase(result.get(i)))
                exists = true;
        }

        return exists;
    }

    public void printDatabase()
    {
        TimetableDatabaseHelper mDbHelper = new TimetableDatabaseHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                TimetableDatabaseContract.Module.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        List<Long> itemIDs = new ArrayList<>();
        List<String> itemModuleIDs = new ArrayList<>();
        List<String> itemModuleNames = new ArrayList<>();

        while (cursor.moveToNext())
        {
            itemIDs.add(cursor.getLong(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Module._ID)));
            itemModuleIDs.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Module.COLUMN_NAME_MODULE_ID)));
            itemModuleNames.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Module.COLUMN_NAME_MODULE_NAME)));
        }
        cursor.close();
        db.close();

        for(int i = 0; i < itemModuleIDs.size(); i++)
        {
            System.out.println("ID: " + itemIDs.get(i) + " Module ID: " + itemModuleIDs.get(i) + " Module Name: " + itemModuleNames.get(i));
        }
    }

    /*
    Called when color button is pressed.
    Calls showColorPicker
     */
    public void onClickColorPicker(View view)
    {
        showColorPicker(view);
    }

    public void showColorPicker(View view)
    {
        final ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
        colorPickerDialog.initialize(R.string.color_picker_default_title,
                new int[]{
                        getResources().getColor(R.color.Red500),
                        getResources().getColor(R.color.GreenA400),
                        getResources().getColor(R.color.Orange500),
                        getResources().getColor(R.color.DeepPurple300),
                        getResources().getColor(R.color.Blue500),
                        getResources().getColor(R.color.Yellow500),
                        getResources().getColor(R.color.Teal500),
                        getResources().getColor(R.color.Green500),
                        getResources().getColor(R.color.Red400),
                        getResources().getColor(R.color.LightBlue500),
                        getResources().getColor(R.color.Pink500),
                        getResources().getColor(R.color.Cyan500)
                }, getResources().getColor(R.color.colorPrimary), 3,2);

        colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                Button bt = findViewById(R.id.btColor);
                bt.setBackgroundColor(color);
                ColorDrawable colorDrawable = (ColorDrawable) bt.getBackground();
                int co = colorDrawable.getColor();
                String c = String.format("#%06X", (0xFFFFFF & co));
                System.out.println("COLOR: " + c);
                colorSelected = true;
            }
        });

        android.app.FragmentManager fm = this.getFragmentManager();
        colorPickerDialog.show(fm, "colorpicker");
    }
}
