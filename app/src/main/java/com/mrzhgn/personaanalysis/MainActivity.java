package com.mrzhgn.personaanalysis;

import android.app.AlertDialog;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import dmax.dialog.SpotsDialog;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements SalonAdapter.OnItemListener {

    private int userId;
    private String userToken;
    private String userName;
    private String userEmail;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView salonsList;
    private SalonAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public AlertDialog pd;
    SharedPreferences sPref;

    List<Salon> salons;

    private ImageView logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        sPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.purple,
                R.color.deep_purple
        );
        logOutButton = (ImageView) findViewById(R.id.back);

        userId = getIntent().getIntExtra("userId", 0);
        userToken = getIntent().getStringExtra("userToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");

        salonsList = (RecyclerView) findViewById(R.id.recycler_salons);
        salonsList.setNestedScrollingEnabled(false);

        pd = new SpotsDialog.Builder().setContext(MainActivity.this).setTheme(R.style.Custom).build();

        salons = new ArrayList<>();
        layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        salonsList.setLayoutManager(layoutManager);
        mAdapter = new SalonAdapter(MainActivity.this, salons, MainActivity.this);
        salonsList.setAdapter(mAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new JsonTask().execute(ApiValues.API_URL);
//                mAdapter.clear();
//                regState = true;
//                List<Salon> newSalons = new ArrayList<>();
//                HttpsURLConnection connection2 = null;
//                Log.d("!!!!!!!!!!", "!!!!!");
//                try {
//                    URL url = new URL(ApiValues.API_URL);
//                    connection2 = (HttpsURLConnection) url.openConnection();
//
//                    int responseCode = connection2.getResponseCode();
//
//                    if (responseCode == HttpsURLConnection.HTTP_OK) {
//                        Log.d(ApiValues.API_CONNECTION, "Connected to Api!!");
//
//                        OkHttpClient client = new OkHttpClient();
//
//                        HttpUrl httpUrl = new HttpUrl.Builder()
//                                .scheme("https")
//                                .host("api.varvara-analytics.ru")
//                                .addPathSegment("api")
//                                .addPathSegment("v1")
//                                .addPathSegment("salons")
//                                .build();
//
//                        Request request = new Request.Builder()
//                                .url(httpUrl)
//                                .addHeader("Token", userToken)
//                                .build();
//
//                        Response response = client.newCall(request).execute();
//                        String responseString = "" + response.body().string();
//                        Log.d(ApiValues.API_RESPONSE, responseString);
//                        if (!responseString.contains("salons")) {
//                            regState = false;
//                            return;
//                        }
//
//                        JSONObject jsonObject = new JSONObject(responseString);
//                        Map<String, Object> pairs = JsonHelper.jsonToMap(jsonObject);
//                        Map<String, Object> decodedPairs = JsonHelper.decodeMap(pairs);
//                        List<Object> salonJsons = (List<Object>) decodedPairs.get("salons");
//                        Log.d("!!!!!!!", "" + salonJsons.size());
//                        for (Object salonJson : salonJsons) {
//                            Log.d("!!!!!!!", "" + (int) ((HashMap<String, Object>) salonJson).get("id"));
//                            Salon salon = Salon.addInstance((int) ((HashMap<String, Object>) salonJson).get("id"));
//                            newSalons.add(salon);
//                            Log.d("!!!!!!!", "added");
//
//                            httpUrl = new HttpUrl.Builder()
//                                    .scheme("https")
//                                    .host("api.varvara-analytics.ru")
//                                    .addPathSegment("api")
//                                    .addPathSegment("v1")
//                                    .addPathSegment("salons")
//                                    .addPathSegment(Integer.toString(salon.getId()))
//                                    .build();
//
//                            request = new Request.Builder()
//                                    .url(httpUrl)
//                                    .addHeader("Token", userToken)
//                                    .build();
//
//                            response = client.newCall(request).execute();
//                            responseString = "" + response.body().string();
//                            Log.d("!!!!", responseString);
//                            Log.d(ApiValues.API_RESPONSE, responseString);
//                            if (!responseString.contains("address")) {
//                                Log.d("!!!!", "breaking");
//                                regState = false;
//                                return;
//                            }
//
//                            Log.d("!!!!!!!!", "breaking1");
//                            jsonObject = new JSONObject(responseString);
//                            Log.d("!!!!", "breaking2");
//                            pairs = JsonHelper.jsonToMap(jsonObject);
//                            Log.d("!!!!", "breaking3");
//                            decodedPairs = JsonHelper.decodeMap(pairs);
//                            Log.d("!!!!", "breaking4");
//                            //Salon salon = new Salon((int) decodedPairs.get("id"));
//                            Log.d("!!!!!!!!", decodedPairs.get("address").toString());
//                            Log.d("!!!!!!!!", decodedPairs.get("title").toString());
//                            salon.setAdres(decodedPairs.get("address").toString());
//                            salon.setTitle(decodedPairs.get("title").toString());
//                            if (decodedPairs.get("chairs_num") instanceof Integer) salon.setChairsNum((Integer) decodedPairs.get("chairs_num"));
//                            else if (decodedPairs.get("chairs_num") instanceof String) salon.setChairsNum(Integer.valueOf((String) decodedPairs.get("chairs_num")));
//                            if (decodedPairs.get("workers_num") instanceof Integer) salon.setWorkersNum((Integer) decodedPairs.get("workers_num"));
//                            else if (decodedPairs.get("workers_num") instanceof String) salon.setWorkersNum(Integer.valueOf((String) decodedPairs.get("workers_num")));
//                        }
//                    } else {
//                        regState = false;
//                        return;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    regState = false;
//                    return;
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Log.d("!!!!!!!!", "!!!");
//                    regState = false;
//                    return;
//                } finally {
//                    if (connection2 != null) connection2.disconnect();
//                }
//                mAdapter.addAll(newSalons);
//
//                swipeRefreshLayout.setRefreshing(false);
            }
        });

        salons = new ArrayList<>();

        logOutButton.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sPref.edit();
            editor.putInt("userid", -1);
            editor.putString("usertoken", "");
            editor.putString("username", "");
            editor.putString("useremail", "");
            editor.commit();

            Intent intent = new Intent(MainActivity.this, SignActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        });

        new JsonTask().execute(ApiValues.API_URL);

    }

    @Override
    public void onItemClick(Salon salon) {
        Intent intent = new Intent(MainActivity.this, ArtplayActivity.class);
        Log.d("!!!!!!!!", "!!!!!!!!");
        intent.putExtra("userId", userId);
        intent.putExtra("userToken", userToken);
        intent.putExtra("userName", userName);
        intent.putExtra("userEmail", userEmail);
        intent.putExtra("salonId", salon.getId());
        intent.putExtra("salonTitle", salon.getTitle());
        intent.putExtra("numChairs", salon.getChairsNum());
        intent.putExtra("numWorkers", salon.getWorkersNum());
        startActivity(intent);
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!swipeRefreshLayout.isRefreshing()) {
                pd.setCancelable(false);
                pd.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            HttpsURLConnection connection = null;
            salons = new ArrayList<>();

            try {
                URL url = new URL(params[0]);
                connection = (HttpsURLConnection) url.openConnection();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.d(ApiValues.API_CONNECTION, "Connected to Api");

                    OkHttpClient client = new OkHttpClient();

                    HttpUrl httpUrl = new HttpUrl.Builder()
                            .scheme("https")
                            .host("api.varvara-analytics.ru")
                            .addPathSegment("api")
                            .addPathSegment("v1")
                            .addPathSegment("salons")
                            .build();

                    Request request = new Request.Builder()
                            .url(httpUrl)
                            .addHeader("Token", userToken)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseString = "" + response.body().string();
                    Log.d(ApiValues.API_RESPONSE, responseString);
                    if (!responseString.contains("salons")) {
                        return null;
                    }

                    JSONObject jsonObject = new JSONObject(responseString);
                    Map<String, Object> pairs = JsonHelper.jsonToMap(jsonObject);
                    Map<String, Object> decodedPairs = JsonHelper.decodeMap(pairs);
                    List<Object> salonJsons = (List<Object>) decodedPairs.get("salons");
                    Log.d("!!!!!!!", "" + salonJsons.size());
                    for (Object salonJson : salonJsons) {
                        Log.d("!!!!!!!", "" + (int) ((HashMap<String, Object>) salonJson).get("id"));
                        Salon salon = Salon.addInstance((int) ((HashMap<String, Object>) salonJson).get("id"));
                        salons.add(salon);
                        Log.d("!!!!!!!", "added");

                        httpUrl = new HttpUrl.Builder()
                                .scheme("https")
                                .host("api.varvara-analytics.ru")
                                .addPathSegment("api")
                                .addPathSegment("v1")
                                .addPathSegment("salons")
                                .addPathSegment(Integer.toString(salon.getId()))
                                .build();

                        request = new Request.Builder()
                                .url(httpUrl)
                                .addHeader("Token", userToken)
                                .build();

                        response = client.newCall(request).execute();
                        responseString = "" + response.body().string();
                        Log.d("!!!!", responseString);
                        Log.d(ApiValues.API_RESPONSE, responseString);
                        if (!responseString.contains("address")) {
                            Log.d("!!!!", "breaking");
                            return null;
                        }

                        Log.d("!!!!!!!!", "breaking1");
                        jsonObject = new JSONObject(responseString);
                        Log.d("!!!!", "breaking2");
                        pairs = JsonHelper.jsonToMap(jsonObject);
                        Log.d("!!!!", "breaking3");
                        decodedPairs = JsonHelper.decodeMap(pairs);
                        Log.d("!!!!", "breaking4");
                        //Salon salon = new Salon((int) decodedPairs.get("id"));
                        Log.d("!!!!!!!!", decodedPairs.get("address").toString());
                        Log.d("!!!!!!!!", decodedPairs.get("title").toString());
                        salon.setAdres(decodedPairs.get("address").toString());
                        salon.setTitle(decodedPairs.get("title").toString());
                        if (decodedPairs.get("chairs_num") instanceof Integer) salon.setChairsNum((Integer) decodedPairs.get("chairs_num"));
                        else if (decodedPairs.get("chairs_num") instanceof String) salon.setChairsNum(Integer.valueOf((String) decodedPairs.get("chairs_num")));
                        if (decodedPairs.get("workers_num") instanceof Integer) salon.setWorkersNum((Integer) decodedPairs.get("workers_num"));
                        else if (decodedPairs.get("workers_num") instanceof String) salon.setWorkersNum(Integer.valueOf((String) decodedPairs.get("workers_num")));
                    }
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("!!!!!!!!", "!!!");
                return null;
            } finally {
                if (connection != null) connection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mAdapter.clear();
            mAdapter.addAll(salons);

//            Salon artplay = Salon.getSalonById(10);
//
//            artplayText.setText(artplay.getTitle());
//            artplayAdress.setText(artplay.getAdres());

            if (pd.isShowing()) {
                pd.dismiss();
            }
            if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);

//            findViewById(R.id.artplay).setOnClickListener(view -> {
//                Intent intent = new Intent(view.getContext(), ArtplayActivity.class);
//                intent.putExtra("userId", userId);
//                intent.putExtra("userToken", userToken);
//                intent.putExtra("userName", userName);
//                intent.putExtra("userEmail", userEmail);
//                intent.putExtra("salonId", artplay.getId());
//                intent.putExtra("numChairs", artplay.getChairsNum());
//                startActivity(intent);
//                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
//            });
        }

    }

    private class JsonTask2 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!swipeRefreshLayout.isRefreshing()) {
                pd.setCancelable(false);
                pd.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            HttpsURLConnection connection = null;
            salons = new ArrayList<>();

            try {
                URL url = new URL(params[0]);
                connection = (HttpsURLConnection) url.openConnection();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.d(ApiValues.API_CONNECTION, "Connected to Api");

                    OkHttpClient client = new OkHttpClient();

                    HttpUrl httpUrl = new HttpUrl.Builder()
                            .scheme("https")
                            .host("api.varvara-analytics.ru")
                            .addPathSegment("api")
                            .addPathSegment("v1")
                            .addPathSegment("salons")
                            .build();

                    Request request = new Request.Builder()
                            .url(httpUrl)
                            .addHeader("Token", userToken)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseString = "" + response.body().string();
                    Log.d(ApiValues.API_RESPONSE, responseString);
                    if (!responseString.contains("salons")) {
                        return null;
                    }

                    JSONObject jsonObject = new JSONObject(responseString);
                    Map<String, Object> pairs = JsonHelper.jsonToMap(jsonObject);
                    Map<String, Object> decodedPairs = JsonHelper.decodeMap(pairs);
                    List<Object> salonJsons = (List<Object>) decodedPairs.get("salons");
                    Log.d("!!!!!!!", "" + salonJsons.size());
                    for (Object salonJson : salonJsons) {
                        Log.d("!!!!!!!", "" + (int) ((HashMap<String, Object>) salonJson).get("id"));
                        Salon salon = Salon.addInstance((int) ((HashMap<String, Object>) salonJson).get("id"));
                        salons.add(salon);
                        Log.d("!!!!!!!", "added");

                        httpUrl = new HttpUrl.Builder()
                                .scheme("https")
                                .host("api.varvara-analytics.ru")
                                .addPathSegment("api")
                                .addPathSegment("v1")
                                .addPathSegment("salons")
                                .addPathSegment(Integer.toString(salon.getId()))
                                .build();

                        request = new Request.Builder()
                                .url(httpUrl)
                                .addHeader("Token", userToken)
                                .build();

                        response = client.newCall(request).execute();
                        responseString = "" + response.body().string();
                        Log.d("!!!!", responseString);
                        Log.d(ApiValues.API_RESPONSE, responseString);
                        if (!responseString.contains("address")) {
                            Log.d("!!!!", "breaking");
                            return null;
                        }

                        Log.d("!!!!!!!!", "breaking1");
                        jsonObject = new JSONObject(responseString);
                        Log.d("!!!!", "breaking2");
                        pairs = JsonHelper.jsonToMap(jsonObject);
                        Log.d("!!!!", "breaking3");
                        decodedPairs = JsonHelper.decodeMap(pairs);
                        Log.d("!!!!", "breaking4");
                        //Salon salon = new Salon((int) decodedPairs.get("id"));
                        Log.d("!!!!!!!!", decodedPairs.get("address").toString());
                        Log.d("!!!!!!!!", decodedPairs.get("title").toString());
                        salon.setAdres(decodedPairs.get("address").toString());
                        salon.setTitle(decodedPairs.get("title").toString());
                        if (decodedPairs.get("chairs_num") instanceof Integer) salon.setChairsNum((Integer) decodedPairs.get("chairs_num"));
                        else if (decodedPairs.get("chairs_num") instanceof String) salon.setChairsNum(Integer.valueOf((String) decodedPairs.get("chairs_num")));
                        if (decodedPairs.get("workers_num") instanceof Integer) salon.setWorkersNum((Integer) decodedPairs.get("workers_num"));
                        else if (decodedPairs.get("workers_num") instanceof String) salon.setWorkersNum(Integer.valueOf((String) decodedPairs.get("workers_num")));
                    }
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("!!!!!!!!", "!!!");
                return null;
            } finally {
                if (connection != null) connection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("!!!!", "" + salons.size());
            mAdapter.clear();
            mAdapter.addAll(salons);

            if (pd.isShowing()) {
                pd.dismiss();
            }
            if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);

//            Salon artplay = Salon.getSalonById(10);
//
//            artplayText.setText(artplay.getTitle());
//            artplayAdress.setText(artplay.getAdres());

//            findViewById(R.id.artplay).setOnClickListener(view -> {
//                Intent intent = new Intent(view.getContext(), ArtplayActivity.class);
//                intent.putExtra("userId", userId);
//                intent.putExtra("userToken", userToken);
//                intent.putExtra("userName", userName);
//                intent.putExtra("userEmail", userEmail);
//                intent.putExtra("salonId", artplay.getId());
//                intent.putExtra("numChairs", artplay.getChairsNum());
//                startActivity(intent);
//                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
//            });
        }

    }
}
