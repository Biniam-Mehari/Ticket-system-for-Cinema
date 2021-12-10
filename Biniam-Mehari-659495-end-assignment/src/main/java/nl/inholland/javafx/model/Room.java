package nl.inholland.javafx.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Room {
    private Movie movie;
    private String title;
    private double price;
    private  int seats;
    private LocalDateTime startTime;
    private  LocalDateTime endTime;
    private String startTimeMovie;
    private String endTimeMovie;


    public Room() {
    }

    public Room(Movie movie, int seats, LocalDateTime startTime, LocalDateTime endTime) {
        this.movie = movie;
        this.seats = seats;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = movie.getTitle();
        this.price = movie.getPrice();
        this.startTimeMovie=startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.endTimeMovie=endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

    }

    public String getStartTimeMovie() {
        return startTimeMovie;
    }

    public String getEndTimeMovie() {
        return endTimeMovie;
    }

    public Movie getMovie() {
        return movie;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
