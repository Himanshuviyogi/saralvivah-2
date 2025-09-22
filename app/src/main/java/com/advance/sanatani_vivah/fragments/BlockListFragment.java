package com.advance.sanatani_vivah.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.activities.OtherUserProfileActivity;
import com.advance.sanatani_vivah.activities.PlanListActivity;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.custom.TouchImageView;
import com.advance.sanatani_vivah.model.BlockItem;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.ApplicationData;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlockListFragment extends Fragment {
    private ListView lv_block;
    private TextView tv_no_data;
    private EditText et_block_id;
    private Button btn_block;
    private TextView lbl_block_visi;
    private Common common;
    private SessionManager session;
    private List<BlockItem> list = new ArrayList<>();
    private Context context;
    private BlockAdapter adp;
    private RelativeLayout loader;
    private boolean continue_request;
    private int page = 0;
    private int placeHolder, photoProtectPlaceHolder;

    public BlockListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_block_list, container, false);

        common = new Common(getActivity());
        session = new SessionManager(getActivity());
        context = getActivity();

        loader = view.findViewById(R.id.loader);
        tv_no_data = view.findViewById(R.id.tv_no_data);
        lv_block = view.findViewById(R.id.lv_block);
        lbl_block_visi = view.findViewById(R.id.lbl_block_visi);
        et_block_id = view.findViewById(R.id.et_block_id);
        btn_block = view.findViewById(R.id.btn_id);

        common.setDrawableLeftTextViewLeft(R.drawable.ban, lbl_block_visi);

        if (session.getLoginData(SessionManager.KEY_GENDER).equals("Female")) {
            photoProtectPlaceHolder = R.drawable.photopassword_male;
            placeHolder = R.drawable.male;
        } else if (session.getLoginData(SessionManager.KEY_GENDER).equals("Male")) {
            photoProtectPlaceHolder = R.drawable.photopassword_female;
            placeHolder = R.drawable.female;
        }


        page = page + 1;
        getListData(page);

        adp = new BlockAdapter(getActivity(), list);
        lv_block.setAdapter(adp);

        lv_block.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                    common.showProgressRelativeLayout(loader);
                }
            }

            public void onScroll(AbsListView view, int firstVisibleItemm, int visibleItemCountt, int totalItemCountt) {
                this.currentFirstVisibleItem = firstVisibleItemm;
                this.currentVisibleItemCount = visibleItemCountt;
                this.totalItem = totalItemCountt;
            }
        });

        btn_block.setOnClickListener(view1 -> {
            if (TextUtils.isEmpty(et_block_id.getText().toString().trim())) {
                et_block_id.setError("Please enter matri id.");
                return;
            }
            blockApi("add", et_block_id.getText().toString().trim(), 0);
        });

        return view;
    }

    private void getListData(int page) {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        common.makePostRequest(AppConstants.block_list + page, param, response -> {
            common.hideProgressRelativeLayout(loader);
            Log.d("resp", response);

            try {
                JSONObject object = new JSONObject(response);
                continue_request = object.getBoolean("continue_request");
                int total_count = object.getInt("total_count");

                if (total_count != 0) {
                    lv_block.setVisibility(View.VISIBLE);
                    tv_no_data.setVisibility(View.GONE);
                    if (list.size() != total_count) {

                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);

                            BlockItem item = new BlockItem();
                            item.setName(obj.getString("username"));
                            item.setMatri_id(obj.getString("matri_id"));
                            item.setUser_id(obj.getString("user_id"));
                            item.setImage(obj.getString("photo1"));
                            item.setImage_approval(obj.getString("photo1_approve"));
                            item.setPhoto_view_status(obj.getString("photo_view_status"));
                            item.setPhoto_view_count(obj.getString("photo_view_count"));

                            AppDebugLog.print("birthdate : " + obj.getString("birthdate"));
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            String description = Common.getDetailsFromValue(obj.getString("profileby"),common.getAge(obj.getString("birthdate"), sdf) + " Years ",
                                    obj.getString("height"),
                                    obj.getString("caste_name"), obj.getString("religion_name"),
                                    obj.getString("state_name"), obj.getString("country_name"),obj.getString("education_name"));

                            item.setDetail(description);
                            list.add(item);
                        }
                        if(list.size() < 10) continue_request = false;
                        adp.notifyDataSetChanged();
                    }
                } else {
                    lv_block.setVisibility(View.GONE);
                    tv_no_data.setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                common.hideProgressRelativeLayout(loader);
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

    private class BlockAdapter extends ArrayAdapter<BlockItem> {
        List<BlockItem> list;
        Context context;

        public BlockAdapter(Context context, List<BlockItem> list) {
            super(context, R.layout.block_item, list);
            this.context = context;
            this.list = list;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.block_item, null, true);

            CircleImageView img_profile = rowView.findViewById(R.id.img_profile);
            TextView tv_name = rowView.findViewById(R.id.tv_name);
            TextView tv_about = rowView.findViewById(R.id.tv_about);

            Button btn_block = rowView.findViewById(R.id.btn_id);
            common.setDrawableLeftButton(R.drawable.unlocked_white, btn_block);

            final BlockItem item = list.get(position);

            tv_name.setText(item.getMatri_id());

            tv_name.setOnClickListener(view14 -> {
                if (MyApplication.getPlan()) {
                    Intent i = new Intent(context, OtherUserProfileActivity.class);
                    i.putExtra("other_id", item.getUser_id());
                    context.startActivity(i);
                } else {
                    common.showToast("Please upgrade your membership to view this profile.");
                    context.startActivity(new Intent(context, PlanListActivity.class));
                }
            });

           // common.setImage(item.getPhoto_view_count(),item.getPhoto_view_status(), item.getImage_approval(), item.getImage(), img_profile, null,0);

            Picasso.get().load(item.getImage())
                    .placeholder(placeHolder)
                    .error(placeHolder)
                    .fit()
                    .centerCrop(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                    .into(img_profile);

            tv_about.setText(Html.fromHtml(item.getDetail()));

            tv_about.setOnClickListener(view14 -> {
                if (MyApplication.getPlan()) {
                    Intent i = new Intent(context, OtherUserProfileActivity.class);
                    i.putExtra("other_id", item.getUser_id());
                    context.startActivity(i);
                } else {
                    common.showToast("Please upgrade your membership to view this profile.");
                    context.startActivity(new Intent(context, PlanListActivity.class));
                }
            });

            btn_block.setOnClickListener(view1 -> unBlockUser("remove", item.getMatri_id(), position));

            img_profile.setOnClickListener(view12 -> {
                if (item.getPhoto_view_status().equals("0") && item.getPhoto_view_count().equals("0")) {
                    alertPhotoPassword(item.getMatri_id());
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

        private void alertPhotoPassword(final String matri_id) {
            final String[] arr = new String[]{"We found your profile to be a good match. Please accept photo password request to proceed further.",
                    "I am interested in your profile. I would like to view photo now, accept photo request."};
            final String[] selected = {"We found your profile to be a good match. Please accept photo password request to proceed further."};
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);

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

        private void sendRequest(String int_msg, String matri_id) {
            common.showProgressRelativeLayout(loader);;

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

    private void unBlockUser(final String tag, final String id, final int pos) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage("Are you sure you want to unblock this user?");
        alert.setTitle("Unblock User");
        alert.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
        alert.setPositiveButton("Yes", (dialogInterface, i) -> blockApi(tag, id, pos));
        alert.show();
    }

    private void blockApi(final String tag, String id, final int position) {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        param.put("blacklist_action", tag);
        if (tag.equals("add")) {
            param.put("blockuserid", id);
        } else
            param.put("unblockuserid", id);

        common.makePostRequest(AppConstants.block_user, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                common.showToast(object.getString("errmessage"));
                if (!object.getString("status").equals("error")) {
                    ApplicationData.getSharedInstance().isProfileChanged = true;
                    if (tag.equals("remove")) {
                        list.remove(position);
                        if (list.size() == 0) {
                            lv_block.setVisibility(View.GONE);
                            tv_no_data.setVisibility(View.VISIBLE);
                        }
                        adp.notifyDataSetChanged();
                    } else {
                        et_block_id.setText("");
                        list.clear();
                        page = 0;
                        page = page + 1;
                        getListData(page);
                    }
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
