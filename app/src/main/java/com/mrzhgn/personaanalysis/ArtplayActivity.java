package com.mrzhgn.personaanalysis;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.JsonObject;
import com.jjoe64.graphview.GraphView;
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import dmax.dialog.SpotsDialog;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.String.format;

public class ArtplayActivity extends AppCompatActivity {

    private static final String TAG = "ArtplayActivity";

    private GraphView graphView;
    private TextView percentView, numChairsView, numWorkersView, viruchView, statsPeriodView, statsPeriod3View, titleView, planView, summaView, jsonView;
    AlertDialog pd;
    private boolean regState;
    private ScrollView scrollView;
    private RelativeLayout toUp;
    private ImageView toMain;
    private Button newJsonTaskStartView;

    private int userId;
    private String userToken;
    private String userName;
    private String userEmail;
    private int salonId;
    private String salonTitle;
    private int numChairs;
    private int numWorkers;
    private Calendar cal, cal2;

    private LineChart mChart, mChart2;
    private SwipeRefreshWithScrollLayout swipeRefreshLayout;
    private RecyclerView pokazateliList, specialistiList, uslugiList, clientiList, kreslaList;
    private DataAdapter mAdapter, mAdapter3, mAdapter4, mAdapter5;
    private DataAdapter2 mAdapter2;
    private RecyclerView.LayoutManager layoutManager, layoutManager2, layoutManager3, layoutManager4, layoutManager5;
    List<DataItem> dataItems, dataItems2, dataItems3, dataItems4, dataItems5;
    ArrayList<Entry> yValues;
    ArrayList<Integer> colors;
//    Map<String, Integer> points;
    Map<String, Object> decodedPairs;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_artplay);

        swipeRefreshLayout = (SwipeRefreshWithScrollLayout) findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setColorSchemeResources(
                R.color.purple,
                R.color.deep_purple
        );

        userId = getIntent().getIntExtra("userId", 0);
        userToken = getIntent().getStringExtra("userToken");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");
        salonId = getIntent().getIntExtra("salonId", 0);
        salonTitle = getIntent().getStringExtra("salonTitle");
        numChairs = getIntent().getIntExtra("numChairs", 0);
        numWorkers = getIntent().getIntExtra("numWorkers", 0);

        pd = new SpotsDialog.Builder().setContext(ArtplayActivity.this).setTheme(R.style.Custom).build();

        scrollView = (ScrollView) findViewById(R.id.scrl);
        toUp = (RelativeLayout) findViewById(R.id.varvara);
        toMain = (ImageView) findViewById(R.id.back);
        pokazateliList = (RecyclerView) findViewById(R.id.recycler_pokazateli);
        specialistiList = (RecyclerView) findViewById(R.id.recycler_specialisti);
        uslugiList = (RecyclerView) findViewById(R.id.recycler_uslugi);
        clientiList = (RecyclerView) findViewById(R.id.recycler_klienti);
        kreslaList = (RecyclerView) findViewById(R.id.recycler_kresla);
        percentView = (TextView) findViewById(R.id.percent);
        viruchView = (TextView) findViewById(R.id.viruch);
        numChairsView = (TextView) findViewById(R.id.chaircol);
        numWorkersView = (TextView) findViewById(R.id.pioplecol);
        statsPeriodView = (TextView) findViewById(R.id.stats_period);
        statsPeriod3View = (TextView) findViewById(R.id.stats_period_3);
        titleView = (TextView) findViewById(R.id.personatextview);
        planView = (TextView) findViewById(R.id.plan);
        summaView = (TextView) findViewById(R.id.summa);
        newJsonTaskStartView = (Button) findViewById(R.id.newjsontaskstart);
