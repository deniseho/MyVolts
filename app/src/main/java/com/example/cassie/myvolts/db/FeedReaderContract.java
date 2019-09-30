package com.example.cassie.myvolts.db;

import android.provider.BaseColumns;

/**
 * Created by cassie on 23/05/2017.
 */

public final class FeedReaderContract {
    
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String _ID = "id";
        public static final String PRODUCT_TABLE_NAME = "product";
        public static final String PRODUCT_COLUMN_NAME = "name";
        public static final String PRODUCT_COLUMN_DESC = "desc";
        public static final String PRODUCT_COLUMN_FILE = "file";
        public static final String PRODUCT_COLUMN_ID = "productId";


        public static final String DEVICE_TABLE_NAME = "device";
        public static final String DEVICE_COLUMN_PID = "p_id";
        public static final String DEVICE_COLUMN_MANU = "manufacturer";
        public static final String DEVICE_COLUMN_NAME = "name";
        public static final String DEVICE_COLUMN_MODEL = "model";
        public static final String DEVICE_COLUMN_MV_UK = "mv_uk";
        public static final String DEVICE_COLUMN_MV_DE = "mv_de";
        public static final String DEVICE_COLUMN_MV_US = "mv_us";

        public static final String TABLE_NAME_HISTORY = "his";
        public static final String HISTORY_COLUMN_NAME_HISTORY_NAME = "name";
        public static final String HISTORY_COLUMN_NAME_HISTORY_ISWHOLE = "isWhole";
        public static final String HISTORY_COLUMN_NAME_HISTORY_PRODUCTID = "productid";

        public static final String TABLE_NAME_MADE = "made";
        public static final String MADE_COLUMN_NAME_NAME = "name";
        public static final String MADE_COLUMN_NAME_TYPE = "type";

        public static final String TABLE_NAME_MANU = "manu";
        public static final String MAMU_COLUMN_NAME_NAME = "name";

        public static final String TABLE_NAME_TESTING = "test";
        public static final String TEST_COLUMN_NAME_TASKID = "taskId";
        public static final String TEST_COLUMN_NAME_DEVICECLICK = "deviceClick";
        public static final String TEST_COLUMN_NAME_BRANDCLICK = "brandClick";
        public static final String TEST_COLUMN_NAME_MODELCLICK = "modelClick";
        public static final String TEST_COLUMN_NAME_SEARCHBOXCLICK = "searchBoxClick";
        public static final String TEST_COLUMN_NAME_SEARCHBTNCLICK = "searchBtnClick";
        public static final String TEST_COLUMN_NAME_HISTORYCLICK= "historyClck";
        public static final String TEST_COLUMN_NAME_RESULTLISTCLICK = "resultListClick";
        public static final String TEST_COLUMN_NAME_SEARCHLISTACTIVITYLISTCLICK = "searchAcivtityListClick";
        public static final String TEST_COLUMN_NAME_WORKTIME = "worktime";
        public static final String TEST_COLUMN_NAME_SCANBTNCLICK= "scanButtonClick";

    }
}

