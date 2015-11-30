package com.example.lucakoval.apposition;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import backend.Backend;
import backend.BackendCallback;
import backend.Day;
import backend.Period;


public class ScheduleFragment extends Fragment {
    int dayIndex = -1;
    LocalDataHandler dataHandler;

    public static ScheduleFragment newInstance(int dayNumber) {
        ScheduleFragment fragment = new ScheduleFragment();
        fragment.dayIndex = dayNumber;
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

        Day day = dataHandler.getDay(dayIndex);
        System.out.println(day);
        if(day == null) {
            System.out.println("null");
            final int userID = dataHandler.getUserData().userID;
            Backend.getDay(dayIndex, userID, new BackendCallback<Day>() {
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


        return fragmentView;
    }

    public void setupSchedule(Day day, View view) {
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.linearLayout);
        for(int a = 0; a < day.getPeriods().size(); a++) {
            TextView textView = styleTextViewWithPeriod(view.getContext(), day.getPeriods().get(a));
            layout.addView(textView);
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
}
