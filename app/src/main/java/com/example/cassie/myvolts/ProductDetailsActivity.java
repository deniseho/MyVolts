package com.example.cassie.myvolts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cassie.myvolts.db.DbHelp;
import com.example.cassie.myvolts.dto.TechSpec;
import com.example.cassie.myvolts.testing.TaskResultActivity;
import com.example.cassie.myvolts.testing.TestingBean;
import com.example.cassie.myvolts.util.DigitUtil;
import com.example.cassie.myvolts.util.HttpUtils;
import com.example.cassie.myvolts.util.TestUtil;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.widget.NormalListDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductDetailsActivity extends AppCompatActivity {

    @BindView(R.id.product_name)
    TextView pro_name;
    @BindView(R.id.why)
    RelativeLayout why;
    @BindView(R.id.warranty)
    RelativeLayout warranty;
    @BindView(R.id.image_more)
    ImageView img;
    @BindView(R.id.image_more_2)
    ImageView img2;
    @BindView(R.id.product_photo)
    ImageView product_photo;
    @BindView(R.id.scroll)
    ScrollView scroll;
    @BindView(R.id.layout)
    RelativeLayout layout;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Context mContext = this;

    private List<TechSpec> techspecs;

    private ArrayList<DialogMenuItem> mMenuItems = new ArrayList<>();
    private DbHelp dbHelp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("base64", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        dbHelp = new DbHelp(this);

        //testTask();

        String scan = getIntent().getExtras().getString("result");
        getdp getdp = new getdp();
        getdp.execute(scan);
    }

    public void testTask(){
        System.out.println(sharedPreferences.getInt("task", 0));
        if(null != sharedPreferences){
            String testmode = sharedPreferences.getString("testmode", "");
            int taskId = sharedPreferences.getInt("task", 0);
                if(3 == taskId){
                    //isDisplay();
                    TestingBean bean = TestUtil.getAllTestingResults(sharedPreferences);
                    dbHelp.saveTestData(bean.getTaskId(),bean.getDeviceClick(),bean.getBrandClick(),bean.getModelClick(),bean.getSearchBoxClick(),
                            bean.getSearchBtnClick(),bean.getHistoryClck(),bean.getResultListClick(),bean.getSearchAcivtityListClick(),bean.getWorktime(),bean.getScanButtonClick());
                    }

        }
    }

   /* private void isDisplay(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ProductDetailsActivity.this);
        builder.setMessage("Task Finished");
        builder.setTitle("Display Test Result?");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(ProductDetailsActivity.this, TaskResultActivity.class);
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
*/
    private void NormalListDialogNoTitle() {
        for(TechSpec tech: techspecs){
            mMenuItems.add(new DialogMenuItem("Vol: " + tech.getVol() + " " + "Amp: "
                    + tech.getAmp() + " " + "Tip_Length: " + tech.getTip(), R.drawable.ic_tech));
        }
        final NormalListDialog dialog = new NormalListDialog(mContext, mMenuItems);
        dialog.title("All Technique Specs")
                .isTitleShow(false)
                .itemPressColor(Color.parseColor("#85D3EF"))
                .itemTextColor(Color.parseColor("#303030"))
                .itemTextSize(10)
                .cornerRadius(2)
                .widthScale(0.75f)
                .show();

    }


    @OnClick({R.id.why,R.id.warranty, R.id.back_but, R.id.techspec})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.why:
                View whytext = findViewById(R.id.why_txt_5);
                if (whytext.getVisibility() == View.GONE) {
                    whytext.setVisibility(View.VISIBLE);
                    img.setImageDrawable(getResources().getDrawable((R.drawable.bottom_icon)));

                }else if(whytext.getVisibility() == View.VISIBLE){
                    whytext.setVisibility(View.GONE);
                    img.setImageDrawable(getResources().getDrawable((R.drawable.right_icon)));
                }
                break;
            case R.id.warranty:
                View warrantytext = findViewById(R.id.warrranty_txt_3);
                if (warrantytext.getVisibility() == View.GONE) {
                    warrantytext.setVisibility(View.VISIBLE);
                    img2.setImageDrawable(getResources().getDrawable((R.drawable.bottom_icon)));
                }else if(warrantytext.getVisibility() == View.VISIBLE){
                    warrantytext.setVisibility(View.GONE);
                    img2.setImageDrawable(getResources().getDrawable((R.drawable.right_icon)));
                }
                break;
            case R.id.back_but:
                finish();
                break;
            case R.id.techspec:
                NormalListDialogNoTitle();
                break;
        }
    }


    public class getdp extends AsyncTask<String, Void, String> {

        ProgressDialog p = new ProgressDialog(ProductDetailsActivity.this,
                ProgressDialog.STYLE_SPINNER);

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
            String url = "http://theme-e.adaptcentre.ie/openrdf-workbench/repositories/29.03mv2.4/query?action=exec&queryLn=SPARQL&query=PREFIX%20%20%3A%20%3Chttp%3A%2F%2Fmyvolts.com%23%3E%0APREFIX%20owl%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0APREFIX%20rdf%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0APREFIX%20rdfs%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0A%0ASELECT%20%20Distinct%20%3Fprod_id%20%3Fpname%20%3Fx%20%3Fy%20%3Fz%0AWHERE%0A%7B%20%0A%20%3Fpi_id%20%3AHasTechSpec%20%3Fts_id%20.%0A%20%3Fprod_id%20%3ASupports%20%3Fts_id%20.%0A%20%3Fts_id%20%3AVoltage%20%3Fx%20.%0A%20%3Fts_id%20%3AAmperage%20%3Fy%20.%0A%20%3Fts_id%20%3ATip_length%20%3Fz%20.%0A%20%3Fprod_id%20%3AProduct_name%20%3Fpname%20.%0A%20%3Fpi_id%20%3APi_asin%20%22" + arg0[0] + "%22%20.%0A%7D%0AORDER%20BY%20%3Fpname&limit=100&infer=true&";
            System.out.println(url);
            String result = "";
            try {
                result = HttpUtils.doGet(url);
            }catch(RuntimeException rt){

            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            p.dismiss();

            System.out.println(result);


            Document doc = null;
            String product_id = "";
            String dname = "";
            String x= "";
            String y = "";
            String z = "";
            String image = "";

            techspecs = new ArrayList<TechSpec>();

            if (null != result && !"".equals(result)) {

                try {
                    doc = DocumentHelper.parseText(result);
                    Element rootElt = doc.getRootElement();
                    Iterator iter = rootElt.elementIterator("results");
                    while (iter.hasNext()) {
                        Element resultRecord = (Element) iter.next();
                        Iterator itersElIterator = resultRecord.elementIterator("result");
                        if (!itersElIterator.hasNext()) {
                            shownorecord();
                            break;
                        }
                        while (itersElIterator.hasNext()) {
                            Element itemEle = (Element) itersElIterator.next();
                            Iterator literLIterator = itemEle.elementIterator("binding");
                            while (literLIterator.hasNext()) {
                                Element ele = (Element) literLIterator.next();
                                if ("pname".equals(ele.attributeValue("name"))) {
                                    dname = ele.elementTextTrim("literal");
                                } else if ("x".equals(ele.attributeValue("name"))) {
                                    x = ele.elementTextTrim("literal");
                                } else if ("y".equals(ele.attributeValue("name"))) {
                                    y = ele.elementTextTrim("literal");
                                } else if ("z".equals(ele.attributeValue("name"))) {
                                    z = ele.elementTextTrim("literal");
                                } else if ("prod_id".equals(ele.attributeValue("name"))) {
                                    product_id = ele.elementTextTrim("uri");
                                }
                            }
                            techspecs.add(new TechSpec(x, y, z));

                        }
                    }

                    if (null != product_id && !"".equals(product_id) && null != image && !"".equals(image)) {
                        String url = "http://frodo.digidave.co.uk/files/images/product/" + DigitUtil.getNumericPid(product_id) + "/4/" + image;
                        ImageLoader.getInstance().displayImage(url, product_photo);
                    }


                    pro_name.setText(dname);

                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }else{
                shownorecord();
            }

        }
    }
    public void shownorecord() {
        View listview = findViewById(R.id.scroll);
        if (null != listview) {

            View no_result = getLayoutInflater().inflate(R.layout.no_results, layout, false);

            listview.setVisibility(View.GONE);

            layout.addView(no_result);

        }
    }
}
