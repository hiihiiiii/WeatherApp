package com.example.designweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText edtSearch;
    Button btnSearch, btnSeeMore;
    TextView txtCity, txtNation, txtStatus, txtTemp, txtWet, txtWind, txtRain, txtDay,txtnext,txttemp,txthour;
    ImageView imgIcon;
    String City="";
    RecyclerView recyclerView;
    HourlyAdapter hourlyAdapter;
    ArrayList<Hourly> weatherhourly;



    IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        recyclerView=findViewById(R.id.layoutHourly);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        weatherhourly=new ArrayList<>();
        hourlyAdapter=new HourlyAdapter(weatherhourly);
        recyclerView.setAdapter(hourlyAdapter);
        GetCurrentWeatherData("Ha Noi");
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city= edtSearch.getText().toString();
                if(city.equals("")){
                    City="Ha Noi";
                    GetCurrentWeatherData(City);
                    InitRecylerView(City);
                }else{
                    City=city;
                    GetCurrentWeatherData(City);
                    InitRecylerView(City);
                }
            }
        });

        txtnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city= edtSearch.getText().toString();
                String ngay= txtDay.toString();
                Intent intent= new Intent(MainActivity.this,SubActivity.class);
                intent.putExtra("City",city);
                intent.putExtra("Day",ngay);
                startActivity(intent);
            }
        });



    }

    private void InitRecylerView(String city) {
        url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&units=metric&appid=bd5e378503939ddaee76f12ad7a97608";
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest= new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            JSONArray jsonArray= jsonObject.getJSONArray("list");
                            for(int i=1;i<jsonArray.length();i++){
                                JSONObject jsonObjectList= jsonArray.getJSONObject(i);
                                String ngay= jsonObjectList.getString("dt");
                                long l= Long.valueOf(ngay);
                                Date date= new Date(l*1000L);
                                SimpleDateFormat simpleDateFormat= new SimpleDateFormat("hh:mm");
                                String day= simpleDateFormat.format(date);
                                String temp= jsonObjectList.getString("temp");
                                JSONArray jsonArrayWeather= jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather= jsonArrayWeather.getJSONObject(0);
                                String icon= jsonObjectWeather.getString("icon");
                                 weatherhourly.add(new Hourly(icon,day,temp));
                            }
                            hourlyAdapter.notifyDataSetChanged();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        requestQueue.add(stringRequest);
    }


    public  void  GetCurrentWeatherData(String data) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&appid=bd5e378503939ddaee76f12ad7a97608";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            String day= jsonObject.getString("dt");
                            String name= jsonObject.getString("name");
                            txtCity.setText(name);

                            long l= Long.valueOf(day);
                            Date date= new Date(l*1000L);
                            SimpleDateFormat simpleDateFormat= new SimpleDateFormat("EEEE yyyy-MM-dd");
                            String Day= simpleDateFormat.format(date);
                            txtDay.setText(Day);

                            String urlIcon="https://openweathermap.org/img/wn/";
                            JSONArray jsonArrayWeather= jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather=jsonArrayWeather.getJSONObject(0);
                            String status= jsonObjectWeather.getString("main");
                            String icon= jsonObjectWeather.getString("icon");
                            Picasso.get().load("https://openweathermap.org/img/wn/"+icon+".png").into(imgIcon);
                            txtStatus.setText(status);

                            JSONObject jsonObjectMain= jsonObject.getJSONObject("main");
                            String temp= jsonObjectMain.getString("temp");
                            String humidity= jsonObjectMain.getString("humidity");

                            Double a= Double.valueOf(temp);
                            String Nhietdo= String.valueOf(a.intValue());
                            txtWet.setText(humidity+ "%");
                            txtTemp.setText(Nhietdo+"°C");

                            JSONObject jsonObjectWind= jsonObject.getJSONObject("wind");
                            String wind= jsonObjectWind.getString("speed");
                            txtWind.setText(wind+"m/s");
                             JSONObject jsonObjectCloud= jsonObject.getJSONObject("clouds");
                             String Cloud= jsonObjectCloud.getString("all");
                             txtRain.setText(Cloud+"%");

                             JSONObject jsonObjectSys= jsonObject.getJSONObject("sys");
                             String nation= jsonObjectSys.getString("country");
                             txtNation.setText(nation);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }
    private void getCurrentLocation(String lon, String lat){}
    String url = "https://api.openweathermap.org/data/2.5/weather?q=&units=metric&appid=bd5e378503939ddaee76f12ad7a97608";

    private void Anhxa() {
        edtSearch= (EditText) findViewById(R.id.edtCityName);
        btnSearch= (Button) findViewById(R.id.btnSearch);

        txtCity= findViewById(R.id.txtThanhpho);
        txtNation= findViewById(R.id.txtQuocgia);
        txtStatus= findViewById(R.id.txtTrangthai);
        txtTemp= findViewById(R.id.txtNhietdo);
        txtWet= findViewById(R.id.txtWet);
        txtWind= findViewById(R.id.txtWind);
        txtRain=findViewById(R.id.txtRain);
        txtDay= findViewById(R.id.txtNgay);
        imgIcon= (ImageView) findViewById(R.id.imgWeather);
        txtnext=findViewById(R.id.txtNext);
    }
}