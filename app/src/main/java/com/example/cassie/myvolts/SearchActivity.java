package com.example.cassie.myvolts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cassie.myvolts.adapter.HistoryAdapter;
import com.example.cassie.myvolts.adapter.ProductAdapter;
import com.example.cassie.myvolts.db.DbHelp;
import com.example.cassie.myvolts.dto.HistoryData;
import com.example.cassie.myvolts.util.DrawableUtil;
import com.example.cassie.myvolts.util.TestUtil;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTouch;

/**
 * Created by cassie on 23/05/2017.
 */

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.hislist)
    ListView hislist;

    HistoryAdapter hisAdapter;

    ProductAdapter productAdapter;

    ArrayAdapter<String> autoAdapter;
    @BindView(R.id.search)
    AutoCompleteTextView search;
    @BindView(R.id.search_view)
    LinearLayout searchView;
    @BindView(R.id.his_view)
    LinearLayout hisView;
    @BindView(R.id.Search_button1)
    ImageView voiceSearch;
    @BindView(R.id.search_btn_layout)
    LinearLayout search_btn_layout;
    @BindView(R.id.clear_history)
    ImageView clear;

    String made;
    String type;
    String model;

    DbHelp dbHelp;

    ImageView searchbtn;
    ImageView del;

    View v1, v2;

    private static final String TAG = "SearchActivity";

    private static final int REQUEST_CODE = 1234;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static String sDefSystemLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        initBtnView();

        sharedPreferences = getSharedPreferences("base64", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        dbHelp = new DbHelp(this);

        hisAdapter = new HistoryAdapter(this);
        hislist.setAdapter(hisAdapter);

        hisAdapter.setDatas(dbHelp.getHisData());

        autoAdapter = new ArrayAdapter<>(this,
                  R.layout.item_data, R.id.item_name, dbHelp.getAutoCompleteOptions());

        search.setAdapter(autoAdapter);

        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TestUtil.storeSearchClicks(sharedPreferences, editor, "searchItemClick");
                String data = autoAdapter.getItem(position);
                String pid = dbHelp.getProductIdByName(data);
                dbHelp.saveHis(search.getText().toString(), "0", "");
                hisAdapter.addDatas(new HistoryData(search.getText().toString(), "0", ""));
                hisAdapter.notifyDataSetChanged();
                ForwardToSannerActivity(pid);
            }
        });


        productAdapter = new ProductAdapter(this);
        //datalist.setAdapter(productAdapter);

        made = this.getIntent().getStringExtra("made");
        type = this.getIntent().getStringExtra("type");
        model = this.getIntent().getStringExtra("model");
        if (!TextUtils.isEmpty(made) || !TextUtils.isEmpty(type) || !TextUtils.isEmpty(model)) {
            search(null);
        }

        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                searchByInput();
            }
        });

        sDefSystemLang = Locale.getDefault().getLanguage();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        sDefSystemLang = newConfig.locale.getLanguage();
    }

    private void ForwardToSannerActivity(String pid) {
        Intent intent = new Intent(SearchActivity.this,ScannerActivity.class);
        intent.putExtra("product", pid);
        startActivity(intent);
    }

    public void initBtnView(){
        v1 = getLayoutInflater().inflate(R.layout.layout_search_btn,search_btn_layout,false);
        v2 = getLayoutInflater().inflate(R.layout.layout_delete_btn,search_btn_layout,false);
        searchbtn = (ImageView) v1.findViewById(R.id.search_but);
        searchbtn.setOnClickListener(this);
        del = (ImageView) v2.findViewById(R.id.del_but);
        del.setOnClickListener(this);
        clear.setOnClickListener(this);

        search_btn_layout.addView(v1);

        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    searchByInput();
                }
                return false;
            }
        });

    }

    @OnClick({R.id.hot_text1,R.id.hot_text2,R.id.hot_text3,R.id.hot_text4,R.id.hot_text5,R.id.hot_text6,
    R.id.hot_text7,R.id.hot_text8,R.id.hot_text9,R.id.hot_text10})
    public void initHotViewOnclick(View v){
        TextView text = (TextView) v;
        search.setText(text.getText());
        searchByInput();
    }

    @OnItemClick(R.id.hislist)
    public void onHisItemClick(int pos) {
        TestUtil.storeSearchClicks(sharedPreferences, editor, "hisClick");
        HistoryData hisitem = hisAdapter.getItem(pos);

        if(hisitem.getIsWholeProduct().equals("0")) {
            search.setText(hisitem.name);
            search.dismissDropDown();
            showAlertDialog(hisitem.name);
//            search(hisitem.name);
        }else if(hisitem.getIsWholeProduct().equals("1")){
            ForwardToSannerActivity(hisitem.getProductId()

            );
        }
    }


    @OnTouch(R.id.search)
    public boolean textOnTouch(View v, MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                View hiddenView = findViewById(R.id.search_but);
                if (null != hiddenView) {

                    ViewGroup parent = (ViewGroup) hiddenView.getParent();

                    parent.removeView(hiddenView);

                    search_btn_layout.addView(v2);
                    break;
                }
        }
        voiceSearch.setImageResource(R.drawable.ic_search);
        return false;

    }

    @OnClick(R.id.Search_button1)
    public void VoiceClick(){
        if (DrawableUtil.areDrawablesIdentical(voiceSearch.getDrawable(), getResources().getDrawable(R.drawable.ic_search))) {
            TestUtil.storeSearchClicks(sharedPreferences, editor, "voiceClick");
            searchByInput();
        }else{

            if (isConnected()) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(intent, REQUEST_CODE);
            } else {
                Toast.makeText(getApplicationContext(), "Please Connect to Internet", Toast.LENGTH_LONG).show();
            }
        }

    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net!=null && net.isAvailable() && net.isConnected()) {
        return true;
        } else {
        return false;
        }
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            if (matches.size() > 0) {
                search.setText(matches.get(0));
            } else {
                Toast.makeText(SearchActivity.this, "Test failed", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

         }

    private void search(String searchStr) {
        Intent intent = new Intent(SearchActivity.this,
                TabActivity.class);
        intent.putExtra("search",searchStr.trim());
        intent.putExtra("made",searchStr.split(",")[0].trim());
        intent.putExtra("type",searchStr.split(",")[1].trim());
        intent.putExtra("model",searchStr.split(",")[2].trim());
        startActivity(intent);
    }

    private void searchSimilar(String searchStr) {
        Intent intent = new Intent(SearchActivity.this,
                TabActivity.class);
        intent.putExtra("search",searchStr.trim());
        intent.putExtra("made", searchStr.trim());
        intent.putExtra("type",searchStr.trim());
        intent.putExtra("model",searchStr.trim());
        startActivity(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        search.setDropDownWidth(searchView.getWidth());
    }

    @OnClick(R.id.back_but)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_but:
                TestUtil.storeSearchClicks(sharedPreferences, editor, "searchBtnClick");
                searchByInput();
                break;

            case R.id.del_but:
                View hiddenView = findViewById ( R.id.del_but) ;

                if ( null != hiddenView ) {
                    ViewGroup parent = ( ViewGroup ) hiddenView.getParent();
                    parent.removeView ( hiddenView );
                    search_btn_layout.addView(v1);
                }
                voiceSearch.setImageResource(R.drawable.icon_mic);

                search.setText("");
                break;
            case R.id.clear_history:

                clearHistory();
                break;
        }

    }

    private void clearHistory(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SearchActivity.this);
        builder.setMessage("Clear all search record？");
        builder.setTitle("Confirm");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
           public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            dbHelp = new DbHelp(SearchActivity.this);
            dbHelp.clearHis();
            hisAdapter.setDatas(dbHelp.getHisData());
            hisAdapter.notifyDataSetChanged();
            Toast.makeText(SearchActivity.this, "All history record has been cleared!", Toast.LENGTH_SHORT)
                    .show();

            }
          });
          builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
           }
          });
          builder.create().show();
    }

    private void searchByInput() {
        String searchInput = search.getText().toString();
        if (!TextUtils.isEmpty(searchInput.trim())) {
            if (searchInput.indexOf(",") == -1) {
                Log.v(TAG, "without comma");
                searchSimilar(searchInput);
            } else {
                Log.v(TAG, "has comma");
                dbHelp.saveHis(search.getText().toString(), "0", "");
                hisAdapter.addDatas(new HistoryData(search.getText().toString(), "0", ""));
                hisAdapter.notifyDataSetChanged();
                showAlertDialog(searchInput);
            }
            search.clearFocus();
        } else {
            Toast.makeText(this, "please fill the search box...", Toast.LENGTH_SHORT).show();
        }
    }

    private EditText editText;
    private void showAlertDialog(final String searchStr) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_confirm, null);
        dialog.setView(dialogView);

        TextView brandTextView = (TextView)dialogView.findViewById(R.id.brand_name);
        brandTextView.setText(searchStr.split(",")[0].trim());

        TextView deviceTextView = (TextView)dialogView.findViewById(R.id.device_name);
        deviceTextView.setText(searchStr.split(",")[1].trim());

        TextView modelTextView = (TextView)dialogView.findViewById(R.id.device_model);
        modelTextView.setText(searchStr.split(",")[2].trim());

        dialog.setPositiveButton("Search for cables", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            search(searchStr);
            editText = (EditText) findViewById(R.id.search);
            editText.setSelection(editText.getText().length());
            editText.requestFocus();
            }
        }) ;
        dialog.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            editText = (EditText) findViewById(R.id.search);
            editText.setSelection(editText.getText().length());
            editText.requestFocus();
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

}

