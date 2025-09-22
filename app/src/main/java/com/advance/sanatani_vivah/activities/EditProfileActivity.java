package com.advance.sanatani_vivah.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SingleSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hbb20.CountryCodePicker;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener, SpinnerListener {
    public static final String KEY_BASIC = "basic";
    public static final String KEY_RELIGION = "religion";
    public static final String KEY_PROFILE = "profile";
    public static final String KEY_EDUCATION = "education";
    public static final String KEY_LIFE = "life";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_FAMILY = "family";

    private TextInputEditText et_f_name, et_l_name, et_dob, et_birth_place, et_birth_time, et_sub_caste, et_gothra,
            et_mobile, et_phone, et_time_call, et_father_name, et_father_ocu, et_mother_name, et_mother_ocu;
    private EditText et_about,et_hoby,et_address,et_about_family;
    private SingleSpinnerSearch spin_religion, spin_mari, spin_t_child, spin_child_status, spin_tongue, spin_height, spin_weight,
            spin_body, spin_eat, spin_smok, spin_drink, spin_skin, spin_blood,
            spin_created, spin_reference, spin_caste, spin_manglik,spin_star,spin_horo,spin_moon,
            spin_country, spin_state, spin_city, spin_residence, spin_emp_in, spin_income, spin_occupation, spin_designation,
            spin_family_type, spin_family_status, spin_no_bro, spin_no_mari_bro, spin_no_sis, spin_no_mari_sis;
    private MultiSpinnerSearch spin_lang, spin_edu;
    private CountryCodePicker spin_code;
    private RelativeLayout lay_child_status, lay_t_child;
    private Button btn_basic, btn_life, btn_about, btn_reli, btn_loca, btn_edu, btn_family;
    private Common common;
    private SessionManager session;
    private final Calendar myCalendar = Calendar.getInstance();
    private final Calendar mcurrentTime = Calendar.getInstance();
    private String pageTag = "";
    private LinearLayout lay_basic, lay_life, lay_about, lay_reli, lay_loca, lay_edu, lay_family;
    private String religion_id = "", caste_id = "", tongue_id = "",
            country_id = "", state_id = "", city_id = "", mari_id = "", total_child_id = "", status_child_id,
            edu_id = "", emp_id = "", income_id = "", occu_id = "", desig_id = "", hite_id = "", weight_id = "", eat_id = "", smok_id = "", drink_id = "",
            body_id = "", skin_id = "", manglik_id = "", star_id = "", horo_id = "", moon_id = "", lang_id = "", blood_id = "", created_id = "",
            reference_id = "", resi_id = "", code_id = "", family_type_id = "", family_status_id = "", no_bro_id = "", no_mari_bro_id = "",
            no_sis_id = "", no_mari_sis_id = "";

    private RelativeLayout loader;
    private SimpleDateFormat mFormat = null;
    private boolean isLoaded = false;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initialize();
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });
    }

    private void initialize() {
        common = new Common(this);
        session = new SessionManager(this);

        setToolbar();

        loader = findViewById(R.id.loader);
        lay_basic = findViewById(R.id.lay_basic);
        lay_life = findViewById(R.id.lay_life);
        lay_about = findViewById(R.id.lay_about);
        lay_reli = findViewById(R.id.lay_reli);
        lay_loca = findViewById(R.id.lay_loca);
        lay_edu = findViewById(R.id.lay_edu);
        lay_family = findViewById(R.id.lay_family);
        lay_child_status = findViewById(R.id.lay_child_status);
        lay_t_child = findViewById(R.id.lay_t_child);
        btn_basic = findViewById(R.id.btn_basic);
        btn_life = findViewById(R.id.btn_life);
        btn_about = findViewById(R.id.btn_about);
        btn_reli = findViewById(R.id.btn_reli);
        btn_loca = findViewById(R.id.btn_loca);
        btn_edu = findViewById(R.id.btn_edu);
        btn_family = findViewById(R.id.btn_family);

        btn_basic.setOnClickListener(this);
        btn_life.setOnClickListener(this);
        btn_about.setOnClickListener(this);
        btn_reli.setOnClickListener(this);
        btn_loca.setOnClickListener(this);
        btn_edu.setOnClickListener(this);
        btn_family.setOnClickListener(this);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("pageTag")) {
                pageTag = b.getString("pageTag");
                switch (pageTag) {
                    case KEY_BASIC:
                        lay_basic.setVisibility(View.VISIBLE);
                        toolbar.setTitle("Basic Details");
                        break;
                    case KEY_RELIGION:
                        lay_reli.setVisibility(View.VISIBLE);
                        toolbar.setTitle("Religion Information");
                        break;
                    case KEY_PROFILE:
                        lay_about.setVisibility(View.VISIBLE);
                        toolbar.setTitle("About Us & Hobby");
                        break;
                    case KEY_EDUCATION:
                        lay_edu.setVisibility(View.VISIBLE);
                        toolbar.setTitle("Education & Occupation Information");
                        break;
                    case KEY_LIFE:
                        lay_life.setVisibility(View.VISIBLE);
                        toolbar.setTitle("Life Style Details");
                        break;
                    case KEY_LOCATION:
                        lay_loca.setVisibility(View.VISIBLE);
                        toolbar.setTitle("Contact Details");
                        break;
                    case KEY_FAMILY:
                        lay_family.setVisibility(View.VISIBLE);
                        toolbar.setTitle("Family Details");
                        break;
                }
            }
        }

        try {
            initData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initData() throws JSONException {
        if (MyApplication.getSpinData() != null) {
            switch (pageTag) {
                case KEY_BASIC:
                    et_f_name = findViewById(R.id.et_f_name);
                    et_l_name = findViewById(R.id.et_l_name);
                    et_dob = findViewById(R.id.et_dob);


                    final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    };

                    et_dob.setOnClickListener(v -> {
                        //For above 18 years+ date
                        Calendar maxDateCalendar = Calendar.getInstance();
                        maxDateCalendar.add(Calendar.YEAR, -18);
                        maxDateCalendar.add(Calendar.DATE, -1);

                        //For below 65 years date
                        Calendar minDateCalendar = Calendar.getInstance();
                        minDateCalendar.add(Calendar.YEAR, -90);
                        minDateCalendar.add(Calendar.DATE, 1);

                        DatePickerDialog dialog = new DatePickerDialog(EditProfileActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                        //set min & max date in date picker
                        dialog.getDatePicker().setMaxDate(maxDateCalendar.getTime().getTime());
                        dialog.getDatePicker().setMinDate(minDateCalendar.getTime().getTime());
                        dialog.show();
                    });

                    spin_mari = findViewById(R.id.spin_mari);
                    setupSearchDropDown(spin_mari, "Marital Status", "marital_status");

                    spin_t_child = findViewById(R.id.spin_t_child);
                    setupSearchDropDown(spin_t_child, "Total Children", "total_children");

                    spin_child_status = findViewById(R.id.spin_child_status);
                    setupSearchDropDown(spin_child_status, "Status Children", "status_children");

                    spin_tongue = findViewById(R.id.spin_tongue);
                    setupSearchDropDown(spin_tongue, "Mother Tongue", "mothertongue_list");

                    spin_lang = findViewById(R.id.spin_lang);
                    setupSearchDropDown(spin_lang, "Language Known*", "mothertongue_list");

                    spin_height = findViewById(R.id.spin_height);
                    setupSearchDropDown(spin_height, "Height", "height_list");

                    spin_weight = findViewById(R.id.spin_weight);
                    setupSearchDropDown(spin_weight, "Weight", "weight_list");
                    break;
                case KEY_RELIGION://,
                    et_sub_caste = findViewById(R.id.et_sub_caste);
                    et_gothra = findViewById(R.id.et_gothra);


                    spin_religion = findViewById(R.id.spin_religion);
                    setupSearchDropDown(spin_religion, "Religion*", "religion_list");

                    spin_caste = findViewById(R.id.spin_caste);
                    setupInitializeSearchDropDown(spin_caste, "Caste*");

                    spin_manglik = findViewById(R.id.spin_manglik);
                    setupSearchDropDown(spin_manglik, "Manglik", "manglik");

                    spin_star = findViewById(R.id.spin_star);
                    setupSearchDropDown(spin_star, "Star", "star_list");

                    spin_horo = findViewById(R.id.spin_horo);
                    setupSearchDropDown(spin_horo, "Horoscope", "horoscope");

                    spin_moon = findViewById(R.id.spin_moon);
                    setupSearchDropDown(spin_moon, "Moonsign", "moonsign_list");
                    break;
                case KEY_PROFILE:
                    et_about = findViewById(R.id.et_about);
                    et_hoby = findViewById(R.id.et_hoby);
                    et_birth_place = findViewById(R.id.et_birth_place);
                    et_birth_time = findViewById(R.id.et_birth_time);


                    et_birth_time.setOnClickListener(v -> {
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(EditProfileActivity.this, (timePicker, selectedHour, selectedMinute) -> {
                            mcurrentTime.set(Calendar.HOUR, selectedHour);
                            mcurrentTime.set(Calendar.MINUTE, selectedMinute);

                            if (mFormat == null)
                                mFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

                            et_birth_time.setText(Common.get12HrTime(selectedHour, selectedMinute));
                        }, hour, minute, false);
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();
                    });

                    spin_created = findViewById(R.id.spin_created);
                    setupSearchDropDown(spin_created, "Created By", "profileby");

                    spin_reference = findViewById(R.id.spin_reference);
                    setupSearchDropDown(spin_reference, "Reference", "reference");
                    break;
                case KEY_EDUCATION:
                    spin_edu = findViewById(R.id.spin_edu);
                    setupSearchDropDown(spin_edu, "Education", "education_list");

                    spin_emp_in = findViewById(R.id.spin_emp_in);
                    setupSearchDropDown(spin_emp_in, "Employee In", "employee_in");

                    spin_income = findViewById(R.id.spin_income);
                    setupSearchDropDown(spin_income, "Annual Income", "income");

                    spin_occupation = findViewById(R.id.spin_occupation);
                    setupSearchDropDown(spin_occupation, "Occupation", "occupation_list");

                    spin_designation = findViewById(R.id.spin_designation);
                    setupSearchDropDown(spin_designation, "Designation", "designation_list");
                    break;
                case KEY_LIFE:
                    spin_body = findViewById(R.id.spin_body);
                    setupSearchDropDown(spin_body, "Body Type", "bodytype");

                    spin_eat = findViewById(R.id.spin_eat);
                    setupSearchDropDown(spin_eat, "Eating Habit", "diet");

                    spin_smok = findViewById(R.id.spin_smok);
                    setupSearchDropDown(spin_smok, "Smoke Habit", "smoke");

                    spin_drink = findViewById(R.id.spin_drink);
                    setupSearchDropDown(spin_drink, "Drink Habit", "drink");

                    spin_skin = findViewById(R.id.spin_skin);
                    setupSearchDropDown(spin_skin, "Skin Tone", "complexion");

                    spin_blood = findViewById(R.id.spin_blood);
                    setupSearchDropDown(spin_blood, "Blood Group", "blood_group");
                    break;
                case KEY_LOCATION:
                    et_address = findViewById(R.id.et_address);
                    et_mobile = findViewById(R.id.et_mobile);
                    et_phone = findViewById(R.id.et_phone);
                    et_time_call = findViewById(R.id.et_time_call);
                    spin_code = findViewById(R.id.spin_code);


                    spin_country = findViewById(R.id.spin_country);
                    setupSearchDropDown(spin_country, "Country", "country_list");

                    spin_state = findViewById(R.id.spin_state);
                    setupInitializeSearchDropDown(spin_state, "State");

                    spin_city = findViewById(R.id.spin_city);
                    setupInitializeSearchDropDown(spin_city, "City");

                    spin_residence = findViewById(R.id.spin_residence);
                    setupSearchDropDown(spin_residence, "Residence", "residence");
                    break;
                case KEY_FAMILY:
                    et_father_name = findViewById(R.id.et_father_name);
                    et_father_ocu = findViewById(R.id.et_father_ocu);
                    et_mother_name = findViewById(R.id.et_mother_name);
                    et_mother_ocu = findViewById(R.id.et_mother_ocu);
                    et_about_family = findViewById(R.id.et_about_family);


                    spin_family_type = findViewById(R.id.spin_family_type);
                    setupSearchDropDown(spin_family_type, "Family Type", "family_type");

                    spin_family_status = findViewById(R.id.spin_family_status);
                    setupSearchDropDown(spin_family_status, "Family Status", "family_status");

                    spin_no_bro = findViewById(R.id.spin_no_bro);
                    setupSearchDropDown(spin_no_bro, "No Of Brothers", "no_of_brothers");

                    spin_no_mari_bro = findViewById(R.id.spin_no_mari_bro);
                    setupSearchDropDown(spin_no_mari_bro, "No Of Married Brothers", "no_marri_brother");

                    spin_no_sis = findViewById(R.id.spin_no_sis);
                    setupSearchDropDown(spin_no_sis, "No Of Sisters", "no_of_brothers");

                    spin_no_mari_sis = findViewById(R.id.spin_no_mari_sis);
                    setupSearchDropDown(spin_no_mari_sis, "No Of Married Sisters", "no_marri_sister");
                    break;
            }

            getMyProfile();
        } else {
            getList();
        }
    }

    //TODO api calls related code
    private void getMyProfile() {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));

        common.makePostRequest(AppConstants.get_my_profile, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                session.setUserData(SessionManager.TOKEN, object.getString("tocken"));
                if (object.getString("status").equals("success")) {
                    JSONObject data = object.getJSONObject("data");

                    switch (pageTag) {
                        case KEY_BASIC:
                            et_f_name.setText(data.getString("firstname"));
                            et_l_name.setText(data.getString("lastname"));
                            mari_id = data.getString("marital_status");
                            total_child_id = data.getString("total_children");
                            status_child_id = data.getString("status_children");
                            tongue_id = data.getString("mother_tongue");
                            hite_id = data.getString("height");
                            weight_id = data.getString("weight");
                            lang_id = data.getString("languages_known");
                            if (!data.getString("birthdate").equals("") &&
                                    !data.getString("birthdate").equals("0000-00-00")) {
                                AppDebugLog.print("birthDate : " + data.getString("birthdate"));
                                String[] arr = data.getString("birthdate").split("-");
                                myCalendar.set(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]) - 1, Integer.parseInt(arr[2]));

                                updateLabel();
                            }

                            spin_mari.setSelection(mari_id);
                            spin_t_child.setSelection(total_child_id);
                            spin_child_status.setSelection(status_child_id);
                            spin_tongue.setSelection(tongue_id);
                            spin_height.setSelection(hite_id);
                            spin_weight.setSelection(weight_id);

                            spin_lang.setSelection(lang_id);

                            break;
                        case KEY_RELIGION:
                            et_sub_caste.setText(data.getString("subcaste"));
                            et_gothra.setText(data.getString("gothra"));

                            religion_id = data.getString("religion");
                            if (!religion_id.equals(""))
                                common.hideProgressRelativeLayout(loader);
                            caste_id = data.getString("caste");
                            manglik_id = data.getString("manglik");
                            star_id = data.getString("star");
                            horo_id = data.getString("horoscope");
                            moon_id = data.getString("moonsign");
                            Log.d("resp", caste_id + "  profile");

                            spin_religion.setSelection(religion_id);
                            spin_caste.setSelection(caste_id);
                            spin_star.setSelection(star_id);
                            spin_horo.setSelection(horo_id);
                            spin_moon.setSelection(moon_id);
                            spin_manglik.setSelection(manglik_id);
                            break;
                        case KEY_PROFILE:
                            et_about.setText(data.getString("profile_text"));
                            et_hoby.setText(data.getString("hobby"));
                            if (!data.getString("birthplace").equals("null"))
                                et_birth_place.setText(data.getString("birthplace"));
                            else
                                et_birth_place.setText("");
                            et_birth_time.setText(data.getString("birthtime"));

                            if (!data.getString("birthtime").equals("") &&
                                    !data.getString("birthtime").equals("00:00") &&
                                    !data.getString("birthtime").equals("Not Available")) {
                                try {
                                    String[] arr = data.getString("birthtime").split(" ");
                                    String[] arr1 = arr[0].split(":");
                                    mcurrentTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(arr1[0]));
                                    mcurrentTime.set(Calendar.MINUTE, Integer.parseInt(arr1[1]));
                                } catch (Exception e) {
                                    AppDebugLog.print("Exception in getMyProfile :" + e.getMessage());
                                }
                            }

                            created_id = data.getString("profileby");
                            reference_id = data.getString("reference");
                            spin_created.setSelection(created_id);
                            spin_reference.setSelection(reference_id);
                            break;
                        case KEY_EDUCATION:
                            edu_id = data.getString("education_detail");
                            emp_id = data.getString("employee_in");
                            income_id = data.getString("income");
                            occu_id = data.getString("occupation");
                            desig_id = data.getString("designation");
                            spin_edu.setSelection(edu_id);

                            spin_emp_in.setSelection(emp_id);
                            spin_income.setSelection(income_id);
                            spin_occupation.setSelection(occu_id);
                            spin_designation.setSelection(desig_id);
                            break;
                        case KEY_LIFE:
                            body_id = data.getString("bodytype");
                            eat_id = data.getString("diet");
                            smok_id = data.getString("smoke");
                            drink_id = data.getString("drink");
                            skin_id = data.getString("complexion");
                            blood_id = data.getString("blood_group");

                            spin_body.setSelection(body_id);
                            spin_eat.setSelection(eat_id);
                            spin_smok.setSelection(smok_id);
                            spin_drink.setSelection(drink_id);
                            spin_skin.setSelection(skin_id);
                            spin_blood.setSelection(blood_id);
                            break;
                        case KEY_LOCATION:
                            country_id = data.getString("country_id");
                            if (country_id != null && !country_id.equals("0") && !country_id.equals("Select Country")) {
                                getDepedentList("state_list", country_id);
                            }
                            state_id = data.getString("state_id");

                            if (state_id != null && !state_id.equals("0") && !state_id.equals("Select State")) {
                                getDepedentList("city_list", state_id);
                            }
                            city_id = data.getString("city");
                            resi_id = data.getString("residence");
                            spin_residence.setSelection(resi_id);
                            spin_country.setSelection(country_id);

                            String[] arr_mob = data.getString("mobile").split("-");

                            if (!data.getString("address").equals("null")) {
                                if(AppConstants.BASE_URL.equalsIgnoreCase("https://narjisinfotech.in/megaDemo/")){
                                    et_address.setText("Disabled in demo");
                                }else{
                                    et_address.setText(data.getString("address"));
                                }
                            } else et_address.setText("");

                            if (arr_mob.length == 2) {
//                                et_mobile.setText(arr_mob[1]);
                                if(AppConstants.BASE_URL.equalsIgnoreCase("https://narjisinfotech.in/megaDemo/")){
                                    et_mobile.setText("Disabled");
                                }else{
                                    et_mobile.setText(arr_mob[1]);
                                }
                                spin_code.setCountryForPhoneCode(Integer.parseInt(arr_mob[0]));
                            }
                            if(AppConstants.BASE_URL.equalsIgnoreCase("https://narjisinfotech.in/megaDemo/")){
                                et_phone.setText("Disabled");
                            }else{
                                et_phone.setText(data.getString("phone"));
                            }

                            et_time_call.setText(data.getString("time_to_call"));
                            break;
                        case KEY_FAMILY:
                            family_type_id = data.getString("family_type");
                            family_status_id = data.getString("family_status");
                            no_bro_id = data.getString("no_of_brothers");
                            no_mari_bro_id = data.getString("no_of_married_brother");
                            no_sis_id = data.getString("no_of_sisters");
                            no_mari_sis_id = data.getString("no_of_married_sister");

                            spin_family_type.setSelection(family_type_id);
                            spin_family_status.setSelection(family_status_id);
                            spin_no_bro.setSelection(no_bro_id);
                            spin_no_sis.setSelection(no_sis_id);

                            et_father_name.setText(data.getString("father_name"));
                            et_father_ocu.setText(data.getString("father_occupation"));
                            et_mother_name.setText(data.getString("mother_name"));
                            et_mother_ocu.setText(data.getString("mother_occupation"));
                            et_about_family.setText(data.getString("family_details"));
                            break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }
        }, error -> {
            common.hideProgressRelativeLayout(loader);
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });
    }

    private void getList() {
        common.showProgressRelativeLayout(loader);
        common.makePostRequest(AppConstants.common_list, new HashMap<String, String>(), response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                session.setUserData(SessionManager.TOKEN, object.getString("tocken"));

                MyApplication.setSpinData(object);
                initData();
            } catch (JSONException e) {
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }

        }, error -> {
            Log.d("resp", error.getMessage() + "   ");
            common.hideProgressRelativeLayout(loader);
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });
    }

    private void getDepedentList(final String tag, final String id) {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("get_list", tag);
        param.put("currnet_val", id);
        param.put("multivar", "");
        param.put("retun_for", "");

        JsonParser jsonParser = new JsonParser();

        common.makePostRequest(AppConstants.common_depedent_list, param, response -> {
            // Log.d("resp",tag+"   ");
            Log.d("matre", "getDepedentList   " + tag + "    " + id);
            isLoaded = true;
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                session.setUserData(SessionManager.TOKEN, object.getString("tocken"));
                if (object.getString("status").equals("success")) {

                    if ("caste_list".equals(tag)) {
                        JsonArray jsonArray = (JsonArray) jsonParser.parse(object.getJSONArray("data").toString());
                        spin_caste.setItems(spin_caste, common.getSpinnerListFromArray(jsonArray), -1, this, "Caste*");
                        spin_caste.setSelection(caste_id);
                    } else if ("state_list".equals(tag)) {
                        JsonArray jsonArray1 = (JsonArray) jsonParser.parse(object.getJSONArray("data").toString());
                        spin_state.setItems(spin_state, common.getSpinnerListFromArray(jsonArray1), -1, this, "State*");
                        spin_state.setSelection(state_id);
                    } else if ("city_list".equals(tag)) {
                        JsonArray jsonArray1 = (JsonArray) jsonParser.parse(object.getJSONArray("data").toString());
                        spin_city.setItems(spin_city, common.getSpinnerListFromArray(jsonArray1), -1, this, "City");
                        if(!AppConstants.BASE_URL.equalsIgnoreCase("https://narjisinfotech.in/megaDemo/")){
                            spin_city.setSelection(city_id);
                        }

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }
        }, error -> {
            Log.d("resp", error.getMessage() + "   ");
            common.hideProgressRelativeLayout(loader);
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });

    }

    private void submitData(HashMap<String, String> param) {
        common.showProgressRelativeLayout(loader);

        common.makePostRequest(AppConstants.edit_profile, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                common.showToast(object.getString("errmessage"));
                if (object.getString("status").equals("success")) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", "reload");
                    returnIntent.putExtra("tabid", "my");
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                common.hideProgressRelativeLayout(loader);
                if (error.networkResponse != null) {
                    common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
                }
            }
        });
    }
    //TODO end api calls related code

    //TODO date selection related code
    public String changeDate(String time) {
        String inputPattern = AppConstants.BIRTH_DATE_FORMAT;
        String outputPattern = AppConstants.BIRTH_DATE_FORMAT;
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
        String myFormat = AppConstants.BIRTH_DATE_FORMAT; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_dob.setText(sdf.format(myCalendar.getTime()));
    }
    //TODO end date selection related code

    //TODO callback methods
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_basic:
                validBasicData();
                break;
            case R.id.btn_life:
                validLifeData();
                break;
            case R.id.btn_about:
                validProfileData();
                break;
            case R.id.btn_reli:
                validReliData();
                break;
            case R.id.btn_loca:
                validLocaData();
                break;
            case R.id.btn_edu:
                validEduData();
                break;
            case R.id.btn_family:
                validFamilyData();
                break;
        }
    }
    //TODO end callback methods

    //TODO form validation related code
    private void validBasicData() {
        String fname = et_f_name.getText().toString().trim();
        String lname = et_l_name.getText().toString().trim();
        String dob = "";
        if (myCalendar != null) {
            String myFormat = AppConstants.BIRTH_DATE_UPLOAD_FORMAT; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            dob = sdf.format(myCalendar.getTime());
        }

        boolean isValid = true;
        if (TextUtils.isEmpty(fname)) {
            et_f_name.setError("Please enter first name");
            isValid = false;
        }
        if (TextUtils.isEmpty(lname)) {
            et_l_name.setError("Please enter last name");
            isValid = false;
        }
        if (mari_id == null || mari_id.equals("0")) {
            common.spinnerSetError(spin_mari, "Please select marital status");
            isValid = false;
        }
        if (mari_id != null && !mari_id.equals("Unmarried")) {
            if (total_child_id.equals("total")) {
                common.spinnerSetError(spin_t_child, "Please select total children");
                isValid = false;
            } else {
                if (!total_child_id.equals("0")) {
                    if (status_child_id.equals("0")) {
                        common.spinnerSetError(spin_child_status, "Please select children status");
                        isValid = false;
                    }
                }
            }
        }

        if (!isValidId(tongue_id)) {
            common.spinnerSetError(spin_tongue, "Please select mother tongue");
            isValid = false;
        }
        if (!isValidId(hite_id)) {
            common.spinnerSetError(spin_height, "Please select height");
            isValid = false;
        }
        if (!isValidId(weight_id)) {
            common.spinnerSetError(spin_weight, "Please select weight");
            isValid = false;
        }
        if (isValid) {
            HashMap<String, String> param = new HashMap<>();
            param.put("firstname", fname);
            param.put("lastname", lname);
            param.put("username", fname + " " + lname);
            param.put("marital_status", getValidId(mari_id));
            param.put("total_children", total_child_id);
            param.put("status_children", getValidId(status_child_id));
            param.put("mother_tongue", getValidId(tongue_id));
            param.put("height", hite_id);
            param.put("weight", weight_id);
            param.put("languages_known", getValidId(lang_id));
            param.put("birthdate", dob);//changeDate(
            param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
            Log.d("resp", changeDate(dob) + "   " + dob);
            submitData(param);
        }
    }

    private void validLifeData() {
        HashMap<String, String> param = new HashMap<>();
        param.put("bodytype", getValidId(body_id));
        param.put("diet", getValidId(eat_id));
        param.put("smoke", getValidId(smok_id));
        param.put("drink", getValidId(drink_id));
        param.put("complexion", getValidId(skin_id));
        param.put("blood_group", getValidId(blood_id));
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
        submitData(param);
    }

    private void validProfileData() {
        boolean isValid = true;

        String about = et_about.getText().toString().trim();
        String hoby = et_hoby.getText().toString().trim();
        String location = et_birth_place.getText().toString().trim();
        String time = et_birth_time.getText().toString().trim();

        if (!isValidId(created_id)) {
            common.spinnerSetError(spin_created, "Please select created by");
            isValid = false;
        }
        if (!isValidId(reference_id)) {
            common.spinnerSetError(spin_reference, "Please select reference by");
            isValid = false;
        }

        if (isValid) {
            HashMap<String, String> param = new HashMap<>();
            param.put("profile_text", about);
            param.put("hobby", hoby);
            param.put("birthplace", location);
            param.put("birthtime", time);
            param.put("profileby", getValidId(created_id));
            param.put("reference", getValidId(reference_id));
            param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
            submitData(param);
        }
    }

    private void validReliData() {
        String subcst = et_sub_caste.getText().toString().trim();
        String gothra = et_gothra.getText().toString().trim();
        boolean isValid = true;

        if (!isValidId(religion_id)) {
            common.spinnerSetError(spin_religion, "Please select religion");
            isValid = false;
        }
        if (!isValidId(caste_id)) {
            common.spinnerSetError(spin_caste, "Please select caste");
            isValid = false;
        }
        Log.d("resp", religion_id + "   " + caste_id);
        if (isValid) {
            HashMap<String, String> param = new HashMap<>();
            param.put("religion", getValidId(religion_id));
            param.put("caste", getValidId(caste_id));
            param.put("subcaste", subcst);
            param.put("manglik", getValidId(manglik_id));
            param.put("star", getValidId(star_id));
            param.put("horoscope", getValidId(horo_id));
            param.put("gothra", gothra);
            param.put("moonsign", getValidId(moon_id));
            param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
            submitData(param);
        }
    }

    private void validLocaData() {
        String add = et_address.getText().toString().trim();
        String mobile = et_mobile.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String time_call = et_time_call.getText().toString().trim();
        code_id = spin_code.getSelectedCountryCodeWithPlus();

        boolean isValid = true;

        if (!isValidId(country_id)) {
            common.spinnerSetError(spin_country, "Please select country");
            isValid = false;
        }
        if (!isValidId(state_id)) {
            common.spinnerSetError(spin_state, "Please select state");
            isValid = false;
        }
        if (!isValidId(city_id)) {
            common.spinnerSetError(spin_city, "Please select city");
            isValid = false;
        }
        if (TextUtils.isEmpty(mobile) || mobile.length() < 8) {
            et_mobile.setError("Please enter valid mobile number");
            isValid = false;
        }

        if (phone.length() > 0 && phone.length() < 8) {
            et_phone.setError("Please enter valid phone number");
            isValid = false;
        }
        if (!isValidId(code_id)) {
            common.spinnerSetError(spin_city, "Please select country code");
            isValid = false;
        }
        if (isValid) {
            HashMap<String, String> param = new HashMap<>();
            param.put("country_id", getValidId(country_id));
            param.put("state_id", getValidId(state_id));
            param.put("city", getValidId(city_id));
            param.put("address", add);
            param.put("country_code", getValidId(code_id));
            param.put("mobile_num", mobile);
            param.put("phone", phone);
            param.put("time_to_call", time_call);
            param.put("residence", getValidId(resi_id));
            param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
            submitData(param);
        }
    }

    private void validEduData() {
        boolean isValid = true;
        if (!isValidId(edu_id)) {
            common.spinnerSetError(spin_edu, "Please select education");
            isValid = false;
        }
        if (!isValidId(occu_id)) {
            common.spinnerSetError(spin_occupation, "Please select occupation");
            isValid = false;
        }

        if (isValid) {
            HashMap<String, String> param = new HashMap<>();
            param.put("education_detail", getValidId(edu_id));
            param.put("employee_in", getValidId(emp_id));
            param.put("income", getValidId(income_id));
            param.put("occupation", getValidId(occu_id));
            param.put("designation", getValidId(desig_id));
            param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
            submitData(param);
        }
    }

    private void validFamilyData() {
        String father_name = et_father_name.getText().toString().trim();
        String father_occupation = et_father_ocu.getText().toString().trim();
        String mother_name = et_mother_name.getText().toString().trim();
        String mother_occupation = et_mother_ocu.getText().toString().trim();
        String family_details = et_about_family.getText().toString().trim();

        HashMap<String, String> param = new HashMap<>();
        param.put("family_type", getValidId(family_type_id));
        param.put("family_status", getValidId(family_status_id));
        param.put("no_of_brothers", getValidId(no_bro_id));
        param.put("no_of_married_brother", getValidId(no_mari_bro_id));
        param.put("no_of_sisters", getValidId(no_sis_id));
        param.put("no_of_married_sister", getValidId(no_mari_sis_id));

        param.put("father_name", father_name);
        param.put("father_occupation", father_occupation);
        param.put("mother_name", mother_name);
        param.put("mother_occupation", mother_occupation);
        param.put("family_details", family_details);
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
        submitData(param);

    }

    private String getValidId(String val) {
        if (val == null || val.equals("") || val.equals("0")) {
            return "";
        }
        return val;
    }

    private boolean isValidId(String val) {
        if (val == null || val.equals("") || val.equals("0")) {
            return false;
        }
        return true;
    }
    //TODO end form validation related code

    //TODO dropdown related code
    @Override public void onItemsSelected(MultiSpinnerSearch singleSpinnerSearch) {
        Common.hideSoftKeyboard(this);
        if (singleSpinnerSearch == null) return;
        if (singleSpinnerSearch.getSelectedIdsInString()== null) return;

        switch (singleSpinnerSearch.getId()) {
            case R.id.spin_lang:
                lang_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_edu:
                edu_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
        }
    }

    @Override public void onItemsSelected(SingleSpinnerSearch singleSpinnerSearch, KeyPairBoolData item) {
        Common.hideSoftKeyboard(this);
        if (item == null) return;
        if (item.getId() == null) return;

        switch (singleSpinnerSearch.getId()) {
            case R.id.spin_family_type:
                family_type_id = item.getId();//family_type_map.get(spin_family_type.getSelectedItem().toString());
                break;
            case R.id.spin_family_status:
                family_status_id = item.getId();//family_status_map.get(spin_family_status.getSelectedItem().toString());
                break;
            case R.id.spin_no_bro:
                no_bro_id = item.getId();// no_bro_map.get(spin_no_bro.getSelectedItem().toString());
                if (isValidId(no_bro_id)) {
                    // common.setSelection(spin_no_mari_bro, no_mari_bro_list_id, no_mari_bro_id);
                    spin_no_mari_bro.setSelection(no_mari_bro_id);
                    //disBro(no_bro_id);
                }

                break;
            case R.id.spin_no_mari_bro:
                no_mari_bro_id = item.getId();// no_mari_bro_map.get(spin_no_mari_bro.getSelectedItem().toString());
                break;
            case R.id.spin_no_sis:
                no_sis_id = item.getId();// no_sis_map.get(spin_no_sis.getSelectedItem().toString());
                if (isValidId(no_sis_id)) {
//                    common.setSelection(spin_no_mari_sis, no_mari_sis_list_id, no_mari_sis_id);
//
                    spin_no_mari_sis.setSelection(no_mari_sis_id);
                    //disSis(no_sis_id);
                }
                break;
            case R.id.spin_no_mari_sis:
                no_mari_sis_id = item.getId();// no_mari_sis_map.get(spin_no_mari_sis.getSelectedItem().toString());
                break;

            case R.id.spin_religion:
                religion_id = item.getId();// reli_map.get(spin_religion.getSelectedItem().toString());
                if (religion_id != null && !religion_id.equals("0") && !religion_id.equals("")) {
                    //caste_id="0";
                    //resetCaste();
                    getDepedentList("caste_list", religion_id);
                }
                break;
            case R.id.spin_caste:
                //if (!spin_caste.getSelectedItem().toString().equals("Select Caste"))
                caste_id = item.getId();// caste_map.get(spin_caste.getSelectedItem().toString());
                break;
            case R.id.spin_manglik:
                manglik_id = item.getId();// manglik_map.get(spin_manglik.getSelectedItem().toString());
                break;
            case R.id.spin_created:
                created_id = item.getId();// created_map.get(spin_created.getSelectedItem().toString());
                break;
            case R.id.spin_reference:
                reference_id = item.getId();// reference_map.get(spin_reference.getSelectedItem().toString());
                break;
            case R.id.spin_tongue:
                tongue_id = item.getId();// tongue_map.get(spin_tongue.getSelectedItem().toString());
                break;
            case R.id.spin_body:
                body_id = item.getId();// body_map.get(spin_body.getSelectedItem().toString());
                break;
            case R.id.spin_eat:
                eat_id = item.getId();// eat_map.get(spin_eat.getSelectedItem().toString());
                break;
            case R.id.spin_smok:
                smok_id = item.getId();// smok_map.get(spin_smok.getSelectedItem().toString());
                break;
            case R.id.spin_drink:
                drink_id = item.getId();// drink_map.get(spin_drink.getSelectedItem().toString());
                break;
            case R.id.spin_skin:
                skin_id = item.getId();// skin_map.get(spin_skin.getSelectedItem().toString());
                break;
            case R.id.spin_blood:
                blood_id = item.getId();// blood_map.get(spin_blood.getSelectedItem().toString());
                break;
            case R.id.spin_country:
                if (isLoaded) {
                    country_id = item.getId();// country_map.get(spin_country.getSelectedItem().toString());
                    if (isValidId(country_id)) {//&& !country_id.equals("Select Country")
                        getDepedentList("state_list", country_id);
                    } else {
                        resetStateAndCity();
                    }
                }
                break;
            case R.id.spin_state:
                if (isLoaded) {
                    state_id = item.getId();// state_map.get(spin_state.getSelectedItem().toString());
                    if (isValidId(state_id)) {
                        getDepedentList("city_list", state_id);
                    } else {
                        resetCity();
                    }
                }
                break;
            case R.id.spin_city:
                if (isLoaded) {
                    city_id = item.getId();//city_map.get(spin_city.getSelectedItem().toString());
                }
                break;
            case R.id.spin_mari:
                mari_id = item.getId();// mari_map.get(spin_mari.getSelectedItem().toString());
                if (mari_id == null) {
                    spin_t_child.setEnabled(false);
                    spin_t_child.setSelection(0);
                    spin_child_status.setEnabled(false);
                    spin_child_status.setSelection(0);
                    status_child_id = "";
                    total_child_id = "";
                    lay_t_child.setVisibility(View.GONE);
                    lay_child_status.setVisibility(View.GONE);
                } else if (mari_id.equals("") || mari_id.equals("Unmarried")) {
                    spin_t_child.setEnabled(false);
                    spin_t_child.setSelection(0);
                    spin_child_status.setEnabled(false);
                    spin_child_status.setSelection(0);
                    status_child_id = "";
                    total_child_id = "";
                    lay_t_child.setVisibility(View.GONE);
                    lay_child_status.setVisibility(View.GONE);
                } else {
                    lay_t_child.setVisibility(View.VISIBLE);
                    lay_child_status.setVisibility(View.VISIBLE);
                    spin_t_child.setEnabled(true);
                    spin_child_status.setEnabled(true);
                }
                break;
            case R.id.spin_t_child:
                total_child_id = item.getId();// total_child_map.get(spin_t_child.getSelectedItem().toString());
                if (total_child_id != null && total_child_id.equals("0")) {
                    status_child_id = "";
                    spin_child_status.setEnabled(false);
                    spin_child_status.setSelection(0);
                    lay_child_status.setVisibility(View.GONE);
                } else {
                    lay_child_status.setVisibility(View.VISIBLE);
                    spin_child_status.setEnabled(true);
                }
                break;
            case R.id.spin_child_status:
                status_child_id = item.getId();// status_child_map.get(spin_child_status.getSelectedItem().toString());
                break;
            case R.id.spin_residence:
                resi_id = item.getId();// resi_map.get(spin_residence.getSelectedItem().toString());
                break;
            case R.id.spin_emp_in:
                emp_id = item.getId();// emp_map.get(spin_emp_in.getSelectedItem().toString());
                break;
            case R.id.spin_income:
                income_id = item.getId();//income_map.get(spin_income.getSelectedItem().toString());
                break;
            case R.id.spin_occupation:
                occu_id = item.getId();// occu_map.get(spin_occupation.getSelectedItem().toString());
                break;
            case R.id.spin_designation:
                desig_id = item.getId();// desig_map.get(spin_designation.getSelectedItem().toString());
                break;
            case R.id.spin_height:
                hite_id = item.getId();// hite_map.get(spin_height.getSelectedItem().toString());
                break;
            case R.id.spin_weight:
                weight_id = item.getId();// weight_map.get(spin_weight.getSelectedItem().toString());
                break;
        }
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

    //use for initialize drop down
    private void setupSearchDropDown(MultiSpinnerSearch spinner, String hint, String listJsonKey) {
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(MyApplication.getSpinData().toString());
        spinner.setItems(spinner, common.getSpinnerListFromArray(gsonObject.get(listJsonKey).getAsJsonArray()), -1, this, hint);
    }

    private void setupSearchDropDown(SingleSpinnerSearch spinner, String hint, String listJsonKey) {
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(MyApplication.getSpinData().toString());
        spinner.setItems(spinner, common.getSpinnerListFromArray(gsonObject.get(listJsonKey).getAsJsonArray()), -1, this, hint);
    }

    private void setupInitializeSearchDropDown(SingleSpinnerSearch spinner, String hint) {
        spinner.setItems(spinner, new ArrayList<>(), -1, this, hint);
    }

    private String listToString(List<String> list) {
        String listString = "";

        for (String s : list) {
            listString += s + ",";// \t
        }

        listString = listString.replaceAll(",$", "");
        return listString;
    }
    //TODO end dropdown related code


}
