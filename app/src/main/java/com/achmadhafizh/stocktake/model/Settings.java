package com.achmadhafizh.stocktake.model;

/**
 * Created by achmad.hafizh on 10/26/2017.
 */

public class Settings {
    private String store_no;
    private String store_name;

    public Settings(String store_no, String store_name) {
        this.store_no = store_no;
        this.store_name = store_name;
    }

    public String getStore_no() {
        return store_no;
    }

    public void setStore_no(String store_no) {
        this.store_no = store_no;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }
}
