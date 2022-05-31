package com.example.c196;

import static com.example.c196.R.id.assessmentList;
import static com.example.c196.R.id.listItem;
import static com.example.c196.R.id.termList;

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
 * Use the {@link AssessmentListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssessmentListFragment extends Fragment implements RecyclerAdapter.ItemClickListener {
    private TermDataBase tdb;
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private String selected;

    final static Pattern firstInt = Pattern.compile("^[0-9]+");


    public AssessmentListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AssessmentListFragment.
     */
    public static AssessmentListFragment newInstance() {
        AssessmentListFragment fragment = new AssessmentListFragment();
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
        return inflater.inflate(R.layout.fragment_assessment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = view.getContext();

        //Get database instance
        tdb = TermDataBase.getInstance(context);

        //Try to display Terms list
        recyclerView = view.findViewById(assessmentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        displayAssessments(view);
    }

    private void displayAssessments(View view) {
        //Display a list of items
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        List<String> assessmentText = new ArrayList<>();
        List<AssessmentObj> assessments = tdb.getAssessments();
        for (AssessmentObj a : assessments) {
            String perf;
            if (a.isPerformance()) {
                perf = "Performance Assessment";
            } else {
                perf = "Objective Assessment";
            }
            assessmentText.add(a.getId() + ": " + a.getTitle() + " Start: " + dateFormat.format(a.getStartDate())
                    + " End: " + dateFormat.format(a.getEndDate()) + " Type: " + perf + "\n");
        }
        if (assessmentText.size() == 0) {
            assessmentText.add("No Assessments Yet");
        } else {
            assessmentText.remove("No Assessments Yet");
        }
        adapter = new RecyclerAdapter(this.getContext(), assessmentText, R.layout.list_layout, listItem);
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
            Assessments.setSelectedAssessment(tdb.getAssessmentById(Integer.parseInt(stringId)));
        }
    }

    //Todo Alerts for all start and end dates for each assessment
}