package com.advance.sanatani_vivah.dynamicprofile;

/**
 * Created by bpncool on 2/24/2016.
 */

/**
 * interface to listen changes in state of sections
 */
public interface SectionStateChangeListener {
    void onSectionStateChanged(ViewProfileSectionBean section, boolean isOpen);
}