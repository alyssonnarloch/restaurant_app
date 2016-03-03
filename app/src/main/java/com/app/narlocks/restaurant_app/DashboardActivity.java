package com.app.narlocks.restaurant_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

public class DashboardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setHeader(String title, boolean btnHomeVisible, boolean btnFeedbackVisible) {
        ViewStub stub = (ViewStub) findViewById(R.id.vsHeader);
        View inflated = stub.inflate();

        TextView txtTitle = (TextView) inflated.findViewById(R.id.txtHeading);
        txtTitle.setText(title);

        Button btnHome = (Button) inflated.findViewById(R.id.btnHome);
        if(!btnHomeVisible) {
            btnHome.setVisibility(View.INVISIBLE);
        }

        Button btnFeedback = (Button) inflated.findViewById(R.id.btnFeedback);
        if(!btnFeedbackVisible) {
            btnFeedback.setVisibility(View.INVISIBLE);
        }
    }

    public void onHomeClick(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
