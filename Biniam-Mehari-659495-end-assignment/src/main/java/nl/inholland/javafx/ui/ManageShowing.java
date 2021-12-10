package nl.inholland.javafx.ui;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import nl.inholland.javafx.dal.Database;
import nl.inholland.javafx.model.Movie;
import nl.inholland.javafx.model.Room;
import nl.inholland.javafx.model.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ManageShowing extends BasicForm{

    private Stage window;
    private Database database ;
    private  User user;
    private Movie movie;
    private ComboBox cmbMovieTitle;
    private Label lblMovieEndTime;
    private DatePicker datePickerDate;
    private TextField txtTime;
    private ComboBox cmbRoomNumber;
    private  Label lblFillNoOfSeats;
    private Label lblMoviePrice;
    private GridPane gridPane2;
    private  Button btnClear;
    private  Button btnAddShowing;



    public ManageShowing(User user,Database database) {

        movie = new Movie();
        this.database=database;
        this.user=user;

        tableViewRoom1 = new TableView();
        tableViewRoom2 = new TableView();
        gridPane2 = new GridPane();
        btnClear = new Button("Clear");
        btnAddShowing = new Button("Add showing");


// set-up the window
        window = new Stage();
        window.setWidth(1000);
        window.setHeight(600);
        window.setTitle("Fantastic Cinema-- -- manage showing--username: "+user.getUserName());

//this methode set-up menu and menubar and check which access level we receive for the user and prints the wright menubar
        adminMenu.getItems().addAll(purchaseTicketMenuItem, manageMoviesMenuItem);
        menubar = makeMenuAndMenuBar(user);

//this methode will make the middle gridpane that contains the formName and two tableview for room1 and room2
        // getting data from the database
        tableViewRoom1.setItems(database.getRoom1());
        tableViewRoom2.setItems(database.getRoom2());
        gridPane = makeAndFillGridPane1();

// this methode will make the bottom gridpane that contains all the label,textfile,combobox and buttons
// information about selected movie will display here
        this.gridPane2 = makeAndFillGridPane2();

        // in order to add all the gridpane i make a borderpane
        BorderPane borderpane = new BorderPane();
        borderpane.setTop(menubar);
        borderpane.setCenter(gridPane);
        borderpane.setBottom(gridPane2);

// create the scene and show the window and adds the borderpane
        Scene scene = new Scene(borderpane);

// css file is added hier
        scene.getStylesheets().add("style.css");
        window.setScene(scene);
        window.show();





        cmbRoomNumber.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (cmbRoomNumber.getValue().equals("Room 1")){
                    lblFillNoOfSeats.setText("200");
                }
                else if(cmbRoomNumber.getValue().equals("Room 2")){
                    lblFillNoOfSeats.setText("100");
                }
            }
        });


        cmbMovieTitle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
               for (Movie m:database.getMovies()){
                   if (cmbMovieTitle.getValue().equals(m.getTitle())){
                       movie.setDuration(m.getDuration());
                       lblMoviePrice.setText(Double.toString(m.getPrice()));
                   }
               }
            }
        });



        datePickerDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //this methode will format the date and time given by user and calculate the duration and print
                // the label end time
                Alert a = new Alert(Alert.AlertType.ERROR);
                if(!txtTime.getText().equals("")) {
                    try {
                        formatTimeAndFillLabel();
                    } catch (DateTimeParseException exp) {
                        a.setContentText("Time format is HH:mm example 20:00 or 06:35");
                        a.show();
                    }
                }
            }
        });

        txtTime.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //this methode will format the date and time given by user and calculate the duration and print
                // the label end time
                Alert a = new Alert(Alert.AlertType.ERROR);
                if(datePickerDate.getValue()!=null){
                    try{
                        formatTimeAndFillLabel();
                    }catch (DateTimeParseException exp){
                        a.setContentText("Time format is HH:mm example 20:00 or 06:35");
                        a.show();
                    }
                }



            }
        });


        btnAddShowing.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert a = new Alert(Alert.AlertType.ERROR);


                LocalDateTime startTime = null;
                LocalDateTime endTime=null;
                try{
                    //this formatter will add the date and time given by user
                    if (!cmbMovieTitle.getValue().toString().equals("")) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        startTime = LocalDateTime.parse(datePickerDate.getValue() + " " + txtTime.getText(), formatter);
                         endTime = LocalDateTime.parse(lblMovieEndTime.getText(), formatter);
                    }

                    Movie movieSelected = new Movie(cmbMovieTitle.getValue().toString(), Double.valueOf(lblMoviePrice.getText()), movie.getDuration());
                    //all type of check will be done her for a show to be added in the room
                    if (cmbRoomNumber.getValue().toString().equals("Room 1")) {

                        //this boolean method will return true if there is overlapping between the movies
                        boolean check = timeIsInRange(database.getRoom1(), startTime,endTime);
                        if (check) {
                            a.setContentText("It is not possible to add a showing that overlaps another showing in the same room");
                            a.show();
                        } else {
                            database.addRoom1(new Room(movieSelected, Integer.parseInt(lblFillNoOfSeats.getText()), startTime, endTime));
                            clearFields();
                        }

                    } else if (cmbRoomNumber.getValue().equals("Room 2")) {
                        boolean check = timeIsInRange(database.getRoom2(), startTime,endTime);
                        if (check) {
                            a.setContentText("It is not possible to add a showing that overlaps another showing in the same room");
                            a.show();
                        } else {
                            database.addRoom2(new Room(movieSelected, Integer.parseInt(lblFillNoOfSeats.getText()), startTime, endTime));
                            clearFields();
                        }
                    }
                    else {
                        a.show();
                    }

                    tableViewRoom1.refresh();
                    tableViewRoom2.refresh();

                }catch (DateTimeParseException e) {
                        a.setContentText( "Fill all required fields and check the date or time format\n"+e.toString());
                        a.show();
                } catch (Exception e) {
                    a.setContentText("Choose title first\n"+e.toString());
                    a.show();
                }








            }

        });

