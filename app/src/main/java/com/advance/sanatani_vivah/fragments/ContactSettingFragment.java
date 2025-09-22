package com.advance.sanatani_vivah.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ContactSettingFragment extends Fragment {
    private TextView lbl_contact_visi;
    private Common common;
    private RadioGroup grp_visi;
    private SessionManager session;
    private Context context;
    private Button btn_submit;
    private RelativeLayout loader;

    public ContactSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_setting, container, false);

        common = new Common(getActivity());
        session = new SessionManager(getActivity());
        context = getActivity();

        loader = view.findViewById(R.id.loader);
        lbl_contact_visi = view.findViewById(R.id.lbl_contact_visi);
        common.setDrawableLeftTextViewLeft(R.drawable.eye_pink, lbl_contact_visi);
        btn_submit = view.findViewById(R.id.btn_id);
        grp_visi = view.findViewById(R.id.grp_visi);

        btn_submit.setOnClickListener(view1 -> changeContact());

        getMyProfile();

        return view;
    }

    private void getMyProfile() {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));

        common.makePostRequest(AppConstants.get_my_profile, param, response -> {
            common.hideProgressRelativeLayout(loader);
            //  Log.d("resp",response);
            try {
                JSONObject object = new JSONObject(response);
                session.setUserData(SessionManager.TOKEN, object.getString("tocken"));
                if (object.getString("status").equals("success")) {
                    JSONObject data = object.getJSONObject("data");

                    String contact_view_security = data.getString("contact_view_security");
                    if (contact_view_security.equals("0")) {
                        ((RadioButton) grp_visi.getChildAt(1)).setChecked(true);
                    } else if (contact_view_security.equals("1")) {
                        ((RadioButton) grp_visi.getChildAt(0)).setChecked(true);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> common.hideProgressRelativeLayout(loader));
    }

    private void changeContact() {
        int pos = grp_visi.getCheckedRadioButtonId();
        if (pos == -1) {
            common.showToast("Please select contact visibility");
            return;
        }

        common.showProgressRelativeLayout(loader);
        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        RadioButton btn = grp_visi.findViewById(grp_visi.getCheckedRadioButtonId());

        if (btn.getText().toString().equals("Show to all paid members")) {
            param.put("contact_view_security", "1");
        } else if (btn.getText().toString().equals("Show to only express interest accepted and paid members")) {
            param.put("contact_view_security", "0");
        }

        common.makePostRequest(AppConstants.contact_setting, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                common.showToast(object.getString("errmessage"));
                if (object.getString("status").equals("success")) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> common.hideProgressRelativeLayout(loader));

    }


}
