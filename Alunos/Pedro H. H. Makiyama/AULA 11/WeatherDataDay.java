package com.pedro;

public class WeatherDataDay {

    private double temp;
    private double tempmin;
    private double tempmax;
    private String conditions;
    private double humidity;
    private double precip;
    private double windspeed;
    private double winddir;

    public double getTempmin() {
        return tempmin;
    }
    public void setTempmin(double tempmin) {
        this.tempmin = tempmin;
    }
    public double getTempmax() {
        return tempmax;
    }
    public void setTempmax(double tempmax) {
        this.tempmax = tempmax;
    }
    public String getConditions() {
        return conditions;
    }
    public void setConditions(String conditions) {
        this.conditions = conditions;
    }
    public double getHumidity() {
        return humidity;
    }
    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
    public double getPrecip() {
        return precip;
    }
    public void setPrecip(double precip) {
        this.precip = precip;
    }
    public double getWindspeed() {
        return windspeed;
    }
    public void setWindspeed(double windspeed) {
        this.windspeed = windspeed;
    }
    public double getWinddir() {
        return winddir;
    }
    public void setWinddir(double winddir) {
        this.winddir = winddir;
    }
    public double getTemp() {
        return temp;
    }
    public void setTemp(double temp) {
        this.temp = temp;
    }    
}
