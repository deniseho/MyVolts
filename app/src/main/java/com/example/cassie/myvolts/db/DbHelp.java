package com.example.cassie.myvolts.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.cassie.myvolts.dto.HistoryData;
import com.example.cassie.myvolts.dto.HotData;
import com.example.cassie.myvolts.dto.ProductData;
import com.example.cassie.myvolts.testing.TestingBean;
import com.example.cassie.myvolts.util.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DbHelp{
    private File dbfile;
    private Context context;
    private SQLiteDatabase mwcdb=null;

    private DbHelper dbHelper;

    private DbManager manager = new DbManager();

    public DbHelp(Context context){
        dbHelper = new DbHelper(context);
        mwcdb = dbHelper.getWritableDatabase();
    }

    //----- initTestData  ---------
    public void initTestData(){
//        if(mwcdb!=null){
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("testData", "Sony DVD player DVP-FX720 Compatible Power Supply Cable & in Car Charger");
//            contentValues.put("testData", "Korg Tuner Pitchblack Compatible Power Supply Cable & in Car Charger");
//            contentValues.put("testData", "Korg PSU part KA-183 Compatible Power Supply Cable & in Car Charger");
//            contentValues.put("testData", "Dymo Label printer LT-100H Compatible Power Supply Plug Charger");
//            contentValues.put("testData", "Seagate PSU part FreeAgent 9NK2AE-500 Compatible Power Supply Plug Charger");
//
///            mwcdb.insert("initTestData", null, contentValues);
//        }

//        new fetchData().execute();

    }

    public List<String> getInitTestData(String searchText){
        List<String> datas = new ArrayList<>();

        //todo: select from database
        datas.add("Sony DVD player DVP-FX720 Compatible Power Supply Cable & in Car Charger");
        datas.add("Korg Tuner Pitchblack Compatible Power Supply Cable & in Car Charger");
        datas.add("Korg PSU part KA-183 Compatible Power Supply Cable & in Car Charger");
        datas.add("Dymo Label printer LT-100H Compatible Power Supply Plug Charger");
        datas.add("Seagate PSU part FreeAgent 9NK2AE-500 Compatible Power Supply Plug Charger");

//        if(mwcdb!=null){
//            Cursor cursor= mwcdb.rawQuery("select testData from initTestData",new String[]{});
//            while(cursor.moveToNext()){
//                datas.add(cursor.getString(1));
//            }
//        }

        return datas;
    }


    //--------------------------------------

    public List<HistoryData> getHisData(){
        List<HistoryData> datas=new ArrayList<>();
        if(mwcdb!=null){
            Cursor cursor= mwcdb.rawQuery("select name, isWhole, productid from his order by id desc limit 20",new String[]{});
            while(cursor.moveToNext()){
                datas.add(new HistoryData(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            }
        }
        return datas;
    }

    public List<String> getAllHisName(){
        List<java.lang.String> datas=new ArrayList<>();
        if(mwcdb!=null){
            Cursor cursor= mwcdb.rawQuery("select name from his order by id desc",new java.lang.String[]{});
            while(cursor.moveToNext()){
                datas.add(cursor.getString(0));
            }
        }
        return datas;
    }

    public void saveHis(String name, String iswhole, String productid){
        if(mwcdb!=null){
            mwcdb.execSQL("delete from his where name=?",new String[]{name});
//            mwcdb.execSQL("insert into his(name, isWhole, productid) " +
//                               "values(?,?,?)",
//                                new String[]{name,iswhole, productid});

            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("isWhole", iswhole);
            contentValues.put("productid", productid);
            mwcdb.insert("his", null, contentValues);
        }
    }

    public void saveTestData(int taskId, int deviceClick, int brandClick, int modelClick,int searchBoxClick,int searchBtnClick,int historyClck, int resultListClick, int searchAcivtityListClick,double worktime, int scanButtonClick){
        if(mwcdb!=null){
//            mwcdb.execSQL("insert into test(taskId, deviceClick, brandClick, modelClick, searchBoxClick, searchBtnClick, " +
//                    "historyClck, resultListClick, searchAcivtityListClick, worktime, scanButtonClick) " +
//                    "values(?,?,?,?,?,?,?,?,?,?,?)",
//                    new String[]{String.valueOf(taskId),
//                            String.valueOf(deviceClick),
//                            String.valueOf(brandClick),
//                            String.valueOf(modelClick),
//                            String.valueOf(searchBoxClick),
//                            String.valueOf(searchBtnClick),
//                            String.valueOf(historyClck),
//                            String.valueOf(resultListClick),
//                            String.valueOf(searchAcivtityListClick),
//                            String.valueOf(worktime),
//                            String.valueOf(scanButtonClick)});
            ContentValues contentValues = new ContentValues();
            contentValues.put("taskId",String.valueOf(taskId));
            contentValues.put("deviceClick",String.valueOf(deviceClick));
            contentValues.put("brandClick",String.valueOf(brandClick));
            contentValues.put("modelClick",String.valueOf(modelClick));
            contentValues.put("searchBoxClick",String.valueOf(searchBoxClick));
            contentValues.put("searchBtnClick",String.valueOf(searchBtnClick));
            contentValues.put("historyClck",String.valueOf(historyClck));
            contentValues.put("resultListClick",String.valueOf(resultListClick));
            contentValues.put("searchAcivtityListClick",String.valueOf(searchAcivtityListClick));
            contentValues.put("worktime",String.valueOf(worktime));
            contentValues.put("scanButtonClick",String.valueOf(scanButtonClick));
            mwcdb.insert("test", null, contentValues);
        }
    }

    public void listTesting(){
        List<TestingBean> bean = new ArrayList<>();
        if(mwcdb!=null){
            Cursor cursor= mwcdb.rawQuery("select taskId, deviceClick, brandClick, modelClick, searchBoxClick, searchBtnClick, " +
                    "historyClck, resultListClick, searchAcivtityListClick, worktime, scanButtonClick from test",new String[]{});
            while(cursor.moveToNext()){
                bean.add(new TestingBean(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)),Integer.parseInt(cursor.getString(2)),Integer.parseInt(cursor.getString(3)),Integer.parseInt(cursor.getString(4)),Integer.parseInt(cursor.getString(5)),
                        Integer.parseInt(cursor.getString(6)),Integer.parseInt(cursor.getString(7)),Integer.parseInt(cursor.getString(8)),Double.parseDouble(cursor.getString(9)),Integer.parseInt(cursor.getString(10))));
            }
           for(TestingBean tests:bean) {
               System.out.println(tests.toString());
           }
        }
    }

    public List<String> getAllProductName(){
        List<String> datas=new ArrayList<>();
        if(mwcdb!=null){
            Cursor cursor= mwcdb.rawQuery("select name from product order by name",new String[]{});
            while(cursor.moveToNext()){
                datas.add(cursor.getString(0));
            }
        }
        return datas;
    }

    public String getProductIdByName(String name){
        String id = "";
//        if(mwcdb!=null){
//            Cursor cursor= mwcdb.rawQuery("select pid from product where name=?",new String[]{name});
//            while(cursor.moveToNext()){
//                id = cursor.getString(0);
//            }
//        }
        return id;
    }

    public List<ProductData> searchProduct(String st, String made, String type){
        List<ProductData> datas=new ArrayList<>();
        if(mwcdb!=null){
            String sql="select * from product where 1=1";
            if(!TextUtils.isEmpty(st)){
                sql+=" and name like '%"+st+"%'";
            }
            if(!TextUtils.isEmpty(made)){
                sql+=" and made = '"+made+"'";
            }
            if(!TextUtils.isEmpty(type)){
                sql+=" and type = '"+type+"'";
            }
            Cursor cursor= mwcdb.rawQuery(sql,new String[]{});
            while(cursor.moveToNext()){
                //datas.add(new ProductData(cursor.getString(cursor.getColumnIndex("name")),cursor.getString(cursor.getColumnIndex("made")),cursor.getString(cursor.getColumnIndex("type"))));
            }
        }
        return datas;
    }

    public List<String> getMade(){
        List<String> datas=new ArrayList<>();
        if(mwcdb!=null){
            //Cursor cursor= mwcdb.rawQuery("select DISTINCT name from made order by name",new String[]{});
            Cursor cursor= mwcdb.rawQuery("select DISTINCT name from manu order by name",new String[]{});
            while(cursor.moveToNext()){
                System.out.println("dsf" + cursor.getString(0));
                datas.add(cursor.getString(0));
            }
        }
        return datas;
    }
    public List<String> getType(String made){
        List<String> datas=new ArrayList<>();
        if(mwcdb!=null){
            Cursor cursor= mwcdb.rawQuery("select DISTINCT type from made where name=? order by type",new String[]{made});
            while(cursor.moveToNext()){
                datas.add(cursor.getString(0));
            }
        }
        return datas;
    }

    public List<HotData> getHot(){
        List<HotData> datas=new ArrayList<>();
        if(mwcdb!=null){
            String sql="select * from hot where 1=1";

            Cursor cursor= mwcdb.rawQuery(sql,new String[]{});
            while(cursor.moveToNext()){
                datas.add(new HotData(cursor.getString(cursor.getColumnIndex("name")),cursor.getString(cursor.getColumnIndex("price"))));
            }
        }
        return datas;
    }

    public List<ProductData> serachProducts(String searchStr) {
        List<ProductData> productData = new ArrayList<ProductData>();
        String param = "%" + searchStr + "%";
        Cursor cursor= mwcdb.rawQuery("SELECT * FROM product WHERE name LIKE '" + param + "'", null);

        if(cursor.moveToFirst()) {
            do {
//                String id = cursor.getString(0);
                String pid = cursor.getString(1);
                String name = cursor.getString(2);

                productData.add(new ProductData(pid, name, null));

            } while (cursor.moveToNext());

            cursor.close();
            mwcdb.close();
        }

        return productData;
    }

    public JSONArray getProductsFromApi(String searchStr){
        JSONArray output_arr = new JSONArray();
        String result = "";

        if(searchStr != null && !searchStr.equals("")) {
            String url = "http://api.myjson.com/bins/1hcph0"; //"http://theme-e.adaptcentre.ie/openrdf-workbench/repositories/mv2.54/query?action=exec&queryLn=SPARQL&query=PREFIX%20%20%3A%20%3Chttp%3A%2F%2Fmyvolts.com%23%3E%0APREFIX%20owl%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0APREFIX%20rdf%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0APREFIX%20rdfs%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0ASELECT%20%20distinct%20%3Fprod_id%20%20%3Fpname%20%3Ftype%20%0AWHERE%20%0A%7B%20%0A%20%3Fprod_id%20%3Aproduct_name%20%3Fpname%20.%0A%20%3Fprod_id%20%3AisOfTypeCategory%20%3Ftype%20.%0A%0A%20filter%20(regex(%3Fpname%2C%20%22" + arg0[0] + "%22%2C%20%22i%22)" + args + ")%20.%0A%7D%0Aorder%20by%20%3Fpname%0ALIMIT%2010"+ offset +"&limit=100&infer=true&";

            result = HttpUtils.doGet(url);
            System.out.println("=======getProductsFromApi result: ======" + result);


            try {
                JSONObject obj = new JSONObject(result);
                JSONArray mv_db_arr = obj.getJSONArray("mv_db");
                JSONArray mv_db_arr2 = mv_db_arr.getJSONArray(0);


                for(int i=0; i<mv_db_arr2.length(); i++) {

                    JSONObject item = (JSONObject) mv_db_arr2.get(i);
                    Iterator<String> keys = item.keys();

                    String category = keys.next();
                    if(category.equals("product")){
                        String category_val = item.optString(category);
                        output_arr.put(category_val);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return output_arr;
    }



    public void clearHis(){
        if(mwcdb!=null){
            mwcdb.execSQL("delete from his");
        }
    }

    public void deleteHisByName(String name){
        if(mwcdb!=null){
            mwcdb.execSQL("delete from his where name=?", new String[]{name});
        }
    }

    public void deleteProducts(){
        if(mwcdb!=null){
            mwcdb.execSQL("delete from products");
        }
    }
/*

    public void addInitManuData(ManufactorData manu){
        if(mwcdb!=null) {
            long insert = mwcdb.insert(FeedReaderContract.FeedEntry.TABLE_NAME_MANU, null, manager.generateManuValues(manu));
        }
    }
*/

//    public String searchCable(String searchInput){
//        if(mwcdb!=null){
//            mwcdb.execSQL(("select * from "));
//        }
//
//        return "";
//    }

}
