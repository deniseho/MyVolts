package com.example.cassie.myvolts.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.cassie.myvolts.ProductDetailsActivity;
import com.example.cassie.myvolts.testing.TaskResultActivity;
import com.example.cassie.myvolts.testing.TestingBean;

/**
 * Created by cassie on 23/06/2017.
 */

public class TestUtil {

    public static double showTestingResult(SharedPreferences sharedPreferences){
        if(null != sharedPreferences) {
            long endtime = System.currentTimeMillis();
            double worktime = (endtime - sharedPreferences.getLong("starttime", 10)) / 1000;
            return worktime;
        }
        return 0;
    }

    public static TestingBean getAllTestingResults(SharedPreferences sharedPreferences){
        if(null != sharedPreferences) {
            TestingBean test = new TestingBean();
            test.setTaskId(sharedPreferences.getInt("task", 0));
            test.setDeviceClick(sharedPreferences.getInt("deviceClick", 0));
            test.setBrandClick(sharedPreferences.getInt("brandClick", 0));
            test.setModelClick(sharedPreferences.getInt("modelClick", 0));
            test.setSearchBoxClick(sharedPreferences.getInt("searchBoxClick", 0));
            test.setSearchBtnClick(sharedPreferences.getInt("searchBtnClick", 0));
            test.setHistoryClck(sharedPreferences.getInt("hisClick", 0));
            test.setSearchAcivtityListClick(sharedPreferences.getInt("searchItemClick", 0));
            test.setWorktime(showTestingResult(sharedPreferences));
            test.setResultListClick(sharedPreferences.getInt("resultsListClick", 0));
            test.setScanButtonClick(sharedPreferences.getInt("scanBtnClick", 0));

            return test;
        }
        return null;
    }

    public static String displayResult(SharedPreferences sharedPreferences){
        TestingBean bean = getAllTestingResults(sharedPreferences);
        String result = "Task id: " + bean.getTaskId() + "\n"
                + "Main Page Search Box Click: " + bean.getSearchBoxClick() + "\n"
                + "Manufacturer box Click: " + bean.getBrandClick() + "\n"
                + "Device Box Click: " + bean.getDeviceClick() + "\n"
                + "Model Box Click: " + bean.getModelClick() + "\n"
                + "Search Activity Search Box Click: " + bean.getSearchBtnClick()+ "\n"
                + "Search List Item Click: " + bean.getSearchAcivtityListClick()+ "\n"
                + "History List Item Click: " + bean.getHistoryClck()+ "\n"
                + "Result List Item Click: " + bean.getResultListClick() + "\n"
                + "Scanner Button Click: " + bean.getScanButtonClick() + "\n"
                + "Action time: " + showTestingResult(sharedPreferences);

        return result;

    }

    public static void storeSearchClicks(SharedPreferences sharedPreferences, SharedPreferences.Editor editor, String objectStr) {
        int searchNum = 0;
        int searchClick = sharedPreferences.getInt(objectStr, 0);
        if(searchClick != 0){
            searchNum = searchClick;
            searchNum++;
        }else{
            searchNum = 1;
        }
        editor.putInt(objectStr, searchNum);
        editor.commit();
    }

    public static void clearSharedPreference(SharedPreferences.Editor editor){
        editor.clear();
        editor.commit();
    }


}
