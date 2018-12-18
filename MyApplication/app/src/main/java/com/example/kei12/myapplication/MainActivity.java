package com.example.kei12.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public String URL = "https://openapi.naver.com/v1/search/movie.json?query=";
    public String Id = "GuzZZ71Lu8Vn__fHp_bY";
    public String PW = "hDl2936sHH";
    public BackPressCloseHandler backPressCloseHandler;
    static RequestQueue rq;

    EditText editText;

    static String keyword;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.searchEditText);
        recyclerView = findViewById(R.id.RC_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
        backPressCloseHandler = new BackPressCloseHandler(this);

        // 인터넷 연결 체크(wifi & data)
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = wifi.isConnected();

        NetworkInfo data = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = data.isConnected();

        if(isWifiConn==false && isMobileConn==false)
        {
            AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(this);
            alert_internet_status.setTitle(getString(R.string.connect));
            alert_internet_status.setMessage(getString(R.string.connectCheck));
            alert_internet_status.setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                public void onClick( DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            alert_internet_status.show();
        }

        Button button = findViewById(R.id.sch_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = editText.getText().toString();
                if(keyword.length() <= 1 || keyword.replace(" ", "").equals("")) { // 공백&미입력 예외처리
                    Toast.makeText(getApplicationContext(), getString(R.string.searchNotice), Toast.LENGTH_SHORT).show();
                }
                else {
             CheckTypesTask task = new CheckTypesTask();
                 task.execute();
                }
            }
        });

        if(rq == null) {
            rq = Volley.newRequestQueue(getApplicationContext());

        }
    }

    class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            asyncDialog.setMessage(getString(R.string.loading));

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                for (int i = 0; i < 2; i++) {
                    asyncDialog.setProgress(i * 50);
                    editText.onEditorAction(EditorInfo.IME_ACTION_DONE);

                    requestWithSomeHttpHeaders();

                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }

    public void requestWithSomeHttpHeaders() {

        try {
            String key = URLEncoder.encode(keyword, "UTF-8");
            String url = URL + key + "&display=50";
            StringRequest req = new StringRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            reaction(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            )

            {
                @Override
                public Map<String, String> getHeaders() {

                    Map<String, String> params = new HashMap();
                    params.put("X-Naver-Client-Id", Id);
                    params.put("X-Naver-Client-Secret", PW);
                    return params;
                }
            };
            req.setShouldCache(false);
            rq.add(req);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void reaction(String response) {

        Gson gs = new Gson();

        ArrayList<MvItem> movieLists = new ArrayList<>();

        final MovieList movieList = gs.fromJson(response, MovieList.class);
        if(movieList.items.size() == 0) {
            Toast.makeText(getApplicationContext(), keyword + getString(R.string.noSearch), Toast.LENGTH_LONG).show();
        }
        for(int i = 0; i < movieList.items.size(); i++) {
            MvItem movieItem = new MvItem(movieList.items.get(i).link,
                                                movieList.items.get(i).image,
                                                movieList.items.get(i).title,
                                                movieList.items.get(i).pubDate,
                                                movieList.items.get(i).director,
                                                movieList.items.get(i).actor,
                                                movieList.items.get(i).userRating);
            movieLists.add(movieItem);
        }

        RecyleAdapter recyleAdapter = new RecyleAdapter(movieLists);

        recyleAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyleAdapter);
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    class MovieList {
        public ArrayList<MvItem> items = new ArrayList<>();
    }
}