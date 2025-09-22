package com.advance.sanatani_vivah.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.advance.sanatani_vivah.activities.SearchResultActivity;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class KeywordSearchFragment extends Fragment {
    private EditText et_keyword;
    private Common common;
    private SessionManager session;
    private Context context;
    private Button btn_save_search, btn_search;
    private RelativeLayout loader;
    private CheckBox checkBox;

    public KeywordSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        common = new Common(getActivity());
        session = new SessionManager(getActivity());
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keyword_search, container, false);

        loader = view.findViewById(R.id.loader);
        et_keyword = view.findViewById(R.id.et_keyword);
        checkBox = view.findViewById(R.id.checkBox);
        btn_search = view.findViewById(R.id.btn_search);
        btn_save_search = view.findViewById(R.id.btn_save_search);
        btn_save_search.setOnClickListener(view1 -> showAlert());

        btn_search.setOnClickListener(view12 -> searchData());

        return view;
    }

    private void searchData() {
        final String key = et_keyword.getText().toString().trim();
        if (key.equals("")) {
            et_keyword.setError("Keyword is require");
            return;
        }

        HashMap<String, String> param = new HashMap<>();
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
        param.put("keyword", key);
        if (checkBox.isChecked()) {
            param.put("photo_search", "photo_search");
        } else
            param.put("photo_search", "");

        if (session.getLoginData(SessionManager.KEY_GENDER).equals("Female")) {
            param.put("gender", "Male");
        } else {
            param.put("gender", "Female");
        }


        Intent i = new Intent(context, SearchResultActivity.class);
        i.putExtra("searchData", Common.getJsonStringFromObject(param));
        //i.putExtra("searchData", param);
        startActivity(i);

    }

    private void showAlert() {
        final String key = et_keyword.getText().toString().trim();
        if (key.equals("")) {
            et_keyword.setError("Keyword is require");
            return;
        }

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
                Validadvance(editText.getText().toString().trim(), key);
            } else
                editText.setError("Please enter title");
        });

        alertDialog.show();
    }

    private void Validadvance(String name, String key) {
        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        param.put("keyword", key);
        if (checkBox.isChecked()) {
            param.put("photo_search", "photo_search");
        } else
            param.put("photo_search", "");
        param.put("save_search", name);
        param.put("search_page_nm", AppConstants.TYPE_SEARCH_KEYWORD);
        param.put("gender", session.getLoginData(SessionManager.KEY_GENDER));
        add_save(param);

    }

    private void add_save(HashMap<String, String> param) {
        common.showProgressRelativeLayout(loader);
        common.makePostRequest(AppConstants.save_search, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                common.showToast(object.getString("errormessage"));
                if (object.getString("status").equals("success")) {
                    et_keyword.setText("");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> common.hideProgressRelativeLayout(loader));
    }

}
