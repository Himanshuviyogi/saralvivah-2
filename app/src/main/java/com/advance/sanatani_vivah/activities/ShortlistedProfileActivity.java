package com.advance.sanatani_vivah.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;
import androidx.cardview.widget.CardView;
import com.advance.sanatani_vivah.adapter.ShortListAdapter;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.custom.TouchImageView;
import com.advance.sanatani_vivah.model.DashboardItem;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.ApplicationData;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShortlistedProfileActivity extends AppCompatActivity implements ShortListAdapter.ItemListener {
    private RecyclerView recyclerView;
    private List<DashboardItem> list = new ArrayList<>();
    private Common common;
    private SessionManager session;
    private RelativeLayout loader;
    private boolean continue_request;
    private TextView tv_no_data;
    private ShortListAdapter adapter;
    private int page = 0;
    private int placeHolder;

    private int currentVisibleItemCount;
    private int currentFirstVisibleItem;
    private int totalItem;
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortlisted_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Shortlisted");
        toolbar.setNavigationOnClickListener(v -> finish());

        common = new Common(this);
        session = new SessionManager(this);

        loader = findViewById(R.id.loader);
        recyclerView = findViewById(R.id.recyclerView);
        tv_no_data = findViewById(R.id.tv_no_data);

        if (session.getLoginData(SessionManager.KEY_GENDER).equals("Female")) {
            placeHolder = R.drawable.male;
        } else if (session.getLoginData(SessionManager.KEY_GENDER).equals("Male")) {
            placeHolder = R.drawable.female;
        }

        initializeRecyclerView();
        page = page + 1;
        getListData(page);
    }

    private void initializeRecyclerView() {
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new ShortListAdapter(this, list);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    currentVisibleItemCount = mLayoutManager.getChildCount();
                    totalItem = mLayoutManager.getItemCount();
                    currentFirstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                    if (continue_request) {
                        if ((currentVisibleItemCount + currentFirstVisibleItem) >= totalItem) {
                            continue_request = false;
                            if (loader != null)
                                common.hideProgressRelativeLayout(loader);
                            page = page + 1;
                            getListData(page);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        MyApplication.getInstance().cancelPendingRequests("req");
        super.onBackPressed();
    }

    private void getListData(int page) {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));

        common.makePostRequestTime(AppConstants.shortlist_profile + page, param, response -> {
            common.hideProgressRelativeLayout(loader);
            Log.d("resp", response);
            try {
                JSONObject object = new JSONObject(response);
                int total_count = object.getInt("total_count");
                if (total_count != 0) {
                    tv_no_data.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    continue_request = object.getBoolean("continue_request");
                    if (list.size() != total_count) {
                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);

                            DashboardItem item = new DashboardItem();
                            item.setId(obj.getString("id"));
                            item.setMatri_id(obj.getString("matri_id"));
                            item.setName(obj.getString("username"));
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            String description = Common.getDetailsFromValue(obj.getString("profileby"), common.getAge(obj.getString("birthdate"), sdf) + " Years ", obj.getString("height"),
                                    obj.getString("caste_name"), obj.getString("religion_name"),
                                    obj.getString("state_name"), obj.getString("country_name"), obj.getString("education_name"));

                            item.setAbout(description);
                            item.setImage_approval(obj.getString("photo1_approve"));
                            item.setImage(obj.getString("photo1"));
                            item.setUser_id(obj.getString("user_id"));
                            item.setPhoto_view_status(obj.getString("photo_view_status"));
                            //item.setPhoto_view_count(obj.getString("photo_view_count"));
                            item.setBadge(obj.getString("badge"));
                            item.setBadgeUrl(obj.getString("badgeUrl"));
                            item.setColor(obj.getString("color"));
                            item.setPhotoUrl(obj.getString("photoUrl"));
                            Log.d("resp", obj.getString("photo_view_status") + "  " + obj.getString("matri_id") +
                                    "    " + obj.getString("photo1") + "   " + obj.getString("photo1_approve"));
                            list.add(item);
                        }
                        if(list.size() < 10) continue_request = false;
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    tv_no_data.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
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

    public class ShortAdapter extends ArrayAdapter<DashboardItem> {
        Context context;
        List<DashboardItem> list;

        public ShortAdapter(Context context, List<DashboardItem> list) {
            super(context, R.layout.short_list_item, list);
            this.context = context;
            this.list = list;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.short_list_item, null, true);

            CardView cardView = rowView.findViewById(R.id.cardView);
            ImageView imgPLanStamp = rowView.findViewById(R.id.imgPLanStamp);
            TextView tv_name = rowView.findViewById(R.id.tv_name);
            TextView tv_detail = rowView.findViewById(R.id.tv_detail);
            ImageView img_profile = rowView.findViewById(R.id.img_profile);
            LikeButton btn_short = rowView.findViewById(R.id.btn_short);

            final DashboardItem item = list.get(position);

            tv_name.setText(item.getMatri_id().toUpperCase());

            common.setImage(item.getPhoto_view_count(), item.getPhoto_view_status(), item.getImage_approval(),
                    item.getPhotoUrl() + item.getImage(), img_profile, null,68);

            if(item.getBadge().length() > 0){
                Picasso.get().load(item.getBadgeUrl()+item.getBadge())
                        .placeholder(R.drawable.ic_transparent_placeholder)
                        .error(R.drawable.ic_transparent_placeholder)
                        .into(imgPLanStamp);
                imgPLanStamp.setVisibility(View.VISIBLE);
            }else{
                imgPLanStamp.setVisibility(View.GONE);
            }

            if(item.getColor().length() > 0){
                //cardView.setShadowColor(Color.parseColor(""+item.getColor()));
            }

            //String about=item.getAbout()+"...<font color='#ff041a'>Read More</font>";
            tv_detail.setText(Html.fromHtml(item.getAbout()));

            img_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.getPhoto_view_status().equals("0") && item.getPhoto_view_count().equals("0")) {
                        alertPhotoPassword(item.getMatri_id());
                    } else if (item.getPhoto_view_status().equals("0") && item.getPhoto_view_count().equals("1") && item.getImage_approval().equals("APPROVED")) {
                        final Dialog dialog = new Dialog(context);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                }
            });

            tv_detail.setOnClickListener(view1 -> {
                if (MyApplication.getPlan()) {
                    Intent i = new Intent(context, OtherUserProfileActivity.class);
                    i.putExtra("other_id", item.getUser_id());
                    context.startActivity(i);
                } else {
                    common.showToast("Please upgrade your membership to chat with this member.");
                    context.startActivity(new Intent(context, PlanListActivity.class));
                }
            });
            tv_name.setOnClickListener(view12 -> {
                if (MyApplication.getPlan()) {
                    Intent i = new Intent(context, OtherUserProfileActivity.class);
                    i.putExtra("other_id", item.getUser_id());
                    context.startActivity(i);
                } else {
                    common.showToast("Please upgrade your membership to chat with this member.");
                    context.startActivity(new Intent(context, PlanListActivity.class));
                }
            });
            btn_short.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {

                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    removeShortlist(position, item.getMatri_id());
                }
            });

            return rowView;

        }
    }

    private void sendRequest(String int_msg, String matri_id) {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("interest_message", int_msg);
        param.put("receiver_id", matri_id);
        param.put("requester_id", session.getLoginData(SessionManager.KEY_MATRI_ID));

        common.makePostRequestTime(AppConstants.photo_password_request, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                Toast.makeText(getApplicationContext(), object.getString("errmessage"), Toast.LENGTH_LONG).show();

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

    @Override public void itemClicked(DashboardItem object, int position) {

    }

    @Override public void alertPhotoPassword(final String matri_id) {
        final String[] arr = new String[]{"We found your profile to be a good match. Please accept photo password request to proceed further.",
                "I am interested in your profile. I would like to view photo now, accept photo request."};
        final String[] selected = {"We found your profile to be a good match. Please accept photo password request to proceed further."};
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(ShortlistedProfileActivity.this);

        alt_bld.setTitle("Photos View Request");
        alt_bld.setSingleChoiceItems(arr, 0, (dialog, item) -> {

            //dialog.dismiss();// dismiss the alertbox after chose option
            selected[0] = arr[item];
        });
        alt_bld.setPositiveButton("Send", (dialogInterface, i) -> sendRequest(selected[0], matri_id));
        alt_bld.setNegativeButton("Cancel", (dialogInterface, i) -> {
            //alertpassword(password,url);
        });
        AlertDialog alert = alt_bld.create();
        alert.show();

    }

    @Override public void removeShortlist(final int position, String id) {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        param.put("shortlist_action", "remove");
        param.put("shortlisteduserid", id);

        common.makePostRequestTime(AppConstants.shortlist_user, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);

                if (object.getString("status").equals("success")) {
                    ApplicationData.getSharedInstance().isProfileChanged = true;
                    common.showAlert("Remove From Shortlist", object.getString("errmessage"), R.drawable.star_gray_fill);
                    list.remove(position);
                    if (list.size() == 0) {
                        tv_no_data.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
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

    }
}
