package com.example.c196;

import static com.example.c196.R.id.listItem;
import static com.example.c196.R.id.listLayout;
import static com.example.c196.R.id.termList;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TermListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TermListFragment extends Fragment implements RecyclerAdapter.ItemClickListener {
    private TermDataBase tdb;
    private RecyclerView listText;
    private RecyclerAdapter adapter;
    private String selected;

    final static Pattern firstInt = Pattern.compile("^[0-9]+");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TermListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TermListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TermListFragment newInstance(String param1, String param2) {
        TermListFragment fragment = new TermListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_term_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = view.getContext();

        //Get database instance
        tdb = TermDataBase.getInstance(context);

        //Try to display Terms list
        listText = view.findViewById(termList);
        listText.setLayoutManager(new LinearLayoutManager(context));
        displayTerms(view);
    }

    private void displayTerms(View view) {
        //Display a list of items
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        List<String> termText = new ArrayList<>();
        List<TermObj> terms = tdb.getTerms();
        for (TermObj t : terms) {
            termText.add(t.getId() + ": " + t.getTitle() + " Start: " + dateFormat.format(t.getStartDate())
                    + " End: " + dateFormat.format(t.getEndDate()) + "\n");
        }
        if (termText.size() == 0) {
            termText.add("No Terms Yet");
        } else {
            termText.remove("No Terms Yet");
        }
        adapter = new RecyclerAdapter(this.getContext(), termText, R.layout.list_layout, listItem);
        adapter.setClickListener(this);
        listText.setAdapter(adapter);
        listText.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
            Terms.setSelectedTerm(tdb.getTermById(Integer.parseInt(stringId)));
        }
    }
}