//        jsonView = (TextView) findViewById(R.id.json);
//        graphView = (GraphView) findViewById(R.id.graph);
        mChart = (LineChart) findViewById(R.id.chart);
        mChart2 = (LineChart) findViewById(R.id.chart2);

        numChairsView.setText(format("%d", numChairs));
        numWorkersView.setText(format("%d", numWorkers));
        titleView.setText(salonTitle);

        Locale locale = new Locale("en", "UK");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        symbols.setGroupingSeparator(' ');
        String pattern = "###,##0";
        DecimalFormat decf = new DecimalFormat(pattern, symbols);
        planView.setText(decf.format(45000));

        scrollView.setSmoothScrollingEnabled(true);
        pokazateliList.setNestedScrollingEnabled(false);
        specialistiList.setNestedScrollingEnabled(false);
        uslugiList.setNestedScrollingEnabled(false);
        clientiList.setNestedScrollingEnabled(false);
        kreslaList.setNestedScrollingEnabled(false);

        cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal2 = Calendar.getInstance();
        if (cal2.get(Calendar.DAY_OF_MONTH) == 1) {
            if (cal.get(Calendar.MONTH) != Calendar.JANUARY) cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
            else {
                cal.set(Calendar.MONTH, Calendar.DECEMBER);
                cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM");
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM");
        String date1 = formatter.format(cal.getTime());
        String date2 = formatter.format(cal2.getTime());
//        Date date = new Date();
//        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        int year  = localDate.getYear();
//        int month = localDate.getMonthValue();
//        int day   = localDate.getDayOfMonth();
//        String statsPeriod = "01.10-";
//        if (day < 10) statsPeriod += "0" + day + ".";
//        else statsPeriod += "" + day + ".";
//        if (month < 10) statsPeriod += "0" + month;
//        else statsPeriod += "" + month;
        statsPeriodView.setText(date1);
        statsPeriod3View.setText(date2);
        viruchView.setText(format("Чистая прибыль на %s", formatter2.format(cal2.getTime())));

        toUp.setOnClickListener(view -> scrollView.smoothScrollTo(0, scrollView.getTop()));

        toMain.setOnClickListener(view -> onBackPressed());

        DatePickerDialog dialog = new DatePickerDialog.Builder(
                (datePickerDialog, year, monthOfYear, dayOfMonth) -> {
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, monthOfYear);
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    statsPeriodView.setText(formatter.format(cal.getTime()));
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH))
                .setThemeDark(true)
                .setAccentColor(0xFF713BB9)
                .build();

        DatePickerDialog dialog2 = new DatePickerDialog.Builder(
                (datePickerDialog, year, monthOfYear, dayOfMonth) -> {
                    cal2.set(Calendar.YEAR, year);
                    cal2.set(Calendar.MONTH, monthOfYear);
                    cal2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    statsPeriod3View.setText(formatter.format(cal2.getTime()));
                },
                cal2.get(Calendar.YEAR),
                cal2.get(Calendar.MONTH),
                cal2.get(Calendar.DAY_OF_MONTH))
                .setThemeDark(true)
                .setAccentColor(0xFF713BB9)
                .build();

        statsPeriodView.setOnClickListener(v -> dialog.show(getSupportFragmentManager(), TAG));

        statsPeriod3View.setOnClickListener(v -> dialog2.show(getSupportFragmentManager(), TAG));

        newJsonTaskStartView.setOnClickListener(v -> new JsonTask2().execute(ApiValues.API_URL));

        swipeRefreshLayout.setOnRefreshListener(() -> new JsonTask2().execute(ApiValues.API_URL));

        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setNoDataText("");
        mChart.invalidate();
        mChart2.setDragEnabled(false);
        mChart2.setScaleEnabled(false);
        mChart2.setNoDataText("");
        mChart2.invalidate();

        dataItems = new ArrayList<>();
        dataItems2 = new ArrayList<>();
        dataItems3 = new ArrayList<>();
        dataItems4 = new ArrayList<>();
        dataItems5 = new ArrayList<>();
//        points = new HashMap<>();

        layoutManager = new LinearLayoutManager(ArtplayActivity.this, LinearLayoutManager.VERTICAL, false);
        layoutManager2 = new LinearLayoutManager(ArtplayActivity.this, LinearLayoutManager.VERTICAL, false);
        layoutManager3 = new LinearLayoutManager(ArtplayActivity.this, LinearLayoutManager.VERTICAL, false);
        layoutManager4 = new LinearLayoutManager(ArtplayActivity.this, LinearLayoutManager.VERTICAL, false);
        layoutManager5 = new LinearLayoutManager(ArtplayActivity.this, LinearLayoutManager.VERTICAL, false);
        pokazateliList.setLayoutManager(layoutManager);
        specialistiList.setLayoutManager(layoutManager2);
        uslugiList.setLayoutManager(layoutManager3);
        clientiList.setLayoutManager(layoutManager4);
        kreslaList.setLayoutManager(layoutManager5);

        mAdapter = new DataAdapter(ArtplayActivity.this, dataItems);
        mAdapter2 = new DataAdapter2(ArtplayActivity.this, dataItems2);
        mAdapter3 = new DataAdapter(ArtplayActivity.this, dataItems3);
        mAdapter4 = new DataAdapter(ArtplayActivity.this, dataItems4);
        mAdapter5 = new DataAdapter(ArtplayActivity.this, dataItems5);
        pokazateliList.setAdapter(mAdapter);
        specialistiList.setAdapter(mAdapter2);
        uslugiList.setAdapter(mAdapter3);
        clientiList.setAdapter(mAdapter4);
        kreslaList.setAdapter(mAdapter5);

        regState = true;
        new JsonTask().execute(ApiValues.API_URL);

    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            if (!swipeRefreshLayout.isRefreshing()) {
                pd.setCancelable(false);
                pd.show();
            }
        }

        protected String doInBackground(String... params) {

            HttpsURLConnection connection = null;

            dataItems = new ArrayList<>();
            dataItems2 = new ArrayList<>();
            dataItems3 = new ArrayList<>();
            dataItems4 = new ArrayList<>();
            dataItems5 = new ArrayList<>();
//            points = new HashMap<>();
            yValues = new ArrayList<>();
            colors = new ArrayList<>();

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
                            .addPathSegment("salons")
                            .addPathSegment(salonId + ".lastWeek")
                            .build();

                    Request request = new Request.Builder()
                            .url(httpUrl)
                            .addHeader("Token", userToken)
                            .post(body)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseString = "" + response.body().string();
                    Log.d(ApiValues.API_RESPONSE, responseString);
                    if (!responseString.contains("1")) {
                        regState = false;
                        return null;
                    }

                    JSONObject jsonObject = new JSONObject(responseString);
                    Map<String, Object> pairs = JsonHelper.jsonToMap(jsonObject);
                    Map<String, Object> decodedPairs = JsonHelper.decodeMap(pairs);
                    for (int i = 3; i < 8; i++) {
                        if (decodedPairs.get("" + i) instanceof Integer) {
//                            points.put("" + i, (Integer) decodedPairs.get("" + i));
                            yValues.add(new Entry(i - 3, (float) (Integer) decodedPairs.get("" + i)));
                            colors.add((Integer) decodedPairs.get("" + i) > 45000 ? Color.parseColor("#91C956") : Color.parseColor("#E56E70"));
                        }
                        else if (decodedPairs.get("" + i) instanceof String) {
//                            points.put("" + i, Integer.valueOf((String) decodedPairs.get("" + i)));
                            yValues.add(new Entry(i - 3, (float) Integer.valueOf((String) decodedPairs.get("" + i))));
                            colors.add(Integer.valueOf((String) decodedPairs.get("" + i)) > 45000 ? Color.parseColor("#91C956") : Color.parseColor("#E56E70"));
                        }
                    }

                    json = new JsonObject();

//                    Calendar c = Calendar.getInstance();
//                    c.set(Calendar.DAY_OF_MONTH, 1);
//                    Calendar c2 = Calendar.getInstance();
//                    if (c2.get(Calendar.DAY_OF_MONTH) == 1) {
//                        if (c.get(Calendar.MONTH) != Calendar.JANUARY) c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
//                        else {
//                            c.set(Calendar.MONTH, Calendar.DECEMBER);
//                            c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
//                        }
//                    }
                    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
                    json.addProperty("date_one", formatter.format(cal.getTime()));
                    json.addProperty("date_two", formatter.format(cal2.getTime()));
//                    json.addProperty("chairs_num", numChairs);
                    body = RequestBody.create(ApiValues.JSON, json.toString());
                    Log.d("!!!!!!!!!!", formatter.format(cal.getTime()));
                    Log.d("!!!!!!!!!!", formatter.format(cal2.getTime()));

                    httpUrl = new HttpUrl.Builder()
                            .scheme("https")
                            .host("api.varvara-analytics.ru")
                            .addPathSegment("api")
                            .addPathSegment("v1")
                            .addPathSegment("salons")
                            .addPathSegment(salonId + ".getStat")
                            .build();

                    request = new Request.Builder()
                            .url(httpUrl)
                            .addHeader("Token", userToken)
                            .post(body)
                            .build();

                    response = client.newCall(request).execute();
                    responseString = "" + response.body().string();
                    Log.d(ApiValues.API_RESPONSE, responseString);
                    if (!responseString.contains("spec")) {
                        regState = false;
                        return null;
                    }
                    return responseString;
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!regState) {
                Toast.makeText(getApplicationContext(), "Ошибка получения данных", Toast.LENGTH_LONG).show();
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
            }
            else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Map<String, Object> pairs = new HashMap<>();
                    decodedPairs = new HashMap<>();
                    pairs = JsonHelper.jsonToMap(jsonObject);
                    decodedPairs = JsonHelper.decodeMap(pairs);

                    List<DataItem> data = new ArrayList<>();
                    Locale locale = new Locale("en", "UK");
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
                    symbols.setGroupingSeparator(' ');
                    String pattern = "###,##0";
                    DecimalFormat df = new DecimalFormat(pattern, symbols);
                    data.add(new DataItem("Потенциал салона", df.format(decodedPairs.get("potencialsalona")) + " руб"));
                    data.add(new DataItem("Реализация потенциала салона", decodedPairs.get("realizpotenciala").toString() + "%"));

                    List<DataItem> data5 = new ArrayList<>();
                    data5.add(new DataItem("Загрузка кресла", decodedPairs.get("zagruzkakresel").toString()));
                    data5.add(new DataItem("Доходность кресла", df.format(decodedPairs.get("dohodkresla")) + " руб"));
                    data5.add(new DataItem("Проходимость кресла", decodedPairs.get("prohodkresla").toString()));

                    List<DataItem> data2 = new ArrayList<>();
                    double vostr = 0.0;
                    if (decodedPairs.get("spec") instanceof Map) {
                        for (Map.Entry<String, Object> pair : ((HashMap<String, Object>) decodedPairs.get("spec")).entrySet()) {
                            if (pair.getValue() instanceof Double) vostr += (Double) pair.getValue();
                            else if (pair.getValue() instanceof String) vostr += Double.valueOf((String) pair.getValue());
                        }
                    } else {
                        for (int i = 0; i < ((ArrayList<Object>) decodedPairs.get("spec")).size(); i++) {
                            HashMap<String, Object> map = (HashMap<String, Object>) ((ArrayList<Object>) decodedPairs.get("spec")).get(i);
                            if (map.get("potencial") instanceof Double) vostr += (Double) map.get("potencial");
                            else if (map.get("potencial") instanceof String) vostr += Double.valueOf((String) map.get("potencial"));
                        }
                    }
                    if (decodedPairs.get("kolspec") instanceof Double) vostr = vostr / (Double) decodedPairs.get("kolspec");
                    else if (decodedPairs.get("kolspec") instanceof Integer) vostr = vostr / (Integer) decodedPairs.get("kolspec");
                    else if (decodedPairs.get("kolspec") instanceof String) vostr = vostr / Double.valueOf((String) decodedPairs.get("kolspec"));
                    BigDecimal bd = new BigDecimal(Double.toString(vostr));
                    bd = bd.setScale(3, RoundingMode.HALF_UP);
                    vostr = bd.doubleValue();
                    percentView.setText(Double.valueOf(vostr).toString() + " %");

                    Log.d("!!!!!", "" + ((ArrayList<Object>) pairs.get("spec")).size());
                    Log.d("!!!!!", "" + ((ArrayList<Object>) decodedPairs.get("spec")).size());
                    for (int i = 0; i < ((ArrayList<Object>) pairs.get("spec")).size(); i++) {
                        HashMap<String, Object> map = (HashMap<String, Object>) ((ArrayList<Object>) pairs.get("spec")).get(i);
                        String name = (String) map.get("name");
                        Log.d("!!!!!", name);
                        byte[] bytes = name.getBytes("UTF-8");
                        name = new String(bytes, "UTF-8");
                        Log.d("!!!!!", name);
                        data2.add(new DataItem(name, ((HashMap) ((ArrayList) decodedPairs.get("spec")).get(i)).get("potencial").toString() + " %"));
                    }

