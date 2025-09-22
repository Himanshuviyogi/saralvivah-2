package com.advance.sanatani_vivah.model;

import java.util.HashMap;

public class SavedItem {
    String name,search_data,search_name,id;
    HashMap<String,String> searchData;

    public SavedItem() {

    }

    public HashMap<String, String> getSearchData() {
        return searchData;
    }

    public void setSearchData(HashMap<String, String> searchData) {
        this.searchData = searchData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSearch_name() {
        return search_name;
    }

    public void setSearch_name(String search_name) {
        this.search_name = search_name;
    }

    public String getSearch_data() {
        return search_data;
    }

    public void setSearch_data(String search_data) {
        this.search_data = search_data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
