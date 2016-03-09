package com.app.narlocks.restaurant_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.narlocks.helper.Extras;
import com.app.narlocks.helper.SessionManager;
import com.app.narlocks.model.User;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etLogin = (EditText) findViewById(R.id.etLogin);
        etLogin.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    public void onLoginClick(View view) {
        EditText etLogin = (EditText) findViewById(R.id.etLogin);
        EditText etPassword = (EditText) findViewById(R.id.etPassword);

        String login = etLogin.getText().toString();
        String password = etPassword.getText().toString();

        if(login.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "O Login e a Senha devem ser informados.", Toast.LENGTH_SHORT).show();
        } else {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("login", login)
                    .add("password", password)
                    .build();

            Request request = new Request.Builder()
                    .url("http://52.36.228.76:8080/restaurant_service/webresources/authentication/login")
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
                            if (!Extras.isNetworkAvailable(LoginActivity.this)) {
                                connectionMessage = "Verifique sua conexão com a internet.";
                            }
                            Toast.makeText(LoginActivity.this, "Erro ao verificar dados do usuário. " + connectionMessage, Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String result = response.body().string();
                        Gson gson = new Gson();

                        user = gson.fromJson(result, User.class);

                        if(user != null && user.getId() > 0) {
                            SessionManager session = new SessionManager(getApplicationContext());
                            session.createLoginSession(user.getName(), user.getId());

                            Intent menuIntent = new Intent(LoginActivity.this, HomeActivity.class);
                            menuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(menuIntent);
                            finish();
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "Dados incorretos, tente novamente!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Erro ao verificar dados do usuário.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }
}
