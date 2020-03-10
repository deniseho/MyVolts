package com.example.cassie.myvolts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.cassie.myvolts.db.DbHelp;
import com.example.cassie.myvolts.dto.ProductData;
import com.example.cassie.myvolts.testing.TestingBean;
import com.example.cassie.myvolts.util.TestUtil;
import com.flyco.dialog.entity.DialogMenuItem;

import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.cassie.myvolts.SearchActivity.sDefSystemLang;

public class ScannerActivity extends AppCompatActivity {

    @BindView(R.id.product_name)
    TextView pro_name;
    @BindView(R.id.link)
    TextView prod_link;
    @BindView(R.id.techspec_textview)
    TextView techSpecTextView;
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

    private ArrayList<DialogMenuItem> mMenuItems = new ArrayList<>();

    private String prod_id;
    private String prod_file;
    private String prod_desc;
    private String url;

    private DbHelp dbHelp;
    public String baseUrl = "";
    private static final String TAG = "ScannerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("base64", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        ProductData pdata = (ProductData) getIntent().getExtras().getSerializable("product");
        String made = getIntent().getStringExtra("made");
        String model = getIntent().getStringExtra("model");

        prod_id = pdata.getProductId();
        dbHelp = new DbHelp(this);
       if(pdata != null && prod_id != null && !prod_id.equals("")) {
           pro_name.setText(pdata.getName());
           prod_id = pdata.getProductId();
           prod_file = pdata.getFile();
           prod_desc = pdata.getDesc();
       } else{
            shownorecord();
        }

//        testTask1();
        switch (sDefSystemLang){
            case "en":
                baseUrl = "http://myvolts.co.uk";
                url = dbHelp.getUrlByPid(prod_id, "mv_uk", made, model);
                break;
            case "de":
                baseUrl = "http://myvolts.de";
                url = dbHelp.getUrlByPid(prod_id, "mv_de", made, model);
                break;
            case "us":
                baseUrl = "http://myvolts.com";
                url = dbHelp.getUrlByPid(prod_id, "mv_us", made, model);
                break;
        }
        prod_link.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent browser= new Intent(Intent.ACTION_VIEW, Uri.parse(baseUrl + "/product/" + url));
                startActivity(browser);
            }
        });

        new GetProductImage(product_photo)
                .execute(prod_file);

    }

    public class GetProductImage extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public GetProductImage(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

//    private void NormalListDialogNoTitle() {
//        for(TechSpec tech: techspecs){
//            mMenuItems.add(new DialogMenuItem("Vol: " + tech.getVol() + " " + "Amp: "
//                    + tech.getAmp() + " " + "Tip_Length: " + tech.getTip(), R.drawable.ic_tech));
//        }
//        final NormalListDialog dialog = new NormalListDialog(mContext, mMenuItems);
//        dialog.title(prod_desc)
//                .isTitleShow(true)
//                .itemPressColor(Color.parseColor("#85D3EF"))
//                .itemTextColor(Color.parseColor("#303030"))
//                .itemTextSize(10)
//                .cornerRadius(2)
//                .widthScale(0.75f)
//                .show();
//
//    }


    public void testTask1(){
        System.out.println(sharedPreferences.getInt("task", 0));
        if(null != sharedPreferences){
            int taskId = sharedPreferences.getInt("task", 0);
                if(1 == taskId || 2 == taskId || 3 == taskId || 4 == taskId){
                    //isDisplay();
                    TestingBean bean = TestUtil.getAllTestingResults(sharedPreferences);
                    dbHelp.saveTestData(bean.getTaskId(),bean.getDeviceClick(),bean.getBrandClick(),bean.getModelClick(),bean.getSearchBoxClick(),
                            bean.getSearchBtnClick(),bean.getHistoryClck(),bean.getResultListClick(),bean.getSearchAcivtityListClick(),bean.getWorktime(),bean.getScanButtonClick());
                    System.out.println(TestUtil.displayResult(sharedPreferences));
                }
        }
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
//                NormalListDialogNoTitle();
                View techspecText = findViewById(R.id.techspec_txt);
                techSpecTextView.setText(prod_desc);
                if (techspecText.getVisibility() == View.GONE) {
                    techspecText.setVisibility(View.VISIBLE);
                    img2.setImageDrawable(getResources().getDrawable((R.drawable.bottom_icon)));
                }else if(techspecText.getVisibility() == View.VISIBLE){
                    techspecText.setVisibility(View.GONE);
                    img2.setImageDrawable(getResources().getDrawable((R.drawable.right_icon)));
                }
                break;
        }
    }