//                data2.add(new DataItem("Востребованность специалиста", Double.valueOf(vostr).toString()));
//                data2.add(new DataItem("Татьяна Ломакина", ((HashMap) decodedPairs.get("spec")).get("Татьяна  Ломакина").toString() + " %"));
//                data2.add(new DataItem("Дмитрий Николаев", ((HashMap) decodedPairs.get("spec")).get("Дмитрий Николаев").toString() + " %"));
//                data2.add(new DataItem("Глеб Гацюк", ((HashMap) decodedPairs.get("spec")).get("Глеб Гацюк").toString() + " %"));
                    //           data2.add(new DataItem("Екатерина Калинина", ((HashMap) decodedPairs.get("spec")).get("Екатерина Калинина").toString() + " %"));
                    //           data2.add(new DataItem("Кирилл Трухманов", ((HashMap) decodedPairs.get("spec")).get("Кирилл Трухманов").toString() + " %"));
                    //           data2.add(new DataItem("Виктория Жаринова", ((HashMap) decodedPairs.get("spec")).get("Виктория Жаринова").toString() + " %"));
//                data2.add(new DataItem("Флоатинг", ((HashMap) decodedPairs.get("spec")).get("Флоатинг").toString() + " %"));
//                data2.add(new DataItem("Оксана Колюшкова", ((HashMap) decodedPairs.get("spec")).get("Оксана Колюшкова").toString() + " %"));
//                data2.add(new DataItem("Кирилл Тимофеев", ((HashMap) decodedPairs.get("spec")).get("Кирилл Тимофеев ").toString() + " %"));
                    //           data2.add(new DataItem("Кристина Иванникова", ((HashMap) decodedPairs.get("spec")).get("  Кристина  Иванникова").toString() + " %"));
