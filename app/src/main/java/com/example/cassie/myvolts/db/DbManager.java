package com.example.cassie.myvolts.db;

import android.content.ContentValues;

import com.example.cassie.myvolts.dto.DeviceData;
import com.example.cassie.myvolts.dto.HistoryData;
import com.example.cassie.myvolts.dto.ManufactorData;
import com.example.cassie.myvolts.dto.ProductData;
import com.example.cassie.myvolts.testing.TestingBean;

/**
 * Created by cassie on 23/05/2017.
 */

public class DbManager {

    private ContentValues values;

    public ContentValues generateProductValues(ProductData productData){
        values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.PRODUCT_COLUMN_ID, productData.getProductId());
        values.put(FeedReaderContract.FeedEntry.PRODUCT_COLUMN_NAME, productData.getName());
        //values.put(FeedReaderContract.FeedEntry.PRODUCT_COLUMN_NAME_TYPE, productData.getType());

        return values;
    }

    public ContentValues generateHistoryValues(HistoryData hisData){
        values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.HISTORY_COLUMN_NAME_HISTORY_NAME, hisData.getItemName());
        values.put(FeedReaderContract.FeedEntry.HISTORY_COLUMN_NAME_HISTORY_ISWHOLE, hisData.getIsWholeProduct());
        values.put(FeedReaderContract.FeedEntry.HISTORY_COLUMN_NAME_HISTORY_PRODUCTID, hisData.getProductId());

        return values;
    }

    public ContentValues generateDeviceValues(DeviceData deviceData){
        values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.MADE_COLUMN_NAME_NAME, deviceData.getManufacturer());

        return values;
    }

    public ContentValues generateManuValues(ManufactorData manuData){
        values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.MAMU_COLUMN_NAME_NAME, manuData.getItemName());

        return values;
    }

    public ContentValues generateTestValues(TestingBean testData){
        values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.TEST_COLUMN_NAME_TASKID, testData.getTaskId());
        values.put(FeedReaderContract.FeedEntry.TEST_COLUMN_NAME_DEVICECLICK, testData.getDeviceClick());
        values.put(FeedReaderContract.FeedEntry.TEST_COLUMN_NAME_BRANDCLICK, testData.getTaskId());
        values.put(FeedReaderContract.FeedEntry.TEST_COLUMN_NAME_MODELCLICK, testData.getDeviceClick());
        values.put(FeedReaderContract.FeedEntry.TEST_COLUMN_NAME_SEARCHBOXCLICK, testData.getTaskId());
        values.put(FeedReaderContract.FeedEntry.TEST_COLUMN_NAME_DEVICECLICK, testData.getDeviceClick());
        values.put(FeedReaderContract.FeedEntry.TEST_COLUMN_NAME_TASKID, testData.getTaskId());
        values.put(FeedReaderContract.FeedEntry.TEST_COLUMN_NAME_DEVICECLICK, testData.getDeviceClick());

        return values;
    }
}
