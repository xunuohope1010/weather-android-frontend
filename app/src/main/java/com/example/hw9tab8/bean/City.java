package com.example.hw9tab8.bean;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class City implements Serializable {
    private String name;
    private String location;
    private String summary;
    private String icon;
    private int temperature;
    private int humidity;
    private String timezone;

    private String precipIntensity;
    private String ozone;
    private int cloudCover;

    private String summaryDaily;
    private String iconDaily;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    private boolean valid = true;

    private ArrayList<String>imgList = new ArrayList<>();

    public ArrayList<String> getImgList() {
        return imgList;
    }

    public void setImgList(ArrayList<String> imgList) {
        this.imgList = imgList;
    }

    public void addImgList(String imgLink){
        this.imgList.add(imgLink);
    }

    public String getIconDaily() {
        return iconDaily;
    }

    public void setIconDaily(String iconDaily) {
        this.iconDaily = iconDaily;
    }

    public String getSummaryDaily() {
        return summaryDaily;
    }

    public void setSummaryDaily(String summaryDaily) {
        this.summaryDaily = summaryDaily;
    }

    public String getPrecipIntensity() {
        return precipIntensity;
    }

    public void setPrecipIntensity(String precipIntensity) {
        this.precipIntensity = precipIntensity;
    }

    public String getOzone() {
        return ozone;
    }

    public void setOzone(String ozone) {
        this.ozone = ozone;
    }

    public int getCloudCover() {
        return cloudCover;
    }

    public void setCloudCover(int cloudCover) {
        this.cloudCover = cloudCover;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    private String windSpeed;
    private String visibility;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public ArrayList<Weekly> getList() {
        return list;
    }

    public void setList(ArrayList<Weekly> list) {
        this.list = list;
    }

    private String pressure;
    private ArrayList<Weekly>list;

}
