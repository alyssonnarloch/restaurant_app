package com.app.narlocks.restaurant_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

import com.app.narlocks.helper.SessionManager;

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

        Button btnLogout = (Button) inflated.findViewById(R.id.btnLogout);
        if(!btnFeedbackVisible) {
            btnLogout.setVisibility(View.INVISIBLE);
        }
    }

    public void onHomeClick(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void onLogoutClick(View view) {
        SessionManager session = new SessionManager(getApplicationContext());

        session.logoutUser();

        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }
}
