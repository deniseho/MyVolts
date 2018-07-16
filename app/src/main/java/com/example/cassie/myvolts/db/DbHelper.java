package com.example.cassie.myvolts.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.example.cassie.myvolts.dto.DeviceData;
import com.example.cassie.myvolts.dto.ManufactorData;
import com.example.cassie.myvolts.dto.ProductData;
import com.example.cassie.myvolts.util.HttpUtils;
import com.example.cassie.myvolts.util.Url;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by cassie on 23/05/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "mwc.db";

    private static DbHelper dbHelp=null;
    private DbManager manager = new DbManager();

    public SQLiteDatabase db;

    private static final String SQL_CREATE_INIT_TEST_DATA=
            "CREATE TABLE 'initTestData' ( 'id' INTEGER PRIMARY KEY, 'testData' TEXT)";

    private static final String SQL_CREATE_PRODUCT_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.PRODUCT_TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.PRODUCT_COLUMN_ID + " TEXT," +
                    FeedReaderContract.FeedEntry.PRODUCT_COLUMN_NAME + " TEXT)";

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
        db.execSQL(SQL_CREATE_HIS_ENTRIES);
        db.execSQL(SQL_CREATE_MADE_ENTRIES);
        db.execSQL(SQL_CREATE_MANU_ENTRIES);
        db.execSQL(SQL_CREATE_TEST_ENTRIES);


        this.db = db;
        if(db != null && db.isOpen()) {
//            initProduct(db);
            //initMade(db);
            CacheManu cacheManu = new CacheManu();
            cacheManu.execute();
            CacheProductName cacheProductName = new CacheProductName();
            cacheProductName.execute();
        }
    }

    public void initProduct(SQLiteDatabase db){
//        ArrayList<ProductData> products = new ArrayList<ProductData>();
//        products.add(new ProductData("Aguilar Effects pedal Tonehammer Compatible USB", "Aguilar", new ArrayList<TechSpec>()));
//        products.add(new ProductData("Ada Amp modeller GCS-2 Compatible USB", "Ada", new ArrayList<TechSpec>()));
//        products.add(new ProductData("Akai Keyboard Advance 49 Compatible USB", "Akai", new ArrayList<TechSpec>()));
//        products.add(new ProductData("Akai Wind synth EWI4000S Compatible", "Akai", new ArrayList<TechSpec>()));
//        products.add(new ProductData("Alesis Compressor 3630 Compatible", "Alesis", ""));
//        products.add(new ProductData("AC Ryan Media player ACR-PV73100 Compatible", "AC Ryan", ""));
//        products.add(new ProductData("Acer Laptop 104378 Compatible", "Acer", ""));
//        products.add(new ProductData("AAcmePoint Monitor VT988 Compatible", "AAcmePoint", ""));
//        products.add(new ProductData("AG Neovo Monitor E-17DA Compatiblee", "AG Neovo", ""));
//        products.add(new ProductData("AG Neovo Monitor E-560 Compatible", "AG Neovo", ""));
//        products.add(new ProductData("Buffalo External hard drive DriveStation Velocity Compatible", "Buffalo", ""));
//        products.add(new ProductData("ClickFree External hard drive HD1035 Co", "ClickFree", ""));
//        products.add(new ProductData("ClickFree External hard drive HD1036 Compatible", "ClickFree", ""));
//        products.add(new ProductData("ClickFree External hard drive HD2087 Compatible", "ClickFree ", ""));
//        products.add(new ProductData("Sony DVD player DVP-FX720 Compatib", "Sony", ""));
//        products.add(new ProductData("ATMT External hard drive HD 350U-P Compatible", "ATMT", ""));
//
//        for(ProductData productData: products) {
//           long insert = db.insert(FeedReaderContract.FeedEntry.PRODUCT_TABLE_NAME, null, manager.generateProductValues(productData));
//        }

    }

    public void initMade(SQLiteDatabase db){
        ArrayList<DeviceData> devices = new ArrayList<DeviceData>();

        devices.add(new DeviceData("Access", "Synth"));
        devices.add(new DeviceData("NEC", "Laptop"));
        devices.add(new DeviceData("ATMT", "External hard drive"));
        devices.add(new DeviceData("ATMT", "Media hard drive"));
        devices.add(new DeviceData("Acer", "Laptop"));
        devices.add(new DeviceData("Acer", "Monitor"));
        devices.add(new DeviceData("Acer", "Projector"));
        devices.add(new DeviceData("Acer", "PSU part"));

        for(DeviceData device: devices) {
            long insert = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME_MADE, null, manager.generateDeviceValues(device));
        }
    }

    private static final String SQL_DELETE_PRODUCT_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.PRODUCT_TABLE_NAME;

    private static final String SQL_DELETE_HIS_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME_HISTORY;

    private static final String SQL_DELETE_MADE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME_MADE;

    private static final String SQL_DELETE_MANU_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME_MANU;

    private static final String SQL_DELETE_TEST_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME_TESTING;

    // private static final String SQL_INSERT_P1="INSERT INTO product VALUES (?,?,?)";

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PRODUCT_ENTRIES);
        db.execSQL(SQL_DELETE_HIS_ENTRIES);
        db.execSQL(SQL_DELETE_MADE_ENTRIES);
        db.execSQL(SQL_DELETE_MANU_ENTRIES);
        db.execSQL(SQL_DELETE_TEST_ENTRIES);

        onCreate(db);
    }

    public class CacheManu extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub

            String result = HttpUtils.doGet(Url.manu_url());
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Document doc = null;

            try {
                doc = DocumentHelper.parseText(result);
                Element rootElt = doc.getRootElement();
                Iterator iter = rootElt.elementIterator("results");

                while (iter.hasNext()) {
                    Element resultRecord = (Element) iter.next();
                    Iterator itersElIterator = resultRecord.elementIterator("result");
                    while (itersElIterator.hasNext()) {
                        Element itemEle = (Element) itersElIterator.next();
                        Iterator literLIterator = itemEle.elementIterator("binding");
                        while(literLIterator.hasNext()){
                            Element ele = (Element) literLIterator.next();
                            if ("mname".equals(ele.attributeValue("name"))){
                                String mname = ele.elementTextTrim("literal");
                                if(!mname.equals(""))
                                    db.insert(FeedReaderContract.FeedEntry.TABLE_NAME_MANU, null, manager.generateManuValues(new ManufactorData(mname)));

                            }

                        }

                    }

                }

            } catch (DocumentException e) {
                e.printStackTrace();
            }

        }
    }

    public class CacheProductName extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub

            String result = HttpUtils.doGet(Url.pname_url());
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            System.out.println(result);

            Document doc = null;
            String product_id = "";

            try {
                doc = DocumentHelper.parseText(result);
                Element rootElt = doc.getRootElement();
                Iterator iter = rootElt.elementIterator("results");

                while (iter.hasNext()) {
                    Element resultRecord = (Element) iter.next();
                    Iterator itersElIterator = resultRecord.elementIterator("result");
                    while (itersElIterator.hasNext()) {
                        Element itemEle = (Element) itersElIterator.next();
                        Iterator literLIterator = itemEle.elementIterator("binding");
                        while(literLIterator.hasNext()){
                            Element ele = (Element) literLIterator.next();
                            if ("product".equals(ele.attributeValue("name"))) {
                                product_id = ele.elementTextTrim("uri");
                            }else if ("pname".equals(ele.attributeValue("name"))){
                                String pname = ele.elementTextTrim("literal");
                                if(!pname.equals(""))
                                    db.insert(FeedReaderContract.FeedEntry.PRODUCT_TABLE_NAME, null, manager.generateProductValues(new ProductData(pname, product_id)));

                            }

                        }

                    }

                }

            } catch (DocumentException e) {
                e.printStackTrace();
            }

            db.insert(FeedReaderContract.FeedEntry.PRODUCT_TABLE_NAME, null, manager.generateProductValues(new ProductData("Korg Tuner Pitchblack Compatible Power Supply Cable & in Car Charger", "1")));

            db.insert(FeedReaderContract.FeedEntry.PRODUCT_TABLE_NAME, null, manager.generateProductValues(new ProductData("Korg PSU part KA-183 Compatible Power Supply Cable & in Car Charger", "2")));

            db.insert(FeedReaderContract.FeedEntry.PRODUCT_TABLE_NAME, null, manager.generateProductValues(new ProductData("Dymo Label printer LT-100H Compatible Power Supply Plug Charger", "3")));

            db.insert(FeedReaderContract.FeedEntry.PRODUCT_TABLE_NAME, null, manager.generateProductValues(new ProductData("Seagate PSU part FreeAgent 9NK2AE-500 Compatible Power Supply Plug Charger", "4")));

        }
    }

}
