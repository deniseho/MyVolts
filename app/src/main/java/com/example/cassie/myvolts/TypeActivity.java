package com.example.cassie.myvolts;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cassie.myvolts.db.DbHelp;
import com.example.cassie.myvolts.util.HttpUtils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by cassie on 23/05/2017.
 */
public class TypeActivity extends AppCompatActivity {
    @BindView(R.id.datalist)
    ListView datalist;
    ArrayAdapter<String> adapter;
    DbHelp dbHelp;
    @BindView(R.id.back_but)
    ImageView back;
    @BindView(R.id.main_title)
    TextView maintitle;
    @BindView(R.id.no_internet)
    ImageView no_internet;
    @BindView(R.id.no_results)
    ImageView no_results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        ButterKnife.bind(this);

        dbHelp = new DbHelp(this);

        String made = this.getIntent().getStringExtra("made");
        String device = this.getIntent().getStringExtra("device");
        if (TextUtils.isEmpty(made) && TextUtils.isEmpty(device)) {
            adapter = new ArrayAdapter<String>(this, R.layout.type_item, R.id.item_name, dbHelp.getMade());
            datalist.setAdapter(adapter);
        }else if(!TextUtils.isEmpty(made) && !TextUtils.isEmpty(device)){
            try {
                maintitle.setText("Select a Model");
                GetModels getModels = new GetModels();
                getModels.execute(URLEncoder.encode(made, "UTF-8"), URLEncoder.encode(device, "UTF-8"));
            }catch (UnsupportedEncodingException ex){

            }
        }else{
            try {
                maintitle.setText("Select a Device");
                GetDevices getDevices = new GetDevices();
                getDevices.execute(URLEncoder.encode(made, "UTF-8"));
            }catch (UnsupportedEncodingException ex){

            }
        }
    }

    @OnItemClick(R.id.datalist)
    public void onItemClick(int pos){
        String item = adapter.getItem(pos);
        Intent intent = new Intent();
        intent.putExtra("result",item);
        setResult(RESULT_OK,intent);
        finish();
    }

    @OnClick(R.id.back_but)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_but:
                finish();
                break;
        }
    }

    public class GetDevices extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            String url = "http://theme-e.adaptcentre.ie/openrdf-workbench/repositories/29.03mv2.4/query?action=exec&queryLn=SPARQL&query=PREFIX%20%20%3A%20%3Chttp%3A%2F%2Fmyvolts.com%23%3E%0APREFIX%20owl%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0APREFIX%20rdf%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0APREFIX%20rdfs%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0A%0ASELECT%20%3Fpi_id%20%20%3Fdname%20WHERE%20%7B%0A%3Fpi_id%20%3AIsManufacturedBy%20%3Fman%20.%0A%3Fman%20%3AManufacturer_name%20%22" + arg0[0] + "%22%20.%0A%3Fpi_id%20%3AIsOfDeviceCategory%20%3Fdevice.%0A%3Fdevice%20%3APiDevice_name%20%3Fdname%20.%0A%7D&limit=100&infer=true&";
            String result = HttpUtils.doGet(url);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            System.out.println(result);
            List<String> devices = new ArrayList<>();

            Document doc = null;

            if(result != null && !result.equals("")) {
                try {
                    doc = DocumentHelper.parseText(result);
                    Element rootElt = doc.getRootElement();
                    Iterator iter = rootElt.elementIterator("results");

                    while (iter.hasNext()) {
                        Element resultRecord = (Element) iter.next();
                        Iterator itersElIterator = resultRecord.elementIterator("result");
                        if (!itersElIterator.hasNext()) {
                            doNoResults();
                            break;
                        }
                        while (itersElIterator.hasNext()) {
                            Element itemEle = (Element) itersElIterator.next();
                            Iterator literLIterator = itemEle.elementIterator("binding");
                            while (literLIterator.hasNext()) {
                                Element ele = (Element) literLIterator.next();
                                if ("dname".equals(ele.attributeValue("name"))) {
                                    String dname = ele.elementTextTrim("literal");
                                    boolean check = false;
                                    if (!dname.equals("")) {
                                        for (String d : devices) {
                                            if (d.equals(dname)) {
                                                check = true;
                                            }
                                        }
                                        if (check == false)
                                            devices.add(dname);
                                    }

                                }

                            }

                        }

                    }

                    if (devices.size() > 0) {
                        adapter = new ArrayAdapter<String>(TypeActivity.this, R.layout.type_item, R.id.item_name, devices);
                    }

                    datalist.setAdapter(adapter);

                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }else{
                doNoInternet();
            }
        }
    }

    private void doNoInternet() {
        no_internet.setVisibility(View.VISIBLE);
        no_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });
    }

    private void doNoResults() {
        no_results.setVisibility(View.VISIBLE);
        no_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });
    }

    public class GetModels extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            String url = "http://theme-e.adaptcentre.ie/openrdf-workbench/repositories/mv2.53/query?action=exec&queryLn=SPARQL&query=PREFIX%20%20%3A%20%3Chttp%3A%2F%2Fmyvolts.com%23%3E%0APREFIX%20owl%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0APREFIX%20rdf%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0APREFIX%20rdfs%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0A%0ASELECT%20%20Distinct%20%3Fmodel%0AWHERE%0A%7B%20%0A%20%3Fpi_id%20%3ApiModel_name%20%3Fmodel%20.%0A%20%3Fpi_id%20%3AisManufacturedBy%20%20%3Fman%20.%0A%20%3Fman%20%3Amanufacturer_name%20%22" + arg0[0] + "%22%20.%0A%20%3Fpi_id%20%3AisOfDeviceCategory%20%20%3Fdevice%20.%0A%20%3Fdevice%20%3ApiDevice_name%20%22" + arg0[1] + "%22%20.%0A%0A%7D%0AORDER%20BY%20%3Fmodel%0A&limit=100&infer=true&";
            String result = HttpUtils.doGet(url);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            System.out.println(result);
            List<String> models = new ArrayList<>();

            Document doc = null;

            if(result != null && !result.equals("")) {

                try {
                    doc = DocumentHelper.parseText(result);
                    Element rootElt = doc.getRootElement();
                    Iterator iter = rootElt.elementIterator("results");

                    while (iter.hasNext()) {
                        Element resultRecord = (Element) iter.next();
                        Iterator itersElIterator = resultRecord.elementIterator("result");
                        if (!itersElIterator.hasNext()) {
                            doNoResults();
                            break;
                        }
                        while (itersElIterator.hasNext()) {
                            Element itemEle = (Element) itersElIterator.next();
                            Iterator literLIterator = itemEle.elementIterator("binding");
                            while (literLIterator.hasNext()) {
                                Element ele = (Element) literLIterator.next();
                                if ("model".equals(ele.attributeValue("name"))) {
                                    String model = ele.elementTextTrim("literal");
                                    models.add(model);
                                }

                            }

                        }

                    }

                    if (models.size() > 0) {
                        adapter = new ArrayAdapter<String>(TypeActivity.this, R.layout.type_item, R.id.item_name, models);
                    }

                    datalist.setAdapter(adapter);


                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }else{
                doNoInternet();
            }

        }
    }
}
