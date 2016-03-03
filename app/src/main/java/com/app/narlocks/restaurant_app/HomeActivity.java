package com.app.narlocks.restaurant_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends DashboardActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setHeader(getString(R.string.app_name), true, true);
    }

    public void onNewOrderClick(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void onPaymentClick(View view) {

    }
}
