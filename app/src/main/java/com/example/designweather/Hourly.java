package com.example.designweather;

public class Hourly {
    public  String image;
    public  String hour;
    public  String temp;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHour() {
        return hour;
    }

    public Hourly(String image, String hour, String temp) {
        this.image = image;
        this.hour = hour;
        this.temp = temp;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
