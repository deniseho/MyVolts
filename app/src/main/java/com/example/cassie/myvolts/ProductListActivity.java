package com.example.cassie.myvolts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cassie.myvolts.adapter.DeviceListAdapter;
import com.example.cassie.myvolts.adapter.ProductListAdapter;
import com.example.cassie.myvolts.db.DbHelp;
import com.example.cassie.myvolts.dto.DeviceData;
import com.example.cassie.myvolts.dto.ProductData;
import com.example.cassie.myvolts.testing.TaskResultActivity;
import com.example.cassie.myvolts.util.DensityUtils;
import com.example.cassie.myvolts.util.NetworkUtil;
import com.example.cassie.myvolts.util.ResourceReader;
import com.example.cassie.myvolts.util.TestUtil;
import com.github.clans.fab.FloatingActionButton;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;

//import com.example.cassie.myvolts.adapter.AsinListAdapter;

public class ProductListActivity extends ActionBarActivity implements AbsListView.OnScrollListener {

    ProductListAdapter adapter;
//    AsinListAdapter asinListAdapter;
    DeviceListAdapter deviceListAdapter;
    ProductData pdata = null;
    List<ProductData> products = new ArrayList<>();
    List<DeviceData> pis = new ArrayList<>();
    List<DeviceData> devices = new ArrayList<>();
    JaroWinkler algorithm = new JaroWinkler();

    private int curPage = 0;

    private int visibleLastIndex = 0;
    private int visibleItemCount;
    private View loadMoreView, loadMoreView2, loadMoreView3;
    private TextView loadmorebutton;

    private View no_internet, no_internet2, no_internet3;
    private View no_result, no_result2, no_result3;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @BindView(R.id.layout)
    RelativeLayout layout;

//    @BindView(R.id.layout2)
//    RelativeLayout layout2;

    @BindView(R.id.layout3)
    RelativeLayout layout3;

    @BindView(R.id.productlist)
    ListView listview;

//    @BindView(R.id.asintlist)
//    ListView asinlist;

    @BindView(R.id.devicelist)
    ListView devicelist;

    //@BindView(R.id.back)
    //ImageView back;

    //@BindView(R.id.search)
    //ImageView searchIcon;

    /*@BindView(R.id.dd)
    LinearLayout dd;*/

    @BindView(R.id.radio_search)
    RadioGroup radioSearch;
    @BindView(R.id.product)
    RadioButton product;
    @BindView(R.id.device)
    RadioButton device;
//    @BindView(R.id.asin)
//    RadioButton asin;

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    SearchBox search;

    String searchStr,made,type,model;

    private static final int REQUEST_CODE = 1234;

    private Toolbar toolbar;

    private DbHelp dbHelp;

    int count = 0;
    private int mPreviousVisibleItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("base64", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        dbHelp = new DbHelp(this);

        // search = (SearchBox) findViewById(R.id.searchbox);
        search.enableVoiceRecognition(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                openSearch();
                return true;
            }
        });

        searchStr = this.getIntent().getStringExtra("search");
        if(null != searchStr && !"".equals(searchStr)) {
            search.setSearchString(searchStr);
        }

        made = this.getIntent().getStringExtra("made");
        type = this.getIntent().getStringExtra("type");
        model = this.getIntent().getStringExtra("model");

        initInternetStatusPage(searchStr);

        setTabSelected(product);

        setFab();

        //testTask2();

        sampleCustomView();
    }


    public void setFab(){
        mFab.hide(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFab.show(true);
                mFab.setShowAnimation(AnimationUtils.loadAnimation(ProductListActivity.this, R.anim.show_from_bottom));
                mFab.setHideAnimation(AnimationUtils.loadAnimation(ProductListActivity.this, R.anim.hide_to_bottom));
            }
        }, 300);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(product.isChecked()){
                    listview.setSelection(0);
                }else if(device.isChecked()){
                    devicelist.setSelection(0);
                }
            }
        });

    }

    public void testTask2() {
        System.out.println(sharedPreferences.getString("testmode", ""));
        System.out.println(sharedPreferences.getInt("task", 0));
        if (null != sharedPreferences) {
            String testmode = sharedPreferences.getString("testmode", "");
            int taskId = sharedPreferences.getInt("task", 0);
            if (null != testmode && "yes".equals(testmode)) {
                if (2 == taskId) {
                    isDisplay();
                }
            }
        }
    }


    private void isDisplay(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ProductListActivity.this);
        builder.setMessage("Task Finished");
        builder.setTitle("Display Test Result?");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(ProductListActivity.this, TaskResultActivity.class);
                startActivity(intent);
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


    private void initInternetStatusPage(String searchStr) {
        boolean internetStatus = NetworkUtil.isNetworkAvailable(ProductListActivity.this);

        if(internetStatus == false){
            doNoInternet();

        }else {
//            doWithInternet(searchStr);
        }
    }

