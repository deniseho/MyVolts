package com.example.cassie.myvolts.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cassie on 23/05/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2019110202;
    public static final String DATABASE_NAME = "mwc.db";

    private static DbHelper dbHelp=null;
    private DbManager manager  = new DbManager();

    public SQLiteDatabase db;

    private static final String SQL_CREATE_PRODUCT_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.PRODUCT_TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.PRODUCT_COLUMN_ID + " TEXT," +
                    FeedReaderContract.FeedEntry.PRODUCT_COLUMN_NAME + " TEXT," +
                    FeedReaderContract.FeedEntry.PRODUCT_COLUMN_DESC + " TEXT," +
                    FeedReaderContract.FeedEntry.PRODUCT_COLUMN_FILE+ " TEXT)";

    private static final String SQL_CREATE_DEVICE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.DEVICE_TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.DEVICE_COLUMN_PID + " TEXT," +
                    FeedReaderContract.FeedEntry.DEVICE_COLUMN_MANU + " TEXT," +
                    FeedReaderContract.FeedEntry.DEVICE_COLUMN_NAME + " TEXT," +
                    FeedReaderContract.FeedEntry.DEVICE_COLUMN_MODEL + " TEXT," +
                    FeedReaderContract.FeedEntry.DEVICE_COLUMN_MV_UK + " TEXT," +
                    FeedReaderContract.FeedEntry.DEVICE_COLUMN_MV_DE + " TEXT," +
                    FeedReaderContract.FeedEntry.DEVICE_COLUMN_MV_US + " TEXT)";


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
}
