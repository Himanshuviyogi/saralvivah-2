package com.advance.sanatani_vivah.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.fragments.OnlineMembersChatFragment;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ChatActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private BroadcastReceiver receiver, receiverForStatus;
    private IntentFilter mIntentFilter = null, intentFilterForStatus = null;

    private OnlineMembersChatFragment onlineMembersChatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chat");
        toolbar.setNavigationOnClickListener(v -> finish());

        setupViewPager();

        this.mIntentFilter = new IntentFilter(AppConstants.MESSAGE_LIST_UPDATE);
        this.intentFilterForStatus = new IntentFilter(AppConstants.STATUS_UPDATE);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                AppDebugLog.print("in onReceive of ChatActivity");
                if (onlineMembersChatFragment != null) onlineMembersChatFragment.reloadData();
            }
        };
        receiverForStatus = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (onlineMembersChatFragment != null) onlineMembersChatFragment.reloadData();
            }
        };
    }

    private void setupViewPager() {
        onlineMembersChatFragment = new OnlineMembersChatFragment();
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Online Members");
                            break;
                    }
                }).attach();
    }

    class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return onlineMembersChatFragment;
                default:
                    return new OnlineMembersChatFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 1;
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, this.mIntentFilter);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverForStatus, this.intentFilterForStatus);
    }

    @Override public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverForStatus);
    }
}