package com.achmadhafizh.stocktake.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.achmadhafizh.stocktake.R;
import com.achmadhafizh.stocktake.activity.MainActivity;
import com.achmadhafizh.stocktake.app.MyApplication;
import com.achmadhafizh.stocktake.helper.DatabaseManager;
import com.achmadhafizh.stocktake.helper.SettingsManager;
import com.achmadhafizh.stocktake.model.Stocktake;
import com.achmadhafizh.stocktake.utilities.CommonConstant;
import com.achmadhafizh.stocktake.utilities.CustomProgressDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.achmadhafizh.stocktake.utilities.CommonConstant.DIRECTORY_STOCKTAKE_CS;
import static com.achmadhafizh.stocktake.utilities.CommonConstant.DIRECTORY_STOCKTAKE_DP;
import static com.achmadhafizh.stocktake.utilities.CommonConstant.FILE_NAME;

public class SettingsFragment extends Fragment {
    private static final String ARG_ITEM = "item";
    private String TAG = SettingsFragment.class.getSimpleName();
    private SettingsManager settingsManager;
    private DatabaseManager databaseManager;
    private CustomProgressDialog customProgressDialog;
    private HashMap<String, String> settingPrefs;
    private View dialogView;
    private String item;

    @BindView(R.id.tv_scanner_id)
    TextView tvScanner;

    @BindView(R.id.tv_store)
    TextView tvStore;

    @BindView(R.id.tv_stamp)
    TextView tvStamp;

    @BindView(R.id.tv_records_cs)
    TextView tvRecordsCS;

    @BindView(R.id.tv_qty_cs)
    TextView tvQtyCS;

    @BindView(R.id.tv_records_dp)
    TextView tvRecordsDP;

    @BindView(R.id.tv_qty_dp)
    TextView tvQtyDP;

    @BindView(R.id.btn_download_config)
    Button btnDownload;

    @BindView(R.id.btn_upload_records)
    Button btnUpload;

    @BindView(R.id.btn_clear_records)
    Button btnClear;

    public SettingsFragment() {}

    public static SettingsFragment newInstance(String item) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = getArguments().getString(ARG_ITEM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        setUpActionBar();
        databaseManager = new DatabaseManager(getActivity());
        settingsManager = new SettingsManager(getActivity());
        customProgressDialog = new CustomProgressDialog(getActivity(), "Loading...", false);

        showConfiguration();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_download_config)
    public void download() {
        downloadConfig();
    }

    @OnClick(R.id.btn_upload_records)
    public void upload() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String currDate = sdf.format(new Date());
        String file_name_cs = FILE_NAME + settingPrefs.get(CommonConstant.STORE_NO) + currDate;
        String file_name_dp = FILE_NAME + settingPrefs.get(CommonConstant.STORE_NO) + currDate;
        boolean flag_cs = false;
        boolean flag_dp = false;

        List<Stocktake> items_cs = databaseManager.getAllStocktakeCS();

        if(!items_cs.isEmpty()) {
            writeToFile(DIRECTORY_STOCKTAKE_CS, file_name_cs, items_cs, currDate);
        }

        if(!items_cs.isEmpty()) {
            for (Stocktake stk : items_cs) {
                Integer id = stk.getId();
                String scanner = stk.getScanner();
                String fixture = stk.getFixture();
                String store = stk.getFixture().substring(0, 2);
                String sku = stk.getBc1().substring(5, 12);
                String classno = stk.getBc1().substring(0, 5);
                String price = stk.getBc2().substring(1, 12);
                Integer qty = stk.getQty();
                String nik = stk.getNik();
                String type = stk.getType();
                String stamp = currDate;

                uploadDataCS(id.toString(), scanner, fixture, store, sku, classno, price, qty.toString(), nik, stamp, type);
            }

            flag_cs = true;
        }

        List<Stocktake> items_dp = databaseManager.getAllStocktakeDP();

        if(!items_dp.isEmpty()) {
            writeToFile(DIRECTORY_STOCKTAKE_DP, file_name_dp, items_dp, currDate);
        }