//                data2.add(new DataItem("Марина Козлова", ((HashMap) decodedPairs.get("spec")).get("Марина  Козлова").toString() + " %"));
//                data2.add(new DataItem("Ольга Бизина", ((HashMap) decodedPairs.get("spec")).get("Ольга Бизина ").toString() + " %"));

                    List<DataItem> data4 = new ArrayList<>();
                    data4.add(new DataItem("Доверие клиента", decodedPairs.get("doverieklienta").toString()));
                    data4.add(new DataItem("% постоянных клиентов спец-та", "!!!"));
                    data4.add(new DataItem("% клиентов салона", "!!!"));
                    data4.add(new DataItem("% новых клиентов", "!!!"));
                    data4.add(new DataItem("Кол-во клиентов на 1 мастера", "!!!"));

                    List<DataItem> data3 = new ArrayList<>();
                    for (int i = 0; i < ((ArrayList<Object>) pairs.get("uslugi")).size(); i++) {
                        HashMap<String, Object> map = (HashMap<String, Object>) ((ArrayList<Object>) pairs.get("uslugi")).get(i);
                        String name = (String) map.get("name");
                        Log.d("!!!!!", name);
                        byte[] bytes = name.getBytes("UTF-8");
                        name = new String(bytes, "UTF-8");
                        Log.d("!!!!!", name);
                        data3.add(new DataItem(name, ((HashMap) ((ArrayList) decodedPairs.get("uslugi")).get(i)).get("potencial").toString() + " %"));
                    }
