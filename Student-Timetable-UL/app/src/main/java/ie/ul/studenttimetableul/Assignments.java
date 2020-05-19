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
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Assignments extends Fragment {

    View view;
    ListView listView;
    ArrayAdapter<String> listViewAdapter;

    //TODO: Refresh Assignment fragments to display any updates


    public Assignments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_assignments, container, false);
        getAssignmentData();
        Button buttonAddAssignment = (Button) view.findViewById(R.id.add_assignment_button);
        buttonAddAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddAssignment.class);
                startActivity(intent);
            }
        });
        return view;
    }


    @Override
    public void onStart(){
        super.onStart();
        ListView lv = view.findViewById(R.id.listView_assignments);
        lv.setAdapter(null);
        getAssignmentData();

    }

    private void getAssignmentData()
    {
        TimetableDatabaseHelper asmntDbHelper = new TimetableDatabaseHelper(getActivity());
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


        List<String> assignmentDetails = new ArrayList<>();
        List<Long> itemIDs = new ArrayList<>();
        while(cursor.moveToNext())
        {
            assignmentDetails.add(cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Assignments.COLUMN_NAME_TITLE)) + " - " +
                    cursor.getString(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Assignments.COLUMN_NAME_MODULE_ID)));
            itemIDs.add(cursor.getLong(cursor.getColumnIndexOrThrow(TimetableDatabaseContract.Assignments._ID)));
        }

        cursor.close();
        db.close();

        System.out.println(assignmentDetails);
        displayAssignmentData(itemIDs,assignmentDetails);
    }


    private void displayAssignmentData(final List<Long> itemIDs,List<String> assignmentDetails){
        listView = (ListView) view.findViewById(R.id.listView_assignments);
        listViewAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1,
                assignmentDetails);


        AdapterView.OnItemClickListener onAssignmentClickedListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(view.getContext(), AssignmentDetailsActivity.class);
                intent.putExtra(ModuleClassesActivity.EXTRA_ID, itemIDs.get(position));
                startActivity(intent);
            }
        };
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(onAssignmentClickedListener);

    }





}
