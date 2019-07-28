package com.example.cassie.myvolts.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cassie on 23/05/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "mwc.db";

    private static DbHelper dbHelp=null;
    private DbManager manager = new DbManager();

    public SQLiteDatabase db;

    private static final String SQL_CREATE_PRODUCT_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.PRODUCT_TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.PRODUCT_COLUMN_ID + " TEXT," +
                    FeedReaderContract.FeedEntry.PRODUCT_COLUMN_NAME + " TEXT)";

    private static final String SQL_CREATE_DEVICE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.DEVICE_TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.DEVICE_COLUMN_PID + " TEXT," +
                    FeedReaderContract.FeedEntry.DEVICE_COLUMN_MANU + " TEXT," +
                    FeedReaderContract.FeedEntry.DEVICE_COLUMN_NAME + " TEXT," +
                    FeedReaderContract.FeedEntry.DEVICE_COLUMN_TYPE + " TEXT," +
                    FeedReaderContract.FeedEntry.DEVICE_COLUMN_MODEL + " TEXT," +
                    FeedReaderContract.FeedEntry.DEVICE_COLUMN_TECH + " TEXT)";


    private static final String SQL_CREATE_HIS_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME_HISTORY + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.HISTORY_COLUMN_NAME_HISTORY_NAME + " TEXT," +
                    FeedReaderContract.FeedEntry.HISTORY_COLUMN_NAME_HISTORY_ISWHOLE + " TEXT," +
                    FeedReaderContract.FeedEntry.HISTORY_COLUMN_NAME_HISTORY_PRODUCTID + " TEXT)";

    private static final String SQL_CREATE_MADE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME_MADE + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.MADE_COLUMN_NAME_NAME + " TEXT," +
                    FeedReaderContract.FeedEntry.MADE_COLUMN_NAME_TYPE + " TEXT)";

    private static final String SQL_CREATE_MANU_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME_MANU + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.MAMU_COLUMN_NAME_NAME + " TEXT)";

    private static final String SQL_CREATE_TEST_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME_TESTING + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.TEST_COLUMN_NAME_TASKID + " TEXT," +
                    FeedReaderContract.FeedEntry.TEST_COLUMN_NAME_DEVICECLICK + " TEXT," +
                    FeedReaderContract.FeedEntry.TEST_COLUMN_NAME_BRANDCLICK + " TEXT," +
                    FeedReaderContract.FeedEntry.TEST_COLUMN_NAME_MODELCLICK + " TEXT," +
                    FeedReaderContract.FeedEntry.TEST_COLUMN_NAME_SEARCHBOXCLICK + " TEXT," +
                    FeedReaderContract.FeedEntry.TEST_COLUMN_NAME_SEARCHBTNCLICK + " TEXT," +
                    FeedReaderContract.FeedEntry.TEST_COLUMN_NAME_HISTORYCLICK + " TEXT," +
                    FeedReaderContract.FeedEntry.TEST_COLUMN_NAME_RESULTLISTCLICK + " TEXT," +
                    FeedReaderContract.FeedEntry.TEST_COLUMN_NAME_SEARCHLISTACTIVITYLISTCLICK + " TEXT," +
                    FeedReaderContract.FeedEntry.TEST_COLUMN_NAME_WORKTIME + " TEXT," +
                    FeedReaderContract.FeedEntry.TEST_COLUMN_NAME_SCANBTNCLICK + " TEXT)";



    private static final String SQL_DELETE_PRODUCT_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.PRODUCT_TABLE_NAME;

    private static final String SQL_DELETE_DEVICE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.DEVICE_TABLE_NAME;

    private static final String SQL_DELETE_HIS_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME_HISTORY;

    private static final String SQL_DELETE_MADE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME_MADE;

    private static final String SQL_DELETE_MANU_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME_MANU;

    private static final String SQL_DELETE_TEST_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME_TESTING;

    // private static final String SQL_INSERT_P1="INSERT INTO product VALUES (?,?,?)";

    private static final String SQL_SELECT_PRODUCT =
            "SELECT * FROM PRODUCT";


    public static DbHelper getDbHelp(Context context){
        if(dbHelp==null){
            dbHelp=new DbHelper(context);
        }
        return dbHelp;
    }

    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PRODUCT_ENTRIES);
        db.execSQL(SQL_CREATE_DEVICE_ENTRIES);
        db.execSQL(SQL_CREATE_HIS_ENTRIES);
        db.execSQL(SQL_CREATE_MADE_ENTRIES);
        db.execSQL(SQL_CREATE_MANU_ENTRIES);
        db.execSQL(SQL_CREATE_TEST_ENTRIES);

