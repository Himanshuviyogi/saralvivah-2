package com.advance.sanatani_vivah.activities;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.advance.sanatani_vivah.R;

public class SuccessActivity extends AppCompatActivity {
    private MaterialButton btnLogin;//,btn_partner;
    private String ragistered_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        btnLogin = findViewById(R.id.btnLogin);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("ragistered_id")) {
                ragistered_id = b.getString("ragistered_id");
            }
        }
        btnLogin.setOnClickListener(view -> {
            startActivity(new Intent(SuccessActivity.this, LoginActivity.class));
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SuccessActivity.this, LoginActivity.class));
        finish();
    }
}
