package com.morninggeng.collweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 闪屏界面
 */
public class SplashActivity extends Activity {

    // 创建定时器
    Timer timer = new Timer();

    TimerTask task = new TimerTask() {
        // 定义标记
        int i = 0;

        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = i++;
            handler.sendMessage(message);
        }
    };

    // 处理定时器发送的消息
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 匹配标记 模拟倒计时
            if (msg.what == 1) {
                // 更新按钮显示信息
                btn_skip.setText("跳过  2 s");
            } else if (msg.what == 2) {
                btn_skip.setText("跳过  1 s");
            } else if (msg.what == 3) {
                // 倒计时完毕，跳转到音乐列表界面
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                // 关闭当前界面
                finish();
            }
            super.handleMessage(msg);
        }

        ;
    };
    @InjectView(R.id.btn_skip)
    Button btn_skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 倒计时完毕，跳转到音乐列表界面
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                // 关闭当前界面
                finish();
            }
        });
        // 开启定时器
        timer.schedule(task, 0, 1000); // 0s后执行task,经过1s再次执行
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // 关闭定时器
        timer.cancel();
    }
}
