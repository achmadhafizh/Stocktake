package com.achmadhafizh.stocktake.model;

/**
 * Created by achmad.hafizh on 10/17/2017.
 */

public class Stocktake {
    private int id;
    private String type;
    private String fixture;
    private String bc1;
    private String bc2;
    private int qty;

    public Stocktake() {
    }

    public Stocktake(int id) {
        this.id = id;
    }

    public Stocktake(int id, int qty) {
        this.id = id;
        this.qty = qty;
    }

    public Stocktake(String type, String fixture, String bc1, String bc2, int qty) {
        this.type = type;
        this.fixture = fixture;
        this.bc1 = bc1;
        this.bc2 = bc2;
        this.qty = qty;
    }

    public Stocktake(int id, String type, String fixture, String bc1, String bc2, int qty) {
        this.id = id;
        this.type = type;
        this.fixture = fixture;
        this.bc1 = bc1;
        this.bc2 = bc2;
        this.qty = qty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFixture() {
        return fixture;
    }

    public void setFixture(String fixture) {
        this.fixture = fixture;
    }

    public String getBc1() {
        return bc1;
    }

    public void setBc1(String bc1) {
        this.bc1 = bc1;
    }

    public String getBc2() {
        return bc2;
    }

    public void setBc2(String bc2) {
        this.bc2 = bc2;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
