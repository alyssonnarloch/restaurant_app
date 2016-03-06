package com.app.narlocks.restaurant_app;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.app.narlocks.helper.Extras;
import com.app.narlocks.model.OrderItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PaymentActivity extends DashboardActivity {

    private TableLayout tbOrdeItems;
    private List<OrderItem> orderItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        setHeader(getString(R.string.title_payment), true, true);

        tbOrdeItems = (TableLayout) findViewById(R.id.tbOrderItems);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://52.36.228.76:8080/restaurant_service/webresources/orderitem/order_items/user/1")
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
                        if (!Extras.isNetworkAvailable(PaymentActivity.this)) {
                            connectionMessage = "Verifique sua conexão com a internet.";
                        }
                        Toast.makeText(PaymentActivity.this, "Erro ao buscar item do pedido do seu usuário. " + connectionMessage, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    String result = response.body().string();
                    Gson g = new Gson();

                    orderItems = g.fromJson(result, new TypeToken<List<OrderItem>>() {}.getType());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            double totalPrice = 0.0;
                            int totalAmount = 0;
                            TableRow tbRowHeader = new TableRow(PaymentActivity.this);

                            TextView tvNameTitle = new TextView(PaymentActivity.this);
                            tvNameTitle.setText("Item");
                            tvNameTitle.setTypeface(null, Typeface.BOLD);
                            tvNameTitle.setTextSize(19);
                            tvNameTitle.setPadding(3, 2, 3, 2);

                            TextView tvPriceTitle = new TextView(PaymentActivity.this);
                            tvPriceTitle.setText("Valor");
                            tvPriceTitle.setTypeface(null, Typeface.BOLD);
                            tvPriceTitle.setTextSize(19);
                            tvPriceTitle.setPadding(7, 2, 7, 2);

                            TextView tvAmountTitle = new TextView(PaymentActivity.this);
                            tvAmountTitle.setText("Qtde.");
                            tvAmountTitle.setTypeface(null, Typeface.BOLD);
                            tvAmountTitle.setTextSize(19);
                            tvAmountTitle.setPadding(3, 2, 3, 2);

                            tbRowHeader.addView(tvNameTitle);
                            tbRowHeader.addView(tvPriceTitle);
                            tbRowHeader.addView(tvAmountTitle);

                            tbOrdeItems.addView(tbRowHeader, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                            for(OrderItem orderItem : orderItems) {
                                TableRow tbRowBody = new TableRow(PaymentActivity.this);
                                tbRowBody.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.MATCH_PARENT));

                                TextView tvName = new TextView(PaymentActivity.this);
                                tvName.setText(orderItem.getItem().getName());
                                tvName.setTextSize(18);
                                tvName.setPadding(3, 2, 3, 2);

                                TextView tvPrice = new TextView(PaymentActivity.this);
                                tvPrice.setText(Extras.brFormat(orderItem.getItem().getPrice()));
                                tvPrice.setTextSize(18);
                                tvPrice.setPadding(7, 2, 7, 2);

                                TextView tvAmount = new TextView(PaymentActivity.this);
                                tvAmount.setText(Integer.toString(orderItem.getAmount()));
                                tvAmount.setTextSize(18);
                                tvAmount.setPadding(3, 2, 3, 2);

                                tbRowBody.addView(tvName);
                                tbRowBody.addView(tvPrice);
                                tbRowBody.addView(tvAmount);

                                tbOrdeItems.addView(tbRowBody, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                                totalPrice += orderItem.getItem().getPrice();
                                totalAmount += orderItem.getAmount();
                            }

                            TableRow tbRowFoot = new TableRow(PaymentActivity.this);
                            tbRowFoot.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.MATCH_PARENT));

                            TextView tvTotalTitle = new TextView(PaymentActivity.this);
                            tvTotalTitle.setText("Total");
                            tvTotalTitle.setTypeface(null, Typeface.BOLD);
                            tvTotalTitle.setTextSize(22);
                            tvTotalTitle.setPadding(3, 8, 3, 2);

                            TextView tvTotalPrice = new TextView(PaymentActivity.this);
                            tvTotalPrice.setText(Extras.brFormat(totalPrice));
                            tvTotalPrice.setTypeface(null, Typeface.BOLD);
                            tvTotalPrice.setTextSize(22);
                            tvTotalPrice.setPadding(10, 8, 10, 2);

                            TextView tvTotalAmount = new TextView(PaymentActivity.this);
                            tvTotalAmount.setText(Integer.toString(totalAmount));
                            tvTotalAmount.setTypeface(null, Typeface.BOLD);
                            tvTotalAmount.setTextSize(22);
                            tvTotalAmount.setPadding(3, 8, 3, 2);

                            tbRowFoot.addView(tvTotalTitle);
                            tbRowFoot.addView(tvTotalPrice);
                            tbRowFoot.addView(tvTotalAmount);

                            tbOrdeItems.addView(tbRowFoot, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PaymentActivity.this, "Erro ao buscar item do pedido do seu usuário.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