//                data3.add(new DataItem("Стрижки, %", "!!!"));
//                data3.add(new DataItem("Окрашивания, %", "!!!"));
//                data3.add(new DataItem("Уходы, %", "!!!"));
//                data3.add(new DataItem("Розница, %", "!!!"));
//                data3.add(new DataItem("Nail, %", "!!!"));
                    //data3.add(new DataItem("Парикмахерские Услуги ,%", ((HashMap) decodedPairs.get("uslugi")).get("Парикмахерские Услуги ").toString() + "%"));
                    //data3.add(new DataItem("Флоатинг ,%", ((HashMap) decodedPairs.get("uslugi")).get("Флоатинг ").toString() + "%"));
                    //           data3.add(new DataItem("Визаж, %", ((HashMap) decodedPairs.get("uslugi")).get("Визаж ").toString() + "%"));
//                data3.add(new DataItem("Косметология, %", "!!!"));
                    //           data3.add(new DataItem("Прочие, %", decodedPairs.get("Other").toString() + "%"));

                    for (int i = 0; i < data.size(); i++) {
                        dataItems.add(data.get(i));
                    }

                    for (int i = 0; i < data2.size(); i++) {
                        dataItems2.add(data2.get(i));
                    }

                    for (int i = 0; i < data3.size(); i++) {
                        dataItems3.add(data3.get(i));
                    }

                    for (int i = 0; i < data4.size(); i++) {
                        dataItems4.add(data4.get(i));
                    }

                    for (int i = 0; i < data5.size(); i++) {
                        dataItems5.add(data5.get(i));
                    }

                    mAdapter.clear();
                    mAdapter2.clear();
                    mAdapter3.clear();
                    mAdapter4.clear();
                    mAdapter5.clear();
                    mAdapter.addAll(dataItems);
                    mAdapter2.addAll(dataItems2);
                    mAdapter3.addAll(dataItems3);
                    mAdapter4.addAll(dataItems4);
                    mAdapter5.addAll(dataItems5);

//                    Log.d("!!!!!", "" + points.size());
//                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                            new DataPoint(0, points.get("3")),
//                            new DataPoint(1, points.get("4")),
//                            new DataPoint(2, points.get("5")),
//                            new DataPoint(3, points.get("6")),
//                            new DataPoint(4, points.get("7"))
//                    });
//                    graphView.addSeries(series);

                    LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");
                    LineDataSet set2 = new LineDataSet(yValues, "Data Set 2");

                    set1.setFillAlpha(0);
                    set1.setLineWidth(2f);
                    set1.setColor(Color.WHITE);
                    set1.setCircleColor(Color.WHITE);
                    set1.setCircleRadius(5f);
                    set1.setDrawCircleHole(true);
                    set1.setCircleHoleColor(Color.parseColor("#2f3245"));
                    set1.setCircleHoleRadius(3f);
                    set1.setValueTextColor(Color.WHITE);
                    set1.setValueTextSize(12f);
                    set1.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            return df.format(value) + " р.";
                        }
                    });
                    set2.setFillAlpha(0);
                    set2.setLineWidth(2f);
                    set2.setColor(Color.WHITE);
                    set2.setCircleColors(colors);
                    set2.setCircleRadius(5f);
                    set2.setDrawCircleHole(false);
                    set2.setValueTextColor(Color.WHITE);
                    set2.setValueTextSize(12f);
                    set2.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            return df.format(value) + " р.";
                        }
                    });

                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(set1);
                    LineData lineData = new LineData(dataSets);
                    ArrayList<ILineDataSet> dataSets2 = new ArrayList<>();
                    dataSets2.add(set2);
                    LineData lineData2 = new LineData(dataSets2);
                    mChart.setData(lineData);
                    mChart2.setData(lineData2);

                    mChart.setBackgroundColor(Color.parseColor("#2f3245")); // use your bg color
                    mChart.getDescription().setText(" ");
                    mChart.setDrawGridBackground(false);
                    mChart.setDrawBorders(false);

                    mChart.setAutoScaleMinMaxEnabled(true);

                    mChart.setExtraLeftOffset(25);
                    mChart.setExtraRightOffset(25);

                    ArrayList<String> xAxisValues = new ArrayList<>();
                    int dayInMs = 1000 * 60 * 60 * 24;
                    xAxisValues.add(new SimpleDateFormat("dd.MM").format(new Date(new Date().getTime() - 4 * dayInMs)));
                    xAxisValues.add(new SimpleDateFormat("dd.MM").format(new Date(new Date().getTime() - 3 * dayInMs)));
                    xAxisValues.add(new SimpleDateFormat("dd.MM").format(new Date(new Date().getTime() - 2 * dayInMs)));
                    xAxisValues.add(new SimpleDateFormat("dd.MM").format(new Date(new Date().getTime() - dayInMs)));
                    xAxisValues.add(new SimpleDateFormat("dd.MM").format(new Date()));

                    YAxis leftAxis = mChart.getAxisLeft();
                    leftAxis.setEnabled(false);
                    YAxis rightAxis = mChart.getAxisRight();
                    rightAxis.setEnabled(false);

                    XAxis xAxis = mChart.getXAxis();
                    xAxis.setLabelCount(5);
                    xAxis.setGranularity(1f);
                    xAxis.setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(false);
                    xAxis.setAxisLineColor(Color.TRANSPARENT);
                    xAxis.disableGridDashedLine();
                    xAxis.removeAllLimitLines();
                    xAxis.setGridColor(Color.parseColor("#FFFFFF"));
                    xAxis.setAxisLineWidth(3);
                    xAxis.setTextColor(Color.WHITE);
