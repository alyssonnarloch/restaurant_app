package com.app.narlocks.restaurant_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.app.narlocks.helper.Extras;
import com.app.narlocks.helper.ItemListMenu;
import com.app.narlocks.model.Item;
import com.app.narlocks.model.Order;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MenuActivity extends DashboardActivity {

    private List<Item> items;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setHeader(getString(R.string.title_new_order), true, true);

        Intent intent = getIntent();

        if(intent != null) {
            order = (Order) intent.getSerializableExtra("order");
        } else {
            throw new RuntimeException("O pedido deve ser informado!");
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://52.36.228.76:8080/restaurant_service/webresources/item/all")
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
                        if (!Extras.isNetworkAvailable(MenuActivity.this)) {
                            connectionMessage = "Verifique sua conexão com a internet.";
                        }
                        Toast.makeText(MenuActivity.this, "Erro ao adicionar o item ao pedido. " + connectionMessage, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    String result = response.body().string();
                    Gson g = new Gson();

                    items = g.fromJson(result, new TypeToken<List<Item>>() {
                    }.getType());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ItemListMenu adapter = new ItemListMenu(MenuActivity.this, items);
                            ListView listView = (ListView) findViewById(R.id.menuList);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Item item = items.get(position);

                                    Intent confirmationView = new Intent(MenuActivity.this, ConfirmationActivity.class);
                                    confirmationView.putExtra("item", item);
                                    confirmationView.putExtra("order", order);
                                    startActivity(confirmationView);
                                    finish();
                                }
                            });
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MenuActivity.this, "Erro ao listar cardápio.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
