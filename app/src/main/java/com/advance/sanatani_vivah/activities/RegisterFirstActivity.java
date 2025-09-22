package com.advance.sanatani_vivah.activities;

import static com.advance.sanatani_vivah.utility.AppConstants.MAX_IMAGE_SIZE_IN_MB_TO_UPLOAD;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.advance.sanatani_vivah.retrofit.ApiRequestResponse;
import com.advance.sanatani_vivah.utility.ContentUriUtils;
import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SingleSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hbb20.CountryCodePicker;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.network.ConnectionDetector;
import com.advance.sanatani_vivah.retrofit.AppApiService;
import com.advance.sanatani_vivah.retrofit.ProgressRequestBody;
import com.advance.sanatani_vivah.retrofit.RetrofitClient;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class RegisterFirstActivity extends AppCompatActivity implements View.OnClickListener, ProgressRequestBody.UploadCallbacks, SpinnerListener, ApiRequestResponse.AppApiRequestCallbacks {

    private static final String STEP_1 = "first";
    private static final String STEP_2 = "second";
    private static final String STEP_3 = "third";
    private static final String STEP_4 = "four";
    private static final String STEP_5 = "five";
    private static final String STEP_6 = "six";

    private static final String DDR_DATA = "ddr_data";
    private static final String DEPENDENT_DDR_DATA = "dependent_ddr_data";
    private static final String CASTE_LIST = "caste_list";
    private static final String STATE_LIST = "state_list";
    private static final String CITY_LIST = "city_list";

    private RadioGroup genderToggle;
    private CountryCodePicker spin_code;
    private MaterialButton btn_login, btn_first_submit, btn_second_prev, btn_second_submit,
            btn_third_submit, btn_third_prev, btn_four_submit, btn_four_prev, btn_five_submit, btn_five_prev, btn_choose,
            btn_six_submit, btn_six_prev;
    private TextInputEditText et_f_name, et_l_name, et_email, et_password, et_mobile, et_dob, et_sub_caste, et_gothra;
    private EditText et_hoby, et_about;
    private Common common;
    private SingleSpinnerSearch spin_religion, spin_caste, spin_tongue, spin_country, spin_state, spin_city, spin_mari, spin_t_child, spin_child_status,
            spin_emp_in, spin_income, spin_occupation, spin_designation, spin_height, spin_weight, spin_eat, spin_smok, spin_drink,
            spin_body, spin_skin, spin_manglik, spin_star, spin_horo, spin_moon;
    private MultiSpinnerSearch spin_edu;
    private LinearLayout lay_second, lay_first, lay_third, lay_four, lay_five, lay_six, layoutBottomSheet;
    private RelativeLayout lay_t_child, lay_status_child;
    private ScrollView ragi_scroll;
    private BottomSheetBehavior sheetBehavior;
    final Calendar myCalendar = Calendar.getInstance();
    private TextView tv_cancel, tv_gallary, tv_camera;
    private String mCurrentPhotoPath, page_name = STEP_1, religion_id = "4", caste_id = "", tongue_id = "", gender = "Male", country_code = "+91",
            ragister_id = "", country_id = "", state_id = "", city_id = "", mari_id = "", total_child_id = "", status_child_id = "",
            edu_id = "", emp_id = "", income_id = "", occu_id = "", desig_id = "", hite_id = "", weight_id = "", eat_id = "", smok_id = "", drink_id = "",
            body_id = "", skin_id = "", manglik_id = "", star_id = "", horo_id = "", moon_id = "", org_path, crop_path, fb_id = "";
    private ImageView img_profile;
    private HashMap<String, String> image_map;
    private ProgressDialog pd;
    private RelativeLayout loader;
    private SessionManager session;
    private Uri resultUri;
    boolean isImageSelect = false;
    private DatePickerDialog.OnDateSetListener date;
    private TextInputLayout pass_input;
    private TextView lblTerms;
    private RelativeLayout layoutBottomTop;

    private final int CROP_PIC = 3;
    private final int PERMISSION_REQUEST_CODE = 122;
    private EasyImage easyImage = null;
    private File compressedFile = null;
    private File originalFile = null;
    private String originalFilePath = "", cropFilePath = "";
    private Uri cropUri;
    final private int SELECT_PICTURE = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_first);
        initialize();
        setUpEasyImage();
    }

    private void initialize() {
        common = new Common(this);
        session = new SessionManager(this);

        image_map = new HashMap<>();

        layoutBottomTop = findViewById(R.id.layoutBottomTop);
        loader = findViewById(R.id.loader);
        genderToggle = findViewById(R.id.genderToggle);
        lay_t_child = findViewById(R.id.lay_t_child);
        lay_status_child = findViewById(R.id.lay_status_child);
        img_profile = findViewById(R.id.img_profile);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_gallary = findViewById(R.id.tv_gallary);
        tv_camera = findViewById(R.id.tv_camera);
        et_f_name = findViewById(R.id.et_f_name);
        et_l_name = findViewById(R.id.et_l_name);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        pass_input = findViewById(R.id.pass_input);

        ragi_scroll = findViewById(R.id.ragi_scroll);
        lay_first = findViewById(R.id.lay_first);
        lay_second = findViewById(R.id.lay_second);
        lay_third = findViewById(R.id.lay_third);
        lay_four = findViewById(R.id.lay_four);
        lay_five = findViewById(R.id.lay_five);
        lay_six = findViewById(R.id.lay_six);

        btn_choose = findViewById(R.id.btn_choose);

        btn_six_submit = findViewById(R.id.btn_six_submit);
        btn_six_prev = findViewById(R.id.btn_six_prev);

        btn_five_submit = findViewById(R.id.btn_five_submit);
        btn_five_prev = findViewById(R.id.btn_five_prev);

        btn_four_submit = findViewById(R.id.btn_four_submit);
        btn_four_prev = findViewById(R.id.btn_four_prev);

        btn_third_submit = findViewById(R.id.btn_third_submit);
        btn_third_prev = findViewById(R.id.btn_third_prev);

        btn_second_prev = findViewById(R.id.btn_second_prev);
        btn_second_submit = findViewById(R.id.btn_second_submit);

        btn_first_submit = findViewById(R.id.btn_first_submit);
        btn_login = findViewById(R.id.btn_login);

        spin_code = findViewById(R.id.spin_code);

        et_sub_caste = findViewById(R.id.et_sub_caste);
        et_gothra = findViewById(R.id.et_gothra);
        et_hoby = findViewById(R.id.et_hoby);
        et_about = findViewById(R.id.et_about);

        et_mobile = findViewById(R.id.et_mobile);
        et_dob = findViewById(R.id.et_dob);

        String mobileNumber = getIntent().getStringExtra("mobile_number");
        country_code = getIntent().getStringExtra("country_code");
        if (mobileNumber != null && country_code != null) {
            et_mobile.setText(mobileNumber);
            spin_code.setCountryForPhoneCode(Integer.parseInt(country_code));
        }

        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        et_dob.setOnClickListener(v -> {
            //For above 18 years date
            Calendar maxDateCalendar = Calendar.getInstance();
            maxDateCalendar.add(Calendar.YEAR, -18);
            maxDateCalendar.add(Calendar.DATE, -1);

            //For below 18 years date
            Calendar minDateCalendar = Calendar.getInstance();
            minDateCalendar.add(Calendar.YEAR, -90);
            minDateCalendar.add(Calendar.DATE, 1);

            DatePickerDialog dialog = new DatePickerDialog(RegisterFirstActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
            //set max date in date picker
            dialog.getDatePicker().setMaxDate(maxDateCalendar.getTime().getTime());
            dialog.getDatePicker().setMinDate(minDateCalendar.getTime().getTime());
            dialog.show();
        });

        et_dob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!et_dob.getText().toString().equals("")) {
                    et_dob.setError(null);
                }
            }
        });

        btn_choose.setOnClickListener(this);
        btn_six_submit.setOnClickListener(this);
        btn_six_prev.setOnClickListener(this);
        btn_five_submit.setOnClickListener(this);
        btn_five_prev.setOnClickListener(this);
        btn_four_submit.setOnClickListener(this);
        btn_four_prev.setOnClickListener(this);
        btn_third_submit.setOnClickListener(this);
        btn_third_prev.setOnClickListener(this);
        btn_second_prev.setOnClickListener(this);
        btn_second_submit.setOnClickListener(this);
        btn_first_submit.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }

        });

        date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        tv_gallary.setOnClickListener(this);
        tv_camera.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

        lblTerms = findViewById(R.id.lblTerms);
        singleTextView(lblTerms);

        initDropDownData();
    }

    private void setUpEasyImage() {
        easyImage = new EasyImage.Builder(RegisterFirstActivity.this)
                .setChooserTitle(getString(R.string.app_name))
                .setCopyImagesToPublicGalleryFolder(false)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setFolderName(AppConstants.DIRECTORY_NAME)
                .allowMultiple(false)
                .build();
    }

    private void pickImage(int pickFor) {
        switch (pickFor) {
            case 100:
                easyImage.openGallery(this);
                break;

            case 200:
                easyImage.openCameraForImage(this);
                break;
        }
    }

    private void openFileChooser() {
        requestPermission();
    }

    @AfterPermissionGranted(122)
    private void requestPermission() {
        if (!checkPermission()) {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                    this,
                    "This needs permission to use feature. You can grant them in app settings.",
                    PERMISSION_REQUEST_CODE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    //TODO Permission related
    private boolean checkPermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        if (EasyPermissions.hasPermissions(this, perms)) {
            return true;
        } else {
            return false;
        }
    }

    private void initDropDownData() {
        if (MyApplication.getSpinDataStr() != null) {
            spin_moon = findViewById(R.id.spin_moon);
            setupSearchDropDown(spin_moon, "Moonsign", "moonsign_list");

            spin_horo = findViewById(R.id.spin_horo);
            setupSearchDropDown(spin_horo, "Horoscope", "horoscope");

            spin_star = findViewById(R.id.spin_star);
            setupSearchDropDown(spin_star, "Star", "star_list");

            spin_manglik = findViewById(R.id.spin_manglik);
            setupSearchDropDown(spin_manglik, "Manglik", "manglik");

            spin_height = findViewById(R.id.spin_height);
            setupSearchDropDown(spin_height, "Height*", "height_list");

            spin_weight = findViewById(R.id.spin_weight);
            setupSearchDropDown(spin_weight, "Weight*", "weight_list");

            spin_eat = findViewById(R.id.spin_eat);
            setupSearchDropDown(spin_eat, "Eating Habits*", "diet");

            spin_smok = findViewById(R.id.spin_smok);
            setupSearchDropDown(spin_smok, "Smoking*", "smoke");

            spin_drink = findViewById(R.id.spin_drink);
            setupSearchDropDown(spin_drink, "Drinking*", "drink");

            spin_body = findViewById(R.id.spin_body);
            setupSearchDropDown(spin_body, "Body Type*", "bodytype");

            spin_skin = findViewById(R.id.spin_skin);
            setupSearchDropDown(spin_skin, "Skin Tone*", "complexion");

            spin_edu = findViewById(R.id.spin_edu);
            setupSearchDropDown(spin_edu, "Education*", "education_list");

            spin_emp_in = findViewById(R.id.spin_emp_in);
            setupSearchDropDown(spin_emp_in, "Employee In*", "employee_in");

            spin_income = findViewById(R.id.spin_income);
            setupSearchDropDown(spin_income, "Annual Income*", "income");

            spin_occupation = findViewById(R.id.spin_occupation);
            setupSearchDropDown(spin_occupation, "Occupation*", "occupation_list");

            spin_designation = findViewById(R.id.spin_designation);
            setupSearchDropDown(spin_designation, "Designation*", "designation_list");

            spin_country = findViewById(R.id.spin_country);
            setupSearchDropDown(spin_country, "Country*", "country_list");

            spin_state = findViewById(R.id.spin_state);
            setupInitializeSearchDropDown(spin_state, "State*");

            spin_city = findViewById(R.id.spin_city);
            setupInitializeSearchDropDown(spin_city, "City*");

            spin_mari = findViewById(R.id.spin_mari);
            setupSearchDropDown(spin_mari, "Marital Status*", "marital_status");

            spin_t_child = findViewById(R.id.spin_t_child);
            setupSearchDropDown(spin_t_child, "Total Children", "total_children");

            spin_child_status = findViewById(R.id.spin_child_status);
            setupSearchDropDown(spin_child_status, "Status Children", "status_children");

            spin_religion = findViewById(R.id.spin_religion);
            setupSearchDropDown(spin_religion, "Religion*", "religion_list");

            spin_caste = findViewById(R.id.spin_caste);
            setupInitializeSearchDropDown(spin_caste, "Caste*");

            spin_tongue = findViewById(R.id.spin_tongue);
            setupSearchDropDown(spin_tongue, "Mother Tongue*", "mothertongue_list");

            lay_first.setVisibility(View.VISIBLE);
            layoutBottomTop.setVisibility(View.VISIBLE);
        } else {
            getList();
        }
    }

    //TODO form validation related code
    private void validFirst() {
        String fname = et_f_name.getText().toString().trim();
        String lname = et_l_name.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String dob = et_dob.getText().toString().trim();
        String mobile = et_mobile.getText().toString().trim();
        country_code = spin_code.getSelectedCountryCodeWithPlus();

        boolean isvalid = true;
        if (TextUtils.isEmpty(fname)) {
            et_f_name.setError("Please enter first name");
            isvalid = false;
        }
        if (TextUtils.isEmpty(lname)) {
            et_l_name.setError("Please enter last name");
            isvalid = false;
        }
        if (TextUtils.isEmpty(email)) {
            et_email.setError("Please enter email");
            isvalid = false;
        } else {
            if (!common.isValidEmail(email)) {
                et_email.setError("Please enter valid email");
                isvalid = false;
            }
        }

        if (TextUtils.isEmpty(password)) {
            et_password.setError("Please enter password");
            isvalid = false;
        }

        if (password.length() < 6) {
            et_password.setError("Please enter atleast 6 characters");
            isvalid = false;
        }

        if (TextUtils.isEmpty(mobile)) {
            et_mobile.setError("Please enter mobile number");
            isvalid = false;
        } else {
            if (mobile.length() < 10) {
                et_mobile.setError("Please enter valid mobile number");
                isvalid = false;
            }
        }

        if (TextUtils.isEmpty(dob)) {
            et_dob.setError("Please enter date of birth");
            isvalid = false;
        }
        if (!isValidId(religion_id)) {
            common.spinnerSetError(spin_religion, "Please select religion");
            isvalid = false;
        }
        if (!isValidId(caste_id)) {
            common.spinnerSetError(spin_caste, "Please select caste");
            isvalid = false;
        }
        if (!isValidId(tongue_id)) {
            common.spinnerSetError(spin_tongue, "Please select mother tongue");
            isvalid = false;
        }

        RadioButton radioButton;
        // get selected radio button from radioGroup
        int selectedId = genderToggle.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);

        gender = radioButton.getText().toString();

        if (isvalid) {
            common.showProgressRelativeLayout(loader);
            HashMap<String, String> param = new HashMap<>();
            param.put("firstname", fname);
            param.put("lastname", lname);
            param.put("email", email);
            param.put("password", password);
            param.put("country_code", country_code);
            param.put("mobile_number", mobile);
            param.put("birthdate", changeDate(dob));
            param.put("religion", getValidId(religion_id));
            param.put("caste", getValidId(caste_id));
            param.put("mother_tongue", getValidId(tongue_id));
            param.put("gender", gender);
            param.put("id", ragister_id);
            param.put("fb_id", fb_id);
            param.put("app_security_key", "fsdhfsfg@fg!hgd*5sdhfvghs44");
            param.put("android_device_id", session.getLoginData(SessionManager.KEY_DEVICE_TOKEN));

            ApiRequestResponse apiRequestResponse = new ApiRequestResponse();
            apiRequestResponse.postRequest(this, AppConstants.register_first, param, this, STEP_1);

            //submitRagister(AppConstants.register_first, STEP_1, param);
        }
    }

    private void validSecond() {
        boolean isvalid = true;
        if (!isValidId(country_id)) {
            common.spinnerSetError(spin_country, "Please select country");
            isvalid = false;
        }
        if (!isValidId(state_id)) {
            common.spinnerSetError(spin_state, "Please select state");
            isvalid = false;
        }
        if (!isValidId(city_id)) {
            common.spinnerSetError(spin_city, "Please select city");
            isvalid = false;
        }
        if (!isValidId(mari_id)) {
            common.spinnerSetError(spin_mari, "Please select marital status");
            isvalid = false;
        } else {
            if (mari_id != null && !mari_id.equals("Unmarried")) {
                if (total_child_id == null || total_child_id.equals("total")) {
                    common.spinnerSetError(spin_t_child, "Please select total children");
                    isvalid = false;
                } else {
                    if (!total_child_id.equals("0")) {
                        if (status_child_id == null || status_child_id.equals("0")) {
                            common.spinnerSetError(spin_child_status, "Please select children status");
                            isvalid = false;
                        }
                    }
                }
            }
        }

        if (isvalid) {
            common.showProgressRelativeLayout(loader);
            HashMap<String, String> param = new HashMap<>();
            param.put("country_id", getValidId(country_id));
            param.put("state_id", getValidId(state_id));
            param.put("city", getValidId(city_id));
            param.put("marital_status", getValidId(mari_id));
            param.put("total_children", checkValue(total_child_id));
            param.put("status_children", checkValue(status_child_id));
            param.put("id", ragister_id);

            ApiRequestResponse apiRequestResponse = new ApiRequestResponse();
            apiRequestResponse.postRequest(this, AppConstants.register_step, param, this, STEP_2);
            //submitRagister(AppConstants.register_step, STEP_2, param);
        }
    }

    private void validThird() {
        boolean isvalid = true;
        if (!isValidId(edu_id)) {
            common.spinnerSetError(spin_edu, "Please select education");
            isvalid = false;
        }
        if (!isValidId(emp_id)) {
            common.spinnerSetError(spin_emp_in, "Please select employment");
            isvalid = false;
        }
        if (!isValidId(income_id)) {
            common.spinnerSetError(spin_income, "Please select annual income");
            isvalid = false;
        }
        if (!isValidId(occu_id)) {
            common.spinnerSetError(spin_occupation, "Please select occupation");
            isvalid = false;
        }
        if (!isValidId(desig_id)) {
            common.spinnerSetError(spin_designation, "Please select designation");
            isvalid = false;
        }

        if (isvalid) {
            common.showProgressRelativeLayout(loader);
            HashMap<String, String> param = new HashMap<>();
            param.put("education_detail", getValidId(edu_id));
            param.put("employee_in", getValidId(emp_id));
            param.put("income", getValidId(income_id));
            param.put("occupation", getValidId(occu_id));
            param.put("designation", getValidId(desig_id));
            param.put("id", ragister_id);

            ApiRequestResponse apiRequestResponse = new ApiRequestResponse();
            apiRequestResponse.postRequest(this, AppConstants.register_step, param, this, STEP_3);
            //submitRagister(AppConstants.register_step, STEP_3, param);
        }

    }

    private void validFour() {
        boolean isvalid = true;
        if (!isValidId(hite_id)) {
            common.spinnerSetError(spin_height, "Please select height");
            isvalid = false;
        }
        if (!isValidId(weight_id)) {
            common.spinnerSetError(spin_weight, "Please select weight");
            isvalid = false;
        }
        if (!isValidId(eat_id)) {
            common.spinnerSetError(spin_eat, "Please select eating habits");
            isvalid = false;
        }
        if (!isValidId(smok_id)) {
            common.spinnerSetError(spin_smok, "Please select smoking");
            isvalid = false;
        }
        if (!isValidId(drink_id)) {
            common.spinnerSetError(spin_drink, "Please select drinking");
            isvalid = false;
        }
        if (!isValidId(body_id)) {
            common.spinnerSetError(spin_body, "Please select body type");
            isvalid = false;
        }
        if (!isValidId(skin_id)) {
            common.spinnerSetError(spin_skin, "Please select skin tone");
            isvalid = false;
        }
        if (isvalid) {
            common.showProgressRelativeLayout(loader);
            HashMap<String, String> param = new HashMap<>();
            param.put("height", getValidId(hite_id));
            param.put("weight", getValidId(weight_id));
            param.put("diet", getValidId(eat_id));
            param.put("smoke", getValidId(smok_id));
            param.put("drink", getValidId(drink_id));
            param.put("bodytype", getValidId(body_id));
            param.put("complexion", getValidId(skin_id));
            param.put("id", ragister_id);

            ApiRequestResponse apiRequestResponse = new ApiRequestResponse();
            apiRequestResponse.postRequest(this, AppConstants.register_step, param, this, STEP_4);
            //submitRagister(AppConstants.register_step, STEP_4, param);
        }
    }

    private void validFive() {
        boolean isvalid = true;
        String sub_caste = et_sub_caste.getText().toString().trim();
        String gothra = et_gothra.getText().toString().trim();
        String hobby = et_hoby.getText().toString().trim();
        String about = et_about.getText().toString().trim();

        if (TextUtils.isEmpty(hobby)) {
            et_hoby.setError("Please enter hobby");
            isvalid = false;
        }
        if (TextUtils.isEmpty(about)) {
            et_about.setError("Please enter about yourself");
            isvalid = false;
        }
        if (isvalid) {
            common.showProgressRelativeLayout(loader);
            HashMap<String, String> param = new HashMap<>();
            param.put("subcaste", sub_caste);
            param.put("manglik", getValidId(manglik_id));
            param.put("star", getValidId(star_id));
            param.put("horoscope", getValidId(horo_id));
            param.put("gothra", getValidId(gothra));
            param.put("moonsign", getValidId(moon_id));
            param.put("profile_text", about);
            param.put("hobby", hobby);
            param.put("id", ragister_id);

            ApiRequestResponse apiRequestResponse = new ApiRequestResponse();
            apiRequestResponse.postRequest(this, AppConstants.register_step, param, this, STEP_5);
            //submitRagister(AppConstants.register_step, STEP_5, param);
        }

        if (gender.equals("Female")) {
            int placeHolder = R.drawable.female;
            img_profile.setImageResource(placeHolder);
        } else if (gender.equals("Male")) {
            int placeHolder = R.drawable.male;
            img_profile.setImageResource(placeHolder);
        }
    }

    private String getValidId(String val) {
        if (val == null || val.equals("") || val.equals("0")) {
            return "";
        }
        return val;
    }

    private String checkValue(String val) {
        if (val == null || val.equals("0") || val.equals("total")) {
            return "";
        } else
            return val;
    }

    private boolean isValidId(String val) {
        if (val == null || val.equals("") || val.equals("0")) {
            return false;
        }
        return true;
    }
    //TODO end form validation related code

    //TODO api calls related code
    private void getList() {
        common.showProgressRelativeLayout(loader);
        ApiRequestResponse apiRequestResponse = new ApiRequestResponse();
        apiRequestResponse.postRequest(this, AppConstants.common_list, null, this, DDR_DATA);
    }

    private void uploadFileToServer() {
        if (!ConnectionDetector.isConnectingToInternet(this)) {
            Toast.makeText(this, getString(R.string.err_msg_no_intenet_connection), Toast.LENGTH_LONG).show();
            return;
        }

        common.hideProgressRelativeLayout(loader);

        // setting progress bar to zero
        pd = new ProgressDialog(RegisterFirstActivity.this);
        pd.setTitle("Uploading...");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setProgress(0);
        pd.setCancelable(false);
        pd.show();

        //File org = new File(org_path);
        File profileOriginalImageCompressedFile = Common.getCompressedImageFile(this, new File(Common.getPath(this, cropUri)));
        //ProgressRequestBody originalFileBody = new ProgressRequestBody(profileOriginalImageCompressedFile, getMimeType(originalFilePath), this);
        //MultipartBody.Part originalFilePart = MultipartBody.Part.createFormData("profil_photo_org", profileOriginalImageCompressedFile.getName().replaceAll("[^a-zA-Z0-9.]", ""), originalFileBody);

        MultipartBody.Part originalFilePart = MultipartBody.Part.createFormData("profil_photo_org", profileOriginalImageCompressedFile.getName(),
                RequestBody.create(MediaType.parse(getMimeType(profileOriginalImageCompressedFile.getAbsolutePath())), profileOriginalImageCompressedFile));

        RequestBody partParam1 = RequestBody.create(MediaType.parse("text/plain"), ragister_id);
        RequestBody partParam2 = RequestBody.create(MediaType.parse("text/plain"), "NI-AAPP");
        RequestBody partParam3 = RequestBody.create(MediaType.parse("text/plain"), session.getLoginData(SessionManager.TOKEN));

        Retrofit retrofit = RetrofitClient.getClient();
        AppApiService appApiService = retrofit.create(AppApiService.class);

        Call<JsonObject> call = null;
        if (page_name.equalsIgnoreCase(STEP_6)) {
            File crop = new File(cropFilePath);
            //ProgressRequestBody cropFileBody = new ProgressRequestBody(profileOriginalImageCompressedFile, getMimeType(cropFilePath), this);
            //MultipartBody.Part cropFilePart = MultipartBody.Part.createFormData("profil_photo", crop.getName().replaceAll("[^a-zA-Z0-9.]", ""), cropFileBody);

            MultipartBody.Part cropFilePart = MultipartBody.Part.createFormData("profil_photo", crop.getName(),
                    RequestBody.create(MediaType.parse(getMimeType(cropFilePath)), crop));

            Map<String, RequestBody> params = new HashMap<>();
            params.put("id", partParam1);
            params.put("user_agent", partParam2);
            params.put("csrf_new_matrimonial", partParam3);

            long fileSizeMB = common.getFIleSizeInMB(profileOriginalImageCompressedFile);
            if (fileSizeMB > MAX_IMAGE_SIZE_IN_MB_TO_UPLOAD) {
                Toast.makeText(RegisterFirstActivity.this, "Image size more than " + MAX_IMAGE_SIZE_IN_MB_TO_UPLOAD + " MB", Toast.LENGTH_SHORT).show();
            } else {
                call = appApiService.uploadPhoto(cropFilePart, originalFilePart, params);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }

                        JsonObject data = response.body();
                        AppDebugLog.print("response in submitData : " + response.body());

                        if (data != null) {
                            common.showToast(data.get("errmessage").getAsString());
                            if (data.get("status").getAsString().equals("success")) {
                                isImageSelect = false;
                                if (page_name.equalsIgnoreCase(STEP_6)) {
                                    Intent i = new Intent(getApplicationContext(), SuccessActivity.class);
                                    i.putExtra("ragistered_id", ragister_id);
                                    startActivity(i);
                                }
                            }
                        } else {
                            Toast.makeText(RegisterFirstActivity.this, getString(R.string.err_msg_try_again_later), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(RegisterFirstActivity.this, getString(R.string.err_msg_something_went_wrong), Toast.LENGTH_LONG).show();
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                    }
                });
            }
        }
    }
    //TODO end api calls related code

    //TODO callback methods
    @Override
    public void onBackPressed() {
        switch (page_name) {
            case STEP_1:
                finish();
                break;
            case STEP_2:
                ragi_scroll.scrollTo(0, 0);
                lay_second.setVisibility(View.GONE);
                lay_first.setVisibility(View.VISIBLE);
                page_name = STEP_1;
                break;
            case STEP_3:
                ragi_scroll.scrollTo(0, 0);
                lay_third.setVisibility(View.GONE);
                lay_second.setVisibility(View.VISIBLE);
                page_name = STEP_2;
                break;
            case STEP_4:
                ragi_scroll.scrollTo(0, 0);
                lay_third.setVisibility(View.VISIBLE);
                lay_four.setVisibility(View.GONE);
                page_name = STEP_3;
                break;
            case STEP_5:
                ragi_scroll.scrollTo(0, 0);
                lay_five.setVisibility(View.GONE);
                lay_four.setVisibility(View.VISIBLE);
                page_name = STEP_4;
                break;
            case STEP_6:
                ragi_scroll.scrollTo(0, 0);
                lay_five.setVisibility(View.VISIBLE);
                lay_six.setVisibility(View.GONE);
                page_name = STEP_5;
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_first_submit) {
            validFirst();
        } else if (id == R.id.btn_second_prev) {
            ragi_scroll.scrollTo(0, 0);
            lay_second.setVisibility(View.GONE);
            lay_first.setVisibility(View.VISIBLE);
            page_name = STEP_1;
        } else if (id == R.id.btn_second_submit) {
            validSecond();
        } else if (id == R.id.btn_third_submit) {
            validThird();
        } else if (id == R.id.btn_third_prev) {
            ragi_scroll.scrollTo(0, 0);
            lay_third.setVisibility(View.GONE);
            lay_second.setVisibility(View.VISIBLE);
            page_name = STEP_2;
        } else if (id == R.id.btn_four_submit) {
            validFour();
        } else if (id == R.id.btn_four_prev) {
            ragi_scroll.scrollTo(0, 0);
            lay_third.setVisibility(View.VISIBLE);
            lay_four.setVisibility(View.GONE);
            page_name = STEP_3;
        } else if (id == R.id.btn_five_submit) {
            validFive();

        } else if (id == R.id.btn_five_prev) {
            ragi_scroll.scrollTo(0, 0);
            lay_five.setVisibility(View.GONE);
            lay_four.setVisibility(View.VISIBLE);
            page_name = STEP_4;
        } else if (id == R.id.btn_six_submit) {
            if (!isImageSelect) {
                Toast.makeText(getApplicationContext(), "Please select image first", Toast.LENGTH_LONG).show();
                return;
            }
            image_map.put("id", ragister_id);
            uploadFileToServer();
        } else if (id == R.id.btn_six_prev) {
            ragi_scroll.scrollTo(0, 0);
            lay_five.setVisibility(View.VISIBLE);
            lay_six.setVisibility(View.GONE);
            page_name = STEP_5;
        } else if (id == R.id.btn_login) {
            startActivity(new Intent(RegisterFirstActivity.this, LoginActivity.class));
            finish();
        } else if (id == R.id.tv_cancel) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (id == R.id.tv_camera) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            pickImage(200);
        } else if (id == R.id.tv_gallary) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            pickImage(100);
        } else if (id == R.id.btn_choose) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent,"Select Photo"), SELECT_PICTURE);
            }else{
                openFileChooser();
            }
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {
        // set current progress
        if (pd != null && pd.isShowing()) {
            pd.setProgress(percentage);
        }

    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {
        //set finish progress
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }
    //TODO end callback methods

    //TODO date selection related code
    public String changeDate(String time) {
        String inputPattern = "dd-MM-yyyy";
        String outputPattern = "yyyy-M-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //yyyy-M-dd
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_dob.setText(sdf.format(myCalendar.getTime()));
    }
    //TODO end date selection related code

    //TODO dropdown related code
    //Search drop down
    @Override
    public void onItemsSelected(MultiSpinnerSearch singleSpinnerSearch) {
        Common.hideSoftKeyboard(this);
        if (singleSpinnerSearch == null) return;
        if (singleSpinnerSearch.getSelectedIdsInString() == null) return;

        switch (singleSpinnerSearch.getId()) {
            case R.id.spin_edu:
                edu_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
        }
    }

    @Override
    public void onItemsSelected(SingleSpinnerSearch singleSpinnerSearch, KeyPairBoolData item) {
        Common.hideSoftKeyboard(this);
        if (item == null) return;
        if (item.getId() == null) return;

        AppDebugLog.print("religion_id in onItemsSelected : " + item.getId());
        switch (singleSpinnerSearch.getId()) {
            case R.id.spin_religion:
                religion_id = item.getId();
                resetCaste();
                if (religion_id != null && !religion_id.equals("0")) {
                    getDependentList(CASTE_LIST, religion_id);
                }
                break;
            case R.id.spin_caste:
                caste_id = item.getId();
                break;
            case R.id.spin_tongue:
                tongue_id = item.getId();
                break;
            case R.id.spin_country:
                country_id = item.getId();
                resetStateAndCity();
                if (country_id != null && !country_id.equals("0")) {
                    getDependentList(STATE_LIST, country_id);
                }
                break;
            case R.id.spin_state:
                state_id = item.getId();
                resetCity();
                if (state_id != null && !state_id.equals("0")) {
                    getDependentList(CITY_LIST, state_id);
                }
                break;
            case R.id.spin_city:
                city_id = item.getId();
                break;
            case R.id.spin_mari:
                mari_id = item.getId();
                if (mari_id == null) {
                    spin_t_child.setEnabled(false);
                    spin_t_child.setSelection(0);
                    spin_child_status.setEnabled(false);
                    spin_child_status.setSelection(0);
                    status_child_id = "";
                    total_child_id = "";
                    lay_t_child.setVisibility(View.GONE);
                    lay_status_child.setVisibility(View.GONE);
                } else if (mari_id.equals("") || mari_id.equals("Unmarried")) {
                    spin_t_child.setEnabled(false);
                    spin_t_child.setSelection(0);
                    spin_child_status.setEnabled(false);
                    spin_child_status.setSelection(0);
                    status_child_id = "";
                    total_child_id = "";
                    lay_t_child.setVisibility(View.GONE);
                    lay_status_child.setVisibility(View.GONE);
                } else {
                    lay_t_child.setVisibility(View.VISIBLE);
                    lay_status_child.setVisibility(View.VISIBLE);
                    spin_t_child.setEnabled(true);
                    spin_child_status.setEnabled(true);
                }
                break;
            case R.id.spin_t_child:
                total_child_id = item.getId();

                if (total_child_id != null && total_child_id.equals("0")) {
                    status_child_id = "";
                    spin_child_status.setEnabled(false);
                    spin_child_status.setSelection(0);
                    lay_status_child.setVisibility(View.GONE);
                } else {
                    lay_status_child.setVisibility(View.VISIBLE);
                    spin_child_status.setEnabled(true);
                }
                break;
            case R.id.spin_child_status:
                status_child_id = item.getId();
                break;
            case R.id.spin_emp_in:
                emp_id = item.getId();
                break;
            case R.id.spin_income:
                income_id = item.getId();
                break;
            case R.id.spin_occupation:
                occu_id = item.getId();
                break;
            case R.id.spin_designation:
                desig_id = item.getId();
                break;
            case R.id.spin_height:
                hite_id = item.getId();
                break;
            case R.id.spin_weight:
                weight_id = item.getId();
                break;
            case R.id.spin_eat:
                eat_id = item.getId();
                break;
            case R.id.spin_smok:
                smok_id = item.getId();
                break;
            case R.id.spin_drink:
                drink_id = item.getId();
                break;
            case R.id.spin_body:
                body_id = item.getId();
                break;
            case R.id.spin_skin:
                skin_id = item.getId();
                break;
            case R.id.spin_manglik:
                manglik_id = item.getId();
                break;
            case R.id.spin_star:
                star_id = item.getId();
                break;
            case R.id.spin_horo:
                horo_id = item.getId();
                break;
            case R.id.spin_moon:
                moon_id = item.getId();
                break;
        }
    }
    //end


    private void getDependentList(final String tag, String id) {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("get_list", tag);
        param.put("currnet_val", id);
        param.put("multivar", "");
        param.put("retun_for", "");

        ApiRequestResponse apiRequestResponse = new ApiRequestResponse();
        apiRequestResponse.postRequest(this, AppConstants.common_depedent_list, param, this, tag);
    }

    private void setupSearchDropDown(MultiSpinnerSearch spinner, String hint, String listJsonKey) {
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(MyApplication.getSpinDataStr());
        spinner.setItems(spinner, common.getSpinnerListFromArray(gsonObject.get(listJsonKey).getAsJsonArray()), -1, this, hint);
    }

    private void setupSearchDropDown(SingleSpinnerSearch spinner, String hint, String listJsonKey) {
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(MyApplication.getSpinDataStr());
        spinner.setItems(spinner, common.getSpinnerListFromArray(gsonObject.get(listJsonKey).getAsJsonArray()), -1, this, hint);
    }

    private void setupInitializeSearchDropDown(SingleSpinnerSearch spinner, String hint) {
        spinner.setItems(spinner, new ArrayList<>(), -1, this, hint);
    }

    private void resetStateAndCity() {
        spin_state.setSelection(0);
        state_id = "";

        resetCity();
    }

    private void resetCity() {
        spin_city.setSelection(0);
        city_id = "";
    }

    private void resetCaste() {
        spin_caste.setSelection(0);
        caste_id = "";
    }
    //TODO end dropdown related code

    //TODO image selection & capture related code
    private void fromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }

    private void fromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            openFileChooser();
        }else if(requestCode == SELECT_PICTURE){
            if (resultCode == Activity.RESULT_OK && data != null) {
                AppDebugLog.print("image pick : "+data);
                try {
                    String tempPath = ContentUriUtils.INSTANCE.getFilePath(this, data.getData());
                    AppDebugLog.print("tempPath : "+tempPath);
                    originalFile = new File(tempPath);
                    originalFilePath = tempPath;
                    cropImage(new File(tempPath));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    AppDebugLog.print("Error in image pick1");
                }
            }else{
                AppDebugLog.print("Error in image pick2");
            }
        } else if (requestCode == CROP_PIC) {
            if (resultCode == Activity.RESULT_OK) {
                cropUri = UCrop.getOutput(data);
                cropFilePath = cropUri.getPath();

                AppDebugLog.print("cropUri.path : " + cropUri.getPath());
                compressedFile = common.getCompressedImageFile(this, new File(cropUri.getPath()));

                //show image in image view
                Picasso.get().load(cropUri).error(R.drawable.placeholder).placeholder(
                        R.drawable.placeholder).into(img_profile);

                isImageSelect = true;

            }
        } else {
            easyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                @Override
                public void onMediaFilesPicked(MediaFile[] mediaFiles, MediaSource mediaSource) {
                    for (MediaFile mediaFile : mediaFiles) {
                        AppDebugLog.print("file : " + mediaFile.getFile().getAbsolutePath());
                        switch (mediaSource) {
                            case DOCUMENTS:
                            case CAMERA_IMAGE:
                            case GALLERY:
                                originalFile = mediaFile.getFile();
                                originalFilePath = mediaFile.getFile().getPath();
                                cropImage(mediaFile.getFile());
                                break;

                            default:
                                cropImage(mediaFile.getFile());
                        }
                    }
                }

                @Override
                public void onImagePickerError(Throwable error, MediaSource source) {
                    super.onImagePickerError(error, source);
                }
            });
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private void cropImage(File attachmentFile) {
        Uri uri = Uri.fromFile(attachmentFile);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageExtension = Common.getExtensionFromPath(Common.getPath(RegisterFirstActivity.this, uri));
        AppDebugLog.print("imageExtension : " + imageExtension);

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), timeStamp + imageExtension)));
        uCrop.withAspectRatio(2, 3);
        uCrop.withMaxResultSize(512, 620);
        UCrop.Options options = new UCrop.Options();

        options.setToolbarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        options.setToolbarWidgetColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        options.setRootViewBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

        uCrop.withOptions(options);
        uCrop.start(this, CROP_PIC);

    }

    public String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
    //TODO end image selection related code

    private void singleTextView(TextView textView) {
        String clickableTextStr = "Terms and Conditions.";
        SpannableStringBuilder spanText = new SpannableStringBuilder();
        spanText.append("By submitting you agree our " + clickableTextStr);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                openCMSDataDialog();
            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(ContextCompat.getColor(RegisterFirstActivity.this, R.color.blue_color));    // you can use custom color
                textPaint.setUnderlineText(false);    // this remove the underline
            }
        }, spanText.length() - clickableTextStr.length(), spanText.length(), 0);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(ContextCompat.getColor(RegisterFirstActivity.this, R.color.transparent));
        textView.setText(spanText, TextView.BufferType.SPANNABLE);
        //  lblTerms.setText(Html.fromHtml(getString(R.string.lbl_service_request)), TextView.BufferType.SPANNABLE);
    }

    private void openCMSDataDialog() {
        Intent intent = new Intent(this, AllCmsActivity.class);
        intent.putExtra(AppConstants.KEY_INTENT, "term");
        startActivity(intent);
    }

    @Override
    public void onSuccess(JsonObject data, @Nullable String requestTag) {
        common.hideProgressRelativeLayout(loader);
        switch (requestTag) {
            case DDR_DATA:
                session.setUserData(SessionManager.TOKEN, data.get("tocken").getAsString());
                MyApplication.setSpinDataStr(data.get("data").toString());
                initDropDownData();
                break;
            case CASTE_LIST:
                spin_caste.setItems(spin_caste, common.getSpinnerListFromArray(data.get("data").getAsJsonArray()), -1, this, "Caste*");
                break;
            case STATE_LIST:
                spin_state.setItems(spin_state, common.getSpinnerListFromArray(data.get("data").getAsJsonArray()), -1, this, "State*");
                break;
            case CITY_LIST:
                spin_city.setItems(spin_city, common.getSpinnerListFromArray(data.get("data").getAsJsonArray()), -1, this, "City*");
                break;
            case STEP_1:
                Toast.makeText(getApplicationContext(), data.get("errmessage").getAsString(), Toast.LENGTH_SHORT).show();
                ragister_id = data.get("id").getAsString();
                ragi_scroll.scrollTo(0, 0);
                lay_first.setVisibility(View.GONE);
                lay_second.setVisibility(View.VISIBLE);
                page_name = STEP_2;

                sendEmailApi(data.get("id").getAsString());

                break;
            case "send_mail_member":
                AppDebugLog.print("send_mail_member response : "+data.toString());
                break;
            case STEP_2:
                Toast.makeText(getApplicationContext(), data.get("errmessage").getAsString(), Toast.LENGTH_SHORT).show();
                ragi_scroll.scrollTo(0, 0);
                lay_third.setVisibility(View.VISIBLE);
                lay_second.setVisibility(View.GONE);
                page_name = STEP_3;
                break;
            case STEP_3:
                Toast.makeText(getApplicationContext(), data.get("errmessage").getAsString(), Toast.LENGTH_SHORT).show();
                ragi_scroll.scrollTo(0, 0);
                lay_four.setVisibility(View.VISIBLE);
                lay_third.setVisibility(View.GONE);
                page_name = STEP_4;
                break;
            case STEP_4:
                Toast.makeText(getApplicationContext(), data.get("errmessage").getAsString(), Toast.LENGTH_SHORT).show();
                ragi_scroll.scrollTo(0, 0);
                lay_four.setVisibility(View.GONE);
                lay_five.setVisibility(View.VISIBLE);
                page_name = STEP_5;
                break;
            case STEP_5:
                Toast.makeText(getApplicationContext(), data.get("errmessage").getAsString(), Toast.LENGTH_SHORT).show();
                ragi_scroll.scrollTo(0, 0);
                lay_six.setVisibility(View.VISIBLE);
                lay_five.setVisibility(View.GONE);
                page_name = STEP_6;
                break;
            case STEP_6:
                Intent i = new Intent(getApplicationContext(), SuccessActivity.class);
                i.putExtra("ragistered_id", ragister_id);
                startActivity(i);
                finish();
                break;
        }
    }

    @Override
    public void onError(boolean isError, @Nullable String requestTag) {
        common.hideProgressRelativeLayout(loader);
    }

    private void sendEmailApi(String memberId) {
        HashMap<String, String> param = new HashMap<>();
        param.put("member_id", memberId);
        ApiRequestResponse apiRequestResponse = new ApiRequestResponse();
        apiRequestResponse.postRequest(this, AppConstants.send_mail_member, param, this, "send_mail_member");
    }
}
