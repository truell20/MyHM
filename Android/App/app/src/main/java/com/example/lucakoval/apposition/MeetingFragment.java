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
import android.widget.Spinner;

import com.example.lucakoval.apposition.model.LocalDataHandler;

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

        // Populate period Spinner with period options
        String[] periodArray = new String[Period.numberOfPeriods];
        for(int a = 0; a < periodArray.length; a++) periodArray[a] = ""+(a+1);
        ArrayAdapter<String> periodAdapter = new ArrayAdapter<String>(fragmentView.getContext(), android.R.layout.simple_spinner_item, periodArray);
        Spinner periodSpinner = (Spinner) fragmentView.findViewById(R.id.periodOptions);
        periodSpinner.setAdapter(periodAdapter);

        // Set listener for the submit button
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