//        this.db = db;
//
//        if(db != null && db.isOpen()) {
////            CacheManu cacheManu = new CacheManu();
////            cacheManu.execute();
//            CacheProductName cacheProductName = new CacheProductName();
//            cacheProductName.execute();
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PRODUCT_ENTRIES);
        db.execSQL(SQL_DELETE_DEVICE_ENTRIES);
        db.execSQL(SQL_DELETE_HIS_ENTRIES);
        db.execSQL(SQL_DELETE_MADE_ENTRIES);
        db.execSQL(SQL_DELETE_MANU_ENTRIES);
        db.execSQL(SQL_DELETE_TEST_ENTRIES);

        onCreate(db);
    }

//    public class CacheManu extends AsyncTask<String, Void, String> {
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//
//        }
//
//        @Override
//        protected String doInBackground(String... arg0) {
//            // TODO Auto-generated method stub
//
//            String result = HttpUtils.doGet(Url.manu_url());
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//
//            Document doc = null;
//
//            try {
//                doc = DocumentHelper.parseText(result);
//                Element rootElt = doc.getRootElement();
//                Iterator iter = rootElt.elementIterator("results");
//
//                while (iter.hasNext()) {
//                    Element resultRecord = (Element) iter.next();
//                    Iterator itersElIterator = resultRecord.elementIterator("result");
//                    while (itersElIterator.hasNext()) {
//                        Element itemEle = (Element) itersElIterator.next();
//                        Iterator literLIterator = itemEle.elementIterator("binding");
//                        while(literLIterator.hasNext()){
//                            Element ele = (Element) literLIterator.next();
//                            if ("mname".equals(ele.attributeValue("name"))){
//                                String mname = ele.elementTextTrim("literal");
//                                if(!mname.equals(""))
//                                    db.insert(FeedReaderContract.FeedEntry.TABLE_NAME_MANU, null, manager.generateManuValues(new ManufactorData(mname)));
//
//                            }
//
//                        }
//
//                    }
//
//                }
//
//            } catch (DocumentException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }

//    public class CacheProductName extends AsyncTask<Object, Void, JSONArray> {
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
//            String result = "";
//
//            String url = "http://api.myjson.com/bins/1hcph0"; //"http://theme-e.adaptcentre.ie/openrdf-workbench/repositories/mv2.54/query?action=exec&queryLn=SPARQL&query=PREFIX%20%20%3A%20%3Chttp%3A%2F%2Fmyvolts.com%23%3E%0APREFIX%20owl%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0APREFIX%20rdf%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0APREFIX%20rdfs%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0ASELECT%20%20distinct%20%3Fprod_id%20%20%3Fpname%20%3Ftype%20%0AWHERE%20%0A%7B%20%0A%20%3Fprod_id%20%3Aproduct_name%20%3Fpname%20.%0A%20%3Fprod_id%20%3AisOfTypeCategory%20%3Ftype%20.%0A%0A%20filter%20(regex(%3Fpname%2C%20%22" + arg0[0] + "%22%2C%20%22i%22)" + args + ")%20.%0A%7D%0Aorder%20by%20%3Fpname%0ALIMIT%2010"+ offset +"&limit=100&infer=true&";
//
//            result = HttpUtils.doGet(url);
//
//
//            try {
//                JSONObject obj = new JSONObject(result);
//                JSONArray mv_db_arr = obj.getJSONArray("mv_db");
//                JSONArray mv_db_arr2 = mv_db_arr.getJSONArray(0);
//
//
//                for(int i=0; i<mv_db_arr2.length(); i++) {
//
//                    JSONObject item = (JSONObject) mv_db_arr2.get(i);
//                    Iterator<String> keys = item.keys();
//
//                    String category = keys.next();
//                    if(category.equals("product")){
//                        String category_val = item.optString(category);
//                        output_arr.put(category_val);
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
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//
//            List<ProductData> newData = new ArrayList<>();
//
//            try {
//                for(int i=0; i<result.length(); i++){
//                    JSONObject jsonObject = new JSONObject(result.getString(i));
//                    System.out.println("----------------------------sjson object");
//                    System.out.println(jsonObject);
//
//                    String name = jsonObject.getString("name");
//                    String productId = jsonObject.getString("productId");
//                    newData.add(new ProductData(productId, name, null));
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            saveToDB(newData);
//        }
//    }
//
//    public void saveToDB(List<ProductData> products) {
//        ContentValues values = new ContentValues();
//
//        for(int i=0; i<products.size(); i++) {
//            ProductData product = products.get(i);
//            values.put(FeedReaderContract.FeedEntry.PRODUCT_COLUMN_ID, product.getProductId());
//            values.put(FeedReaderContract.FeedEntry.PRODUCT_COLUMN_NAME, product.getName());
//            db.insert(FeedReaderContract.FeedEntry.PRODUCT_TABLE_NAME, null, values);
//        }
//    }

