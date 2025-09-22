package com.advance.sanatani_vivah.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.retrofit.AppApiService;
import com.advance.sanatani_vivah.retrofit.RetrofitClient;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SendInquiryActivity extends AppCompatActivity {

    private SessionManager session;
    private RelativeLayout loader;

    private Common common;

    private String selectedVendorId;

    private Button btnSendInquiry;
    private CountryCodePicker countryCodePicker;
    //private AppCompatSpinner spinSelectNoGuest;
    private EditText txtName, txtMobile, txtEmail, txtDOB, txtDescription,txtSelectNoGuest;
    private CheckBox cbEmail, cbCall;

    final Calendar myCalendar = Calendar.getInstance();

    //Retrofit related
    private Retrofit retrofit;
    private AppApiService appApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_inquiry);
        initialize();
    }

    private void initialize() {
        setToolbar();

        session = new SessionManager(this);
        retrofit = RetrofitClient.getClient();
        appApiService = retrofit.create(AppApiService.class);
        common = new Common(this);

        loader = findViewById(R.id.loader);
        txtName = findViewById(R.id.txtName);
        txtMobile = findViewById(R.id.txtMobile);
        txtEmail = findViewById(R.id.txtEmail);
        txtDOB = findViewById(R.id.txtDOB);

        txtDescription = findViewById(R.id.txtDescription);
        txtSelectNoGuest = findViewById(R.id.txtSelectNoGuest);
        //spinSelectNoGuest = findViewById(R.id.spinSelectNoGuest);

        btnSendInquiry = findViewById(R.id.btnSendInquiry);

        cbEmail = findViewById(R.id.cbEmail);
        cbCall = findViewById(R.id.cbCall);

        countryCodePicker = findViewById(R.id.countryCodePicker);

        selectedVendorId = getIntent().getStringExtra("vendor_id");

        //setDataForGuestSpinner();

        btnSendInquiry.setOnClickListener(view -> {
            sendInquiry();
        });


        DatePickerDialog.OnDateSetListener date;

        date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        txtDOB.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(SendInquiryActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMinDate(System.currentTimeMillis()+24*60*60*1000);
            // dialog.getDatePicker().setMaxDate(d.getTime());
            dialog.show();
        });
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //yyyy-M-dd
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txtDOB.setText(sdf.format(myCalendar.getTime()));
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Send Inquiry");

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void sendInquiry() {
        boolean isValid = true;
        if (txtName.getText().length() == 0) {
            isValid = false;
            txtName.setError("Please enter your name");
        }
        if (txtDescription.getText().length() == 0) {
            isValid = false;
            txtDescription.setError("Please enter description");
        }
        if (txtMobile.getText().length() == 0) {
            isValid = false;
            txtMobile.setError("Please enter mobile number");
        } else {
            if (txtMobile.getText().toString().length() < 10) {
                txtMobile.setError("Please enter valid mobile number");
                isValid = false;
            }
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
        if (TextUtils.isEmpty(txtDOB.getText().toString())) {
            txtDOB.setError("Please select wedding date");
            isValid = false;
        }
        if (txtSelectNoGuest.getText().length() == 0) {
            isValid = false;
            Toast.makeText(this, "Please enter Guest No Of Seat", Toast.LENGTH_LONG).show();
        }

        if (isValid) {
            common.showProgressRelativeLayout(loader);
            Map<String, String> params = new HashMap<>();
            params.put("csrf_new_matrimonial", session.getLoginData(SessionManager.TOKEN));
            params.put("user_agent", AppConstants.USER_AGENT);
            params.put("vendor_id", selectedVendorId);
            params.put("name", txtName.getText().toString());
            params.put("email", txtEmail.getText().toString());
            params.put("country_code", countryCodePicker.getSelectedCountryCodeWithPlus());
            params.put("phone", txtMobile.getText().toString());
            Date date = Common.getDateFromDateString(AppConstants.dateFormat,txtDOB.getText().toString());
            String weddingDate = Common.getDateStringFromDate(AppConstants.dateFormat,date);
            params.put("weddingdate", weddingDate);
            params.put("guest", txtSelectNoGuest.getText().toString());
            //params.put("guest", guetCountId);
            params.put("description", txtDescription.getText().toString());

            String sendMeInfoVia = "";

            if (cbEmail.isChecked()) {
                sendMeInfoVia = "E-Mail";
            }
            if (cbCall.isChecked()) {
                if (sendMeInfoVia.length() > 0) {
                    sendMeInfoVia = sendMeInfoVia + ", " + "Need Call back";
                } else {
                    sendMeInfoVia = "Need Call back";
                }
            }
            params.put("send_inq_info", sendMeInfoVia);

            for (String string : params.values()) {
                AppDebugLog.print("params : " + string + "\n");
            }

            Call<JsonObject> call = appApiService.sendVendorInquiry(params);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    common.hideProgressRelativeLayout(loader);
                    JsonObject data = response.body();

                    AppDebugLog.print("response in sendInquiry : " + data);
                    if (data != null) {
                        if (data.get("status").getAsString().equalsIgnoreCase("success")) {
                            finish();
                            // Common.showToast(SendInquiryActivity.this, data.get("data").getAsString());
                            Toast.makeText(getApplicationContext(), data.get("errmessage").getAsString(), Toast.LENGTH_SHORT).show();
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

    private List<String> guestIdList = new ArrayList<>();
    private HashMap<String, String> guestListMap = new HashMap<>();
    private String guetCountId = "";

    private void setDataForGuestSpinner() {
        List<String> val = new ArrayList<>();
        guestIdList.clear();
        val.add("Select Number of Guest");
        guestIdList.add("0");
        guestIdList.add("50 to 100");
        val.add("50 to 100");
        guestIdList.add("50 to 100");
        val.add("101 to 150");
        guestIdList.add("101 to 150");
        val.add("151 to 200");
        guestIdList.add("151 to 200");
        val.add("201 and above");
        guestIdList.add("201 and above");
        for (int k = 0; k < val.size(); k++) {
            guestListMap.put(val.get(k), guestIdList.get(k));
        }

        ArrayAdapter adpter = new ArrayAdapter<>(this, R.layout.spinner_item, val);
//        spinSelectNoGuest.setAdapter(adpter);
//        spinSelectNoGuest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                guetCountId = guestListMap.get(spinSelectNoGuest.getSelectedItem().toString());
//            }
//
//            @Override public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }
}