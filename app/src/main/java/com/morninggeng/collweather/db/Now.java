package com.morninggeng.collweather.db;

import com.google.gson.annotations.SerializedName;

/**
 * Created by morninggeng on 2017/9/15.
 */

public class Now {
    @SerializedName("tmp")
    public String tempearture;
    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;
    }
}
