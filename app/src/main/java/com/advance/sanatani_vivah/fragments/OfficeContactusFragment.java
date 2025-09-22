package com.advance.sanatani_vivah.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class OfficeContactusFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private RelativeLayout loader;
    private TextView tv_mobile, tv_address, tv_email;
    private Common common;
    private SessionManager session;
    private Context context;
    private String addres = "";

    public OfficeContactusFragment() {
        // Required empty public constructor
    }

    public static OfficeContactusFragment newInstance(String param1, String param2) {
        OfficeContactusFragment fragment = new OfficeContactusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_office_contactus, container, false);

        common = new Common(getActivity());
        session = new SessionManager(getActivity());
        context = getActivity();

        loader = view.findViewById(R.id.loader);
        tv_mobile = view.findViewById(R.id.tv_mobile);
        tv_address = view.findViewById(R.id.tv_address);
        tv_email = view.findViewById(R.id.tv_email);

        getData();
        return view;
    }

    private void getData() {
        common.showProgressRelativeLayout(loader);

        common.makePostRequest(AppConstants.site_data, new HashMap<String, String>(), response -> {
            Log.d("resp", response);
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                JSONObject config_data = object.getJSONObject("config_data");

                tv_address.setText(Html.fromHtml(config_data.getString("full_address")));
                tv_email.setText(config_data.getString("contact_email"));
                tv_mobile.setText(config_data.getString("contact_no"));

                addres = config_data.getString("map_address");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> common.hideProgressRelativeLayout(loader));

    }
}
