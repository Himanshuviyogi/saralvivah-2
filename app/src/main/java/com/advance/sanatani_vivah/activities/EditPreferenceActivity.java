package com.advance.sanatani_vivah.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditPreferenceActivity extends AppCompatActivity implements View.OnClickListener, SpinnerListener {
    public static final String KEY_BASIC = "basic";
    public static final String KEY_RELIGION = "religion";
    public static final String KEY_EDUCATION = "education";
    public static final String KEY_LOCATION = "location";

    private Common common;
    private SessionManager session;
    private String pageTag = "";
    private LinearLayout lay_basic, lay_reli, lay_edu, lay_loca;
    private Button btn_basic, btn_reli, btn_loca, btn_edu;
    private EditText et_expcts;
    private SingleSpinnerSearch spin_height_from, spin_height_to, spin_age_from, spin_age_to, spin_manglik, spin_income;
    private MultiSpinnerSearch spin_looking, spin_complx, spin_body_type, spin_eat, spin_smok, spin_drink, spin_mtongue,
            spin_religion, spin_caste, spin_residence, spin_country, spin_state, spin_city, spin_edu,
            spin_emp_in, spin_occupation, spin_designation, spin_star;
    private String look_id = "", complx_id = "", body_type_id = "", eat_id = "", smoke_id = "", drink_id = "", mtongue_id = "", fage_id = "", tage_id = "", incm_id = "",
            fhite_id = "", thite_id = "", reli_id = "", manglik_id = "", caste_id = "", star_id = "", resi_id = "", country_id = "", state_id = "",
            city_id = "", edu_id = "", emp_in_id = "", occu_id = "", desi_id = "";
    private RelativeLayout loader;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_preference);

        initialize();
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Preference");
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });
    }

    private void initialize() {
        setToolbar();

        loader = findViewById(R.id.loader);

        common = new Common(this);
        session = new SessionManager(this);

        btn_basic = findViewById(R.id.btn_basic);
        btn_basic.setOnClickListener(this);
        btn_reli = findViewById(R.id.btn_reli);
        btn_reli.setOnClickListener(this);
        btn_loca = findViewById(R.id.btn_loca);
        btn_loca.setOnClickListener(this);
        btn_edu = findViewById(R.id.btn_edu);
        btn_edu.setOnClickListener(this);

        lay_basic = findViewById(R.id.lay_basic);
        lay_reli = findViewById(R.id.lay_reli);
        lay_edu = findViewById(R.id.lay_edu);
        lay_loca = findViewById(R.id.lay_loca);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("pageTag")) {
                pageTag = b.getString("pageTag");
                switch (pageTag) {
                    case KEY_BASIC:
                        toolbar.setTitle("Basic Preferences");
                        lay_basic.setVisibility(View.VISIBLE);
                        break;
                    case KEY_RELIGION:
                        toolbar.setTitle("Religion Preferences");
                        lay_reli.setVisibility(View.VISIBLE);
                        break;
                    case KEY_EDUCATION:
                        toolbar.setTitle("Education & Occupation Preferences");
                        lay_edu.setVisibility(View.VISIBLE);
                        break;
                    case KEY_LOCATION:
                        toolbar.setTitle("Location Preferences");
                        lay_loca.setVisibility(View.VISIBLE);
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
            //spin_edu,spin_emp_in,spin_income,spin_occupation,spin_designation
            switch (pageTag) {
                case KEY_EDUCATION:
                    spin_edu = findViewById(R.id.spin_edu);
                    setupSearchDropDown(spin_edu, "Education", "education_list");

                    spin_emp_in = findViewById(R.id.spin_emp_in);
                    setupSearchDropDown(spin_emp_in, "Employed In", "employee_in");

                    spin_occupation = findViewById(R.id.spin_occupation);
                    setupSearchDropDown(spin_occupation, "Occupation", "occupation_list");

                    spin_designation = findViewById(R.id.spin_designation);
                    setupSearchDropDown(spin_designation, "Designation", "designation_list");

                    spin_income = findViewById(R.id.spin_income);
                    setupSearchDropDown(spin_income, "Annual Income", "income");
                    break;
                case KEY_LOCATION:
                    spin_residence = findViewById(R.id.spin_residence);
                    setupSearchDropDown(spin_residence, "Residence", "residence");

                    spin_country = findViewById(R.id.spin_country);
                    setupSearchDropDown(spin_country, "Country", "country_list");

                    spin_state = findViewById(R.id.spin_state);
                    setupInitializeSearchDropDown(spin_state, "State");


                    spin_city = findViewById(R.id.spin_city);
                    setupInitializeSearchDropDown(spin_city, "City");
                    break;
                case KEY_RELIGION:
                    spin_religion = findViewById(R.id.spin_religion);
                    setupSearchDropDown(spin_religion, "Religion", "religion_list");

                    spin_caste = findViewById(R.id.spin_caste);
                    setupInitializeSearchDropDown(spin_caste, "Caste");

                    spin_manglik = findViewById(R.id.spin_manglik);
                    setupSearchDropDown(spin_manglik, "Manglik", "manglik");

                    spin_star = findViewById(R.id.spin_star);
                    setupSearchDropDown(spin_star, "Star", "star_list");

                    break;
                case KEY_BASIC:
                    et_expcts = findViewById(R.id.et_expcts);

                    spin_looking = findViewById(R.id.spin_looking);
                    setupSearchDropDown(spin_looking, "Looking For", "marital_status");

                    spin_complx = findViewById(R.id.spin_complx);
                    setupSearchDropDown(spin_complx, "Skin Tone", "complexion");

                    spin_height_from = findViewById(R.id.spin_height_from);
                    setupSearchDropDown(spin_height_from, "From Height", "height_list");

                    spin_height_to = findViewById(R.id.spin_height_to);
                    setupSearchDropDown(spin_height_to, "To Height", "height_list");

                    spin_age_from = findViewById(R.id.spin_age_from);
                    setupSearchDropDown(spin_age_from, "From Age", "age_rang");

                    spin_age_to = findViewById(R.id.spin_age_to);
                    setupSearchDropDown(spin_age_to, "To Age", "age_rang");

                    spin_body_type = findViewById(R.id.spin_body_type);
                    setupSearchDropDown(spin_body_type, "Body type", "bodytype");

                    spin_eat = findViewById(R.id.spin_eat);
                    setupSearchDropDown(spin_eat, "Eating Habit", "diet");

                    spin_smok = findViewById(R.id.spin_smok);
                    setupSearchDropDown(spin_smok, "Smoking Habit", "smoke");

                    spin_drink = findViewById(R.id.spin_drink);
                    setupSearchDropDown(spin_drink, "Drinking Habit", "drink");

                    spin_mtongue = findViewById(R.id.spin_mtongue);
                    setupSearchDropDown(spin_mtongue, "Mother Tongue", "mothertongue_list");
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
                        case KEY_EDUCATION:
                            edu_id = data.getString("part_education");
                            emp_in_id = data.getString("part_employee_in");
                            occu_id = data.getString("part_occupation");
                            desi_id = data.getString("part_designation");
                            incm_id = data.getString("part_income");

                            spin_edu.setSelection(edu_id);
                            spin_emp_in.setSelection(emp_in_id);
                            spin_occupation.setSelection(occu_id);
                            spin_designation.setSelection(desi_id);
                            spin_income.setSelection(incm_id);
                            break;
                        case KEY_LOCATION:
                            country_id = data.getString("part_country_living");
                            spin_country.setSelection(country_id);
                            city_id = data.getString("part_city");
                            state_id = data.getString("part_state");
                            if (country_id != null && !country_id.equals("0") && !country_id.equals(""))
                                getDepedentList("state_list", country_id);

                            if (state_id != null && !state_id.equals("0") && !state_id.equals(""))
                                getDepedentList("city_list", state_id);

                            resi_id = data.getString("part_resi_status");
                            spin_residence.setSelection(resi_id);
                            break;
                        case KEY_RELIGION:
                            reli_id = data.getString("part_religion");
                            caste_id = data.getString("part_caste");
                            if (reli_id != null && !reli_id.equals("0") && !reli_id.equals(""))
                                getDepedentList("caste_list", reli_id);
                            star_id = data.getString("part_star");
                            manglik_id = data.getString("part_manglik");

                            spin_religion.setSelection(reli_id);
                            spin_star.setSelection(star_id);
                            spin_manglik.setSelection(manglik_id);
                            break;
                        case KEY_BASIC:
                            et_expcts.setText(data.getString("part_expect"));

                            fage_id = data.getString("part_frm_age");
                            tage_id = data.getString("part_to_age");
                            fhite_id = data.getString("part_height");
                            thite_id = data.getString("part_height_to");

                            spin_age_from.setSelection(fage_id);
                            spin_age_to.setSelection(tage_id);
                            spin_height_from.setSelection(fhite_id);
                            spin_height_to.setSelection(thite_id);

                            look_id = data.getString("looking_for");
                            complx_id = data.getString("part_complexion");
                            body_type_id = data.getString("part_bodytype");
                            eat_id = data.getString("part_diet");
                            drink_id = data.getString("part_drink");
                            smoke_id = data.getString("part_smoke");
                            mtongue_id = data.getString("part_mother_tongue");

                            spin_looking.setSelection(look_id);
                            spin_complx.setSelection(complx_id);
                            spin_body_type.setSelection(body_type_id);
                            spin_eat.setSelection(eat_id);
                            spin_drink.setSelection(drink_id);
                            spin_smok.setSelection(smoke_id);
                            spin_mtongue.setSelection(mtongue_id);

                            break;

                    }
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
        });
    }

    private void getDepedentList(final String tag, String id) {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("get_list", tag);
        param.put("currnet_val", id);
        param.put("multivar", "multi");
        param.put("retun_for", "json");

        JsonParser jsonParser = new JsonParser();

        common.makePostRequest(AppConstants.common_depedent_list, param, response -> {
            Log.d("resp", tag + "   ");
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                session.setUserData(SessionManager.TOKEN, object.getString("tocken"));
                if (object.getString("status").equals("success")) {
                    if ("caste_list".equals(tag)) {
                        JsonArray jsonArray = (JsonArray) jsonParser.parse(object.getJSONArray("data").toString());
                        spin_caste.setItems(spin_caste, common.getSpinnerListFromArray(jsonArray), -1, this, "Caste");
                        spin_caste.setSelection(caste_id);
                    } else if ("state_list".equals(tag)) {
                        JsonArray jsonArray = (JsonArray) jsonParser.parse(object.getJSONArray("data").toString());
                        spin_state.setItems(spin_state, common.getSpinnerListFromArray(jsonArray), -1, this, "State");
                        spin_state.setSelection(state_id);
                    } else if ("city_list".equals(tag)) {
                        JsonArray jsonArray = (JsonArray) jsonParser.parse(object.getJSONArray("data").toString());
                        spin_city.setItems(spin_city, common.getSpinnerListFromArray(jsonArray), -1, this, "City");
                        spin_city.setSelection(city_id);
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
                    returnIntent.putExtra("tabid", "pref");
                    setResult(RESULT_OK, returnIntent);
                    finish();
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
    //TODO end api calls related code

    //TODO callback methods
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_basic:
                validBasicData();
                break;
            case R.id.btn_reli:
                validReliData();
                break;
            case R.id.btn_loca:
                validcountryData();
                break;
            case R.id.btn_edu:
                validEduData();
                break;
        }
    }
    //TODO end callback methods

    //TODO form validation related code
    private void validBasicData() {
        String expcts = et_expcts.getText().toString().trim();

        boolean isValid = true;

        if (!isValidId(look_id)) {
            common.spinnerSetError(spin_looking, "Please select Looking For");
            isValid = false;
        }
        if (!isValidId(fage_id)) {
            common.spinnerSetError(spin_age_from, "Please select from age");
            isValid = false;
        }
        if (!isValidId(tage_id)) {
            common.spinnerSetError(spin_age_to, "Please select to age");
            isValid = false;
        }
        if (!isValidId(fhite_id)) {
            common.spinnerSetError(spin_height_from, "Please select from height");
            isValid = false;
        }
        if (!isValidId(thite_id)) {
            common.spinnerSetError(spin_height_to, "Please select to height");
            isValid = false;
        }

        if (isValid) {
            HashMap<String, String> param = new HashMap<>();
            param.put("looking_for", getValidId(look_id));
            param.put("part_complexion", getValidId(complx_id));
            param.put("part_frm_age", getValidId(fage_id));
            param.put("part_to_age", getValidId(tage_id));
            param.put("part_height", getValidId(fhite_id));
            param.put("part_height_to", getValidId(thite_id));
            param.put("part_bodytype", getValidId(body_type_id));
            param.put("part_diet", getValidId(eat_id));
            param.put("part_smoke", getValidId(smoke_id));
            param.put("part_drink", getValidId(drink_id));
            param.put("part_mother_tongue", getValidId(mtongue_id));
            param.put("part_expect", getValidId(expcts));
            param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
            submitData(param);
        }
    }

    private void validReliData() {
        if (!isValidId(reli_id)) {
            common.spinnerSetError(spin_religion, "Please select religion");
            return;
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("part_religion", getValidId(reli_id));
        param.put("part_caste", getValidId(caste_id));
        param.put("part_manglik", getValidId(manglik_id));
        param.put("part_star", getValidId(star_id));
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
        submitData(param);
    }

    private void validcountryData() {

        HashMap<String, String> param = new HashMap<>();
        param.put("part_country_living", getValidId(country_id));
        param.put("part_state", getValidId(state_id));
        param.put("part_city", getValidId(city_id));
        param.put("part_resi_status", getValidId(resi_id));
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
        submitData(param);
    }

    private void validEduData() {
        HashMap<String, String> param = new HashMap<>();
        param.put("part_education", getValidId(edu_id));
        param.put("part_occupation", getValidId(occu_id));
        param.put("part_designation", getValidId(desi_id));
        param.put("part_employee_in", getValidId(emp_in_id));
        param.put("part_income", getValidId(incm_id));
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
    //TODO endform validation related code

    //TODO dropdown related code
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

    private void setupInitializeSearchDropDown(MultiSpinnerSearch spinner, String hint) {
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

    @Override
    public void onItemsSelected(MultiSpinnerSearch singleSpinnerSearch) {
        Common.hideSoftKeyboard(this);
        if (singleSpinnerSearch == null) return;
        if (singleSpinnerSearch.getSelectedIdsInString() == null) return;

        switch (singleSpinnerSearch.getId()) {
            case R.id.spin_edu:
                edu_id = singleSpinnerSearch.getSelectedIdsInString();//listToString(strings);
                break;
            case R.id.spin_emp_in:
                emp_in_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_occupation:
                occu_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_designation:
                desi_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_country:
                country_id = singleSpinnerSearch.getSelectedIdsInString();
                AppDebugLog.print("country_id in initData : " + country_id);
                if (country_id != null && !country_id.equals("0") && !country_id.equals("")) {
                    getDepedentList("state_list", country_id);
                } else {
                    resetStateAndCity();
                }
                break;
            case R.id.spin_state:
                state_id = singleSpinnerSearch.getSelectedIdsInString();
                Log.d("ressel", state_id);
                if (state_id != null && !state_id.equals("0") && !state_id.equals("")) {
                    getDepedentList("city_list", state_id);
                } else {
                    resetCity();
                }
                break;
            case R.id.spin_city:
                city_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_residence:
                resi_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_religion:
                reli_id = singleSpinnerSearch.getSelectedIdsInString();
                Log.d("ressel", reli_id);
                if (reli_id != null && !reli_id.equals("0") && !reli_id.equals(""))
                    getDepedentList("caste_list", reli_id);
                else {
                    resetCaste();
                }
                break;
            case R.id.spin_caste:
                caste_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_star:
                star_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_looking:
                look_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_complx:
                complx_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_body_type:
                body_type_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_eat:
                eat_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_smok:
                smoke_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_drink:
                drink_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_mtongue:
                mtongue_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
        }
    }

    @Override
    public void onItemsSelected(SingleSpinnerSearch singleSpinnerSearch, KeyPairBoolData item) {
        Common.hideSoftKeyboard(this);
        if (item == null) return;
        if (item.getId() == null) return;

        switch (singleSpinnerSearch.getId()) {
            case R.id.spin_height_from:
                fhite_id = item.getId();//fhite_map.get(spin_height_from.getSelectedItem().toString());
                break;
            case R.id.spin_height_to:
                thite_id = item.getId();// thite_map.get(spin_height_to.getSelectedItem().toString());
                break;
            case R.id.spin_age_from:
                fage_id = item.getId();// fage_map.get(spin_age_from.getSelectedItem().toString());
                break;
            case R.id.spin_age_to:
                tage_id = item.getId();// tage_map.get(spin_age_to.getSelectedItem().toString());
                break;
            case R.id.spin_manglik:
                manglik_id = item.getId();// manglik_map.get(spin_manglik.getSelectedItem().toString());
                break;
            case R.id.spin_income:
                incm_id = item.getId();// income_map.get(spin_income.getSelectedItem().toString());
                break;
        }
    }
}
