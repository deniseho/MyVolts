package com.example.cassie.myvolts.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.cassie.myvolts.dto.DeviceData;
import com.example.cassie.myvolts.dto.HistoryData;
import com.example.cassie.myvolts.dto.HotData;
import com.example.cassie.myvolts.dto.ProductData;
import com.example.cassie.myvolts.testing.TestingBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
                String productId = jsonObject.getString("productId");
                newData.add(new ProductData(productId, name, null));
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

                String pi_id = jsonObject.getString("p_id");
                String manufacturer = jsonObject.getString("manufacturer");
                String type = jsonObject.getString("type");
                String name = jsonObject.getString("name");
                String model = jsonObject.getString("model");
                String stech = jsonObject.getString("tech");

                newData.add(new DeviceData(pi_id, manufacturer, type, name, model, stech));
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

    public List<ProductData> getSearchedProducts(String searchStr){
        String value = searchStr.split(",")[2].trim();

        List<ProductData> datas=new ArrayList<>();
        if(mwcdb!=null){
//            Cursor cursor= mwcdb.rawQuery("select * from product where name like '%" + searchStr + "%'",new String[]{});
            Cursor cursor= mwcdb.rawQuery("select  product.name, product.pid from product, device where product.pid = device.pid AND device.model = ?",new String[]{value});

            while(cursor.moveToNext()){
                datas.add(new ProductData(cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("pid"))));
            }
        }
        return datas;
    }

    public List<DeviceData> getSearchedDevices(String pid){
        List<DeviceData> datas=new ArrayList<>();
        if(mwcdb!=null){
//            Cursor cursor= mwcdb.rawQuery("select * from product where name like '%" + searchStr + "%'",new String[]{});
            Cursor cursor= mwcdb.rawQuery("select * from device where pid =" + pid ,new String[]{});
            while(cursor.moveToNext()){
                datas.add(new DeviceData(cursor.getString(cursor.getColumnIndex("pid")),
                        cursor.getString(cursor.getColumnIndex("manufacturer")),
                        cursor.getString(cursor.getColumnIndex("type")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("model")),
                        cursor.getString(cursor.getColumnIndex("tech"))
                ));
            }
        }
        return datas;
    }

    public void saveProductList(List<ProductData> products) {
        ContentValues values = new ContentValues();

        for(int i=0; i<products.size(); i++) {
            ProductData product = products.get(i);
            values.put(FeedReaderContract.FeedEntry.PRODUCT_COLUMN_ID, product.getProductId());
            values.put(FeedReaderContract.FeedEntry.PRODUCT_COLUMN_NAME, product.getName());
            mwcdb.insert(FeedReaderContract.FeedEntry.PRODUCT_TABLE_NAME, null, values);
        }
    }

    public void saveDeviceList(List<DeviceData> devices) {
        ContentValues values = new ContentValues();

        for(int i=0; i<devices.size(); i++) {
            DeviceData device = devices.get(i);
            values.put(FeedReaderContract.FeedEntry.DEVICE_COLUMN_PID, device.getPi_id());
            values.put(FeedReaderContract.FeedEntry.DEVICE_COLUMN_MANU, device.getManufacturer());
            values.put(FeedReaderContract.FeedEntry.DEVICE_COLUMN_NAME, device.getName());
            values.put(FeedReaderContract.FeedEntry.DEVICE_COLUMN_TYPE, device.getType());
            values.put(FeedReaderContract.FeedEntry.DEVICE_COLUMN_MODEL, device.getModel());
            values.put(FeedReaderContract.FeedEntry.DEVICE_COLUMN_TECH, device.getsTech());
            mwcdb.insert(FeedReaderContract.FeedEntry.DEVICE_TABLE_NAME, null, values);
        }
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

    public List<ProductData> searchProducts(String searchStr) {
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
