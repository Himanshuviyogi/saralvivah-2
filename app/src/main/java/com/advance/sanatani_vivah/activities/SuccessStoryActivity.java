package com.advance.sanatani_vivah.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.fragments.SuccessWeMetFragment;
import com.advance.sanatani_vivah.utility.SessionManager;

public class SuccessStoryActivity extends AppCompatActivity {

    private SessionManager session;
    private TextView btn_add_story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_story);

        btn_add_story = findViewById(R.id.btn_add_story);
        btn_add_story.setOnClickListener(v -> {
            startActivity(new Intent(SuccessStoryActivity.this, AddSuccessStoryActivity.class));
        });

        initialize();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void initialize() {
        setToolbar();
        session = new SessionManager(this);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.layout_container, new SuccessWeMetFragment()).commit();
    }
}