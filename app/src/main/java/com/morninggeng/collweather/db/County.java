package com.morninggeng.collweather.db;

import org.litepal.crud.DataSupport;

/**
 * 县 javaBaen类
 * Created by morninggeng on 2017/9/9.
 */

public class County extends DataSupport {

    private int id;
    private String cuntyName;
    private int cityId;
    private String weatherId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCuntyName() {
        return cuntyName;
    }

    public void setCuntyName(String cuntyName) {
        this.cuntyName = cuntyName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}
