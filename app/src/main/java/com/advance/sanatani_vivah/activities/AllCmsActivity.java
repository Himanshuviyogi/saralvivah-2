package com.advance.sanatani_vivah.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.Common;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AllCmsActivity extends AppCompatActivity {
    private TextView tv_about;
    private Common common;
    private RelativeLayout loader;
    private String cms_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cms);
        loader = findViewById(R.id.loader);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("CMS");
        toolbar.setNavigationOnClickListener(v -> finish());

        common = new Common(this);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey(AppConstants.KEY_INTENT)) {
                switch (b.getString(AppConstants.KEY_INTENT)) {
                    case "privacy":
                        getSupportActionBar().setTitle("Privacy Policy");
                        cms_name = "Privacy Policy";
                        break;
                    case "about":
                        getSupportActionBar().setTitle("About us");
                        cms_name = "About Us";
                        break;
                    case "refund":
                        getSupportActionBar().setTitle("Refund Policy");
                        cms_name = "Refund Policy";
                        break;
                    case "term":
                        getSupportActionBar().setTitle("Terms and Conditions");
                        cms_name = "Terms and Condition";
                        break;
                }
            }
        }

        tv_about = findViewById(R.id.tv_about);
        getCMS();
    }

    private void getCMS() {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("cms_name", cms_name);

        common.makePostRequest(AppConstants.all_cms, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("success")) {
                    JSONObject data = object.getJSONObject("data");
                    String page_content = data.getString("page_content");
                    tv_about.setText(Html.fromHtml(page_content));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> common.hideProgressRelativeLayout(loader));
    }
}
