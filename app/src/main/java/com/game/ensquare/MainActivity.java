package com.game.ensquare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
    TextView mHighestScoreView;
    TypedArray t;
    int mCurrentScore, mHighestScore;
    View mButtons;
    View mLogo;
    Color c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        mButtons = findViewById(R.id.buttons);
        mLogo = findViewById(R.id.logo);
        mHighestScoreView = findViewById(R.id.high_score);

        TextView t;
        ImageView i = new ImageView(this);

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
        i.setColorFilter(getResources().getColor(typedValue.resourceId));

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }, 2000);
    }
    private void start() {
        Button b = (Button)findViewById(R.id.play);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
        mHighestScoreView.setVisibility(View.VISIBLE);
        mButtons.setVisibility(View.VISIBLE);
        mLogo.animate().alpha(0.0f).setDuration(500);
    }

    private void play() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("HIGH_SCORE", Context.MODE_PRIVATE);
        mHighestScore = prefs.getInt("HIGH_SCORE", 0);
        mHighestScoreView.setText("Best score : "+mHighestScore);
        mHighestScoreView.setTextColor(Color.BLACK);
    }
}
