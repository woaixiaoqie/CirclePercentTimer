package com.xiaoniup.downtime;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.xiaoniup.downtime.views.CirclePercentTimerView;

public class MainActivity extends Activity {

    private CirclePercentTimerView mCirclePercentView;
    private double mDeadTimeTime_Iong = 2000;//倒计时的总秒数
    private long out_deadline_time = 1800;//倒计时的总秒数
    private String delivery_time = "20秒";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCirclePercentView = (CirclePercentTimerView) findViewById(R.id.deadtime_circle_view);

        //默认显示，下边还会判断
        mCirclePercentView.setVisibility(View.VISIBLE);

        mCirclePercentView.initLeftTime(out_deadline_time, mDeadTimeTime_Iong*1.0, delivery_time);
        mCirclePercentView.setOnLimitTimeListener(new CirclePercentTimerView.LimitTimeListener() {
            @Override
            public void onTimeOver(boolean flag) {
                mCirclePercentView.stopTimeCount();
            }

            @Override
            public void onLeftTime(long leftTime) {
            }
        });

    }
}
