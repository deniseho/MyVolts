package com.example.cassie.myvolts.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.example.cassie.myvolts.dto.DeviceData;
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
    GetProducts db;

    private DbManager manager = new DbManager();

    public DbHelp(Context context){
        dbHelper = new DbHelper(context);
        mwcdb = dbHelper.getWritableDatabase();
//            deleteProducts();
//            deleteDevices();

//        if(db != null && db.isOpen()) {
//            CacheManu cacheManu = new CacheManu();
//            cacheManu.execute();
//        }
        GetProducts cacheProducts = new GetProducts();
        cacheProducts.execute();

        getInitData("");
    }

    public List<String> getInitData(String input){
        List<String> datas = new ArrayList<>();
        if(mwcdb!=null){
            Cursor cursor= mwcdb.rawQuery("select manufacturer, name, model from device",new String[]{});
            while(cursor.moveToNext()){
                datas.add(cursor.getString(0) + ", " + cursor.getString(1) + ", " + cursor.getString(2)  );
            }
        }
        return datas;
    }


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

    public void saveHis(String name, String isWhole, String productid){
        if(mwcdb!=null){
//            mwcdb.execSQL("delete from his where name=?",new String[]{name});
//            mwcdb.execSQL("insert into his(name, isWhole, productid) " +
//                               "values(?,?,?)",
//                                new String[]{name,iswhole, productid});
            ContentValues contentValues = new ContentValues();
            contentValues.put("name",String.valueOf(name));
            contentValues.put("isWhole",String.valueOf(isWhole));
            contentValues.put("productid",String.valueOf(productid));
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

//    public class GetDevices extends AsyncTask<Object, Void, JSONArray> {
//
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//        }
//
//
//        @Override
//        protected JSONArray doInBackground(Object... arg0) {
//            // TODO Auto-generated method stub
//            JSONArray output_arr = new JSONArray();
//
//
//
//            String url = "http://frodo.digidave.co.uk/api/RipApp/result.php?start=0&limit=3"; //"http://api.myjson.com/bins/1hcph0";
//            String result = HttpUtils.doGet(url);
//
//
//            try {
//                JSONObject obj = new JSONObject(result);
//                JSONArray mv_db_arr = obj.getJSONArray("mv_db");
//
//                for(int i=0; i<mv_db_arr.length(); i++) {
//                    JSONArray itemArray = mv_db_arr.getJSONArray(i);
//                    JSONObject item = itemArray.getJSONObject(0);
//
//                    Iterator<String> keys = item.keys();
//
//                    String category = keys.next();
//                    if(category.equals("device")){
//                        String device = item.optString(category);
//                        output_arr.put(device);
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return output_arr;
//        }
//
//        @Override
//        protected void onPostExecute(JSONArray result) {
//
//
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//
//            List<DeviceData> newData = new ArrayList<>();
//
//            try {
//                for(int i=0; i<result.length(); i++){
//                    JSONObject jsonObject = new JSONObject(result.getString(i));
//
//                    String p_id = jsonObject.getString("p_id");
//                    String manufacturer = jsonObject.getString("manufacturer");
//                    String name = jsonObject.getString("name");
//                    String model = jsonObject.getString("model");
//                    String mv_uk = jsonObject.getString("mv_uk");
//                    String mv_de = jsonObject.getString("mv_de");
//                    String mv_us = jsonObject.getString("mv_us");
//
//                    newData.add(new DeviceData(p_id, manufacturer, name, model, mv_uk, mv_de, mv_us));
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            saveDeviceToDB(newData);
//        }
//    }

    public void saveDeviceToDB(List<DeviceData> devices) {
        ContentValues values = new ContentValues();

        for(int i=0; i<devices.size(); i++) {
            DeviceData device = devices.get(i);
            values.put(FeedReaderContract.FeedEntry.DEVICE_COLUMN_PID, device.getP_id());
            values.put(FeedReaderContract.FeedEntry.DEVICE_COLUMN_MANU, device.getManufacturer());
            values.put(FeedReaderContract.FeedEntry.DEVICE_COLUMN_NAME, device.getName());
            values.put(FeedReaderContract.FeedEntry.DEVICE_COLUMN_MODEL, device.getModel());
            values.put(FeedReaderContract.FeedEntry.DEVICE_COLUMN_MV_UK, device.getMv_uk());
            values.put(FeedReaderContract.FeedEntry.DEVICE_COLUMN_MV_DE, device.getMv_de());
            values.put(FeedReaderContract.FeedEntry.DEVICE_COLUMN_MV_US, device.getMv_us());
            mwcdb.insert(FeedReaderContract.FeedEntry.DEVICE_TABLE_NAME, null, values);
        }
    }



    public class GetProducts extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub

            String url = "http://frodo.digidave.co.uk/api/RipApp/result.php?start=0&limit=3"; //"http://api.myjson.com/bins/1hcph0";
            String result = HttpUtils.doGet(url);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            // TODO Auto-generated method stub
            super.onPostExecute(result);

            JSONArray product_arr = new JSONArray();
            JSONArray device_arr = new JSONArray();
            List<ProductData> newProductData = new ArrayList<>();
            List<DeviceData> newDeviceData = new ArrayList<>();

            try {
                JSONObject obj = new JSONObject(result);
                JSONArray mv_db_arr = obj.getJSONArray("mv_db");

                if(mv_db_arr != null ){
                    for(int i=0; i<mv_db_arr.length(); i++) {
                        JSONArray itemArray = mv_db_arr.getJSONArray(i);
                        JSONObject item = itemArray.getJSONObject(0);

                        Iterator<String> keys = item.keys();
                        String category = keys.next();

                        if(category.equals("product")){
                            String product = item.optString(category);
                            product_arr.put(product);
                        }

                        if(category.equals("device")){
                            String device = item.optString(category);
                            device_arr.put(device);
                        }
                    }
                }

                for(int i=0; i<product_arr.length(); i++){
                    JSONObject productObject = new JSONObject(product_arr.getString(i));

                    String name = productObject.getString("name");
                    String productId = productObject.getString("productId");
                    String desc = productObject.getString("desc");
                    String file = productObject.getString("file");
                    newProductData.add(new ProductData(name, desc, file, productId));
                }


                for(int i=0; i<device_arr.length(); i++) {
                    JSONObject deviceObject = new JSONObject(device_arr.getString(i));
                    String p_id = deviceObject.getString("p_id");
                    String manufacturer = deviceObject.getString("manufacturer");
                    String name = deviceObject.getString("name");
                    String model = deviceObject.getString("model");
                    String mv_uk = deviceObject.getString("mv_uk");
                    String mv_de = deviceObject.getString("mv_de");
                    String mv_us = deviceObject.getString("mv_us");

                    newDeviceData.add(new DeviceData(p_id, manufacturer, name, model, mv_uk, mv_de, mv_us));
                }

                } catch (JSONException e) {
                e.printStackTrace();
            }

            saveProductToDB(newProductData);
            saveDeviceToDB(newDeviceData);
        }
    }

    public void saveProductToDB(List<ProductData> products) {
        ContentValues values = new ContentValues();

        for(int i=0; i<products.size(); i++) {
            ProductData product = products.get(i);
            values.put(FeedReaderContract.FeedEntry.PRODUCT_COLUMN_ID, product.getProductId());
            values.put(FeedReaderContract.FeedEntry.PRODUCT_COLUMN_NAME, product.getName());
            values.put(FeedReaderContract.FeedEntry.PRODUCT_COLUMN_DESC, product.getFile());
            values.put(FeedReaderContract.FeedEntry.PRODUCT_COLUMN_FILE, product.getFile());
            mwcdb.insert(FeedReaderContract.FeedEntry.PRODUCT_TABLE_NAME, null, values);
        }
    }


    public List<ProductData> getProductData(String result) {
        JSONArray output_arr = new JSONArray();
        List<ProductData> newData = new ArrayList<>();

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

            for(int i=0; i<output_arr.length(); i++){
                JSONObject jsonObject = new JSONObject(output_arr.getString(i));

                String name = jsonObject.getString("name");
                String desc = jsonObject.getString("desc");
                String file = jsonObject.getString("file");
                String productId = jsonObject.getString("productId");

                newData.add(new ProductData(name, desc, file, productId));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newData;
    }



    public List<DeviceData> getAllDeviceData(String result){
        JSONArray output_arr = new JSONArray();
        List<DeviceData> newData = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(result);
            JSONArray mv_db_arr = obj.getJSONArray("mv_db");
            JSONArray mv_db_arr2 = mv_db_arr.getJSONArray(0);


            for(int i=0; i<mv_db_arr2.length(); i++) {

                JSONObject item = (JSONObject) mv_db_arr2.get(i);
                Iterator<String> keys = item.keys();

                String category = keys.next();
                if(category.equals("device")){
                    String category_val = item.optString(category);
                    output_arr.put(category_val);
                }
            }

            for(int i=0; i<output_arr.length(); i++){
                JSONObject jsonObject = new JSONObject(output_arr.getString(i));

                String p_id = jsonObject.getString("p_id");
                String manufacturer = jsonObject.getString("manufacturer");
                String name = jsonObject.getString("name");
                String model = jsonObject.getString("model");
                String mv_uk = jsonObject.getString("mv_uk");
                String mv_de = jsonObject.getString("mv_de");
                String mv_us = jsonObject.getString("mv_us");

                newData.add(new DeviceData(p_id, manufacturer, name, model, mv_uk, mv_de, mv_us));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newData;
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

    public List<ProductData> getProductListByPid(String pid){
        List<ProductData> datas=new ArrayList<>();
        if(mwcdb!=null){
            Cursor cursor= mwcdb.rawQuery("select distinct p.productId, p.name, p.desc, p.file from product as p, device  as d where p.productId in (select p_id from device where device.manufacturer = ? )",new String[]{pid});
            while(cursor.moveToNext()){
                datas.add(new ProductData(cursor.getString(cursor.getColumnIndex("name")),
                cursor.getString(cursor.getColumnIndex("desc")),
                cursor.getString(cursor.getColumnIndex("file")),
                cursor.getString(cursor.getColumnIndex("productId"))));
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

}
