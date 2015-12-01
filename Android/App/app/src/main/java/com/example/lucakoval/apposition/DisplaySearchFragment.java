package com.example.lucakoval.apposition;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

import backend.Backend;
import backend.BackendCallback;
import backend.UserData;

interface DisplaySearchInterface {
    void doneDisplaying();
}

public class DisplaySearchFragment extends Fragment {
    String searchQuery;
    ArrayList<String> stringList = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    DisplaySearchInterface callback;

    public static DisplaySearchFragment newInstance(String searchQuery, DisplaySearchInterface callback) {
        DisplaySearchFragment fragment = new DisplaySearchFragment();
        fragment.searchQuery = searchQuery;
        fragment.callback = callback;

        return fragment;
    }

    public DisplaySearchFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_display_search, container, false);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, stringList);
        ListView list = (ListView)fragmentView.findViewById(R.id.listSearchResults);
        list.setAdapter(adapter);

        Backend.searchForUsers(searchQuery, new BackendCallback<ArrayList<UserData>>() {
            @Override
            public void callback(ArrayList<UserData> result) {
                if(result == null) {
                    System.out.println("null");
                    return;
                }
                System.out.println("NOT");
                ListView list = (ListView)fragmentView.findViewById(R.id.listSearchResults);
                for(UserData user : result) {
                    System.out.println("once around");
                    stringList.add(user.firstName + " " + user.lastName);
                    adapter.notifyDataSetChanged();
                }
            };
        });

        return fragmentView;
    }
}
