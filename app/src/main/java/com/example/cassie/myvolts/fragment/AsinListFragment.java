//package com.example.cassie.myvolts.fragment;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.AnimationUtils;
//import android.widget.AbsListView;
//import android.widget.FrameLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.cassie.myvolts.R;
//import com.example.cassie.myvolts.ScannerActivity;
//import com.example.cassie.myvolts.adapter.AsinListAdapter;
//import com.example.cassie.myvolts.dto.DeviceData;
//import com.example.cassie.myvolts.dto.ProductData;
//import com.example.cassie.myvolts.dto.TechSpec;
//import com.example.cassie.myvolts.util.HttpUtils;
//import com.example.cassie.myvolts.util.RegexUtil;
//import com.github.clans.fab.FloatingActionButton;
//
//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;
//
//
//public class AsinListFragment extends Fragment implements AbsListView.OnScrollListener{
//    // TODO: Rename parameter arguments, choose names that match
//    View view;
//
//    ProductData pdata = null;
//
//    private int curPage = 0;
//    private int count = 0;
//    private AsinListAdapter adapter;
//
//    private int visibleLastIndex = 0;
//    private int visibleItemCount;
//    private int mPreviousVisibleItem;
//    private View loadMoreView2;
//    private TextView loadmorebutton;
//
//    JaroWinkler algorithm = new JaroWinkler();
//    List<DeviceData> asins = new ArrayList<>();
//
//
//    private SharedPreferences sharedPreferences;
//    private SharedPreferences.Editor editor;
//
//    private String searchStr,made,type,model;
//
//    ListView listView;
//    FloatingActionButton mFab;
//    FrameLayout asinfrag;
//
//    View no_result,no_internet;
//
//    SearchByAsin db;
//
//    GetProductByAsin getProductByAsin;
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        sharedPreferences = getActivity().getSharedPreferences("base64", Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//
//        Bundle bundle = this.getArguments();
//
//        if(bundle != null) {
//            searchStr = bundle.getString("searchStr");
//            made = bundle.getString("made");
//            type = bundle.getString("device");
//            model = bundle.getString("model");
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        if (view == null) {
////            view = inflater.inflate(R.layout.fragment_asin_list, container,false);
////            mFab = (FloatingActionButton) view.findViewById(R.id.fab);
////            listView = (ListView) view.findViewById(R.id.asintlist);
////            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                @Override
////                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                    TestUtil.storeSearchClicks(sharedPreferences, editor, "resultsListClick");
////                    if(asins.size() > 0) {
////                        DeviceData device = asins.get(position);
////                        getProductByAsin = new GetProductByAsin();
////                        getProductByAsin.execute(device.getAsin());
////                    }
////                }
////            });
////            asinfrag = (FrameLayout) view.findViewById(R.id.asin_fragment);
//        }
//
//        initInternetStatusPage(searchStr);
//        setFab();
//        return view;
//    }
//
//    public void updateView(){
////        if(NetworkUtil.isNetworkAvailable(getActivity()) == true) {
////            if(null != no_internet && null != listView) {
////                no_internet.setVisibility(View.GONE);
////                listView.setVisibility(View.VISIBLE);
////            }
////                FragmentTransaction ft = getFragmentManager().beginTransaction();
////                ft.detach(AsinListFragment.this).attach(AsinListFragment.this).commit();
////        }
//    }
//
//    private void initInternetStatusPage(String searchStr) {
////        boolean internetStatus = NetworkUtil.isNetworkAvailable(getActivity());
////
////        if(internetStatus == false){
////            doNoInternet();
////
////        }else {
////            doWithInternet(searchStr);
////        }
//    }
//
//    private void doWithInternet(String searchStr) {
//        db = new SearchByAsin();
//        if (searchStr != null && !searchStr.equals("")) {
//            try {
//                db.execute(RegexUtil.spliteRegex(searchStr));
//            }catch(Exception ex){
//
//            }
//        }
//
//        listView.setOnScrollListener(this);
//    }
//
//    private void doNoInternet() {
////        if (null != listView) {
////            if (null == no_internet) {
////                no_internet = getActivity().getLayoutInflater().inflate(R.layout.no_internet, null);
////                asinfrag.addView(no_internet);
////                no_internet.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        if(NetworkUtil.isNetworkAvailable(getActivity()) == true) {
////                            no_internet.setVisibility(View.GONE);
////                            listView.setVisibility(View.VISIBLE);
////                            FragmentTransaction ft = getFragmentManager().beginTransaction();
////                            ft.detach(AsinListFragment.this).attach(AsinListFragment.this).commit();
////                            DeviceListFragment deviceListFragment = (DeviceListFragment)getFragmentManager().findFragmentByTag("android:switcher:"+R.id.viewpager+":1");
////                            deviceListFragment.updateView();
////                            //ProductListFragment productListFragment = (ProductListFragment) getFragmentManager().findFragmentByTag("android:switcher:"+R.id.viewpager+":0");
////                            //productListFragment.updateView();
////                        }
////                    }
////                });
////            }
////            listView.setVisibility(View.GONE);
////        }
//    }
//
//
//    private void removeListViewToNoResults() {
////        if (null != listView) {
////            no_result = getActivity().getLayoutInflater().inflate(R.layout.no_results, null);
////            listView.setVisibility(View.GONE);
////            asinfrag.addView(no_result);
////        }
//    }
//
//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        int itemsLastIndex = adapter.getCount() - 1;
//        int lastIndex = itemsLastIndex + 1;
//        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
//            if (searchStr != null && !searchStr.equals("")) {
//                if(adapter.getCount() % 10 == 0) {
//                    SearchByAsin searchByAsin = new SearchByAsin();
//                    searchByAsin.execute(searchStr);
//                    adapter.notifyDataSetChanged();
//                    listView.setSelection(visibleLastIndex - visibleItemCount + 1);
//                }else{
//                    Toast.makeText(getActivity(), "No more results", Toast.LENGTH_SHORT).show();;
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        this.visibleItemCount = visibleItemCount;
//        visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
//
//        if (firstVisibleItem > mPreviousVisibleItem) {
//            mFab.hide(true);
//        } else if (firstVisibleItem < mPreviousVisibleItem) {
//            mFab.show(true);
//        }
//        mPreviousVisibleItem = firstVisibleItem;
//    }
//
//    private void ForwardPdataToScannerActivity(ProductData product) {
//        Bundle b = new Bundle();
//        Intent intent = new Intent(getActivity(), ScannerActivity.class);
//        b.putSerializable("product", product);
//        intent.putExtras(b);
//        startActivity(intent);
//    }
//
//    public void setFab(){
//        mFab.hide(false);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mFab.show(true);
//                mFab.setShowAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.show_from_bottom));
//                mFab.setHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom));
//            }
//        }, 300);
//
//        mFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listView.setSelection(0);
//            }
//        });
//
//    }
//
//
//    public class GetProductByAsin extends AsyncTask<String, Void, String> {
//
//        ProgressDialog p = new ProgressDialog(getActivity(),
//                R.style.SpinnerTheme);
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
//            //System.out.println(result);
//
//
//            Document doc = null;
//            String product_id = "";
//
//            if (result != null && !result.equals("")) {
//
//                try {
//                    doc = DocumentHelper.parseText(result);
//                    Element rootElt = doc.getRootElement();
//                    Iterator iter = rootElt.elementIterator("results");
//
//                    while (iter.hasNext()) {
//                        Element resultRecord = (Element) iter.next();
//                        Iterator itersElIterator = resultRecord.elementIterator("result");
//                        if (!itersElIterator.hasNext()) {
//                            removeListViewToNoResults();
//                        }
//                        while (itersElIterator.hasNext()) {
//                            Element itemEle = (Element) itersElIterator.next();
//                            Iterator literLIterator = itemEle.elementIterator("binding");
//                            while (literLIterator.hasNext()) {
//                                Element ele = (Element) literLIterator.next();
//                                if ("prod_id".equals(ele.attributeValue("name"))) {
//                                    product_id = ele.elementTextTrim("uri");
//                                }
//                            }
//                        }
//                    }
//                    pdata = new ProductData();
//                    pdata.setProductId(product_id);
//                    ForwardPdataToScannerActivity(pdata);
//                } catch (DocumentException e) {
//                    e.printStackTrace();
//                }
//
//            }else{
//                removeListViewToNoResults();
//            }
//        }
//    }
//
//    public class SearchByAsin extends AsyncTask<String, Void, String> {
//
//        ProgressDialog p = new ProgressDialog(getActivity(),
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
//                            if (!asins.contains(device)) {
//                                newData.add(device);
//                            }
//                        }
//                    }
//
//                    if (adapter == null) {
//                        adapter = new AsinListAdapter(asins, getContext(), searchStr);
//                        listView.setAdapter(adapter);
//                        if(loadMoreView2 == null) {
//                            loadMoreView2 = getActivity().getLayoutInflater().inflate(R.layout.load_more, null);
//                            loadmorebutton = (TextView) loadMoreView2.findViewById(R.id.loadMoreButton);
//                            listView.addFooterView(loadmorebutton);
//                        }
//                    }
//
//                    asins.addAll(newData);
//                    adapter.setDatas(asins);
//
//                } catch (DocumentException e) {
//                    e.printStackTrace();
//                }
//            }else {
//                doNoInternet();
//            }
//        }
//    }
//}
