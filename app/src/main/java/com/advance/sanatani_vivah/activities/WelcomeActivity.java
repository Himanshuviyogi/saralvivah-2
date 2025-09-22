package com.advance.sanatani_vivah.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.AppConstants;

public class WelcomeActivity extends AppCompatActivity {

    private TextView btnLogin,btnRegister,btnWeddingVendor,btnContactUs,btnAboutUs,btnPrivacyPolicy;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnWeddingVendor = findViewById(R.id.btnWeddingVendor);
        btnContactUs = findViewById(R.id.btnContactUs);
        btnAboutUs = findViewById(R.id.btnAboutUs);
        btnPrivacyPolicy = findViewById(R.id.btnPrivacyPolicy);


        btnLogin.setOnClickListener(v-> startActivity(new Intent(this,LoginActivity.class)));
        btnRegister.setOnClickListener(v-> startActivity(new Intent(this,RegisterFirstActivity.class)));
        btnWeddingVendor.setOnClickListener(v-> startActivity(new Intent(this,FirstVendorCategoryListActivity.class)));
        btnContactUs.setOnClickListener(v-> startActivity(new Intent(this,ContactUsActivity.class)));
        btnAboutUs.setOnClickListener(v-> {
            Intent intent = new Intent(this, AllCmsActivity.class);
            intent.putExtra(AppConstants.KEY_INTENT, "about");
            startActivity(intent);
        });
        btnPrivacyPolicy.setOnClickListener(v-> {
            Intent intent1 = new Intent(this, AllCmsActivity.class);
            intent1.putExtra(AppConstants.KEY_INTENT, "privacy");
            startActivity(intent1);
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
            startActivity(intent);
            finish();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(getApplicationContext(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}