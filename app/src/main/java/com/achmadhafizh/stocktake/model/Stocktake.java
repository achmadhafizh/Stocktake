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
    private String nik;
    private String scanner;
    private int flag;

    public Stocktake() {
    }

    public Stocktake(int id) {
        this.id = id;
    }

    public Stocktake(int id, int qty) {
        this.id = id;
        this.qty = qty;
    }

    public Stocktake(String type, String fixture, String bc1, String bc2, int qty, String nik, String scanner, int flag) {
        this.type = type;
        this.fixture = fixture;
        this.bc1 = bc1;
        this.bc2 = bc2;
        this.qty = qty;
        this.nik = nik;
        this.scanner = scanner;
        this.flag = flag;
    }

    public Stocktake(int id, String type, String fixture, String bc1, String bc2, int qty, String nik, String scanner, int flag) {
        this.id = id;
        this.type = type;
        this.fixture = fixture;
        this.bc1 = bc1;
        this.bc2 = bc2;
        this.qty = qty;
        this.nik = nik;
        this.scanner = scanner;
        this.flag = flag;
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

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getScanner() {
        return scanner;
    }

    public void setScanner(String scanner) {
        this.scanner = scanner;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
