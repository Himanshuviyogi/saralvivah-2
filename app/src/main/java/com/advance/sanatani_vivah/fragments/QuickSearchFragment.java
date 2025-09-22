package com.advance.sanatani_vivah.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SingleSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.advance.sanatani_vivah.activities.SearchResultActivity;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuickSearchFragment extends Fragment implements SpinnerListener {
    private TextView tv_height_label, tv_min_height, tv_max_height, tv_weight_label, tv_min_age, tv_max_age;
    private Common common;
    private SessionManager session;
    private CrystalRangeSeekbar range_height, range_age;
    private MultiSpinnerSearch spin_mari, spin_religion, spin_caste, spin_tongue, spin_country, spin_state, spin_city, spin_edu;
    private Context context;
    private Button btn_save_search, btn_search;
    private RelativeLayout loader;
    private HashMap<String, String> height_map = new HashMap<>();
    private String religion_id = "", caste_id = "", tongue_id = "", country_id = "", state_id = "", city_id = "", mari_id = "", height_from = "",
            height_to = "", age_from = "", age_to = "", edu_id = "";
    private CheckBox chk_photo;

    public QuickSearchFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quick_search, container, false);

        common = new Common(getActivity());
        context = getActivity();
        session = new SessionManager(getActivity());

        try {
            initView(view);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void searchData() {
        HashMap<String, String> param = new HashMap<>();
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
        param.put("from_age", age_from);
        param.put("to_age", age_to);
        param.put("from_height", height_from);
        param.put("to_height", height_to);
        param.put("looking_for", getValue(mari_id));
        param.put("religion", getValue(religion_id));
        param.put("caste", getValue(caste_id));
        param.put("mothertongue", getValue(tongue_id));
        param.put("country", getValue(country_id));
        param.put("state", getValue(state_id));
        param.put("city", getValue(city_id));
        param.put("education", getValue(edu_id));
        if (session.getLoginData(SessionManager.KEY_GENDER).equals("Female")) {
            param.put("gender", "Male");
        } else {
            param.put("gender", "Female");
        }
        if (chk_photo.isChecked()) {
            param.put("photo_search", "photo_search");
        } else
            param.put("photo_search", "");

        Intent i = new Intent(context, SearchResultActivity.class);
        i.putExtra("searchData", Common.getJsonStringFromObject(param));
        startActivity(i);
    }

    private void isValidQuick(String name) {
        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        param.put("from_age", age_from);
        param.put("to_age", age_to);
        param.put("from_height", height_from);
        param.put("to_height", height_to);
        param.put("looking_for", getValue(mari_id));
        param.put("religion", getValue(religion_id));
        param.put("caste", getValue(caste_id));
        param.put("mothertongue", getValue(tongue_id));
        param.put("country", getValue(country_id));
        param.put("state", getValue(state_id));
        param.put("city", getValue(city_id));
        param.put("education", getValue(edu_id));
        if (chk_photo.isChecked()) {
            param.put("photo_search", "photo_search");
        } else
            param.put("photo_search", "");
        param.put("save_search", name);
        param.put("search_page_nm", AppConstants.TYPE_SEARCH_QUICK);

        addSave(param);

    }

    private void initView(View view) throws JSONException {
        if (MyApplication.getSpinData() != null) {

            chk_photo = view.findViewById(R.id.chk_photo);
            loader = view.findViewById(R.id.loader);
            btn_search = view.findViewById(R.id.btn_search);
            btn_save_search = view.findViewById(R.id.btn_save_search);

            btn_save_search.setOnClickListener(view12 -> showAlert());
            btn_search.setOnClickListener(view1 -> searchData());

            spin_mari = view.findViewById(R.id.spin_mari);
            setupSearchDropDown(spin_mari, "Marital Status", "marital_status");

            spin_religion = view.findViewById(R.id.spin_religion);
            setupSearchDropDown(spin_religion, "Religion", "religion_list");

            spin_caste = view.findViewById(R.id.spin_caste);
            setupInitializeSearchDropDown(spin_caste, "Caste");

            spin_tongue = view.findViewById(R.id.spin_tongue);
            setupSearchDropDown(spin_tongue, "Mother Tongue", "mothertongue_list");

            spin_country = view.findViewById(R.id.spin_country);
            setupSearchDropDown(spin_country, "Country", "country_list");

            spin_state = view.findViewById(R.id.spin_state);
            setupInitializeSearchDropDown(spin_state, "State");

            spin_city = view.findViewById(R.id.spin_city);
            setupInitializeSearchDropDown(spin_city, "City");

            spin_edu = view.findViewById(R.id.spin_edu);
            setupSearchDropDown(spin_edu, "Education", "education_list");


            tv_height_label = view.findViewById(R.id.tv_height_label);
            tv_min_height = view.findViewById(R.id.search_tv_min_height);
            tv_max_height = view.findViewById(R.id.search_tv_max_height);
            range_height = view.findViewById(R.id.search_range_height);

            tv_weight_label = view.findViewById(R.id.tv_weight_label);
            tv_min_age = view.findViewById(R.id.search_tv_min_age);
            tv_max_age = view.findViewById(R.id.search_tv_max_age);
            range_age = view.findViewById(R.id.search_range_age);

            JSONArray arr = MyApplication.getSpinData().getJSONArray("height_list");
            JSONObject obj = arr.getJSONObject(0);
            JSONObject obj1 = arr.getJSONObject(arr.length() - 1);
            range_height.setMinStartValue(Float.parseFloat(obj.getString("id"))).setMaxStartValue(Float.parseFloat(obj1.getString("id"))).apply();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject object = arr.getJSONObject(i);
                if (object.getString("id").equals("48")) {
                    height_map.put(object.getString("id"), "Below 4ft");
                } else if (object.getString("id").equals("85")) {
                    height_map.put(object.getString("id"), "Above 7ft");
                } else {
                    height_map.put(object.getString("id"), object.getString("val"));
                }
            }

            range_height.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
                height_from = String.valueOf(minValue);
                height_to = String.valueOf(maxValue);
                tv_min_height.setText(disHeight(height_from));
                tv_max_height.setText(disHeight(height_to));
            });

            range_age.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
                age_from = String.valueOf(minValue);
                age_to = String.valueOf(maxValue);
                tv_min_age.setText(minValue + " Years");
                tv_max_age.setText(maxValue + " Years");
            });

            //common.setDrawableLeftTextViewLeft(R.drawable.height_pink, tv_height_label);
            //common.setDrawableLeftTextViewLeft(R.drawable.weight_pink, tv_weight_label);
        } else {
            getList(view);
        }
    }

    private void getList(final View view) {
        common.showProgressRelativeLayout(loader);
        common.makePostRequest(AppConstants.common_list, new HashMap<String, String>(), response -> {

            try {
                JSONObject object = new JSONObject(response);
                session.setUserData(SessionManager.TOKEN, object.getString("tocken"));

                MyApplication.setSpinData(object);
                initView(view);

                common.hideProgressRelativeLayout(loader);

            } catch (JSONException e) {
                common.hideProgressRelativeLayout(loader);
                e.printStackTrace();
            }

        }, error -> {
            Log.d("resp", error.getMessage() + "   ");
            common.hideProgressRelativeLayout(loader);
        });
    }

    private String disHeight(String val) {
        return height_map.get(val);
    }

    private String listToString(List<String> list) {
        String listString = "";

        for (String s : list) {
            listString += s + ",";// \t
        }
        listString = listString.replaceAll(",$", "");
        return listString;
    }

    private void showAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_save_search, null);
        dialogBuilder.setView(dialogView);

        EditText editText = dialogView.findViewById(R.id.editText);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        AlertDialog alertDialog = dialogBuilder.create();

        btnCancel.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        btnSave.setOnClickListener(view -> {
            if (editText.getText().length() > 0) {
                alertDialog.dismiss();
                isValidQuick(editText.getText().toString().trim());
            } else
                editText.setError("Please enter title");
        });

        alertDialog.show();
    }

    private void getDependentList(final String tag, String id) {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("get_list", tag);
        param.put("currnet_val", id);
        param.put("multivar", "multi");
        param.put("retun_for", "json");

        JsonParser jsonParser = new JsonParser();

        common.makePostRequest(AppConstants.common_depedent_list, param, response -> {
            try {
                common.hideProgressRelativeLayout(loader);
                JSONObject object = new JSONObject(response);
                session.setUserData(SessionManager.TOKEN, object.getString("tocken"));
                if (object.getString("status").equals("success")) {
                    switch (tag) {
                        case "caste_list":
                            JsonArray jsonArray = (JsonArray) jsonParser.parse(object.getJSONArray("data").toString());
                            spin_caste.setItems(spin_caste, common.getSpinnerListFromArray(jsonArray), -1, this, "Caste");
                            break;
                        case "state_list":
                            JsonArray jsonArray1 = (JsonArray) jsonParser.parse(object.getJSONArray("data").toString());
                            spin_state.setItems(spin_state, common.getSpinnerListFromArray(jsonArray1), -1, this, "State");
                            break;
                        case "city_list":
                            JsonArray jsonArray2 = (JsonArray) jsonParser.parse(object.getJSONArray("data").toString());
                            spin_city.setItems(spin_city, common.getSpinnerListFromArray(jsonArray2), -1, this, "City");
                            break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            common.hideProgressRelativeLayout(loader);
        });
    }

    private String getValue(String val) {
        if (val == null || val.equals("0")) return "";
        else return val;
    }

    private void addSave(HashMap<String, String> param) {
        common.showProgressRelativeLayout(loader);
        common.makePostRequest(AppConstants.save_search, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                common.showToast(object.getString("errormessage"));
                if (object.getString("status").equals("success"))  AppDebugLog.print("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> common.hideProgressRelativeLayout(loader));
    }


    private void setupSearchDropDown(MultiSpinnerSearch spinner, String hint, String listJsonKey) {
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(MyApplication.getSpinData().toString());
        spinner.setItems(spinner, common.getSpinnerListFromArray(gsonObject.get(listJsonKey).getAsJsonArray()), -1, this, hint);
    }

    private void setupInitializeSearchDropDown(MultiSpinnerSearch spinner, String hint) {
        spinner.setItems(spinner, new ArrayList<>(), -1, this, hint);
    }

    @Override public void onItemsSelected(MultiSpinnerSearch singleSpinnerSearch) {
        Common.hideSoftKeyboard(getActivity());
        if (singleSpinnerSearch == null) return;
        if (singleSpinnerSearch.getSelectedIdsInString()== null) return;

        switch (singleSpinnerSearch.getId()) {
            case R.id.spin_mari:
                mari_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_religion:
                religion_id = singleSpinnerSearch.getSelectedIdsInString();
                if (religion_id != null && !religion_id.equals("0")) {
                    getDependentList("caste_list", religion_id);
                } else {
                    setupInitializeSearchDropDown(spin_caste, "Caste");
                }
                break;
            case R.id.spin_caste:
                caste_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_tongue:
                tongue_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_country:
                country_id = singleSpinnerSearch.getSelectedIdsInString();
                state_id = "";
                city_id = "";
                if (country_id != null && !country_id.equals("0")) {
                    getDependentList("state_list", country_id);
                }
                setupInitializeSearchDropDown(spin_state, "State");
                setupInitializeSearchDropDown(spin_city, "City");
                break;
            case R.id.spin_state:
                state_id = singleSpinnerSearch.getSelectedIdsInString();
                city_id = "";
                if (state_id != null && !state_id.equals("0")) {
                    getDependentList("city_list", state_id);
                }
                setupInitializeSearchDropDown(spin_city, "City");
                break;
            case R.id.spin_city:
                city_id = singleSpinnerSearch.getSelectedIdsInString();
                break;
            case R.id.spin_edu:
                edu_id = singleSpinnerSearch.getSelectedIdsInString();
                break;

        }
    }

    @Override public void onItemsSelected(SingleSpinnerSearch singleSpinnerSearch, KeyPairBoolData item) {

    }

}
