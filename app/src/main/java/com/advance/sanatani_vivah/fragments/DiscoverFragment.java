package com.advance.sanatani_vivah.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;
import androidx.cardview.widget.CardView;
import com.advance.sanatani_vivah.activities.ConversationActivity;
import com.advance.sanatani_vivah.activities.OtherUserProfileActivity;
import com.advance.sanatani_vivah.activities.PlanListActivity;
import com.advance.sanatani_vivah.activities.ReportMissuseActivity;
import com.advance.sanatani_vivah.adapter.CommonListAdapter;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.custom.TouchImageView;
import com.advance.sanatani_vivah.model.DashboardItem;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.ApplicationData;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiscoverFragment extends Fragment implements CommonListAdapter.ItemListener {
    private RecyclerView recyclerView;
    private CommonListAdapter adapter;
    private RelativeLayout loader;
    private Context context;
    private Common common;
    private SessionManager session;
    private List<DashboardItem> list;

    private TextView tv_no_data;

    public DiscoverFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

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
        AppDebugLog.print("in onResume of discover");
        if (ApplicationData.getSharedInstance().isProfileChanged) {
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

        common.makePostRequestTime(AppConstants.recent_join, param, response -> {
            common.hideProgressRelativeLayout(loader);
            AppDebugLog.print("recent_join : " + response.toString());
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
                        item.setPhoto_view_status(obj.getString("photo_view_status"));//012
                        item.setPhoto_password(obj.getString("photo_password"));//1-act,0-disc
                        item.setId(obj.getString("id"));
                        item.setPhoto_view_count(obj.getString("photo_view_count"));
                        item.setBadge(obj.getString("badge"));
                        item.setBadgeUrl(obj.getString("badgeUrl"));
                        item.setColor(obj.getString("color"));
                        item.setEducation(obj.getString("education_name"));
                        item.setState(obj.getString("state_name"));
                        item.setProfileCreatedBy(obj.getString("profileby"));
                        item.setPhotoUrl(obj.getString("photoUrl"));

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

    public class DiscoverAdapter extends ArrayAdapter<DashboardItem> {
        Context context;
        List<DashboardItem> list;
        Common common;

        public DiscoverAdapter(Context context, List<DashboardItem> list) {
            super(context, R.layout.discover_item, list);
            this.context = context;
            this.list = list;
            common = new Common(context);
        }

        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.recomdation_item, null, true);

            CardView cardView = rowView.findViewById(R.id.cardView);
            final LikeButton btn_interest = rowView.findViewById(R.id.btn_interest);
            LikeButton btn_like = rowView.findViewById(R.id.btn_like);
            LikeButton btn_block = rowView.findViewById(R.id.btn_id);
            LikeButton btn_chat = rowView.findViewById(R.id.btn_chat);
            LikeButton btn_short = rowView.findViewById(R.id.btn_short);

            ImageView imgPLanStamp = rowView.findViewById(R.id.imgPLanStamp);
            ImageView img_profile = rowView.findViewById(R.id.img_profile);
            TextView tv_name = rowView.findViewById(R.id.tv_name);
            TextView tv_detail = rowView.findViewById(R.id.tv_detail);

            final DashboardItem item = list.get(position);

            try {
                if (item.getAction().getString("is_like").equals("Yes"))
                    btn_like.setLiked(true);
                else
                    btn_like.setLiked(false);
                if (item.getAction().getInt("is_block") == 1)
                    btn_block.setLiked(true);
                else
                    btn_block.setLiked(false);
                if (item.getAction().getInt("is_shortlist") == 1)
                    btn_short.setLiked(true);
                else
                    btn_short.setLiked(false);
                if (!item.getAction().getString("is_interest").equals(""))
                    btn_interest.setLiked(true);
                else
                    btn_interest.setLiked(false);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            tv_name.setText(item.getName().toUpperCase());

            common.setImage(item.getPhoto_view_count(),item.getPhoto_view_status(), item.getImage_approval(),
                    item.getPhotoUrl()+item.getImage(), img_profile, null,0);

            String description = Common.getDetailsFromValue(item.getProfileCreatedBy(),item.getAge(), item.getHeight(),
                    item.getCaste(), item.getReligion(),
                    item.getState(), item.getCountry(),item.getEducation());

            tv_detail.setText(Html.fromHtml(description));

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
               // cardView.setShadowColor(Color.parseColor(""+item.getColor()));
            }

            btn_chat.setOnClickListener(view1 -> {
                if (MyApplication.getPlan()) {
                    Intent i = new Intent(context, ConversationActivity.class);
                    i.putExtra("matri_id", item.getName());
                    startActivity(i);
                } else {
                    common.showToast("Please upgrade your membership to chat with this member.");
                    context.startActivity(new Intent(context, PlanListActivity.class));
                }
            });

            btn_like.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    likeButton.setEnabled(false);
                    likeRequest("Yes", item.getName(), position);
                    likeButton.setEnabled(true);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    likeButton.setEnabled(false);
                    likeRequest("No", item.getName(), position);
                    likeButton.setEnabled(true);
                }
            });
            btn_block.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    blockRequest("add", item.getName());
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    blockRequest("remove", item.getName());
                }
            });

            btn_short.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    shortlistRequest("add", item.getName());
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    shortlistRequest("remove", item.getName());
                }
            });
            btn_interest.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(final LikeButton likeButton) {
                    likeButton.setLiked(false);
                    LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    final View vv = inflater1.inflate(R.layout.bottom_sheet_interest, null, true);
                    //context.getLayoutInflater().inflate(R.layout.bottom_sheet_interest, null);
                    final RadioGroup grp_interest = vv.findViewById(R.id.grp_interest);

                    final BottomSheetDialog dialog = new BottomSheetDialog(context);
                    dialog.setContentView(vv);
                    dialog.show();

                    Button send = vv.findViewById(R.id.btn_send_intr);
                    send.setOnClickListener(view12 -> {
                        dialog.dismiss();
                        if (grp_interest.getCheckedRadioButtonId() != -1) {
                            RadioButton btn = vv.findViewById(grp_interest.getCheckedRadioButtonId());
                            interestRequest(item.getName(), btn.getText().toString().trim(), likeButton);
                        }
                    });
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    likeButton.setLiked(true);
                    common.showToast("You already sent interest to this user.");
                }
            });

            tv_detail.setOnClickListener(view14 -> {
                if (MyApplication.getPlan()) {
                    Intent i = new Intent(context, OtherUserProfileActivity.class);
                    i.putExtra("other_id", item.getId());
                    context.startActivity(i);
                } else {
                    common.showToast("Please upgrade your membership to view this profile.");
                    context.startActivity(new Intent(context, PlanListActivity.class));
                }
            });

            tv_name.setOnClickListener(view15 -> {
                if (MyApplication.getPlan()) {
                    Intent i = new Intent(context, OtherUserProfileActivity.class);
                    i.putExtra("other_id", item.getId());
                    context.startActivity(i);
                } else {
                    common.showToast("Please upgrade your membership to view this profile.");
                    context.startActivity(new Intent(context, PlanListActivity.class));
                }
            });
            img_profile.setOnClickListener(view16 -> {
                if (item.getPhoto_view_status().equals("0") && item.getPhoto_view_count().equals("0")) {
                    alertPhotoPassword(item.getPhoto_password(), item.getImage(), item.getName());
                } else if (item.getPhoto_view_status().equals("0") && item.getPhoto_view_count().equals("1") && item.getImage_approval().equals("APPROVED")) {
                    final Dialog dialog = new Dialog(context);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.show_image_alert);
                    TouchImageView img_url = dialog.findViewById(R.id.img_url);
                    Picasso.get().load(item.getImage()).into(img_url);
                    dialog.show();
                } else {
                    if (MyApplication.getPlan()) {
                        Intent i = new Intent(context, OtherUserProfileActivity.class);
                        i.putExtra("other_id", item.getId());
                        context.startActivity(i);
                    } else {
                        common.showToast("Please upgrade your membership to view this profile.");
                        context.startActivity(new Intent(context, PlanListActivity.class));
                    }

                }

            });

            btn_short.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    shortlistRequest("add", item.getName());
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    shortlistRequest("remove", item.getName());
                }
            });

            return rowView;
        }

        private void alertPhotoPassword(final String password, final String url, final String matri_id) {
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

}
