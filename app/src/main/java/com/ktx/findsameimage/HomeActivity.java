package com.ktx.findsameimage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    /**
     * On button click dùng onClick trong xml
     * @param view
     */
    public void onBtnPlayClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
