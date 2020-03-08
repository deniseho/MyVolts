package com.example.cassie.myvolts.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cassie.myvolts.R;
import com.example.cassie.myvolts.ScannerActivity;
import com.example.cassie.myvolts.adapter.ProductListAdapter;
import com.example.cassie.myvolts.db.DbHelp;
import com.example.cassie.myvolts.dto.ProductData;
import com.example.cassie.myvolts.util.NetworkUtil;
import com.example.cassie.myvolts.util.TestUtil;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductListFragment extends Fragment implements AbsListView.OnScrollListener{

    View view;

    private int count = 0;
    private int curPage = 0;
    private ProductListAdapter adapter;

    private int visibleLastIndex = 0;
    private int visibleItemCount;
    private int mPreviousVisibleItem;
    private View loadMoreView;
    private TextView loadmorebutton;

    private List<ProductData> products = new ArrayList<>();;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String searchStr,made,type,model;

    ListView listView;
    FloatingActionButton mFab;
    FrameLayout productfrag;

    View no_result,no_internet,email;

    DbHelp dbHelp;
    private static final String TAG = "ProductListFragment";


    public ProductListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getActivity();
        dbHelp = new DbHelp(context);

        sharedPreferences = getActivity().getSharedPreferences("base64", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Bundle bundle = this.getArguments();

        if(bundle != null) {
            searchStr = bundle.getString("searchStr");
            made = bundle.getString("made");
            type = bundle.getString("device");
            model = bundle.getString("model");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_product_list, container,false);
            mFab = (FloatingActionButton) view.findViewById(R.id.fab);
            listView = (ListView) view.findViewById(R.id.productlist);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TestUtil.storeSearchClicks(sharedPreferences, editor, "resultsListClick");
                if(products.size() > 0) {
                    ProductData productData = products.get(position);
                    ForwardPdataToScannerActivity(productData);
                }
                }
            });
            productfrag = (FrameLayout) view.findViewById(R.id.product_fragment);
        }

        initInternetStatusPage(searchStr);
        setFab();

        return view;
    }

    private void initInternetStatusPage(String searchStr) {
        boolean internetStatus = NetworkUtil.isNetworkAvailable(getActivity());

        if(internetStatus == false){
            doNoInternet();

        }else {
            doWithInternet(searchStr);
        }
    }

    private void doWithInternet(String searchStr) {

        if (searchStr != null && !searchStr.equals("")) {
            try {
                Log.d(TAG, "getProducts");
                getProducts();
            }catch(Exception ex){
                Log.d(TAG, "getProducts ex" + ex);
            }
        }
        listView.setOnScrollListener(this);
    }

    private void doNoInternet() {
        if (null != listView) {
            if (null == no_internet) {
                no_internet = getActivity().getLayoutInflater().inflate(R.layout.no_internet, null);
                productfrag.addView(no_internet);
                no_internet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    if(NetworkUtil.isNetworkAvailable(getActivity()) == true) {
                        no_internet.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(ProductListFragment.this).attach(ProductListFragment.this).commit();
                            DeviceListFragment deviceListFragment = (DeviceListFragment)getFragmentManager().findFragmentByTag("android:switcher:"+R.id.viewpager+":1");
                            deviceListFragment.updateView();
                        //AsinListFragment asinListFragment = (AsinListFragment) getFragmentManager().findFragmentByTag("android:switcher:"+R.id.viewpager+":2");
                        //asinListFragment.updateView();
                    }
                    }
                });
            }
            listView.setVisibility(View.GONE);
        }
    }

    public void updateView(){
        if(NetworkUtil.isNetworkAvailable(getActivity()) == true) {
            if(null != no_internet && null != listView) {
                no_internet.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(ProductListFragment.this).attach(ProductListFragment.this).commit();
        }
    }

    private void ForwardPdataToScannerActivity(ProductData product) {
        Bundle b = new Bundle();
        Intent intent = new Intent(getActivity(), ScannerActivity.class);
        b.putSerializable("product", product);
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        db = new GetProducts();
        int itemsLastIndex = adapter.getCount() - 1;
        int lastIndex = itemsLastIndex + 1;
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
            if (searchStr != null && !searchStr.equals("")) {
                if(adapter.getCount() % 10 == 0) {
//                    GetProducts.getData();
//                    db.execute(RegexUtil.spliteRegex(searchStr));
                    getProducts();
                    adapter.notifyDataSetChanged();
                    listView.setSelection(visibleLastIndex - visibleItemCount + 1);
                }else{
                    Toast.makeText(getActivity(), "No more results", Toast.LENGTH_SHORT).show();;
                }
            } else {
                try {
//                    db.execute(URLEncoder.encode(made, "UTF-8"), URLEncoder.encode(type, "UTF-8"));
                    adapter.notifyDataSetChanged();
                    listView.setSelection(visibleLastIndex - visibleItemCount + 1);
                } catch (Exception ex) {

                }
            }
        }
    }

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

    public void setFab(){
        mFab.hide(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFab.show(true);
                mFab.setShowAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.show_from_bottom));
                mFab.setHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom));
            }
        }, 300);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setSelection(0);
            }
        });

    }

    private void getProducts() {
        List<ProductData> productList =  new ArrayList<>();
        List<String> pids = dbHelp.getPidByDevice(made, model);
        Log.d(TAG, "pid: "  + pids);

        for(int i=0; i<pids.size(); i++) {
            productList.add(dbHelp.getProductById(pids.get(i)));
        }

        Log.d(TAG, "productList size: "  + productList.size());

        if (productList.size() == 0 ) {
            removeListViewToNoResults();
        } else {
            if(searchStr != null)
                Collections.sort(productList);

            if (adapter == null) {
                adapter = new ProductListAdapter(productList, getContext(), searchStr);
                listView.setAdapter(adapter);
                if(loadMoreView == null) {
                    loadMoreView = getActivity().getLayoutInflater().inflate(R.layout.load_more, null);
                    loadmorebutton = (TextView) loadMoreView.findViewById(R.id.loadMoreButton);
                    listView.addFooterView(loadmorebutton);
                }
            }

            products.addAll(productList);
            adapter.setDatas(productList);
        }
    }

    private void removeListViewToNoResults() {
        if (null != listView) {
            no_result = getActivity().getLayoutInflater().inflate(R.layout.no_results, null);
            listView.setVisibility(View.GONE);
            productfrag.addView(no_result);

            Button buttonUpdateData = (Button) no_result.findViewById(R.id.updateDataButton);
            Button buttonOpenEmailView = (Button) no_result.findViewById(R.id.openEmailView);


            buttonUpdateData.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    updataData();
                }
            });

            buttonOpenEmailView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    no_result.setVisibility(View.GONE);
                    email = getActivity().getLayoutInflater().inflate(R.layout.activity_email, null);
                    productfrag.addView(email);
                }
            });


        }
    }

    private void updataData(){
        dbHelp.loadData();
    }



}
