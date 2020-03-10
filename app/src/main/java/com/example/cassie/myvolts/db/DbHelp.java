package com.example.cassie.myvolts.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

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

    private static final String TAG = "DbHelp";


    private DbManager manager = new DbManager();
    List<ProductData> products = new ArrayList<>();
    List<DeviceData> devices = new ArrayList<>();

    public DbHelp(Context context){
        dbHelper =  new DbHelper(context);
        mwcdb = dbHelper.getWritableDatabase();

        if(mwcdb == null && mwcdb.isOpen()) {
        }

    }

    public void getInitData(){
        deleteProducts();
        deleteDevices();
        loadData();
        saveProductToDB();
        saveDeviceToDB();
    }

    public void loadData() {
        LoadData data = new LoadData();
        data.execute();
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
            mwcdb.execSQL("delete from his where name=?",new String[]{name});
            mwcdb.execSQL("insert into his(name, isWhole, productid) " +
                               "values(?,?,?)",
                                new String[]{name,isWhole, productid});
        }
    }

    public void saveTestData(int taskId, int deviceClick, int brandClick, int modelClick,int searchBoxClick,int searchBtnClick,int historyClck, int resultListClick, int searchAcivtityListClick,double worktime, int scanButtonClick){
        if(mwcdb!=null){
            mwcdb.execSQL("insert into test(taskId, deviceClick, brandClick, modelClick, searchBoxClick, searchBtnClick, " +
                    "historyClck, resultListClick, searchAcivtityListClick, worktime, scanButtonClick) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?)",
                    new String[]{String.valueOf(taskId),
                            String.valueOf(deviceClick),
                            String.valueOf(brandClick),
                            String.valueOf(modelClick),
                            String.valueOf(searchBoxClick),
                            String.valueOf(searchBtnClick),
                            String.valueOf(historyClck),
                            String.valueOf(resultListClick),
                            String.valueOf(searchAcivtityListClick),
                            String.valueOf(worktime),
                            String.valueOf(scanButtonClick)});
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

    public String getUrlByPid(String pid, String country, String made, String model) {
        String url = "";

        if(mwcdb!=null){
            Cursor cursor= mwcdb.rawQuery("select " + country + " from device where p_id =? and manufacturer=? and model=?", new String[]{ pid, made, model});
            while(cursor.moveToNext()){
                url = cursor.getString(0);
            }
        }
        Log.v(TAG,"url: " + url);

        return url;
    }

    public void saveDeviceToDB() {
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



    private class LoadData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub

            String url = "http://frodo.digidave.co.uk/api/RipApp/result.php?start=0&limit=40";
            String result = HttpUtils.doGet(url);

            if(result != null){

                JSONArray product_arr = new JSONArray();
                JSONArray device_arr = new JSONArray();

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
                        products.add(new ProductData(name, desc, file, productId));
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

                        devices.add(new DeviceData(p_id, manufacturer, name, model, mv_uk, mv_de, mv_us));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            // TODO Auto-generated method stub
            super.onPostExecute(result);

            saveProductToDB();
            saveDeviceToDB();

        }

    }

    public void saveProductToDB() {
        ContentValues values = new ContentValues();

        for(int i=0; i<products.size(); i++) {
            ProductData product = products.get(i);
            values.put(FeedReaderContract.FeedEntry.PRODUCT_COLUMN_ID, product.getProductId());
            values.put(FeedReaderContract.FeedEntry.PRODUCT_COLUMN_NAME, product.getName());
            values.put(FeedReaderContract.FeedEntry.PRODUCT_COLUMN_DESC, product.getDesc());
            values.put(FeedReaderContract.FeedEntry.PRODUCT_COLUMN_FILE, product.getFile());
            mwcdb.insert(FeedReaderContract.FeedEntry.PRODUCT_TABLE_NAME, null, values);
        }
    }

    public List<String> getAutoCompleteOptions(){
        List<String> datas = new ArrayList<>();
        if(mwcdb!=null){
            Cursor cursor= mwcdb.rawQuery("select manufacturer, name, model from device",new String[]{});
            while(cursor.moveToNext()){
                datas.add(cursor.getString(0) + ", " + cursor.getString(1) + ", " + cursor.getString(2)  );
            }
        }
        return datas;
    }

    public List<String> getPidByDevice(String made, String model) {
        List<String> datas=new ArrayList<>();

        if(mwcdb!=null){
            Cursor cursor= mwcdb.rawQuery("select DISTINCT p_id from device where device.manufacturer=? or device.model =?",new String[]{made, model});

            while(cursor.moveToNext()){
                datas.add(cursor.getString(0));
            }

        }
        return datas;
    }

    public ProductData getProductById(String pid){
        ProductData data = new ProductData();
        if(mwcdb!=null){
            Cursor cursor= mwcdb.rawQuery("select name,desc, file,productId  from product where productId = ?",new String[]{pid});

            while(cursor.moveToNext()){
                data = new ProductData(cursor.getString(cursor.getColumnIndex("name")),
                cursor.getString(cursor.getColumnIndex("desc")),
                cursor.getString(cursor.getColumnIndex("file")),
                cursor.getString(cursor.getColumnIndex("productId")));
            }

        }
        return data;
    }

//    public List<DeviceData> getDevicesList(String pid){
//        List<DeviceData> datas=new ArrayList<>();
//        if(mwcdb!=null){
//            Cursor cursor= mwcdb.rawQuery("select * from device where p_id = ?",new String[]{pid});
//            while(cursor.moveToNext()){
//                datas.add(new DeviceData(cursor.getString(cursor.getColumnIndex("p_id")),
//                cursor.getString(cursor.getColumnIndex("manufacturer")),
//                cursor.getString(cursor.getColumnIndex("name")),
//                cursor.getString(cursor.getColumnIndex("model")),
//                cursor.getString(cursor.getColumnIndex("mv_uk")),
//                cursor.getString(cursor.getColumnIndex("mv_de")),
//                cursor.getString(cursor.getColumnIndex("mv_us"))));
//            }
//        }
//        return datas;
//    }

    public String getProductIdByName(String name){
        String id = "";
        if(mwcdb!=null){
            Cursor cursor= mwcdb.rawQuery("select pid from product where name=?",new String[]{name});
            while(cursor.moveToNext()){
                id = cursor.getString(0);
            }
        }
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
            mwcdb.execSQL("delete from product");
        }
    }

    public void deleteDevices(){
        if(mwcdb!=null){
            mwcdb.execSQL("delete from device");
        }
    }

}
