package com.mrzhgn.personaanalysis;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import dmax.dialog.SpotsDialog;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistrationActivity extends AppCompatActivity {

    final String LOG_TAG = "Registration Activity";

    private EditText login;
    private EditText password;
    private EditText firstName;

    private int currentState;
    private boolean regState;
    AlertDialog pd;

    private int userId;
    private String userToken;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SignActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.registration_activity);

        pd = new SpotsDialog.Builder().setContext(RegistrationActivity.this).setTheme(R.style.Custom).build();

        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        firstName = (EditText) findViewById(R.id.first);

        findViewById(R.id.register).setOnClickListener(view -> {
            regState = true;
            new JsonTask().execute(ApiValues.API_URL);
        });

        findViewById(R.id.tech).setOnClickListener(view -> {
            Intent intent = new Intent(RegistrationActivity.this, SupportActivity.class);
            intent.putExtra("userEmail", login.getText().toString().isEmpty() ? "" : login.getText().toString());
            intent.putExtra("userName", firstName.getText().toString().isEmpty() ? "" : firstName.getText().toString());
            Log.d("!!!!!!", login.getText().toString());
            startActivityForResult(intent, RequestCode.REQUEST_CODE_REGISTRATION);
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        });
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("current_state", getCurrentState());
        outState.putString("login", login.getText().toString());
        outState.putString("password", password.getText().toString());
        outState.putString("first", firstName.getText().toString());
        Log.d(LOG_TAG, "onSaveInstanceState");
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setCurrentState(savedInstanceState.getInt("current_state"));
        login.setText(savedInstanceState.getString("login"));
        password.setText(savedInstanceState.getString("password"));
        firstName.setText(savedInstanceState.getString("first"));
        Log.d(LOG_TAG, "onRestoreInstanceState");
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    private void setState(boolean client) {
        if (client) currentState = RESULT_OK;
        else currentState = RESULT_CANCELED;
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if (firstName.getText().toString().isEmpty() || login.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                regState = false;
                return null;
            }

            if (!login.getText().toString().toUpperCase().matches("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$")) {
                regState = false;
                return null;
            }

            HttpsURLConnection connection = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpsURLConnection) url.openConnection();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.d(ApiValues.API_CONNECTION, "Connected to Api");

                    OkHttpClient client = new OkHttpClient();
                    JsonObject json = new JsonObject();

                    json.addProperty("name", firstName.getText().toString());
                    json.addProperty("email", login.getText().toString());
                    json.addProperty("password", password.getText().toString());
                    RequestBody body = RequestBody.create(ApiValues.JSON, json.toString());

                    HttpUrl httpUrl = new HttpUrl.Builder()
                            .scheme("https")
                            .host("api.varvara-analytics.ru")
                            .addPathSegment("api")
                            .addPathSegment("v1")
                            .addPathSegment("users.register")
                            .build();

                    Request request = new Request.Builder()
                            .url(httpUrl)
                            .post(body)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseString = "" + response.body().string();
                    Log.d(ApiValues.API_RESPONSE, responseString);
                    if (!responseString.contains("success")) {
                        regState = false;
                        return null;
                    }

                    json = new JsonObject();

                    json.addProperty("email", login.getText().toString());
                    json.addProperty("password", password.getText().toString());
                    body = RequestBody.create(ApiValues.JSON, json.toString());

                    httpUrl = new HttpUrl.Builder()
                            .scheme("https")
                            .host("api.varvara-analytics.ru")
                            .addPathSegment("api")
                            .addPathSegment("v1")
                            .addPathSegment("users.login")
                            .build();

                    request = new Request.Builder()
                            .url(httpUrl)
                            .post(body)
                            .build();

                    response = client.newCall(request).execute();
                    responseString = "" + response.body().string();
                    Log.d(ApiValues.API_RESPONSE, responseString);
                    if (!responseString.contains("token")) {
                        regState = false;
                        return null;
                    }

                    JSONObject jsonObject = new JSONObject(responseString);
                    Map<String, Object> pairs = JsonHelper.jsonToMap(jsonObject);
                    Map<String, Object> decodedPairs = JsonHelper.decodeMap(pairs);
                    userId = (int) decodedPairs.get("id");
                    userToken = decodedPairs.get("token").toString();

                    json = new JsonObject();

                    body = RequestBody.create(ApiValues.JSON, json.toString());

                    httpUrl = new HttpUrl.Builder()
                            .scheme("https")
                            .host("api.varvara-analytics.ru")
                            .addPathSegment("api")
                            .addPathSegment("v1")
                            .addPathSegment("users")
                            .addPathSegment(userId + ".sendConfirmationLetter")
                            .build();

                    request = new Request.Builder()
                            .url(httpUrl)
                            .addHeader("Token", userToken)
                            .post(body)
                            .build();

                    response = client.newCall(request).execute();
                    responseString = "" + response.body().string();
                    Log.d(ApiValues.API_RESPONSE, responseString);
                    if (!responseString.contains("success")) {
                        regState = false;
                        return null;
                    }

                } else {
                    regState = false;
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                regState = false;
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                regState = false;
                return null;
            } finally {
                if (connection != null) connection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (pd.isShowing()) {
                pd.dismiss();
            }

            if (regState) currentState = RESULT_OK;
            else currentState = RESULT_CANCELED;

            if (currentState == RESULT_CANCELED) Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
            else if (currentState == RESULT_OK) {
                Intent intent = new Intent(RegistrationActivity.this, SuccessActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("userToken", userToken);
                intent.putExtra("userName", firstName.getText().toString());
                intent.putExtra("userEmail", login.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
            }
        }

    }

}