        if(!items_dp.isEmpty()) {
            for (Stocktake stk : items_dp) {
                Integer id = stk.getId();
                String scanner = stk.getScanner();
                String fixture = stk.getFixture();
                String store = stk.getFixture().substring(0, 2);
                String sku = stk.getBc1().substring(5, 12);
                String classno = stk.getBc1().substring(0, 5);
                String price = stk.getBc2().substring(1, 12);
                Integer qty = stk.getQty();
                String nik = stk.getNik();
                String type = stk.getType();
                String stamp = currDate;

                uploadDataDP(id.toString(), scanner, fixture, store, sku, classno, price, qty.toString(), nik, stamp, type);
            }

            flag_dp = true;
        }

        if(flag_cs == true || flag_dp == true) {
            showMessage("Successfully uploads");
        } else {
            showMessage("Please check total qty and records");
        }
    }

    @OnClick(R.id.btn_clear_records)
    public void clear() {
        inputPassword();
    }

    @OnClick(R.id.tv_scanner_id)
    public void setScannerID() {
        inputScannerID();
    }

    @SuppressWarnings("ConstantConditions")
    private void setUpActionBar() {
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(item);
        MainActivity.mTitleTextView.setText(item);
    }

    private String formatDate(String formatDate) {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm:ss aaa");

        Date newDate = null;

        try {
            newDate = sdf.parse(currentDateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sdf = new SimpleDateFormat(formatDate);
        currentDateTimeString = sdf.format(newDate);

        return currentDateTimeString;
    }

    public void showConfiguration() {
        settingPrefs = settingsManager.getSettings();

        tvScanner.setText(settingPrefs.get(CommonConstant.SCANNER_ID));
        tvStore.setText(settingPrefs.get(CommonConstant.STORE_NO) + " - " + settingPrefs.get(CommonConstant.STORE_NAME));
        tvStamp.setText(settingPrefs.get(CommonConstant.TIME_STAMP));

        Integer totalRecordsCS = databaseManager.getStocktakeCountCS();
        Integer totalRecordsDP = databaseManager.getStocktakeCountDP();
        Integer totalQtyCs = databaseManager.getStocktakeSummarizeCS();
        Integer totalQtyDp = databaseManager.getStocktakeSummarizeDP();

        tvRecordsCS.setText(totalRecordsCS.toString().trim());
        tvRecordsDP.setText(totalRecordsDP.toString().trim());
        tvQtyCS.setText(totalQtyCs.toString().trim());
        tvQtyDP.setText(totalQtyDp.toString().trim());
    }

    public void showMessage(String msg) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        dialogView = inflater.inflate(R.layout.custom_dialog_error, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        final TextView message = (TextView) dialogView.findViewById(R.id.tv_detail);
        final Button btnOk = (Button) dialogView.findViewById(R.id.btn_ok);

        final AlertDialog alertDialog = dialogBuilder.create();

        message.setText(msg);
        btnOk.setText(getResources().getString(R.string.ok));

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void inputScannerID() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        dialogView = inflater.inflate(R.layout.custom_dialog_input, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        final LinearLayout llOneButton = (LinearLayout) dialogView.findViewById(R.id.ll_one_button);
        final LinearLayout llTwoButton = (LinearLayout) dialogView.findViewById(R.id.ll_two_button);
        final EditText etScannerID = (EditText) dialogView.findViewById(R.id.et_scanner_id);
        final EditText etPassword = (EditText) dialogView.findViewById(R.id.et_password);
        final CheckBox showPassword = (CheckBox) dialogView.findViewById(R.id.showPassword);
        final Button btnYes = (Button) dialogView.findViewById(R.id.button_save);
        final Button btnNo = (Button) dialogView.findViewById(R.id.button_cancel);

        llOneButton.setVisibility(View.GONE);
        llTwoButton.setVisibility(View.VISIBLE);
        btnYes.setText("Yes");
        btnNo.setText("No");

        final AlertDialog alertDialog = dialogBuilder.create();

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                alertDialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                settingPrefs = settingsManager.getSettings();
                String scannerID = etScannerID.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if(scannerID.isEmpty()) {
                    etScannerID.setError(getResources().getString(R.string.err_msg_scanner_id1));
                } else if (scannerID.length() < 2) {
                    etScannerID.setError(getResources().getString(R.string.err_msg_scanner_id2));
                } else if(password.isEmpty()) {
                    etPassword.setError(getResources().getString(R.string.err_msg_password1));
                } else if (password.length() < 6) {
                    etPassword.setError(getResources().getString(R.string.err_msg_password2));
                } else if(!password.equals(settingPrefs.get(CommonConstant.PASSWORD))) {
                    etPassword.setError(getResources().getString(R.string.err_msg_password3));
                } else {
                    settingsManager.saveSettings(scannerID);
                    tvScanner.setText(scannerID);
                    alertDialog.dismiss();
                    showMessage("Configurations successfully updated");
                }
            }
        });

        alertDialog.show();
    }

    private void inputPassword() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        dialogView = inflater.inflate(R.layout.custom_dialog_input, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        final LinearLayout llOneButton = (LinearLayout) dialogView.findViewById(R.id.ll_one_button);
        final LinearLayout llTwoButton = (LinearLayout) dialogView.findViewById(R.id.ll_two_button);
        final EditText etScannerID = (EditText) dialogView.findViewById(R.id.et_scanner_id);
        final EditText etPassword = (EditText) dialogView.findViewById(R.id.et_password);
        final CheckBox showPassword = (CheckBox) dialogView.findViewById(R.id.showPassword);
        final Button btnYes = (Button) dialogView.findViewById(R.id.button_save);
        final Button btnNo = (Button) dialogView.findViewById(R.id.button_cancel);

        etScannerID.setVisibility(View.GONE);
        llOneButton.setVisibility(View.GONE);
        llTwoButton.setVisibility(View.VISIBLE);
        btnYes.setText("Yes");
        btnNo.setText("No");

        final AlertDialog alertDialog = dialogBuilder.create();

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                alertDialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                settingPrefs = settingsManager.getSettings();
                String password = etPassword.getText().toString().trim();

                if(password.isEmpty()) {
                    etPassword.setError(getResources().getString(R.string.err_msg_password1));
                } else if (password.length() < 6) {
                    etPassword.setError(getResources().getString(R.string.err_msg_password2));
                } else if(!password.equals(settingPrefs.get(CommonConstant.PASSWORD))) {
                    etPassword.setError(getResources().getString(R.string.err_msg_password3));
                } else {
                    databaseManager.deleteAllStocktake();
                    showConfiguration();

                    alertDialog.dismiss();
                    showMessage("All records successfully deleted");
                }
            }
        });

        alertDialog.show();
    }

    private void downloadConfig() {
        customProgressDialog.showDialog();

        StringRequest volleyRequest = new StringRequest(Request.Method.POST, CommonConstant.URL_DATA_CONFIG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Download Config Response: " + response.toString());

                try {
                    JSONObject responseObject = new JSONObject(response);
                    boolean error = responseObject.getBoolean("error");

                    if (error) {
                        showMessage(responseObject.getString("message"));
                    } else {
                        JSONArray dataArray = responseObject.getJSONArray("data");
                        for (int k = 0; k < dataArray.length(); k++) {
                            JSONObject data = dataArray.getJSONObject(k);
                            String store_no = data.getString("store_no").trim();
                            String store_name = data.getString("branch_name").trim();
                            String password = data.getString("password").trim();
                            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                            settingsManager.saveSettings("", store_no, store_name, password, currentDateTimeString);

                            tvStore.setText(store_no + " - " + store_name);
                            tvStamp.setText(currentDateTimeString);

                            showMessage("Configurations successfully updated");
                        }
                    }

                } catch (JSONException e) {
                    if (customProgressDialog.isShowing()) {
                        customProgressDialog.hideDialog();
                    }
                    e.printStackTrace();
                } finally {
                    if (customProgressDialog.isShowing()) {
                        customProgressDialog.hideDialog();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (customProgressDialog.isShowing()) {
                        customProgressDialog.hideDialog();
                    }
                    if (error.networkResponse.statusCode == 400) {
                        showMessage(getResources().getString(R.string.network_error));
                    } else {
                        showMessage(getResources().getString(R.string.timeout_error));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        volleyRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(volleyRequest, CommonConstant.URL_DATA_CONFIG);
    }

    private void uploadDataCS(final String id, final String scanner, final String fixture, final String store,
                            final String sku, final String classno, final String price, final String qty,
                            final String nik, final String stamp, final String type) {
        customProgressDialog.showDialog();

        StringRequest volleyRequest = new StringRequest(Request.Method.POST, CommonConstant.URL_DATA_STOCKTAKE_CS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Upload Data CS Response: " + response.toString());

                try {
                    JSONObject responseObject = new JSONObject(response);
                    boolean error = responseObject.getBoolean("error");

                    if (error) {
                        showMessage(responseObject.getString("message"));
                    } else {
                        Integer id = Integer.valueOf(responseObject.getString("id"));

                        databaseManager.deleteStocktake(new Stocktake(id));

                        Log.d(TAG, "Succesfully synchronize for " + "Id : " + id);

                        showConfiguration();
                    }

                } catch (JSONException e) {
                    if (customProgressDialog.isShowing()) {
                        customProgressDialog.hideDialog();
                    }
                    e.printStackTrace();
                } finally {
                    if (customProgressDialog.isShowing()) {
                        customProgressDialog.hideDialog();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (customProgressDialog.isShowing()) {
                        customProgressDialog.hideDialog();
                    }
                    if (error.networkResponse.statusCode == 400) {
                        showMessage(getResources().getString(R.string.network_error));
                    } else {
                        showMessage(getResources().getString(R.string.timeout_error));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params = new HashMap<String, String>();
                params.put("id", id);
                params.put("scanner", scanner);
                params.put("fixture", fixture);
                params.put("store", store);
                params.put("sku", sku);
                params.put("class", classno);
                params.put("price", price);
                params.put("qty", qty);
                params.put("nik", nik);
                params.put("stamp", stamp);
                params.put("type", type);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        volleyRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(volleyRequest, CommonConstant.URL_DATA_STOCKTAKE_CS);
    }

    private void uploadDataDP(final String id, final String scanner, final String fixture, final String store,
                              final String sku, final String classno, final String price, final String qty,
                              final String nik, final String stamp, final String type) {
        customProgressDialog.showDialog();

        StringRequest volleyRequest = new StringRequest(Request.Method.POST, CommonConstant.URL_DATA_STOCKTAKE_DP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Upload Data DP Response: " + response.toString());

                try {
                    JSONObject responseObject = new JSONObject(response);
                    boolean error = responseObject.getBoolean("error");

                    if (error) {
                        showMessage(responseObject.getString("message"));
                    } else {
                        Integer id = Integer.valueOf(responseObject.getString("id"));

                        databaseManager.deleteStocktake(new Stocktake(id));

                        Log.d(TAG, "Succesfully synchronize for " + "Id : " + id);

                        showConfiguration();
                    }

                } catch (JSONException e) {
                    if (customProgressDialog.isShowing()) {
                        customProgressDialog.hideDialog();
                    }
                    e.printStackTrace();
                } finally {
                    if (customProgressDialog.isShowing()) {
                        customProgressDialog.hideDialog();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (customProgressDialog.isShowing()) {
                        customProgressDialog.hideDialog();
                    }
                    if (error.networkResponse.statusCode == 400) {
                        showMessage(getResources().getString(R.string.network_error));
                    } else {
                        showMessage(getResources().getString(R.string.timeout_error));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params = new HashMap<String, String>();
                params.put("id", id);
                params.put("scanner", scanner);
                params.put("fixture", fixture);
                params.put("store", store);
                params.put("sku", sku);
                params.put("class", classno);
                params.put("price", price);
                params.put("qty", qty);
                params.put("nik", nik);
                params.put("stamp", stamp);
                params.put("type", type);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        volleyRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(volleyRequest, CommonConstant.URL_DATA_STOCKTAKE_DP);
    }

    private void writeToFile(String filePath, String fileName, List data, String time_stamp) {
        File folder = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator + filePath
        );

        if(!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(folder, fileName + ".txt");

        try {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

            List<Stocktake> items = data;

            for (Stocktake stk : items) {
                String text = stk.getScanner() + "|" + stk.getFixture() + "|" +
                              stk.getFixture().substring(0, 2) + "|" + stk.getBc1().substring(5, 12) + "|" +
                              stk.getBc1().substring(0, 5) + "|    |" + stk.getBc2().substring(1, 12) + "|" +
                              stk.getQty() + "|" + stk.getNik() + "|" + time_stamp;

                myOutWriter.append(text);
                myOutWriter.append("\n");
            }

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
