package com.advance.sanatani_vivah.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import androidx.cardview.widget.CardView;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.activities.OtherUserProfileActivity;
import com.advance.sanatani_vivah.activities.PlanListActivity;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.custom.TouchImageView;
import com.advance.sanatani_vivah.model.ExpressItem;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PhotoPasswordSentFragment extends Fragment {
    private ListView lv_exp_sent;
    private Context context;
    private List<ExpressItem> list = new ArrayList<>();
    private Sent_Adapter adapter;
    private Common common;
    private SessionManager session;
    private RelativeLayout loader;
    private boolean continue_request;
    private TextView tv_no_data;
    private int page = 0;

    private int placeHolder, photoProtectPlaceHolder;


    public PhotoPasswordSentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_password_sent, container, false);

        context = getActivity();
        common = new Common(getActivity());
        session = new SessionManager(getActivity());

        if (session.getLoginData(SessionManager.KEY_GENDER).equals("Female")) {
            photoProtectPlaceHolder = R.drawable.photopassword_male;
            placeHolder = R.drawable.male;
        } else if (session.getLoginData(SessionManager.KEY_GENDER).equals("Male")) {
            photoProtectPlaceHolder = R.drawable.photopassword_female;
            placeHolder = R.drawable.female;
        }

        loader = view.findViewById(R.id.loader);
        lv_exp_sent = view.findViewById(R.id.lv_exp_sent);
        tv_no_data = view.findViewById(R.id.tv_no_data);

        page = page + 1;
        getListData(page);

        adapter = new Sent_Adapter(getActivity(), list);
        lv_exp_sent.setAdapter(adapter);

        lv_exp_sent.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentVisibleItemCount;
            private int currentFirstVisibleItem;
            private int totalItem;

            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && scrollState == SCROLL_STATE_IDLE) {
                    if (continue_request) {
                        page = page + 1;
                        getListData(page);
                    }
                }
            }

            public void onScroll(AbsListView view, int firstVisibleItemm, int visibleItemCountt, int totalItemCountt) {
                this.currentFirstVisibleItem = firstVisibleItemm;
                this.currentVisibleItemCount = visibleItemCountt;
                this.totalItem = totalItemCountt;
            }
        });

        return view;
    }

    private void getListData(int page) {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));

        common.makePostRequest(AppConstants.photo_pass_request_send + page, param, response -> {
            common.hideProgressRelativeLayout(loader);
            Log.d("resp", response);
            try {
                JSONObject object = new JSONObject(response);
                continue_request = object.getBoolean("continue_request");
                int total_count = object.getInt("total_count");
                if (total_count != 0) {
                    tv_no_data.setVisibility(View.GONE);
                    lv_exp_sent.setVisibility(View.VISIBLE);
                    if (total_count != list.size()) {
                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            ExpressItem item = new ExpressItem();
                            item.setId(obj.getString("ph_reqid"));
                            item.setMatri_id(obj.getString("ph_requester_id"));
                            item.setPh_receiver_id(obj.getString("ph_receiver_id"));
                            item.setAbout(obj.getString("ph_msg"));
                            item.setUser_id(obj.getString("user_id"));
                            item.setReceiver_response(obj.getString("receiver_response"));
                            item.setPhoto_view_count(obj.getString("photo_view_count"));
                            item.setPhoto_view_status(obj.getString("photo_view_status"));
                            item.setImage_approval(obj.getString("photo1_approve"));
                            item.setImage(obj.getString("photo1"));
                            item.setDate(getDate(obj.getString("ph_reqdate")));
                            item.setBadge(obj.getString("badge"));
                            item.setBadgeUrl(obj.getString("badgeUrl"));
                            item.setColor(obj.getString("color"));
                            item.setPhotoUrl(obj.getString("photoUrl"));
                            list.add(item);
                        }
                        if(list.size() < 10) continue_request = false;
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    lv_exp_sent.setVisibility(View.GONE);
                    tv_no_data.setVisibility(View.VISIBLE);
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

    private String getDate(String time) {
        String outputPattern = "MMM dd, yyyy";
        String inputPattern = "yyyy-MM-dd";
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

    private class Sent_Adapter extends ArrayAdapter<ExpressItem> {

        Context context;
        List<ExpressItem> list;

        public Sent_Adapter(Context context, List<ExpressItem> list) {
            super(context, R.layout.photo_password, list);
            this.context = context;
            this.list = list;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.photo_password, null, true);

            CardView cardView = rowView.findViewById(R.id.cardView);
            ImageView imgPLanStamp = rowView.findViewById(R.id.imgPLanStamp);

            TextView tv_name = rowView.findViewById(R.id.tv_name);
            TextView tv_date = rowView.findViewById(R.id.tv_date);
            TextView tv_detail = rowView.findViewById(R.id.tv_detail);
            TextView tv_status = rowView.findViewById(R.id.tv_status);
            MaterialButton btnDelete = rowView.findViewById(R.id.btnDelete);

            ImageView img_profile = rowView.findViewById(R.id.img_profile);

            final ExpressItem item = list.get(position);
            tv_name.setText(item.getPh_receiver_id());

            if (item.getReceiver_response().equals("Pending")) {
                btnDelete.setVisibility(View.VISIBLE);
            } else {
                btnDelete.setVisibility(View.GONE);
            }

            common.setImage(item.getPhoto_view_count(),item.getPhoto_view_status(), item.getImage_approval(),
                    item.getPhotoUrl()+item.getImage(), img_profile, null,0);

            tv_date.setText(item.getDate());

            tv_detail.setText(item.getAbout());
            tv_status.setText(item.getReceiver_response());

            btnDelete.setOnClickListener(view1 -> deleteAlert(item.getId(), position));

            if (item.getBadge().length() > 0) {
                Picasso.get().load(item.getBadgeUrl() + item.getBadge())
                        .placeholder(R.drawable.ic_transparent_placeholder)
                        .error(R.drawable.ic_transparent_placeholder)
                        .into(imgPLanStamp);
                imgPLanStamp.setVisibility(View.VISIBLE);
            } else {
                imgPLanStamp.setVisibility(View.GONE);
            }

            if (item.getColor().length() > 0) {
                //cardView.setShadowColor(Color.parseColor("" + item.getColor()));
            }

            img_profile.setOnClickListener(view12 -> {

                if (item.getPhoto_view_status().equals("0") && item.getPhoto_view_count().equals("0")) {
                    alertPhotoPassword(item.getImage(), item.getMatri_id());
                } else if (item.getPhoto_view_status().equals("0") && item.getPhoto_view_count().equals("1") && item.getImage_approval().equals("APPROVED")) {
                    final Dialog dialog = new Dialog(context);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.show_image_alert);
                    TouchImageView img_url = dialog.findViewById(R.id.img_url);
                    Picasso.get().load(item.getImage()).placeholder(placeHolder).error(placeHolder).into(img_url);
                    dialog.show();
                } else {
                    if (MyApplication.getPlan()) {
                        Intent i = new Intent(context, OtherUserProfileActivity.class);
                        i.putExtra("other_id", item.getUser_id());
                        context.startActivity(i);
                    } else {
                        common.showToast("Please upgrade your membership to view this profile.");
                        context.startActivity(new Intent(context, PlanListActivity.class));
                    }
                }
            });

            return rowView;
        }

        private void alertPhotoPassword(final String url, final String matri_id) {
            final String[] arr = new String[]{"We found your profile to be a good match. Please accept photo password request to proceed further.",
                    "I am interested in your profile. I would like to view photo now, accept photo request."};
            final String[] selected = {"We found your profile to be a good match. Please accept photo password request to proceed further."};
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);

            alt_bld.setTitle("Photos View Request");
            alt_bld.setSingleChoiceItems(arr, 0, (dialog, item) -> {
                // dismiss the alertbox after chose option
                selected[0] = arr[item];
            });
            alt_bld.setPositiveButton("Send", (dialogInterface, i) -> sendRequest(selected[0], matri_id));
            alt_bld.setNegativeButton("Cancel", (dialogInterface, i) -> {
                //alertpassword(password,url);
            });
            AlertDialog alert = alt_bld.create();
            alert.show();

        }

        private void sendRequest(String int_msg, String matri_id) {
            common.showProgressRelativeLayout(loader);

            HashMap<String, String> param = new HashMap<>();
            param.put("interest_message", int_msg);
            param.put("receiver_id", matri_id);
            param.put("requester_id", session.getLoginData(SessionManager.KEY_MATRI_ID));

            common.makePostRequest(AppConstants.photo_password_request, param, response -> {
                common.hideProgressRelativeLayout(loader);
                try {
                    JSONObject object = new JSONObject(response);
                    Toast.makeText(context, object.getString("errmessage"), Toast.LENGTH_LONG).show();

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

    private void deleteAlert(final String id, final int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Delete");
        alert.setMessage("Are you sure you want to delete this photo request?");
        alert.setNegativeButton("Cancel", null);
        alert.setPositiveButton("Delete", (dialogInterface, i) -> {
            common.showProgressRelativeLayout(loader);

            HashMap<String, String> param = new HashMap<>();
            param.put("requester_id", id);
            param.put("status", "sender");

            common.makePostRequest(AppConstants.delete_request, param, response -> {
                common.hideProgressRelativeLayout(loader);
                Log.d("resp", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.has("errmessage"))
                        common.showToast(object.getString("errmessage"));
                    if (object.getString("status").equals("success")) {
                        list.remove(position);
                        if (list.size() == 0) {
                            lv_exp_sent.setVisibility(View.GONE);
                            tv_no_data.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
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

        });
        alert.show();


    }

}
