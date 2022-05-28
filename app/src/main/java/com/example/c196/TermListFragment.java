package com.example.c196;

import static com.example.c196.R.id.termList;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TermListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TermListFragment extends Fragment {
    private TermDataBase tdb;
    private TextView listText;

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

        //Get database instance
        tdb = TermDataBase.getInstance(view.getContext());

        //Try to display Terms list
        listText = view.findViewById(termList);
        displayTerms();
    }

    private void displayTerms() {
        //Display a list of items
        StringBuilder termText = new StringBuilder();
        List<TermObj> terms = tdb.getTerms();
        for (TermObj t : terms) {
            termText.append(t.getId() + " " + t.getTitle() + " " + t.getStartDate() + " " + t.getEndDate() + "\n");
        }
        if (termText.length() == 0) {
            termText.append("No Terms Yet");
        }
        listText.setText(termText);
    }
}