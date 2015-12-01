package com.example.lucakoval.apposition;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.lucakoval.apposition.model.LocalDataHandler;

public class SearchFragment extends Fragment {
    private final String displaySearchFragmentTag = "displaySearchFragment";

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();

        return fragment;
    }

    public SearchFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_search, container, false);

        Button searchButton = (Button)fragmentView.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = ((EditText)fragmentView.findViewById(R.id.searchInput)).getText().toString();

                fragmentView.findViewById(R.id.searchContainer).setVisibility(View.INVISIBLE);

                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                ft.add(R.id.searchContainer, DisplaySearchFragment.newInstance(searchQuery, new DisplaySearchInterface() {
                    @Override
                    public void doneDisplaying() {
                        getChildFragmentManager().beginTransaction().remove(getChildFragmentManager().findFragmentByTag(displaySearchFragmentTag)).commit();
                        fragmentView.findViewById(R.id.searchContainer).setVisibility(View.VISIBLE);
                    }
                }), displaySearchFragmentTag).commit();

            }
        });

        return fragmentView;
    }
}
