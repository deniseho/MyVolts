package com.example.cassie.myvolts.testing;

/**
 * Created by cassie on 23/06/2017.
 */

public class TestingBean {
    private int taskId;
    private int deviceClick;
    private int brandClick;
    private int modelClick;
    private int searchBoxClick;
    private int searchBtnClick;
    private int historyClck;
    private int resultListClick;
    private int searchAcivtityListClick;
    private double worktime;
    private int scanButtonClick;


    public TestingBean() {
    }

    public TestingBean(int taskId, int deviceClick, int brandClick, int modelClick, int searchBoxClick, int searchBtnClick, int historyClck, int resultListClick, int searchAcivtityListClick, double worktime, int scanButtonClick) {
        this.taskId = taskId;
        this.deviceClick = deviceClick;
        this.brandClick = brandClick;
        this.modelClick = modelClick;
        this.searchBoxClick = searchBoxClick;
        this.searchBtnClick = searchBtnClick;
        this.historyClck = historyClck;
        this.resultListClick = resultListClick;
        this.searchAcivtityListClick = searchAcivtityListClick;
        this.worktime = worktime;
        this.scanButtonClick = scanButtonClick;
    }

    @Override
    public String toString() {
        return "TestingBean{" +
                "taskId=" + taskId +
                ", deviceClick=" + deviceClick +
                ", brandClick=" + brandClick +
                ", modelClick=" + modelClick +
                ", searchBoxClick=" + searchBoxClick +
                ", searchBtnClick=" + searchBtnClick +
                ", historyClck=" + historyClck +
                ", resultListClick=" + resultListClick +
                ", searchAcivtityListClick=" + searchAcivtityListClick +
                ", worktime=" + worktime +
                ", scanButtonClick=" + scanButtonClick +
                '}';
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getDeviceClick() {
        return deviceClick;
    }

    public void setDeviceClick(int deviceClick) {
        this.deviceClick = deviceClick;
    }

    public int getBrandClick() {
        return brandClick;
    }

    public void setBrandClick(int brandClick) {
        this.brandClick = brandClick;
    }

    public int getSearchBoxClick() {
        return searchBoxClick;
    }

    public void setSearchBoxClick(int searchBoxClick) {
        this.searchBoxClick = searchBoxClick;
    }

    public int getHistoryClck() {
        return historyClck;
    }

    public void setHistoryClck(int historyClck) {
        this.historyClck = historyClck;
    }

    public int getResultListClick() {
        return resultListClick;
    }

    public void setResultListClick(int resultListClick) {
        this.resultListClick = resultListClick;
    }

    public int getSearchAcivtityListClick() {
        return searchAcivtityListClick;
    }

    public void setSearchAcivtityListClick(int searchAcivtityListClick) {
        this.searchAcivtityListClick = searchAcivtityListClick;
    }

    public double getWorktime() {
        return worktime;
    }

    public void setWorktime(double worktime) {
        this.worktime = worktime;
    }

    public int getSearchBtnClick() {
        return searchBtnClick;
    }

    public void setSearchBtnClick(int searchBtnClick) {
        this.searchBtnClick = searchBtnClick;
    }

    public int getModelClick() {
        return modelClick;
    }

    public void setModelClick(int modelClick) {
        this.modelClick = modelClick;
    }

    public int getScanButtonClick() {
        return scanButtonClick;
    }

    public void setScanButtonClick(int scanButtonClick) {
        this.scanButtonClick = scanButtonClick;
    }
}
