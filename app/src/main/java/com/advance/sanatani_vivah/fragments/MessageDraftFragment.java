package com.advance.sanatani_vivah.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.advance.sanatani_vivah.adapter.MessageAdapter;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.custom.EndlessRecyclerViewScrollListener;
import com.advance.sanatani_vivah.model.Message_item;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.custom.RecyclerItemTouchHelper;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.advance.sanatani_vivah.utility.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MessageDraftFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private RecyclerView recycler_draft;
    private EditText search_view;
    private RelativeLayout loader;
    private Context context;
    private Common common;
    private SessionManager session;
    private boolean continue_request;
    private List<Message_item> list = new ArrayList<>();
    private MessageAdapter adapter;
    private int page = 0;
    private TextView tv_no_data;

    public MessageDraftFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        common = new Common(context);
        session = new SessionManager(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_draft, container, false);

        loader = view.findViewById(R.id.loader);
        search_view = view.findViewById(R.id.search_view);
        recycler_draft = view.findViewById(R.id.recycler_draft);
        tv_no_data = view.findViewById(R.id.tv_no_data);

        search_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = search_view.getText().toString().toLowerCase(Locale.getDefault());
                adapter.getFilter().filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recycler_draft.setLayoutManager(mLayoutManager);
        recycler_draft.setItemAnimator(new DefaultItemAnimator());
        recycler_draft.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        adapter = new MessageAdapter(getActivity(), list);
        recycler_draft.setAdapter(adapter);

        page = page + 1;
        getMessage(page);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler_draft);

        recycler_draft.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int pag, int totalItemsCount, RecyclerView view) {
                if (continue_request) {
                    page = page + 1;
                    getMessage(page);
                }
            }
        });


        return view;
    }

    private void deletemessge(final int position) {
        HashMap<String, String> param = new HashMap<>();
        param.put("status", "delete");
        param.put("mode", "draft");
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));
        param.put("selected_val", list.get(position).getId());

        common.makePostRequest(AppConstants.update_status, param, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("success")) {
                    adapter.removeItem(position);
                    common.showAlert("Delete", object.getString("error_meessage"), R.drawable.trash_red);
                    if (list.size() == 0) {
                        tv_no_data.setVisibility(View.VISIBLE);
                        recycler_draft.setVisibility(View.GONE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        });

    }

    private void getMessage(int page) {
        common.showProgressRelativeLayout(loader);
        HashMap<String, String> param = new HashMap<>();
        param.put("mode", "draft");
        param.put("page_number", String.valueOf(page));
        param.put("matri_id", session.getLoginData(SessionManager.KEY_MATRI_ID));

        common.makePostRequest(AppConstants.message_list, param, response -> {
            common.hideProgressRelativeLayout(loader);
            Log.d("resp", response);
            try {
                JSONObject object = new JSONObject(response);
                continue_request = object.getBoolean("continue_request");
                int total_count = object.getInt("total_count");
                if (total_count != 0) {
                    tv_no_data.setVisibility(View.GONE);
                    recycler_draft.setVisibility(View.VISIBLE);
                    if (total_count != list.size()) {

                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            Message_item item = new Message_item();

                            item.setId(obj.getString("id"));
                            item.setContent(obj.getString("content"));
                            item.setRead_status(obj.getString("read_status"));
                            item.setReceiver(obj.getString("receiver"));
                            item.setReceiver_delete(obj.getString("receiver_delete"));
                            item.setSender(obj.getString("sender"));
                            item.setDisplay_name(obj.getString("receiver"));
                            item.setSender_delete(obj.getString("sender_delete"));
                            item.setSent_on(common.changeDate(obj.getString("sent_on"), "MMM dd,yy hh:mm a"));
                            item.setSubject(obj.getString("subject"));
                            if (obj.getJSONArray("member_photo").length() != 0) {
                                String url = obj.getJSONArray("member_photo").getJSONObject(0).getString("photo1");
                                item.setImage(url);
                            } else
                                item.setImage("");

                            list.add(item);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    tv_no_data.setVisibility(View.VISIBLE);
                    recycler_draft.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> common.hideProgressRelativeLayout(loader));

    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof MessageAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = list.get(viewHolder.getAdapterPosition()).getSender();

            // backup of removed item for undo purpose
            final Message_item deletedItem = list.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            //alert.setTitle("Delete Message");
            alert.setMessage("Are you sure you want to delete this message?");
            alert.setPositiveButton("Yes", (dialogInterface, i) -> deletemessge(deletedIndex));
            alert.setNegativeButton("No", (dialogInterface, i) -> {
                adapter.removeItem(viewHolder.getAdapterPosition());
                adapter.restoreItem(deletedItem, deletedIndex);
            });
            alert.show();
        }
    }


}
