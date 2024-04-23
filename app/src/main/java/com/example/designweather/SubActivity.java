package com.example.designweather;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.PixelCopy;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SubActivity extends AppCompatActivity {
    String url = "";
    String tenthanhpho = "";
    ImageView imgback;
    TextView txtthanhpho;
    ListView listView;
    MyAdapter myAdapter;
    ArrayList<Weather> weatherArrayList;

    TextView tvMin,tvMax,txtcloud,txtwet,txtwind;
    ImageView imgIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        Anhxa();
        txtthanhpho = (TextView) findViewById(R.id.txtCity);
        tvMin=findViewById(R.id.txtMin);
        tvMax=findViewById(R.id.txtMax);
        imgIcon=findViewById(R.id.imgIcon);
        txtwind=findViewById(R.id.txtWind);
        txtcloud=findViewById(R.id.txtCloud);
        txtwet=findViewById(R.id.txtWet);
        Intent intent = getIntent();
        String city = intent.getStringExtra("City");
        String date= intent.getStringExtra("Day");
        if (city.equals("")) {
            tenthanhpho = "Ha Noi";
            tomorrowWeather(tenthanhpho);
            Get7DaysData(tenthanhpho);

        } else {
            tenthanhpho = city;
          //  txtthanhpho.setText(tenthanhpho);
            tomorrowWeather(tenthanhpho);
            Get7DaysData(tenthanhpho);
        }



        weatherArrayList = new ArrayList<Weather>();
        myAdapter = new MyAdapter(SubActivity.this, weatherArrayList);
        listView.setAdapter(myAdapter);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Anhxa() {
        imgback = findViewById(R.id.img_back);
        listView = findViewById(R.id.lstview);

    }

    public void Get7DaysData(String city) {
        url = "https://api.openweathermap.org/data/2.5/forecast/daily?q=" + city + "&units=metric&appid=bd5e378503939ddaee76f12ad7a97608";
        RequestQueue requestQueue= Volley.newRequestQueue(SubActivity.this);
        StringRequest stringRequest= new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       try {
                           JSONObject jsonObject= new JSONObject(response);
                           JSONObject jsonObjectCity= jsonObject.getJSONObject("city");
                           String name= jsonObjectCity.getString("name");
                           Log.d(name,"thanh pho");

                           txtthanhpho.setText(name);

                           JSONArray jsonArray= jsonObject.getJSONArray("list");
                           for(int i=1;i<jsonArray.length();i++){
                               JSONObject jsonObjectList= jsonArray.getJSONObject(i);
                               String ngay= jsonObjectList.getString("dt");
                               long l= Long.valueOf(ngay);
                               Date date= new Date(l*1000L);
                               SimpleDateFormat simpleDateFormat= new SimpleDateFormat("EEEE MM-dd");
                               String day= simpleDateFormat.format(date);
                               JSONObject jsonObjectTemp= jsonObjectList.getJSONObject("temp");
                               String max= jsonObjectTemp.getString("max");
                               String min= jsonObjectTemp.getString("min");
                               Double a= Double.valueOf(max);
                               Double b= Double.valueOf(min);
                               String Nhietdomax= String.valueOf(a.intValue());
                               String Nhietdomin= String.valueOf(b.intValue());
                               JSONArray jsonArrayWeather= jsonObjectList.getJSONArray("weather");
                               JSONObject jsonObjectWeather= jsonArrayWeather.getJSONObject(0);
                               String status= jsonObjectWeather.getString("description");
                               String icon= jsonObjectWeather.getString("icon");

                               weatherArrayList.add(new Weather(day,status,icon,Nhietdomax,Nhietdomin));
                           }
                           myAdapter.notifyDataSetChanged();
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
    public  void tomorrowWeather(String city){
        RequestQueue queue=Volley.newRequestQueue(this);
        String url="https://api.openweathermap.org/data/2.5/forecast/daily?q=" + city + "&units=metric&appid=bd5e378503939ddaee76f12ad7a97608";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Xử lý dữ liệu nhận được từ API để lấy thông tin về thời tiết ngày tiếp theo
                            JSONObject nextDayWeather = response.getJSONArray("list").getJSONObject(1);
                            JSONObject jsonObjectTemp= nextDayWeather.getJSONObject("temp");

                            String max= jsonObjectTemp.getString("max");
                            String min= jsonObjectTemp.getString("min");
                            Double a= Double.valueOf(max);
                            Double b= Double.valueOf(min);
                            String Nhietdomax= String.valueOf(a.intValue());
                            String Nhietdomin= String.valueOf(b.intValue());
                            tvMin.setText(Nhietdomin + "°C");
                            tvMax.setText(Nhietdomax +"°C");
                            JSONArray jsonArrayWeather= nextDayWeather.getJSONArray("weather");
                            JSONObject jsonObjectWeather= jsonArrayWeather.getJSONObject(0);
                           String clouds= nextDayWeather.getString("clouds");
                           txtcloud.setText(clouds+ "%");
                           String humidity=nextDayWeather.getString("humidity");
                           txtwet.setText(humidity + "%");
                            String wind=nextDayWeather.getString("speed");
                            txtwind.setText(wind);
                            String icon= jsonObjectWeather.getString("icon");
                            Picasso.get().load("https://openweathermap.org/img/wn/"+icon+".png").into(imgIcon);

                            // Hiển thị thông tin thời tiết ngày tiếp theo

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Xử lý khi gặp lỗi
            }
        });

        // Thêm request vào hàng đợi
        queue.add(jsonObjectRequest);
    }
}