package com.app.narlocks.restaurant_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.app.narlocks.helper.ItemListMenu;
import com.app.narlocks.model.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MenuActivity extends AppCompatActivity {

    private List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                Log.d("Irráááááá", result);

                Gson g = new Gson();

                items = g.fromJson(result, new TypeToken<List<Item>>(){}.getType());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ItemListMenu adapter = new ItemListMenu(MenuActivity.this, items);
                        ListView listView = (ListView) findViewById(R.id.menuList);
                        listView.setAdapter(adapter);
                    }
                });

            }
        });
    }
}
