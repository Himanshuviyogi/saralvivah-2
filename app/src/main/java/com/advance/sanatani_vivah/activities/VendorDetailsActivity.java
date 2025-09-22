package com.advance.sanatani_vivah.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.chaek.android.RatingBar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.adapter.ImageSliderAdapter;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.model.VendorModel;
import com.advance.sanatani_vivah.model.VendorParentModel;
import com.advance.sanatani_vivah.network.ConnectionDetector;
import com.advance.sanatani_vivah.retrofit.AppApiService;
import com.advance.sanatani_vivah.retrofit.RetrofitClient;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VendorDetailsActivity extends AppCompatActivity {

    private SessionManager session;
    private Common common;
    private RelativeLayout loader;

    private String selectedVendorId;
    private VendorParentModel vendorParentModel = null;
    private VendorModel vendorModel = null;

    private TextView lblReview;
    private Button btnSendInquiry;
    private ImageView imgVerify;
    private ImageView btnFacebook, btnTwitter, btnInstagram, btnLinkedIn;
    private TextView lblStoreName, lblLocation, lblCapacity, lblAvgPrice, label1, label2, lblValue1, lblValue2, lblDescription;
    private TextView lblCountry, lblState, lblCity, lblPhone, lblEmail;
    private RatingBar ratingBar;
    private String imageUrl;

    private TableRow layoutLabels1, layoutLabels2;

    //Retrofit related
    private Retrofit retrofit;
    private AppApiService appApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_details);
        initialize();
    }

    private void initialize() {
        setToolbar();

        common = new Common(this);
        session = new SessionManager(this);
        retrofit = RetrofitClient.getClient();
        appApiService = retrofit.create(AppApiService.class);

        selectedVendorId = getIntent().getStringExtra("vendor_id");
        imageUrl = getIntent().getStringExtra("imageUrl");

        loader = findViewById(R.id.loader);
        lblReview = findViewById(R.id.lblReview);
        lblStoreName = findViewById(R.id.lblStoreName);
        lblLocation = findViewById(R.id.lblLocation);
        lblCity = findViewById(R.id.lblCity);
        lblState = findViewById(R.id.lblState);
        lblCountry = findViewById(R.id.lblCountry);
        lblCapacity = findViewById(R.id.lblCapacity);
        lblAvgPrice = findViewById(R.id.lblAvgPrice);
        label1 = findViewById(R.id.label1);
        label2 = findViewById(R.id.label2);
        lblValue1 = findViewById(R.id.lblValue1);
        lblValue2 = findViewById(R.id.lblValue2);
        lblDescription = findViewById(R.id.lblDescription);
        lblPhone = findViewById(R.id.lblPhone);
        lblEmail = findViewById(R.id.lblEmail);

        imgVerify = findViewById(R.id.imgVerify);

        btnFacebook = findViewById(R.id.btnFacebook);
        btnTwitter = findViewById(R.id.btnTwitter);
        btnInstagram = findViewById(R.id.btnInstagram);
        btnLinkedIn = findViewById(R.id.btnLinkedIn);

        layoutLabels1 = findViewById(R.id.layoutLabels1);
        layoutLabels2 = findViewById(R.id.layoutLabels2);

        layoutLabels1.setVisibility(View.GONE);
        layoutLabels2.setVisibility(View.GONE);

        ratingBar = findViewById(R.id.ratingBar);
        btnSendInquiry = findViewById(R.id.btnSendInquiry);

        btnSendInquiry.setOnClickListener(view -> {
            Intent intent = new Intent(this, SendInquiryActivity.class);
            intent.putExtra("vendor_id", vendorModel.getId());
            startActivity(intent);
        });

        lblReview.setOnClickListener(view -> {
            Intent intent = new Intent(this, ReviewListActivity.class);
            AppDebugLog.print("vendorModel : " + vendorParentModel);
            if (vendorParentModel != null) {
                String modelString = Common.getStringFromModel(vendorParentModel);
                intent.putExtra("vendorModel", modelString);
                startActivity(intent);
            }
        });
    }

    @Override protected void onResume() {
        super.onResume();
        MyApplication.isFromAddedReview = false;
        getVendorDetails();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Vendor Details");

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void getVendorDetails() {
        if (!ConnectionDetector.isConnectingToInternet(this)) {
            Common.showToast(getString(R.string.err_msg_no_intenet_connection));
            return;
        }

        common.showProgressRelativeLayout(loader);

        Map<String, String> params = new HashMap<>();
        params.put("user_agent", AppConstants.USER_AGENT);
        params.put("csrf_new_matrimonial", session.getLoginData(SessionManager.TOKEN));
        params.put("vendor_id", selectedVendorId);

        for (String string : params.values()) {
            AppDebugLog.print("params : " + string + "\n");
        }

        Call<JsonObject> call = appApiService.getVendorDetails(params);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                common.hideProgressRelativeLayout(loader);
                JsonObject data = response.body();

                AppDebugLog.print("response in getCategoryList : " + data);
                if (data != null) {
                    if (data.get("status").getAsString().equalsIgnoreCase("success")) {
                        Gson gson = new GsonBuilder().setDateFormat(AppConstants.GSONDateTimeFormat).create();
                        vendorParentModel = gson.fromJson(data, VendorParentModel.class);
                        vendorModel = vendorParentModel.getVendorModel();
                        setUp();
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

    void setUp() {
        lblStoreName.setText(vendorModel.getPlannerName());
        lblLocation.setText(vendorModel.getAddress());
        lblAvgPrice.setText("₹" + vendorModel.getStartRateRange() + " - ₹" + vendorModel.getEndRateRange());
        lblCapacity.setText(vendorModel.getCapacity());
        lblDescription.setText((Html.fromHtml(vendorModel.getDescription())));
        lblCountry.setText(vendorModel.getCountryName());
        lblState.setText(vendorModel.getStateName());
        lblCity.setText(vendorModel.getCityName());
        lblEmail.setText(vendorModel.getEmail());
        lblPhone.setOnClickListener(view -> {
            lblPhone.setText(vendorModel.getMobile());
        });

        if (vendorModel.getVerifiedBySW() != null && vendorModel.getVerifiedBySW().equalsIgnoreCase("Yes")) {
            imgVerify.setVisibility(View.VISIBLE);
        } else {
            imgVerify.setVisibility(View.GONE);
        }

        if (vendorModel.getFacebookLink() != null && vendorModel.getFacebookLink().length() > 0) {
            btnFacebook.setVisibility(View.VISIBLE);
            btnFacebook.setOnClickListener(view -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(vendorModel.getFacebookLink()));
                startActivity(browserIntent);
            });
        }
        if (vendorModel.getTwitterLink() != null && vendorModel.getTwitterLink().length() > 0) {
            btnTwitter.setVisibility(View.VISIBLE);
            btnTwitter.setOnClickListener(view -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(vendorModel.getTwitterLink()));
                startActivity(browserIntent);
            });
        }
        if (vendorModel.getGoogleLink() != null && vendorModel.getGoogleLink().length() > 0) {
            btnInstagram.setVisibility(View.VISIBLE);
            btnInstagram.setOnClickListener(view -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(vendorModel.getGoogleLink()));
                startActivity(browserIntent);
            });
        }
        if (vendorModel.getLinkedinLink() != null && vendorModel.getLinkedinLink().length() > 0) {
            btnLinkedIn.setVisibility(View.VISIBLE);
            btnLinkedIn.setOnClickListener(view -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(vendorModel.getLinkedinLink()));
                startActivity(browserIntent);
            });
        }

        ArrayList<String> imageList = new ArrayList<String>();
        if (vendorModel.getImage() != null && vendorModel.getImage().length() > 0) {
            imageList.add(vendorModel.getImage());
        }
        if (vendorModel.getImage2() != null && vendorModel.getImage2().length() > 0) {
            imageList.add(vendorModel.getImage2());
        }
        if (vendorModel.getImage3() != null && vendorModel.getImage3().length() > 0) {
            imageList.add(vendorModel.getImage3());
        }
        if (vendorModel.getImage4() != null && vendorModel.getImage4().length() > 0) {
            imageList.add(vendorModel.getImage4());
        }
        if (vendorModel.getImage5() != null && vendorModel.getImage5().length() > 0) {
            imageList.add(vendorModel.getImage5());
        }

        if (vendorModel.getLabel1() != null && vendorModel.getLabel1().length() > 0) {
            layoutLabels1.setVisibility(View.VISIBLE);
            label1.setText(vendorModel.getLabel1());
            lblValue1.setText(vendorModel.getValue1());
        }
        if (vendorModel.getLabel2() != null && vendorModel.getLabel2().length() > 0) {
            layoutLabels2.setVisibility(View.VISIBLE);
            label2.setText(vendorModel.getLabel2());
            lblValue2.setText(vendorModel.getValue2());
        }

        if (imageList.size() > 0) {
            setUpImageSlider(imageList);
        } else {
            imageList.add("https://www.eduaid.net/site/img//default.png");
            setUpImageSlider(imageList);
        }

        AppDebugLog.print("rating : "+vendorParentModel.getVendorReviewAverage());
        if (vendorParentModel.getVendorReviewAverage() > 0) {
            ratingBar.setScore(Math.round(Float.valueOf(vendorParentModel.getVendorReviewAverage())));
        }
    }

    void setUpImageSlider(ArrayList<String> imageList) {
        SliderView sliderView = findViewById(R.id.imageSlider);
        ImageSliderAdapter adapter = new ImageSliderAdapter(this, imageList,imageUrl);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        // sliderView?.indicatorSelectedColor = Color.WHITE
        //  sliderView?.indicatorUnselectedColor = Color.GRAY
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }
}