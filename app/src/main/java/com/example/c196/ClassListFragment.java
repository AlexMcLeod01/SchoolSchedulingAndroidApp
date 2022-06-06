package com.example.c196;

import static com.example.c196.R.id.assessmentList;
import static com.example.c196.R.id.classList;
import static com.example.c196.R.id.listItem;
import static com.example.c196.R.id.termSelectionSpinner;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassListFragment extends Fragment implements RecyclerAdapter.ItemClickListener {
    //Database
    private TermDataBase tdb;

    //Views
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;

    //Selection data
    private String selected;
    private static List<CourseObj> courses;

    final static Pattern firstInt = Pattern.compile("^[0-9]+");

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CLASS_LIST_PARAM = "param1";

    public ClassListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment
     *
     * @return A new instance of fragment ClassListFragment.
     */
    public static ClassListFragment newInstance(List<CourseObj> courseList) {
        ClassListFragment fragment = new ClassListFragment();
        Bundle args = new Bundle();
        args.putSerializable(CLASS_LIST_PARAM, (Serializable) courseList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courses = (List<CourseObj>) getArguments().getSerializable(CLASS_LIST_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = view.getContext();

        //Get database instance
        tdb = TermDataBase.getInstance(context);

        //Declare views
        recyclerView = view.findViewById(classList);

        //Try to display Course list
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        displayClasses();
    }

    private void displayClasses() {
        //Display a list of items
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        List<String> classText = new ArrayList<>();
        for (CourseObj c : courses) {
            classText.add(c.getId() + ": " + c.getTitle() + " Start: " + dateFormat.format(c.getStartDate())
                    + " End: " + dateFormat.format(c.getEndDate()) + " Status: " + c.getStatus() + "\n");
        }
        if (classText.size() == 0) {
            classText.add("No Classes Yet");
        } else {
            classText.remove("No Classes Yet");
        }
        adapter = new RecyclerAdapter(this.getContext(), classText, R.layout.list_layout, listItem);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView rv, int newState) {
                super.onScrollStateChanged(rv, newState);
            }
        });
    }

    /**
     * ItemClick implementation
     */
    @Override
    public void onItemClick(View view, int pos) {
        this.selected = adapter.getItem(pos);
        Matcher match = firstInt.matcher(this.selected);
        if (match.find()) {
            String stringId = match.group();
            Classes.setSelectedClass(tdb.getCourseById(Integer.parseInt(stringId)));
        }
    }
}