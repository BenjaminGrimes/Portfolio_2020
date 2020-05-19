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
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Modules extends Fragment {

    View view;
    ListView listView;
    ArrayAdapter<String> listViewAdapter;

    public Modules() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_modules, container, false);
        getModuleData();

        Button buttonAddModule = (Button) view.findViewById(R.id.add_module_button);
        buttonAddModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddModule.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        getModuleData();
        Button buttonAddModule = (Button) view.findViewById(R.id.add_module_button);
        buttonAddModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddModule.class);
                startActivity(intent);
            }
        });
    }


    /*
    Get all module details from DB
     */
    private void getModuleData()
    {
        TimetableDatabaseHelper mDbHelper = new TimetableDatabaseHelper(getActivity());
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

        List<String> moduleDetails = new ArrayList<>();
        while(cursor.moveToNext())
        {
            moduleDetails.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Module.COLUMN_NAME_MODULE_ID)) + " - " +
            cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Module.COLUMN_NAME_MODULE_NAME)));
        }
        cursor.close();
        db.close();

        //System.out.println(moduleDetails);

        displayModuleData(moduleDetails);
    }

    /*
    Fill listview with all module details
     */
    private void displayModuleData(List<String> moduleDetails)
    {
        listView = (ListView) view.findViewById(R.id.list_modules);
        listViewAdapter = new ArrayAdapter<>(getActivity(),
            android.R.layout.simple_list_item_1,
            moduleDetails);

        listView.setAdapter(listViewAdapter);

        AdapterView.OnItemClickListener onModuleClickedListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(view.getContext(), ModuleClassesActivity.class);
                String moduleID = listView.getItemAtPosition(position).toString();
                intent.putExtra(ModuleClassesActivity.EXTRA_ID, moduleID);
                startActivity(intent);
            }
        };
        listView.setOnItemClickListener(onModuleClickedListener);
    }
}
