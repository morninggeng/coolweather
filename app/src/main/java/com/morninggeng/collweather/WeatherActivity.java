package com.morninggeng.collweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.morninggeng.collweather.db.Forecast;
import com.morninggeng.collweather.db.Weather;
import com.morninggeng.collweather.service.AutoUpdateService;
import com.morninggeng.collweather.util.HttpUtil;
import com.morninggeng.collweather.util.Utility;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 天气信息显示界面
 */
public class WeatherActivity extends AppCompatActivity {


    @InjectView(R.id.title_city)
    TextView titleCity;
    @InjectView(R.id.title_update_time)
    TextView titleUpdateTime;
    @InjectView(R.id.degree_text)
    TextView degreeText;
    @InjectView(R.id.weather_info_text)
    TextView weatherInfoText;
    @InjectView(R.id.forecast_layout)
    LinearLayout forecastLayout;
    @InjectView(R.id.aqi_text)
    TextView aqiText;
    @InjectView(R.id.pm25_text)
    TextView pm25Text;
    @InjectView(R.id.comfort_text)
    TextView comfortText;
    @InjectView(R.id.car_wash_text)
    TextView carWashText;
    @InjectView(R.id.sport_text)
    TextView sportText;
    @InjectView(R.id.weather_layout)
    ScrollView weatherLayout;
    @InjectView(R.id.bing_pic_img)
    ImageView bingPicImg;
    @InjectView(R.id.swipe_refresh)
    public SwipeRefreshLayout swipeRefresh;
    @InjectView(R.id.nav_button)
    Button navButton;
    @InjectView(R.id.drawer_layout)
    public DrawerLayout drawerLayout;

    public String weatherId;
    @InjectView(R.id.weather_state)
    ImageView weatherState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        ButterKnife.inject(this);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }


        if (weatherString != null) {
            // 从缓存中直接解析Json数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            shwoWeatherInfo(weather);
        } else {
            // 无缓存时去服务器查询天气
            weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.VISIBLE);
            requestWeather(weatherId);
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });
    }

    /**
     * 下载BingPic图片
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                edit.putString("bing_pic", bingPic);
                edit.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    /**
     * 服务器查询天气数据
     *
     * @param weatherId id
     */
    public void requestWeather(String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=bc0418b57b2d4918819d3974ac1285d9";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(WeatherActivity.this, "请求网络失败", Toast.LENGTH_SHORT).show();
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            shwoWeatherInfo(weather);
                            // 开启服务进行后台数据更新
                            Intent intent = new Intent(WeatherActivity.this, AutoUpdateService.class);
                            startService(intent);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }

    /**
     * 将天气数据显示到控件中
     *
     * @param weather
     */
    private void shwoWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.tempearture + "℃";
        String weartherInfo = weather.now.more.info;
        // 根据天气状态更改图片显示
        if (weartherInfo.equals("晴")) {
            weatherState.setImageResource(R.mipmap.sun);
        } else if (weartherInfo.equals("多云")) {
            weatherState.setImageResource(R.mipmap.cloudy);
        } else if (weartherInfo.equals("阴")) {
            weatherState.setImageResource(R.mipmap.cloud);
        } else if (weartherInfo.equals("阵雨")) {
            weatherState.setImageResource(R.mipmap.shower);
        }
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weartherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
        }
        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动建议：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.nav_button)
    public void onViewClicked() {
        drawerLayout.openDrawer(Gravity.START);
    }
}
