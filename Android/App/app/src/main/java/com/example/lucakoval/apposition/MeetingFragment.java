package com.example.lucakoval.apposition;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lucakoval.apposition.model.LocalDataHandler;

import backend.Meeting;

interface MeetingCallback {
    void callback(Meeting meeting);
}

public class MeetingFragment extends Fragment {
    int dayIndex = -1;
    int periodIndex = -1;
    LocalDataHandler dataHandler;

    MeetingCallback callback;

    public static MeetingFragment newInstance(int dayNumber, int periodIndex, MeetingCallback callback) {
        MeetingFragment fragment = new MeetingFragment();
        fragment.dayIndex = dayNumber;
        fragment.periodIndex = periodIndex;
        return fragment;
    }

    public MeetingFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataHandler = new LocalDataHandler(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(R.layout.fragment_meeting, container, false);

        Button doneButton = (Button)fragmentView.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Meeting meeting = new Meeting();
                callback.callback(meeting);
            }
        });

        return fragmentView;
    }

}
