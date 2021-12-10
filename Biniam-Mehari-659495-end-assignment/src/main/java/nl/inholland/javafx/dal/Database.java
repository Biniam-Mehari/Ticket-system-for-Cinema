package nl.inholland.javafx.dal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import nl.inholland.javafx.model.AccessLevel;
import nl.inholland.javafx.model.Movie;
import nl.inholland.javafx.model.User;
import nl.inholland.javafx.model.Room;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database {
    private List<User> users = new ArrayList<>();
    private ObservableList<Movie> movies ;
    private  ObservableList<Room> room1;
    private  ObservableList<Room> room2;

    public Database() {
          room1 = FXCollections.observableArrayList();
          room2 = FXCollections.observableArrayList();
          movies = FXCollections.observableArrayList();
        addPersons();
        addMoviesAndRoom();
    }
    private  void addPersons(){

        User user1 = new User("admin","admin123", AccessLevel.admin);
        User user2 = new User("user","user123",AccessLevel.user);

        users.add(user1);
        users.add(user2);

    }

    private void addMoviesAndRoom(){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String startTime1 = "23-10-2021 12:00";
        LocalDateTime startTime11 = LocalDateTime.parse(startTime1 , formatter);
        String endTime1 = "23-10-2021 14:05";
        LocalDateTime endTime11 = LocalDateTime.parse(endTime1 , formatter);
        String startTime2 = "23-10-2021 15:00";
        LocalDateTime startTime22 = LocalDateTime.parse(startTime2 , formatter);
        String endTime2 = "23-10-2021 16:32";
        LocalDateTime endTime22 = LocalDateTime.parse(endTime2 , formatter);

        Movie movie1 = new Movie ("No time to lie",12,125);
        Movie movie2 = new Movie("The Addams Family 19",9,92);
        movies.add(movie1);
        movies.add(movie2);
        room1.addAll(Arrays.asList(new Room(movie1,200,startTime11,endTime11 ),
                new Room(movie2,200,startTime22,endTime22)));
        room2.addAll(Arrays.asList(new Room(movie1,100,startTime11,endTime11),
                new Room(movie2,100,startTime22,endTime22)));


    }

    public void addRoom1(Room r){
        room1.addAll(r);

    }
    public void addRoom2(Room r){
        room2.add(r);

    }
    public void addMovie(Movie m){
        movies.add(m);

    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public ObservableList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ObservableList<Movie> movies) {
        this.movies = movies;
    }

    public ObservableList<Room> getRoom1() {
        return room1;
    }

    public void setRoom1(ObservableList<Room> room1) {
        this.room1 = room1;
    }

    public ObservableList<Room> getRoom2() {
        return room2;
    }

    public void setRoom2(ObservableList<Room> room2) {
        this.room2 = room2;
    }
}


