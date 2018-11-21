package com.example.hagome.myapplication;

public class Sensor {
    @Override
    public String toString() {
        return "Sensor{" +
                "pots='" + pots + '\'' +
                '}';
    }

    String pots;
    String flower1;
    String flower2;
    String flower3;
    String flower4;
    String sensor1;
    String sensor2;
    String sensor3;
    String sensor4;
    String date;
    String update_time;
    Boolean auto;
    int limited;
    int pumptime;

    public Boolean getAuto() {
        return auto;
    }

    public void setAuto(Boolean auto) {
        this.auto = auto;
    }

    public int getLimited() {
        return limited;
    }

    public void setLimited(int limited) {
        this.limited = limited;
    }

    public int getPumptime() {
        return pumptime;
    }

    public void setPumptime(int pumptime) {
        this.pumptime = pumptime;
    }

    public Sensor(String pots, String flower1, String flower2, String flower3, String flower4, String sensor1, String sensor2, String sensor3, String sensor4, String date, String update_time, Boolean auto, int limited, int pumptime) {
        this.pots = pots;
        this.flower1 = flower1;
        this.flower2 = flower2;
        this.flower3 = flower3;
        this.flower4 = flower4;
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
        this.sensor4 = sensor4;
        this.date = date;
        this.update_time = update_time;
        this.auto = auto;
        this.limited = limited;

        this.pumptime = pumptime;
    }

    public String getPots() {
        return pots;
    }

    public void setPots(String pots) {
        this.pots = pots;
    }

    public String getFlower1() {
        return flower1;
    }

    public void setFlower1(String flower1) {
        this.flower1 = flower1;
    }

    public String getFlower2() {
        return flower2;
    }

    public void setFlower2(String flower2) {
        this.flower2 = flower2;
    }

    public String getFlower3() {
        return flower3;
    }

    public void setFlower3(String flower3) {
        this.flower3 = flower3;
    }

    public String getFlower4() {
        return flower4;
    }

    public void setFlower4(String flower4) {
        this.flower4 = flower4;
    }

    public String getSensor1() {
        return sensor1;
    }

    public void setSensor1(String sensor1) {
        this.sensor1 = sensor1;
    }

    public String getSensor2() {
        return sensor2;
    }

    public void setSensor2(String sensor2) {
        this.sensor2 = sensor2;
    }

    public String getSensor3() {
        return sensor3;
    }

    public void setSensor3(String sensor3) {
        this.sensor3 = sensor3;
    }

    public String getSensor4() {
        return sensor4;
    }

    public void setSensor4(String sensor4) {
        this.sensor4 = sensor4;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate(String update) {
        this.update_time = update_time;
    }
}
