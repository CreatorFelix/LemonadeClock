package com.creator.lemonade.sample.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.creator.lemonade.clock.widget.Stopwatch;
import com.creator.lemonade.sample.R;

public class StopwatchActivity extends BaseDemoActivity implements View.OnClickListener {

    private Stopwatch mStopwatch;
    private Button mBtnStart;
    private ViewGroup mGroupPauseAndLap;
    private ViewGroup mGroupResumeAndReset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        mBtnStart = findViewById(R.id.btn_start);
        Button btnPause = findViewById(R.id.btn_pause);
        Button btnResume = findViewById(R.id.btn_resume);
        Button btnReset = findViewById(R.id.btn_reset);
        Button btnLap = findViewById(R.id.btn_lap);
        mGroupPauseAndLap = findViewById(R.id.group_pause_and_lap);
        mGroupResumeAndReset = findViewById(R.id.group_resume_and_reset);
        mStopwatch = findViewById(R.id.stopwatch);
        mStopwatch.setStopwatchListener(new Stopwatch.StopwatchListenerAdapter() {

            @Override
            public void onStateChanged(boolean started, boolean paused) {
                mBtnStart.setVisibility(started ? View.GONE : View.VISIBLE);
                mGroupPauseAndLap.setVisibility(started && !paused ? View.VISIBLE : View.GONE);
                mGroupResumeAndReset.setVisibility(started && paused ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onLap(long lapTimeInMillis) {
                Toast.makeText(StopwatchActivity.this.getApplicationContext(), String.valueOf(lapTimeInMillis), Toast.LENGTH_SHORT).show();
            }
        });
        mBtnStart.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnResume.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnLap.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                mStopwatch.startOrResume();
                break;
            case R.id.btn_pause:
                mStopwatch.pause();
                break;
            case R.id.btn_resume:
                mStopwatch.startOrResume();
                break;
            case R.id.btn_reset:
                mStopwatch.reset();
                break;
            case R.id.btn_lap:
                mStopwatch.lap();
                break;
        }
    }
}
