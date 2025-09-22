package com.advance.sanatani_vivah.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.advance.sanatani_vivah.activities.OtherUserProfileActivity;
import com.advance.sanatani_vivah.activities.PlanListActivity;
import com.advance.sanatani_vivah.activities.ReportMissuseActivity;
import com.advance.sanatani_vivah.adapter.CommonListAdapter;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.model.DashboardItem;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.ApplicationData;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.advance.sanatani_vivah.utility.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecommendationFragment extends Fragment implements CommonListAdapter.ItemListener {
    private RecyclerView recyclerView;
    private CommonListAdapter adapter;
    private RelativeLayout loader;
    private Context context;
    private Common common;
    private SessionManager session;
    private List<DashboardItem> list;

    private TextView tv_no_data;

    public RecommendationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommendation, container, false);

        context = getActivity();
        common = new Common(context);
        session = new SessionManager(context);

        loader = view.findViewById(R.id.loader);
        recyclerView = view.findViewById(R.id.recyclerView);
        tv_no_data = view.findViewById(R.id.tv_no_data);

        initializeRecyclerView();
        getData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppDebugLog.print("in onResume of recomm");
        if (ApplicationData.getSharedInstance().isProfileChanged) {
            ApplicationData.getSharedInstance().isProfileChanged = false;
            common.hideProgressRelativeLayout(loader);
            list.clear();
            adapter.notifyDataSetChanged();
            getData();
        }
    }

    private void initializeRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new CommonListAdapter(getActivity(), list);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        common.showProgressRelativeLayout(loader);
        HashMap<String, String> param = new HashMap<>();
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));

        common.makePostRequestTime(AppConstants.recent_login, param, response -> {
            common.hideProgressRelativeLayout(loader);
            AppDebugLog.print("response in recommanded list : " + response);
            //Log.d("resp",response);
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("success")) {

                    JSONArray data = object.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.getJSONObject(i);
                        DashboardItem item = new DashboardItem();
                        item.setName(obj.getString("matri_id"));
                        item.setImage(obj.getString("photo1"));
                        item.setImage_approval(obj.getString("photo1_approve"));
                        item.setAge(obj.getString("age"));
                        item.setHeight(obj.getString("height"));
                        item.setCaste(obj.getString("caste_name"));
                        item.setReligion(obj.getString("religion_name"));
                        item.setCity(obj.getString("city_name"));
                        item.setCountry(obj.getString("country_name"));
                        item.setDesignation(obj.getString("designation_name"));
                        item.setPhoto_protect(obj.getString("photo_protect"));
                        item.setPhoto_view_status(obj.getString("photo_view_status"));
                        item.setPhoto_password(obj.getString("photo_password"));
                        item.setId(obj.getString("id"));
                        item.setPhoto_view_count(obj.getString("photo_view_count"));
                        item.setBadge(obj.getString("badge"));
                        item.setBadgeUrl(obj.getString("badgeUrl"));
                        item.setColor(obj.getString("color"));
                        item.setPhotoUrl(obj.getString("photoUrl"));

                        item.setEducation(obj.getString("education_name"));
                        item.setState(obj.getString("state_name"));
                        item.setProfileCreatedBy(obj.getString("profileby"));
                        JSONArray action = obj.getJSONArray("action");
                        item.setAction(action.getJSONObject(0));
                        list.add(item);
                    }
                    if (list.size() == 0) {
                        tv_no_data.setVisibility(View.VISIBLE);
                    } else {
                        tv_no_data.setVisibility(View.GONE);
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