//    private void doWithInternet(String searchStr) {
//        getdp db = new getdp();
//
//        if (searchStr != null && !searchStr.equals("")) {
//            try {
//                search.setSearchString(searchStr);
//                db.execute(RegexUtil.spliteRegex(searchStr));
//            }catch(Exception ex){
//
//            }
//        } else {
//            if(made != null && type != null && model != null)
//                try {
//                    checkInternetStatusForSelectBox();
//                    radioSearch.setVisibility(View.GONE);
//                    db.execute(URLEncoder.encode(made), URLEncoder.encode(type, "UTF-8"), URLEncoder.encode(model, "UTF-8"));
//                }catch(Exception ex){
//
//                }
//        }
//
//        listview.setOnScrollListener(this);
//    }

    private void checkInternetStatusForSelectBox() {
        if(NetworkUtil.isNetworkAvailable(ProductListActivity.this) == false){
            final View listview = findViewById(R.id.productlist);
            if (null != listview) {
                if (null == no_internet) {
                    no_internet = getLayoutInflater().inflate(R.layout.no_internet, layout, false);
                    layout.addView(no_internet);
                }
                listview.setVisibility(View.GONE);
                no_internet.setVisibility(View.VISIBLE);
                no_internet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        startActivity(getIntent());
                    }
                });
            }
        }
    }


    private void doNoInternet() {
        final View listview = findViewById(R.id.productlist);

        if (null != listview) {

            no_internet = getLayoutInflater().inflate(R.layout.no_internet,layout,false);

            listview.setVisibility(View.GONE);

            layout.addView(no_internet);

            no_internet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listview.setVisibility(View.VISIBLE);
                    initInternetStatusPage(searchStr);

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ProductListActivity.this, MainActivity.class);
                if(made != null && type != null && model != null) {
                    intent.putExtra("madeInput", made);
                    intent.putExtra("deviceInput", type);
                    intent.putExtra("modelInput", model);
                }
                startActivity(intent);
                return true;
            case R.id.search:

            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @OnClick({R.id.product, R.id.asin, R.id.device})
    @OnClick({R.id.product, R.id.device})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.product:
                setTabSelected(product);
//                invisibleAsinlistDevieList();
                searchByInput(searchStr);
                break;
            case R.id.device:
                setTabSelected(device);
                searchByDevices();
                break;
//            case R.id.asin:
//                setTabSelected(asin);
//                searchByAsin();
//                break;
        }
    }


    @Override
    protected void onStart() {
        System.out.println("on start");
        super.onStart();
    }

