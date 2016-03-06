package com.app.narlocks.restaurant_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.narlocks.helper.Extras;
import com.app.narlocks.model.Item;
import com.app.narlocks.model.Order;
import com.app.narlocks.model.OrderItem;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConfirmationActivity extends DashboardActivity {

    private Order order;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        setHeader(getString(R.string.title_confirmation_order), true, true);

        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvPrice = (TextView) findViewById(R.id.tvPrice);

        Intent intent = getIntent();
        if(intent != null) {
            item = (Item) intent.getSerializableExtra("item");
            order = (Order) intent.getSerializableExtra("order");

            tvName.setText(item.getName().toString());
            tvPrice.setText(Extras.brFormat(item.getPrice()).toString());
        } else {
            throw new RuntimeException("O pedido e o item devem ser informados!");
        }
    }

    public void onSendOrderClick(View view) {
        EditText etAmount = (EditText) findViewById(R.id.etAmount);

        String amountText = etAmount.getText().toString();

        if(amountText.isEmpty()) {
            Toast.makeText(ConfirmationActivity.this, "Informe a quantidade a ser pedida!", Toast.LENGTH_LONG).show();
        } else {
            int amount = Integer.parseInt(amountText);

            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("order_id", Integer.toString(order.getId()))
                    .add("item_id", Integer.toString(item.getId()))
                    .add("amount", Integer.toString(amount))
                    .build();

            Request request = new Request.Builder()
                    .url("http://52.36.228.76:8080/restaurant_service/webresources/orderitem/add_item")
                    .post(formBody)
                    .header("Authorization", "Basic dHJ1dGFsb2NvQMOpbm9pem1hbm81Ng==")
                    .addHeader("Accept", "application/json")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String connectionMessage = "";
                            if (!Extras.isNetworkAvailable(ConfirmationActivity.this)) {
                                connectionMessage = "Verifique sua conexão com a internet.";
                            }
                            Toast.makeText(ConfirmationActivity.this, "Erro ao salvar o item ao pedido. " + connectionMessage, Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String result = response.body().string();
                        Gson gson = new Gson();

                        OrderItem orderItem = gson.fromJson(result, OrderItem.class);

                        Intent menuIntent = new Intent(ConfirmationActivity.this, MenuActivity.class);
                        menuIntent.putExtra("order", order);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ConfirmationActivity.this, item.getName() + " adicionado ao pedido com sucesso!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        startActivity(menuIntent);
                        finish();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ConfirmationActivity.this, "Erro ao adicionar item ao pedido.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }
}
