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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cassie.myvolts.R;
import com.example.cassie.myvolts.ScannerActivity;
import com.example.cassie.myvolts.adapter.DeviceListAdapter;
import com.example.cassie.myvolts.db.DbHelp;
import com.example.cassie.myvolts.dto.DeviceData;
import com.example.cassie.myvolts.dto.ProductData;
import com.example.cassie.myvolts.util.HttpUtils;
import com.example.cassie.myvolts.util.NetworkUtil;
import com.example.cassie.myvolts.util.RegexUtil;
import com.example.cassie.myvolts.util.TestUtil;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;


public class DeviceListFragment extends Fragment implements AbsListView.OnScrollListener{
    // TODO: Rename parameter arguments, choose names that match

    View view;
    private ProductData pdata = null;
    private int curPage = 0;
    private int count = 0;

    private int visibleLastIndex = 0;
    private int visibleItemCount;
    private int mPreviousVisibleItem;
    private View loadMoreView3;
    private TextView loadmorebutton;

    List<DeviceData> pis;
    private DeviceListAdapter adapter;


    JaroWinkler algorithm = new JaroWinkler();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String searchStr,made,type,model;

    ListView listView;
    FloatingActionButton mFab;
    FrameLayout devicefrag;

    View no_result,no_internet;

    GetDeviceByName db;
    DbHelp dbHelp;

    private EditText mailTo;
    private EditText mailSubject;
    private EditText mailMessage;

