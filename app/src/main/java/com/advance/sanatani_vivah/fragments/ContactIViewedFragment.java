package com.advance.sanatani_vivah.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.activities.OtherUserProfileActivity;
import com.advance.sanatani_vivah.activities.PlanListActivity;
import com.advance.sanatani_vivah.activities.ReportMissuseActivity;
import com.advance.sanatani_vivah.adapter.CommonListAdapter;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.model.DashboardItem;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactIViewedFragment extends Fragment implements CommonListAdapter.ItemListener {
    private RecyclerView recyclerView;
    private CommonListAdapter adapter;
    private List<DashboardItem> list = new ArrayList<>();
    private Common common;
    private SessionManager session;
    private RelativeLayout loader;
    private boolean continue_request;
    private TextView tv_no_data;
    private int page = 0;
    private Context context;

    private int currentVisibleItemCount;
    private int currentFirstVisibleItem;
    private int totalItem;

    public ContactIViewedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_i_viewed, container, false);

        context = getActivity();
        common = new Common(getActivity());
        session = new SessionManager(getActivity());

        loader = view.findViewById(R.id.loader);
        recyclerView = view.findViewById(R.id.recyclerView);
        tv_no_data = view.findViewById(R.id.tv_no_data);

        initializeRecyclerView();
        page = page + 1;
        getListData(page);

        return view;
    }

    private void initializeRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new CommonListAdapter(getActivity(), list);
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

    private void getListData(int page) {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
        common.makePostRequestTime(AppConstants.i_viewed_contact_list + page, param, response -> {
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
                            item.setId(obj.getString("user_id"));
                            item.setUser_id(obj.getString("user_id"));
                            item.setMatri_id(obj.getString("matri_id"));
                            item.setName(obj.getString("matri_id"));
//                            item.setName(obj.getString("username"));

//                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//                            String description = Common.getDetailsFromValue(obj.getString("profileby"),common.getAge(obj.getString("birthdate"), sdf) + " Years ", obj.getString("height"),
//                                    obj.getString("caste_name"), obj.getString("religion_name"),
//                                    obj.getString("city_name"), obj.getString("country_name"),obj.getString("education_name"));
                            //item.setAbout(description);

                            item.setImage_approval(obj.getString("photo1_approve"));
                            item.setImage(obj.getString("photo1"));
                            item.setPhoto_view_count(obj.getString("photo_view_count"));
                            item.setPhoto_view_status(obj.getString("photo_view_status"));
                            item.setBadge(obj.getString("badge"));
                            item.setBadgeUrl(obj.getString("badgeUrl"));
                            item.setColor(obj.getString("color"));
                            item.setPhotoUrl(obj.getString("photoUrl"));

                            item.setEducation(obj.getString("education_name"));
                            item.setState(obj.getString("state_name"));
                            item.setProfileCreatedBy(obj.getString("profileby"));
                            item.setAge(obj.getString("age"));
                            item.setHeight(obj.getString("height"));
                            item.setCaste(obj.getString("caste_name"));
                            item.setReligion(obj.getString("religion_name"));
                            item.setCity(obj.getString("city_name"));
                            item.setCountry(obj.getString("country_name"));

                            JSONArray action = obj.getJSONArray("action");
                            item.setAction(action.getJSONObject(0));

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

    @Override
    public void likeRequest(final String tag, String matri_id, int index) {
        common.showProgressRelativeLayout(loader);
        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        param.put("other_id", matri_id);
        param.put("like_status", tag);

        common.makePostRequestTime(AppConstants.like_profile, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                if (tag.equals("Yes")) {
                    common.showAlert("Like", object.getString("errmessage"), R.drawable.heart_fill_pink);
                } else
                    common.showAlert("Unlike", object.getString("errmessage"), R.drawable.heart_gray_fill);
                if (object.getString("status").equals("success")) {

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

    @Override
    public void interestRequest(String matri_id, String int_msg, final LikeButton button) {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        param.put("receiver", matri_id);
        param.put("message", int_msg);

        common.makePostRequestTime(AppConstants.send_interest, param, response -> {
            common.hideProgressRelativeLayout(loader);
            //Log.d("resp",response);
            try {
                JSONObject object = new JSONObject(response);

                if (object.getString("status").equals("success")) {
                    button.setLiked(true);
                    common.showAlert("Interest", object.getString("errmessage"), R.drawable.check_fill_green);
                } else
                    common.showAlert("Interest", object.getString("errmessage"), R.drawable.check_gray_fill);

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

    @Override
    public void shortlistRequest(final String tag, String id) {
        common.showProgressRelativeLayout(loader);
        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        if (tag.equals("remove")) {
            param.put("shortlisteduserid", id);
        } else
            param.put("shortlistuserid", id);

        param.put("shortlist_action", tag);

        common.makePostRequestTime(AppConstants.shortlist_user, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                if (tag.equals("add")) {
                    common.showAlert("Shortlist", object.getString("errmessage"), R.drawable.star_fill_yellow);
                } else
                    common.showAlert("Remove From Shortlist", object.getString("errmessage"), R.drawable.star_gray_fill);

                if (object.getString("status").equals("success")) {

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

    @Override
    public void blockRequest(final String tag, String id) {
        common.showProgressRelativeLayout(loader);
        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        if (tag.equals("remove")) {
            param.put("unblockuserid", id);
        } else
            param.put("blockuserid", id);

        param.put("blacklist_action", tag);

        common.makePostRequestTime(AppConstants.block_user, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                if (tag.equals("add")) {
                    common.showAlert("Block", object.getString("errmessage"), R.drawable.ban);
                } else
                    common.showAlert("Unblock", object.getString("errmessage"), R.drawable.ban_gry);

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

    @Override
    public void alertPhotoPassword(String matri_id) {
        final String[] arr = new String[]{"We found your profile to be a good match. Please accept photo password request to proceed further.",
                "I am interested in your profile. I would like to view photo now, accept photo request."};
        final String[] selected = {"We found your profile to be a good match. Please accept photo password request to proceed further."};
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);

        alt_bld.setTitle("Photos View Request");
        alt_bld.setSingleChoiceItems(arr, 0, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                //dialog.dismiss();// dismiss the alertbox after chose option
                selected[0] = arr[item];
            }
        });
        alt_bld.setPositiveButton("Send", (dialogInterface, i) -> sendRequest(selected[0], matri_id));
        alt_bld.setNegativeButton("Cancel", (dialogInterface, i) -> {
            //alertpassword(password,url);
        });
        AlertDialog alert = alt_bld.create();
        alert.show();

    }

    private void showFilterPopup(View v, final String id) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.inflate(R.menu.discover_more);
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.report:
                    context.startActivity(new Intent(context, ReportMissuseActivity.class));
                    return true;
                case R.id.view_profile:
                    if (MyApplication.getPlan()) {
                        Intent i = new Intent(context, OtherUserProfileActivity.class);
                        i.putExtra("other_id", id);
                        context.startActivity(i);
                    } else {
                        common.showToast("Please upgrade your membership to view this profile.");
                        context.startActivity(new Intent(context, PlanListActivity.class));
                    }
                    return true;
                default:
                    return false;
            }
        });
        popup.show();
    }

}