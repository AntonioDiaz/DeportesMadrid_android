package com.adiaz.deportesmadrid;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import com.adiaz.deportesmadrid.activities.MainActivity;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeNoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

}
