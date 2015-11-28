package com.example.lucakoval.apposition;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
        pager = (ViewPager)result.findViewById(R.id.scheduleContainer);
        pager.setAdapter(adapter);

        return result;
    }

    public class ScheduleAdapter extends FragmentPagerAdapter {

        public ScheduleAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScheduleFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0) return "Schedule 1";
            else return "Schedule 2";
        }
    }
}
