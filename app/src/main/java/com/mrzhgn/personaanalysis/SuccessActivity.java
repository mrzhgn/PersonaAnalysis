package com.mrzhgn.personaanalysis;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class SuccessActivity extends AppCompatActivity {

    SharedPreferences sPref;

    private TextView bodyText;
    private RelativeLayout daleeButton;

    private int userId;
    private String userToken;
    private String userName;
    private String userEmail;

    private int currentState;
    private boolean regState;
    AlertDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_success);

        pd = new SpotsDialog.Builder().setContext(SuccessActivity.this).setTheme(R.style.Custom).build();

        sPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        bodyText = (TextView) findViewById(R.id.body_text);
        daleeButton = (RelativeLayout) findViewById(R.id.voiti_button);

        userId = getIntent().getIntExtra("userId", 0);
        userToken = getIntent().getStringExtra("userToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");

        StringBuilder sb = new StringBuilder();
        sb.append("Письмо с данными для входа в приложение отправлено на Вашу почту: ")
                .append(userEmail)
                .append(". Желаем приятного использования и успехов!");

        bodyText.setText(sb.toString());

        daleeButton.setOnClickListener(view -> {
            regState = true;
            new SuccessActivity.JsonTask().execute(ApiValues.API_URL);
        });
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("current_state", getCurrentState());
        outState.putInt("userId", userId);
        outState.putString("userToken", userToken);
        outState.putString("userName", userName);
        outState.putString("userPassword", userEmail);
        Log.d("Authentication Activity", "onSaveInstanceState");
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setCurrentState(savedInstanceState.getInt("current_state"));
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


            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpsURLConnection connection = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpsURLConnection) url.openConnection();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.d(ApiValues.API_CONNECTION, "Connected to Api");

                    OkHttpClient client = new OkHttpClient();
                    JsonObject json = new JsonObject();
                    RequestBody body = RequestBody.create(ApiValues.JSON, json.toString());

                    HttpUrl httpUrl = new HttpUrl.Builder()
                            .scheme("https")
                            .host("api.varvara-analytics.ru")
                            .addPathSegment("api")
                            .addPathSegment("v1")
                            .addPathSegment("users")
                            .build();

                    Request request = new Request.Builder()
                            .url(httpUrl)
                            .addHeader("Token", userToken)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseString = "" + response.body().string();
                    Log.d(ApiValues.API_RESPONSE, responseString);
                    if (!responseString.contains("users")) {
                        regState = false;
                        return null;
                    }
                    Log.d("!!!", "4");

                    JSONObject jsonObject = new JSONObject(responseString);
                    Map<String, Object> pairs = JsonHelper.jsonToMap(jsonObject);
                    Map<String, Object> decodedPairs = JsonHelper.decodeMap(pairs);
                    userName = ((HashMap<String, Object>) ((List<Object>) decodedPairs.get("users")).get(0)).get("name").toString();
                    userEmail = ((HashMap<String, Object>) ((List<Object>) decodedPairs.get("users")).get(0)).get("email").toString();
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

            Log.d("!!!", "5");
            if (currentState == RESULT_CANCELED) Toast.makeText(getApplicationContext(), "Вы не подтвердили свою почту", Toast.LENGTH_SHORT).show();
            else if (currentState == RESULT_OK) {
                SharedPreferences.Editor editor = sPref.edit();
                editor.putInt("userid", userId);
                editor.putString("usertoken", userToken);
                editor.putString("username", userName);
                editor.putString("useremail", userEmail);
                editor.commit();

                Intent intent = new Intent(SuccessActivity.this, MainActivity.class);
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