// this methode will call purchase tickets window and sends as parameter database and user in order to prevent data lose
        purchaseTicketMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {
                    new PurchaseTicket(user,database);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                window.close();

            }
        });

// this methode will call manage movies window and sends as parameter database and user in order to prevent data lose
        manageMoviesMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {
                    new ManageMovie(database,user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                window.close();

            }
        });

// this  event will work to logout and it will send the right database until the system is shutdown
// this will help us to prevent data lose while the app is running
        logoutMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {
                    new Login().start(database);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                window.close();


            }
        });

        btnClear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                clearFields();
            }
        });


    }





    private  GridPane makeAndFillGridPane2(){
        // adding label , textfield and buttons to show and edit selected movie
        Label lblMovieTitle = new Label("Movie Title");
        GridPane.setConstraints(lblMovieTitle,0,0);
        Label lblRoom = new Label("Room");
        GridPane.setConstraints(lblRoom,0,1);
        Label lblNoOfSeats = new Label("No. of seats");
        GridPane.setConstraints(lblNoOfSeats,0,2);
        Label lblStartTime = new Label("Start");
        GridPane.setConstraints(lblStartTime,2,0);
        Label lblEndTime = new Label("End");
        GridPane.setConstraints(lblEndTime,2,1);
        Label lblPrice = new Label("Price");
        GridPane.setConstraints(lblPrice,2,2);
        GridPane.setConstraints(btnAddShowing,5,1);
        GridPane.setConstraints(btnClear,5,2);

// the part which is filled with information of selected movie
        lblFillNoOfSeats = new Label();
        GridPane.setConstraints(lblFillNoOfSeats,1,2);
        lblMovieEndTime = new Label();
        GridPane.setConstraints(lblMovieEndTime,3,1);
        lblMoviePrice = new Label();
        GridPane.setConstraints(lblMoviePrice,3,2);


        cmbMovieTitle = new ComboBox();
        for (Movie m:database.getMovies()){
            cmbMovieTitle.getItems().addAll(m.getTitle());
        }

        GridPane.setConstraints(cmbMovieTitle, 1, 0);
        cmbRoomNumber = new ComboBox();
        cmbRoomNumber.getItems().addAll("Room 1","Room 2");
        GridPane.setConstraints(cmbRoomNumber, 1, 1);
        datePickerDate = new DatePicker();
        GridPane.setConstraints(datePickerDate, 3, 0);
        txtTime = new TextField();
        txtTime.setPromptText("Time HH:mm press Enter");
        GridPane.setConstraints(txtTime, 4, 0);


        gridPane2.minHeight(400);
        gridPane2.setPadding(new Insets(20, 20, 20, 20));
        gridPane2.setHgap(20);
        gridPane2.setVgap(20);
        gridPane2.getChildren().addAll(lblMovieTitle,lblRoom,lblNoOfSeats,btnClear,btnAddShowing,lblPrice,lblEndTime,lblStartTime);
        gridPane2.getChildren().addAll(txtTime, datePickerDate,cmbRoomNumber,cmbMovieTitle,lblMoviePrice,lblMovieEndTime,lblFillNoOfSeats);

        return  gridPane2;
    }



    boolean timeIsInRange(ObservableList<Room> rooms, LocalDateTime startTime, LocalDateTime endTime){
        for (Room r: rooms){

            Duration durationStart = Duration.between(endTime, r.getStartTime()).abs();
            Duration durationEnd = Duration.between(r.getEndTime(), startTime).abs();

            if ( durationStart.toMinutes()< 16 || durationEnd.toMinutes() < 16 ||
                    startTime.isEqual(r.getStartTime()) || startTime.isEqual(r.getEndTime()) ||
                    (startTime.isAfter( r.getStartTime() )  && startTime.isBefore( r.getEndTime() ))||
                    (endTime.isAfter( r.getStartTime() )  && endTime.isBefore( r.getEndTime() ))){
                return true;
            }
        }
        return false;
    }

    public void formatTimeAndFillLabel(){
        for (Movie m:database.getMovies()){
            if (cmbMovieTitle.getValue().equals(m.getTitle())){

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime startTime = LocalDateTime.parse(datePickerDate.getValue() + " " + txtTime.getText(), formatter);
                lblMovieEndTime.setText(startTime.plusMinutes(m.getDuration()).format(formatter));
            }
        }
    }

   public  void clearFields(){
       cmbMovieTitle.setValue("");
       cmbRoomNumber.setValue("");
       lblFillNoOfSeats.setText("");
       datePickerDate.setValue(null);
       txtTime.clear();
       lblMovieEndTime.setText("");
       lblMoviePrice.setText("");
   }


}
