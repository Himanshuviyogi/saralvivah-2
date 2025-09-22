package com.advance.sanatani_vivah.activities;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.fragments.ProfileIViewedFragment;
import com.advance.sanatani_vivah.fragments.ViewedMyProfileFragment;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.Common;

import java.util.ArrayList;
import java.util.List;

public class ViewedProfileActivity extends AppCompatActivity {

    private Common common;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewed_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Viewed Profile");
        toolbar.setNavigationOnClickListener(v -> finish());

        common = new Common(this);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey(AppConstants.KEY_INTENT)) {
                if (b.getString(AppConstants.KEY_INTENT).equals("i_viewed")) {
                    viewPager.setCurrentItem(0);
                } else if (b.getString(AppConstants.KEY_INTENT).equals("my_profile")) {
                    viewPager.setCurrentItem(1);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        MyApplication.getInstance().cancelPendingRequests("req");
        super.onBackPressed();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProfileIViewedFragment(), "Profile I Viewed");
        adapter.addFragment(new ViewedMyProfileFragment(), "Viewed My Profile");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
