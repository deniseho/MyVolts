package com.example.cassie.myvolts.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cassie.myvolts.R;
import com.example.cassie.myvolts.ScannerActivity;
import com.example.cassie.myvolts.adapter.ProductListAdapter;
import com.example.cassie.myvolts.db.DbHelp;
import com.example.cassie.myvolts.db.DbManager;
import com.example.cassie.myvolts.dto.ProductData;
import com.example.cassie.myvolts.util.NetworkUtil;
import com.example.cassie.myvolts.util.RegexUtil;
import com.example.cassie.myvolts.util.TestUtil;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;

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

    JaroWinkler algorithm = new JaroWinkler();

    private List<ProductData> products = new ArrayList<>();;


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String searchStr,made,type,model;

    ListView listView;
    FloatingActionButton mFab;
    FrameLayout product;

    View no_result,no_internet;

    GetProducts db;

    DbHelp dbHelp;
    DbManager manager;

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
            product = (FrameLayout) view.findViewById(R.id.product_fragment);
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

        db = new GetProducts();
        if (searchStr != null && !searchStr.equals("")) {
            try {
                db.execute(RegexUtil.spliteRegex(searchStr));
            }catch(Exception ex){

            }
        } else {
            if(made != null && type != null && model != null)
                try {
                    db.execute(URLEncoder.encode(made), URLEncoder.encode(type, "UTF-8"), URLEncoder.encode(model, "UTF-8"));
                }catch(Exception ex){

                }
        }

        listView.setOnScrollListener(this);
    }

    private void doNoInternet() {
            if (null != listView) {
                if (null == no_internet) {
                    no_internet = getActivity().getLayoutInflater().inflate(R.layout.no_internet, null);
                    product.addView(no_internet);
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
        db = new GetProducts();
        int itemsLastIndex = adapter.getCount() - 1;
        int lastIndex = itemsLastIndex + 1;
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
            if (searchStr != null && !searchStr.equals("")) {
                if(adapter.getCount() % 10 == 0) {
                    db.execute(RegexUtil.spliteRegex(searchStr));
                    adapter.notifyDataSetChanged();
                    listView.setSelection(visibleLastIndex - visibleItemCount + 1);
                }else{
                    Toast.makeText(getActivity(), "No more results", Toast.LENGTH_SHORT).show();;
                }
            } else {
                try {
                    db.execute(URLEncoder.encode(made, "UTF-8"), URLEncoder.encode(type, "UTF-8"));
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



    public class GetProducts extends AsyncTask<Object, Void, JSONArray> {

        ProgressDialog p = new ProgressDialog(getActivity(),
                ProgressDialog.STYLE_SPINNER);


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            p.setMessage("Loading...");
            p.show();
        }


        @Override
        protected JSONArray doInBackground(Object... arg0) {
            // TODO Auto-generated method stub
            JSONArray arr = dbHelp.updateDataFromApi(searchStr);
            return arr;

////            String offset = "";
//            JSONArray output_arr = new JSONArray();
////
////            if(curPage > 0) {
////                offset = "%0AOFFSET%20" + curPage * 10;
////            }
////            curPage++;
////
//            String result = "";
//
//            if(searchStr != null && !searchStr.equals("")) {
////                String args = "";
////                for(int i = 1; i < arg0.length; i++){
////                    args = args + "%20%7C%7C%20%20regex(%3Fpname%2C%20%22" + arg0[i] + "%22%2C%20%22i%22)";
////                }
//                String url = "http://api.myjson.com/bins/1hcph0"; //"http://theme-e.adaptcentre.ie/openrdf-workbench/repositories/mv2.54/query?action=exec&queryLn=SPARQL&query=PREFIX%20%20%3A%20%3Chttp%3A%2F%2Fmyvolts.com%23%3E%0APREFIX%20owl%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0APREFIX%20rdf%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0APREFIX%20rdfs%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0ASELECT%20%20distinct%20%3Fprod_id%20%20%3Fpname%20%3Ftype%20%0AWHERE%20%0A%7B%20%0A%20%3Fprod_id%20%3Aproduct_name%20%3Fpname%20.%0A%20%3Fprod_id%20%3AisOfTypeCategory%20%3Ftype%20.%0A%0A%20filter%20(regex(%3Fpname%2C%20%22" + arg0[0] + "%22%2C%20%22i%22)" + args + ")%20.%0A%7D%0Aorder%20by%20%3Fpname%0ALIMIT%2010"+ offset +"&limit=100&infer=true&";
//
//                result = HttpUtils.doGet(url);
//                System.out.println("=======productfragment result: ======" + result);
//
//
//                try {
//                    JSONObject obj = new JSONObject(result);
//                    JSONArray mv_db_arr = obj.getJSONArray("mv_db");
//                    JSONArray mv_db_arr2 = mv_db_arr.getJSONArray(0);
//
//
//                    for(int i=0; i<mv_db_arr2.length(); i++) {
//
//                        JSONObject item = (JSONObject) mv_db_arr2.get(i);
//                        Iterator<String> keys = item.keys();
//
//                        String category = keys.next();
//                        if(category.equals("product")){
//                            String category_val = item.optString(category);
//                            output_arr.put(category_val);
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            return output_arr;
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            p.dismiss();

            List<ProductData> productData = new ArrayList<ProductData>();
            productData = dbHelp.serachProducts(searchStr);

            if(searchStr != null)
                Collections.sort(productData);

            if (adapter == null) {
                adapter = new ProductListAdapter(products, getContext(), searchStr);
                listView.setAdapter(adapter);
                if(loadMoreView == null) {
                    loadMoreView = getActivity().getLayoutInflater().inflate(R.layout.load_more, null);
                    loadmorebutton = (TextView) loadMoreView.findViewById(R.id.loadMoreButton);
                    listView.addFooterView(loadmorebutton);
                }
            }

            products.addAll(productData);
            adapter.setDatas(products);
//            adapter.setDatas(products);

//            String product_id = "";
//            String dname = "";
//            String x= "";
//            String y = "";
//            String z = "";
//            Document doc = null;
//            String type = "";
//            String images = "";

//            newData.add(new ProductData("3", "Dymo Label printer LT-100H Compatible Power Supply Plug Charger",null));
//            newData.add(new ProductData("1", "Korg Tuner Pitchblack Compatible Power Supply Cable & in Car Charger",null));
//            newData.add(new ProductData("2", "Korg PSU part KA-183 Compatible Power Supply Cable & in Car Charger",null));



//            if(result != null && !result.equals("")) {

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
//                            //Toast.makeText(getContext(), "No more results...", Toast.LENGTH_SHORT).show();
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

//                    newData.add(new ProductData("4","Seagate PSU part FreeAgent 9NK2AE-500 Compatible Power Supply Plug Charger",null));

//                } catch (DocumentException e) {
//                    e.printStackTrace();
//                }
//            }else{
//                doNoInternet();
//            }
        }
    }

    private void removeListViewToNoResults() {
        if (null != listView) {
            no_result = getActivity().getLayoutInflater().inflate(R.layout.no_results, null);
            listView.setVisibility(View.GONE);
            product.addView(no_result);
        }
    }

}
