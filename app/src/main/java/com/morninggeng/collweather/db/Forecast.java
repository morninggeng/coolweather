package com.morninggeng.collweather.db;

import com.google.gson.annotations.SerializedName;

/**
 * Created by morninggeng on 2017/9/15.
 */

public class Forecast {

    public String date;

    @SerializedName("cond")
    public More more;

    @SerializedName("tmp")
    public Temperature temperature;


    public class More {
        @SerializedName("txt_d")
        public String info;
    }

    public class Temperature {
        public String max;
        public String min;
    }
}
