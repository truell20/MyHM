package com.example.lucakoval.apposition;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ScheduleHolderFragment extends Fragment {
    private ScheduleAdapter adapter;
    private ViewPager pager;

    public static ScheduleHolderFragment newInstance() {
        ScheduleHolderFragment fragment = new ScheduleHolderFragment();
        return fragment;
    }

    public ScheduleHolderFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragement_schedule_holder, container, false);

        adapter = new ScheduleAdapter(getChildFragmentManager());
        pager = (ViewPager)result.findViewById(R.id.schedulePager);
        pager.setAdapter(adapter);

        return result;
    }

    public class ScheduleAdapter extends FragmentPagerAdapter {

        public ScheduleAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScheduleFragment.newInstance(position, new ScheduleInterface() {
                @Override
                public void displayFragment(Fragment f, String tag) {
                    getView().findViewById(R.id.schedulePager).setVisibility(View.INVISIBLE);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.add(R.id.scheduleTabContainer, f, tag).commit();
                }

                @Override
                public void removeFragment(String tag) {
                    Fragment f = getFragmentManager().findFragmentByTag(tag);
                    if(f != null) getFragmentManager().beginTransaction().remove(f);

                    getView().findViewById(R.id.schedulePager).setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Day " + position;
        }
    }
}
