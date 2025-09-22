package com.advance.sanatani_vivah.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.adapter.ConversationListAdapter;
import com.advance.sanatani_vivah.model.ChatConversationBean;
import com.advance.sanatani_vivah.model.CreateConversationBean;
import com.advance.sanatani_vivah.network.ConnectionDetector;
import com.advance.sanatani_vivah.retrofit.AppApiService;
import com.advance.sanatani_vivah.retrofit.RetrofitClient;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.ApplicationData;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChatConversationActivityNew extends AppCompatActivity implements ConversationListAdapter.ItemListener {
    private RecyclerView recyclerView;
    private TextView lblNoRecordsFound;
    private ConversationListAdapter adapter;
    private List<ChatConversationBean> arrayList = new ArrayList<>();
    private CreateConversationBean createConversationBean;

    private Common common;
    private EditText txtMessage;
    private TextView tvPageTitle,tv_online;
    private ImageButton btnSend;
    private Menu menu;
    private boolean is_mark = false;
    private RelativeLayout progressBar;
    private String otherId, otherMatriId, conversationId,status;
    private int deletedMessagePosition = -1;
    private int placeHolderId;
    private SessionManager session;

    private ImageView imgProfile;

    private int pageNumber = 1;
    private boolean continueRequest;

    //Retrofit related
    private Retrofit retrofit;
    private AppApiService appApiService;

    private IntentFilter intentFilter,intentFilterForStatus;
    private BroadcastReceiver br,brForStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_conversation_new);

        initialize();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> {
            if (is_mark) {
                for (ChatConversationBean itm : arrayList) {
                    itm.setIs_checked(false);
                }
                adapter.notifyDataSetChanged();
                menu.findItem(R.id.delete).setVisible(false);
                menu.findItem(R.id.mark_all).setVisible(false);
                is_mark = false;
            } else {
                startActivity(new Intent(getApplicationContext(), ChatActivity.class));
                finish();
            }
        });

        tv_online = findViewById(R.id.tv_online);
        imgProfile = findViewById(R.id.imgProfile);
        tvPageTitle = findViewById(R.id.tvPageTitle);
        tvPageTitle.setText(otherMatriId);

        imgProfile.setOnClickListener(view -> {
            Intent i = new Intent(this, OtherUserProfileActivity.class);
            i.putExtra("other_id", createConversationBean.getReceiverMemberId());
            startActivity(i);
        });
        tvPageTitle.setOnClickListener(view -> {
            Intent i = new Intent(this, OtherUserProfileActivity.class);
            i.putExtra("other_id", createConversationBean.getReceiverMemberId());
            startActivity(i);
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(br, intentFilter);
        LocalBroadcastManager.getInstance(this).registerReceiver(brForStatus,intentFilterForStatus);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(br);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(brForStatus);
    }

    @Override protected void onStop() {
        super.onStop();
        ApplicationData.currentChatMatriId = "";
    }

    private void initialize() {
        conversationId = getIntent().getStringExtra("CONVERSATION_ID");
        otherId = getIntent().getStringExtra("OTHER_ID");
        otherMatriId = getIntent().getStringExtra("OTHER_MATRI_ID");
        status = getIntent().getStringExtra("STATUS");
        AppDebugLog.print("otherid : " + otherId);
        AppDebugLog.print("otherMatriId : " + otherMatriId);
        AppDebugLog.print("conversationId : " + conversationId);
        AppDebugLog.print("status : " + status);

        ApplicationData.currentChatMatriId = otherId;

        setToolbar();

        tv_online.setText(status);

        intentFilter = new IntentFilter(AppConstants.CONVERSATION_LIST_UPDATE);
        intentFilterForStatus = new IntentFilter(AppConstants.STATUS_UPDATE);
        br = new UpdateBroadcastReceiver();
        brForStatus = new StatusUpdateBroadcastReceiver();
        common = new Common(this);
        session = new SessionManager(this);
        retrofit = RetrofitClient.getClient();
        appApiService = retrofit.create(AppApiService.class);

        progressBar = findViewById(R.id.progressBar);

        btnSend = findViewById(R.id.btn_send);
        txtMessage = findViewById(R.id.et_message);

        placeHolderId = session.getPlaceHolderDrawableId();

        btnSend.setOnClickListener(view -> {
            if (txtMessage.getText().length() > 0)
                sendMessage(txtMessage.getText().toString());
            else
                Common.showToast("Please enter message");
        });
        recyclerView = findViewById(R.id.recyclerView);
        lblNoRecordsFound = findViewById(R.id.lblNoRecordsFound);

        // initializeRecyclerView();
        createConversation();
    }

    private void createConversation() {
        if (!ConnectionDetector.isConnectingToInternet(this)) {
            Common.showToast(getString(R.string.err_msg_no_intenet_connection));
            return;
        }

        common.showProgressRelativeLayout(progressBar);

        Map<String, String> params = new HashMap<>();
        params.put("conversation_id", conversationId);
        params.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
        params.put("member1_id", session.getLoginData(SessionManager.KEY_USER_ID));
        params.put("member1_matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        params.put("member2_id", otherId);
        params.put("member2_matri_id", otherMatriId);
        params.put("user_agent", "NI-AAPP");
        params.put("csrf_new_matrimonial", "dfdfd");

        for (String string : params.keySet()) {
            AppDebugLog.print(string + ":" + params.get(string) + "\n");
        }

        AppDebugLog.print("createConversation api : " + AppConstants.BASE_URL + AppConstants.CREATE_CONVERSATION);
        Call<JsonObject> call = appApiService.createConversation(params);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                common.hideProgressRelativeLayout(progressBar);
                JsonObject data = response.body();
                AppDebugLog.print("response in getConversionList : " + data);
                if (data != null) {
                    if (data.get("status").getAsString().equalsIgnoreCase("success")) {
                        Gson gson = new GsonBuilder().setDateFormat(AppConstants.GSONDateTimeFormat).create();
                        createConversationBean = gson.fromJson(data.get("data").getAsJsonObject(), CreateConversationBean.class);
                        arrayList.addAll(createConversationBean.getConversationList());
                        //when tempArrayList size > 0
                        if (createConversationBean.getConversationList().size() > 0) {
                            if(createConversationBean.getConversationList().size() > 19) {
                                continueRequest = true;
                            }
                            initializeRecyclerView();
                        }
                        setVisibilityNoRecordsFound();

                        if (createConversationBean.getSenderMemberPhotoUrl() != null && createConversationBean.getSenderMemberPhotoUrl().length() > 0) {
                            Picasso.get()
                                    .load(createConversationBean.getReceiverMemberPhotoUrl())
                                    .placeholder(placeHolderId)
                                    .error(placeHolderId)
                                    .fit()
                                    // .resize(0, imageWidthHeight)
                                    .centerCrop()
                                    .into(imgProfile);
                        }
                    } else {
                        setVisibilityNoRecordsFound();
                    }
                } else {
                    Common.showToast(getString(R.string.err_msg_something_went_wrong));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AppDebugLog.print("error in getConversionList : " + t.getMessage());
                t.printStackTrace();
                Common.showToast(getString(R.string.err_msg_something_went_wrong));
                //hideProgressLayout();
            }
        });
    }

    private void getConversionList() {
        if (!ConnectionDetector.isConnectingToInternet(this)) {
            Common.showToast(getString(R.string.err_msg_no_intenet_connection));
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
        params.put("member1_id", session.getLoginData(SessionManager.KEY_USER_ID));
        params.put("member1_matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        params.put("member2_id", otherId);
        params.put("member2_matri_id", otherMatriId);
        params.put("conversation_id", conversationId);
        params.put("user_agent", "NI-AAPP");
        params.put("csrf_new_matrimonial", "dfdfd");

        for (String string : params.values()) {
            AppDebugLog.print("params : " + string + "\n");
        }

        common.makePostRequest(AppConstants.GET_CONVERSATION_LIST + pageNumber, params, response -> {
            JsonObject data = JsonParser.parseString(response).getAsJsonObject();
            if (data.get("status").getAsString().equalsIgnoreCase("success")) {
                Gson gson = new GsonBuilder().setDateFormat(AppConstants.GSONDateTimeFormat).create();

                List<ChatConversationBean> tempArrayList = gson.fromJson(data.getAsJsonArray("data"), new TypeToken<List<ChatConversationBean>>() {
                }.getType());

                continueRequest = data.get("continue_request").getAsBoolean();

                if (pageNumber == 1) {
                    arrayList.clear();
                }

                //when tempArrayList size > 0
                if (tempArrayList.size() > 0 && arrayList.size() > 0) {
                    //remove loading view
                    arrayList.remove(arrayList.size() - 1);
                    arrayList.addAll(tempArrayList);
                    adapter.notifyDataChanged();
                }

                //when new product list size == 0
                if (tempArrayList.size() == 0 && arrayList.size() > 0) {
                    //not more data available on server
                    adapter.setMoreDataAvailable(false);
                    //remove loading view
                    arrayList.remove(arrayList.size() - 1);
                    adapter.notifyDataChanged();
                }
//                if (pageNumber == 1) {
//                    recyclerView.smoothScrollToPosition(0);
//                    setVisibilityNoRecordsFound();
//                }
            } else {
                //not more data available on server
                adapter.setMoreDataAvailable(false);
                if (arrayList.size() > 0) {
                    //remove loading view
                    arrayList.remove(arrayList.size() - 1);
                    adapter.notifyDataChanged();
                } else {
                    Common.showToast(data.get("message").getAsString());
                }
                setVisibilityNoRecordsFound();
            }
        }, error -> {
            common.hideProgressRelativeLayout(progressBar);
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });
    }

    private void sendMessage(String msg) {
        txtMessage.setText("");

        ChatConversationBean conversationBean = new ChatConversationBean();
        conversationBean.setMessage(msg);
        conversationBean.setSendOn(null);
        conversationBean.setIsRead("Yes");
        conversationBean.setIsSender(true);
        conversationBean.setId("id");
        arrayList.add(0,conversationBean);
        if(adapter !=null) {
            adapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(0);
            lblNoRecordsFound.setVisibility(View.GONE);
        }else{
            initializeRecyclerView();
        }

        Map<String, String> params = new HashMap<>();
        params.put("conversation_id", conversationId);
        params.put("sender_member_id", session.getLoginData(SessionManager.KEY_USER_ID));
        params.put("sender_member_matri_id",  session.getLoginData(SessionManager.KEY_MATRI_ID));
        params.put("receiver_member_id", otherId);
        params.put("receiver_member_matri_id", otherMatriId);
        params.put("sender_member_photo_url", createConversationBean.getSenderMemberPhotoUrl());
        params.put("message", msg);
        params.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
        params.put("user_agent", "NI-AAPP");
        params.put("csrf_new_matrimonial", "dfdfd");

        sendMessageRequest(params);
    }

    private void sendMessageRequest(Map<String, String> params) {
        if (!ConnectionDetector.isConnectingToInternet(this)) {
            Common.showToast(getString(R.string.err_msg_no_intenet_connection));
            return;
        }

        //common.showProgressRelativeLayout(progressBar);
        AppDebugLog.print("params in sendMessageRequest : " + params.toString());
        Call<JsonObject> call = appApiService.sendMessageRequest(params);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                AppDebugLog.print("response in sendMessageRequest : " + response.body());
               // common.hideProgressRelativeLayout(progressBar);
                JsonObject data = response.body();
                if (data != null) {
                    //Utility.showSnackBar(thisthis, data.get("message").getAsString());
                    if (data.get("status").getAsString().equalsIgnoreCase("success")) {
                        AppDebugLog.print("Message sent");
//                        adapter.setMoreDataAvailable(true);
//                        pageNumber = 1;
//                        arrayList.clear();
//                        createConversation();
                    } else {
                       // Common.showToast(data.get("message").getAsString());
                    }
                } else {
                  //  Common.showToast(getString(R.string.err_msg_try_again_later));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                AppDebugLog.print("error in sendMessageRequest : " + t.getMessage());
                common.hideProgressRelativeLayout(progressBar);
                Common.showToast(getString(R.string.err_msg_try_again_later));
            }
        });
    }

