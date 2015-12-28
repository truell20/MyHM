package com.example.lucakoval.apposition;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lucakoval.apposition.model.LocalDataHandler;

import java.util.ArrayList;

import backend.Meeting;
import backend.Period;

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
        fragment.callback = callback;
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

        // Populate Spinner with location options
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.locationsArray, android.R.layout.simple_spinner_item);
        Spinner locationSpinner = (Spinner) fragmentView.findViewById(R.id.locationOptions);
        locationSpinner.setAdapter(locationAdapter);

        // Fill in meeting descriptor textview
        TextView meetingDescriptor = (TextView)fragmentView.findViewById(R.id.meetingDescriptor);
        meetingDescriptor.setText("Meeting during Period " + (periodIndex+1) + " and Day " + (dayIndex+1));

        // Set listener for the submit button
        Button doneButton = (Button)fragmentView.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Meeting meeting = getMeetingFromUI();
                callback.callback(meeting);
            }
        });

        return fragmentView;
    }

    private Meeting getMeetingFromUI() {
        Meeting meeting = new Meeting();

        meeting.name = ((EditText)getView().findViewById(R.id.nameInput)).getText().toString();
        meeting.dayIndex = dayIndex;
        meeting.periodIndex = periodIndex;
        meeting.memberIDs.add(dataHandler.getUserData().userID);

        return meeting;
    }

}