//                    xAxis.setValueFormatter(new IAxisValueFormatter() {
//
//                        @Override
//                        public String getFormattedValue(float value, AxisBase axis) {
//
//                            return numMap.get((int)value);
//                        }
//                    });

                    // hide legend

                    Legend legend = mChart.getLegend();
                    legend.setEnabled(false);

                    mChart.animateY(2000, Easing.EaseOutBack);
                    mChart.invalidate();

                    mChart2.setBackgroundColor(Color.parseColor("#2f3245")); // use your bg color
                    mChart2.getDescription().setText(" ");
                    mChart2.setDrawGridBackground(false);
                    mChart2.setDrawBorders(false);

                    mChart2.setAutoScaleMinMaxEnabled(true);

                    mChart2.setExtraLeftOffset(25);
                    mChart2.setExtraRightOffset(25);

                    leftAxis = mChart2.getAxisLeft();
                    leftAxis.setEnabled(false);
                    rightAxis = mChart2.getAxisRight();
                    rightAxis.setEnabled(false);

                    xAxis = mChart2.getXAxis();
                    xAxis.setLabelCount(5);
                    xAxis.setGranularity(1f);
                    xAxis.setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(false);
                    xAxis.setAxisLineColor(Color.TRANSPARENT);
                    xAxis.disableGridDashedLine();
                    xAxis.removeAllLimitLines();
                    xAxis.setGridColor(Color.parseColor("#FFFFFF"));
                    xAxis.setAxisLineWidth(3);
                    xAxis.setTextColor(Color.WHITE);

                    legend = mChart2.getLegend();
                    legend.setEnabled(false);
                    mChart2.animateY(2000, Easing.EaseOutBack);
                    mChart2.invalidate();

                    summaView.setText("+" + df.format((int) yValues.get(4).getY()) + " ");

                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                    if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class JsonTask2 extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            if (!swipeRefreshLayout.isRefreshing()) {
                pd.setCancelable(false);
                pd.show();
            }
        }

        protected String doInBackground(String... params) {

            HttpsURLConnection connection = null;

            dataItems = new ArrayList<>();
            dataItems2 = new ArrayList<>();
            dataItems3 = new ArrayList<>();
            dataItems4 = new ArrayList<>();
            dataItems5 = new ArrayList<>();
//            points = new HashMap<>();

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
                            .addPathSegment("salons")
                            .addPathSegment(salonId + ".lastWeek")
                            .build();

                    Request request = new Request.Builder()
                            .url(httpUrl)
                            .addHeader("Token", userToken)
                            .post(body)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseString = "" + response.body().string();
                    Log.d(ApiValues.API_RESPONSE, responseString);
                    if (!responseString.contains("1")) {
                        regState = false;
                        return null;
                    }

                    JSONObject jsonObject = new JSONObject(responseString);
                    Map<String, Object> pairs = JsonHelper.jsonToMap(jsonObject);
                    Map<String, Object> decodedPairs = JsonHelper.decodeMap(pairs);
                    for (int i = 3; i < 8; i++) {
//                        if (decodedPairs.get("" + i) instanceof Integer) points.put("" + i, (Integer) decodedPairs.get("" + i));
//                        else if (decodedPairs.get("" + i) instanceof String) points.put("" + i, Integer.valueOf((String) decodedPairs.get("" + i)));
                    }

                    json = new JsonObject();

//                    Calendar c = Calendar.getInstance();
//                    c.set(Calendar.DAY_OF_MONTH, 1);
//                    Calendar c2 = Calendar.getInstance();
//                    if (c2.get(Calendar.DAY_OF_MONTH) == 1) {
//                        if (c.get(Calendar.MONTH) != Calendar.JANUARY) c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
//                        else {
//                            c.set(Calendar.MONTH, Calendar.DECEMBER);
//                            c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
//                        }
//                    }
                    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
                    json.addProperty("date_one", formatter.format(cal.getTime()));
                    json.addProperty("date_two", formatter.format(cal2.getTime()));
//                    json.addProperty("chairs_num", numChairs);
                    body = RequestBody.create(ApiValues.JSON, json.toString());
                    Log.d("!!!!!!!!!!", formatter.format(cal.getTime()));
                    Log.d("!!!!!!!!!!", formatter.format(cal2.getTime()));

                    httpUrl = new HttpUrl.Builder()
                            .scheme("https")
                            .host("api.varvara-analytics.ru")
                            .addPathSegment("api")
                            .addPathSegment("v1")
                            .addPathSegment("salons")
                            .addPathSegment(salonId + ".getStat")
                            .build();

                    request = new Request.Builder()
                            .url(httpUrl)
                            .addHeader("Token", userToken)
                            .post(body)
                            .build();

                    response = client.newCall(request).execute();
                    responseString = "" + response.body().string();
                    Log.d(ApiValues.API_RESPONSE, responseString);
                    if (!responseString.contains("spec")) {
                        regState = false;
                        return null;
                    }
                    return responseString;
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (!regState) {
                Toast.makeText(getApplicationContext(), "Ошибка получения данных", Toast.LENGTH_LONG).show();
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
            }
            else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Map<String, Object> pairs = new HashMap<>();
                    decodedPairs = new HashMap<>();
                    pairs = JsonHelper.jsonToMap(jsonObject);
                    decodedPairs = JsonHelper.decodeMap(pairs);

                    List<DataItem> data = new ArrayList<>();
                    Locale locale = new Locale("en", "UK");
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
                    symbols.setGroupingSeparator(' ');
                    String pattern = "###,##0";
                    DecimalFormat df = new DecimalFormat(pattern, symbols);
                    data.add(new DataItem("Потенциал салона", df.format(decodedPairs.get("potencialsalona")) + " руб"));
                    Log.d("!!!!!!!!!!", df.format(decodedPairs.get("potencialsalona")) + " руб");
                    data.add(new DataItem("Реализация потенциала салона", decodedPairs.get("realizpotenciala").toString() + "%"));

                    List<DataItem> data5 = new ArrayList<>();
                    data5.add(new DataItem("Загрузка кресла", decodedPairs.get("zagruzkakresel").toString()));
                    data5.add(new DataItem("Доходность кресла", df.format(decodedPairs.get("dohodkresla")) + " руб"));
                    data5.add(new DataItem("Проходимость кресла", decodedPairs.get("prohodkresla").toString()));

                    List<DataItem> data2 = new ArrayList<>();
                    double vostr = 0.0;
                    if (decodedPairs.get("spec") instanceof Map) {
                        for (Map.Entry<String, Object> pair : ((HashMap<String, Object>) decodedPairs.get("spec")).entrySet()) {
                            if (pair.getValue() instanceof Double) vostr += (Double) pair.getValue();
                            else if (pair.getValue() instanceof String) vostr += Double.valueOf((String) pair.getValue());
                        }
                    } else {
                        for (int i = 0; i < ((ArrayList<Object>) decodedPairs.get("spec")).size(); i++) {
                            HashMap<String, Object> map = (HashMap<String, Object>) ((ArrayList<Object>) decodedPairs.get("spec")).get(i);
                            if (map.get("potencial") instanceof Double) vostr += (Double) map.get("potencial");
                            else if (map.get("potencial") instanceof String) vostr += Double.valueOf((String) map.get("potencial"));
                        }
                    }
                    if (decodedPairs.get("kolspec") instanceof Double) vostr = vostr / (Double) decodedPairs.get("kolspec");
                    else if (decodedPairs.get("kolspec") instanceof Integer) vostr = vostr / (Integer) decodedPairs.get("kolspec");
                    else if (decodedPairs.get("kolspec") instanceof String) vostr = vostr / Double.valueOf((String) decodedPairs.get("kolspec"));
                    BigDecimal bd = new BigDecimal(Double.toString(vostr));
                    bd = bd.setScale(3, RoundingMode.HALF_UP);
                    vostr = bd.doubleValue();
                    percentView.setText(Double.valueOf(vostr).toString() + " %");

                    Log.d("!!!!!", "" + ((ArrayList<Object>) pairs.get("spec")).size());
                    Log.d("!!!!!", "" + ((ArrayList<Object>) decodedPairs.get("spec")).size());
                    for (int i = 0; i < ((ArrayList<Object>) pairs.get("spec")).size(); i++) {
                        HashMap<String, Object> map = (HashMap<String, Object>) ((ArrayList<Object>) pairs.get("spec")).get(i);
                        String name = (String) map.get("name");
                        Log.d("!!!!!", name);
                        byte[] bytes = name.getBytes("UTF-8");
                        name = new String(bytes, "UTF-8");
                        Log.d("!!!!!", name);
                        data2.add(new DataItem(name, ((HashMap) ((ArrayList) decodedPairs.get("spec")).get(i)).get("potencial").toString() + " %"));
                    }

//                data2.add(new DataItem("Востребованность специалиста", Double.valueOf(vostr).toString()));
//                data2.add(new DataItem("Татьяна Ломакина", ((HashMap) decodedPairs.get("spec")).get("Татьяна  Ломакина").toString() + " %"));
//                data2.add(new DataItem("Дмитрий Николаев", ((HashMap) decodedPairs.get("spec")).get("Дмитрий Николаев").toString() + " %"));
//                data2.add(new DataItem("Глеб Гацюк", ((HashMap) decodedPairs.get("spec")).get("Глеб Гацюк").toString() + " %"));
                    //           data2.add(new DataItem("Екатерина Калинина", ((HashMap) decodedPairs.get("spec")).get("Екатерина Калинина").toString() + " %"));
                    //           data2.add(new DataItem("Кирилл Трухманов", ((HashMap) decodedPairs.get("spec")).get("Кирилл Трухманов").toString() + " %"));
                    //           data2.add(new DataItem("Виктория Жаринова", ((HashMap) decodedPairs.get("spec")).get("Виктория Жаринова").toString() + " %"));
//                data2.add(new DataItem("Флоатинг", ((HashMap) decodedPairs.get("spec")).get("Флоатинг").toString() + " %"));
//                data2.add(new DataItem("Оксана Колюшкова", ((HashMap) decodedPairs.get("spec")).get("Оксана Колюшкова").toString() + " %"));
//                data2.add(new DataItem("Кирилл Тимофеев", ((HashMap) decodedPairs.get("spec")).get("Кирилл Тимофеев ").toString() + " %"));
                    //           data2.add(new DataItem("Кристина Иванникова", ((HashMap) decodedPairs.get("spec")).get("  Кристина  Иванникова").toString() + " %"));
//                data2.add(new DataItem("Марина Козлова", ((HashMap) decodedPairs.get("spec")).get("Марина  Козлова").toString() + " %"));
//                data2.add(new DataItem("Ольга Бизина", ((HashMap) decodedPairs.get("spec")).get("Ольга Бизина ").toString() + " %"));

                    List<DataItem> data4 = new ArrayList<>();
                    data4.add(new DataItem("Доверие клиента", decodedPairs.get("doverieklienta").toString()));
                    data4.add(new DataItem("% постоянных клиентов спец-та", "!!!"));
                    data4.add(new DataItem("% клиентов салона", "!!!"));
                    data4.add(new DataItem("% новых клиентов", "!!!"));
                    data4.add(new DataItem("Кол-во клиентов на 1 мастера", "!!!"));

                    List<DataItem> data3 = new ArrayList<>();
                    for (int i = 0; i < ((ArrayList<Object>) pairs.get("uslugi")).size(); i++) {
                        HashMap<String, Object> map = (HashMap<String, Object>) ((ArrayList<Object>) pairs.get("uslugi")).get(i);
                        String name = (String) map.get("name");
                        Log.d("!!!!!", name);
                        byte[] bytes = name.getBytes("UTF-8");
                        name = new String(bytes, "UTF-8");
                        Log.d("!!!!!", name);
                        data3.add(new DataItem(name, ((HashMap) ((ArrayList) decodedPairs.get("uslugi")).get(i)).get("potencial").toString() + " %"));
                    }
//                data3.add(new DataItem("Стрижки, %", "!!!"));
//                data3.add(new DataItem("Окрашивания, %", "!!!"));
//                data3.add(new DataItem("Уходы, %", "!!!"));
//                data3.add(new DataItem("Розница, %", "!!!"));
//                data3.add(new DataItem("Nail, %", "!!!"));
                    //data3.add(new DataItem("Парикмахерские Услуги ,%", ((HashMap) decodedPairs.get("uslugi")).get("Парикмахерские Услуги ").toString() + "%"));
                    //data3.add(new DataItem("Флоатинг ,%", ((HashMap) decodedPairs.get("uslugi")).get("Флоатинг ").toString() + "%"));
                    //           data3.add(new DataItem("Визаж, %", ((HashMap) decodedPairs.get("uslugi")).get("Визаж ").toString() + "%"));
//                data3.add(new DataItem("Косметология, %", "!!!"));
                    //           data3.add(new DataItem("Прочие, %", decodedPairs.get("Other").toString() + "%"));

                    for (int i = 0; i < data.size(); i++) {
                        dataItems.add(data.get(i));
                    }

                    for (int i = 0; i < data2.size(); i++) {
                        dataItems2.add(data2.get(i));
                    }

                    for (int i = 0; i < data3.size(); i++) {
                        dataItems3.add(data3.get(i));
                    }

                    for (int i = 0; i < data4.size(); i++) {
                        dataItems4.add(data4.get(i));
                    }

                    for (int i = 0; i < data5.size(); i++) {
                        dataItems5.add(data5.get(i));
                    }

                    mAdapter.clear();
                    mAdapter2.clear();
                    mAdapter3.clear();
                    mAdapter4.clear();
                    mAdapter5.clear();
                    mAdapter.addAll(dataItems);
                    mAdapter2.addAll(dataItems2);
                    mAdapter3.addAll(dataItems3);
                    mAdapter4.addAll(dataItems4);
                    mAdapter5.addAll(dataItems5);

//                    Log.d("!!!!!", "" + points.size());
//                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                            new DataPoint(0, points.get("3")),
//                            new DataPoint(1, points.get("4")),
//                            new DataPoint(2, points.get("5")),
//                            new DataPoint(3, points.get("6")),
//                            new DataPoint(4, points.get("7"))
//                    });
//                    graphView.addSeries(series);


                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                    if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
