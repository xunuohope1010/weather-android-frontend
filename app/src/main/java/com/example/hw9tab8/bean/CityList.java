package com.example.hw9tab8.bean;

import java.util.ArrayList;

public class CityList{

    private static ArrayList<City>cities = new ArrayList<>();

    public static void initCities(int size){
        for (int i=0;i<size;i++){
            cities.add(new City());
        }
    }

    public static void addCity(City city){
        cities.add(city);
    }

    public static void setCityIndex(int index, City city){
        cities.set(index,city);
    }

    public static City getFirstCity(){
        return cities.get(0);
    }

    public static int getCityPosition(City city){
        for (int i=0;i<cities.size();i++){
            if (city.getName().equals(cities.get(i).getName())){
                return i;
            }
        }
        return -1;
    }

    public static void removeCity(City city){
        cities.remove(city);
    }
    public static boolean isFirstCity(City city){
        City firstCity = cities.get(0);
        return city.getName().equals(firstCity.getName());
    }
    public static boolean isCityExist(City city){
        for (int i=0;i<cities.size();i++){
            if (city.getName().equals(cities.get(i).getName())){
                return true;
            }
        }
        return false;
    }
    public static ArrayList<City> getCities() {
        return cities;
    }

}