//    public class CacheProductName extends AsyncTask<String, Void, String> {
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//
//        }
//
//        @Override
//        protected String doInBackground(String... arg0) {
//            // TODO Auto-generated method stub
//
//            String result = HttpUtils.doGet(Url.pname_url());
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//
//            System.out.println("db helper cacheproduct");
//            System.out.println(result);
//
//            Document doc = null;
//            String product_id = "";
//
//            try {
//                doc = DocumentHelper.parseText(result);
//                Element rootElt = doc.getRootElement();
//                Iterator iter = rootElt.elementIterator("results");
//
//                while (iter.hasNext()) {
//                    Element resultRecord = (Element) iter.next();
//                    Iterator itersElIterator = resultRecord.elementIterator("result");
//                    while (itersElIterator.hasNext()) {
//                        Element itemEle = (Element) itersElIterator.next();
//                        Iterator literLIterator = itemEle.elementIterator("binding");
//                        while(literLIterator.hasNext()){
//                            Element ele = (Element) literLIterator.next();
//                            if ("product".equals(ele.attributeValue("name"))) {
//                                product_id = ele.elementTextTrim("uri");
//                            }else if ("pname".equals(ele.attributeValue("name"))){
//                                String pname = ele.elementTextTrim("literal");
//                                if(!pname.equals(""))
//                                    db.insert(FeedReaderContract.FeedEntry.PRODUCT_TABLE_NAME, null, manager.generateProductValues(new ProductData(pname, product_id)));
//
//                            }
//
//                        }
//
//                    }
//
//                }
//
//            } catch (DocumentException e) {
//                e.printStackTrace();
//            }
//
//            db.insert(FeedReaderContract.FeedEntry.PRODUCT_TABLE_NAME, null, manager.generateProductValues(new ProductData("Korg Tuner Pitchblack Compatible Power Supply Cable & in Car Charger", "1")));
//
//            db.insert(FeedReaderContract.FeedEntry.PRODUCT_TABLE_NAME, null, manager.generateProductValues(new ProductData("Korg PSU part KA-183 Compatible Power Supply Cable & in Car Charger", "2")));
//
//            db.insert(FeedReaderContract.FeedEntry.PRODUCT_TABLE_NAME, null, manager.generateProductValues(new ProductData("Dymo Label printer LT-100H Compatible Power Supply Plug Charger", "3")));
//
//            db.insert(FeedReaderContract.FeedEntry.PRODUCT_TABLE_NAME, null, manager.generateProductValues(new ProductData("Seagate PSU part FreeAgent 9NK2AE-500 Compatible Power Supply Plug Charger", "4")));
//
//        }
//    }

}
