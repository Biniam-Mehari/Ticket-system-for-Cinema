package nl.inholland.javafx.model;

import java.io.*;
import java.sql.Time;
import java.time.LocalTime;

public class Movie {
    private  String title;
    private  double price;
    private int duration;

    public Movie() {
    }

    public Movie(String title, double price,int duration) {
        this.title = title;
        this.price = price;
        this.duration=duration;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
