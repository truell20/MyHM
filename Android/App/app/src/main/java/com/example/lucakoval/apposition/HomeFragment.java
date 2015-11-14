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

public class HomeFragment extends Fragment {
    private static final String EMAIL_KEY = "emailKey";

    private String email = null;

    public static HomeFragment newInstance(String email) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(EMAIL_KEY, email);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(EMAIL_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        TextView home = (TextView) fragmentView.findViewById(R.id.homeText);
        home.setText(email);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.locationsArray, android.R.layout.simple_spinner_item);
        Spinner spinner = (Spinner) fragmentView.findViewById(R.id.options);
        spinner.setAdapter(adapter);

        Button submit = (Button) fragmentView.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView locationText = (TextView) getView().findViewById(R.id.locationText);
                Spinner spinner = (Spinner) getView().findViewById(R.id.options);

                locationText.setText(spinner.getSelectedItem().toString());
            }
        });

        return fragmentView;
    }

}
