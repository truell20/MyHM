package com.example.lucakoval.apposition;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.lucakoval.apposition.model.LocalDataHandler;

import org.w3c.dom.Text;

import java.util.ArrayList;

import backend.*;

interface ScheduleInterface {
    void displayFragment(Fragment f, String tag);
    void removeFragment(String tag);
}

public class ScheduleFragment extends Fragment {
    int dayIndex = -1;
    LocalDataHandler dataHandler;
    ScheduleInterface callbackInterface;

    public static ScheduleFragment newInstance(int dayNumber, ScheduleInterface callbackInterface) {
        ScheduleFragment fragment = new ScheduleFragment();
        fragment.dayIndex = dayNumber;
        fragment.callbackInterface = callbackInterface;

        return fragment;
    }

    public ScheduleFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataHandler = new LocalDataHandler(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(R.layout.fragment_schedule, container, false);

        ((TextView)fragmentView.findViewById(R.id.dayLabel)).setText("Day " + (dayIndex+1));

        // Get the day assigned to this schedule. If it is not stored locally,
        // Then get it from remote backend and store it locally.
        Day day = dataHandler.getDay(dayIndex);
        System.out.println(day);
        if(day == null) {
            Backend.getDay(dayIndex, dataHandler.getUserData().userID, new BackendCallback<Day>() {
                @Override
                public void callback(Day result) {
                    // Populate LinearLayout with Periods
                    System.out.println(result.getPeriods().size());

                    setupSchedule(result, fragmentView);
                    dataHandler.setDay(dayIndex, result);
                }
            });
        } else {
            System.out.println("not null");
            setupSchedule(day, fragmentView);
        }

        // Setup meetings
        Backend.getMeetings(dataHandler.getUserData().userID, dayIndex, new BackendCallback<ArrayList<Meeting>>() {
            @Override
            public void callback(ArrayList<Meeting> result) {
                if(result != null) {
                    for(Meeting meeting : result){
                        System.out.println("Meeting");
                        System.out.println(meeting.name);
                        addMeetingToUI(meeting);
                    }
                }
            }
        });

        return fragmentView;
    }

    public void setupSchedule(Day day, View view) {
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.linearLayout);
        for(int a = 0; a < day.getPeriods().size(); a++) {
            final int periodIndex = a;
            TextView periodView = styleTextViewWithPeriod(view.getContext(), day.getPeriods().get(a));
            periodView.setId(periodViewID(a));
            // When text view of period is clicked,
            // open new activity or fragment that lets you set up a meeting.
            periodView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MeetingFragment meetingFragment = MeetingFragment.newInstance(dayIndex, periodIndex, new MeetingCallback() {
                        @Override
                        public void callback(Meeting meeting) {
                            Backend.addMeeting(meeting);
                            callbackInterface.removeFragment("meeting");
                        }
                    });
                    callbackInterface.displayFragment(meetingFragment, "meeting");
                }
            });
            layout.addView(periodView);
        }
    }

    public TextView styleTextViewWithPeriod(Context c, Period period) {
        TextView textView = new TextView(c);
        textView.setText(period.name);
        textView.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0.12f));
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setBackgroundResource(R.drawable.back);

        return textView;
    }

    public void addMeetingToUI(Meeting meeting) {
        TextView periodView = (TextView)getView().findViewById(periodViewID(meeting.periodIndex));
        periodView.setText(periodView.getText()+"\nMEETING: " + meeting.name);
    }

    public int periodViewID(int periodIndex) {
        return getActivity().getResources().getIdentifier("periodView" + periodIndex, "id", getActivity().getPackageName());
    }
}
