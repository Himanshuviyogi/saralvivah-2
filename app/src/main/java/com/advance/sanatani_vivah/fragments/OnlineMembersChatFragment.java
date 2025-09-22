package com.advance.sanatani_vivah.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.adapter.ChatMemberListAdapter;
import com.advance.sanatani_vivah.model.ChatMemberBean;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class OnlineMembersChatFragment extends Fragment {
    private NestedScrollView scrollView;
    private RecyclerView rvChatList;
    private EditText etSearch;
    private RelativeLayout progressBar;
    private SwipeRefreshLayout swipe;
    private Common common;
    private SessionManager session;
    private boolean continue_request;
    private boolean isFirst = false;
    private List<ChatMemberBean> list = new ArrayList<>();
    private ChatMemberListAdapter adapter;
    private int page = 0;
    private TextView tv_no_data;

    private View view;

    private LinearLayoutManager mLayoutManager;

    private int currentVisibleItemCount;
    private int currentFirstVisibleItem;
    private int totalItem;

    public OnlineMembersChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_online_members_chat, container, false);
        initialize();
        return view;
    }

    private void initialize() {
        session = new SessionManager(requireActivity());
        common = new Common(requireActivity());
        scrollView = view.findViewById(R.id.scrollView);
        scrollView.setVisibility(View.GONE);
        progressBar = view.findViewById(R.id.progressBar);
        swipe = view.findViewById(R.id.swipe);
        etSearch = view.findViewById(R.id.etSearch);
        rvChatList = view.findViewById(R.id.rvChatList);
        tv_no_data = view.findViewById(R.id.tv_no_data);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = etSearch.getText().toString().toLowerCase(Locale.getDefault());
                if(adapter !=null)
                adapter.getFilter().filter(text);
            }
        });

        initializeRecyclerView();

        swipe.setOnRefreshListener(() -> {
            list.clear();
            page = 1;
            getOnlineMembers(page,true);
        });

//        rvChatList.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
//            @Override
//            public void onLoadMore(int pag, int totalItemsCount, RecyclerView view) {
//                if (continue_request) {
//                    page += 1;
//                    getOnlineMembers(page,true);
//                }
//            }
//        });

        rvChatList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    currentVisibleItemCount = mLayoutManager.getChildCount();
                    totalItem = mLayoutManager.getItemCount();
                    currentFirstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                    if (continue_request) {
                        if ((currentVisibleItemCount + currentFirstVisibleItem) >= totalItem) {
                            continue_request = false;
                            page = page + 1;
                            getOnlineMembers(1, true);
                        }
                    }
                }
            }
        });

        page = 1;
        getOnlineMembers(1,true);
    }

    public void reloadData() {
        list.clear();
        page = 1;
        getOnlineMembers(page,false);
    }

    private void initializeRecyclerView() {
        mLayoutManager = new LinearLayoutManager(requireActivity());
        rvChatList.setLayoutManager(mLayoutManager);
        rvChatList.setItemAnimator(new DefaultItemAnimator());
       // rvChatList.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        adapter = new ChatMemberListAdapter(requireActivity(), list);
        rvChatList.setAdapter(adapter);
    }

    private void getOnlineMembers(int page,boolean isDisplayLoader) {
        if(page == 1 && isDisplayLoader) {
            common.showProgressRelativeLayout(progressBar);
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("page_number", String.valueOf(page));
        param.put("searchKeyword", "");
        param.put("gender", session.getLoginData(SessionManager.KEY_GENDER));
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));

        AppDebugLog.print("getOnlineMembers params : " + param);

        common.makePostRequestTime(AppConstants.online_chat_list, param, response -> {
            common.hideProgressRelativeLayout(progressBar);
            swipe.setRefreshing(false);
            isFirst = false;
            AppDebugLog.print("getOnlineMembers resp : : " + response);
            try {
                JSONObject object = new JSONObject(response);
                continue_request = object.getBoolean("continue_request");
                int total_count = object.getInt("total_count");
                scrollView.setVisibility(View.VISIBLE);
                if (total_count != 0) {
                    tv_no_data.setVisibility(View.GONE);
                    rvChatList.setVisibility(View.VISIBLE);
                    if (total_count != list.size()) {
                        Gson gson = new GsonBuilder().setDateFormat(AppConstants.GSONDateTimeFormat).create();
                        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
                        List<ChatMemberBean> tempArrayList = gson.fromJson(jsonObject.get("data").getAsJsonArray(), new TypeToken<List<ChatMemberBean>>() {
                        }.getType());
                        list.addAll(tempArrayList);
                        if(adapter != null) {
                            adapter.notifyDataSetChanged();
                        }else{
                            initializeRecyclerView();
                        }
                    }
                } else {
                    tv_no_data.setVisibility(View.VISIBLE);
                    rvChatList.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }
        }, error -> {
            isFirst = false;
            common.hideProgressRelativeLayout(progressBar);
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });
    }
}