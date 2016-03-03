package com.app.narlocks.restaurant_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.app.narlocks.helper.Extras;
import com.app.narlocks.model.Item;

public class ConfirmationActivity extends DashboardActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        setHeader(getString(R.string.title_confirmation_order), true, true);

        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvPrice = (TextView) findViewById(R.id.tvPrice);

        Intent intent = getIntent();
        if(intent != null) {
            Item item = (Item) intent.getSerializableExtra("item");

            tvName.setText(item.getName().toString());
            tvPrice.setText(Extras.brFormat(item.getPrice()).toString());
        }
    }
}