//    private void searchByAsin() {
//        invisibleListviewDeviceList();
//        resetScrollVariables();
//        SearchByAsin searchByAsin = new SearchByAsin();
//        try {
//            searchByAsin.execute(URLEncoder.encode(searchStr, "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }

    private void searchByDevices(){
//        invisibleAsinProList();
        resetScrollVariables();
//        getDeviceByName getDeviceByName = new getDeviceByName();

//        getDeviceByName.execute(RegexUtil.spliteRegex(searchStr));

    }

    public int getSelectedRadioId(){
        return radioSearch.getCheckedRadioButtonId();

    }

    public void openSearch(){
       // setTabSelected(product);
        radioSearch.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
//        layout2.setVisibility(View.GONE);
        layout3.setVisibility(View.GONE);
        search.revealFromMenuItem(R.id.search, this);

        List<String> list=
                dbHelp.getAllProductName();
        for(int i = 0; i < list.size(); i++){
            SearchResult option = new SearchResult(list.get(i), getResources().getDrawable(R.drawable.ic_search));
            search.addSearchable(option);
        }
        //search.setLogoText(searchStr);
        search.setMenuListener(new SearchBox.MenuListener(){

            @Override
            public void onMenuClick() {
                //Hamburger has been clicked
                //Toast.makeText(ProductListActivity.this, "Menu click", Toast.LENGTH_LONG).show();
            }

        });
        search.setSearchListener(new SearchBox.SearchListener(){

            @Override
            public void onSearchOpened() {
                //Use this to tint the screen
            }

            @Override
            public void onSearchClosed() {
                //Use this to un-tint the screen
                search.setSearchString(searchStr);
                radioSearch.setVisibility(View.VISIBLE);
                search.hideCircularly(ProductListActivity.this);
            }

            @Override
            public void onSearchTermChanged(String s) {

            }

            @Override
            public void onSearch(String searchTerm) {
                if(!searchTerm.equals("")) {
                    searchStr = searchTerm;
                    onSearchBarFinished();
                    search.setSearchString(searchStr);
                    radioSearch.setVisibility(View.VISIBLE);
                    search.hideCircularly(ProductListActivity.this);
                }else{
                    Toast.makeText(ProductListActivity.this, "Please fill the search box!", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onResultClick(SearchResult result){
                //React to a result being clicked
                closeSearch();

                String pid = dbHelp.getProductIdByName(result.toString());
                dbHelp.saveHis(result.toString(), "1", pid);
                forwardToSannerActivity(pid);
            }


            @Override
            public void onSearchCleared() {
            }

        });
    }

    public void onSearchBarFinished(){
        int id = getSelectedRadioId();
        if(id == product.getId()){
//            invisibleAsinlistDevieList();
            searchByInput(searchStr);
        }else if(id == device.getId()){
            searchByDevices();
        }
//        else if(id == asin.getId()){
//            searchByAsin();
//        }
    }

    protected void closeSearch() {
        radioSearch.setVisibility(View.VISIBLE);
        product.setChecked(true);
        search.hideCircularly(this);
        layout.setVisibility(View.VISIBLE);
//        layout2.setVisibility(View.GONE);
        layout3.setVisibility(View.GONE);
    }

    private void searchByInput(String st) {
        if (!TextUtils.isEmpty(st.trim())) {
            dbHelp.saveHis(st, "0", "");
            resetSearchVariables();
            searchStr = st;
            initInternetStatusPage(st);
        } else {
            Toast.makeText(this, "please fill the search box...", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetSearchVariables() {
        count = 0;
        curPage = 0;
        adapter = null;
        deviceListAdapter = null;
//        asinListAdapter = null;
        products.clear();
        devices.clear();
        pis.clear();

        resetNoResultsPage();
        resetNoInternetPage();
    }

    private void resetNoResultsPage() {
        if(no_result != null){
            if(((ViewGroup)no_result.getParent()).getId() == layout.getId()){
                layout.removeView(no_result);
                listview.setVisibility(View.VISIBLE);
                no_result = null;
            }
        }
//        if(no_result2 != null){
//            if(((ViewGroup)no_result2.getParent()).getId() == layout2.getId()){
//                layout2.removeView(no_result2);
//                asinlist.setVisibility(View.VISIBLE);
//                no_result2 = null;
//            }
//        }

        if(no_result3 != null){
            if(((ViewGroup)no_result3.getParent()).getId() == layout3.getId()){
                layout3.removeView(no_result3);
                devicelist.setVisibility(View.VISIBLE);
                no_result3 = null;
            }
        }
    }

    private void resetNoInternetPage() {
        if(no_internet != null){
            if(((ViewGroup)no_internet.getParent()).getId() == layout.getId()){
                layout.removeView(no_internet);
                listview.setVisibility(View.VISIBLE);
                no_result = null;
            }
        }
//        if(no_internet2 != null){
//            if(((ViewGroup)no_internet2.getParent()).getId() == layout2.getId()){
//                layout2.removeView(no_internet2);
//                asinlist.setVisibility(View.VISIBLE);
//                no_internet2 = null;
//            }
//        }

        if(no_internet3 != null){
            if(((ViewGroup)no_internet3.getParent()).getId() == layout3.getId()){
                layout3.removeView(no_internet3);
                devicelist.setVisibility(View.VISIBLE);
                no_internet3 = null;
            }
        }
    }

    private void forwardToSannerActivity(String pid) {
        Intent intent = new Intent(ProductListActivity.this,ScannerActivity.class);
        intent.putExtra("productId", pid);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            if (matches.size() > 0) {
                search.populateEditText(matches.get(0));
            } else {
                Toast.makeText(ProductListActivity.this, "Test failed", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }


    @OnItemClick(R.id.productlist)
    public void onProductListItemClick(int pos) {
        TestUtil.storeSearchClicks(sharedPreferences, editor, "resultsListClick");
        if(products.size() > 0) {
            ProductData productData = products.get(pos);
            ForwardPdataToScannerActivity(productData);
        }
    }

//    @OnItemClick(R.id.asintlist)
//    public void onAsinListItemClick(int pos) {
//        TestUtil.storeSearchClicks(sharedPreferences, editor, "resultsListClick");
//        if(pis.size() > 0) {
//            DeviceData device = pis.get(pos);
//            GetProductByAsin getProductByAsin = new GetProductByAsin();
//            getProductByAsin.execute(device.getAsin());
//        }
//    }

    @OnItemClick(R.id.devicelist)
    public void onDeviceListItemClick(int pos) {
        TestUtil.storeSearchClicks(sharedPreferences, editor, "resultsListClick");
        if(devices.size() > 0) {
            DeviceData device = devices.get(pos);
//            GetProductByAsin getProductByAsin = new GetProductByAsin();
//            getProductByAsin.execute(device.getAsin());
        }
    }

    public void invisibleListviewDeviceList(){
        layout.setVisibility(View.GONE);
        layout3.setVisibility(View.GONE);
//        layout2.setVisibility(View.VISIBLE);
//        if(asinlist.getVisibility() == View.GONE){
//            asinlist.setVisibility(View.VISIBLE);
//        }
        if(no_internet2 != null && no_internet2.getVisibility() == View.VISIBLE){
            no_internet2.setVisibility(View.GONE);
        }

        if(no_result2 != null && no_result2.getVisibility() == View.VISIBLE){
            no_result2.setVisibility(View.GONE);
        }

//        clearOnScrollListnerOnAsinList();
//        asinlist.setOnScrollListener(this);
    }

//    public void invisibleAsinlistDevieList(){
//        layout.setVisibility(View.VISIBLE);
//        layout2.setVisibility(View.GONE);
//        layout3.setVisibility(View.GONE);
//        if(listview.getVisibility() == View.GONE){
//            listview.setVisibility(View.VISIBLE);
//        }
//
//        if(no_internet != null && no_internet.getVisibility() == View.VISIBLE){
//            no_internet.setVisibility(View.GONE);
//        }
//
//        if(no_result != null && no_result.getVisibility() == View.VISIBLE){
//            no_result.setVisibility(View.GONE);
//        }
//        clearOnScrollListnerOnProdList();
//        listview.setOnScrollListener(this);
//    }

//    public void invisibleAsinProList(){
//        layout3.setVisibility(View.VISIBLE);
//        layout.setVisibility(View.GONE);
//        layout2.setVisibility(View.GONE);
//        if(devicelist.getVisibility() == View.GONE){
//            devicelist.setVisibility(View.VISIBLE);
//        }
//
//        if(no_internet3 != null && no_internet3.getVisibility() == View.VISIBLE){
//            no_internet3.setVisibility(View.GONE);
//        }
//        if(no_result3 != null && no_result3.getVisibility() == View.VISIBLE){
//            no_result3.setVisibility(View.GONE);
//        }
//        clearOnScrollListnerOnDeviceList();
//        devicelist.setOnScrollListener(this);
//    }



    private void ForwardPdataToScannerActivity(ProductData product) {
        Bundle b = new Bundle();
        Intent intent = new Intent(ProductListActivity.this, ScannerActivity.class);
        b.putSerializable("product", product);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void clearOnScrollListnerOnProdList(){
        adapter = null;
//        asinListAdapter = null;
        deviceListAdapter = null;
        devices.clear();
        pis.clear();
        products.clear();
        resetScrollVariables();
//        asinlist.setOnScrollListener(null);
        devicelist.setOnScrollListener(null);
    }

//    public void clearOnScrollListnerOnAsinList(){
//        adapter = null;
//        asinListAdapter = null;
//        deviceListAdapter = null;
//        products.clear();
//        devices.clear();
//        pis.clear();
//        resetScrollVariables();
//        listview.setOnScrollListener(null);
//        devicelist.setOnScrollListener(null);
//    }

    public void clearOnScrollListnerOnDeviceList(){
        adapter = null;
//        asinListAdapter = null;
        deviceListAdapter = null;
        products.clear();
        pis.clear();
        devices.clear();
        resetScrollVariables();
        listview.setOnScrollListener(null);
//        asinlist.setOnScrollListener(null);
    }

    private void resetScrollVariables() {
        count = 0;
        curPage = 0;
        visibleItemCount = 0;
        visibleLastIndex = 0;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.i("LOADMORE", "loading...");

//        if(asin.isChecked()){
//            if(asinListAdapter.getCount() % 10 == 0) {
//                int itemsLastIndex = asinListAdapter.getCount() - 1;
//                int lastIndex = itemsLastIndex + 1;
//                doOnScrollAsinList(scrollState, lastIndex);
//            }else{
//                Toast.makeText(ProductListActivity.this, "No more results", Toast.LENGTH_SHORT).show();
//            }
//        }else
        if(device.isChecked()){
            if(deviceListAdapter.getCount() % 10 == 0) {
                int itemsLastIndex = deviceListAdapter.getCount() - 1;
                int lastIndex = itemsLastIndex + 1;
                doOnScrollDeviceList(scrollState, lastIndex);
            }else{
                Toast.makeText(ProductListActivity.this, "No more results", Toast.LENGTH_SHORT).show();
            }
        }else{
            if(adapter.getCount() % 10 == 0) {
                int itemsLastIndex = adapter.getCount() - 1;
                int lastIndex = itemsLastIndex + 1;
//                doOnScrollExtra(scrollState, lastIndex);
            }else{
                Toast.makeText(ProductListActivity.this, "No more results", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    private void doOnScrollAsinList(int scrollState, int lastIndex) {
//
//        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
//            SearchByAsin searchByAsin = new SearchByAsin();
//            searchByAsin.execute(searchStr);
//            asinListAdapter.notifyDataSetChanged();
//            asinlist.setSelection(visibleLastIndex - visibleItemCount + 1);
//        }
//    }

    private void doOnScrollDeviceList(int scrollState, int lastIndex) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
//            getDeviceByName searchByDevice = new getDeviceByName();
//            searchByDevice.execute(RegexUtil.spliteRegex(searchStr));
            deviceListAdapter.notifyDataSetChanged();
            devicelist.setSelection(visibleLastIndex - visibleItemCount + 1);
        }
    }

//    private void doOnScrollExtra(int scrollState, int lastIndex) {
//        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
//            if (searchStr != null && !searchStr.equals("")) {
//                    getdp db = new getdp();
//                    db.execute(RegexUtil.spliteRegex(searchStr));
//                    adapter.notifyDataSetChanged();
//                    listview.setSelection(visibleLastIndex - visibleItemCount + 1);
//            } else {
//                getdp db = new getdp();
//                try {
//                    db.execute(URLEncoder.encode(made, "UTF-8"), URLEncoder.encode(type, "UTF-8"));
//                    adapter.notifyDataSetChanged();
//                    listview.setSelection(visibleLastIndex - visibleItemCount + 1);
//                } catch (Exception ex) {
//
//                }
//            }
//        }
//    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.visibleItemCount = visibleItemCount;
        visibleLastIndex = firstVisibleItem + visibleItemCount - 1;

        if (firstVisibleItem > mPreviousVisibleItem) {
            mFab.hide(true);
        } else if (firstVisibleItem < mPreviousVisibleItem) {
            mFab.show(true);
        }
        mPreviousVisibleItem = firstVisibleItem;
    }

//    public class getdp extends AsyncTask<String, Void, String> {
//
//        ProgressDialog p = new ProgressDialog(ProductListActivity.this,
//                ProgressDialog.STYLE_SPINNER);
//
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//            p.setMessage("Loading...");
//            p.show();
//        }
//
//        @Override
//        protected String doInBackground(String... arg0) {
//            // TODO Auto-generated method stub
//            String offset = "";
//            if(curPage > 0) {
//                offset = "%0AOFFSET%20" + curPage * 10;
//            }
//            curPage++;
//
//            String result = "";
//
//            if(searchStr != null && !searchStr.equals("")) {
//                String args = "";
//                for(int i = 1; i < arg0.length; i++){
//                    args = args + "%20%7C%7C%20%20regex(%3Fpname%2C%20%22" + arg0[i] + "%22%2C%20%22i%22)";
//                }
//                String url = "http://theme-e.adaptcentre.ie/openrdf-workbench/repositories/mv2.54/query?action=exec&queryLn=SPARQL&query=PREFIX%20%20%3A%20%3Chttp%3A%2F%2Fmyvolts.com%23%3E%0APREFIX%20owl%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0APREFIX%20rdf%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0APREFIX%20rdfs%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0ASELECT%20%20distinct%20%3Fprod_id%20%20%3Fpname%20%3Ftype%20%0AWHERE%20%0A%7B%20%0A%20%3Fprod_id%20%3Aproduct_name%20%3Fpname%20.%0A%20%3Fprod_id%20%3AisOfTypeCategory%20%3Ftype%20.%0A%0A%20filter%20(regex(%3Fpname%2C%20%22" + arg0[0] + "%22%2C%20%22i%22)" + args + ")%20.%0A%7D%0Aorder%20by%20%3Fpname%0ALIMIT%2010"+ offset +"&limit=100&infer=true&";
//                System.out.println(url);
//
//                result = HttpUtils.doGet(url);
//
//            }else{
//                String url1 = "http://theme-e.adaptcentre.ie/openrdf-workbench/repositories/mv2.53/query?action=exec&queryLn=SPARQL&query=PREFIX%20%20%3A%20%3Chttp%3A%2F%2Fmyvolts.com%23%3E%0A%0APREFIX%20owl%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0A%0APREFIX%20rdf%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0A%0APREFIX%20rdfs%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0A%0ASELECT%20distinct%20%3Fprod_id%20%3Fpname%20%20WHERE%20%7B%0A%0A%3Fpi_id%20%3AhasTechSpec%20%3Fts_id%20.%0A%0A%3Fprod_id%20%3Asupports%20%3Fts_id%20.%0A%0A%3Fts_id%20%3Avoltage%20%3Fx%20.%0A%0A%3Fts_id%20%3Aamperage%20%3Fy%20.%0A%0A%3Fts_id%20%3Atip_length%20%3Fz%20.%0A%0A%3Fpi_id%20%3AisManufacturedBy%20%3Fman%20.%0A%0A%3Fman%20%3Amanufacturer_name%20%22" + arg0[0] + "%22%20.%0A%0A%3Fpi_id%20%3AisOfDeviceCategory%20%3Fdevice%20.%0A%0A%3Fdevice%20%3ApiDevice_name%20%22" + arg0[1] + "%22%20.%0A%0A%3Fpi_id%20%3ApiModel_name%20%22" + arg0[2] + "%22%20.%0A%0A%3Fprod_id%20%3Aproduct_name%20%20%3Fpname%20.%0A%0A%3Fpi_id%20%3Api_asin%20%3Fasin%20.%0A%0AFilter(%3Fasin%20!%3D%20%22%22)%7D%0ALIMIT%2010"+ offset +"&limit=100&infer=true&";
//
//                result = HttpUtils.doGet(url1);
//
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//            p.dismiss();
//
//            System.out.println(result);
//
//            List<ProductData> newData = new ArrayList<>();
//
//            String product_id = "";
//            String dname = "";
//            String x= "";
//            String y = "";
//            String z = "";
//            Document doc = null;
//            String type = "";
//            String images = "";
//
//            if(result != null && !result.equals("")) {
//
//                try {
//                    doc = DocumentHelper.parseText(result);
//                    Element rootElt = doc.getRootElement();
//                    Iterator iter = rootElt.elementIterator("results");
//                    while (iter.hasNext()) {
//
//                        Element resultRecord = (Element) iter.next();
//                        Iterator itersElIterator = resultRecord.elementIterator("result");
//                        if (!itersElIterator.hasNext() && count == 0) {
//                            removeListViewToNoResults();
//                            break;
//                        }else if (!itersElIterator.hasNext()) {
//                            Toast.makeText(getApplicationContext(), "No more results...", Toast.LENGTH_SHORT).show();
//                        }
//                        count++;
//                        while (itersElIterator.hasNext()) {
//                            Element itemEle = (Element) itersElIterator.next();
//                            Iterator literLIterator = itemEle.elementIterator("binding");
//                            while (literLIterator.hasNext()) {
//                                Element ele = (Element) literLIterator.next();
//                                if ("prod_id".equals(ele.attributeValue("name"))) {
//                                    product_id = ele.elementTextTrim("uri");
//                                } else if ("x".equals(ele.attributeValue("name"))) {
//                                    x = ele.elementTextTrim("literal");
//                                } else if ("y".equals(ele.attributeValue("name"))) {
//                                    y = ele.elementTextTrim("literal");
//                                } else if ("z".equals(ele.attributeValue("name"))) {
//                                    z = ele.elementTextTrim("literal");
//                                } else if ("pname".equals(ele.attributeValue("name"))) {
//                                    dname = ele.elementTextTrim("literal");
//                                }else if ("type".equals(ele.attributeValue("name"))) {
//                                    type = ele.elementTextTrim("uri");
//                                }
//                            }
//                            ProductData p = new ProductData(product_id, dname, null);
//
//                            if(!"".equals(type) && !"".equals(images)){
//                                String tid = DigitUtil.getNumericTid(type);
//                                p.setType(tid);
//                            }
//                            if(searchStr != null) {
//                                p.setSimilarity(algorithm.getSimilarity(searchStr, dname));
//                            }
//                            newData.add(p);
//                        }
//                    }
//
//
//                    if(searchStr != null)
//                        Collections.sort(newData);
//
//                    if (adapter == null) {
//                        adapter = new ProductListAdapter(products, ProductListActivity.this, searchStr);
//                        listview.setAdapter(adapter);
//                        if(loadMoreView == null) {
//                            loadMoreView = getLayoutInflater().inflate(R.layout.load_more, null);
//                            loadmorebutton = (TextView) loadMoreView.findViewById(R.id.loadMoreButton);
//                            listview.addFooterView(loadmorebutton);
//                        }
//                    }
//                    products.addAll(newData);
//                    adapter.setDatas(products);
//
//                } catch (DocumentException e) {
//                    e.printStackTrace();
//                }
//            }else{
//                removeListViewToNoInternet();
//            }
//        }
//    }

//    public class getDeviceByName extends AsyncTask<String, Void, String> {
//
//        ProgressDialog p = new ProgressDialog(ProductListActivity.this,
//                ProgressDialog.STYLE_SPINNER);
//
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//            p.setMessage("Loading...");
//            p.show();
//        }
//
//        @Override
//        protected String doInBackground(String... arg0) {
//            // TODO Auto-generated method stub
//            String offset = "";
//            if(curPage > 0) {
//                offset = "%0AOFFSET%20" + curPage * 10;
//            }
//            curPage++;
//            String result = "";
//            if(searchStr != null && !searchStr.equals("")) {
//                String args = "";
//                for(int i = 1; i < arg0.length; i++){
//                    args = args + "%20%7C%7C%20%20regex(%3Fdname%2C%20%22" + arg0[i] + "%22%2C%20%22i%22)";
//                }
//
//                String url = "http://theme-e.adaptcentre.ie/openrdf-workbench/repositories/mv2.53/query?action=exec&queryLn=SPARQL&query=PREFIX%20%20%3A%20%3Chttp%3A%2F%2Fmyvolts.com%23%3E%0A%0APREFIX%20owl%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0A%0APREFIX%20rdf%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0A%0APREFIX%20rdfs%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0A%0ASELECT%20distinct%20%3Fpi_id%20%3Fmname%20%3Fdname%20%3Fmodel%20%3Fasin%20WHERE%20%7B%0A%0A%3Fpi_id%20%3AisManufacturedBy%20%3Fman%20.%0A%0A%3Fman%20%3Amanufacturer_name%20%3Fmname%20.%0A%0A%3Fpi_id%20%3Api_name%20%3Fdname%20.%0A%0A%3Fpi_id%20%3ApiModel_name%20%3Fmodel%20.%0A%0A%3Fpi_id%20%3Api_asin%20%3Fasin%20.%0A%0AFILTER%20(regex(%3Fdname%2C%20%22" + arg0[0] + "%22%2C%20%22i%22)" + args + ")%20.%0AFilter(%3Fasin%20!%3D%20%22%22)%0A%7D%0AOrder%20By%20%3Fdname%0ALIMIT%2010" + offset +"&limit=100&infer=true&";
//                System.out.println(url);
//
//                result = HttpUtils.doGet(url);
//
//            }
//
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//            p.dismiss();
//
//            System.out.println(result);
//
//            List<DeviceData> newData = new ArrayList<>();
//
//            String pi_id = "";
//            String dname = "";
////            String asin= "";
//            String model = "";
//            String mname = "";
//            Document doc = null;
//
//            if(result != null && !result.equals("")) {
//
//                try {
//                    doc = DocumentHelper.parseText(result);
//                    Element rootElt = doc.getRootElement();
//                    Iterator iter = rootElt.elementIterator("results");
//                    while (iter.hasNext()) {
//
//                        Element resultRecord = (Element) iter.next();
//                        Iterator itersElIterator = resultRecord.elementIterator("result");
//                        if (!itersElIterator.hasNext() && count == 0) {
//                            removDeviceListViewToNoResults();
//                            break;
//                        } else if (!itersElIterator.hasNext()) {
//                            Toast.makeText(getApplicationContext(), "No more results...", Toast.LENGTH_SHORT).show();
//                        }
//                        count++;
//                        while (itersElIterator.hasNext()) {
//                            Element itemEle = (Element) itersElIterator.next();
//                            Iterator literLIterator = itemEle.elementIterator("binding");
//                            while (literLIterator.hasNext()) {
//                                Element ele = (Element) literLIterator.next();
//                                if ("pi_id".equals(ele.attributeValue("name"))) {
//                                    pi_id = ele.elementTextTrim("uri");
//                                } else if ("model".equals(ele.attributeValue("name"))) {
//                                    model = ele.elementTextTrim("literal");
////                                } else if ("asin".equals(ele.attributeValue("name"))) {
////                                    asin = ele.elementTextTrim("literal");
//                                } else if ("mname".equals(ele.attributeValue("name"))) {
//                                    mname = ele.elementTextTrim("literal");
//                                } else if ("dname".equals(ele.attributeValue("name"))) {
//                                    dname = ele.elementTextTrim("literal");
//                                }
//                            }
//
//                            DeviceData data = new DeviceData(pi_id, dname, mname, null, model);
//                            newData.add(data);
//                        }
//                    }
//
//                    if(searchStr != null)
//                        Collections.sort(newData);
//
//                    if (deviceListAdapter == null) {
//                        deviceListAdapter = new DeviceListAdapter(devices, ProductListActivity.this, searchStr);
//                        devicelist.setAdapter(deviceListAdapter);
//                        if(loadMoreView3 == null) {
//                            loadMoreView3 = getLayoutInflater().inflate(R.layout.load_more, null);
//                            loadmorebutton = (TextView) loadMoreView3.findViewById(R.id.loadMoreButton);
//                            devicelist.addFooterView(loadmorebutton);
//                        }
//                    }
//
//                    devices.addAll(newData);
//                    deviceListAdapter.setDatas(devices);
//
//                } catch (DocumentException e) {
//                    e.printStackTrace();
//                }
//            }else{
//                removDeviceListViewToNoResults();
//            }
//        }
//    }

//    public class SearchByAsin extends AsyncTask<String, Void, String> {
//
//        ProgressDialog p = new ProgressDialog(ProductListActivity.this,
//                ProgressDialog.STYLE_SPINNER);
//
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//            p.setMessage("Loading...");
//            p.show();
//        }
//
//        @Override
//        protected String doInBackground(String... arg0) {
//            // TODO Auto-generated method stub
//            String offset = "";
//            if(curPage > 0) {
//                offset = "%0AOFFSET%20" + curPage * 10;
//            }
//            curPage++;
//
//            String url = "http://theme-e.adaptcentre.ie/openrdf-workbench/repositories/29.03mv2.4/query?action=exec&queryLn=SPARQL&query=PREFIX%20%20%3A%20%3Chttp%3A%2F%2Fmyvolts.com%23%3E%0APREFIX%20owl%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0APREFIX%20rdf%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0APREFIX%20rdfs%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0A%0ASELECT%20%20Distinct%20%3Fpi_id%20%3Fpiname%20%3Fx%20%3Fy%20%3Fz%20%3Fbrand%20%3Fasin%20%0AWHERE%0A%7B%20%0A%20%3Fpi_id%20%3AHasTechSpec%20%3Fts_id%20.%0A%20%3Fprod_id%20%3ASupports%20%3Fts_id%20.%0A%20%3Fts_id%20%3AVoltage%20%3Fx%20.%0A%20%3Fts_id%20%3AAmperage%20%3Fy%20.%0A%20%3Fts_id%20%3ATip_length%20%3Fz%20.%0A%20%3Fpi_id%20%3APi_name%20%3Fpiname%20.%0A%20%3Fpi_id%20%3AIsManufacturedBy%20%3Fman%20.%0A%20%3Fman%20%3AManufacturer_name%20%3Fbrand%20.%0A%20%3Fpi_id%20%3APi_asin%20%3Fasin%20.%0A%0A%20%20FILTER%20regex(%3Fasin%2C%20%22" + arg0[0]+ "%22%2C%20%22i%22)%20.%0A%7D%0AORDER%20BY%20%3Fpiname%0ALIMIT%2010"+ offset +"&limit=100&infer=true&";
//            String result = HttpUtils.doGet(url);
//
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//            p.dismiss();
//
//            System.out.println(result);
//
//            List<DeviceData> newData = new ArrayList<>();
//            String piname = "";
//            String asin = "";
//            String brand = "";
//            String x= "";
//            String y = "";
//            String z = "";
//            String id = "";
//            Document doc = null;
//
//            if(result != null && !result.equals("")) {
//
//                try {
//                    doc = DocumentHelper.parseText(result);
//                    Element rootElt = doc.getRootElement();
//                    Iterator iter = rootElt.elementIterator("results");
//                    while (iter.hasNext()) {
//
//                        Element resultRecord = (Element) iter.next();
//                        Iterator itersElIterator = resultRecord.elementIterator("result");
//
//                        if (!itersElIterator.hasNext() && count == 0) {
//                            removeAsinListViewToNoResults();
//                            break;
//                        }if (!itersElIterator.hasNext()) {
//                            Toast.makeText(getApplicationContext(), "No more results...", Toast.LENGTH_SHORT).show();
//                        }
//                        count++;
//
//                        while (itersElIterator.hasNext()) {
//                            Element itemEle = (Element) itersElIterator.next();
//                            Iterator literLIterator = itemEle.elementIterator("binding");
//                            while (literLIterator.hasNext()) {
//                                Element ele = (Element) literLIterator.next();
//                                if ("x".equals(ele.attributeValue("name"))) {
//                                    x = ele.elementTextTrim("literal");
//                                } else if ("y".equals(ele.attributeValue("name"))) {
//                                    y = ele.elementTextTrim("literal");
//                                } else if ("z".equals(ele.attributeValue("name"))) {
//                                    z = ele.elementTextTrim("literal");
//                                } else if ("asin".equals(ele.attributeValue("name"))) {
//                                    asin = ele.elementTextTrim("literal");
//                                } else if ("piname".equals(ele.attributeValue("name"))) {
//                                    piname = ele.elementTextTrim("literal");
//                                }else if ("brand".equals(ele.attributeValue("name"))) {
//                                    brand = ele.elementTextTrim("literal");
//                                }else if ("pi_id".equals(ele.attributeValue("name"))) {
//                                    id = ele.elementTextTrim("uri");
//                                }
//
//                            }
//                            TechSpec tech = new TechSpec(x, y, z);
//                            DeviceData device = new DeviceData(piname, brand, asin, tech);
//                            device.setPi_id(id);
//                            if (!pis.contains(device)) {
//                                newData.add(device);
//                            }
//                        }
//                    }
//
//                    if (asinListAdapter == null) {
//                        asinListAdapter = new AsinListAdapter(pis, ProductListActivity.this, searchStr);
//                        asinlist.setAdapter(asinListAdapter);
//                        if(loadMoreView2 == null) {
//                            loadMoreView2 = getLayoutInflater().inflate(R.layout.load_more, null);
//                            loadmorebutton = (TextView) loadMoreView2.findViewById(R.id.loadMoreButton);
//                            asinlist.addFooterView(loadmorebutton);
//                        }
//                    }
//
//                    pis.addAll(newData);
//                    asinListAdapter.setDatas(pis);
//
//                } catch (DocumentException e) {
//                    e.printStackTrace();
//                }
//            }else {
//                removeAsinListToNoInternet();
//            }
//        }
//    }
//
//    public class GetProductByAsin extends AsyncTask<String, Void, String> {
//
//        ProgressDialog p = new ProgressDialog(ProductListActivity.this,
//                ProgressDialog.STYLE_SPINNER);
//
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//            p.setMessage("Loading...");
//            p.show();
//        }
//
//        @Override
//        protected String doInBackground(String... arg0) {
//            // TODO Auto-generated method stub
//            String url = "http://theme-e.adaptcentre.ie/openrdf-workbench/repositories/29.03mv2.4/query?action=exec&queryLn=SPARQL&query=PREFIX%20%20%3A%20%3Chttp%3A%2F%2Fmyvolts.com%23%3E%0APREFIX%20owl%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0APREFIX%20rdf%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0APREFIX%20rdfs%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0A%0ASELECT%20%20Distinct%20%3Fprod_id%20%3Fpname%20%3Fx%20%3Fy%20%3Fz%0AWHERE%0A%7B%20%0A%20%3Fpi_id%20%3AHasTechSpec%20%3Fts_id%20.%0A%20%3Fprod_id%20%3ASupports%20%3Fts_id%20.%0A%20%3Fts_id%20%3AVoltage%20%3Fx%20.%0A%20%3Fts_id%20%3AAmperage%20%3Fy%20.%0A%20%3Fts_id%20%3ATip_length%20%3Fz%20.%0A%20%3Fprod_id%20%3AProduct_name%20%3Fpname%20.%0A%20%3Fpi_id%20%3APi_asin%20%22" + arg0[0] + "%22%20.%0A%7D%0AORDER%20BY%20%3Fpname&limit=100&infer=true&";
//            System.out.println(url);
//            String result = "";
//            try {
//                result = HttpUtils.doGet(url);
//            }catch(RuntimeException rt){
//
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//            p.dismiss();
//
//            System.out.println(result);
//
//
//            Document doc = null;
//            String product_id = "";
//
//            try {
//                doc = DocumentHelper.parseText(result);
//                Element rootElt = doc.getRootElement();
//                Iterator iter = rootElt.elementIterator("results");
//
//                while (iter.hasNext()) {
//                    Element resultRecord = (Element) iter.next();
//                    Iterator itersElIterator = resultRecord.elementIterator("result");
//                    if (!itersElIterator.hasNext() && count == 0) {
//
//                        removeListViewToNoResults();
//                        break;
//                    }else if (!itersElIterator.hasNext()) {
//                        Toast.makeText(getApplicationContext(), "No more results...", Toast.LENGTH_SHORT).show();
//                    }
//                    count++;
//                    while (itersElIterator.hasNext()) {
//                        Element itemEle = (Element) itersElIterator.next();
//                        Iterator literLIterator = itemEle.elementIterator("binding");
//                        while(literLIterator.hasNext()){
//                            Element ele = (Element) literLIterator.next();
//                            if ("prod_id".equals(ele.attributeValue("name"))){
//                                product_id = ele.elementTextTrim("uri");
//                            }
//                        }
//                    }
//                }
//                pdata = new ProductData();
//                pdata.setProductId(product_id);
//                ForwardPdataToScannerActivity(pdata);
//            } catch (DocumentException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }

    private void removeListViewToNoResults() {
        View listview = findViewById(R.id.productlist);
        if (null != listview) {

            no_result = getLayoutInflater().inflate(R.layout.no_results,layout,false);

            listview.setVisibility(View.GONE);

            layout.addView(no_result);
        }
    }

//    private void removeAsinListViewToNoResults() {
//        View listview = findViewById(R.id.asintlist);
//        if (null != listview) {
//
//            no_result2 = getLayoutInflater().inflate(R.layout.no_results,layout2,false);
//
//            listview.setVisibility(View.GONE);
//
//            layout2.addView(no_result2);
//        }
//    }

    private void removDeviceListViewToNoResults() {
        View listview = findViewById(R.id.devicelist);
        if (null != listview) {

            no_result3 = getLayoutInflater().inflate(R.layout.no_results,layout3,false);

            listview.setVisibility(View.GONE);

            layout3.addView(no_result3);
        }
    }
    private void removeListViewToNoInternet(){
        final View listview = findViewById(R.id.productlist);
        if (null != listview) {
            if(null == no_internet) {
                no_internet = getLayoutInflater().inflate(R.layout.no_internet, layout, false);
                layout.addView(no_internet);
            }
            listview.setVisibility(View.GONE);
            no_internet.setVisibility(View.VISIBLE);

            no_internet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    no_internet.setVisibility(View.GONE);
                    if(no_internet2 != null)
                        no_internet2.setVisibility(View.GONE);

                    listview.setVisibility(View.VISIBLE);
                    initInternetStatusPage(searchStr);
                }
            });
        }
    }

//    private void removeAsinListToNoInternet(){
//        View listview = findViewById(R.id.asintlist);
//        if (null != listview) {
//            if(null == no_internet2) {
//                no_internet2 = getLayoutInflater().inflate(R.layout.no_internet, layout2, false);
//                layout2.addView(no_internet2);
//            }
//            asinlist.setVisibility(View.GONE);
//            no_internet2.setVisibility(View.VISIBLE);
//
//            no_internet2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    no_internet2.setVisibility(View.GONE);
//
//                    if(no_internet != null)
//                        no_internet.setVisibility(View.GONE);
//                    if(no_internet3 != null)
//                        no_internet3.setVisibility(View.GONE);
//
//                    asinlist.setVisibility(View.VISIBLE);
//                    searchByAsin();
//                }
//            });
//        }
//    }

    private void removeDeviceListToNoInternet(){
        View listview = findViewById(R.id.devicelist);
        if (null != listview) {
            if(null == no_internet3) {
                no_internet3 = getLayoutInflater().inflate(R.layout.no_internet, layout3, false);
                layout3.addView(no_internet3);
            }
            devicelist.setVisibility(View.GONE);
            no_internet3.setVisibility(View.VISIBLE);

            no_internet3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    no_internet3.setVisibility(View.GONE);

                    if(no_internet != null)
                        no_internet.setVisibility(View.GONE);
                    if(no_internet2 != null)
                        no_internet2.setVisibility(View.GONE);

                    devicelist.setVisibility(View.VISIBLE);
                    searchByDevices();
                }
            });
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
    }

    private void setTabSelected(RadioButton btnSelected) {
        Drawable selectedDrawable = ResourceReader.readDrawable(this, R.drawable.shape_nav_indicator);
        int screenWidth = DensityUtils.getScreenSize(ProductListActivity.this)[0];
        int right = screenWidth / 3;
        selectedDrawable.setBounds(0, 0, right, DensityUtils.dipTopx(this, 3));
        btnSelected.setSelected(true);
        btnSelected.setCompoundDrawables(null, null, null, selectedDrawable);

        int size = radioSearch.getChildCount();
        for (int i = 0; i < size; i++) {
            if (btnSelected.getId() != radioSearch.getChildAt(i).getId()) {
                radioSearch.getChildAt(i).setSelected(false);
                ((RadioButton) radioSearch.getChildAt(i)).setCompoundDrawables(null, null, null, null);
            }
        }
    }

    public void sampleCustomView() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.bottomdialog_layout, null);

        new BottomDialog.Builder(this)
                .setTitle("Welcome!")
                .setContent("You can select your device to find a charger")
                .setNegativeText("Close")
                .show();
    }



}
