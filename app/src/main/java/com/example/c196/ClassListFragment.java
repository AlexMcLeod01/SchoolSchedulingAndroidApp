package com.example.c196;

import static com.example.c196.R.id.assessmentList;
import static com.example.c196.R.id.classList;
import static com.example.c196.R.id.listItem;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private TermDataBase tdb;
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private String selected;

    final static Pattern firstInt = Pattern.compile("^[0-9]+");

    public ClassListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment
     *
     * @return A new instance of fragment ClassListFragment.
     */
    public static ClassListFragment newInstance() {
        ClassListFragment fragment = new ClassListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        //Try to display Terms list
        recyclerView = view.findViewById(classList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        displayClasses(view, -1);
    }

    private void displayClasses(View view, int termId) {
        //Display a list of items
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        List<String> classText = new ArrayList<>();
        List<CourseObj> courses = tdb.getCourseByTermId(termId);
        for (CourseObj c : courses) {
            classText.add(c.getId() + ": " + c.getTitle() + " Start: " + dateFormat.format(c.getStartDate())
                    + " End: " + dateFormat.format(c.getEndDate()) + " Status: " + c.getStatus() + "\n");
        }
        if (classText.size() == 0) {
            classText.add("No Assessments Yet");
        } else {
            classText.remove("No Assessments Yet");
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