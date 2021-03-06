package com.morninggeng.collweather.db;

import org.litepal.crud.DataSupport;

/**
 * 省 javaBaen类
 * Created by morninggeng on 2017/9/9.
 */

public class Province extends DataSupport {
    private int id;

    private String provinceName;

    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
