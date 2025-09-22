package com.advance.sanatani_vivah.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.retrofit.AppApiService;
import com.advance.sanatani_vivah.retrofit.RetrofitClient;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddReviewActivity extends AppCompatActivity {

    private SessionManager session;
    private RelativeLayout loader;

    private Common common;

    private String selectedVendorId;

    private Button btnSubmit;
    private RatingBar ratingBar;
    private EditText txtName, txtEmail, txtTitleReview, txtReviewDesc;

    //Retrofit related
    private Retrofit retrofit;
    private AppApiService appApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        initialize();
    }

    private void initialize() {
        setToolbar();

        session = new SessionManager(this);
        retrofit = RetrofitClient.getClient();
        appApiService = retrofit.create(AppApiService.class);
        common = new Common(this);

        ratingBar = findViewById(R.id.ratingBar);

        loader = findViewById(R.id.loader);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtTitleReview = findViewById(R.id.txtTitleReview);
        txtReviewDesc = findViewById(R.id.txtReviewDesc);
        btnSubmit = findViewById(R.id.btnSubmit);

        selectedVendorId = getIntent().getStringExtra("vendor_id");

        btnSubmit.setOnClickListener(view -> {
            sendReviewRequest();
        });
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Write Review");

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void sendReviewRequest() {
        boolean isValid = true;
        if (txtName.getText().length() == 0) {
            isValid = false;
            txtName.setError("Please enter your name");
        }
        if (txtReviewDesc.getText().length() == 0) {
            isValid = false;
            txtReviewDesc.setError("Please enter review description");
        }
        if (txtTitleReview.getText().length() == 0) {
            isValid = false;
            txtTitleReview.setError("Please enter review title");
        }
        if (txtEmail.getText().length() == 0) {
            isValid = false;
            txtEmail.setError("Please enter email");
        } else {
            if (!common.isValidEmail(txtEmail.getText().toString())) {
                txtEmail.setError("Please enter valid email");
                isValid = false;
            }
        }
        if (ratingBar.getRating() <= 0) {
            isValid = false;
            Toast.makeText(this, "Please give your rating", Toast.LENGTH_SHORT).show();
        }

        if (isValid) {
            common.showProgressRelativeLayout(loader);

            Map<String, String> params = new HashMap<>();
            params.put("csrf_new_matrimonial", session.getLoginData(SessionManager.TOKEN));
            params.put("user_agent", AppConstants.USER_AGENT);
            params.put("vendor_id", selectedVendorId);
            params.put("r_name", txtName.getText().toString());
            params.put("r_title", txtTitleReview.getText().toString());
            params.put("r_email", txtEmail.getText().toString());
            params.put("r_message", txtReviewDesc.getText().toString());
            String rating = String.valueOf(Math.round(Float.valueOf(ratingBar.getRating())));
            params.put("r_star", rating);

            for (String string : params.values()) {
                AppDebugLog.print("params : " + string + "\n");
            }

            Call<JsonObject> call = appApiService.sendVendorReview(params);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    common.hideProgressRelativeLayout(loader);
                    JsonObject data = response.body();

                    AppDebugLog.print("response in sendReviewRequest : " + data);
                    if (data != null) {
                        if (data.get("status").getAsString().equalsIgnoreCase("success")) {
                            // Common.showToast(SendInquiryActivity.this, data.get("data").getAsString());
                            MyApplication.isFromAddedReview = true;
                            Toast.makeText(getApplicationContext(), data.get("errmessage").getAsString(), Toast.LENGTH_SHORT).show();
                            finish();
                        } else if (data.get("status").getAsString().equalsIgnoreCase("error")) {
                            Toast.makeText(getApplicationContext(), data.get("errmessage").getAsString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Common.showToast(getString(R.string.err_msg_something_went_wrong));
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    AppDebugLog.print("error in getCategoryList : " + t.getMessage());
                    Common.showToast(getString(R.string.err_msg_something_went_wrong));
                    common.hideProgressRelativeLayout(loader);
                }
            });
        }
    }
}