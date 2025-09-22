package com.advance.sanatani_vivah.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import androidx.appcompat.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SpinnerAdapter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MultiSelectionSpinner extends AppCompatSpinner implements OnMultiChoiceClickListener {

    public interface OnMultipleItemsSelectedListener {
        void selectedIndices(List<Integer> indices);
        void selectedStrings(MultiSelectionSpinner spinner,List<String> strings);
    }

    private OnMultipleItemsSelectedListener listener;

    private String popup_title = "Select Option";
    private String[] _items = null;
    private Integer[] _ids = null;
    private String[] _ids_string = null;
    private boolean[] mSelection = null;
    private boolean[] mSelectionAtStart = null;
    private String _itemsAtStart = null;
    private MultiSelectionSpinner spinner;

    ArrayAdapter<String> simple_adapter;

    public MultiSelectionSpinner(Context context) {
        super(context);

        simple_adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item);
        super.setAdapter(simple_adapter);
    }

    public void setSpinnerObject(MultiSelectionSpinner spinner) {
        this.spinner = spinner;
    }

    public MultiSelectionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        simple_adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item);
        super.setAdapter(simple_adapter);
    }

    public void setListener(OnMultipleItemsSelectedListener listener) {
        this.listener = listener;
    }

    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (which == 0) {
            for (int i = 0; i < mSelection.length; i++) {
                mSelection[i] = false;
                mSelectionAtStart[i] = false;
            }
            simple_adapter.notifyDataSetChanged();
        } else {
            if (mSelection != null && which < mSelection.length) {
                for (int i = 0; i < _items.length; i++) {
                    if (_items[i].equals("Doesn't Matter")) {
                        if (mSelection[i]) {
                            mSelection[i] = false;
                        }
                    } else {
                        mSelection[which] = isChecked;
                        simple_adapter.clear();
                        simple_adapter.add(buildSelectedItemString());
                    }
                }
                simple_adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(popup_title);
        builder.setMultiChoiceItems(_items, mSelection, (dialogInterface, which, isChecked) -> {
            final AlertDialog alert = (AlertDialog) dialogInterface;
            final ListView list = alert.getListView();
            if (which == 0) {
                mSelection[which] = isChecked;
                if (isChecked) {
                    int i = 0;
                    while (i < _items.length) {
                        list.setItemChecked(i, false);
                        mSelection[i] = false;
                        mSelection[0] = true;
                        i++;
                    }
                }
                list.setItemChecked(which, isChecked);
                simple_adapter.clear();
                simple_adapter.add(buildSelectedItemString());

            } else {
                if (mSelection != null && which < mSelection.length) {
                    mSelection[which] = isChecked;
                    if (isChecked) {
                        list.setItemChecked(0, false);
                        mSelection[0] = false;
                    }

                    list.setItemChecked(which, isChecked);
                    simple_adapter.clear();
                    simple_adapter.add(buildSelectedItemString());
                }
            }
            simple_adapter.notifyDataSetChanged();
        });
        _itemsAtStart = getSelectedItemsAsString();
        builder.setPositiveButton("Submit", (dialog, which) -> {
            if(listener==null) return;
            System.arraycopy(mSelection, 0, mSelectionAtStart, 0, mSelection.length);
            if(mSelection.length > 1){
                listener.selectedStrings(spinner,getSelectedStrings());
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            simple_adapter.clear();
            simple_adapter.add(_itemsAtStart);
            System.arraycopy(mSelectionAtStart, 0, mSelection, 0, mSelectionAtStart.length);
        });
        builder.show();
        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException("setAdapter is not supported by MultiSelectSpinner.");
    }

    public void setItems(String[] items) {
        _items = items;
        mSelection = new boolean[_items.length];
        mSelectionAtStart = new boolean[_items.length];
        simple_adapter.clear();
        simple_adapter.add(_items[0]);
        Arrays.fill(mSelection, false);
        mSelection[0] = true;
        mSelectionAtStart[0] = true;
    }

    public void setItems(List<String> items, List<Integer> ids, String title) {
        popup_title = title;
        _items = items.toArray(new String[items.size()]);
        _ids = ids.toArray(new Integer[items.size()]);
        mSelection = new boolean[_items.length];
        mSelectionAtStart = new boolean[_items.length];
        simple_adapter.clear();
        simple_adapter.add(_items[0]);
        Arrays.fill(mSelection, false);
        mSelection[0] = true;
    }

    public void setItems_string_id(List<String> items, List<String> ids, String title) {
        popup_title = title;
        _items = items.toArray(new String[items.size()]);
        _ids_string = ids.toArray(new String[items.size()]);
        mSelection = new boolean[_items.length];
        mSelectionAtStart = new boolean[_items.length];
        simple_adapter.clear();
        simple_adapter.add(_items[0]);
        Arrays.fill(mSelection, false);
        mSelection[0] = true;
    }

    public void setSelection(String[] selection) {
        if (selection[0].equals("null") || selection[0].equals("")) {
            setSelection(0);
        } else {
            for (int i = 0; i < mSelection.length; i++) {
                mSelection[i] = false;
                mSelectionAtStart[i] = false;
            }
            for (String cell : selection) {
                for (int j = 0; j < _ids_string.length; ++j) {//_items
                    if (_ids_string[j].equals(cell)) {//_items
                        mSelection[j] = true;
                        mSelectionAtStart[j] = true;
                    }
                }
            }
            simple_adapter.clear();
            simple_adapter.add(buildSelectedItemString());
        }
    }

    public void setSelection(int index) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
            mSelectionAtStart[i] = false;
        }
        if (index >= 0 && index < mSelection.length) {
            mSelection[index] = true;
            mSelectionAtStart[index] = true;
        } else {
            throw new IllegalArgumentException("Index " + index + " is out of bounds.");
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public List<String> getSelectedStrings() {
        List<String> selection = new LinkedList<>();
        for (int i = 0; i < _ids_string.length; ++i) {
            if (mSelection[i]) {
                selection.add(_ids_string[i]);
            }
        }
        if (selection.size() == 0) {
            selection.add("0");
            setSelection(0);
        }
        return selection;
    }

    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;

                sb.append(_items[i]);
            }
        }
        return sb.toString();
    }

    public String getSelectedItemsAsString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;
                sb.append(_items[i]);
            }
        }
        return sb.toString();
    }

    public void setSelection(List<String> selection) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
            mSelectionAtStart[i] = false;
        }
        for (String sel : selection) {
            for (int j = 0; j < _items.length; ++j) {
                if (_items[j].equals(sel)) {
                    mSelection[j] = true;
                    mSelectionAtStart[j] = true;
                }
            }
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public void setSelection(int[] selectedIndices) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
            mSelectionAtStart[i] = false;
        }
        for (int index : selectedIndices) {
            if (index >= 0 && index < mSelection.length) {
                mSelection[index] = true;
                mSelectionAtStart[index] = true;
            } else {
                throw new IllegalArgumentException("Index " + index + " is out of bounds.");
            }
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public List<Integer> getSelectedIndices() {
        List<Integer> selection = new LinkedList<>();
        for (int i = 0; i < _ids.length; ++i) {
            if (mSelection[i]) {
                selection.add(_ids[i]);
            }
        }
        return selection;
    }

    public List<String> getSelectedIdString() {
        List<String> selection = new LinkedList<>();
        for (int i = 0; i < _ids_string.length; ++i) {
            if (mSelection[i]) {
                selection.add(_ids_string[i]);
            }
        }
        return selection;
    }

}