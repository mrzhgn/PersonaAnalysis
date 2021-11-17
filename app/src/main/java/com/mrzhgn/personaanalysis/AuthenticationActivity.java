package com.mrzhgn.personaanalysis;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import dmax.dialog.SpotsDialog;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthenticationActivity extends AppCompatActivity {

    SharedPreferences sPref;

    private EditText login;
    private EditText password;

    private int userId;
    private String userToken;
    private String userName;
    private String userEmail;

    private int currentState;
    private boolean regState;
    AlertDialog pd;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SignActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.authentication_activity);

        sPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);

        pd = new SpotsDialog.Builder().setContext(AuthenticationActivity.this).setTheme(R.style.Custom).build();

        findViewById(R.id.voiti_button).setOnClickListener(view -> {
            regState = true;
            new JsonTask().execute(ApiValues.API_URL);
        });

        findViewById(R.id.pswdfrgt).setOnClickListener(view -> {
            Intent intent = new Intent(AuthenticationActivity.this, SupportActivity.class);
            intent.putExtra("userEmail", login.getText().toString().isEmpty() ? "" : login.getText().toString());
            Log.d("!!!!!!", login.getText().toString());
            startActivityForResult(intent, RequestCode.REQUEST_CODE_REGISTRATION);
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        });

        findViewById(R.id.tech).setOnClickListener(view -> {
            //view.setBackgroundColor(Color.parseColor("#484849"));
            Intent intent = new Intent(AuthenticationActivity.this, SupportActivity.class);
            intent.putExtra("userEmail", login.getText().toString().isEmpty() ? "" : login.getText().toString());
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
        outState.putInt("userId", userId);
        outState.putString("userToken", userToken);
        outState.putString("userName", userName);
        outState.putString("userPassword", userEmail);
        Log.d("Authentication Activity", "onSaveInstanceState");
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setCurrentState(savedInstanceState.getInt("current_state"));
        login.setText(savedInstanceState.getString("login"));
        password.setText(savedInstanceState.getString("password"));
        userId = savedInstanceState.getInt("userId");
        userToken = savedInstanceState.getString("userToken");
        userName = savedInstanceState.getString("userName");
        userEmail = savedInstanceState.getString("userEmail");
        Log.d("Authentication Activity", "onRestoreInstanceState");
    }

    private int getCurrentState() {
        return currentState;
    }

    private void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d("!!!", "0");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpsURLConnection connection = null;
            Log.d("!!!", "1");

            try {
                URL url = new URL(params[0]);
                connection = (HttpsURLConnection) url.openConnection();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.d(ApiValues.API_CONNECTION, "Connected to Api");
                    Log.d("!!!", "2");

                    OkHttpClient client = new OkHttpClient();
                    JsonObject json = new JsonObject();

                    json.addProperty("email", login.getText().toString());
                    json.addProperty("password", password.getText().toString());
                    RequestBody body = RequestBody.create(ApiValues.JSON, json.toString());

                    HttpUrl httpUrl = new HttpUrl.Builder()
                            .scheme("https")
                            .host("api.varvara-analytics.ru")
                            .addPathSegment("api")
                            .addPathSegment("v1")
                            .addPathSegment("users.login")
                            .build();

                    Request request = new Request.Builder()
                            .url(httpUrl)
                            .post(body)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseString = "" + response.body().string();
                    Log.d(ApiValues.API_RESPONSE, responseString);
                    if (!responseString.contains("token")) {
                        regState = false;
                        return null;
                    }
                    Log.d("!!!", "3");

                    JSONObject jsonObject = new JSONObject(responseString);
                    Map<String, Object> pairs = JsonHelper.jsonToMap(jsonObject);
                    Map<String, Object> decodedPairs = JsonHelper.decodeMap(pairs);
                    if (decodedPairs.get("id") instanceof Integer) userId = (int) decodedPairs.get("id");
                    else if (decodedPairs.get("id") instanceof String) userId = Integer.valueOf((String) decodedPairs.get("id"));
                    userToken = decodedPairs.get("token").toString();

                    httpUrl = new HttpUrl.Builder()
                            .scheme("https")
                            .host("api.varvara-analytics.ru")
                            .addPathSegment("api")
                            .addPathSegment("v1")
                            .addPathSegment("users")
                            .build();

                    request = new Request.Builder()
                            .url(httpUrl)
                            .addHeader("Token", userToken)
                            .build();

                    response = client.newCall(request).execute();
                    responseString = "" + response.body().string();
                    Log.d(ApiValues.API_RESPONSE, responseString);
                    if (!responseString.contains("users")) {
                        regState = false;
                        return null;
                    }
                    Log.d("!!!", "4");

                    jsonObject = new JSONObject(responseString);
                    pairs = JsonHelper.jsonToMap(jsonObject);
                    decodedPairs = JsonHelper.decodeMap(pairs);
                    userName = ((HashMap<String, Object>) ((List<Object>) decodedPairs.get("users")).get(0)).get("name").toString();
                    userEmail = ((HashMap<String, Object>) ((List<Object>) decodedPairs.get("users")).get(0)).get("email").toString();

                    return null;
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
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (pd.isShowing()) {
                pd.dismiss();
            }

            if (regState) currentState = RESULT_OK;
            else currentState = RESULT_CANCELED;

            Log.d("!!!", "5");
            if (currentState == RESULT_CANCELED) Toast.makeText(getApplicationContext(), "Неправильный логин или пароль", Toast.LENGTH_SHORT).show();
            else if (currentState == RESULT_OK) {
                SharedPreferences.Editor editor = sPref.edit();
                editor.putInt("userid", userId);
                editor.putString("usertoken", userToken);
                editor.putString("username", userName);
                editor.putString("useremail", userEmail);
                editor.commit();

                Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("userToken", userToken);
                intent.putExtra("userName", userName);
                intent.putExtra("userEmail", userEmail);
                startActivity(intent);
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
            }
        }

    }

}
