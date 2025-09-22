package com.advance.sanatani_vivah.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.fcm.NotificationUtils;
import com.advance.sanatani_vivah.model.ConversationItem;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationActivity extends AppCompatActivity {
    private TextView tv_page_title, tv_online;
    private EditText et_message;
    private ImageButton btn_send;
    private ListView lv_list;
    private CircleImageView img_title_profile;
    private String matri_id;
    private RelativeLayout loader;
    private SessionManager session;
    private SwipeRefreshLayout swipe;
    private Common common;
    private List<ConversationItem> list = new ArrayList<>();
    private ListAdapter adapter;
    private boolean is_mark = false;
    private Menu menu;
    private IntentFilter mIntentFilter = null;
    private BroadcastReceiver receiver;
    private JSONObject opposite_user_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(this);

        if (!session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        setContentView(R.layout.activity_conversation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        common = new Common(this);

        this.mIntentFilter = new IntentFilter(AppConstants.CHAT_TAG);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                HandleData(intent);
            }
        };

        loader = findViewById(R.id.loader);

        swipe = findViewById(R.id.swipe);
        lv_list = findViewById(R.id.lv_list);
        img_title_profile = findViewById(R.id.img_title_profile);
        tv_page_title = findViewById(R.id.tv_page_title);
        tv_online = findViewById(R.id.tv_online);
        btn_send = findViewById(R.id.btn_send);
        et_message = findViewById(R.id.et_message);

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey("matri_id")) matri_id = b.getString("matri_id");

        swipe.setOnRefreshListener(() -> {
            list.clear();
            getList();
        });

        btn_send.setOnClickListener(view -> {
            String msg = et_message.getText().toString().trim();
            if (!msg.isEmpty()) sendMsg(msg);
        });

        adapter = new ListAdapter(this, list);
        lv_list.setAdapter(adapter);

        lv_list.setOnItemLongClickListener((adapterView, view, position, l) -> {
            if (!list.get(position).isIs_checked()) {
                is_mark = true;
                list.get(position).setIs_checked(true);
                menu.findItem(R.id.delete).setVisible(true);
                menu.findItem(R.id.mark_all).setVisible(true);
            } else {
                int sel = 0;
                list.get(position).setIs_checked(false);
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isIs_checked()) {
                        sel = sel + 1;
                    }
                }
                if (sel == 0) {
                    is_mark = false;
                }
                if (!is_mark) {
                    menu.findItem(R.id.delete).setVisible(false);
                    menu.findItem(R.id.mark_all).setVisible(false);
                }
            }
            adapter.notifyDataSetChanged();
            return true;
        });

        lv_list.setOnItemClickListener((adapterView, view, position, l) -> {
            if (is_mark) {
                if (!list.get(position).isIs_checked()) {
                    is_mark = true;
                    list.get(position).setIs_checked(true);
                    menu.findItem(R.id.delete).setVisible(true);
                    menu.findItem(R.id.mark_all).setVisible(true);
                } else {
                    int sel = 0;
                    list.get(position).setIs_checked(false);
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isIs_checked()) {
                            sel = sel + 1;
                        }
                    }
                    if (sel == 0) {
                        is_mark = false;
                    }

                    if (!is_mark) {
                        menu.findItem(R.id.delete).setVisible(false);
                        menu.findItem(R.id.mark_all).setVisible(false);
                    }
                }
            }
            adapter.notifyDataSetChanged();
        });

        getList();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle b = intent.getExtras();
        if (b != null && b.containsKey("matri_id")) {
            if (!b.getString("matri_id").equals(matri_id)) {
                matri_id = b.getString("matri_id");
                if (is_mark) {
                    for (ConversationItem itm : list) {
                        itm.setIs_checked(false);
                    }
                    adapter.notifyDataSetChanged();
                    menu.findItem(R.id.delete).setVisible(false);
                    menu.findItem(R.id.mark_all).setVisible(false);
                    is_mark = false;
                }
                list.clear();
                getList();
            }
        }
    }

    private void HandleData(Intent intent) {

        HashMap<String, String> hashMap = (HashMap<String, String>) intent.getSerializableExtra("message");
        if (hashMap != null) {
            if (hashMap.containsKey("data_msg")) {
                String message = hashMap.get("data_msg");
                try {
                    JSONObject obj = new JSONObject(message);
                    if (obj.getString("sender").equals(matri_id)) {
                        ConversationItem item = new ConversationItem();
                        item.setId(obj.getString("id"));
                        item.setIs_checked(false);
                        item.setSender(obj.getString("sender"));
                        item.setReceiver(obj.getString("receiver"));
                        item.setContent(obj.getString("content"));
                        item.setSent_on(getDate(obj.getString("sent_on")));
                        item.setIs_sent_receive("receive");
                        item.setPhoto_url(opposite_user_data.getString("photo_url"));
                        list.add(item);
                        adapter.notifyDataSetChanged();
                        lv_list.setSelection(adapter.getCount() - 1);
                        NotificationUtils.clearNotifications(this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.conversation_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        session = new SessionManager(this);

        if (!session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, this.mIntentFilter);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onPause();
    }

    private void getList() {
        common.showProgressRelativeLayout(loader);
        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        param.put("other_id", matri_id);

        common.makePostRequest(AppConstants.conversation, param, response -> {
            common.hideProgressRelativeLayout(loader);
            swipe.setRefreshing(false);
            try {
                JSONObject object = new JSONObject(response);
                opposite_user_data = object.getJSONObject("opposite_user_data");
                tv_page_title.setText(opposite_user_data.getString("matri_id"));
                if (!opposite_user_data.getString("photo_url").equals("")) {
                    Picasso.get().load(opposite_user_data.getString("photo_url")).into(img_title_profile);
                } else {
                    img_title_profile.setImageResource(R.drawable.placeholder);
                }

                String logged_in = opposite_user_data.getString("logged_in");

                if (logged_in.equals("1")) {
                    tv_online.setVisibility(View.VISIBLE);
                } else {
                    tv_online.setVisibility(View.GONE);
                }
                if (object.getString("status").equals("success")) {
                    int total_count = object.getInt("total_count");
                    if (total_count != 0) {
                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            ConversationItem item = new ConversationItem();
                            item.setId(obj.getString("id"));
                            item.setIs_checked(false);
                            item.setSender(obj.getString("sender"));
                            item.setReceiver(obj.getString("receiver"));
                            item.setContent(obj.getString("content"));
                            item.setSent_on(getDate(obj.getString("sent_on")));
                            item.setIs_sent_receive(obj.getString("is_sent_receive"));
                            item.setPhoto_url(obj.getString("photo_url"));
                            list.add(item);
                        }
                        adapter.notifyDataSetChanged();
                        lv_list.setSelection(adapter.getCount() - 1);
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

    private void sendMsg(String msg) {
        common.showProgressRelativeLayout(loader);
        HashMap<String, String> param = new HashMap<>();
        param.put("msg_status", "sent");
        param.put("message", msg);
        param.put("receiver_id", matri_id);
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));

        common.makePostRequest(AppConstants.send_message, param, response -> {
            common.hideProgressRelativeLayout(loader);
            Log.d("resp", response);
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("success")) {
                    et_message.setText("");
                    list.clear();
                    getList();

                } else {
                    common.showToast(object.getString("error_message"));
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

    class ListAdapter extends BaseAdapter {
        List<ConversationItem> list;

        Context context;
        LayoutInflater inflter;

        public ListAdapter(Context context, List<ConversationItem> list) {
            this.list = list;
            this.context = context;
            inflter = (LayoutInflater.from(context));
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public ConversationItem getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            ConversationItem item = list.get(position);
            if (item.getIs_sent_receive().equals("receive")) {
                view = inflter.inflate(R.layout.chat_conversation_other, viewGroup, false);
            } else {
                view = inflter.inflate(R.layout.chat_conversation_me, viewGroup, false);
            }
            if (item.isIs_checked()) {
                view.setBackgroundColor(getResources().getColor(R.color.mark_background));
            } else {
                view.setBackgroundColor(getResources().getColor(R.color.unmark_background));
            }
            CircleImageView img_profile = view.findViewById(R.id.img_profile);
            TextView tv_date = view.findViewById(R.id.lblTime);
            TextView tv_msg = view.findViewById(R.id.lblMessage);

            if (!item.getPhoto_url().equals(""))
               // if (img_profile==null) Toast.makeText(context,"hi",Toast.LENGTH_LONG).show();
                if(img_profile!=null)Picasso.get().load(item.getPhoto_url()).into(img_profile);

            tv_msg.setText(Html.fromHtml(item.getContent()));
            tv_date.setText(item.getSent_on());

            return view;
        }


        @Override
        public boolean hasStableIds() {
            return true;
        }


    }

    private String getDate(String time) {
        String outputPattern = "MMM dd, yyyy";//HH:mm a
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String nowPatern = "HH:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        SimpleDateFormat nowFormat = new SimpleDateFormat(nowPatern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time)
            ;
            if (DateUtils.isToday(date.getTime())) {
                str = nowFormat.format(date);//"Today "+
            } else {
                str = outputFormat.format(date);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public void onBackPressed() {
        if (is_mark) {
            for (ConversationItem itm : list) {
                itm.setIs_checked(false);
            }
            adapter.notifyDataSetChanged();
            menu.findItem(R.id.delete).setVisible(false);
            menu.findItem(R.id.mark_all).setVisible(false);
            is_mark = false;
        } else {
            super.onBackPressed();
        }
    }

    private void deleteMsg() {
        String selectedId = "";
        for (ConversationItem itm : list) {
            if (itm.isIs_checked())
                selectedId += itm.getId() + ",";
        }
        selectedId = selectedId.replaceAll(",$", "");
        Log.d("resp", selectedId);
        if (!selectedId.equals(""))
            deleteConApi(selectedId);
    }

    private void deleteConApi(final String selectedId) {
        common.showProgressRelativeLayout(loader);
        HashMap<String, String> param = new HashMap<>();
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        param.put("selected_val", selectedId);
        param.put("status", "delete");
        param.put("mode", "inbox");

        common.makePostRequest(AppConstants.delete_msg, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("success")) {
                    is_mark = false;
                    menu.findItem(R.id.delete).setVisible(false);
                    menu.findItem(R.id.mark_all).setVisible(false);
                    list.clear();
                    getList();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            if (is_mark) {
                for (ConversationItem itm : list) {
                    itm.setIs_checked(false);
                }
                adapter.notifyDataSetChanged();
                menu.findItem(R.id.delete).setVisible(false);
                menu.findItem(R.id.mark_all).setVisible(false);
                is_mark = false;
            } else {
                onBackPressed();
            }

        } else if (id == R.id.delete) {
            deleteMsg();
        } else if (id == R.id.mark_all) {
            for (ConversationItem itm : list) {
                itm.setIs_checked(true);
            }
            adapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

}
