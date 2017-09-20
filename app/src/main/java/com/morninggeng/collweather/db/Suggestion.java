package com.morninggeng.collweather.db;

import com.google.gson.annotations.SerializedName;

/**
 * Created by morninggeng on 2017/9/15.
 */

public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public Comfort carWash;

    public Sport sport;

    public class Comfort {
        @SerializedName("txt")
        public String info;
    }

    public class CarWash {
        @SerializedName("txt")
        public String info;
    }

    public class Sport {
        @SerializedName("txt")
        public String info;

        ;
    }


}
