package com.example.cassie.myvolts;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.claudiodegio.msv.BaseMaterialSearchView;
import com.claudiodegio.msv.OnSearchViewListener;
import com.claudiodegio.msv.SuggestionMaterialSearchView;
import com.example.cassie.myvolts.adapter.ViewPagerAdapter;
import com.example.cassie.myvolts.db.DbHelp;
import com.example.cassie.myvolts.fragment.AsinListFragment;
import com.example.cassie.myvolts.fragment.DeviceListFragment;
import com.example.cassie.myvolts.fragment.ProductListFragment;

import java.util.List;

public class TabActivity extends AppCompatActivity implements OnSearchViewListener{

    private BaseMaterialSearchView mSearchView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private DbHelp dbHelp;
    private String searchStr,made,type,model;
    private RecyclerView mRvItem;


    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


        initValue();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mSearchView = (BaseMaterialSearchView) findViewById(R.id.sv);

        mRvItem = (RecyclerView) findViewById(R.id.rv_item);

        dbHelp = new DbHelp(this);

        List<String> list=
                dbHelp.getAllProductName();
        String[] stockArr = new String[list.size()];
        stockArr = list.toArray(stockArr);


        SuggestionMaterialSearchView cast = (SuggestionMaterialSearchView)mSearchView;
        cast.setSuggestion(stockArr);
        mSearchView.setOnSearchViewListener(this);


    }

    private void initValue(){
        searchStr = this.getIntent().getStringExtra("search");
        made = this.getIntent().getStringExtra("made");
        type = this.getIntent().getStringExtra("type");
        model = this.getIntent().getStringExtra("model");
    }

    private Bundle getValue(){
        Bundle bundle = new Bundle();
        bundle.putString("searchStr", searchStr);
        bundle.putString("made", made);
        bundle.putString("device", type);
        bundle.putString("model", model);

        return bundle;
    }

    private Bundle getNewBundle(String searchStr){
        Bundle bundle = new Bundle();
        bundle.putString("searchStr", searchStr);

        return bundle;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), getValue());

        adapter.addFragment(new ProductListFragment(), "Product");
        adapter.addFragment(new DeviceListFragment(), "Device");
        adapter.addFragment(new AsinListFragment(), "ASIN");

        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.home:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        if (mSearchView.isOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onSearchViewShown() {
        System.out.println("1");
    }

    @Override
    public void onSearchViewClosed() {
        System.out.println("2");
    }

    @Override
    public boolean onQueryTextSubmit(String s) {

        String pid = dbHelp.getProductIdByName(s);
        if(null != pid && !"".equals(pid)){
            dbHelp.saveHis(s, "1", pid);
            forwardToSannerActivity(pid);
        }else {
            int id = viewPager.getCurrentItem();
            adapter.removeAll();
            adapter = new ViewPagerAdapter(getSupportFragmentManager(), getNewBundle(s));
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(1);
           adapter.addFragment(new ProductListFragment(), "Product");
           adapter.addFragment(new DeviceListFragment(), "Device");
           adapter.addFragment(new AsinListFragment(), "ASIN");

            adapter.notifyChangeInPosition(1);
            adapter.notifyDataSetChanged();

//            adapter.updateBundle(getNewBundle(s));
            viewPager.setCurrentItem(id);
            return false;

        }

        return true;
    }

    @Override
    public void onQueryTextChange(String s) {
        System.out.println("4");
    }

    private void forwardToSannerActivity(String pid) {
        Intent intent = new Intent(TabActivity.this,ScannerActivity.class);
        intent.putExtra("product_id", pid);
        startActivity(intent);
    }

}