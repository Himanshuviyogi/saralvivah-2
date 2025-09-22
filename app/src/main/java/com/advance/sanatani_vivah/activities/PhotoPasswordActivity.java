package com.advance.sanatani_vivah.activities;

import android.content.Intent;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.advance.sanatani_vivah.fragments.PhotoPasswordReceivedFragment;
import com.advance.sanatani_vivah.fragments.PhotoPasswordSentFragment;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class PhotoPasswordActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManager session=new SessionManager(this);
        if (!session.isLoggedIn()){
            startActivity(new Intent(this,LoginActivity.class));
            return;
        }
        setContentView(R.layout.activity_photo_password);

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Photo Request");
        toolbar.setNavigationOnClickListener(v -> finish());

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Bundle b=getIntent().getExtras();
        if (b!=null){
            if (b.containsKey("ppassword_tag")){
                if (b.getString("ppassword_tag").equals("receive")){
                    viewPager.setCurrentItem(1);
                }
            }
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle b = intent.getExtras();
        if (b != null) {
            if (b.containsKey("ppassword_tag")) {
                if (b.getString("ppassword_tag").equals("receive")) {
                    viewPager.setCurrentItem(1);
                }
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PhotoPasswordSentFragment(), "Sent");
        adapter.addFragment(new PhotoPasswordReceivedFragment(), "Received");
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
        finish();
    }
}