package com.androidbuts.multispinnerfilter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MultiSpinnerSearch extends AppCompatSpinner implements OnCancelListener {
    private static final String TAG = MultiSpinnerSearch.class.getSimpleName();
    private List<KeyPairBoolData> items = new ArrayList<>();
    private String defaultText = "";
    private String spinnerTitle = "";
    private SpinnerListener listener;
    private int limit = -1;
    private int selected = 0;
    private LimitExceedListener limitListener;
    private MyAdapter adapter;
    public static AlertDialog.Builder builder;
    public static AlertDialog ad;
    private TextView searchSpinnerTitle;
    private ImageButton btnCloseDialog;

    //Added by nasirali 30122019
    private MultiSpinnerSearch multiSpinnerSearch;

    public MultiSpinnerSearch(Context context) {
        super(context);
    }

    public MultiSpinnerSearch(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        TypedArray a = arg0.obtainStyledAttributes(arg1, R.styleable.MultiSpinnerSearch);
        for (int i = 0; i < a.getIndexCount(); ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.MultiSpinnerSearch_hintText) {
                spinnerTitle = a.getString(attr);
                defaultText = spinnerTitle;
                break;
            }
        }
        Log.i(TAG, "spinnerTitle: " + spinnerTitle);
        a.recycle();
    }

    public MultiSpinnerSearch(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    public String getSelectedIdsInString() {
        String selectedItemsIds = "";
        for (KeyPairBoolData item : items) {
            if (!item.getId().equalsIgnoreCase("0") && item.isSelected()) {
                if (selectedItemsIds.length() == 0) {
                    selectedItemsIds = item.getId();
                } else {
                    selectedItemsIds = selectedItemsIds + "," + item.getId();
                }
            }
        }
        return selectedItemsIds;
    }

    public void setItems(MultiSpinnerSearch multiSpinnerSearch, List<KeyPairBoolData> items, int position, SpinnerListener listener, String spinnerHint) {
        this.items = items;
        this.listener = listener;
        this.multiSpinnerSearch = multiSpinnerSearch;

        spinnerTitle = spinnerHint;
        defaultText = spinnerTitle;

        StringBuilder spinnerBuffer = new StringBuilder();

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isSelected()) {//if (i > 0 && items.get(i).isSelected()) {
                spinnerBuffer.append(items.get(i).getName());
                spinnerBuffer.append(", ");
            }
        }
        String spinnerText = spinnerBuffer.toString();
        if (spinnerText.length() > 2)
            spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        else
            spinnerText = defaultText;

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getContext(), R.layout.textview_for_spinner, new String[]{spinnerText});
        setAdapter(adapterSpinner);

        if (position != -1) {
            onCancel(null);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner
        StringBuilder spinnerBuffer = new StringBuilder();
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                //AppDebugLog.print("in value onCancel : "+ items.get(i).isSelected());
                if (items.get(i).isSelected()) {//if (i > 0 && items.get(i).isSelected()) {
                    spinnerBuffer.append(items.get(i).getName());
                    spinnerBuffer.append(", ");
                }
            }
        }

        String spinnerText = spinnerBuffer.toString();
        if (spinnerText.length() > 2)
            spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        else
            spinnerText = defaultText;

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getContext(), R.layout.textview_for_spinner, new String[]{spinnerText});
        setAdapter(adapterSpinner);

        if (adapter != null)
            adapter.notifyDataSetChanged();

        if (items != null) listener.onItemsSelected(multiSpinnerSearch);
    }

    @Override
    public boolean performClick() {
        builder = new AlertDialog.Builder(getContext(), R.style.FullScreenDialogStyle);
        //builder.setTitle(spinnerTitle);

        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view = inflater.inflate(R.layout.alert_dialog_listview_search, null);
        builder.setView(view);

        final ListView listView = view.findViewById(R.id.alertSearchListView);
        searchSpinnerTitle = view.findViewById(R.id.searchSpinnerTitle);
        btnCloseDialog = view.findViewById(R.id.btnCloseDialog);
        searchSpinnerTitle.setText(spinnerTitle);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setFastScrollEnabled(false);
        adapter = new MyAdapter(getContext(), items);
        listView.setAdapter(adapter);

        final TextView emptyText = view.findViewById(R.id.empty);
        listView.setEmptyView(emptyText);

        final EditText editText = view.findViewById(R.id.alertSearchEditText);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            dialog.cancel();
        });

        btnCloseDialog.setOnClickListener(view12 -> ad.cancel());

        builder.setOnCancelListener(this);

        //2. now setup to change color of the button
        ad = builder.create();
        ad.setOnShowListener(arg0 -> {
            ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.ColorPrimary));
        });
        ad.show();
        ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        return true;
    }

    public void setSelection(String selectionIds) {
        String[] values = selectionIds.split(",");
        if (values == null || values[0].equals("null") || values[0].equals("")) {
            onCancel(null);
        } else {
            for (KeyPairBoolData keyPairBoolData : items) {
                for (int i = 0; i < values.length; i++) {
                    if (keyPairBoolData.getId().equalsIgnoreCase(values[i])) {
                        AppDebugLog.print("id & selection id : " + keyPairBoolData.getId() + " : " + values[i]);
                        keyPairBoolData.setSelected(true);
                    }
                }
            }
            onCancel(null);
        }
    }

    //Adapter Class
    public class MyAdapter extends BaseAdapter implements Filterable {

        List<KeyPairBoolData> arrayList;
        List<KeyPairBoolData> mOriginalValues; // Original Values
        LayoutInflater inflater;

        public MyAdapter(Context context, List<KeyPairBoolData> arrayList) {
            this.arrayList = arrayList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (arrayList == null) {
                return 0;
            }
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            TextView textView;
            CheckBox checkBox;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Log.i(TAG, "getView() enter");
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_listview_multiple, parent, false);
                holder.textView = convertView.findViewById(R.id.alertTextView);
                holder.checkBox = convertView.findViewById(R.id.alertCheckbox);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //final int backgroundColor = (position%2 == 0) ? R.color.list_even : R.color.list_odd;
            //convertView.setBackgroundColor(ContextCompat.getColor(getContext(), backgroundColor));

            final KeyPairBoolData data = arrayList.get(position);

            holder.textView.setText(data.getName());
            holder.textView.setTypeface(null, Typeface.NORMAL);
            holder.textView.setTextColor(ContextCompat.getColor(getContext(), R.color.ColorDarkGrey));
            holder.checkBox.setChecked(data.isSelected());

            convertView.setOnClickListener(v -> {
                if (data.isSelected()) { // deselect
                    selected--;
                } else { // selected
                    selected++;
                }

                final ViewHolder temp = (ViewHolder) v.getTag();
                temp.checkBox.setChecked(!temp.checkBox.isChecked());

                data.setSelected(!data.isSelected());
                Log.i(TAG, "On Click Selected Item : " + data.getName() + " : " + data.isSelected());
                notifyDataSetChanged();
            });
            if (data.isSelected()) {
                holder.textView.setTypeface(null, Typeface.BOLD);
                holder.textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                //convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.list_selected));
            }
            holder.checkBox.setTag(holder);

            return convertView;
        }

        @SuppressLint("DefaultLocale")
        @Override
        public Filter getFilter() {
            return new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    arrayList = (List<KeyPairBoolData>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    List<KeyPairBoolData> FilteredArrList = new ArrayList<>();

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<>(arrayList); // saves the original data in mOriginalValues
                    }

                    /********
                     *
                     *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                     *  else does the Filtering and returns FilteredArrList(Filtered)
                     *
                     ********/
                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < mOriginalValues.size(); i++) {
                            Log.i(TAG, "Filter : " + mOriginalValues.get(i).getName() + " -> " + mOriginalValues.get(i).isSelected());
                            String data = mOriginalValues.get(i).getName();
                            if (data.toLowerCase().contains(constraint.toString())) {
                                FilteredArrList.add(mOriginalValues.get(i));
                            }
                        }
                        // set the Filtered result to return
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    }
                    return results;
                }
            };
        }
    }

    public interface LimitExceedListener {
        void onLimitListener(KeyPairBoolData data);
    }

    public void setLimit(int limit, LimitExceedListener listener) {
        this.limit = limit;
        this.limitListener = listener;
    }

    public List<KeyPairBoolData> getSelectedItems() {
        List<KeyPairBoolData> selectedItems = new ArrayList<>();
        for (KeyPairBoolData item : items) {
            if (!item.getId().equalsIgnoreCase("0") && item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    public List<String> getSelectedIds() {
        List<String> selectedItemsIds = new ArrayList<>();
        for (KeyPairBoolData item : items) {
            if (!item.getId().equalsIgnoreCase("0") && item.isSelected()) {
                selectedItemsIds.add(item.getId());
            }
        }
        return selectedItemsIds;
    }
}