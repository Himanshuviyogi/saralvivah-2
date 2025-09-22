package com.advance.sanatani_vivah.cardStack;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.activities.ConversationActivity;
import com.advance.sanatani_vivah.activities.OtherUserProfileActivity;
import com.advance.sanatani_vivah.activities.PlanListActivity;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.custom.TouchImageView;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CardProfileImagesFragment extends Fragment {

    private View view;
    private CardItem cardItem;
    private RecyclerView userProfileRecyclerView;
    private SessionManager session;
    private Common common;
    private LinearLayout layoutGoToMembership;
    private Button btnGoToPlan;
    private int adapterPosition;
    private TextView txtFocus;
    private NestedScrollView scrollView;
    private boolean isLoaded = true;

    private CardView btnSendMessage, btnShare, btnMore, btnShowPhotos, btnCallWhatsapp, btnConnect, btnNotNow;
    private TextView tvPhotoCount, tvName, tvAge, tvHeight, tv_detail;
    private ImageView imgProfile, imgPLanStamp;
    private CardStackAdapter.ItemListener itemListener;
    private RelativeLayout protecedBlurLayout;
    private LinearLayout layoutInfo;
    CardView cardView;

    private LikeButton btn_interest;
    private LikeButton btn_like;
    private LikeButton btn_block;
    private LikeButton btn_chat;
    private LikeButton btn_short;

    public CardProfileImagesFragment(CardItem item, int position, CardStackAdapter.ItemListener listener) {
        // Required empty public constructor
        cardItem = item;
        adapterPosition = position;
        itemListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_card_profile_images, container, false);
        initialize();
        return view;
    }

    private void initialize() {
        session = new SessionManager(requireActivity());
        common = new Common(requireActivity());

        scrollView = view.findViewById(R.id.scrollView);
        txtFocus = view.findViewById(R.id.txtFocus);
        userProfileRecyclerView = view.findViewById(R.id.userProfileRecyclerView);

        common = new Common(requireActivity());

        cardView = view.findViewById(R.id.cardView);
        btnMore = view.findViewById(R.id.btnMore);
        tvName = view.findViewById(R.id.tv_name);
        tv_detail = view.findViewById(R.id.tv_detail);
        imgProfile = view.findViewById(R.id.imgProfile);
        imgPLanStamp = view.findViewById(R.id.imgPLanStamp);
        layoutInfo = view.findViewById(R.id.layoutInfo);

        btn_interest = view.findViewById(R.id.btn_interest);
        btn_like = view.findViewById(R.id.btn_like);
        btn_block = view.findViewById(R.id.btn_id);
        btn_chat = view.findViewById(R.id.btn_chat);
        btn_short = view.findViewById(R.id.btn_short);

        //set data in views
        tvName.setText(getValue(cardItem.getMatriId().toUpperCase()));
        String description = Common.getDetailsFromValue(cardItem.getProfileby(), cardItem.getAge(), cardItem.getHeight(),
                cardItem.getCasteName(), cardItem.getReligionName(),
                cardItem.getStateName(), cardItem.getCountryName(), cardItem.getEducationName());

        tv_detail.setText(Html.fromHtml(description));

        common.setImage(cardItem.getPhotoViewCount().toString(),cardItem.getPhotoViewStatus(),
                cardItem.getPhoto1Approve(), cardItem.getPhotoUrl() + cardItem.getPhoto1(), imgProfile, null,20);

        JsonObject object = cardItem.getAction().get(0).getAsJsonObject();

        if (object.get("is_like").getAsString().equals("Yes")) {
            btn_like.setLiked(true);
        } else {
            btn_like.setLiked(false);
        }

        if (object.get("is_block").getAsInt() == 1) {
            btn_block.setLiked(true);
        } else {
            btn_block.setLiked(false);
        }

        if (!object.get("is_interest").getAsString().equals("")) {
            btn_interest.setLiked(true);
        } else {
            btn_interest.setLiked(false);
        }

        if (object.get("is_shortlist").getAsInt() == 1) {
            btn_short.setLiked(true);
        } else {
            btn_short.setLiked(false);
        }


        if (cardItem.getBadge().length() > 0) {
            Picasso.get().load(cardItem.getBadgeUrl() + cardItem.getBadge())
                    .placeholder(R.drawable.ic_transparent_placeholder)
                    .error(R.drawable.ic_transparent_placeholder)
                    .into(imgPLanStamp);
            imgPLanStamp.setVisibility(View.VISIBLE);
        } else {
            imgPLanStamp.setVisibility(View.GONE);
        }

        if (cardItem.getColor().length() > 0) {
           // cardView.setShadowColor(Color.parseColor("" + cardItem.getColor()));
        }

        layoutInfo.setOnClickListener(v->{
            if (MyApplication.getPlan()) {
                Intent i = new Intent(getActivity(), OtherUserProfileActivity.class);
                i.putExtra("other_id", cardItem.getId());
                startActivity(i);
            } else {
                common.showToast("Please upgrade your membership to view this profile.");
                startActivity(new Intent(getActivity(), PlanListActivity.class));
            }
        });

        btn_chat.setOnClickListener(view1 -> {
            if (MyApplication.getPlan()) {
                Intent i = new Intent(getActivity(), ConversationActivity.class);
                i.putExtra("matri_id", cardItem.getMatriId());
                startActivity(i);
            } else {
                common.showToast("Please upgrade your membership to chat with this member.");
                startActivity(new Intent(getActivity(), PlanListActivity.class));
            }
        });

        btn_like.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                likeRequest("Yes", cardItem.getMatriId(), adapterPosition);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                likeRequest("No", cardItem.getMatriId(), adapterPosition);
            }
        });
        btn_block.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                blockRequest("add", cardItem.getMatriId());
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                blockRequest("remove", cardItem.getMatriId());
            }
        });

        btn_short.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                shortlistRequest("add", cardItem.getMatriId());
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                shortlistRequest("remove", cardItem.getMatriId());
            }
        });
        btn_interest.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(final LikeButton likeButton) {
                likeButton.setLiked(false);
                LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View vv = inflater1.inflate(R.layout.bottom_sheet_interest, null, true);
                final RadioGroup grp_interest = vv.findViewById(R.id.grp_interest);

                final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
                dialog.setContentView(vv);
                dialog.show();

                Button send = vv.findViewById(R.id.btn_send_intr);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (grp_interest.getCheckedRadioButtonId() != -1) {
                            RadioButton btn = vv.findViewById(grp_interest.getCheckedRadioButtonId());
                            interestRequest(cardItem.getMatriId(), btn.getText().toString().trim(), likeButton);
                        }
                    }
                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                likeButton.setLiked(true);
                common.showToast("You already sent interest to this user.");
            }
        });

        btn_interest.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(final LikeButton likeButton) {
                likeButton.setLiked(false);
                LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View vv = inflater1.inflate(R.layout.bottom_sheet_interest, null, true);
                final RadioGroup grp_interest = vv.findViewById(R.id.grp_interest);

                final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
                dialog.setContentView(vv);
                dialog.show();

                Button send = vv.findViewById(R.id.btn_send_intr);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (grp_interest.getCheckedRadioButtonId() != -1) {
                            RadioButton btn = vv.findViewById(grp_interest.getCheckedRadioButtonId());
                            interestRequest(cardItem.getMatriId(), btn.getText().toString().trim(), likeButton);
                        }
                    }
                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                likeButton.setLiked(true);
                common.showToast("You already sent interest to this user.");
            }
        });

        imgProfile.setOnClickListener(view12 -> {
            if (cardItem.getPhotoViewStatus().equals("0") && cardItem.getPhotoViewCount().equals("0")) {
                alertPhotoPassword(cardItem.getPhotoPassword(), cardItem.getPhotoUrl(), cardItem.getMatriId());
            } else if (cardItem.getPhotoViewStatus().equals("0") && cardItem.getPhotoViewCount().equals("1") && cardItem.getPhoto1Approve().equals("APPROVED")) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.show_image_alert);
                TouchImageView img_url = dialog.findViewById(R.id.img_url);
                Picasso.get().load(cardItem.getPhoto1()).into(img_url);
                dialog.show();
            } else {
                if (MyApplication.getPlan()) {
                    Intent i = new Intent(getActivity(), OtherUserProfileActivity.class);
                    i.putExtra("other_id", cardItem.getId());
                    startActivity(i);
                } else {
                    common.showToast("Please upgrade your membership to view this profile.");
                    startActivity(new Intent(getActivity(), PlanListActivity.class));
                }
            }
        });
    }

    private void alertPhotoPassword(final String password, final String url, final String matri_id) {
        final String[] arr = new String[]{"We found your profile to be a good match. Please accept photo password request to proceed further.",
                "I am interested in your profile. I would like to view photo now, accept photo request."};
        final String[] selected = {"We found your profile to be a good match. Please accept photo password request to proceed further."};
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());

        alt_bld.setTitle("Photos View Request");
        alt_bld.setSingleChoiceItems(arr, 0, (dialog, item) -> {
            selected[0] = arr[item];
        });
        alt_bld.setPositiveButton("Send", (dialogInterface, i) -> sendRequest(selected[0], matri_id));
        alt_bld.setNegativeButton("Cancel", (dialogInterface, i) -> {
        });
        AlertDialog alert = alt_bld.create();
        alert.show();

    }

    private void sendRequest(String int_msg, String matri_id) {

        HashMap<String, String> param = new HashMap<>();
        param.put("interest_message", int_msg);
        param.put("receiver_id", matri_id);
        param.put("requester_id", session.getLoginData(SessionManager.KEY_MATRI_ID));

        common.makePostRequestTime(AppConstants.photo_password_request, param, response -> {
            try {
                JSONObject object = new JSONObject(response);
                Toast.makeText(getActivity(), object.getString("errmessage"), Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }
        }, error -> {
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });

    }

    private void likeRequest(final String tag, String matri_id, int index) {
        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        param.put("other_id", matri_id);
        param.put("like_status", tag);

        common.makePostRequestTime(AppConstants.like_profile, param, response -> {
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
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });

    }

    private void interestRequest(String matri_id, String int_msg, final LikeButton button) {
        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        param.put("receiver", matri_id);
        param.put("message", int_msg);

        common.makePostRequestTime(AppConstants.send_interest, param, response -> {
            Log.d("resp", response);
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
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });

    }

    private void blockRequest(final String tag, String id) {
        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        if (tag.equals("remove")) {
            param.put("unblockuserid", id);
        } else
            param.put("blockuserid", id);

        param.put("blacklist_action", tag);

        common.makePostRequestTime(AppConstants.block_user, param, response -> {
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
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });
    }

    private void shortlistRequest(final String tag, String id) {
        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        if (tag.equals("remove")) {
            param.put("shortlisteduserid", id);
        } else
            param.put("shortlistuserid", id);

        param.put("shortlist_action", tag);

        common.makePostRequestTime(AppConstants.shortlist_user, param, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (tag.equals("add")) {
                    common.showAlert("Shortlist", object.getString("errmessage"), R.drawable.star_fill_yellow);
                } else {
                    common.showAlert("Remove From Shortlist", object.getString("errmessage"), R.drawable.star_gray_fill);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }
        }, error -> {
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });
    }

    private String getValue(String val) {
        if (val == null || val.equals("")) return "N/A";
        else return val;
    }
}