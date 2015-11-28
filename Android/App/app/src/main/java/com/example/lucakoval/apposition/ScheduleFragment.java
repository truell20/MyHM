package com.example.lucakoval.apposition;

import android.app.ActionBar;
import android.app.Activity;
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

import org.w3c.dom.Text;


public class ScheduleFragment extends Fragment {
    int dayNumber = -1;

    public static ScheduleFragment newInstance(int dayNumber) {
        ScheduleFragment fragment = new ScheduleFragment();
        fragment.dayNumber = dayNumber;
        return fragment;
    }

    public ScheduleFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_schedule, container, false);

        ((TextView)result.findViewById(R.id.dayLabel)).setText("Day " + dayNumber);

        // Populate LinearLayout with Periods
        LinearLayout layout = (LinearLayout)result.findViewById(R.id.linearLayout);
        for(int a = 0; a < 8; a++) {
            TextView period = new TextView(result.getContext());
            period.setText("Period " + a);

            period.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.12f));
            period.setGravity(Gravity.CENTER_HORIZONTAL);
            period.setBackgroundResource(R.drawable.back);

            layout.addView(period);
        }

        return result;
    }
}