//    public class getproductById extends AsyncTask<String, Void, String> {
//
//        ProgressDialog p = new ProgressDialog(ScannerActivity.this,
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
//
//            String url = "http://theme-e.adaptcentre.ie/openrdf-workbench/repositories/mv2.55/query?action=exec&queryLn=SPARQL&query=PREFIX%20%20%3A%20%3Chttp%3A%2F%2Fmyvolts.com%23%3E%0APREFIX%20owl%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0APREFIX%20rdf%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0APREFIX%20rdfs%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0A%0ASELECT%20%20distinct%20%3Fpname%20%3Fx%20%3Fy%20%3Fz%20%3Fimage%0AWHERE%0A%7B%20%0A%20%3C" + arg0[0] + "%3E%20%3Aproduct_name%20%20%3Fpname%20.%0A%20%3Fprod_id%20%3Asupports%20%3Fts_id%20.%0A%20%3Fts_id%20%3Avoltage%20%3Fx%20.%0A%20%3Fts_id%20%3Aamperage%20%3Fy%20.%0A%20%3Fts_id%20%3Atip_length%20%3Fz%20.%0A%20%3C" + arg0[0] + "%3E%20%3AhasImageFile%20%3Fimage%20.%0A%7D%0A&limit=100&infer=true&";
//            System.out.println(url);
//            String result = "";
//
////            try {
////                result = HttpUtils.doGet(url);
////            }catch (RuntimeException re){
////
////            }
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
//            String pname = "";
//            String x = "";
//            String y = "";
//            String z = "";
//            String image = "";
//            Document doc = null;
//
//            techspecs = new ArrayList<TechSpec>();
//            if (null != result && !"".equals(result)) {
//                try {
//                    doc = DocumentHelper.parseText(result);
//                    Element rootElt = doc.getRootElement();
//                    Iterator iter = rootElt.elementIterator("results");
//
//                    while (iter.hasNext()) {
//                        Element resultRecord = (Element) iter.next();
//                        Iterator itersElIterator = resultRecord.elementIterator("result");
//                        if (!itersElIterator.hasNext()) {
//                            shownorecord();
//                            break;
//                        }
//                        while (itersElIterator.hasNext()) {
//                            Element itemEle = (Element) itersElIterator.next();
//                            Iterator literLIterator = itemEle.elementIterator("binding");
//                            while (literLIterator.hasNext()) {
//                                Element ele = (Element) literLIterator.next();
//                                if ("pname".equals(ele.attributeValue("name"))) {
//                                    pname = ele.elementTextTrim("literal");
//                                } else if ("x".equals(ele.attributeValue("name"))) {
//                                    x = ele.elementTextTrim("literal");
//                                } else if ("y".equals(ele.attributeValue("name"))) {
//                                    y = ele.elementTextTrim("literal");
//                                } else if ("z".equals(ele.attributeValue("name"))) {
//                                    z = ele.elementTextTrim("literal");
//                                } else if ("image".equals(ele.attributeValue("name"))) {
//                                    image = ele.elementTextTrim("literal");
//                                }
//                            }
//                            techspecs.add(new TechSpec(x, y, z));
//                        }
//
//                    }
//                    if (null != prod_id && !"".equals(prod_id) && null != image && !"".equals(image)) {
//                        String url = "http://frodo.digidave.co.uk/files/images/product/" + DigitUtil.getNumericPid(prod_id) + "/4/" + image;
//                        ImageLoader.getInstance().displayImage(url, product_photo);
//                    }
//
//                    pro_name.setText(pname);
//
//
//                } catch (DocumentException e) {
//                    e.printStackTrace();
//                }
//
//            }else{
////                shownorecord();
//            }
//        }
//    }
    public void shownorecord() {
        View listview = findViewById(R.id.scroll);
        if (null != listview) {

            View no_result = getLayoutInflater().inflate(R.layout.no_results, layout, false);

            listview.setVisibility(View.GONE);

            layout.addView(no_result);

            layout.setVisibility(View.VISIBLE);

        }
    }

}
