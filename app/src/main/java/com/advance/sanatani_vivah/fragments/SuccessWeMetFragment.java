package com.advance.sanatani_vivah.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.adapter.SuccessStoryListAdapter;
import com.advance.sanatani_vivah.model.SuccessStoryBean;
import com.advance.sanatani_vivah.network.ConnectionDetector;
import com.advance.sanatani_vivah.retrofit.AppApiService;
import com.advance.sanatani_vivah.retrofit.RetrofitClient;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class SuccessWeMetFragment extends Fragment implements SuccessStoryListAdapter.ItemListener {

    private View view;
    private RecyclerView recyclerView;
    private TextView lblNoRecordsFound;
    private RelativeLayout loader;

    private List<SuccessStoryBean> arrayList = new ArrayList<>();
    private SuccessStoryListAdapter adapter;

    private int pageNumber = 1;

    private SessionManager session;
    private Common common;
    //Retrofit related
    private Retrofit retrofit;
    private AppApiService appApiService;

    public SuccessWeMetFragment() {
        // Required empty public constructor
    }

    public static SuccessWeMetFragment newInstance(String param1, String param2) {
        SuccessWeMetFragment fragment = new SuccessWeMetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_success_we_met, container, false);
        setHasOptionsMenu(false);
        initialize();

        return view;
    }

    private void initialize() {
        loader = view.findViewById(R.id.loader);
        recyclerView = view.findViewById(R.id.recyclerView);
        arrayList = new ArrayList<>();
        lblNoRecordsFound = view.findViewById(R.id.lblNoRecordsFound);

        common = new Common(getActivity());
        session = new SessionManager(getActivity());
        retrofit = RetrofitClient.getClientWithHeaderAfterLogin();
        appApiService = retrofit.create(AppApiService.class);

       // initializeRecyclerView();
        getSuccessStoryList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_refresh, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            refreshList();
        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshList() {
        pageNumber = 1;
        arrayList.clear();
        getSuccessStoryList();
    }

    String weddingphotoUrl = "";
    private void getSuccessStoryList() {
        if (!ConnectionDetector.isConnectingToInternet(getActivity())) {
            Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
            return;
        }

        if (pageNumber == 1)
            common.showProgressRelativeLayout(loader);
        Map<String, String> params = new HashMap<>();
        params.put("user_agent", AppConstants.USER_AGENT);
        params.put("page", String.valueOf(pageNumber));

        for (String string : params.values()) {
            AppDebugLog.print("params : " + string + "\n");
        }

        Call<JsonObject> call = appApiService.getSuccessStoryListRequest(params);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                common.hideProgressRelativeLayout(loader);
                JsonObject data = response.body();
                AppDebugLog.print("response in getSuccessStoryList : " + data);
                if (data != null) {
                    if (data.get("status").getAsString().equalsIgnoreCase("success")) {

                        if(data.get("total_count").getAsInt() > 0){

                            weddingphotoUrl = data.get("weddingphotoUrl").getAsString();

                            Gson gson = new GsonBuilder().setDateFormat(common.GSONDateTimeFormat).create();
                            List<SuccessStoryBean> tempArrayList = gson.fromJson(data.getAsJsonArray("data"), new TypeToken<List<SuccessStoryBean>>() {
                            }.getType());

                            //when tempArrayList size > 0
                            if (tempArrayList.size() > 0 && arrayList.size() > 0) {
                                //remove loading view
                                arrayList.remove(arrayList.size() - 1);
                                arrayList.addAll(tempArrayList);
                                adapter.notifyDataChanged();
                                initializeRecyclerView();
                            }

                            //when activity start
                            if (arrayList.size() == 0) {
                                arrayList.addAll(tempArrayList);
                                //adapter.notifyDataChanged();
                                initializeRecyclerView();
                                if (arrayList.size() == 0) {
                                    lblNoRecordsFound.setVisibility(View.VISIBLE);
                                }
                            }

                            //when new product list size == 0
                            if (tempArrayList.size() == 0 && arrayList.size() > 0) {
                                //not more data available on server
                                adapter.setMoreDataAvailable(false);
                                //remove loading view
                                arrayList.remove(arrayList.size() - 1);
                                adapter.notifyDataChanged();
                                lblNoRecordsFound.setVisibility(View.GONE);
                            }
                        }else{
                            lblNoRecordsFound.setVisibility(View.VISIBLE);
                        }
                    } else {
                        //not more data available on server
                        adapter.setMoreDataAvailable(false);
                        if (arrayList.size() > 0) {
                            //remove loading view
                            arrayList.remove(arrayList.size() - 1);
                            adapter.notifyDataChanged();
                            lblNoRecordsFound.setVisibility(View.GONE);
                        } else {
                            lblNoRecordsFound.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Something went to wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AppDebugLog.print("error in getSuccessStoryList : " + t.getMessage());
                Toast.makeText(getActivity(), "Something went to wrong!", Toast.LENGTH_LONG).show();
                common.hideProgressRelativeLayout(loader);
            }
        });
    }

    private void initializeRecyclerView() {
        adapter = new SuccessStoryListAdapter(getActivity(), arrayList,weddingphotoUrl);
        adapter.setListener(this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        //mLayoutManager.setAutoMeasureEnabled(true);
        //recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setLoadMoreListener(() -> {
            recyclerView.post(() -> {
                loadMore();// a method which requests remote data
            });
            //Calling loadMore function in Runnable to fix the
            // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
        });
        recyclerView.setAdapter(adapter);
    }

    /**
     * Load more task on scroll task list
     */
    private void loadMore() {
        pageNumber++;
        //add loading progress view
        AppDebugLog.print("In loadMore");
        SuccessStoryBean successStoryBean = new SuccessStoryBean();
//        successStoryBean.setBridename(getString(R.string.str_loading));
        arrayList.add(successStoryBean);
        getSuccessStoryList();
        adapter.notifyItemInserted(arrayList.size() - 1);
    }

    @Override
    public void onItemClick(SuccessStoryBean item) {

    }

    @Override
    public void startVideoDialog(SuccessStoryBean item) {
        FragmentManager fragmentManager = getFragmentManager();
        VideoViewDialogFragment newFragment = VideoViewDialogFragment.newInstance(item.getWeddingphoto());
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }
}