//    @Override
//    public void deleteMessage(ChatConversationBean item, int position) {
//        deletedMessagePosition = position;
//        Common.showConfirmationAlert(this, "Message", "Are you sure you want to delete this message?", "Yes", "No", (dialog, which) -> {
//            Map<String, String> params = new HashMap<>();
//            params.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
//            params.put("other_id", otherMatriId);
//            params.put("action", "deleteimportantmessage");
//            params.put("id", item.getId());
//            sendDeleteMessageRequest(params);
//        }, null);
//    }

    private void sendDeleteMessageRequest(Map<String, String> params) {
        if (!ConnectionDetector.isConnectingToInternet(this)) {
            Common.showToast(getString(R.string.err_msg_no_intenet_connection));
            return;
        }

        common.showProgressRelativeLayout(progressBar);
        AppDebugLog.print("params in sendDeleteMessageRequest : " + params.toString());
        Call<JsonObject> call = appApiService.deleteMessageRequest(params);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                common.hideProgressRelativeLayout(progressBar);
                AppDebugLog.print("response in sendDeleteMessageRequest : " + response.body());
                JsonObject data = response.body();
                if (data != null) {
                    if (Integer.valueOf(data.get("statuscode").getAsString()) == 1002) {
                        Common.showToast(data.get("message").getAsString());
                        session.logoutUser();
                        return;
                    }
                    //Utility.showSnackBar(this, data.get("message").getAsString());
                    if (Integer.valueOf(data.get("statuscode").getAsString()) == 200) {
                        AppDebugLog.print("Delete message");
                        arrayList.remove(deletedMessagePosition);
                        adapter.notifyItemRemoved(deletedMessagePosition);
                    } else {
                        Common.showToast(data.get("message").getAsString());
                    }
                } else {
                    Common.showToast(getString(R.string.err_msg_try_again_later));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                common.hideProgressRelativeLayout(progressBar);
                Common.showToast(getString(R.string.err_msg_try_again_later));
            }
        });
    }

    private void initializeRecyclerView() {
        adapter = new ConversationListAdapter(this, arrayList, createConversationBean);
        adapter.setListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        // mLayoutManager.setAutoMeasureEnabled(true);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ((LinearLayoutManager) mLayoutManager).setReverseLayout(true);
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);
        adapter.setLoadMoreListener(() -> {
            recyclerView.post(() -> {
                if(continueRequest) {
                    loadMore();// a method which requests remote data
                }
            });
            //Calling loadMore function in Runnable to fix the
            // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
        });
        recyclerView.setAdapter(adapter);
        recyclerView.smoothScrollToPosition(0);
    }

    /**
     * Load more task on scroll task list
     */
    private void loadMore() {
        pageNumber++;
        //add loading progress view
        AppDebugLog.print("In loadMore");
        ChatConversationBean conversationBean = new ChatConversationBean();
        conversationBean.setId("loading");
        arrayList.add(conversationBean);
        getConversionList();
        adapter.notifyItemInserted(arrayList.size() - 1);
    }

    public class UpdateBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String notificationOtherId = intent.getStringExtra("OTHER_MATRI_ID");
            String message = intent.getStringExtra("MESSAGE");
            String date = intent.getStringExtra("DATE");
            conversationId = intent.getStringExtra("CONVERSATION_ID");

            AppDebugLog.print("Matri id from notification : " + notificationOtherId);
            AppDebugLog.print("message : "+message);
            AppDebugLog.print("date : "+date);

            //check if current user notification then update conversation otherwise nothing happened
            if (notificationOtherId.equals(otherId)) {
                adapter.setMoreDataAvailable(true);
//                pageNumber = 1;
//                continueRequest = false;
//                arrayList.clear();
                ChatConversationBean conversationBean = new ChatConversationBean();
                conversationBean.setMessage(message);
                conversationBean.setSendOn(date);
                conversationBean.setIsRead("Yes");
                conversationBean.setIsSender(false);
                conversationBean.setId("id");
                arrayList.add(0,conversationBean);
                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(0);
            }
        }
    }

    public class StatusUpdateBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isOnline = intent.getBooleanExtra("is_online",false);
            AppDebugLog.print("isOnline : "+isOnline);
            if(isOnline) {
                tv_online.setText("Online");
            }else{
                tv_online.setText("Offline");
            }
        }
    }

    private void setVisibilityNoRecordsFound() {
        if (arrayList.size() == 0) {
            lblNoRecordsFound.setVisibility(View.VISIBLE);
        } else {
            lblNoRecordsFound.setVisibility(View.GONE);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        this.menu = menu;
//        getMenuInflater().inflate(R.menu.menu_view_profile, menu);
//        return true;
//    }
}
