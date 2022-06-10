package com.example.c196;

import static com.example.c196.R.id.assessmentToNotifySpinner;
import static com.example.c196.R.id.assessmentsInTermText;
import static com.example.c196.R.id.classToNotifySpinner;
import static com.example.c196.R.id.classesInTermText;
import static com.example.c196.R.id.scheduleUpcomingAssessmentNotification;
import static com.example.c196.R.id.scheduleUpcomingClassNotification;
import static com.example.c196.R.id.termRemainingText;
import static com.example.c196.R.id.termTitle;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    //Views
    private TextView term;
    private TextView timeRemaining;
    private TextView classes;
    private TextView assessments;

    private Button classNotify;
    private Button assessNotify;

    private Spinner classSpinner;
    private Spinner assessSpinner;

    //Database
    TermDataBase tdb;

    //Notification Channel ID
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private static final String default_notification_channel_id = "default";

    //Data for passin'
    List<AssessmentObj> assessmentList;
    List<CourseObj> courses;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        tdb = TermDataBase.getInstance(getContext());

        defineViews(root);
        fillTextViews();
        setupSpinners();
        setButtons();

        return root;
    }

    private void defineViews(View root) {
        term = root.findViewById(termTitle);
        timeRemaining = root.findViewById(termRemainingText);
        classes = root.findViewById(classesInTermText);
        assessments = root.findViewById(assessmentsInTermText);
        classNotify = root.findViewById(scheduleUpcomingClassNotification);
        assessNotify = root.findViewById(scheduleUpcomingAssessmentNotification);
        classSpinner = root.findViewById(classToNotifySpinner);
        assessSpinner = root.findViewById(assessmentToNotifySpinner);
    }

    private void fillTextViews() {
        List<TermObj> terms = tdb.getTerms();
        TermObj currentTerm = null;
        for (TermObj t : terms) {
            if (t.getId() > 0) {
                Date now = new Date();
                if (t.getStartDate().before(now) || t.getStartDate().equals(now)) {
                    if (t.getEndDate().equals(now) || t.getEndDate().after(now)) {
                        currentTerm = t;
                    }
                }
            }
        }
        if (currentTerm == null) {
            currentTerm = terms.get(0);
        }
        term.setText(currentTerm.getTitle());

        courses = tdb.getCourseByTermId(currentTerm.getId());
        assessmentList = new ArrayList<AssessmentObj>();
        StringBuilder courseText = new StringBuilder();
        for (CourseObj c : courses) {
            courseText.append(c.getId()).append(": ").append(c.getTitle()).append(" Starts: ")
                    .append(DateStringFormatter.getText(c.getStartDate(), true)).append(" Ends: ")
                    .append(DateStringFormatter.getText(c.getEndDate(), true)).append("\n");
            assessmentList.addAll(tdb.getAssessmentByCourseId(c.getId()));
        }
        classes.setText(courseText);

        StringBuilder assessmentText = new StringBuilder();
        for (AssessmentObj a : assessmentList) {
            assessmentText.append(a.getId()).append(": ").append(a.getTitle()).append(" Scheduled For: ")
                    .append(DateStringFormatter.getText(a.getStartDate(), true)).append("\n");
        }
        assessments.setText(assessmentText);
    }

    private void setupSpinners() {
        ArrayAdapter<CourseObj> adapter = new ArrayAdapter<CourseObj>(getContext(),
                android.R.layout.simple_spinner_item, courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);

        ArrayAdapter<AssessmentObj> assessAdapter = new ArrayAdapter<AssessmentObj>(getContext(),
                android.R.layout.simple_spinner_item, assessmentList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assessSpinner.setAdapter(assessAdapter);
    }

    private void setButtons() {
        classNotify.setOnClickListener(v -> {
            Date now = new Date();
            CourseObj c = (CourseObj) classSpinner.getSelectedItem();
            long diff = c.getStartDate().getTime();// - now.getTime();
            Notification not1 = getNotification("Class Upcoming:", c.getTitle() + /**" Starts on " + c.getStartDate() + */" diff " + diff);
            scheduleNotification(not1, diff);

            diff = c.getEndDate().getTime();// - now.getTime();
            Notification not2 = getNotification("Class Ending:", c.getTitle() + /**" Ends on " + c.getEndDate() +*/ " diff " + diff);
            scheduleNotification(not2, diff);
        });
        assessNotify.setOnClickListener(v -> {
            Date now = new Date();
            AssessmentObj a = (AssessmentObj) assessSpinner.getSelectedItem();
            long diff = a.getStartDate().getTime();// - now.getTime();
            Notification not1 = getNotification("Assessment Upcoming:", a.getTitle() + /**" Starts on " + a.getStartDate() + */" diff " + diff);
            scheduleNotification(not1, diff);

            diff = a.getEndDate().getTime();// - now.getTime();
            Notification not2 = getNotification("Assessment Upcoming:", a.getTitle() + /**" Ends on " + a.getEndDate() + */" diff " + diff);
            scheduleNotification(not2, diff);
        });
    }

    private void scheduleNotification(Notification notification, long delay) {
        Intent intent = new Intent(getContext(), NotificationPublisher.class);
        intent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        intent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pending = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        assert alarm != null;
        alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, delay, pending);
    }

    private Notification getNotification(String title, String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), default_notification_channel_id);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setSmallIcon(R.drawable.ic_notification_logo);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build();
    }
}