package com.app.narlocks.restaurant_app;

import android.os.Bundle;

public class HomeActivity extends DashboardActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setHeader(getString(R.string.app_name), true, true);
    }
}
