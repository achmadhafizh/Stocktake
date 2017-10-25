package com.achmadhafizh.stocktake.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.achmadhafizh.stocktake.model.Stocktake;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Created by achmad.hafizh on 10/17/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "stocktakeManager";

    // table name
    private static final String TABLE_STOCKTAKE = "stocktake";

    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_FIXTURE = "fixture";
    private static final String KEY_BC1 = "bc1";
    private static final String KEY_BC2 = "bc2";
    private static final String KEY_QTY = "qty";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_STOCKTAKE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TYPE + " TEXT," + KEY_FIXTURE + " TEXT,"
                + KEY_BC1 + " TEXT," + KEY_BC2 + " TEXT," + KEY_QTY + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCKTAKE);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addStocktake(Stocktake stocktake) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, stocktake.getType());
        values.put(KEY_FIXTURE, stocktake.getFixture());
        values.put(KEY_BC1, stocktake.getBc1());
        values.put(KEY_BC2, stocktake.getBc2());
        values.put(KEY_QTY, stocktake.getQty());

        // Inserting Row
        db.insert(TABLE_STOCKTAKE, null, values);
        db.close(); // Closing database connection
    }

    // Getting single stocktake
    public Stocktake getSpesificStocktake(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_STOCKTAKE, new String[] { KEY_ID, KEY_TYPE, KEY_FIXTURE,
                                                                 KEY_BC1, KEY_BC2, KEY_QTY }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Stocktake stocktake = new Stocktake(parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), parseInt(cursor.getString(5)));
        // return stocktake
        return stocktake;
    }

    // Getting single stocktake
    public Stocktake getSpesificStocktake(String type, String fixture, String bc1, String bc2) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_STOCKTAKE, new String[] { KEY_ID, KEY_TYPE, KEY_FIXTURE,
                        KEY_BC1, KEY_BC2, KEY_QTY }, KEY_TYPE + "=? AND " + KEY_FIXTURE + "=? AND " + KEY_BC1 + "=? AND " + KEY_BC2 + "=?",
                new String[] { String.valueOf(type), String.valueOf(fixture), String.valueOf(bc1), String.valueOf(bc2) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Stocktake stocktake = new Stocktake(parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), parseInt(cursor.getString(5)));
        // return stocktake
        return stocktake;
    }

    // Getting All Stocktake
    public List<Stocktake> getAllStocktake() {
        List<Stocktake> stocktakeList = new ArrayList<Stocktake>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STOCKTAKE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Stocktake stocktake = new Stocktake();
                stocktake.setId(parseInt(cursor.getString(0)));
                stocktake.setType(cursor.getString(1));
                stocktake.setFixture(cursor.getString(2));
                stocktake.setBc1(cursor.getString(3));
                stocktake.setBc2(cursor.getString(4));
                stocktake.setQty(parseInt(cursor.getString(5)));
                // Adding stocktake to list
                stocktakeList.add(stocktake);
            } while (cursor.moveToNext());
        }

        // return contact list
        return stocktakeList;
    }

    // Updating single stocktake
    public int updateStocktake(Stocktake stocktake) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QTY, stocktake.getQty());

        // updating row
        return db.update(TABLE_STOCKTAKE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(stocktake.getId()) });
    }

    // Deleting single stocktake
    public void deleteStocktake(Stocktake stocktake) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STOCKTAKE, KEY_ID + " = ?",
                new String[] { String.valueOf(stocktake.getId()) });
        db.close();
    }


    // Getting stocktake Count
    public int getStocktakeCount() {
        String countQuery = "SELECT  * FROM " + TABLE_STOCKTAKE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

    // Getting All Stocktake DP
    public List<Stocktake> getAllStocktakeDP() {
        List<Stocktake> stocktakeList = new ArrayList<Stocktake>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STOCKTAKE + " WHERE " + KEY_TYPE + " = 'DP'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Stocktake stocktake = new Stocktake();
                stocktake.setId(parseInt(cursor.getString(0)));
                stocktake.setType(cursor.getString(1));
                stocktake.setFixture(cursor.getString(2));
                stocktake.setBc1(cursor.getString(3));
                stocktake.setBc2(cursor.getString(4));
                stocktake.setQty(parseInt(cursor.getString(5)));
                // Adding stocktake to list
                stocktakeList.add(stocktake);
            } while (cursor.moveToNext());
        }

        // return contact list
        return stocktakeList;
    }

    // Getting All Stocktake CS
    public List<Stocktake> getAllStocktakeCS() {
        List<Stocktake> stocktakeList = new ArrayList<Stocktake>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STOCKTAKE + " WHERE " + KEY_TYPE + " = 'CS'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Stocktake stocktake = new Stocktake();
                stocktake.setId(parseInt(cursor.getString(0)));
                stocktake.setType(cursor.getString(1));
                stocktake.setFixture(cursor.getString(2));
                stocktake.setBc1(cursor.getString(3));
                stocktake.setBc2(cursor.getString(4));
                stocktake.setQty(parseInt(cursor.getString(5)));
                // Adding stocktake to list
                stocktakeList.add(stocktake);
            } while (cursor.moveToNext());
        }

        // return contact list
        return stocktakeList;
    }

    // Getting stocktake DP Count
    public int getStocktakeCountDP() {
        String countQuery = "SELECT  * FROM " + TABLE_STOCKTAKE + " WHERE " + KEY_TYPE + " = 'DP'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

    // Getting stocktake DP Count
    public int getStocktakeCountDP(String fixture, String bc1, String bc2) {
        String countQuery = "SELECT  * FROM " + TABLE_STOCKTAKE + " WHERE " +
                            KEY_TYPE + " = 'DP' AND " +
                            KEY_FIXTURE + " = " + fixture + " AND " +
                            KEY_BC1 + " = " + bc1 + " AND " +
                            KEY_BC2 + " = " + bc2;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

    // Getting stocktake CS Count
    public int getStocktakeCountCS() {
        String countQuery = "SELECT  * FROM " + TABLE_STOCKTAKE + " WHERE " + KEY_TYPE + " = 'CS'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

    // Getting stocktake CS Count
    public int getStocktakeCountCS(String fixture, String bc1, String bc2) {
        String countQuery = "SELECT  * FROM " + TABLE_STOCKTAKE + " WHERE " +
                            KEY_TYPE + " = 'CS' AND " +
                            KEY_FIXTURE + " = '" + fixture + "' AND " +
                            KEY_BC1 + " = '" + bc1 + "' AND " +
                            KEY_BC2 + " = '" + bc2 + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

}