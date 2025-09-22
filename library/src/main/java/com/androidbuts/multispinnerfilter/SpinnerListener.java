package com.androidbuts.multispinnerfilter;

import java.util.List;

public interface SpinnerListener {
    void onItemsSelected(MultiSpinnerSearch singleSpinnerSearch);
    void onItemsSelected(SingleSpinnerSearch singleSpinnerSearch,KeyPairBoolData item);
}
