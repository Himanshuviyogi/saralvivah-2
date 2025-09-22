package com.advance.sanatani_vivah.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.advance.sanatani_vivah.utility.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PhotoPasswordFragment extends Fragment {
    private TextView lbl_photo_visi;
    private Common common;
    private SessionManager session;
    private Context context;
    private RadioGroup grp_visi;
    private RelativeLayout loader;
    private boolean isfirst = true;

    public PhotoPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void tabChanged(){
        getMyProfile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_password, container, false);

        context = getActivity();
        session = new SessionManager(context);
        common = new Common(getActivity());

        loader = view.findViewById(R.id.loader);
        lbl_photo_visi = view.findViewById(R.id.lbl_photo_visi);
        common.setDrawableLeftTextViewLeft(R.drawable.eye_pink, lbl_photo_visi);

        grp_visi = view.findViewById(R.id.grp_visi);

        getMyProfile();

        grp_visi.setOnCheckedChangeListener((radioGroup, i) -> {
            if (!isfirst) {
                RadioButton checkedRadioButton = radioGroup.findViewById(i);
                String val = "";
                if (checkedRadioButton.getText().toString().equals("Hide for All")) {
                    val = "0";
                } else if (checkedRadioButton.getText().toString().equals("Visible to All")) {
                    val = "1";
                } else if (checkedRadioButton.getText().toString().equals("Visible to only paid members")) {
                    val = "2";
                }
                changeVisiApi(val);
            }

        });

        return view;
    }
    
    private void changeVisiApi(String val) {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        param.put("photo_view_status", val);
        param.put("action", "photo_view_status");

        common.makePostRequest(AppConstants.photo_visibility_status, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                common.showToast(object.getString("errmessage"));
                if (object.getString("status").equals("success")) {
                    //  getMyProfile();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            common.hideProgressRelativeLayout(loader);
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });
    }

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

                    String photo_view_status = data.getString("photo_view_status");
                    if (photo_view_status.equals("0")) {
                        ((RadioButton) grp_visi.getChildAt(0)).setChecked(true);
                    } else if (photo_view_status.equals("1")) {
                        ((RadioButton) grp_visi.getChildAt(1)).setChecked(true);
                    } else if (photo_view_status.equals("2")) {
                        ((RadioButton) grp_visi.getChildAt(2)).setChecked(true);
                    }
                    grp_visi.check(grp_visi.getCheckedRadioButtonId());
                    String photo_protect = data.getString("photo_protect");
                }
                isfirst = false;

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

}