    public DeviceListFragment() {
        // Required empty public constructor
        pis = new ArrayList<>();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            view = inflater.inflate(R.layout.fragment_device_list, container,false);
            mFab = (FloatingActionButton) view.findViewById(R.id.fab);
            listView = (ListView) view.findViewById(R.id.devicelist);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TestUtil.storeSearchClicks(sharedPreferences, editor, "resultsListClick");
//                    if(pis.size() > 0) {
//                        DeviceData device = pis.get(position);
//                        GetProductByAsin getProductByAsin = new GetProductByAsin();
//                        getProductByAsin.execute(device.getAsin());
//                    }
                }
            });
            devicefrag = (FrameLayout) view.findViewById(R.id.device_fragment);


            mailTo = (EditText) view.findViewById(R.id.edit_text_id);
            mailSubject = (EditText) view.findViewById(R.id.edit_text_subject);
            mailMessage = (EditText) view.findViewById(R.id.edit_text_message);

            Button buttonSend = (Button) view.findViewById(R.id.email_send);
            buttonSend.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    sendMail();
                    System.out.println("clicked send email");
                }
            });

            Button buttonUpdateData = (Button) view.findViewById(R.id.update_data);
            buttonUpdateData.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    updataData();
                }
            });
        }

        initInternetStatusPage(searchStr);
        setFab();
        return view;
    }


    private void updataData(){
        db = new GetDeviceByName();
        db.execute(RegexUtil.spliteRegex(""));
        Toast.makeText(getActivity(), "update data from deviceListFrag", Toast.LENGTH_SHORT).show();
    }


    private void sendMail() {
        String recipientList = mailTo.getText().toString();
        String[] recipients = recipientList.split(",");

        String subject = mailSubject.getText().toString();
        String message = mailMessage.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rcf822");
        startActivity(Intent.createChooser(intent,"Choose an email client"));
    }




    private void initInternetStatusPage(String searchStr) {
        boolean internetStatus = NetworkUtil.isNetworkAvailable(getActivity());

        if(internetStatus == false){
            doNoInternet();

        }else {
            doWithInternet(searchStr);
        }
    }

    public void updateView(){
        if(NetworkUtil.isNetworkAvailable(getActivity()) == true) {
            if(null != no_internet && null != listView) {
                no_internet.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(DeviceListFragment.this).attach(DeviceListFragment.this).commit();
        }
    }

    private void doWithInternet(String searchStr) {

        db = new GetDeviceByName();
        if (searchStr != null && !searchStr.equals("")) {
            try {
                db.execute(RegexUtil.spliteRegex(searchStr));
            }catch(Exception ex){

            }
        }

        listView.setOnScrollListener(this);
    }

    private void doNoInternet() {
        if (null != listView) {
            if (null == no_internet) {
                no_internet = getActivity().getLayoutInflater().inflate(R.layout.no_internet, null);
                devicefrag.addView(no_internet);
                no_internet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(NetworkUtil.isNetworkAvailable(getActivity()) == true) {
                            no_internet.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(DeviceListFragment.this).attach(DeviceListFragment.this).commit();
                            ProductListFragment productListFragment = (ProductListFragment) getFragmentManager().findFragmentByTag("android:switcher:"+R.id.viewpager+":0");
                            productListFragment.updateView();
//                            AsinListFragment asinListFragment = (AsinListFragment) getFragmentManager().findFragmentByTag("android:switcher:"+R.id.viewpager+":2");
//                            asinListFragment.updateView();
                        }
                    }
                });
            }
            listView.setVisibility(View.GONE);
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        db = new GetDeviceByName();
        int itemsLastIndex = adapter.getCount() - 1;
        int lastIndex = itemsLastIndex + 1;
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
            if(adapter.getCount() % 10 == 0) {
                db.execute(RegexUtil.spliteRegex(searchStr));
                adapter.notifyDataSetChanged();
                listView.setSelection(visibleLastIndex - visibleItemCount + 1);
            }else{
                Toast.makeText(getActivity(), "No more results", Toast.LENGTH_SHORT).show();;

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

    private void ForwardPdataToScannerActivity(ProductData product) {
        Bundle b = new Bundle();
        Intent intent = new Intent(getActivity(), ScannerActivity.class);
        b.putSerializable("product", product);
        intent.putExtras(b);
        startActivity(intent);
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

    public class GetDeviceByName extends AsyncTask<String, Void, String> {

        ProgressDialog p = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            p.setMessage("Loading...");
            p.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
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

//                String url = "https://api.myjson.com/bins/x6a9y";//"http://theme-e.adaptcentre.ie/openrdf-workbench/repositories/mv2.53/query?action=exec&queryLn=SPARQL&query=PREFIX%20%20%3A%20%3Chttp%3A%2F%2Fmyvolts.com%23%3E%0A%0APREFIX%20owl%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0A%0APREFIX%20rdf%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0A%0APREFIX%20rdfs%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0A%0ASELECT%20distinct%20%3Fpi_id%20%3Fmname%20%3Fdname%20%3Fmodel%20%3Fasin%20WHERE%20%7B%0A%0A%3Fpi_id%20%3AisManufacturedBy%20%3Fman%20.%0A%0A%3Fman%20%3Amanufacturer_name%20%3Fmname%20.%0A%0A%3Fpi_id%20%3Api_name%20%3Fdname%20.%0A%0A%3Fpi_id%20%3ApiModel_name%20%3Fmodel%20.%0A%0A%3Fpi_id%20%3Api_asin%20%3Fasin%20.%0A%0AFILTER%20(regex(%3Fdname%2C%20%22" + arg0[0] + "%22%2C%20%22i%22)" + args + ")%20.%0AFilter(%3Fasin%20!%3D%20%22%22)%0A%7D%0AOrder%20By%20%3Fdname%0ALIMIT%2010" + offset +"&limit=100&infer=true&";
//
//                result = HttpUtils.doGet(url);
//            }
//
//            return result;
            String result = "";
            String url = "http://frodo.digidave.co.uk/api/RipApp/result.php?start=0&limit=3";
            result = HttpUtils.doGet(url);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            p.dismiss();

//            List<DeviceData> newData = new ArrayList<>();

//            String pi_id = "";
//            String dname = "";
//            String asin= "";
//            String model = "";
//            String mname = "";
//            Document doc = null;

            if(result != null && !result.equals("")) {

//                try {
//                    doc = DocumentHelper.parseText(result);
//                    Element rootElt = doc.getRootElement();
//                    Iterator iter = rootElt.elementIterator("mv_db");
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
//                                if ("pi_id".equals(ele.attributeValue("name"))) {
//                                    pi_id = ele.elementTextTrim("uri");
//                                } else if ("model".equals(ele.attributeValue("name"))) {
//                                    model = ele.elementTextTrim("literal");
//                                } else if ("asin".equals(ele.attributeValue("name"))) {
//                                    asin = ele.elementTextTrim("literal");
//                                } else if ("mname".equals(ele.attributeValue("name"))) {
//                                    mname = ele.elementTextTrim("literal");
//                                } else if ("dname".equals(ele.attributeValue("name"))) {
//                                    dname = ele.elementTextTrim("literal");
//                                }
//                            }
//
//                            DeviceData data = new DeviceData(pi_id, dname, mname, asin, model);
//                            newData.add(data);
//                        }


                String p_id = sharedPreferences.getString("p_id", null);
                String manufacturer = sharedPreferences.getString("manufacturer", null);
                String name = sharedPreferences.getString("name", null);
                String model = sharedPreferences.getString("model", null);
                String mv_uk = sharedPreferences.getString("mv_uk", null);
                String mv_de = sharedPreferences.getString("mv_de", null);
                String mv_us= sharedPreferences.getString("mv_us", null);

                DeviceData data = new DeviceData(p_id, manufacturer, name, model, mv_uk, mv_de, mv_us);
                List<DeviceData> deviceData = new ArrayList<>();//dbHelp.getSearchedDevices(searchStr);

//                String str = sharedPreferences.getString("deviceData", "");
//                if (!str.isEmpty()) {
//                    DeviceData device = new GsonBuilder()
//                            .serializeNulls()
//                            .create()
//                            .fromJson(str, DeviceData.class);
//                    System.out.println(device);
//                }

                deviceData.add(data);
                    if(searchStr != null)
                        Collections.sort(deviceData);

                    if (adapter == null) {
                        adapter = new DeviceListAdapter(deviceData, getContext(), searchStr);
                        listView.setAdapter(adapter);
                        if(loadMoreView3 == null) {
                            loadMoreView3 = getActivity().getLayoutInflater().inflate(R.layout.load_more, null);
                            loadmorebutton = (TextView) loadMoreView3.findViewById(R.id.loadMoreButton);
                            listView.addFooterView(loadmorebutton);
                        }
                    }

//                    pis.addAll(deviceData);
                    adapter.setDatas(deviceData);
//
//                } catch (DocumentException e) {
//                    e.printStackTrace();
//                }
            }else{
                removeListViewToNoResults();
            }
        }
    }

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
//            //System.out.println(url);
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
//            if(result != null && !result.equals("")) {
//                try {
//                    doc = DocumentHelper.parseText(result);
//                    Element rootElt = doc.getRootElement();
//                    Iterator iter = rootElt.elementIterator("results");
//
//                    while (iter.hasNext()) {
//                        Element resultRecord = (Element) iter.next();
//                        Iterator itersElIterator = resultRecord.elementIterator("result");
//
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
//            } else {
//                doNoInternet();
//            }
//        }
//    }


    private void removeListViewToNoResults() {
        if (null != listView) {
            no_result = getActivity().getLayoutInflater().inflate(R.layout.no_results, null);
            listView.setVisibility(View.GONE);
//            devicefrag.addView(no_result);
        }
    }
}
