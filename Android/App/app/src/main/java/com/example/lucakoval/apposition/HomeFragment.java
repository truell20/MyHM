package com.example.lucakoval.apposition;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lucakoval.apposition.model.LocalDataHandler;

import backend.Backend;
import backend.UserData;

public class HomeFragment extends Fragment {
    private LocalDataHandler dataHandler;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataHandler = new LocalDataHandler(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        // Set the title text to the person's email
        TextView home = (TextView) fragmentView.findViewById(R.id.homeText);
        home.setText(dataHandler.getUserData().email);

        // Setup the menu for selecting locations
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.locationsArray, android.R.layout.simple_spinner_item);
        Spinner spinner = (Spinner) fragmentView.findViewById(R.id.options);
        spinner.setAdapter(adapter);

        // Set the current location
        String currentLocation = dataHandler.getUserData().currentLocation;
        TextView locationText = (TextView) fragmentView.findViewById(R.id.locationText);
        locationText.setText(currentLocation);
        spinner.setSelection(((ArrayAdapter<CharSequence>) spinner.getAdapter()).getPosition(currentLocation));

        // Setup submit button listener
        Button submit = (Button) fragmentView.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Spinner spinner = (Spinner) getView().findViewById(R.id.options);
                setLocation(spinner.getSelectedItem().toString());
            }
        });

        return fragmentView;
    }

    public void setLocation(String location) {
        // Set a text view to the new location
        TextView locationText = (TextView) getView().findViewById(R.id.locationText);
        locationText.setText(location);

        // Set spinner to location
        Spinner spinner = (Spinner) getView().findViewById(R.id.options);
        spinner.setSelection(((ArrayAdapter<CharSequence>)spinner.getAdapter()).getPosition(location));

        // Update remote and local UserData with new location
        UserData currentUserData = dataHandler.getUserData();
        currentUserData.currentLocation = location;
        dataHandler.setUserData(currentUserData);
        Backend.setUserData(currentUserData);
    }

}
