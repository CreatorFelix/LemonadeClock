package com.creator.lemonade.sample.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.creator.lemonade.clock.widget.Timer;
import com.creator.lemonade.sample.R;

public class TimerActivity extends BaseDemoActivity implements View.OnClickListener {

    private Timer mTimer;
    private Button mBtnStart;
    private ViewGroup mGroupPauseResumeAndRest;
    private Button mBtnPauseResume;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        mBtnStart = findViewById(R.id.btn_start);
        mGroupPauseResumeAndRest = findViewById(R.id.group_pause_resume_and_reset);
        mBtnPauseResume = findViewById(R.id.btn_pause_or_resume);
        Button btnRest = findViewById(R.id.btn_reset);
        Button btnSetTime = findViewById(R.id.btn_set_time);
        mBtnStart.setOnClickListener(this);
        mBtnPauseResume.setOnClickListener(this);
        btnRest.setOnClickListener(this);
        btnSetTime.setOnClickListener(this);
        mTimer = findViewById(R.id.timer);
        mTimer.setTimerListener(new Timer.TimerListenerAdapter() {

            @Override
            public void onStateChanged(boolean started, boolean paused) {
                mBtnStart.setVisibility(started ? View.GONE : View.VISIBLE);
                mGroupPauseResumeAndRest.setVisibility(started ? View.VISIBLE : View.GONE);
                if (mGroupPauseResumeAndRest.getVisibility() == View.VISIBLE) {
                    mBtnPauseResume.setText(paused ? R.string.resume : R.string.pause);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                mTimer.startOrResume();
                break;
            case R.id.btn_pause_or_resume:
                if (mTimer.isPaused()) {
                    mTimer.startOrResume();
                } else {
                    mTimer.pause();
                }
                break;
            case R.id.btn_reset:
                mTimer.rest();
                break;
            case R.id.btn_set_time:
                mTimer.setTotalTime(1000 * 20);
                break;
        }
    }
}
